package archivo;

import exception.CrediYaException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import model.Reporte;

public class ArchivoReportes {

    private final String ARCHIVO = "reportes_crediya.dat";
    private List<Reporte> reportes = new ArrayList<>();

    public ArchivoReportes() {
        try {
            cargar();
        } catch (CrediYaException e) {
            reportes = new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private void cargar() throws CrediYaException {
        File f = new File(ARCHIVO);
        if (!f.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            reportes = (List<Reporte>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CrediYaException("No se pudo leer el archivo de reportes");
        }
    }

    private void guardar() throws CrediYaException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(reportes);
        } catch (IOException e) {
            throw new CrediYaException("No se pudo guardar el archivo de reportes");
        }
    }

    private int getId() {
        return reportes.isEmpty() ? 1 : reportes.getLast().getId() + 1;
    }

    public void agregar(Reporte reporte) throws CrediYaException {
        reporte.setId(getId());
        reportes.add(reporte);
        guardar();
    }

    public List<Reporte> listar() {
        return reportes;
    }

    public Reporte buscar(int id) {
        for (Reporte r : reportes) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public String getRuta() {
        return new File(ARCHIVO).getAbsolutePath();
    }
}
