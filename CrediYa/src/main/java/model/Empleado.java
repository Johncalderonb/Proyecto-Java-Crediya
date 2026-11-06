package model;

public class Empleado extends Persona {

    private RolEmpleado rol;
    private double salario;

    public Empleado(long id, String nombre, String documento, RolEmpleado rol, String correo, double salario, String clave) {
        super(id, nombre, documento, correo, clave);
        this.rol = rol;
        this.salario = salario;
    }

    public Empleado(String nombre, String documento, RolEmpleado rol, String correo, double salario, String clave) {
        super(nombre, documento, correo, clave);
        this.rol = rol;
        this.salario = salario;
    }

    @Override
    public String tipoPersona() {
        return "EMPLEADO";
    }

    @Override
    public String resumen() {
        return "%-9s | %-20s | Doc: %-12s | Rol: %s".formatted(tipoPersona(), getNombre(), getDocumento(), rol);
    }

    @Override
    public String toString() {
        return """
               ************ EMPLEADO ************
               Id:          %d
               Nombre:      %s
               Documento:   %s
               Rol:         %s
               Correo:      %s
               Salario:     $%s
               """.formatted(getId(), getNombre(), getDocumento(), rol, getCorreo(),
                String.format("%,.0f", salario));
    }

    public RolEmpleado getRol() {
        return rol;
    }

    public void setRol(RolEmpleado rol) {
        this.rol = rol;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
