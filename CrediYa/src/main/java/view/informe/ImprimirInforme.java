package view.informe;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import model.Reporte;

public class ImprimirInforme {

    public Function<Reporte, String> aIndice = r -> "#%-4d | %s | %s (%d registros)"
            .formatted(r.getId(), r.getFechaGeneracion(), r.getTitulo(), r.getLineas().size());

    public Consumer<Reporte> mostrar = r -> System.out.println(r);

    public void indice(List<Reporte> reportes) {
        System.out.println("======== REPORTES GUARDADOS ========");
        reportes.forEach(r -> System.out.println(aIndice.apply(r)));
        System.out.println("====================================");
    }
}
