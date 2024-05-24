package ar.edu.unlp.info.oo2.facturacion_llamadas;

import java.util.SortedSet;

public class UltimoGenerador implements Generador {
    public String obtenerNumeroLibre(SortedSet<String> lineas) {
        String linea = lineas.last();
        lineas.remove(linea);
        return linea;
    }
}