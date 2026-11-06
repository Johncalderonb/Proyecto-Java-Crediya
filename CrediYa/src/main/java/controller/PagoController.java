package controller;

import dao.PagoDao;
import dao.PrestamoDao;
import exception.CrediYaException;
import java.time.LocalDate;
import java.util.List;
import model.EstadoPrestamo;
import model.Pago;
import model.Prestamo;
import view.Mensajes;
import view.pago.ImprimirPago;
import view.pago.IngresoPago;
import view.pago.MenuPago;
import view.prestamo.ImprimirPrestamo;

public class PagoController {

    private final MenuPago menu = new MenuPago();
    private final IngresoPago ingreso = new IngresoPago();
    private final ImprimirPago imprimir = new ImprimirPago();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final PagoDao dao = new PagoDao();
    private final PrestamoDao prestamoDao = new PrestamoDao();
    private final Mensajes mensajes = new Mensajes();

    private Prestamo pedirPrestamo() throws CrediYaException {
        Prestamo p = prestamoDao.buscar(ingreso.pedirIdPrestamo());
        if (p == null) {
            throw new CrediYaException("No existe un prestamo con ese id");
        }
        return p;
    }

    private void registrarAbono() throws CrediYaException {
        mensajes.informativo("=========== Registrar abono ===========");
        List<Prestamo> pendientes = prestamoDao.listar().stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .toList();
        if (pendientes.isEmpty()) {
            mensajes.informativo("No hay prestamos pendientes por pagar");
            return;
        }
        imprimirPrestamo.lista(pendientes);
        Prestamo prestamo = pedirPrestamo();
        double valor = ingreso.pedirMonto();
        LocalDate fecha = ingreso.pedirFecha();
        if (fecha.isBefore(prestamo.getFechaInicio())) {
            throw new CrediYaException("La fecha del pago no puede ser anterior al inicio del prestamo ("
                    + prestamo.getFechaInicio() + ")");
        }
        prestamo.registrarAbono(valor);
        Pago pago = dao.registrar(new Pago(prestamo.getId(), fecha, valor), prestamo);
        mensajes.exito("""
                       Pago #%d registrado
                       Saldo pendiente: $%s
                       Estado del prestamo: %s""".formatted(pago.getId(),
                String.format("%,.0f", prestamo.getSaldo()), prestamo.getEstado()));
    }

    private void historial() throws CrediYaException {
        mensajes.informativo("=========== Historial de pagos ===========");
        Prestamo prestamo = pedirPrestamo();
        List<Pago> pagos = dao.listarPorPrestamo(prestamo.getId());
        imprimirPrestamo.fila.accept(prestamo);
        if (pagos.isEmpty()) {
            mensajes.informativo("El prestamo no tiene pagos registrados");
            return;
        }
        imprimir.lista(pagos);
        mensajes.informativo("Pagos: %d | Total abonado: $%s | Saldo: $%s".formatted(pagos.size(),
                String.format("%,.0f", pagos.stream().mapToDouble(Pago::getMonto).sum()),
                String.format("%,.0f", prestamo.getSaldo())));
    }

    private void listarTodos() throws CrediYaException {
        List<Pago> pagos = dao.listar();
        if (pagos.isEmpty()) {
            mensajes.informativo("No hay pagos registrados");
            return;
        }
        imprimir.lista(pagos);
    }

    public void menu() {
        int op;
        do {
            op = menu.menu();
            try {
                switch (op) {
                    case 1 -> registrarAbono();
                    case 2 -> historial();
                    case 3 -> listarTodos();
                    case 4 -> mensajes.informativo("Regresando al menu principal...");
                    default -> mensajes.error("La opcion esta fuera de rango");
                }
            } catch (CrediYaException e) {
                mensajes.error(e.getMessage());
            }
        } while (op != 4);
    }
}
