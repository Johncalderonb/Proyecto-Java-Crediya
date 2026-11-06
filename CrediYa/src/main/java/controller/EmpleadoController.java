package controller;

import dao.EmpleadoDao;
import exception.CrediYaException;
import java.util.List;
import model.Empleado;
import model.RolEmpleado;
import seguridad.Encriptador;
import seguridad.Sesion;
import view.Mensajes;
import view.empleado.ImprimirEmpleado;
import view.empleado.IngresoEmpleado;
import view.empleado.MenuEmpleado;

public class EmpleadoController {

    private final MenuEmpleado menu = new MenuEmpleado();
    private final IngresoEmpleado ingreso = new IngresoEmpleado();
    private final ImprimirEmpleado imprimir = new ImprimirEmpleado();
    private final EmpleadoDao dao = new EmpleadoDao();
    private final Mensajes mensajes = new Mensajes();
    private final Sesion sesion = Sesion.getInstance();

    private void registrar() throws CrediYaException {
        mensajes.informativo("=========== Registrar empleado ===========");
        String nombre = ingreso.pedirNombre();
        String documento = ingreso.pedirDocumento();
        if (dao.buscarPorDocumento(documento) != null) {
            throw new CrediYaException("Ya existe un empleado con el documento " + documento);
        }
        Empleado e = new Empleado(nombre, documento, ingreso.pedirRol(), ingreso.pedirCorreo(),
                ingreso.pedirSalario(), Encriptador.encriptar(ingreso.pedirClave()));
        dao.agregar(e);
        mensajes.exito("Empleado registrado con id " + e.getId());
    }

    private boolean listar() throws CrediYaException {
        List<Empleado> empleados = dao.listar();
        if (empleados.isEmpty()) {
            mensajes.informativo("No hay empleados registrados");
            return false;
        }
        imprimir.lista(empleados);
        return true;
    }

    private Empleado buscarExistente() throws CrediYaException {
        Empleado e = dao.buscar(ingreso.pedirId());
        if (e == null) {
            throw new CrediYaException("No existe un empleado con ese id");
        }
        return e;
    }

    private void buscar() throws CrediYaException {
        mensajes.informativo("=========== Buscar empleado ===========");
        imprimir.entidad.accept(buscarExistente());
    }

    private void actualizar() throws CrediYaException {
        mensajes.informativo("=========== Actualizar empleado ===========");
        if (!listar()) {
            return;
        }
        Empleado e = buscarExistente();
        switch (menu.opcionesActualizar()) {
            case 1 -> e.setNombre(ingreso.pedirNombre());
            case 2 -> {
                RolEmpleado nuevoRol = ingreso.pedirRol();
                if (sesion.esUsuarioActual(e) && nuevoRol != RolEmpleado.ADMINISTRADOR) {
                    throw new CrediYaException("No puede quitarse a si mismo el rol de ADMINISTRADOR");
                }
                e.setRol(nuevoRol);
            }
            case 3 -> e.setCorreo(ingreso.pedirCorreo());
            case 4 -> e.setSalario(ingreso.pedirSalario());
            case 5 -> e.setClave(Encriptador.encriptar(ingreso.pedirClave()));
            case 6 -> {
                mensajes.informativo("Operacion cancelada");
                return;
            }
            default -> throw new CrediYaException("Opcion fuera de rango");
        }
        if (dao.actualizar(e)) {
            mensajes.exito("Empleado actualizado correctamente");
        }
    }

    private void eliminar() throws CrediYaException {
        mensajes.informativo("=========== Eliminar empleado ===========");
        if (!listar()) {
            return;
        }
        Empleado e = buscarExistente();
        if (sesion.esUsuarioActual(e)) {
            throw new CrediYaException("No puede eliminar su propio usuario mientras tiene la sesion abierta");
        }
        if (ingreso.confirmar("¿Esta seguro de eliminar a " + e.getNombre() + "?")) {
            dao.eliminar(e);
            mensajes.exito("Empleado eliminado correctamente");
        } else {
            mensajes.informativo("Eliminacion cancelada");
        }
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
                    case 6 -> mensajes.informativo("Regresando al menu principal...");
                    default -> mensajes.error("La opcion esta fuera de rango");
                }
            } catch (CrediYaException e) {
                mensajes.error(e.getMessage());
            }
        } while (op != 6);
    }
}
