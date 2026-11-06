package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Reporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String fechaGeneracion;
    private List<String> lineas = new ArrayList<>();
    private String resumen;

    public Reporte(String titulo) {
        this.titulo = titulo;
        this.fechaGeneracion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.resumen = "";
    }

    public void agregarLinea(String linea) {
        lineas.add(linea);
    }

    public boolean estaVacio() {
        return lineas.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================================\n");
        sb.append("  CREDIYA SAS - ").append(titulo.toUpperCase()).append("\n");
        sb.append("  ").append(id > 0 ? "Reporte #" + id : "Reporte sin guardar")
                .append(" | Generado: ").append(fechaGeneracion).append("\n");
        sb.append("==================================================================\n");
        if (lineas.isEmpty()) {
            sb.append("  Sin resultados para este reporte\n");
        }
        for (String linea : lineas) {
            sb.append("  ").append(linea).append("\n");
        }
        sb.append("------------------------------------------------------------------\n");
        sb.append("  ").append(resumen).append("\n");
        sb.append("==================================================================\n");
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public List<String> getLineas() {
        return lineas;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
}
