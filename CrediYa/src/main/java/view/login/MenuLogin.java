package view.login;

import view.Validaciones;

public class MenuLogin {

    private final Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               =====================================
                                    BIENVENIDO A CREDIYA SAS
                                 Sistema de prestamos y cartera
                               =====================================
                               1- Ingresar como administrador.
                               2- Ingresar como cliente.
                               3- Salir.
                               """);
    }
}
