package model;

import seguridad.Encriptador;

public abstract class Persona {

    private long id;
    private String nombre;
    private String documento;
    private String correo;
    private String clave;

    public Persona(long id, String nombre, String documento, String correo, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.clave = clave;
    }

    public Persona(String nombre, String documento, String correo, String clave) {
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.clave = clave;
    }

    public abstract String tipoPersona();

    public abstract String resumen();

    public boolean verificarClave(String claveIngresada) {
        return Encriptador.coincide(claveIngresada, clave);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
