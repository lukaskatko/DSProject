import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Account Implementation for CRUD operations.
 *
 */
public class AccountDaoImpl implements AccountDao {

	@Override
	public boolean deposit(String accountNumber, double balance, String serverNum) {
		Connection connection = ConnectionFactory.getConnection(serverNum);
		boolean flag = false;
		Account account = getBalance(accountNumber, serverNum);
		if (account == null) {
			flag = insertBalance(account, connection, serverNum);
		} else {
			flag = updateBalance(account, balance, connection, true, serverNum);
		}
		return flag;
	}

	private boolean insertBalance(Account account, Connection connection, String serverNum) {

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		String server = "cs6650_" + serverNum;
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO" + server + ".Account (balance,account_number,first_name,last_name,update_date VALUES (?, ?, ?,?,?)");
			ps.setDouble(1, account.getBalance());
			ps.setNString(2, account.getAccountNumber());
			ps.setString(3, account.getFirstName());
			ps.setString(4, account.getLastName());
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

	private boolean updateBalance(Account account, double balance, Connection connection, boolean flag, String serverNum) {

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		double bal = 0.0;
		if (flag) {
			bal = account.getBalance().doubleValue() + balance;
		} else {
			bal = account.getBalance().doubleValue() - balance;
		}
		String server = "cs6650_" + serverNum;
		try {
			PreparedStatement ps = connection.prepareStatement(
					"Update" + server + ".Account set balance = ? update_date =? where account_number = ?");
			ps.setDouble(1, bal);
			ps.setDate(2, date);
			ps.setNString(3, account.getAccountNumber());

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
	public double withDraw(String accountNumber, double balance, String serverNum) {
		Connection connection = ConnectionFactory.getConnection(serverNum);		
		Account account = getBalance(accountNumber, serverNum);
		if (account == null) {
			return 0.0;
		} else {
			updateBalance(account, balance, connection, false, serverNum);
		}
		account = getBalance(accountNumber, serverNum);
		return account.getBalance();
	}

	@Override
	public Account getBalance(String accountNumber, String serverNum) {
		Connection connection = ConnectionFactory.getConnection(serverNum);
		Account account = null;
		Statement stmt = null;
		String server = "cs6650_" + serverNum;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM" + server + ".Account WHERE account_number=" + accountNumber);

			if (rs.next()) {
				account = new Account();

				account.setAccountNumber(rs.getString("account_number"));
				account.setBalance(rs.getDouble("balance"));
				account.setCreateDate(rs.getDate("update_date"));
				account.setFirstName(rs.getString("first_name"));
				account.setLastName(rs.getNString("last_name"));

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
		return account;
	}

}
