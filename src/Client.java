import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.util.Map;
import java.util.Scanner;

/**
 * Client class for the program. User interacts with the application from this
 * class. Client connects to 1 of 5 servers via RoundRobinServer for load
 * balancing.
 *
 *
 */

public class Client {

	public static void main(String[] args) throws ServerNotActiveException, InterruptedException,
			SocketTimeoutException, MalformedURLException, RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", 2011);
		RoundRobinInterface server = (RoundRobinInterface) registry.lookup("loadbalancer");
		String serverName = server.getServer();
		Util util = new Util();
		Map<String, String> serverDetails = util.getPropValue();
		String serverPort = serverDetails.get(serverName);
		System.out.println(serverPort);
		Menu menu = new Menu();
		System.out.println(menu.menu());
		Registry serverRegistry = LocateRegistry.getRegistry("localhost", Integer.parseInt(serverPort));
		AccountDao accountDao = (AccountDao) serverRegistry.lookup(serverName);

		Scanner sc = new Scanner(System.in);
		String input = "";
		while (!input.toLowerCase().equals("exit")) {
			System.out.print("Enter command: ");
			input = sc.nextLine();
			if (input.equals("exit")) {
				System.out.println("Goodbye");
				break;
			}
			Parser x = new Parser();
			int command = x.Parse(input);
			if (command == 0) {
				System.out.println("Must enter an integer between 1 and 5");
			}
			command = Integer.parseInt(input);
			if (command == 1) {
				input = "";
				System.out.print("To create your account, we will need a first and last name");
				System.out.print("Please enter First Name:");
				String firstName = sc.nextLine();
				System.out.print("Please enter Last Name:");
				String lastName = sc.nextLine();
				System.out.print("Please enter User Name:");
				String userName = sc.nextLine();
				long accountNumber = accountDao.createUser(firstName, lastName, userName);
				if (accountNumber == 0) {
					System.out.println("Server Error, could not create new account.");
				} else {
					System.out.println("Account number created is " + accountNumber);
				}
			}
			if (command == 2) {
				input = "";
				System.out.print("To get your balance, we will need your userName and Account number");
				System.out.print("Please enter User Name:");
				String userName = sc.nextLine();
				System.out.print("Please enter Account Number:");
				String acctNum = sc.nextLine();
				Account account = accountDao.getBalance(Long.parseLong(acctNum), userName);
				System.out.println("Balance for user " + userName + " is " + account.getBalance());

			}
			if (command == 3) {
				input = "";
				System.out.println("To withdraw we will need your Account number and Username");

				long accountNumber = 0;
				while (accountNumber == 0) {
					try {
						System.out.println("Please enter Account Number:");
						accountNumber = Long.parseLong(sc.nextLine());
					} catch (Exception e) {
						System.out.println("Please enter a valid Account Number in number format.");
					}
				}

				System.out.print("Please enter User Name:");
				String userName = sc.nextLine();

				System.out.println("Please enter Amount to withdraw:");
				double withdrawAmount = 0;
				while (withdrawAmount == 0) {
					try {
						withdrawAmount = Double.parseDouble(sc.nextLine());
						if (withdrawAmount <= 0) {
							System.out.println("Amount must be positive");
							withdrawAmount = 0;
							System.out.println("Please enter amount to withdraw");
						}
					} catch (Exception e) {
						System.out.println("Please enter a valid $$ amount in $$.$$ format.");
					}
				}

				double balance = accountDao.withDraw(accountNumber, userName, withdrawAmount);
				System.out.println("Withdraw was " + (balance > 0.0 ? "Successful" : "Unsuccessful"));
			}
			if (command == 4) {
				input = "";
				System.out.println("To Deposit we will need your Account number and Username");

				long accountNumber = 0;
				while (accountNumber == 0) {
					try {
						System.out.println("Please enter Account Number:");
						accountNumber = Long.parseLong(sc.nextLine());
					} catch (Exception e) {
						System.out.println("Please enter a valid Account Number in number format.");
					}
				}

				System.out.print("Please enter User Name:");
				String userName = sc.nextLine();

				System.out.println("Please enter Amount to deposit:");
				double depositAmount = 0;
				while (depositAmount == 0) {
					try {
						depositAmount = Double.parseDouble(sc.nextLine());
						if (depositAmount <= 0) {
							System.out.println("Amount must be positive");
							depositAmount = 0;
							System.out.println("Please enter amount to deposit:");
						}
					} catch (Exception e) {
						System.out.println("Please enter a valid $$ amount in $$.$$ format.");
					}
				}

				boolean result = accountDao.deposit(accountNumber, userName, depositAmount);
				System.out.println("Deposit was " + (result ? "Successful" : "Unsuccessful"));

			}
			if (command == 5) {
				System.out.println(menu.menu());
			}
		}
	}
}
