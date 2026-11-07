package view.cliente;

import java.util.List;
import java.util.function.Consumer;
import model.Cliente;
import view.Tirilla;

public class ImprimirCliente {

    public Consumer<Cliente> entidad = c -> System.out.println(c);

    public void lista(List<Cliente> clientes) {
        Tirilla t = new Tirilla().encabezado("LISTADO DE CLIENTES");
        for (Cliente c : clientes) {
            t.dato("CLIENTE", "#" + c.getId())
                    .dato("Nombre", c.getNombre())
                    .dato("Documento", c.getDocumento())
                    .dato("Correo", c.getCorreo())
                    .dato("Telefono", c.getTelefono())
                    .linea('-');
        }
        t.dato("Clientes registrados", String.valueOf(clientes.size()));
        System.out.println(t.pie("Fin del listado").construir());
    }
}
