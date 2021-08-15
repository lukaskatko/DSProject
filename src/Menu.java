/**
 * Text based menu of services for the application.
 * @author alitz
 *
 */
public class Menu {
	public String menu() {
		return "Welcome to the Transaction Manager\n"
				+ "----------------------------------\n"
				+ "Please enter 1-5 to access the following commands:\n"
				+ "1: Create Account\n"
				+ "2: Get Balance\n"
				+ "3: Withdraw Funds\n"
				+ "4: Deposit Funds\n"
				+ "5: Show Menu\n"
				+ "----------------------------------\n"
				+ "What would you like to do?";

	}

}
