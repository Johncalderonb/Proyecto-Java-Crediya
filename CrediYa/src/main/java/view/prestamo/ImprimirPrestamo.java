package view.prestamo;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import model.Pago;
import model.Prestamo;
import view.Tirilla;

public class ImprimirPrestamo {

    public Function<Prestamo, String> aFila = p -> "#%-4d | %-18s | Total: $%13s | Saldo: $%13s | Cuota: $%11s | %s"
            .formatted(p.getId(), p.getCliente().getNombre(),
                    String.format("%,.0f", p.getMontoTotal()), String.format("%,.0f", p.getSaldo()),
                    String.format("%,.0f", p.getValorCuota()), p.getEstado());

    public Consumer<Prestamo> fila = p -> System.out.println(aFila.apply(p));

    public Consumer<Prestamo> detalle = p -> System.out.println(p);

    public void lista(List<Prestamo> prestamos) {
        lista(prestamos, "LISTADO DE PRESTAMOS");
    }

    public void lista(List<Prestamo> prestamos, String titulo) {
        Tirilla t = new Tirilla().encabezado(titulo);
        for (Prestamo p : prestamos) {
            t.dato("PRESTAMO", "#" + p.getId())
                    .dato("Cliente", p.getCliente().getNombre())
                    .dato("Documento", p.getCliente().getDocumento())
                    .dinero("Total a pagar", p.getMontoTotal())
                    .dato("Cuotas", p.getCuotas() + " x $" + String.format("%,.0f", p.getValorCuota()))
                    .dinero("Saldo", p.getSaldo())
                    .dato("Estado", p.estaAtrasado() ? "PENDIENTE (EN MORA)" : p.getEstado().name())
                    .linea('-');
        }
        double cartera = prestamos.stream().mapToDouble(Prestamo::getSaldo).sum();
        double mora = prestamos.stream().mapToDouble(Prestamo::valorEnMora).sum();
        t.dato("Cantidad de prestamos", String.valueOf(prestamos.size()))
                .dinero("SALDO TOTAL PENDIENTE", cartera);
        if (mora > 0) {
            t.dinero("TOTAL EN MORA", mora);
        }
        System.out.println(t.pie("Fin del listado").construir());
    }

    public void tirilla(Prestamo p, List<Pago> pagos) {
        String titulo = p.getId() == 0 ? "SIMULACION DE PRESTAMO" : "ESTADO DE CUENTA No. " + p.getId();
        Tirilla t = new Tirilla()
                .encabezado(titulo)
                .dato("Cliente", p.getCliente().getNombre())
                .dato("Documento", p.getCliente().getDocumento())
                .dato("Otorgado por", p.getEmpleado().getNombre())
                .linea('-')
                .dinero("Monto prestado", p.getMonto())
                .dinero("Interes " + String.format("%.2f", p.getInteres()) + "%", p.getMontoTotal() - p.getMonto())
                .dinero("TOTAL A PAGAR", p.getMontoTotal())
                .dato("Cuotas", p.getCuotas() + " x $" + String.format("%,.0f", p.getValorCuota()))
                .dato("Fecha inicio", p.getFechaInicio().toString())
                .dato("Fecha final", p.getFechaFin().toString())
                .linea('-');
        if (!pagos.isEmpty()) {
            t.centro("PAGOS REALIZADOS");
            for (Pago pago : pagos) {
                t.dinero("#" + pago.getId() + "  " + pago.getFechaPago(), pago.getMonto());
            }
            t.linea('-');
        }
        t.dinero("Total pagado", p.getTotalPagado())
                .dinero("SALDO PENDIENTE", p.getSaldo())
                .dato("Estado", p.getEstado().name());
        if (p.estaAtrasado()) {
            t.dinero("EN MORA", p.valorEnMora());
        }
        System.out.println(t.pie("Gracias por confiar en CrediYa").construir());
    }
}
