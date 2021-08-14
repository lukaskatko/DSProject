import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Java RMI server, creating the local host server on port 8001 with name
 * Server1.
 * 
 *
 */
public class Server2 {
	public final int serverNumber;
	public final int portNumber;

	public Server2() throws RemoteException {
		serverNumber = 2;
		portNumber = 8002;
	}

	public Integer getServerNum() {
		return serverNumber;
	}

	public int getPortNum() {
		return portNumber;
	}

	public static void main(String args[]) throws RemoteException {
		try {
			AccountDao server = new AccountDaoImpl(2);
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server2"));
			java.rmi.registry.LocateRegistry.createRegistry(port);
			Naming.rebind("Server2", server);

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}
}