package view.prestamo;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import model.Prestamo;

public class ImprimirPrestamo {

    public Function<Prestamo, String> aFila = p -> "#%-4d | %-18s | Total: $%13s | Saldo: $%13s | Cuota: $%11s | %s"
            .formatted(p.getId(), p.getCliente().getNombre(),
                    String.format("%,.0f", p.getMontoTotal()), String.format("%,.0f", p.getSaldo()),
                    String.format("%,.0f", p.getValorCuota()), p.getEstado());

    public Consumer<Prestamo> fila = p -> System.out.println(aFila.apply(p));

    public Consumer<Prestamo> detalle = p -> System.out.println(p);

    public void lista(List<Prestamo> prestamos) {
        prestamos.forEach(fila);
    }
}
