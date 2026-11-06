package view;

public class MenuGeneral {

    private final Validaciones v = new Validaciones();

    public int menu(String nombre) {
        return v.validarEntero("""
                               ======= PANEL ADMINISTRADOR =======
                               Usuario: %s
                               1- Gestionar Empleados.
                               2- Gestionar Clientes.
                               3- Gestionar Prestamos.
                               4- Gestionar Pagos.
                               5- Informes.
                               6- Cerrar sesion.
                               """.formatted(nombre));
    }
}
