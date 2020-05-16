package es.uned.jugador;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import es.uned.common.CallbackJugadorInterface;
import es.uned.common.Disparo;
import es.uned.common.TipoEvento;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Utils;
import es.uned.common.Gui;
import es.uned.common.ListaEventos;
import es.uned.common.PartidaInterface;

/**
 * Clase que contiene el main de la entidad "Jugador"
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class Jugador {
	
	private static int puertoAutenticacion = 5002;
	private static int puertoGestor = 5003;
	private static ServicioAutenticacionInterface sAutenticacion;
	private static ServicioGestorInterface sGestor;
	private static CallbackJugadorInterface jugador;
	private static String userName;
	private static int partidaActual;
	private static ListaEventos eventos = new ListaEventos();
	private static boolean turno = false; 
	
	public static void main(String[] args) {
		
		try {
			Utils.setCodeBase(CallbackJugadorInterface.class);
			
			// Conexion con servicios
			String URLRegistroAutenticacion = "rmi://localhost:" + puertoAutenticacion + "/Autenticacion";
			sAutenticacion = (ServicioAutenticacionInterface) Naming.lookup(URLRegistroAutenticacion);	
			String URLRegistroGestor = "rmi://localhost:" + puertoGestor + "/Gestor";
			sGestor = (ServicioGestorInterface) Naming.lookup(URLRegistroGestor);
			
			jugador = new CallbackJugadorImpl(eventos);
			menuInicial();
			
		} catch (RemoteException ex) {
			System.out.println("Error de comunicacion: " + ex.toString());
			System.exit(1);
		} catch (InterruptedException ex) {
			System.out.println(ex.toString());
		} catch (Exception ex) {
			System.out.println("Excepcion en jugador: " + ex.toString());
			System.exit(1);
		}
	}
	
	/**
	 * Muestra en pantalla el menu inicial
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void menuInicial() throws RemoteException, InterruptedException {
		int opt = 0;
		String[] opciones = new String[] { "Registrar un nuevo jugador",
										   "Hacer login",
										   "Salir "};
		do {
			Gui.printNombreJuego();
			opt = Gui.menu("Menu Inicial", opciones); 
			switch (opt) {
			case 0: 				
				registro(); 
				break;
			case 1:
				login(); 
				break;
			case 2: 
				System.exit(1);
			}
		} while (opt != 2);
	}
	
	/**
	 * Muestra en pantalla el menu de partida
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void menuPartida() throws RemoteException, InterruptedException {
		int opt = 0;
		String[] opciones = new String[] {"Informacion del jugador",
										  "Iniciar una partida",
										  "Listar partidas iniciadas a la espera de contrincante",
										  "Unirse a una partida ya iniciada",
										  "Salir \"Logout\""};
		do {
			opt = Gui.menu("Menu Partida", opciones);
			switch (opt) {
			case 0: 
				Gui.printNombreJuego();
				getInfoJugador(); 
				break;
			case 1: 
				crearPartida(); 
				break;
			case 2: 
				Gui.printNombreJuego();
				printPartidasCreadas(); 
				break;
			case 3: 
				unirsePartida(); 
				break;
			case 4: logout();
			}
		} while (opt != 4);
	}
	
	/**
	 * Realiza el registro de un nuevo jugador
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void registro() throws RemoteException, InterruptedException {
		String username, password = null;
		
		do {
			username = Gui.menu("Ingrese su nombre de usuario: ");
		} while (username == null || username.isEmpty());
		
		do {
			password = Gui.menu("Ingrese su contraseña: ");
		} while (password == null || password.isEmpty());
		
		if (sAutenticacion.registro(username, password)) {
			System.out.println("Usuario " + username + " registrado correctamente.");
			System.out.println();
		} else {
			System.out.println("Error al registrar a: " + username);
			System.out.println();
		}
        Thread.sleep(2000);  
	}
	
	/**
	 * Inicia sesion para el jugador
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void login() throws RemoteException, InterruptedException{
		String username, password = null;	
		do {
			username = Gui.menu("Ingrese su nombre de usuario: ");
		} while (username == null || username.isEmpty());
		
		do {
			password = Gui.menu("Ingrese su contraseña: ");
		} while (password == null || password.isEmpty());
		
		if (sAutenticacion.login(username, password, jugador)) {
			userName = username;
			Gui.printNombreJuego();
			menuPartida();
		} else {
			System.out.println("Error al iniciar sesion");
			System.out.println();
		}
        Thread.sleep(2000);  
	}
	
	/**
	 * Muestra en pantalla la informacion del jugador
	 * @throws RemoteException
	 */
	private static void getInfoJugador() throws RemoteException {
		System.out.println("Puntuacion del jugador " + userName + ": "+
				            sGestor.getPuntuacionJugador(userName) + " puntos.");
		System.out.println();
	}
	
	/**
	 * Crea una partida nueva
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void crearPartida() throws RemoteException, InterruptedException {
		partidaActual = sGestor.crearPartida(userName);
		turno = true;
		leerEvento();	
	}
	
	/**
	 * Muestra en pantalla una lista de partidas esperando oponente
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void printPartidasCreadas() throws RemoteException, InterruptedException {
		ArrayList<PartidaInterface> partidas = (ArrayList<PartidaInterface>) sGestor.getPartidasCreadas();
		if (partidas.size() == 0) {
			System.out.println("No existen partidas en juego en este momento");
		} else {
			System.out.println("========  PARTIDAS   CREADAS  ========");
			System.out.println("JUGADOR ................... ID PARTIDA");	
			for (PartidaInterface p : partidas) {
				String j1 = p.getJugador1();
				int id = p.getId();
				int nDots = 38 - j1.length() - Integer.toString(id).length();
				System.out.print(j1);
				Gui.printChar('.', nDots);
				System.out.print(id + "\n");
			}
			System.out.println();
			System.out.println("Total partidas: " + partidas.size());
		}
		System.out.println();
	}
	
	/**
	 * Se une a una partida que espera por oponente
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void unirsePartida() throws RemoteException, InterruptedException {
		String input ;
		do {
			input = Gui.menu("Introduzca el id de partida: ");
			int idPartida = Integer.parseInt(input);
			if (sGestor.existePartidaCreada(idPartida)) {
				System.out.println("Uniendose a la partida " + idPartida);
				System.out.println();
				sGestor.unirPartida(userName, idPartida);
				partidaActual = idPartida;
				turno = false;
				leerEvento();
			} else {
				System.out.println("Partida " + idPartida + " no encontrada");
				System.out.println();
				input = null;
			}
		} while (input == null || input.isEmpty());
	}
	
	/**
	 * Cierra sesion
	 * @throws RemoteException
	 */
	private static void logout() throws RemoteException {
		sAutenticacion.logout(userName);
		System.exit(1);
	}

	/**
	 * Lee el evento de la lista y lo procesa
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	private static void leerEvento() throws RemoteException,InterruptedException {
		boolean enJuego = true;
		do {
			TipoEvento evt = eventos.ObtenerEvento();
			switch (evt) {
			case PARTIDA_CREADA:
				System.out.println("A la espera de contrincante");
				break;
			case JUGADOR_UNIDO:
				System.out.println("Otro jugador se ha unido a su partida");
				break;
			case COLOCAR_BARCOS:
				colocarBarco(1);
				colocarBarco(2);
				System.out.println("Espere a que el otro jugador coloque sus barcos");
				break;
			case JUEGO_INICIADO:
				System.out.println("\n Comienza el juego" + "\n");
				printTableros();
				if (turno) realizarDisparo();
				else System.out.println("Esperando disparo del oponente..."); 
				break;
			case DISPARO_REALIZADO_AGUA:
				System.out.println();
				System.out.println("\n Disparo realizado: Agua" + "\n");
				printTableros();
				System.out.println("Esperando disparo del oponente...");
				break;
			case DISPARO_REALIZADO_TOCADO:
				System.out.println();
				System.out.println("\n Disparo realizado: Tocado" + "\n");
				printTableros();
				System.out.println("Esperando disparo del oponente...");
				break;
			case DISPARO_RECIBIDO_AGUA:
				System.out.println();
				System.out.println("\n Disparo recibido: Agua" + "\n");
				printTableros();
				realizarDisparo();
				break;
			case DISPARO_RECIBIDO_TOCADO:
				System.out.println();
				System.out.println("\n Disparo recibido: Tocado" + "\n");
				printTableros();
				realizarDisparo();
				break;
			case HAS_HUNDIDO_BARCO:
				System.out.println("\n Has hundido un barco al oponente");
				printTableros();
				System.out.println("Esperando disparo del oponente...");
				break;
			case TE_HAN_HUNDIDO_BARCO:
				System.out.println("\n El oponente te ha hundido un barco");
				printTableros();
				realizarDisparo();
				break;
			case HAS_GANADO:
				Gui.printGanador(false);
				showPuntuacion();
				enJuego = false;
				Gui.printNombreJuego();
				break;
			case HAS_PERDIDO:
				System.out.println();
				System.out.println("Has perdido la partida");
				showPuntuacion();
				enJuego = false;
				Gui.printNombreJuego();
				break;
			case RIVAL_RENDIDO:
				Gui.printGanador(true);
				showPuntuacion();
				enJuego = false;
				Gui.printNombreJuego();
			default:	
				break;
			}
		} while (enJuego);
	}
	
	/**
	 * Coloca un barco en el tablero de juego
	 * @param n Numero de barco a colocar
	 * @throws RemoteException
	 */
	private static void colocarBarco(int n) throws RemoteException {
		boolean valida = false;
		do {
			System.out.println();
			System.out.println("Introduzca coordenadas barco " + n + ": ");
			String c = Gui.read().toUpperCase();
			valida = sGestor.colocacionBarco(c, userName, partidaActual);
			if (!valida) System.out.println("Coordenadas no validas" + "\n");
		} while(!valida);
	}
	
	/**
	 * Muestra en pantalla ambos tableros y la leyenda
	 */
	private static void printTableros() throws RemoteException {
		Gui.printTableros(sGestor.getTableroPropio(partidaActual, userName), 
				sGestor.getTableroOponente(partidaActual, userName));
	}	
	
	/**
	 * Realiza un disparo
	 * @throws RemoteException
	 */
	private static void realizarDisparo() throws RemoteException {
		boolean disparoValido = false;
		do {
			System.out.println("Introduzca coordenadas de tiro [YX]: ");
			String inp = Gui.read().toUpperCase();
			if (inp.equals("ME_RINDO")) {
				sGestor.rendirJugador(partidaActual, userName);
				disparoValido = true;
			} else {
				try {
					Disparo d = new Disparo(userName, inp);
					sGestor.realizarDisparo(d, userName, partidaActual);
					disparoValido = true;
				} catch (Exception e) {
					System.out.println("Coordenadas no validas: " + e.getMessage() + "\n");
				}
			}
		} while(!disparoValido);
	}
	
	/**
	 * Muestra la puntuacion de la partida
	 * @throws RemoteException
	 */
	private static void showPuntuacion() throws RemoteException {
		int p = sGestor.getPuntuacion(partidaActual, userName);
		System.out.println("Puntuacion de la partida: " + p);
		System.out.println();
		Gui.waitForEnter();
	}
}
