package controller;

import seguridad.Sesion;
import view.Mensajes;
import view.MenuGeneral;

public class ControllerGeneral {

    private final EmpleadoController empleado = new EmpleadoController();
    private final ClienteController cliente = new ClienteController();
    private final PrestamoController prestamo = new PrestamoController();
    private final PagoController pago = new PagoController();
    private final InformeController informe = new InformeController();
    private final MenuGeneral menu = new MenuGeneral();
    private final Mensajes mensajes = new Mensajes();

    public void gestionarMenu() {
        int op;
        do {
            op = menu.menu(Sesion.getInstance().getUsuario().getNombre());
            switch (op) {
                case 1 -> empleado.menu();
                case 2 -> cliente.menu();
                case 3 -> prestamo.menu();
                case 4 -> pago.menu();
                case 5 -> informe.menu();
                case 6 -> mensajes.informativo("Sesion de administrador cerrada");
                default -> mensajes.error("Opcion no encontrada!");
            }
        } while (op != 6);
    }
}
