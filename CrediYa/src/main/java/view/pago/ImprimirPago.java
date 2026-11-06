package view.pago;

import java.util.List;
import java.util.function.Consumer;
import model.Pago;

public class ImprimirPago {

    public Consumer<Pago> entidad = p -> System.out.println(p);

    public void lista(List<Pago> pagos) {
        pagos.forEach(entidad);
    }
}
