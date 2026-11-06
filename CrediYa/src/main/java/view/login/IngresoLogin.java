package view.login;

import view.Validaciones;

public class IngresoLogin {

    private final Validaciones v = new Validaciones();

    public String pedirDocumento() {
        return v.validarNumerico("Documento:", 6, 15);
    }

    public String pedirClave() {
        return v.validarClave("Clave:");
    }

    public String pedirClaveNueva() {
        return v.validarClave("Ingrese la nueva clave (4 a 20 caracteres):");
    }

    public String confirmarClaveNueva() {
        return v.validarClave("Repita la nueva clave:");
    }
}
