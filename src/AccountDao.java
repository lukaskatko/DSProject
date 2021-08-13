/**
 * 
 * Data access object for performing CRUD operation for transaction to a given
 * user.
 *
 */
public interface AccountDao {

	boolean deposit(String accountNumber, double balance);
	
	double withDraw(String accountNumber, double balance);
	
	Account getBalance(String accountNumber);
}
