package view.pago;

import java.time.LocalDate;
import view.Validaciones;

public class IngresoPago {

    private final Validaciones v = new Validaciones();

    public long pedirIdPrestamo() {
        return v.validarEnteroGrande("Ingrese el id del prestamo:");
    }

    public double pedirMonto() {
        return v.validarDecimal("Ingrese el valor del abono:");
    }

    public LocalDate pedirFecha() {
        return v.validarFecha("Ingrese la fecha del pago");
    }
}
