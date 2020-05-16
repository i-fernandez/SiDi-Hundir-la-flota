package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaz del servicio de datos
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public interface ServicioDatosInterface extends Remote {
	
	/**
	 * Registra un nuevo jugador en el sistema
	 * @param username Nombre del jugador
	 * @param password Contraseña
	 * @throws RemoteException
	 */
	public boolean registro(String username, String password) throws RemoteException;
	
	/**
	 * Almacena la tupla usuario / objeto callback
	 * @param username Usuario
	 * @param jugador Objeto Callback
	 * @throws RemoteException
	 */
	public void conectarJugador(String username, CallbackJugadorInterface jugador) throws RemoteException;
	
	/**
	 * Comprueba si un jugador esta conectado
	 * @param username Nombre de usuario
	 * @return true si el jugador esta conectado
	 * @throws RemoteException
	 */
	public boolean jugadorConectado(String username) throws RemoteException;
	
	/**
	 * Devuelve true si las credenciales son correctas
	 * @param username Usuario
	 * @param password Contraseña
	 * @return true si las credenciales con correctas
	 * @throws RemoteException
	 */
	public boolean comprobarCredenciales(String username, String password) throws RemoteException; 
	
	/**
	 * Desregistra a un jugador
	 * @param username Usuario
	 * @throws RemoteException
	 */
	public void desconectarJugador(String username) throws RemoteException;
	
	/**
	 * Consulta la puntuacion de un jugador
	 * @param username Usuario
	 * @return Puntuacion
	 * @throws RemoteException
	 */
	public int getPuntuacionJugador(String username) throws RemoteException;
	
	/**
	 * Añade una puntuacion al jugador
	 * @param username Usuario
	 * @param puntuacion Puntuacion a añadir
	 * @throws RemoteException
	 */
	public void sumaPuntuacion(String username, int puntuacion) throws RemoteException;
	
	/**
	 * Añade una partida a la lista de partidas
	 * @param partida Partida a añadir
	 * @throws RemoteException
	 */
	public void nuevaPartida(PartidaInterface partida) throws RemoteException;
	
	/**
	 * Devuelve la partida que coincide con el parametro
	 * @param idPartida Identificador de partida
	 * @return Partida
	 * @throws RemoteException
	 */
	public PartidaInterface getPartida(int idPartida) throws RemoteException;
	
	/**
	 * Devuelve todas las partidas que tengan un estado concreto
	 * @param estado Estado de la partida
	 * @return Partidas con el estado buscado
	 * @throws RemoteException
	 */
	public List<PartidaInterface> getPartidas(EstadoPartida estado) throws RemoteException;
	
	/**
	 * Devuelve el objeto de callback de un jugador
	 * @param username Usuario
	 * @return Objeto callback del usuario
	 * @throws RemoteException
	 */
	public CallbackJugadorInterface getCallbackObjeto(String username) throws RemoteException;

}
