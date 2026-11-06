package view.empleado;

import java.util.List;
import java.util.function.Consumer;
import model.Empleado;

public class ImprimirEmpleado {

    public Consumer<Empleado> entidad = e -> System.out.println(e);

    public void lista(List<Empleado> empleados) {
        empleados.forEach(entidad);
    }
}
