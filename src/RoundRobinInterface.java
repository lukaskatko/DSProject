import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the load balancing server.
 * @author alitz
 *
 */
public interface RoundRobinInterface extends Remote {

	/**
	 * getServer() returns the server the client will be interacting with, Server 1 through 5.
	 * Sequentially goes through the servers as clients connect.
	 * @return a String representing a server number.
	 * @throws RemoteException
	 */
	public String getServer() throws RemoteException;
}