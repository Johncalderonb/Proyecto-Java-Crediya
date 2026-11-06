package seguridad;

import model.Persona;

public class Sesion {

    private static Sesion instancia;
    private Persona usuario;

    private Sesion() {
    }

    public static Sesion getInstance() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    public void iniciar(Persona persona) {
        usuario = persona;
    }

    public void cerrar() {
        usuario = null;
    }

    public Persona getUsuario() {
        return usuario;
    }

    public boolean esUsuarioActual(Persona persona) {
        return usuario != null && usuario.getId() == persona.getId()
                && usuario.tipoPersona().equals(persona.tipoPersona());
    }
}
