package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tirilla {

    private static final int ANCHO = 42;

    private final StringBuilder sb = new StringBuilder();

    public Tirilla encabezado(String titulo) {
        linea('=');
        centro("CREDIYA SAS");
        centro("Creditos personales");
        centro("Bucaramanga - Santander");
        linea('=');
        centro(titulo);
        dato("Impreso", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        linea('-');
        return this;
    }

    public Tirilla linea(char simbolo) {
        sb.append(String.valueOf(simbolo).repeat(ANCHO)).append("\n");
        return this;
    }

    public Tirilla centro(String texto) {
        String corto = recortar(texto, ANCHO);
        int izquierda = (ANCHO - corto.length()) / 2;
        sb.append(" ".repeat(izquierda)).append(corto).append("\n");
        return this;
    }

    public Tirilla dato(String etiqueta, String valor) {
        String v = recortar(valor, ANCHO - 4);
        String e = recortar(etiqueta, ANCHO - v.length() - 2);
        int puntos = ANCHO - e.length() - v.length() - 2;
        sb.append(e).append(" ").append(".".repeat(puntos)).append(" ").append(v).append("\n");
        return this;
    }

    public Tirilla dinero(String etiqueta, double valor) {
        return dato(etiqueta, "$" + String.format("%,.0f", valor));
    }

    public Tirilla texto(String texto) {
        sb.append(recortar(texto, ANCHO)).append("\n");
        return this;
    }

    public Tirilla pie(String mensaje) {
        linea('=');
        centro(mensaje);
        linea('=');
        return this;
    }

    public String construir() {
        return sb.toString();
    }

    private String recortar(String texto, int maximo) {
        return texto.length() <= maximo ? texto : texto.substring(0, maximo);
    }
}
