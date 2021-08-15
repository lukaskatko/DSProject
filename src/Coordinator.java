
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * Coordinator to handle server coordination. This class get the ports number
 * from the user to run 4 instances of the server. It sets the ports number of
 * the other servers to each server, this is required to sync the request
 * between servers. This class also handles publishing to other server,
 * publishing to one server in case the initial publish is missed by a particular
 * server. This also handles sending commit to other servers when the initial
 * server issues the command and supports committing to individual server to
 * handle failures.
 *
 */
public class Coordinator  {

	static HashMap<String, Integer> serverMap = new HashMap<>();

	/**
	 * driver method to get port and register remote interface to a port.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception 
	{
		ServerMap serverMapClass = new ServerMap();
		serverMap = serverMapClass.getServerConfig();	
	}

	/**
	 * Method to publish client request to other server, this is the initial request
	 * sent to the other servers. These servers receive the requests and add the
	 * client request to a temp storage. Method responds with the port number, these
	 * are the port numbers of the server that effectively added the client request
	 * to the temporary storage. These port numbers act as the ACK message for the
	 * initial request from the requesting server.
	 * 
	 * @param currentPort, port number of the current server.
	 * @param message,     client message
	 * @return the ack message which is the port number of the successful servers
	 *         that stored the client request in a temp storage.
	 */
	public List<Integer> publishToOtherServers(int currentPort) 
	{
		List<Integer> tempList = new ArrayList<Integer>();
		for(String entry: serverMap.keySet())
		{
			final int port = serverMap.get(entry);
			if (port != currentPort)
			{
				try 
				{
					Registry registry = LocateRegistry.getRegistry(port);
					AccountDao stub = (AccountDao) registry.lookup(entry);
					tempList.add(stub.requestToPublishAtServer());
				} 
				catch (Exception e) 
				{
					System.out.println("publish to other server failed on port " + port);
					System.out.println(e.getMessage());
				}
			}		
		}
		
		return tempList;
	}

	/**
	 * Method to remove the client message from all servers in case the transaction
	 * fails in one of the servers.
	 * 
	 * @param currentPort, port number of the current server.
	 * @param message      from client.
	 */
//	public static void removeMessageFromTemp(int currentPort, String message) {
//
//		for (int i = 0; i < ports.length; i++) {
//			if (ports[i] != currentPort)
//				try {
//					Registry registry = LocateRegistry.getRegistry(ports[i]);
//					ServerInterface stub = (ServerInterface) registry.lookup("ServerInterface");
//					stub.removeMessageFromTemp(message);
//				} catch (Exception e) {
//					System.out.println("removeMessageFromTemp to other server failed on port " + ports[i]);
//					System.out.println(e.getMessage());
//
//				}
//		}
//
//	}

	/**
	 * Method to publish client message to missing server, in case the intial
	 * publish fails on any of the server.
	 * 
	 * @param currentPort of the current server.
	 * @param message     is client message
	 * @return port of the number of the other server in case publish is successful
	 */
	public int publishToMissingServer(int currentPort, String message) {

		int successPort = 0;
		try {
			Registry registry = LocateRegistry.getRegistry(currentPort);
			ServerInterface stub = (ServerInterface) registry.lookup("ServerInterface");
			successPort = stub.populateTempStorage(message);
		} catch (Exception e) {
			System.out.println("publishToMissingServer to other server failed on port " + currentPort);
			System.out.println(e.getMessage());
		}

		return successPort;
	}

	/**
	 * Method to commit client message to a missing server in case the intial commit
	 * to this server fails. This is to ensure that another try is done.
	 * 
	 * @param currentPort, current port of the server
	 * @param message      is the client message.
	 * @return portNumber
	 */
	public int commitMissingServer(int currentPort, String message) {

		int successPort = 0;
		try {
			Registry registry = LocateRegistry.getRegistry(currentPort);
			ServerInterface stub = (ServerInterface) registry.lookup("ServerInterface");
			successPort = stub.commit(message);
		} catch (Exception e) {
			System.out.println("commitMissingServer to other server failed on port " + currentPort);
			System.out.println(e.getMessage());
		}

		return successPort;
	}

	/**
	 * Method to request other servers to move the client request from temp storage
	 * to a permanent DB or file. This method returns the port number of the servers
	 * that moved the client request to permanent storage. These port numbers act as
	 * a ACK message for the GO message for the server requesting to sync.
	 * 
	 * @param currentPort is the port number of the server requesting to commit
	 * @param message     is the client message
	 * @return the list of port numbers of the server that succesfully committed.
	 */
	public static List<Integer> proceedToCommit(int currentPort, String action, long accountNumber, String userName, Double amount) 
	{
		List<Integer> tempList = new ArrayList<Integer>();
		for (String entry: serverMap.keySet()) 
		{
			final int port = serverMap.get(entry);

			if (port != currentPort)
			try 
			{
				Registry registry = LocateRegistry.getRegistry(port);
				AccountDao stub = (AccountDao) registry.lookup(entry);
				
				if(action=="D")
				{
					if(stub.depositFor2PC(accountNumber, userName, amount))
						tempList.add(port) ;
				}
				else if(action == "W")
				{
					if(stub.withDrawFor2PC(accountNumber, userName, amount) > 0.0)
						tempList.add(port) ;
				}
			}
			catch (Exception e) 
			{
				System.out.println("proceedToCommit to other server failed on port " + currentPort);
				System.out.println(e.getMessage());
			}
		}
		return tempList;
	}
}
