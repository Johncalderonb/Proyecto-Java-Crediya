package model;

import exception.CrediYaException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {

    private long id;
    private Cliente cliente;
    private Empleado empleado;
    private double monto;
    private double interes;
    private int cuotas;
    private LocalDate fechaInicio;
    private EstadoPrestamo estado;
    private double saldo;
    private double montoTotal;
    private double valorCuota;

    public Prestamo(long id, Cliente cliente, Empleado empleado, double monto, double interes, int cuotas,
            LocalDate fechaInicio, EstadoPrestamo estado, double saldo) {
        this.id = id;
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
        this.saldo = saldo;
        calcularTotales();
    }

    public Prestamo(Cliente cliente, Empleado empleado, double monto, double interes, int cuotas, LocalDate fechaInicio) {
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = EstadoPrestamo.PENDIENTE;
        calcularTotales();
        this.saldo = montoTotal;
    }

    private void calcularTotales() {
        montoTotal = redondear(monto + (monto * interes / 100));
        valorCuota = redondear(montoTotal / cuotas);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }

    public void registrarAbono(double valor) throws CrediYaException {
        if (estado == EstadoPrestamo.PAGADO) {
            throw new CrediYaException("El prestamo #" + id + " ya se encuentra PAGADO");
        }
        if (valor <= 0) {
            throw new CrediYaException("El valor del abono debe ser mayor a cero");
        }
        if (valor > saldo) {
            throw new CrediYaException("El abono supera el saldo pendiente: $" + String.format("%,.0f", saldo));
        }
        saldo = redondear(saldo - valor);
        if (saldo == 0) {
            estado = EstadoPrestamo.PAGADO;
        }
    }

    public void cambiarEstado(EstadoPrestamo nuevoEstado) throws CrediYaException {
        if (estado == nuevoEstado) {
            throw new CrediYaException("El prestamo ya se encuentra en estado " + nuevoEstado);
        }
        if (nuevoEstado == EstadoPrestamo.PENDIENTE && saldo == 0) {
            throw new CrediYaException("No se puede pasar a PENDIENTE un prestamo sin saldo");
        }
        if (nuevoEstado == EstadoPrestamo.PAGADO) {
            saldo = 0;
        }
        estado = nuevoEstado;
    }

    public double getTotalPagado() {
        return redondear(montoTotal - saldo);
    }

    public LocalDate getFechaFin() {
        return fechaInicio.plusMonths(cuotas);
    }

    public int cuotasQueDebeLlevar() {
        long meses = ChronoUnit.MONTHS.between(fechaInicio, LocalDate.now());
        if (meses < 0) {
            return 0;
        }
        return (int) Math.min(meses, cuotas);
    }

    public double valorEnMora() {
        double deberiaLlevar = cuotasQueDebeLlevar() * valorCuota;
        double mora = deberiaLlevar - getTotalPagado();
        return mora > 0 ? redondear(Math.min(mora, saldo)) : 0;
    }

    public boolean estaAtrasado() {
        return estado == EstadoPrestamo.PENDIENTE && valorEnMora() > 0;
    }

    public boolean estaVencido() {
        return estado == EstadoPrestamo.PENDIENTE && getFechaFin().isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return """
               ************ PRESTAMO #%d ************
               Cliente:         %s (Doc: %s)
               Empleado:        %s
               Monto prestado:  $%s
               Interes:         %.2f%%
               Total a pagar:   $%s
               Cuotas:          %d de $%s
               Fecha inicio:    %s
               Fecha fin:       %s
               Total pagado:    $%s
               Saldo pendiente: $%s
               Estado:          %s
               """.formatted(id, cliente.getNombre(), cliente.getDocumento(), empleado.getNombre(),
                String.format("%,.0f", monto), interes, String.format("%,.0f", montoTotal),
                cuotas, String.format("%,.0f", valorCuota), fechaInicio, getFechaFin(),
                String.format("%,.0f", getTotalPagado()), String.format("%,.0f", saldo), estado);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public double getMonto() {
        return monto;
    }

    public double getInteres() {
        return interes;
    }

    public int getCuotas() {
        return cuotas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public double getValorCuota() {
        return valorCuota;
    }
}
