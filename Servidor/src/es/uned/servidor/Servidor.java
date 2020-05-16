package es.uned.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import es.uned.common.Gui;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.Utils;

/**
 * Clase que contiene el main de la entidad "Servidor"
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class Servidor {
	
	private static int puertoAutenticacion = 5002;
	private static int puertoGestor = 5003;
	private static String urlRegistroAutenticacion;
	private static String urlRegistroGestor;
	private static ServicioAutenticacionImpl autenticacionExportado;
	private static ServicioGestorImpl gestorExportado;
	
	public static void main(String[] args) throws Exception {

		try {
			Utils.setCodeBase(ServicioAutenticacionInterface.class);
			Utils.setCodeBase(ServicioGestorImpl.class);
			
			// Inicio del servicio autenticacion
			autenticacionExportado = new ServicioAutenticacionImpl();
			LocateRegistry.createRegistry(puertoAutenticacion);
			urlRegistroAutenticacion = "rmi://localhost:" + puertoAutenticacion + "/Autenticacion";
			Naming.rebind(urlRegistroAutenticacion, autenticacionExportado);
			
			// Inicio del servicio gestor
			gestorExportado = new ServicioGestorImpl();
			LocateRegistry.createRegistry(puertoGestor);
			urlRegistroGestor = "rmi://localhost:" + puertoGestor + "/Gestor";
			Naming.rebind(urlRegistroGestor, gestorExportado);
			
			Gui.printServidor();
			menuInicial();
			
		} 
		catch (RemoteException ex) {
			System.out.println("Error de comunicacion: " + ex.toString());
			exit();
		}
		catch (Exception ex) {
			System.out.println("Excepcion en servidor: " + ex.toString());
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
		String[] opciones = new String[] { "Informacion del Servidor",
											"Estado de las partidas que se estan jugando en este momento",
											"Salir "};
		
		do {
			opt = Gui.menu(" Menu  ", opciones); 
			switch (opt) {
				case 0: showInfo(); break;
				case 1: showEstadoPartidas(); break;
				case 2: exit();
			}
		}
		while (opt != 3);
	}
	
	/**
	 * Muestra informacion del servidor
	 */
	private static void showInfo() {
		Gui.printServidor();
		System.out.println("Informacion del servidor:");
		System.out.println(" - Servicio de autenticacion:");
		System.out.println("    " + urlRegistroAutenticacion);
		System.out.println(" - Servicio gestor:");
		System.out.println("    " + urlRegistroGestor);
		System.out.println();
	}
	
	/**
	 * Muestra en pantalla el estado de las partidas en curso
	 * @throws RemoteException
	 */
	private static void showEstadoPartidas() throws RemoteException {
		Gui.printServidor();
		gestorExportado.printPartidas();
	}
	
	/**
	 * Sale de la aplicacion y desregistra los objetos
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 */
	private static void exit() throws RemoteException, NotBoundException, MalformedURLException {
		Naming.unbind(urlRegistroAutenticacion);
		Naming.unbind(urlRegistroGestor);
		UnicastRemoteObject.unexportObject(autenticacionExportado, false);
		UnicastRemoteObject.unexportObject(gestorExportado, false);
		System.exit(1);
	}

}
