import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Java RMI server, creating the local host server on port 8001 with name
 * Server1.
 * 
 *
 */
public class Server3 {
	
	

	public static void main(String args[]) throws RemoteException {
		try {
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server3"));
			AccountDao server = new AccountDaoImpl(3,port);
			
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("Server3", server);	

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}

		System.out.println("Server 1 waiting.");
	}
}