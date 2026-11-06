package view.pago;

import view.Validaciones;

public class MenuPago {

    private final Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               ========= MODULO PAGOS =========
                               1- Registrar abono a un prestamo.
                               2- Historial de pagos de un prestamo.
                               3- Listar todos los pagos.
                               4- Regresar.
                               """);
    }
}
