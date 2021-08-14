import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Java RMI server, creating the local host server on port 8001 with name
 * Server1.
 * 
 *
 */
public class Server1 {
	public final int serverNumber;
	public final int portNumber;

	public Server1() throws RemoteException {
		serverNumber = 1;
		portNumber = 8001;
	}

	public int getServerNum() {
		return serverNumber;
	}

	public int getPortNum() {
		return portNumber;
	}

	public static void main(String args[]) throws RemoteException {
		try {
			AccountDao server = new AccountDaoImpl(1);
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server1"));
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("Server1", server);	

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
		
	}
}