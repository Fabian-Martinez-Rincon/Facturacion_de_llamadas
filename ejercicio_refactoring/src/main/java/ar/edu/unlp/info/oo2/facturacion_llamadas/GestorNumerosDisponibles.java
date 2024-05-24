package ar.edu.unlp.info.oo2.facturacion_llamadas;

import java.util.TreeSet;
import java.util.SortedSet;

public class GestorNumerosDisponibles {
	private SortedSet<String> lineas = new TreeSet<String>(); 
	private Generador tipoGenerador = new UltimoGenerador();

	public String obtenerNumeroLibre() {
		return tipoGenerador.obtenerNumeroLibre(lineas);
	}

	public void cambiarTipoGenerador(Generador generador) {
		this.tipoGenerador = generador;
	}
	public boolean agregarNumeroTelefono(String numero) {
		return this.lineas.add(numero);
	}	
}


