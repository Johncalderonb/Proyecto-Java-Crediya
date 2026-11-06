package controller;

import dao.ClienteDao;
import dao.PrestamoDao;
import exception.CrediYaException;
import java.util.List;
import model.Cliente;
import model.Prestamo;
import seguridad.Encriptador;
import view.Mensajes;
import view.cliente.ImprimirCliente;
import view.cliente.IngresoCliente;
import view.cliente.MenuCliente;
import view.prestamo.ImprimirPrestamo;

public class ClienteController {

    private final MenuCliente menu = new MenuCliente();
    private final IngresoCliente ingreso = new IngresoCliente();
    private final ImprimirCliente imprimir = new ImprimirCliente();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final ClienteDao dao = new ClienteDao();
    private final PrestamoDao prestamoDao = new PrestamoDao();
    private final Mensajes mensajes = new Mensajes();

    private void registrar() throws CrediYaException {
        mensajes.informativo("=========== Registrar cliente ===========");
        String nombre = ingreso.pedirNombre();
        String documento = ingreso.pedirDocumento();
        if (dao.buscarPorDocumento(documento) != null) {
            throw new CrediYaException("Ya existe un cliente con el documento " + documento);
        }
        Cliente c = new Cliente(nombre, documento, ingreso.pedirCorreo(), ingreso.pedirTelefono(),
                Encriptador.encriptar(ingreso.pedirClave()));
        dao.agregar(c);
        mensajes.exito("Cliente registrado con id " + c.getId()
                + "\nYa puede ingresar al portal con su documento y la clave asignada");
    }

    private boolean listar() throws CrediYaException {
        List<Cliente> clientes = dao.listar();
        if (clientes.isEmpty()) {
            mensajes.informativo("No hay clientes registrados");
            return false;
        }
        imprimir.lista(clientes);
        return true;
    }

    private Cliente buscarExistente() throws CrediYaException {
        Cliente c = dao.buscar(ingreso.pedirId());
        if (c == null) {
            throw new CrediYaException("No existe un cliente con ese id");
        }
        return c;
    }

    private void buscar() throws CrediYaException {
        mensajes.informativo("=========== Buscar cliente ===========");
        imprimir.entidad.accept(buscarExistente());
    }

    private void actualizar() throws CrediYaException {
        mensajes.informativo("=========== Actualizar cliente ===========");
        if (!listar()) {
            return;
        }
        Cliente c = buscarExistente();
        switch (menu.opcionesActualizar()) {
            case 1 -> c.setNombre(ingreso.pedirNombre());
            case 2 -> c.setCorreo(ingreso.pedirCorreo());
            case 3 -> c.setTelefono(ingreso.pedirTelefono());
            case 4 -> c.setClave(Encriptador.encriptar(ingreso.pedirClave()));
            case 5 -> {
                mensajes.informativo("Operacion cancelada");
                return;
            }
            default -> throw new CrediYaException("Opcion fuera de rango");
        }
        if (dao.actualizar(c)) {
            mensajes.exito("Cliente actualizado correctamente");
        }
    }

    private void eliminar() throws CrediYaException {
        mensajes.informativo("=========== Eliminar cliente ===========");
        if (!listar()) {
            return;
        }
        Cliente c = buscarExistente();
        if (ingreso.confirmar("¿Esta seguro de eliminar a " + c.getNombre() + "?")) {
            dao.eliminar(c);
            mensajes.exito("Cliente eliminado correctamente");
        } else {
            mensajes.informativo("Eliminacion cancelada");
        }
    }

    private void consultarPrestamos() throws CrediYaException {
        mensajes.informativo("=========== Prestamos del cliente ===========");
        if (!listar()) {
            return;
        }
        Cliente c = buscarExistente();
        List<Prestamo> prestamos = prestamoDao.listar().stream()
                .filter(p -> p.getCliente().getId() == c.getId())
                .toList();
        if (prestamos.isEmpty()) {
            mensajes.informativo(c.getNombre() + " no tiene prestamos registrados");
            return;
        }
        imprimirPrestamo.lista(prestamos);
        double deudaTotal = prestamos.stream().mapToDouble(Prestamo::getSaldo).sum();
        mensajes.informativo("Cliente: %s | Prestamos: %d | Deuda total: $%s"
                .formatted(c.getNombre(), prestamos.size(), String.format("%,.0f", deudaTotal)));
    }

    public void menu() {
        int op;
        do {
            op = menu.menu();
            try {
                switch (op) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> actualizar();
                    case 5 -> eliminar();
                    case 6 -> consultarPrestamos();
                    case 7 -> mensajes.informativo("Regresando al menu principal...");
                    default -> mensajes.error("La opcion esta fuera de rango");
                }
            } catch (CrediYaException e) {
                mensajes.error(e.getMessage());
            }
        } while (op != 7);
    }
}
