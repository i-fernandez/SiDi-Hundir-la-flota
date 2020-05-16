package es.uned.servidor;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import es.uned.common.CallbackJugadorInterface;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;

/**
 * Implementacion del servicio de autenticacion
 * @author Israel Fernandez Gutierrez
 *         ifernande931@alumno.uned.es
 */
public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface {
	
	private static final long serialVersionUID = -8474559761888132253L;
	private static int puertoDatos = 5001;
	private static ServicioDatosInterface sDatos;
	
	public ServicioAutenticacionImpl() throws RemoteException {
		conexionDatos();
	}
	
	@Override
	public boolean registro(String username, String password) throws RemoteException {
		return sDatos.registro(username, password);
	}
	
	@Override
	public boolean login(String username, String password, CallbackJugadorInterface jugador) throws RemoteException {
		// Comprueba que el jugador no ha hecho login antes
		if (sDatos.jugadorConectado(username)) return false;
		
		if (sDatos.comprobarCredenciales(username, password)) {
			sDatos.conectarJugador(username, jugador);
			return true;
		}
		return false;
	}
	
	@Override
	public void logout(String username) throws RemoteException {
		sDatos.desconectarJugador(username);
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
	
}
