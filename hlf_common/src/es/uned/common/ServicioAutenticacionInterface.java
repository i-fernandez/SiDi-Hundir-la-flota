package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface del servicio de autenticacion
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public interface ServicioAutenticacionInterface extends Remote {

	/**
	 * Registra un nuevo usuario en el sistema
	 * @param username Nombre de usuario
	 * @param password Contraseña
	 * @return true si el registro ha sido satisfactorio
	 * @throws RemoteException
	 */
	public boolean registro(String username, String password) throws RemoteException;
	
	/**
	 * Realiza el inicio de sesion para un jugador 
	 * @param username Nombre de usuario
	 * @param password Contraseña
	 * @param jugador Objeto de callback
	 * @return true si ha sido satisfactorio
	 * @throws RemoteException
	 */
	public boolean login(String username, String password, CallbackJugadorInterface jugador) throws RemoteException;
	
	/**
	 * Realiza el cierre de sesion para un jugador
	 * @param username Nombre de usuario
	 * @throws RemoteException
	 */
	public void logout (String username) throws RemoteException;
}
