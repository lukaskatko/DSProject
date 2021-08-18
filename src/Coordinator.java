import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Coordinator to handle server coordination. This class get the ports number
 * from the user to run 4 instances of the server. It sets the ports number of
 * the other servers to each server, this is required to sync the request
 * between servers. This also handles sending commit to other servers when the
 * initial server issues the command.
 *
 */
public class Coordinator {

	static Map<String, String> serverMap = new HashMap<>();

	public Coordinator() {
		Util util = new Util();
		serverMap = util.getPropValue();
	}

	/**
	 * driver method to get port and register remote interface to a port.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		Coordinator ct = new Coordinator();
	}

	/**
	 * Method to publish client request to other server, this is the initial request
	 * sent to the other servers. These servers receive the requests and add the
	 * client request to a temp storage. Method responds with the port number, these
	 * are the port numbers of the server that effectively added the client request
	 * to the temporary storage. These port numbers act as the ACK message for the
	 * initial request from the requesting server. * @param currentPort, port number
	 * of the current server.
	 * 
	 * @return the ack message which is the port number of the successful servers
	 * 
	 */
	public List<Integer> publishToOtherServers(int currentPort) {
		List<Integer> tempList = new ArrayList<Integer>();

		for (String entry : serverMap.keySet()) {
			final int port = Integer.parseInt(serverMap.get(entry));
			if (port != currentPort) {
				try {
					Registry registry = LocateRegistry.getRegistry(port);
					AccountDao stub = (AccountDao) registry.lookup(entry);
					tempList.add(stub.requestToPublishAtServer());
				} catch (Exception e) {
					System.out.println("publish to other server failed on port " + port);
					System.out.println(e.getMessage());
				}
			}
		}

		return tempList;
	}

	/**
	 * 
	 * Method to request other servers to move the client request from temp storage
	 * to a permanent DB. These port numbers act as a ACK message for the GO message
	 * for the server requesting to sync.
	 * 
	 * @param currentPort,  port of the current server
	 * @param action        (withdraw or deposit)
	 * @param accountNumber
	 * @param userName
	 * @param amount
	 * @return
	 */
	public static List<Integer> proceedToCommit(int currentPort, String action, long accountNumber, String userName,
			Double amount) {
		List<Integer> tempList = new ArrayList<Integer>();
		for (String entry : serverMap.keySet()) {
			final int port = Integer.parseInt(serverMap.get(entry));

			if (port != currentPort)
				try {
					Registry registry = LocateRegistry.getRegistry(port);
					AccountDao stub = (AccountDao) registry.lookup(entry);

					if (action == "D") {
						if (stub.depositFor2PC(accountNumber, userName, amount))
							tempList.add(port);
					} else if (action == "W") {
						if (stub.withDrawFor2PC(accountNumber, userName, amount) > 0.0)
							tempList.add(port);
					}
				} catch (Exception e) {
					System.out.println("proceedToCommit to other server failed on port " + currentPort);
					System.out.println(e.getMessage());
				}
		}
		return tempList;
	}

	/**
	 * Method to request other servers to move the client request to permanent
	 * storage. These port numbers act as a ACK message for the GO message for the
	 * server requesting to sync.
	 * 
	 * @param currentPort
	 * @param userName
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public static List<Integer> proceedToCommitUser(int currentPort, String userName, String firstName,
			String lastName) {
		List<Integer> tempList = new ArrayList<Integer>();
		for (String entry : serverMap.keySet()) {
			final int port = Integer.parseInt(serverMap.get(entry));

			if (port != currentPort)
				try {
					Registry registry = LocateRegistry.getRegistry(port);
					AccountDao stub = (AccountDao) registry.lookup(entry);

					stub.createUserFor2PC(firstName, lastName, userName);
				} catch (Exception e) {
					System.out.println("proceedToCommit to other server failed on port " + currentPort);
					System.out.println(e.getMessage());
				}
		}
		return tempList;
	}
}