package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface del servicio de autenticacion
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public interface ServicioGestorInterface extends Remote {
	
	/**
	 * Consulta la puntuacion de un jugador
	 * @param username Nombre de usuario
	 * @return Puntuacion del jugador
	 * @throws RemoteException
	 */
	public int getPuntuacionJugador(String username) throws RemoteException;
	
	/**
	 * Crea una partida nueva
	 * @param username Nombre del primer jugador
	 * @return id de la partida
	 * @throws RemoteException
	 */
	public int crearPartida(String username) throws RemoteException, InterruptedException;
	
	/**
	 * Devuelve las partidas almacenadas
	 * @return Partidas almacenadas
	 * @throws RemoteException
	 */
	public List<PartidaInterface> getPartidasCreadas() throws RemoteException;
	
	/**
	 * Comprueba si existe una partida creada
	 * @param id id de partida
	 * @return true si la partida existe
	 * @throws RemoteException
	 */
	public boolean existePartidaCreada(int id) throws RemoteException;
	
	/**
	 * Une a un jugador a una partida existente
	 * @param username Nombre de usuario
	 * @param idPartida id de partida
	 * @throws RemoteException
	 */
	public void unirPartida(String username, int idPartida) throws RemoteException;
	
	/**
	 * Coloca un barco en el tablero
	 * @param coordenada Coordenada del barco incluida orientacion [XYZ] 
	 * @param username Nombre de usuario
	 * @param idPartida id de la partida
	 * @return true si la coordenada es valida
	 * @throws RemoteException
	 */
	public boolean colocacionBarco(String coordenada, String username, int idPartida) throws RemoteException;
	
	/**
	 * Realiza un disparo sobre el tablero
	 * @param disparo Disparo realizado
	 * @param username Nombre de usuario que realiza el disparo
	 * @param idPartida id de la partida
	 * @throws RemoteException
	 */
	public void realizarDisparo(Disparo disparo, String username, int idPartida) throws RemoteException;
	
	/**
	 * Devuelve el tablero propio (disparos recibidos)
	 * @param idPartida id de la partida
	 * @param username Nombre de usuario
	 * @return tablero
	 * @throws RemoteException
	 */
	public char[][] getTableroPropio(int idPartida, String username) throws RemoteException;
	
	/**
	 * Devuelve el tablero del oponente (disparos efectuados)
	 * @param idPartida id de la partida
	 * @param username Nombre de usuario
	 * @return tablero
	 * @throws RemoteException
	 */
	public char[][] getTableroOponente(int idPartida, String username) throws RemoteException;
	
	/**
	 * El jugador se rinde
	 * @param idPartida id de la partida
	 * @param username Nombe de usuario que se rinde
	 * @throws RemoteException
	 */
	public void rendirJugador(int idPartida, String username) throws RemoteException;
	
	/**
	 * Devuelve la puntuacion de un jugador en la partida
	 * @param idPartida id de la partida
	 * @param username Nombre de usuario
	 * @return
	 * @throws RemoteException
	 */
	public int getPuntuacion(int idPartida, String username) throws RemoteException;

}
