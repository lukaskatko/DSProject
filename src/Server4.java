import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
/**
 * Java RMI server, creating the local host server on port 8001 with name Server1.
 * 
 *
 */
public class Server4 {	

  public static void main(String args[]) throws RemoteException {
		try {
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server4"));
			AccountDao server = new AccountDaoImpl(4,port);			
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("Server4", server);	
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