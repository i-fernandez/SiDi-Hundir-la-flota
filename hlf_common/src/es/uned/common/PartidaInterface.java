package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface de partida
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public interface PartidaInterface extends Remote {
	
	/**
	 * Devuelve el id de partida
	 * @return id de partida
	 * @throws RemoteException
	 */
	public int getId() throws RemoteException;
	
	/**
	 * Devuelve el estado de la partida
	 * @return Estado de partida
	 * @throws RemoteException
	 */
	public EstadoPartida getEstado() throws RemoteException;
	
	/**
	 * Devuelve el jugador 1
	 * @return Jugador 1
	 * @throws RemoteException
	 */
	public String getJugador1() throws RemoteException;
	
	/**
	 * Devuelve el jugador 2
	 * @return Jugador 2
	 * @throws RemoteException
	 */
	public String getJugador2() throws RemoteException;
	
	/**
	 * Devuelve el nombre del oponente de un jugador
	 * @param username Jugador a consultar
	 * @return Nombre del oponente
	 * @throws RemoteException
	 */
	public String getNombreOponente(String username) throws RemoteException;
	
	/**
	 * Añade un juador a una partida
	 * @param jugador Jugador que se une
	 * @throws RemoteException
	 */
	public void unirJugador(String jugador) throws RemoteException;
	
	/**
	 * Coloca un barco en el tablero
	 * @param coordenada Coordenada del barco [XYZ]
	 * @param username Usuario que coloca el barco
	 * @return true si la coordenada es valida
	 * @throws RemoteException
	 */
	public boolean colocarBarco(String coordenada, String username) throws RemoteException;
	
	/**
	 * Realiza un disparo 
	 * @param disparo Disparo a realizar
	 * @param username Jugador que realiza el disparo
	 * @return Resultado del disparo
	 * @throws RemoteException
	 */
	public ResultadoDisparo realizarDisparo(Disparo disparo, String username) throws RemoteException;
	
	/**
	 * Consulta si todos los barcos han sido colocados
	 * @return true si ambos jugadores han colocados sus barcos
	 * @throws RemoteException
	 */
	public boolean barcosColocados() throws RemoteException;
	
	/**
	 * Obtiene la puntuacion actual de un jugador
	 * @param username Jugador
	 * @return Puntuacion del jugador
	 * @throws RemoteException
	 */
	public int getPuntuacion(String username) throws RemoteException;
	
	/**
	 * Realiza la rendicion de un jugador
	 * @param username Jugador que se rinde
	 * @throws RemoteException
	 */
	public void rendirJugador(String username) throws RemoteException;
	
	/**
	 * Devuelve el tablero con los barcos del jugador
	 * @param username Jugador
	 * @return Tablero
	 * @throws RemoteException
	 */
	public char[][] getTableroPropio(String username) throws RemoteException;
	
	/**
	 * Devuelve el tablero con los disparos realizados
	 * @param username Jugador
	 * @return Tablero
	 * @throws RemoteException
	 */
	public char[][] getTableroOponente(String username) throws RemoteException;

}
