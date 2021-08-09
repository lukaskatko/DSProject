import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Transaction Implementation for CRUD operations.
 *
 */
public class TransactionDaoImpl implements TransactionDao {

	@Override
	public boolean deposit(String accountNumber, double balance) {
		Connection connection = ConnectionFactory.getConnection();
		boolean flag = false;
		Transaction transaction = getBalance(accountNumber);
		if (transaction == null) {
			flag = insertBalance(transaction, connection);
		} else {
			flag = updateBalance(transaction, balance, connection, true);
		}
		return flag;
	}

	private boolean insertBalance(Transaction transaction, Connection connection) {

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO cs6650.transaction (balance,account_number,first_name,last_name,update_date VALUES (?, ?, ?,?,?)");
			ps.setDouble(1, transaction.getBalance());
			ps.setNString(2, transaction.getAccountNumber());
			ps.setString(3, transaction.getFirstName());
			ps.setString(4, transaction.getLastName());
			ps.setDate(5, date);
			int i = ps.executeUpdate();

			if (i == 1) {
				return true;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	private boolean updateBalance(Transaction transaction, double balance, Connection connection, boolean flag) {

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		double bal = 0.0;
		if (flag) {
			bal = transaction.getBalance().doubleValue() + balance;
		} else {
			bal = transaction.getBalance().doubleValue() - balance;
		}
		try {
			PreparedStatement ps = connection.prepareStatement(
					"Update cs6650.transaction set balance = ? update_date =? where account_number = ?");
			ps.setDouble(1, bal);
			ps.setDate(2, date);
			ps.setNString(3, transaction.getAccountNumber());

			int i = ps.executeUpdate();

			if (i == 1) {
				return true;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public double withDraw(String accountNumber, double balance) {
		Connection connection = ConnectionFactory.getConnection();
		boolean flag = false;
		Transaction transaction = getBalance(accountNumber);
		if (transaction == null) {
			return 0.0;
		} else {
			flag = updateBalance(transaction, balance, connection, false);
		}
		transaction = getBalance(accountNumber);
		return transaction.getBalance();
	}

	@Override
	public Transaction getBalance(String accountNumber) {
		Connection connection = ConnectionFactory.getConnection();
		Transaction transaction = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM cs6650.transaction WHERE account_number=" + accountNumber);

			if (rs.next()) {
				transaction = new Transaction();

				transaction.setAccountNumber(rs.getString("account_number"));
				transaction.setBalance(rs.getDouble("balance"));
				transaction.setCreateDate(rs.getDate("update_date"));
				transaction.setFirstName(rs.getString("first_name"));
				transaction.setLastName(rs.getNString("last_name"));

			}

		} catch (SQLException ex) {
			System.out.println("exception in getting balance " + ex.getStackTrace());
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (Exception e) {

			}
		}
		return transaction;
	}

}
