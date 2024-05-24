package ar.edu.unlp.info.oo2.facturacion_llamadas;

import java.util.SortedSet;

public class PrimeroGenerador implements Generador {    
    public String obtenerNumeroLibre(SortedSet<String> lineas) {
        String linea = lineas.first();
        lineas.remove(linea);
        return linea;     
    }
}