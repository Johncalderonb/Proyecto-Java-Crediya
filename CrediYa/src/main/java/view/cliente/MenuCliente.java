package view.cliente;

import view.Validaciones;

public class MenuCliente {

    private final Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               ========= MODULO CLIENTES =========
                               1- Registrar.
                               2- Listar.
                               3- Buscar.
                               4- Actualizar.
                               5- Eliminar.
                               6- Consultar prestamos del cliente.
                               7- Regresar.
                               """);
    }

    public int opcionesActualizar() {
        return v.validarEntero("""
                               Ingrese que desea actualizar:
                               1- Nombre.
                               2- Correo.
                               3- Telefono.
                               4- Restablecer clave.
                               5- Cancelar.
                               """);
    }
}
