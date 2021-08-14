import java.rmi.Naming;
import java.rmi.RemoteException;



/**
 * Java RMI server, creating the local host server on port 8001 with name Server1.
 * 
 *
 */
public class Server4 {
	public final int serverNumber;
	public final int portNumber;


  public Server4() throws RemoteException {
	  serverNumber = 4;
	  portNumber = 8004;
  }
	
  public Integer getServerNum() {
	  return serverNumber;
  }
  
  public int getPortNum() {
	  return portNumber;
  }

  public static void main(String args[]) throws RemoteException {
		try {
			AccountDao server = new AccountDaoImpl(3);
			Util util = new Util();
			int port = Integer.parseInt(util.getPropValue().get("Server4"));
			java.rmi.registry.LocateRegistry.createRegistry(port);
			Naming.rebind("Server4", server);

		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}

		System.out.println("Server 1 waiting.");
	}
}