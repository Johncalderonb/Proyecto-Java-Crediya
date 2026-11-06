package view.cliente;

import view.Validaciones;

public class IngresoCliente {

    private final Validaciones v = new Validaciones();

    public String pedirNombre() {
        return v.validarTexto("Ingrese el nombre del cliente:");
    }

    public String pedirDocumento() {
        return v.validarNumerico("Ingrese el documento del cliente:", 6, 15);
    }

    public String pedirCorreo() {
        return v.validarCorreo("Ingrese el correo del cliente:");
    }

    public String pedirTelefono() {
        return v.validarNumerico("Ingrese el telefono del cliente:", 7, 10);
    }

    public String pedirClave() {
        return v.validarClave("Asigne una clave de acceso para el cliente (4 a 20 caracteres):");
    }

    public long pedirId() {
        return v.validarEnteroGrande("Ingrese el id del cliente:");
    }

    public boolean confirmar(String mensaje) {
        return v.confirmar(mensaje);
    }
}
