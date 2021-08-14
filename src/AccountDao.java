import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * Data access object for performing CRUD operation for transaction to a given
 * user.
 *
 */
public interface AccountDao extends Remote{

	boolean deposit(String accountNumber, double balance) throws RemoteException ;
	
	double withDraw(String accountNumber, double balance) throws RemoteException;
	
	Account getBalance(String accountNumber) throws RemoteException;
}
