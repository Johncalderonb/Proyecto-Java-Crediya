package model;

public class Cliente extends Persona {

    private String telefono;

    public Cliente(long id, String nombre, String documento, String correo, String telefono, String clave) {
        super(id, nombre, documento, correo, clave);
        this.telefono = telefono;
    }

    public Cliente(String nombre, String documento, String correo, String telefono, String clave) {
        super(nombre, documento, correo, clave);
        this.telefono = telefono;
    }

    @Override
    public String tipoPersona() {
        return "CLIENTE";
    }

    @Override
    public String resumen() {
        return "%-9s | %-20s | Doc: %-12s | Tel: %s".formatted(tipoPersona(), getNombre(), getDocumento(), telefono);
    }

    @Override
    public String toString() {
        return """
               ************ CLIENTE ************
               Id:          %d
               Nombre:      %s
               Documento:   %s
               Correo:      %s
               Telefono:    %s
               """.formatted(getId(), getNombre(), getDocumento(), getCorreo(), telefono);
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
