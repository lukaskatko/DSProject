import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Account Implementation for CRUD operations.
 *
 */
public class AccountDaoImpl extends UnicastRemoteObject implements AccountDao {
	int connectionNum;
	int portNumber;

	protected AccountDaoImpl(int connection, int portNumber) throws RemoteException {
		super();
		this.portNumber = portNumber;
		connectionNum = connection;
	}

	@Override
	public boolean deposit(long accountNumber, String userName, double amount) throws RemoteException {

		// 2pc logic
		Coordinator ct = new Coordinator();
		List<Integer> ackList;
		ackList = ct.publishToOtherServers(portNumber);
		if (ackList.size() >= 2) {
			System.out.println((ackList.size() + 1) + "servers in consensus. Proceeding with commit");
		}

		ackList = ct.proceedToCommit(portNumber, "D", accountNumber, userName, amount);
		if (ackList.size() >= 2) {
			System.out.println("Commit success on " + (ackList.size() + 1) + " servers.");
		}

		Connection connection = ConnectionFactory.getConnection(connectionNum + "");
		boolean flag = false;
		Account account = getBalance(accountNumber, userName);
		if (account == null) {
			flag = false;
		} else {
			flag = updateBalance(account, amount, connection, true, connectionNum);
		}
		return flag;
	}

	public boolean depositFor2PC(long accountNumber, String userName, double amount) throws RemoteException {
		Connection connection = ConnectionFactory.getConnection(connectionNum + "");
		boolean flag = false;
		Account account = getBalance(accountNumber, userName);
		if (account == null) {
			flag = false;
		} else {
			flag = updateBalance(account, amount, connection, true, connectionNum);
		}
		return flag;
	}

	private boolean updateBalance(Account account, double balance, Connection connection, boolean flag, int number)
			throws RemoteException {

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		double bal = 0.0;
		if (flag) {
			bal = account.getBalance().doubleValue() + balance;
		} else {
			bal = account.getBalance().doubleValue() - balance;
		}
		if (bal < 0.0)
			throw new RemoteException("Insufficient balance to with draw the requested amount " + balance);
		try {
			PreparedStatement ps = connection.prepareStatement(
					"Update cs6650_" + number + ".account set balance = ? update_date =? where account_number = ?");
			ps.setDouble(1, bal);
			ps.setDate(2, date);
			ps.setLong(3, account.getAccountNumber());

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
	public double withDraw(long accountNumber, String userName, double amount) throws RemoteException {
		// 2pc logic
		Coordinator ct = new Coordinator();
		List<Integer> ackList;
		ackList = ct.publishToOtherServers(portNumber);
		if (ackList.size() >= 2) {
			System.out.println((ackList.size() + 1) + "servers in consensus. Proceeding with commit");
		}

		ackList = ct.proceedToCommit(portNumber, "D", accountNumber, userName, amount);
		if (ackList.size() >= 2) {
			System.out.println("Commit success on " + (ackList.size() + 1) + " servers.");
		}

		Connection connection = ConnectionFactory.getConnection(connectionNum + "");
		Account account = getBalance(accountNumber, userName);
		if (account == null) {
			return 0.0;
		} else {
			updateBalance(account, amount, connection, false, connectionNum);
		}
		account = getBalance(accountNumber, userName);
		return account.getBalance();
	}

	@Override
	public double withDrawFor2PC(long accountNumber, String userName, double amount) throws RemoteException {
		Connection connection = ConnectionFactory.getConnection(connectionNum + "");
		Account account = getBalance(accountNumber, userName);
		if (account == null) {
			return 0.0;
		} else {
			updateBalance(account, amount, connection, false, connectionNum);
		}
		account = getBalance(accountNumber, userName);
		return account.getBalance();
	}

	@Override
	public Account getBalance(long accountNumber, String userName) {
		Connection connection = ConnectionFactory.getConnection(connectionNum + "");
		Account account = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM cs6650_" + connectionNum + ".account WHERE account_number='"
					+ accountNumber + "' and user_name = '" + userName + "'");

			if (rs.next()) {
				account = new Account();
				account.setAccountNumber(rs.getLong("account_number"));
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

	@Override
	public long createUser(String firstName, String lastName, String userName) throws RemoteException {
		Connection connection = ConnectionFactory.getConnection(connectionNum + "");
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO cs6650_" + connectionNum
					+ ".account (balance,first_name,last_name,user_name,update_date) VALUES (?, ?, ?,?,?)");
			ps.setDouble(1, 0.0);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, userName);
			ps.setDate(5, date);
			ps.executeUpdate();
			Statement stmt = connection.createStatement();
			System.out.println("SELECT * FROM cs6650_" + connectionNum + ".Account WHERE user_name='" + userName + "'");
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM cs6650_" + connectionNum + ".Account WHERE user_name='" + userName + "'");
			if (rs.next()) {
				return rs.getLong("account_number");
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return 0;

	}

	@Override
	public long createUserFor2PC(String firstName, String lastName, String userName) throws RemoteException {
		// 2pc logic
		Coordinator ct = new Coordinator();
		List<Integer> ackList;
		ackList = ct.publishToOtherServers(portNumber);
		if (ackList.size() >= 2) {
			System.out.println((ackList.size() + 1) + "servers in consensus. Proceeding with commit");
		}

		ackList = ct.proceedToCommitUser(portNumber, userName, firstName, lastName);
		if (ackList.size() >= 2) {
			System.out.println("Commit success on " + (ackList.size() + 1) + " servers.");
		}
		Connection connection = ConnectionFactory.getConnection(connectionNum + "");
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO cs6650_" + connectionNum
					+ ".account (balance,first_name,last_name,user_name,update_date) VALUES (?, ?, ?,?,?)");
			ps.setDouble(1, 0.0);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, userName);
			ps.setDate(5, date);
			ps.executeUpdate();
			Statement stmt = connection.createStatement();
			System.out.println("SELECT * FROM cs6650_"+connectionNum+".Account WHERE user_name='" + userName +"'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM cs6650_"+connectionNum+".Account WHERE user_name='" + userName +"'");
			if (rs.next()) {
				return rs.getLong("account_number");
			} 

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return 0;

	}
	
	

	@Override
	public Integer requestToPublishAtServer()
	{
		return portNumber;
	}
}
