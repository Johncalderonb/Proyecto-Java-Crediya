package view.empleado;

import view.Validaciones;

public class MenuEmpleado {

    private final Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               ========= MODULO EMPLEADOS =========
                               1- Registrar.
                               2- Listar.
                               3- Buscar.
                               4- Actualizar.
                               5- Eliminar.
                               6- Regresar.
                               """);
    }

    public int opcionesActualizar() {
        return v.validarEntero("""
                               Ingrese que desea actualizar:
                               1- Nombre.
                               2- Rol.
                               3- Correo.
                               4- Salario.
                               5- Restablecer clave.
                               6- Cancelar.
                               """);
    }
}
