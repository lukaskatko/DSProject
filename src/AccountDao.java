/**
 * 
 * Data access object for performing CRUD operation for transaction to a given
 * user.
 *
 */
public interface AccountDao {

	boolean deposit(String accountNumber, double balance, String serverNum);
	
	double withDraw(String accountNumber, double balance, String serverNum);
	
	Account getBalance(String accountNumber, String serverNum);
}
