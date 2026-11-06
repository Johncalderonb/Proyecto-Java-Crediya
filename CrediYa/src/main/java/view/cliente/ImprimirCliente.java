package view.cliente;

import java.util.List;
import java.util.function.Consumer;
import model.Cliente;

public class ImprimirCliente {

    public Consumer<Cliente> entidad = c -> System.out.println(c);

    public void lista(List<Cliente> clientes) {
        clientes.forEach(entidad);
    }
}
