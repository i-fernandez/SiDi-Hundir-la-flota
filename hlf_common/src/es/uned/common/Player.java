package es.uned.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase que representa uno de los jugadores de una partida concreta
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class Player extends UnicastRemoteObject {

	private static final long serialVersionUID = 1697641746743000472L;
	private static final char barco = 'B';
	private static final char agua = 'A';
	private static final char tocado = 'X';
	private static final char vacio = ' ';
	
	private String username;
	private int puntuacion = 0;
	private char[][] tableroPropio = new char[10][10];
	private char[][] tableroOponente = new char[10][10];
	private List<String> barco1 = new ArrayList<String>();
	private List<String> barco2 = new ArrayList<String>();
	
	/**
	 * Constructor
	 * @param name Nombre del jugador
	 * @throws RemoteException
	 */
	public Player(String name) throws RemoteException{
		username = name;
		initTablero(tableroPropio);
		initTablero(tableroOponente);
	}

	/**
	 * Devuelve el nombre del jugador
	 * @return Nombre del jugador
	 */
	protected String getUsername() {
		return username;
	}
	
	/**
	 * Devuelve la puntuacion del jugador
	 * @return Puntuacion
	 */
	protected int getPuntuacion() {
		return puntuacion;
	}
	
	/**
	 * Devuelve el tablero con los barcos del jugador
	 * @return Tablero
	 */
	protected char[][] getTableroPropio() {
		return tableroPropio;
	}
	
	/**
	 * Devuelve el tablero con los disparos efectuados
	 * @return Tablero
	 */
	protected char[][] getTableroOponente() {
		return tableroOponente;
	}
	
	/**
	 * Añade una puntuacion al jugador
	 * @param puntos Puntuacion a añadir
	 */
	protected void sumaPuntuacion(int puntos) {
		puntuacion += puntos;
	}
	
	/**
	 * Colocar un barco en el tablero
	 * @param coordenada Coordenada del barco [XYZ]
	 * @return true si el barco se coloca satisfactoriamente
	 */
	protected boolean colocarBarco(String coordenada) {		
		char filaChar = coordenada.charAt(0);
		if ("ABCDEFGHIJ".indexOf(filaChar) == -1) return false;
		int fila = (int) filaChar - 65; 		// Convierte a entero
		coordenada = coordenada.substring(1);	// Elimina el caracter de fila
		char oriChar = coordenada.charAt(coordenada.length()-1);
		if (oriChar != 'V' && oriChar != 'H') return false; 
		String colChar = coordenada.substring(0, coordenada.length()-1);
		int columna;
		try {
			columna = Integer.parseInt(colChar) - 1;
			if (columna < 0 || columna > 9) return false;
		} catch (Exception e) {
			return false;
		}
		
		// Comprueba que el barco no se sale del tablero
		if (oriChar == 'H') {
			if (columna + 2 > 9) return false;
		} else if (oriChar == 'V') {
			if (fila + 2 > 9) return false;
		}
		
		if (!casillasLibres(fila, columna, oriChar)) return false;
		addBarco(filaChar, columna+1, oriChar);
		reservaCasillas(fila, columna, oriChar);
		return true;
	}
	
	/**
	 * Comprueba si el jugador ha colocados todos sus barcos
	 * @return true si ya se han colocado ambos barcos
	 */
	protected boolean barcosColocados() {
		return (!barco1.isEmpty() && !barco2.isEmpty());
	}
	
	
	/**
	 * Marca un disparo realizado en el tablero propio
	 * @param d Disparo
	 * @param r Resultado en el oponente (true=tocado) (false=agua)
	 */
	protected void realizarDisparo(Disparo d, ResultadoDisparo r) {
		int f = d.getFilaInt();
		int c = d.getColumna();
		if (r == ResultadoDisparo.AGUA) {
			if (tableroOponente[f][c] == vacio) tableroOponente[f][c] = agua;
		}
		else tableroOponente[f][c] = tocado;
	}
	
	/**
	 * Recibe un disparo del oponente
	 * @param d Disparo
	 * @return r Resultado del disparo
	 */
	protected ResultadoDisparo recibirDisparo(Disparo d) {
		int f = d.getFilaInt();
		int c = d.getColumna();
		if (tableroPropio[f][c] == barco) {
			tableroPropio[f][c] = tocado;
			String s = d.getFilaChar() + Integer.toString(c + 1);
			if (barco1.contains(s)) {
				barco1.remove(s);
				if (barco1.isEmpty()) {
					if (barco2.isEmpty()) return ResultadoDisparo.GANADOR;
					else return ResultadoDisparo.HUNDIDO;
				}
				return ResultadoDisparo.TOCADO;
			}
			
			if (barco2.contains(s)) {
				barco2.remove(s);
				if (barco2.isEmpty()) {
					if (barco1.isEmpty()) return ResultadoDisparo.GANADOR;
					else return ResultadoDisparo.HUNDIDO;
				}
				return ResultadoDisparo.TOCADO;
			}
		} 
		if (tableroPropio[f][c] == vacio) tableroPropio[f][c] = agua;
		return ResultadoDisparo.AGUA;
	}
	
	/**
	 * Realiza la rendicion del jugador
	 */
	protected void rendirse() {
		puntuacion = 0;
	}
	
	/**
	 * Realiza la rendicion del oponente
	 */
	protected void rendirOponente() {
		puntuacion = 16;
	}
	
	/**
	 * Inicializa un tablero en blanco
	 * @param tablero
	 */
	private void initTablero(char[][] tablero) {
		for (int i = 0; i < 10; i++ ) {
			for (int j = 0; j < 10; j++) {
				tablero[i][j] = vacio;
			}
		}
	}
	
	/**
	 * Añade un barco al tablero
	 * @param filaChar Fila (A-J)
	 * @param col Numero de columna 
	 * @param orientacion Orientacion (H, V)
	 */
	private void addBarco(char filaChar, int col, char orientacion) {
		ArrayList<String> barco;
		if (barco1.isEmpty()) {
			barco = (ArrayList<String>)barco1; 
		} else {
			barco = (ArrayList<String>)barco2;
		}
		barco.add(filaChar + Integer.toString(col));
		if (orientacion == 'H') {
			barco.add(filaChar + Integer.toString(col + 1));
			barco.add(filaChar + Integer.toString(col + 2));
		} else if (orientacion == 'V') {
			barco.add((char) (filaChar + 1) + Integer.toString(col));
			barco.add((char) (filaChar + 2) + Integer.toString(col));
		}
	}
	
	/**
	 * Marca las casillas con la colocacion de un barco
	 * @param fila Numero de fila
	 * @param col Numero de columna
	 * @param orientacion Orientacion
	 */
	private void reservaCasillas(int fila, int col, char orientacion) {
		tableroPropio[fila][col] = barco;
		if (orientacion == 'H') {
			tableroPropio[fila][col+1] = barco;
			tableroPropio[fila][col+2] = barco;
		} else if (orientacion == 'V') {
			tableroPropio[fila+1][col] = barco;
			tableroPropio[fila+2][col] = barco;
		}
	}
	
	/**
	 * Comprueba si las casillas de un barco estan libres u ocupadas
	 * @param fila Numero de fila
	 * @param col Numero de columna
	 * @param orientacion Orientacion
	 * @return true si las casillas estan libres
	 */
	private boolean casillasLibres(int fila, int col, char orientacion) {
		if (tableroPropio[fila][col] == barco) return false;
		if (orientacion == 'H' && 
				(tableroPropio[fila][col+1] == barco || tableroPropio[fila][col+2] == barco)) return false;
		if (orientacion == 'V' && 
				(tableroPropio[fila+1][col] == barco || tableroPropio[fila+2][col] == barco)) return false;
		return true;
	}
}
