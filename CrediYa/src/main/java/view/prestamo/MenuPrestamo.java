package view.prestamo;

import view.Validaciones;

public class MenuPrestamo {

    private final Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               ========= MODULO PRESTAMOS =========
                               1- Crear prestamo.
                               2- Listar prestamos.
                               3- Buscar prestamo (detalle).
                               4- Cambiar estado.
                               5- Regresar.
                               """);
    }
}
