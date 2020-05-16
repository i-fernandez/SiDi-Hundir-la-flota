package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface del callback de jugador
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public interface CallbackJugadorInterface extends Remote {
	
	/**
	 * Escribe un evento en la lista de eventos
	 * @param evt Evento a escribir
	 * @throws RemoteException
	 */
	public void escribeEvento(TipoEvento evt) throws RemoteException;
	
}
