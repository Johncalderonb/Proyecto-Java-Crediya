package view.portal;

import view.Validaciones;

public class MenuPortalCliente {

    private final Validaciones v = new Validaciones();

    public int menu(String nombre) {
        return v.validarEntero("""
                               ========= PORTAL CLIENTE =========
                               Hola, %s
                               1- Mis datos.
                               2- Mis prestamos.
                               3- Detalle e historial de pagos de un prestamo.
                               4- Realizar un abono.
                               5- Cambiar mi clave.
                               6- Cerrar sesion.
                               """.formatted(nombre));
    }
}
