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
 * Java RMI server, creating the local host server on port 8001 with name Server1.
 * 
 *
 */
public class Server4 extends java.rmi.server.UnicastRemoteObject  {
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
	    	Server4 server = new Server4(); 

	        java.rmi.registry.LocateRegistry.createRegistry(8004);
	        Naming.rebind("rmi://localhost:8004/Server4", server);
	        
	      } catch (Exception e) {
	        System.out.println("Trouble: " + e);
	      }

    System.out.println("Server 4 waiting.");
  }
}