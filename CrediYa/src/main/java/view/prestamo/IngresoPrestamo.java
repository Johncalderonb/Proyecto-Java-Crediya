package view.prestamo;

import java.time.LocalDate;
import model.EstadoPrestamo;
import view.Validaciones;

public class IngresoPrestamo {

    private final Validaciones v = new Validaciones();

    public double pedirMonto() {
        return v.validarDecimalRango("Ingrese el monto a prestar:", 50000, 500000000);
    }

    public double pedirInteres() {
        return v.validarDecimalRango("Ingrese el porcentaje de interes total (ej: 10 = 10%):", 0, 100);
    }

    public int pedirCuotas() {
        return v.validarRango("Ingrese el numero de cuotas mensuales (1 a 120):", 1, 120);
    }

    public LocalDate pedirFechaInicio() {
        return v.validarFecha("Ingrese la fecha de inicio del prestamo");
    }

    public long pedirId() {
        return v.validarEnteroGrande("Ingrese el id del prestamo:");
    }

    public long pedirIdCliente() {
        return v.validarEnteroGrande("Ingrese el id del cliente:");
    }

    public long pedirIdEmpleado() {
        return v.validarEnteroGrande("Ingrese el id del empleado que otorga el prestamo:");
    }

    public EstadoPrestamo pedirEstado() {
        String opciones = "Seleccione el nuevo estado:\n";
        EstadoPrestamo[] estados = EstadoPrestamo.values();
        for (int i = 0; i < estados.length; i++) {
            opciones += (i + 1) + "- " + estados[i] + "\n";
        }
        int op = v.validarRango(opciones, 1, estados.length);
        return estados[op - 1];
    }

    public boolean confirmar(String mensaje) {
        return v.confirmar(mensaje);
    }
}
