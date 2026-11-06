package controller;

import dao.ClienteDao;
import dao.PagoDao;
import dao.PrestamoDao;
import exception.CrediYaException;
import java.time.LocalDate;
import java.util.List;
import model.Cliente;
import model.EstadoPrestamo;
import model.Pago;
import model.Prestamo;
import seguridad.Encriptador;
import view.Mensajes;
import view.cliente.ImprimirCliente;
import view.login.IngresoLogin;
import view.pago.ImprimirPago;
import view.pago.IngresoPago;
import view.portal.MenuPortalCliente;
import view.prestamo.ImprimirPrestamo;

public class PortalClienteController {

    private final MenuPortalCliente menu = new MenuPortalCliente();
    private final ImprimirCliente imprimirCliente = new ImprimirCliente();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final ImprimirPago imprimirPago = new ImprimirPago();
    private final IngresoPago ingresoPago = new IngresoPago();
    private final IngresoLogin ingresoLogin = new IngresoLogin();
    private final ClienteDao clienteDao = new ClienteDao();
    private final PrestamoDao prestamoDao = new PrestamoDao();
    private final PagoDao pagoDao = new PagoDao();
    private final Mensajes mensajes = new Mensajes();

    private Cliente cliente;

    private List<Prestamo> misPrestamos() throws CrediYaException {
        return prestamoDao.listar().stream()
                .filter(p -> p.getCliente().getId() == cliente.getId())
                .toList();
    }

    private Prestamo pedirMiPrestamo() throws CrediYaException {
        Prestamo p = prestamoDao.buscar(ingresoPago.pedirIdPrestamo());
        if (p == null || p.getCliente().getId() != cliente.getId()) {
            throw new CrediYaException("No tiene un prestamo con ese id");
        }
        return p;
    }

    private void misDatos() {
        imprimirCliente.entidad.accept(cliente);
    }

    private boolean verMisPrestamos() throws CrediYaException {
        List<Prestamo> prestamos = misPrestamos();
        if (prestamos.isEmpty()) {
            mensajes.informativo("Aun no tiene prestamos registrados");
            return false;
        }
        imprimirPrestamo.lista(prestamos);
        double deuda = prestamos.stream().mapToDouble(Prestamo::getSaldo).sum();
        double mora = prestamos.stream().mapToDouble(Prestamo::valorEnMora).sum();
        mensajes.informativo("Deuda total: $%s | Valor en mora: $%s".formatted(
                String.format("%,.0f", deuda), String.format("%,.0f", mora)));
        return true;
    }

    private void detalleYPagos() throws CrediYaException {
        if (!verMisPrestamos()) {
            return;
        }
        Prestamo p = pedirMiPrestamo();
        imprimirPrestamo.detalle.accept(p);
        if (p.estaAtrasado()) {
            mensajes.error("Tiene cuotas atrasadas. Valor en mora: $" + String.format("%,.0f", p.valorEnMora()));
        }
        List<Pago> pagos = pagoDao.listarPorPrestamo(p.getId());
        if (pagos.isEmpty()) {
            mensajes.informativo("Este prestamo no tiene pagos registrados");
        } else {
            mensajes.informativo("Historial de pagos");
            imprimirPago.lista(pagos);
        }
    }

    private void realizarAbono() throws CrediYaException {
        List<Prestamo> pendientes = misPrestamos().stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .toList();
        if (pendientes.isEmpty()) {
            mensajes.informativo("No tiene prestamos pendientes por pagar");
            return;
        }
        imprimirPrestamo.lista(pendientes);
        Prestamo prestamo = pedirMiPrestamo();
        double valor = ingresoPago.pedirMonto();
        prestamo.registrarAbono(valor);
        Pago pago = pagoDao.registrar(new Pago(prestamo.getId(), LocalDate.now(), valor), prestamo);
        mensajes.exito("""
                       Abono #%d registrado
                       Saldo pendiente: $%s
                       Estado del prestamo: %s""".formatted(pago.getId(),
                String.format("%,.0f", prestamo.getSaldo()), prestamo.getEstado()));
    }

    private void cambiarClave() throws CrediYaException {
        if (!cliente.verificarClave(ingresoLogin.pedirClave())) {
            throw new CrediYaException("La clave actual no es correcta");
        }
        String nueva = ingresoLogin.pedirClaveNueva();
        if (!nueva.equals(ingresoLogin.confirmarClaveNueva())) {
            throw new CrediYaException("Las claves no coinciden");
        }
        cliente.setClave(Encriptador.encriptar(nueva));
        clienteDao.actualizar(cliente);
        mensajes.exito("Clave actualizada correctamente");
    }

    public void menu(Cliente clienteEnSesion) {
        cliente = clienteEnSesion;
        int op;
        do {
            op = menu.menu(cliente.getNombre());
            try {
                switch (op) {
                    case 1 -> misDatos();
                    case 2 -> verMisPrestamos();
                    case 3 -> detalleYPagos();
                    case 4 -> realizarAbono();
                    case 5 -> cambiarClave();
                    case 6 -> mensajes.informativo("Sesion de cliente cerrada");
                    default -> mensajes.error("La opcion esta fuera de rango");
                }
            } catch (CrediYaException e) {
                mensajes.error(e.getMessage());
            }
        } while (op != 6);
    }
}
