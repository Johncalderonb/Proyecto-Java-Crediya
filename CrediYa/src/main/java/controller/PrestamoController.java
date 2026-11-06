package controller;

import dao.ClienteDao;
import dao.EmpleadoDao;
import dao.PrestamoDao;
import exception.CrediYaException;
import java.time.LocalDate;
import java.util.List;
import model.Cliente;
import model.Empleado;
import model.EstadoPrestamo;
import model.Prestamo;
import view.Mensajes;
import view.cliente.ImprimirCliente;
import view.empleado.ImprimirEmpleado;
import view.prestamo.ImprimirPrestamo;
import view.prestamo.IngresoPrestamo;
import view.prestamo.MenuPrestamo;

public class PrestamoController {

    private final MenuPrestamo menu = new MenuPrestamo();
    private final IngresoPrestamo ingreso = new IngresoPrestamo();
    private final ImprimirPrestamo imprimir = new ImprimirPrestamo();
    private final ImprimirCliente imprimirCliente = new ImprimirCliente();
    private final ImprimirEmpleado imprimirEmpleado = new ImprimirEmpleado();
    private final PrestamoDao dao = new PrestamoDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final EmpleadoDao empleadoDao = new EmpleadoDao();
    private final Mensajes mensajes = new Mensajes();

    private Cliente pedirCliente() throws CrediYaException {
        List<Cliente> clientes = clienteDao.listar();
        if (clientes.isEmpty()) {
            throw new CrediYaException("Debe registrar al menos un cliente antes de crear prestamos");
        }
        mensajes.informativo("Clientes disponibles");
        imprimirCliente.lista(clientes);
        do {
            Cliente c = clienteDao.buscar(ingreso.pedirIdCliente());
            if (c != null) {
                return c;
            }
            mensajes.error("No existe dicho cliente, intente nuevamente");
        } while (true);
    }

    private Empleado pedirEmpleado() throws CrediYaException {
        List<Empleado> empleados = empleadoDao.listar();
        if (empleados.isEmpty()) {
            throw new CrediYaException("Debe registrar al menos un empleado antes de crear prestamos");
        }
        mensajes.informativo("Empleados disponibles");
        imprimirEmpleado.lista(empleados);
        do {
            Empleado e = empleadoDao.buscar(ingreso.pedirIdEmpleado());
            if (e != null) {
                return e;
            }
            mensajes.error("No existe dicho empleado, intente nuevamente");
        } while (true);
    }

    private void crear() throws CrediYaException {
        mensajes.informativo("=========== Crear prestamo ===========");
        Cliente cliente = pedirCliente();
        Empleado empleado = pedirEmpleado();
        double monto = ingreso.pedirMonto();
        double interes = ingreso.pedirInteres();
        int cuotas = ingreso.pedirCuotas();
        LocalDate fechaInicio = ingreso.pedirFechaInicio();

        Prestamo p = new Prestamo(cliente, empleado, monto, interes, cuotas, fechaInicio);
        mensajes.informativo("Vista previa del prestamo");
        imprimir.detalle.accept(p);
        if (!ingreso.confirmar("¿Desea registrar este prestamo?")) {
            mensajes.informativo("Prestamo cancelado");
            return;
        }
        dao.agregar(p);
        mensajes.exito("Prestamo #" + p.getId() + " registrado. Cuota mensual: $"
                + String.format("%,.0f", p.getValorCuota()));
    }

    private boolean listar() throws CrediYaException {
        List<Prestamo> prestamos = dao.listar();
        if (prestamos.isEmpty()) {
            mensajes.informativo("No hay prestamos registrados");
            return false;
        }
        imprimir.lista(prestamos);
        return true;
    }

    private Prestamo buscarExistente() throws CrediYaException {
        Prestamo p = dao.buscar(ingreso.pedirId());
        if (p == null) {
            throw new CrediYaException("No existe un prestamo con ese id");
        }
        return p;
    }

    private void buscar() throws CrediYaException {
        mensajes.informativo("=========== Detalle de prestamo ===========");
        Prestamo p = buscarExistente();
        imprimir.detalle.accept(p);
        if (p.estaAtrasado()) {
            mensajes.error("Prestamo con atraso. Valor en mora: $" + String.format("%,.0f", p.valorEnMora()));
        }
    }

    private void cambiarEstado() throws CrediYaException {
        mensajes.informativo("=========== Cambiar estado ===========");
        if (!listar()) {
            return;
        }
        Prestamo p = buscarExistente();
        EstadoPrestamo nuevo = ingreso.pedirEstado();
        if (nuevo == EstadoPrestamo.PAGADO && p.getSaldo() > 0) {
            String aviso = "El prestamo aun tiene saldo de $%s. ¿Marcarlo como PAGADO y dejar el saldo en 0?"
                    .formatted(String.format("%,.0f", p.getSaldo()));
            if (!ingreso.confirmar(aviso)) {
                mensajes.informativo("Cambio cancelado");
                return;
            }
        }
        p.cambiarEstado(nuevo);
        dao.actualizarEstadoYSaldo(p);
        mensajes.exito("Prestamo #" + p.getId() + " ahora esta " + p.getEstado());
    }

    public void menu() {
        int op;
        do {
            op = menu.menu();
            try {
                switch (op) {
                    case 1 -> crear();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> cambiarEstado();
                    case 5 -> mensajes.informativo("Regresando al menu principal...");
                    default -> mensajes.error("La opcion esta fuera de rango");
                }
            } catch (CrediYaException e) {
                mensajes.error(e.getMessage());
            }
        } while (op != 5);
    }
}
