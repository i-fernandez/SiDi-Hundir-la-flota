package es.uned.basededatos;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import es.uned.common.Gui;
import es.uned.common.Utils;

/**
 * Clase que contiene el main de la entidad "Base de datos"
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class Basededatos {
	
	private static int puertoDatos = 5001;
	private static String urlRegistroDatos;
	private static ServicioDatosImpl datosExportado;

	public static void main(String[] args) throws Exception {
		
		try {
			Utils.setCodeBase(ServicioDatosImpl.class);
			
			// Inicio del servicio datos
			datosExportado = new ServicioDatosImpl();
			LocateRegistry.createRegistry(puertoDatos);
			urlRegistroDatos = "rmi://localhost:" + puertoDatos + "/Datos";
			Naming.rebind(urlRegistroDatos, datosExportado);	

			Gui.printDatos();
			menuInicial();
		} 
		catch (RemoteException ex) {
			System.out.println("Error de comunicacion: " + ex.toString());
			exit();
		}
		catch (Exception ex) {
			System.out.println("Excepcion en base de datos: " + ex.toString());
			exit();
		}
	}
	
	/**
	 * Muestra el menu inicial
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 */
	private static void menuInicial() throws RemoteException, NotBoundException, MalformedURLException {
		int opt = 0;
		String[] opciones = new String[] { "Informacion de la Base De Datos",
											"Lista de jugadores registrados",
											"Salir "};
		
		do {
			opt = Gui.menu(" Menu ", opciones); 
			switch (opt) {
				case 0: showInfo(); break;
				case 1: showListaJugadores(); break;
				case 2: exit();
			}
		}
		while (opt != 3);
	}
	
	/**
	 * Muestra informacion de la base de datos
	 */
	private static void showInfo() {
		Gui.printDatos();
		System.out.println("Informacion de la base de datos:");
		System.out.println(urlRegistroDatos);
		System.out.println();
	}
	
	/**
	 * Muestra en pantalla la relacion jugadores / puntuaciones
	 */
	private static void showListaJugadores() {
		Gui.printDatos();
		datosExportado.printPuntuaciones();
	}
	
	/**
	 * Sale de la aplicacion y desregistra los objetos
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 */
	private static void exit() throws RemoteException, NotBoundException, MalformedURLException {
		Naming.unbind(urlRegistroDatos);
		UnicastRemoteObject.unexportObject(datosExportado, false);
		System.exit(1);
	}
}
