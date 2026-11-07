package controller;

import archivo.ArchivoReportes;
import archivo.ExportadorArchivos;
import dao.ClienteDao;
import dao.EmpleadoDao;
import dao.PrestamoDao;
import exception.CrediYaException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import model.Empleado;
import model.EstadoPrestamo;
import model.Persona;
import model.Prestamo;
import model.Reporte;
import view.Mensajes;
import view.informe.CuadroIndicadores;
import view.informe.ImprimirInforme;
import view.informe.MenuInforme;

public class InformeController {

    private final MenuInforme menu = new MenuInforme();
    private final ImprimirInforme imprimir = new ImprimirInforme();
    private final PrestamoDao prestamoDao = new PrestamoDao();
    private final EmpleadoDao empleadoDao = new EmpleadoDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final ArchivoReportes archivo = new ArchivoReportes();
    private final ExportadorArchivos exportador = new ExportadorArchivos();
    private final Mensajes mensajes = new Mensajes();

    public Predicate<Prestamo> activo = p -> p.getEstado() == EstadoPrestamo.PENDIENTE;
    public Predicate<Prestamo> vencido = p -> p.estaVencido();
    public Predicate<Prestamo> atrasado = p -> p.estaAtrasado();

    public Function<Prestamo, String> aFilaPrestamo = p -> "#%-4d %-18s | Inicio: %s | Fin: %s | Saldo: $%s"
            .formatted(p.getId(), p.getCliente().getNombre(), p.getFechaInicio(), p.getFechaFin(), dinero(p.getSaldo()));

    private String dinero(double valor) {
        return String.format("%,.0f", valor);
    }

    private String indicadores() throws CrediYaException {
        List<Prestamo> prestamos = prestamoDao.listar();
        long activos = prestamos.stream().filter(activo).count();
        double cartera = prestamos.stream().filter(activo).mapToDouble(Prestamo::getSaldo).sum();
        long clientesEnMora = prestamos.stream()
                .filter(atrasado)
                .map(p -> p.getCliente().getId())
                .distinct()
                .count();
        long vencidos = prestamos.stream().filter(vencido).count();
        double recaudado = prestamos.stream().mapToDouble(Prestamo::getTotalPagado).sum();
        long pagados = prestamos.stream().filter(activo.negate()).count();
        return new CuadroIndicadores()
                .agregar("Prestamos activos", String.valueOf(activos))
                .agregar("Cartera pendiente", "$" + dinero(cartera))
                .agregar("Clientes en mora", String.valueOf(clientesEnMora))
                .agregar("Prestamos vencidos", String.valueOf(vencidos))
                .agregar("Total recaudado", "$" + dinero(recaudado))
                .agregar("Prestamos pagados", String.valueOf(pagados))
                .construir();
    }

    private void presentar(Reporte reporte) throws CrediYaException {
        reporte.setIndicadores(indicadores());
        imprimir.mostrar.accept(reporte);
        if (menu.deseaGuardar()) {
            archivo.agregar(reporte);
            mensajes.exito("Reporte guardado como #" + reporte.getId() + "\nArchivo: " + archivo.getRuta());
        }
    }

    private void prestamosActivos() throws CrediYaException {
        List<Prestamo> activos = prestamoDao.listar().stream()
                .filter(activo)
                .sorted(Comparator.comparing(Prestamo::getFechaInicio))
                .toList();
        Reporte reporte = new Reporte("Prestamos activos");
        activos.stream().map(aFilaPrestamo).forEach(reporte::agregarLinea);
        double cartera = activos.stream().mapToDouble(Prestamo::getSaldo).sum();
        reporte.setResumen("Prestamos activos: %d | Cartera pendiente: $%s".formatted(activos.size(), dinero(cartera)));
        presentar(reporte);
    }

    private void prestamosVencidos() throws CrediYaException {
        List<Prestamo> vencidos = prestamoDao.listar().stream()
                .filter(vencido)
                .sorted(Comparator.comparing(Prestamo::getFechaFin))
                .toList();
        Reporte reporte = new Reporte("Prestamos vencidos");
        vencidos.stream().map(aFilaPrestamo).forEach(reporte::agregarLinea);
        double totalVencido = vencidos.stream().mapToDouble(Prestamo::getSaldo).sum();
        reporte.setResumen("Prestamos con plazo terminado y saldo: %d | Total vencido: $%s"
                .formatted(vencidos.size(), dinero(totalVencido)));
        presentar(reporte);
    }

    private void clientesMorosos() throws CrediYaException {
        Map<String, List<Prestamo>> porCliente = prestamoDao.listar().stream()
                .filter(atrasado)
                .collect(Collectors.groupingBy(p -> p.getCliente().getNombre() + " (Doc " + p.getCliente().getDocumento() + ")"));
        Reporte reporte = new Reporte("Clientes morosos");
        double moraTotal = 0;
        for (Map.Entry<String, List<Prestamo>> entry : porCliente.entrySet()) {
            double moraCliente = entry.getValue().stream().mapToDouble(Prestamo::valorEnMora).sum();
            moraTotal += moraCliente;
            reporte.agregarLinea("%-35s | Prestamos atrasados: %d | En mora: $%s"
                    .formatted(entry.getKey(), entry.getValue().size(), dinero(moraCliente)));
        }
        reporte.setResumen("Clientes morosos: %d | Valor total en mora: $%s".formatted(porCliente.size(), dinero(moraTotal)));
        presentar(reporte);
    }

    private void resumenPorEstado() throws CrediYaException {
        List<Prestamo> prestamos = prestamoDao.listar();
        Map<EstadoPrestamo, Long> cantidad = prestamos.stream()
                .collect(Collectors.groupingBy(Prestamo::getEstado, Collectors.counting()));
        Map<EstadoPrestamo, Double> prestado = prestamos.stream()
                .collect(Collectors.groupingBy(Prestamo::getEstado, Collectors.summingDouble(Prestamo::getMonto)));
        Map<EstadoPrestamo, Double> saldo = prestamos.stream()
                .collect(Collectors.groupingBy(Prestamo::getEstado, Collectors.summingDouble(Prestamo::getSaldo)));
        Reporte reporte = new Reporte("Resumen de cartera por estado");
        for (EstadoPrestamo estado : EstadoPrestamo.values()) {
            reporte.agregarLinea("%-10s | Cantidad: %3d | Prestado: $%15s | Saldo: $%15s".formatted(estado,
                    cantidad.getOrDefault(estado, 0L), dinero(prestado.getOrDefault(estado, 0.0)),
                    dinero(saldo.getOrDefault(estado, 0.0))));
        }
        double recaudado = prestamos.stream().mapToDouble(Prestamo::getTotalPagado).sum();
        reporte.setResumen("Total prestamos: %d | Total recaudado: $%s".formatted(prestamos.size(), dinero(recaudado)));
        presentar(reporte);
    }

    private void gestionPorEmpleado() throws CrediYaException {
        List<Prestamo> prestamos = prestamoDao.listar();
        List<Empleado> empleados = empleadoDao.listar();
        Map<Long, List<Prestamo>> porEmpleado = prestamos.stream()
                .collect(Collectors.groupingBy(p -> p.getEmpleado().getId()));
        Reporte reporte = new Reporte("Gestion por empleado");
        String mejor = "Sin prestamos";
        double mayorColocado = 0;
        for (Empleado e : empleados) {
            List<Prestamo> suyos = porEmpleado.getOrDefault(e.getId(), List.of());
            double colocado = suyos.stream().mapToDouble(Prestamo::getMonto).sum();
            double recaudado = suyos.stream().mapToDouble(Prestamo::getTotalPagado).sum();
            long morosos = suyos.stream().filter(atrasado).count();
            reporte.agregarLinea("%-18s %-15s | Prestamos: %2d | Colocado: $%13s | Recaudado: $%13s | Atrasados: %d"
                    .formatted(e.getNombre(), "(" + e.getRol() + ")", suyos.size(),
                            dinero(colocado), dinero(recaudado), morosos));
            if (colocado > mayorColocado) {
                mayorColocado = colocado;
                mejor = e.getNombre();
            }
        }
        reporte.setResumen("Empleados: %d | Mayor colocacion: %s ($%s)"
                .formatted(empleados.size(), mejor, dinero(mayorColocado)));
        presentar(reporte);
    }

    private void topDeudores() throws CrediYaException {
        Map<String, Double> deudaPorCliente = prestamoDao.listar().stream()
                .filter(activo)
                .collect(Collectors.groupingBy(p -> p.getCliente().getNombre(), Collectors.summingDouble(Prestamo::getSaldo)));
        Reporte reporte = new Reporte("Top 5 clientes con mayor deuda");
        deudaPorCliente.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> reporte.agregarLinea("%-20s | Deuda: $%s".formatted(entry.getKey(), dinero(entry.getValue()))));
        reporte.setResumen("Clientes con deuda activa: %d".formatted(deudaPorCliente.size()));
        presentar(reporte);
    }

    private void directorioGeneral() throws CrediYaException {
        List<Persona> personas = new ArrayList<>();
        personas.addAll(empleadoDao.listar());
        personas.addAll(clienteDao.listar());
        Reporte reporte = new Reporte("Directorio general");
        personas.stream()
                .sorted(Comparator.comparing(Persona::getNombre))
                .map(Persona::resumen)
                .forEach(reporte::agregarLinea);
        Map<String, Long> porTipo = personas.stream()
                .collect(Collectors.groupingBy(Persona::tipoPersona, Collectors.counting()));
        reporte.setResumen("Total personas: %d | %s".formatted(personas.size(), porTipo));
        presentar(reporte);
    }

    private void verGuardados() {
        List<Reporte> reportes = archivo.listar();
        if (reportes.isEmpty()) {
            mensajes.informativo("Aun no hay reportes guardados en el archivo");
            return;
        }
        imprimir.indice(reportes);
        int id = menu.pedirIdReporte();
        if (id == 0) {
            return;
        }
        Reporte r = archivo.buscar(id);
        if (r == null) {
            mensajes.error("No existe un reporte con ese numero");
        } else {
            imprimir.mostrar.accept(r);
        }
    }

    private void exportarTxt() throws CrediYaException {
        List<Reporte> reportes = archivo.listar();
        if (reportes.isEmpty()) {
            mensajes.informativo("Primero guarde un reporte (opciones 1 a 7) para poder exportarlo");
            return;
        }
        imprimir.indice(reportes);
        int id = menu.pedirIdExportar();
        if (id == 0) {
            return;
        }
        Reporte r = archivo.buscar(id);
        if (r == null) {
            throw new CrediYaException("No existe un reporte con ese numero");
        }
        String ruta = exportador.exportarTxt(r);
        mensajes.exito("Reporte exportado. Abralo con el Bloc de notas:\n" + ruta);
    }

    private void exportarCsv() throws CrediYaException {
        List<Prestamo> prestamos = prestamoDao.listar();
        if (prestamos.isEmpty()) {
            mensajes.informativo("No hay prestamos para exportar");
            return;
        }
        String ruta = exportador.exportarCsv(prestamos);
        mensajes.exito("Se exportaron " + prestamos.size() + " prestamos. Abra el archivo con Excel:\n" + ruta);
    }

    public void menu() {
        int op;
        do {
            op = menu.menu();
            try {
                switch (op) {
                    case 1 -> prestamosActivos();
                    case 2 -> prestamosVencidos();
                    case 3 -> clientesMorosos();
                    case 4 -> resumenPorEstado();
                    case 5 -> gestionPorEmpleado();
                    case 6 -> topDeudores();
                    case 7 -> directorioGeneral();
                    case 8 -> verGuardados();
                    case 9 -> exportarTxt();
                    case 10 -> exportarCsv();
                    case 11 -> mensajes.informativo("Regresando al menu principal...");
                    default -> mensajes.error("La opcion esta fuera de rango");
                }
            } catch (CrediYaException e) {
                mensajes.error(e.getMessage());
            }
        } while (op != 11);
    }
}
