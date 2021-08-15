import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * 
 * Data access object for performing CRUD operation for transaction to a given
 * user.
 *
 */
public interface AccountDao extends Remote {

	/**
	 * Method to create a new user in the SQL Databases.
	 * @param firstName String
	 * @param lastName String
	 * @param userName String
	 * @return long representing the account number given to the new user
	 * @throws RemoteException
	 */
	long createUser(String firstName, String lastName, String userName) throws RemoteException;
	
	/**
	 * 2-Phase commit protocol helper method to create new user
	 * @param firstName String
	 * @param lastName String
	 * @param userName String
	 * @return long representing the account number given to the new user
	 * @throws RemoteException
	 */
	long createUserFor2PC(String firstName, String lastName, String userName) throws RemoteException;

	/**
	 * Deposit specified amount into specified users account.
	 * @param accountNumber long
	 * @param userName String
	 * @param amount double
	 * @return boolean representing if deposit is successful or unsuccessful.
	 * @throws RemoteException
	 */
	boolean deposit(long accountNumber, String userName, double amount) throws RemoteException;

	/**
	 * 2-Phase commit protocol helper method for Deposit.
	 * @param accountNumber long
	 * @param userName String
	 * @param amount double
	 * @return boolean representing if deposit is successful or unsuccessful.
	 * @throws RemoteException
	 */
	boolean depositFor2PC(long accountNumber, String userName, double amount) throws RemoteException;

	/**
	 * Withdraw specified amount from specified user.
	 * @param accountNumber long
	 * @param userName String
	 * @param amount double
	 * @return boolean representing if deposit is successful or unsuccessful.
	 * @throws RemoteException
	 */
	double withDraw(long accountNumber, String userName, double amount) throws RemoteException;

	/**
	 * 2-Phase commit protocol helper method for Deposit.
	 * @param accountNumber long
	 * @param userName String
	 * @param amount double
	 * @return boolean representing if deposit is successful or unsuccessful.
	 * @throws RemoteException
	 */
	double withDrawFor2PC(long accountNumber, String userName, double amount) throws RemoteException;

	/**
	 * Gets the Account information from a specific account number / user name
	 * @param accountNumber long
	 * @param userName String
	 * @return Account class
	 * @throws RemoteException
	 */
	Account getBalance(long accountNumber, String userName) throws RemoteException;

	/**
	 * 2-phase commit protocol to update databases at each server
	 * @return an Integer representing pass/fail
	 * @throws RemoteException
	 */
	Integer requestToPublishAtServer() throws RemoteException;
	
	/**
	 * Gets table records at a database
	 * @return a Map<Long, Account>
	 * @throws RemoteException
	 */
	Map<Long, Account> getRecords() throws RemoteException;
	
	/**
	 * Replicates data from a database to the current servers database.
	 * @param account
	 * @return boolean representing pass/fail.
	 * @throws RemoteException
	 */
	boolean replicateData(List<Account> account) throws RemoteException;
}