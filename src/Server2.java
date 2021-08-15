import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

/**
 * Java RMI server, creating the local host server on port 8002 with name
 * Server2.
 * 
 *
 */
public class Server2 {	

	public static void main(String args[]) throws RemoteException {
		try {
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server2"));
			AccountDao server = new AccountDaoImpl(2,port);			
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("Server2", server);	
			AccountDao nextServer = util.getActiveServer(port);
			Map<Long, Account> sourceList = server.getRecords();
			Map<Long, Account> otherList = nextServer.getRecords();
			List<Account> updatedList = util.getUpdatedRecords(sourceList, otherList);
			server.replicateData(updatedList);
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}
}