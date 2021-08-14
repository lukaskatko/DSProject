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
	
	
	public static void main(String args[]) throws RemoteException {
		try {
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server1"));
			AccountDao server = new AccountDaoImpl(1, port);
			
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("Server1", server);	

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
		
	}
}