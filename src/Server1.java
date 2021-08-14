import java.rmi.Naming;
import java.rmi.RemoteException;

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
			java.rmi.registry.LocateRegistry.createRegistry(port);
			Naming.rebind("Server1", server);

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}

		System.out.println("Server 1 waiting.");
	}
}