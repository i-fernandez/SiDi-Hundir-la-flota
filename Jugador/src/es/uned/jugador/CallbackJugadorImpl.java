package es.uned.jugador;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import es.uned.common.CallbackJugadorInterface;
import es.uned.common.TipoEvento;
import es.uned.common.ListaEventos;

/**
 * Implemantacion del callback de jugador
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class CallbackJugadorImpl extends UnicastRemoteObject implements CallbackJugadorInterface {
	
	private static final long serialVersionUID = -1586019456539959233L;
	ListaEventos eventos = new ListaEventos();
	
	public CallbackJugadorImpl(ListaEventos lista) throws RemoteException {
		this.eventos = lista;
	}
	
	@Override
	public void escribeEvento(TipoEvento evt) throws RemoteException {
		this.eventos.AñadirEvento(evt);
	}
}
