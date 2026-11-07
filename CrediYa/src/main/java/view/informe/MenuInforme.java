package view.informe;

import view.Validaciones;

public class MenuInforme {

    private final Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               ========= MODULO INFORMES =========
                               1- Prestamos activos.
                               2- Prestamos vencidos.
                               3- Clientes morosos.
                               4- Resumen de cartera por estado.
                               5- Gestion por empleado.
                               6- Top 5 clientes con mayor deuda.
                               7- Directorio general (empleados y clientes).
                               8- Ver reportes guardados en archivo.
                               9- Exportar un reporte guardado a texto (.txt).
                               10- Exportar prestamos a Excel (.csv).
                               11- Regresar.
                               """);
    }

    public boolean deseaGuardar() {
        return v.confirmar("Desea guardar este reporte en el archivo?");
    }

    public int pedirIdExportar() {
        return v.validarEntero("Ingrese el numero del reporte que desea exportar (0 = volver):");
    }

    public int pedirIdReporte() {
        return v.validarEntero("Ingrese el numero del reporte que desea ver (0 = volver):");
    }
}
