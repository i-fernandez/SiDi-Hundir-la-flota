package es.uned.common;

import java.io.Serializable;

/**
 * Clase que representa un disparo dentro de la partida
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class Disparo  implements Serializable{

	private static final long serialVersionUID = -3946259539044728095L;
	private String jugador;
	private char fila;
	private int columna;
	private ResultadoDisparo resultado;

	/**
	 * Constructor
	 * @param jugador Jugador que realiza el disparo
	 * @param input Coordenadas leidas
	 * @throws IllegalArgumentException
	 */
	public Disparo(String jugador, String input) throws IllegalArgumentException {
		this.jugador = jugador;
		fila = input.charAt(0);
		if ("ABCDEFGHIJ".indexOf(fila) == -1) throw new IllegalArgumentException("Fila no valida.");

		input = input.substring(1);		
		columna = Integer.parseInt(input);
		if (columna < 1 || columna > 10) throw new IllegalArgumentException("Columna no valida.");
	}
	
	/**
	 * Establece el resultado del disparo
	 * @param rs Resultado del disparo
	 */
	public void setResultado(ResultadoDisparo rs) {
		resultado = rs;
	}
	
	/**
	 * Devuelve el resutado del disparo
	 * @return Resultado del disparo
	 */
	public ResultadoDisparo getResultado() {
		return resultado;
	}
	
	/**
	 * Develve la columna del disparo
	 * @return Columna
	 */
	public int getColumna() {
		return columna - 1;
	}
	
	/**
	 * Devuelve el numero de fila
	 * @return Fila
	 */
	public int getFilaInt() {
		return (int) fila - 65;
	}
	
	/**
	 * Devuelve la fila (A-J)
	 * @return Fila
	 */
	public char getFilaChar() {
		return fila;
	}
	
	/**
	 * Devuelve el jugador que realiza el disparo
	 * @return Jugador
	 */
	public String getJugador() {
		return jugador;
	}
}
