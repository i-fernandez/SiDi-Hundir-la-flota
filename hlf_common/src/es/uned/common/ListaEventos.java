package es.uned.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una lista de eventos sincronizada
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class ListaEventos {

	private List <TipoEvento> lista;
	
	public ListaEventos() {
		 lista= new ArrayList<TipoEvento>();
	}
	
	/**
	 * Devuelve el numero de elementos pendientes de la lista
	 * @return Numero de elementos pendientes
	 */
	public synchronized int NdatosPendientes() {
		return (lista.size());
	}
	   
	/**
	 * Añade un nuevo evento a la lista
	 * @param evento Evento a añadir
	 */
	public synchronized void AñadirEvento(TipoEvento evento) {
		lista.add(evento);
		notifyAll();
	}
	
	/**
	 * Obtiene el siguiente evento de la lista, espera si está vacia
	 * @return Evento
	 * @throws InterruptedException
	 */
	public synchronized TipoEvento ObtenerEvento() throws InterruptedException {
		if (lista.size()==0)
			wait();
	    TipoEvento evento = lista.get(0);
	    lista.remove(0);
	    return evento;
	} 

}
