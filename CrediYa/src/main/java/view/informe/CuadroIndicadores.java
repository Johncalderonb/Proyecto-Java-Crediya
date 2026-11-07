package view.informe;

import java.util.ArrayList;
import java.util.List;

public class CuadroIndicadores {

    private static final int ANCHO_CELDA = 20;
    private static final int POR_FILA = 3;

    private final List<String> titulos = new ArrayList<>();
    private final List<String> valores = new ArrayList<>();

    public CuadroIndicadores agregar(String titulo, String valor) {
        titulos.add(titulo);
        valores.add(valor);
        return this;
    }

    public String construir() {
        String borde = ("+" + "-".repeat(ANCHO_CELDA)).repeat(POR_FILA) + "+";
        StringBuilder sb = new StringBuilder();
        sb.append(borde).append("\n");
        for (int inicio = 0; inicio < titulos.size(); inicio += POR_FILA) {
            int fin = Math.min(inicio + POR_FILA, titulos.size());
            sb.append(fila(titulos.subList(inicio, fin))).append("\n");
            sb.append(fila(valores.subList(inicio, fin))).append("\n");
            sb.append(borde).append("\n");
        }
        return sb.toString();
    }

    private String fila(List<String> celdas) {
        StringBuilder sb = new StringBuilder("|");
        for (String celda : celdas) {
            sb.append(centrar(celda)).append("|");
        }
        for (int vacia = celdas.size(); vacia < POR_FILA; vacia++) {
            sb.append(" ".repeat(ANCHO_CELDA)).append("|");
        }
        return sb.toString();
    }

    private String centrar(String texto) {
        String corto = texto.length() <= ANCHO_CELDA ? texto : texto.substring(0, ANCHO_CELDA);
        int izquierda = (ANCHO_CELDA - corto.length()) / 2;
        int derecha = ANCHO_CELDA - corto.length() - izquierda;
        return " ".repeat(izquierda) + corto + " ".repeat(derecha);
    }
}
