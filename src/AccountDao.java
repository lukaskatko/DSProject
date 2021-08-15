import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * Data access object for performing CRUD operation for transaction to a given
 * user.
 *
 */
public interface AccountDao extends Remote{
	
	long createUser(String firstName, String lastName, String userName) throws RemoteException;
	
	boolean deposit(long accountNumber, String userName, double amount) throws RemoteException ;
	boolean depositFor2PC(long accountNumber, String userName, double amount) throws RemoteException ;

	double withDraw(long accountNumber, String userName, double amount) throws RemoteException;
	double withDrawFor2PC(long accountNumber, String userName, double amount) throws RemoteException;

	Account getBalance(long accountNumber, String userName) throws RemoteException;

	Integer requestToPublishAtServer();
}
