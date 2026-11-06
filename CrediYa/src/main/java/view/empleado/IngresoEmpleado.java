package view.empleado;

import model.RolEmpleado;
import view.Validaciones;

public class IngresoEmpleado {

    private final Validaciones v = new Validaciones();

    public String pedirNombre() {
        return v.validarTexto("Ingrese el nombre del empleado:");
    }

    public String pedirDocumento() {
        return v.validarNumerico("Ingrese el documento del empleado:", 6, 15);
    }

    public RolEmpleado pedirRol() {
        String opciones = "Seleccione el rol del empleado:\n";
        RolEmpleado[] roles = RolEmpleado.values();
        for (int i = 0; i < roles.length; i++) {
            opciones += (i + 1) + "- " + roles[i] + "\n";
        }
        int op = v.validarRango(opciones, 1, roles.length);
        return roles[op - 1];
    }

    public String pedirCorreo() {
        return v.validarCorreo("Ingrese el correo del empleado:");
    }

    public double pedirSalario() {
        return v.validarDecimalRango("Ingrese el salario del empleado:", 1, 100000000);
    }

    public String pedirClave() {
        return v.validarClave("Asigne una clave de acceso para el empleado (4 a 20 caracteres):");
    }

    public long pedirId() {
        return v.validarEnteroGrande("Ingrese el id del empleado:");
    }

    public boolean confirmar(String mensaje) {
        return v.confirmar(mensaje);
    }
}
