package ar.edu.unlp.info.oo2.facturacion_llamadas;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

public class RandomGenerador implements Generador {
    public String obtenerNumeroLibre(SortedSet<String> lineas) {
        List<String> lista = new ArrayList<>(lineas);
        String linea = lista.get(new Random().nextInt(lista.size()));
        lineas.remove(linea);
        return linea;
    }
}