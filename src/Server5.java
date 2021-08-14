import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

/**
 * Java RMI server, creating the local host server on port 8001 with name
 * Server1.
 * 
 *
 */
public class Server5 {
	public final int serverNumber;
	public final int portNumber;

	public Server5() throws RemoteException {
		serverNumber = 5;
		portNumber = 8005;
	}

	public Integer getServerNum() {
		return serverNumber;
	}

	public int getPortNum() {
		return portNumber;
	}

	public static void main(String args[]) throws RemoteException {
		try {
			AccountDao server = new AccountDaoImpl(5);
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server5"));
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("Server5", server);	

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}

		System.out.println("Server 1 waiting.");
	}
}