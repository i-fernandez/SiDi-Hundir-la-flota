package es.uned.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Clase de apoyo para mostrar informacion en pantalla
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class Gui {

	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));	
	
	/**
	 * Muestra un menu en pantalla y lee la eleccion del usuario
	 * @param name Nombre del menu
	 * @param entradas Diferentes opciones del menu
	 * @return Eleccion del usuario
	 */
	public static int menu(String name, String[] entradas) {
		System.out.println("=== " + name + " ===");
		
		for (int i = 0; i < entradas.length; i++) {
			System.out.println((i+1) + ".- " + entradas[i]);
		}
		
		int opt = -1;
		do {
			try {
				opt = Integer.parseInt(reader.readLine().trim());
			} 
			catch (IOException ex) {
				throw new RuntimeException(ex);
			} 
			catch (NumberFormatException ex) {
				System.out.println("Opcion no valida");
			}
			
			if (opt - 1 >= entradas.length || opt <= 0) {
				System.out.println("Ingrese una opcion del 1 al " + entradas.length);
				opt = -1;
			}
		}
		while (opt == -1);
		
		System.out.println();
		return opt - 1;
	}
	
	/**
	 * Muestra en pantalla un menu de accion y lee la respuesta del usuario
	 * @param msg Mensaje a mostrar
	 * @return Respuesta del usuario
	 */
	public static String menu(String msg) {
		String input = null;
		System.out.println(msg);
		
		do {
			try {
				input = reader.readLine().trim();
			} 
			catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} while (input == null || input.length() < 1);
		return input;
	}
	
	/**
	 * Espera a que el usuario presione enter
	 */
	public static void waitForEnter() {
		String line = null;
		do {
			System.out.println("Presione enter para continuar");
			try {
				line = reader.readLine();
			}
			catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} while (line == null);
	}
	
	/**
	 * Lee una linea desde la consola
	 * @return valor leido
	 */
	public static String read() {
		String line = null;
		do {
			try {
				line = reader.readLine();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} while (line == null || line.length() < 1);
		return line;
	}
	
	/**
	 * Muestra en pantalla ambos tableros de un jugador
	 * @param tPropio Tablero del jugador
	 * @param tOponente Tablero del oponente
	 */
	public static void printTableros(char[][] tPropio, char[][] tOponente) {
		int f = 65;
		printChar(' ', 13);
		System.out.print("DISPAROS   RECIBIDOS");
		printChar(' ', 26);
		System.out.print("DISPAROS REALIZADOS" + "\n");
		System.out.println("   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10|"+
					"     | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10|");
		System.out.println("   |---|---|---|---|---|---|---|---|---|---|"+
						"     |---|---|---|---|---|---|---|---|---|---|");
		for (int i = 0; i < tPropio.length; i++) {
			System.out.print(" " + (char) f + " |");			
			for (int j = 0; j < tPropio[0].length; j++) {
				System.out.print(" " + tPropio[i][j] + " |");
			}
			System.out.print("   " + (char) f + " |");
			for (int j = 0; j < tOponente[0].length; j++) {
				System.out.print(" " + tOponente[i][j] + " |");
			}
			f++;
			System.out.println();
		}
		System.out.println();
		printLeyenda();
	}
	
	/**
	 * Muestra en pantalla un caracter n veces
	 * @param c Caracter a mostrar
	 * @param n Numero de veces
	 */
	public static void printChar(char c, int n) {
		for (int i = 0; i < n; i++) {
			System.out.print(c);
		}
	}
	
	/**
	 * Muestra en pantalla los mensajes al ganador
	 * @param rendido true si el rival se ha rendido
	 */
	public static void printGanador(boolean rendido) {
		System.out.println();
		if (rendido) System.out.println("Tu rival se ha rendido.");
		System.out.println(" ENHORABUENA !!!!!!");
		System.out.println("Has ganado la partida.");
	}
	
	/**
	 * Muestra en pantalla la cabecera del juego
	 */
	public static void printNombreJuego() {
		System.out.println("   _    _                 _ _        ");      
		System.out.println("  | |  | |               | (_)       ");   
		System.out.println("  | |__| |_   _ _ __   __| |_ _ __   ");
		System.out.println("  |  __  | | | | \'_ \\ / _` | | '__|");
		System.out.println("  | |_ | | |_| | | | | (_| | | |     ");
		System.out.println("  |_| ||_|\\__,_|_| |_|\\__,_|_|_|   ");
		System.out.println("    | |     __ _                     ");
		System.out.println("    | |    / _` |                    ");
		System.out.println("    | |___| (_| |                    ");
		System.out.println("   _|______\\__,_| _                 ");
		System.out.println("  |  ____| |     | |                 ");
		System.out.println("  | |__  | | ___ | |_ __ _           ");
		System.out.println("  |  __| | |/ _ \\| __/ _` |         ");
		System.out.println("  | |    | | (_) | || (_| |          ");
		System.out.println("  |_|    |_|\\___/ \\__\\__,_|       ");
		System.out.println("");
		System.out.println("");
	}
	
	/**
	 * Muestra en pantalla la cabecera del servicio de datos
	 */
	public static void printDatos() {
		System.out.println(" _____          _                   ");              
		System.out.println("|  __ \\        | |                 ");             
		System.out.println("| |  | |  __ _ | |_  ___   ___      ");             
		System.out.println("| |  | | / _` || __|/ _ \\ / __|    ");             
		System.out.println("| |__| || (_| || |_| (_) |\\__ \\   ");             
		System.out.println("|_____/  \\__,_| \\__|\\___/ |___/  ");
		System.out.println("");
		System.out.println("");
	}
	
	/**
	 * Muestra en pantalla la cabecera del servidor
	 */
	public static void printServidor() {
		System.out.println("  _____                     _      _                         ");              
		System.out.println(" / ____|                   (_)    | |                        ");    
		System.out.println("| (___    ___  _ __ __   __ _   __| |  ___   _ __            "); 
		System.out.println(" \\___ \\  / _ \\| '__|\\ \\ / /| | / _` | / _ \\ | '__| ");
		System.out.println(" ____) ||  __/| |    \\ V / | || (_| || (_) || |             ");
		System.out.println("|_____/  \\___||_|     \\_/  |_| \\__,_| \\___/ |_|          ");
		System.out.println("");
		System.out.println("");
	}
	
	/**
	 * Muestra en pantalla la leyenda
	 */
	private static void printLeyenda() {
		System.out.println("  LEYENDA: ");
		System.out.println("    B : BARCO");
		System.out.println("    X : TOCADO");
		System.out.println("    A : AGUA");
		System.out.println();
	}
}

