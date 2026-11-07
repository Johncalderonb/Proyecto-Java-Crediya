package view.pago;

import java.util.List;
import java.util.function.Consumer;
import model.Pago;
import model.Prestamo;
import view.Tirilla;

public class ImprimirPago {

    public Consumer<Pago> entidad = p -> System.out.println(p);

    public void lista(List<Pago> pagos) {
        pagos.forEach(entidad);
    }

    public void recibo(Pago pago, Prestamo prestamo) {
        double saldoAnterior = prestamo.getSaldo() + pago.getMonto();
        Tirilla t = new Tirilla()
                .encabezado("RECIBO DE PAGO No. " + pago.getId())
                .dato("Fecha de pago", pago.getFechaPago().toString())
                .dato("Prestamo", "#" + prestamo.getId())
                .dato("Cliente", prestamo.getCliente().getNombre())
                .dato("Documento", prestamo.getCliente().getDocumento())
                .linea('-')
                .dinero("Saldo anterior", saldoAnterior)
                .dinero("VALOR ABONADO", pago.getMonto())
                .dinero("NUEVO SALDO", prestamo.getSaldo())
                .dato("Estado", prestamo.getEstado().name());
        String mensaje = prestamo.getSaldo() == 0 ? "PRESTAMO PAGADO - PAZ Y SALVO" : "Conserve este recibo";
        System.out.println(t.pie(mensaje).construir());
    }
}
