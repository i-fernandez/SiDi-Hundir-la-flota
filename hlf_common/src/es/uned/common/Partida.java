package es.uned.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase que representa una partida
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class Partida extends UnicastRemoteObject implements PartidaInterface {

	private static final long serialVersionUID = 1L;
	private static int seed = new Random().nextInt(1000);
	
	private int idPartida;
	private EstadoPartida estado;
	private Player jugador1, jugador2;
	private List<Disparo> listaDisparos;
	
	/**
	 * Constructor
	 * @param jugador Jugador que crea la partida
	 * @throws RemoteException
	 */
	public Partida(String jugador) throws RemoteException{
		idPartida = seed++;
		jugador1 = new Player(jugador); 
		estado = EstadoPartida.CREADA;
		listaDisparos = new ArrayList<Disparo>();
	}
	
	@Override
	public int getId() throws RemoteException {
		return this.idPartida;
	}
	
	@Override
	public EstadoPartida getEstado() throws RemoteException {
		return estado;
	}
	
	@Override
	public String getJugador1() throws RemoteException {
		return jugador1.getUsername();
	}
	
	@Override
	public String getJugador2() throws RemoteException {
		return jugador2.getUsername();
	}
	
	@Override
	public String getNombreOponente(String username) throws RemoteException {
		return getOponente(username).getUsername();
	}
	
	@Override
	public void unirJugador(String jugador) throws RemoteException {
		jugador2 = new Player(jugador);
		estado = EstadoPartida.EN_CURSO;
	}
	
	@Override
	public boolean colocarBarco(String coordenada, String username) throws RemoteException {
		return getJugador(username).colocarBarco(coordenada);
	}
	
	@Override
	public char[][] getTableroPropio(String username) throws RemoteException {
		return getJugador(username).getTableroPropio();
	}
	
	@Override
	public char[][] getTableroOponente(String username) throws RemoteException {
		return getJugador(username).getTableroOponente();
	}
	
	@Override
	public boolean barcosColocados() throws RemoteException {
		return (jugador1.barcosColocados() && jugador2.barcosColocados());
	}
	
	@Override
	public int getPuntuacion(String username) throws RemoteException {
		return getJugador(username).getPuntuacion();
	}
	
	@Override
	public void rendirJugador(String username) throws RemoteException {
		getJugador(username).rendirse();  
		getOponente(username).rendirOponente();
		estado = EstadoPartida.FINALIZADA;
	}
	
	@Override
	public ResultadoDisparo realizarDisparo(Disparo disparo, String username) 
			throws RemoteException {
		Player j = getJugador(username);
		ResultadoDisparo r = getOponente(username).recibirDisparo(disparo);
		j.realizarDisparo(disparo, r);
		Disparo ds = disparo;
		ds.setResultado(r);
		listaDisparos.add(ds);
		// Suma la puntuacion
		if (r == ResultadoDisparo.TOCADO || 
				r == ResultadoDisparo.HUNDIDO ||
				r == ResultadoDisparo.GANADOR) j.sumaPuntuacion(1);
		if (r == ResultadoDisparo.GANADOR) {
			j.sumaPuntuacion(10);
			estado = EstadoPartida.FINALIZADA;
		}
		return r;
	}
	
	/**
	 * Devuelve el objeto Jugador a partir del nombre de usuario
	 * @param username Nombre de usuario
	 * @return Objeto jugador del usuario
	 */
	private Player getJugador(String username) {
		if (jugador1.getUsername().equals(username)) {
			return jugador1;
		} else {
			return jugador2;
		}
	}
	
	/**
	 * Devuelve el objeto Jugador del rival
	 * @param username Nombre de usuario
	 * @return Objeto Jugador del rival
	 */
	private Player getOponente(String username) {
		if (jugador1.getUsername().equals(username)) {
			return jugador2;
		} else {
			return jugador1;
		}
	}
}
