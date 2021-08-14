import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Java RMI server, creating the local host server on port 8001 with name
 * Server1.
 * 
 *
 */
public class Server5 {
	

	public static void main(String args[]) throws RemoteException {
		try {
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server5"));
			AccountDao server = new AccountDaoImpl(5,port);
			
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("Server5", server);	

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}

		System.out.println("Server 1 waiting.");
	}
}