/**
 * 
 * Data access object for performing CRUD operation for transaction to a given
 * user.
 *
 */
public interface TransactionDao {

	boolean deposit(String accountNumber, double balance);
	
	double withDraw(String accountNumber, double balance);
	
	Transaction getBalance(String accountNumber);
}
