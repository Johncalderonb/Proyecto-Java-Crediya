package archivo;

import exception.CrediYaException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import model.Prestamo;
import model.Reporte;

public class ExportadorArchivos {

    private final String CARPETA = "exportados";

    public Function<Prestamo, String> aLineaCsv = p -> String.join(";",
            String.valueOf(p.getId()),
            texto(p.getCliente().getNombre()),
            p.getCliente().getDocumento(),
            texto(p.getEmpleado().getNombre()),
            numero(p.getMonto()),
            numero(p.getInteres()),
            numero(p.getMontoTotal()),
            String.valueOf(p.getCuotas()),
            numero(p.getValorCuota()),
            p.getFechaInicio().toString(),
            p.getFechaFin().toString(),
            numero(p.getTotalPagado()),
            numero(p.getSaldo()),
            p.getEstado().name(),
            numero(p.valorEnMora()));

    private File prepararArchivo(String nombre) {
        File carpeta = new File(CARPETA);
        carpeta.mkdirs();
        return new File(carpeta, nombre);
    }

    public String exportarTxt(Reporte reporte) throws CrediYaException {
        String nombre = "reporte_" + reporte.getId() + "_"
                + reporte.getTitulo().toLowerCase().replaceAll("[^a-z0-9]+", "-") + ".txt";
        File archivo = prepararArchivo(nombre);
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, StandardCharsets.UTF_8))) {
            pw.print(reporte.toString());
        } catch (IOException e) {
            throw new CrediYaException("No se pudo crear el archivo de texto: " + e.getMessage());
        }
        return archivo.getAbsolutePath();
    }

    public String exportarCsv(List<Prestamo> prestamos) throws CrediYaException {
        File archivo = prepararArchivo("prestamos_" + LocalDate.now() + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, StandardCharsets.UTF_8))) {
            pw.print('﻿');
            pw.println("Id;Cliente;Documento;Otorgado por;Monto;Interes %;Total a pagar;Cuotas;"
                    + "Valor cuota;Fecha inicio;Fecha final;Total pagado;Saldo;Estado;En mora");
            prestamos.stream().map(aLineaCsv).forEach(pw::println);
        } catch (IOException e) {
            throw new CrediYaException("No se pudo crear el archivo CSV: " + e.getMessage());
        }
        return archivo.getAbsolutePath();
    }

    private String texto(String valor) {
        return valor.replace(";", ",");
    }

    private String numero(double valor) {
        return String.format("%.2f", valor);
    }
}
