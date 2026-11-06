package controller;

import dao.ClienteDao;
import dao.EmpleadoDao;
import exception.CrediYaException;
import model.Cliente;
import model.Empleado;
import model.RolEmpleado;
import seguridad.Encriptador;
import seguridad.Sesion;
import view.Mensajes;
import view.empleado.IngresoEmpleado;
import view.login.IngresoLogin;
import view.login.MenuLogin;

public class LoginController {

    private final int MAX_INTENTOS = 3;

    private final MenuLogin menu = new MenuLogin();
    private final IngresoLogin ingreso = new IngresoLogin();
    private final IngresoEmpleado ingresoEmpleado = new IngresoEmpleado();
    private final EmpleadoDao empleadoDao = new EmpleadoDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final Mensajes mensajes = new Mensajes();
    private final Sesion sesion = Sesion.getInstance();
    private final ControllerGeneral panelAdministrador = new ControllerGeneral();
    private final PortalClienteController portalCliente = new PortalClienteController();

    private boolean existeAdministrador() throws CrediYaException {
        return empleadoDao.listar().stream()
                .anyMatch(e -> e.getRol() == RolEmpleado.ADMINISTRADOR);
    }

    private void registrarPrimerAdministrador() throws CrediYaException {
        mensajes.informativo("""
                             ========= CONFIGURACION INICIAL =========
                             No existe ningun administrador en el sistema.
                             Registre el primer administrador para continuar.""");
        String nombre = ingresoEmpleado.pedirNombre();
        String documento = ingresoEmpleado.pedirDocumento();
        Empleado admin = new Empleado(nombre, documento, RolEmpleado.ADMINISTRADOR, ingresoEmpleado.pedirCorreo(),
                ingresoEmpleado.pedirSalario(), Encriptador.encriptar(ingresoEmpleado.pedirClave()));
        empleadoDao.agregar(admin);
        mensajes.exito("Administrador creado. Ya puede ingresar con su documento y clave.");
    }

    private void ingresarAdministrador() throws CrediYaException {
        if (!existeAdministrador()) {
            registrarPrimerAdministrador();
            return;
        }
        mensajes.informativo("=========== Ingreso administrador ===========");
        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            Empleado e = empleadoDao.buscarPorDocumento(ingreso.pedirDocumento());
            String clave = ingreso.pedirClave();
            if (e != null && e.getRol() == RolEmpleado.ADMINISTRADOR && e.verificarClave(clave)) {
                sesion.iniciar(e);
                mensajes.exito("Bienvenido(a) " + e.getNombre());
                panelAdministrador.gestionarMenu();
                sesion.cerrar();
                return;
            }
            mensajes.error("Documento o clave incorrectos. Intentos restantes: " + (MAX_INTENTOS - intento));
        }
        mensajes.error("Demasiados intentos fallidos. Regresando al inicio.");
    }

    private void ingresarCliente() throws CrediYaException {
        mensajes.informativo("=========== Ingreso cliente ===========");
        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            Cliente c = clienteDao.buscarPorDocumento(ingreso.pedirDocumento());
            String clave = ingreso.pedirClave();
            if (c != null && c.verificarClave(clave)) {
                sesion.iniciar(c);
                mensajes.exito("Bienvenido(a) " + c.getNombre());
                portalCliente.menu(c);
                sesion.cerrar();
                return;
            }
            mensajes.error("Documento o clave incorrectos. Intentos restantes: " + (MAX_INTENTOS - intento));
        }
        mensajes.error("Demasiados intentos fallidos. Regresando al inicio.");
    }

    public void iniciar() {
        int op;
        do {
            op = menu.menu();
            try {
                switch (op) {
                    case 1 -> ingresarAdministrador();
                    case 2 -> ingresarCliente();
                    case 3 -> mensajes.informativo("Gracias por usar CrediYa SAS!");
                    default -> mensajes.error("Opcion no encontrada!");
                }
            } catch (CrediYaException e) {
                sesion.cerrar();
                mensajes.error(e.getMessage());
            }
        } while (op != 3);
    }
}
