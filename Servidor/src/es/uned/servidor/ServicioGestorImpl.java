package es.uned.servidor;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import es.uned.common.CallbackJugadorInterface;
import es.uned.common.Disparo;
import es.uned.common.EstadoPartida;
import es.uned.common.Gui;
import es.uned.common.TipoEvento;
import es.uned.common.Partida;
import es.uned.common.PartidaInterface;
import es.uned.common.ResultadoDisparo;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;

/**
 * Implementacion del servicio gestor
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface {
	
	private static final long serialVersionUID = -172071003866986205L;
	private static int puertoDatos = 5001;
	private static ServicioDatosInterface sDatos;

	public ServicioGestorImpl() throws RemoteException {
		conexionDatos();
	}
	
	@Override
	public int getPuntuacionJugador(String username) throws RemoteException {
		return sDatos.getPuntuacionJugador(username);
	}
	
	@Override
	public int crearPartida(String username) throws RemoteException, InterruptedException {
		Partida partida = new Partida(username);
		sDatos.nuevaPartida(partida);
		CallbackJugadorInterface callback = sDatos.getCallbackObjeto(username);
		callback.escribeEvento(TipoEvento.PARTIDA_CREADA);		
		return partida.getId();
	}
	
	@Override
	public List<PartidaInterface> getPartidasCreadas() throws RemoteException {
		return sDatos.getPartidas(EstadoPartida.CREADA);
	}
	
	@Override
	public boolean existePartidaCreada(int id) throws RemoteException {
		PartidaInterface p = sDatos.getPartida(id);
		return (p != null && p.getEstado() == EstadoPartida.CREADA);
	}
	
	@Override
	public void unirPartida(String username, int idPartida) throws RemoteException {
		PartidaInterface p = sDatos.getPartida(idPartida);
		p.unirJugador(username);
		CallbackJugadorInterface callback1 = sDatos.getCallbackObjeto(p.getJugador1());
		callback1.escribeEvento(TipoEvento.JUGADOR_UNIDO);		
		CallbackJugadorInterface callback2 = sDatos.getCallbackObjeto(username);
		callback2.escribeEvento(TipoEvento.COLOCAR_BARCOS);
		callback1.escribeEvento(TipoEvento.COLOCAR_BARCOS);	
	}
	
	@Override
	public boolean colocacionBarco(String coordenada, String username, int idPartida) throws RemoteException {
		PartidaInterface partida = sDatos.getPartida(idPartida);
		boolean resultado = partida.colocarBarco(coordenada, username); 
		if (partida.barcosColocados()) {
			sDatos.getCallbackObjeto(partida.getJugador1()).escribeEvento(TipoEvento.JUEGO_INICIADO);
			sDatos.getCallbackObjeto(partida.getJugador2()).escribeEvento(TipoEvento.JUEGO_INICIADO);
		}
		return resultado;
	}
	
	@Override
	public void realizarDisparo(Disparo disparo, String username, int idPartida) throws RemoteException {
		PartidaInterface p = sDatos.getPartida(idPartida);
		ResultadoDisparo resultado = p.realizarDisparo(disparo, username);
		CallbackJugadorInterface cb_dispara = sDatos.getCallbackObjeto(username);
		CallbackJugadorInterface cb_recibe = sDatos.getCallbackObjeto(p.getNombreOponente(username));
		
		switch (resultado) {
		case TOCADO:
			cb_dispara.escribeEvento(TipoEvento.DISPARO_REALIZADO_TOCADO);
			cb_recibe.escribeEvento(TipoEvento.DISPARO_RECIBIDO_TOCADO);
			break;
		case HUNDIDO:
			cb_dispara.escribeEvento(TipoEvento.HAS_HUNDIDO_BARCO);
			cb_recibe.escribeEvento(TipoEvento.TE_HAN_HUNDIDO_BARCO);
			break;
		case GANADOR:
			String rival = p.getNombreOponente(username);
			sDatos.sumaPuntuacion(username, p.getPuntuacion(username));
			sDatos.sumaPuntuacion(rival, p.getPuntuacion(rival));
			cb_dispara.escribeEvento(TipoEvento.HAS_GANADO);
			cb_recibe.escribeEvento(TipoEvento.HAS_PERDIDO);
			break;
		case AGUA:
			cb_dispara.escribeEvento(TipoEvento.DISPARO_REALIZADO_AGUA);
			cb_recibe.escribeEvento(TipoEvento.DISPARO_RECIBIDO_AGUA);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void rendirJugador(int idPartida, String username) throws RemoteException {
		PartidaInterface p = sDatos.getPartida(idPartida);
		String rival = p.getNombreOponente(username);
		CallbackJugadorInterface cb_perdedor = sDatos.getCallbackObjeto(username);
		CallbackJugadorInterface cb_ganador = sDatos.getCallbackObjeto(rival);
		p.rendirJugador(username);
		sDatos.sumaPuntuacion(rival, p.getPuntuacion(rival));
		cb_ganador.escribeEvento(TipoEvento.RIVAL_RENDIDO);
		cb_perdedor.escribeEvento(TipoEvento.HAS_PERDIDO);
	}
	
	@Override
	public char[][] getTableroPropio(int idPartida, String username) throws RemoteException {
		return sDatos.getPartida(idPartida).getTableroPropio(username);
	}
	
	@Override
	public char[][] getTableroOponente(int idPartida, String username) throws RemoteException {
		return sDatos.getPartida(idPartida).getTableroOponente(username);
	}
	
	@Override
	public int getPuntuacion(int idPartida, String username) throws RemoteException {
		PartidaInterface p = sDatos.getPartida(idPartida);
		return p.getPuntuacion(username);
	}
	
	/**
	 * Realiza la conexion con el servicio de datos
	 */
	private void conexionDatos() {
		try {
			String URLRegistroDatos = "rmi://localhost:" + puertoDatos + "/Datos";
			sDatos = (ServicioDatosInterface) Naming.lookup(URLRegistroDatos);		
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		} 
	}
	
	/**
	 * Muestra en pantalla la lista de partidas en curso y su informacion
	 * @throws RemoteException
	 */
	protected void printPartidas() throws RemoteException {
		List<PartidaInterface> partidas = sDatos.getPartidas(EstadoPartida.EN_CURSO);
		if (partidas.size() == 0) {
			System.out.println("No hay partidas en juego en este momento.\n");
		} else {		
			for (PartidaInterface partida : partidas) {
				// Datos
				int id = partida.getId();			
				String j1 = partida.getJugador1();
				String j2 = partida.getJugador2();
				int p1 = partida.getPuntuacion(j1);
				int p2 = partida.getPuntuacion(j2);
				int nDots1 = 30 - j1.length() - Integer.toString(p1).length();
				int nDots2 = 30 - j2.length() - Integer.toString(p2).length();
				// Cabecera
				System.out.println();
				System.out.print("Partida: ");
				Gui.printChar(' ', 7 - Integer.toString(id).length());
				System.out.print(id + "\n");
				// Jugadores
				System.out.print(j1);
				Gui.printChar('.', nDots1);
				System.out.print(p1 + " puntos. \n");
				System.out.print(j2);
				Gui.printChar('.', nDots2);
				System.out.print(p2 + " puntos. \n");
			}
			System.out.println();
		}
	}
}

