package es.uned.basededatos;

import es.uned.common.CallbackJugadorInterface;
import es.uned.common.EstadoPartida;
import es.uned.common.Gui;
import es.uned.common.PartidaInterface;
import es.uned.common.ServicioDatosInterface;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementacion del servicio de datos
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface {

	private static final long serialVersionUID = -5651881820692928452L;
	
	// Colecciones de datos
	private Map<String, String> jugadoresRegistrados;
	private Map<String, CallbackJugadorInterface> jugadoresConectados;
	private Map<String, Integer> puntuaciones;
	private List<PartidaInterface> partidas;
	
	public  ServicioDatosImpl() throws RemoteException {
		jugadoresRegistrados = new HashMap<String, String>();
		jugadoresConectados = new HashMap<String, CallbackJugadorInterface>();
		puntuaciones = new HashMap<String, Integer>();
		partidas = new ArrayList<PartidaInterface>();
	}
	
	@Override
	public boolean registro (String username, String password) throws RemoteException {
		if (jugadoresRegistrados.containsKey(username))  return false;
				
		jugadoresRegistrados.put(username, password);
		puntuaciones.put(username, 0);
		return true;
	}
	
	@Override
	public boolean comprobarCredenciales(String username, String password) throws RemoteException {
		return (jugadoresRegistrados.containsKey(username) && 
				jugadoresRegistrados.get(username).equals(password));
	}
	
	@Override
	public void conectarJugador(String username, CallbackJugadorInterface jugador) throws RemoteException {
		if (jugadoresRegistrados.containsKey(username)) {
			jugadoresConectados.put(username, jugador);
		}
	}
	
	@Override
	public boolean jugadorConectado(String username) throws RemoteException {
		return jugadoresConectados.containsKey(username);
	}
	
	
	@Override
	public void desconectarJugador(String username) throws RemoteException {
		if (jugadoresConectados.containsKey(username)) {
			jugadoresConectados.remove(username);
		}
	}
	
	@Override
	public int getPuntuacionJugador(String username) throws RemoteException {
		return puntuaciones.get(username);
	}
	
	@Override
	public void sumaPuntuacion(String username, int puntuacion) throws RemoteException {
		int currentp = puntuaciones.get(username);
		puntuaciones.replace(username, currentp + puntuacion);
	}
	
	@Override
	
	public void nuevaPartida(PartidaInterface p) throws RemoteException {
		partidas.add(p);
	}
	
	@Override
	public PartidaInterface getPartida(int idPartida) throws RemoteException {
		for (PartidaInterface p : partidas) {
			if (p.getId() == idPartida)
				return p;
		}
		return null;
	}
	
	@Override
	public List<PartidaInterface> getPartidas(EstadoPartida estado) throws RemoteException {
		List<PartidaInterface> p = new ArrayList<PartidaInterface>();
		for (PartidaInterface item : partidas) {
			if (item.getEstado() == estado) p.add(item);
		}
		return p;
	}
	
	@Override
	public CallbackJugadorInterface getCallbackObjeto(String username) throws RemoteException {
		return jugadoresConectados.get(username);
	}
	
	/**
	 * Muestra en pantalla la relacion jugadores / puntuaciones actuales
	 */
	protected void printPuntuaciones() {
		int d = 30;
		System.out.println("======  JUGADORES  REGISTRADOS  ======");
		for (String item : puntuaciones.keySet()) {
			int p = puntuaciones.get(item);
			int nDots = d - item.length() - Integer.toString(p).length();
			System.out.print(item);
			Gui.printChar('.', nDots);
			System.out.print(p + " puntos. \n");
		}
		System.out.println();
	}
}
