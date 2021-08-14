import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Account Implementation for CRUD operations.
 *
 */
public class AccountDaoImpl extends UnicastRemoteObject implements AccountDao {
	int connectionNum;
	protected AccountDaoImpl(int connection) throws RemoteException {
		
		super();
		connectionNum = connection;
	}

	@Override
	public boolean deposit(String accountNumber, double balance) throws RemoteException{
		Connection connection = ConnectionFactory.getConnection(connectionNum+"");
		boolean flag = false;
		Account account = getBalance(accountNumber);
		if (account == null) {
			flag = insertBalance(account, connection,connectionNum);
		} else {
			flag = updateBalance(account, balance, connection, true,connectionNum);
		}
		return flag;
	}

	private boolean insertBalance(Account account, Connection connection, int number) {

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO cs6650_"+number+".account (balance,account_number,first_name,last_name,update_date VALUES (?, ?, ?,?,?)");
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

	private boolean updateBalance(Account account, double balance, Connection connection, boolean flag, int number) {

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		double bal = 0.0;
		if (flag) {
			bal = account.getBalance().doubleValue() + balance;
		} else {
			bal = account.getBalance().doubleValue() - balance;
		}
		try {
			PreparedStatement ps = connection.prepareStatement(
					"Update cs6650_"+number+".account set balance = ? update_date =? where account_number = ?");
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
	public double withDraw(String accountNumber, double balance) {
		Connection connection = ConnectionFactory.getConnection(connectionNum+"");	
		Account account = getBalance(accountNumber);
		if (account == null) {
			return 0.0;
		} else {
			updateBalance(account, balance, connection, false,connectionNum);
		}
		account = getBalance(accountNumber);
		return account.getBalance();
	}

	@Override
	public Account getBalance(String accountNumber) {
		Connection connection = ConnectionFactory.getConnection(connectionNum+"");
		Account account = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM cs6650.Account WHERE account_number=" + accountNumber);

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
