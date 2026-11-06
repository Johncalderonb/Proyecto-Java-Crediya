package model;

import java.time.LocalDate;

public class Pago {

    private long id;
    private long prestamoId;
    private LocalDate fechaPago;
    private double monto;

    public Pago(long id, long prestamoId, LocalDate fechaPago, double monto) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.fechaPago = fechaPago;
        this.monto = monto;
    }

    public Pago(long prestamoId, LocalDate fechaPago, double monto) {
        this.prestamoId = prestamoId;
        this.fechaPago = fechaPago;
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Pago #%-4d | Prestamo #%-4d | Fecha: %s | Valor: $%s"
                .formatted(id, prestamoId, fechaPago, String.format("%,.0f", monto));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrestamoId() {
        return prestamoId;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public double getMonto() {
        return monto;
    }
}
