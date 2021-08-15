
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to handle 3 request types from client. aInterface extends
 * Remote interface and throws RemoteException.
 *
 */
public interface ServerInterface extends Remote {

	/**
	 * This method gets the client request and check the action, for GET action it
	 * calls the request handler to get the value of the key, for PUT and DELETE it
	 * populates the client request to a temp storage, next it publishes the client
	 * request to the other server asking them to move the client request to the
	 * temp storage Response for the initial publish is added to a list and this
	 * list is added to the ackStorage Vector. This method then check ACKs are
	 * received from all the server if it does not then it proceeds to do the
	 * rollback by removing message from the temp storage and asking the other
	 * server to do the same. It return the same message to the client. If the
	 * server receives initial ack from all the server then it tell the other
	 * servers to proceed to commit to a permanent store, responses from these Go is
	 * stored in a map this method checks to see if all the server ports are on it,
	 * if not then it issues a Go call again. Finally is moves the client message to
	 * a permanent storage.
	 *
	 * 
	 * @param message     for the request type
	 * @param requestType is the action
	 * @return success or fail message to client.
	 * @throws RemoteException      for handling remote actions
	 */
	public String handleRequest(String message, String requestType) throws RemoteException;

	/**
	 * Set the port for the server to make the sync or commit call to the other
	 * servers running on these ports
	 * 
	 * @param otherPorts, port of other servers.
	 * @param currentPort is the port of the current server
	 * @throws RemoteException for handling remote actions
	 */
	public void setPorts(int[] otherPorts, int currentPort) throws RemoteException;

	/**
	 * Get the current port of the server requesting the action.
	 * 
	 * @return the current port of the server
	 * @throws RemoteException for handling remote actions.
	 */
	public int getCurrentPort() throws RemoteException;

	/**
	 * Method for servers to populate client request to their temporary storage,
	 * this is required for the requesting server can send updates to the other
	 * server asking to sync client requests to their replica. Each server will send
	 * their port number if the client request is moved to temp storage. This port
	 * number is the ACK message to the server requesting other servers to add
	 * client request to the temp storage. This is the first leg of the transaction
	 * 
	 * @param message, client message
	 * @return return the port number.
	 * @throws RemoteException
	 */
	public int populateTempStorage(String message) throws RemoteException;

	/**
	 * This method moves the client request from the temp storage to the permanent
	 * store. Once the servers move the client request to the permanent store, they
	 * respond with their port number that server as the ACK to the GO call for the
	 * requesting server. once the calling server gets the ACKs from all the server
	 * it will proceed to commit.
	 * 
	 * @param message from client
	 * @return the port number
	 * @throws RemoteException
	 */
	public int commit(String message) throws RemoteException;

	/**
	 * This method removes the client request from the temp storage after it is
	 * written to permanent store.
	 * 
	 * @param message from client
	 * @throws RemoteException
	 */
	public void removeMessageFromTemp(String message) throws RemoteException;

}
