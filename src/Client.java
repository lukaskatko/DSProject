import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;



public class Client {
	
	public static void main(String[] args) throws ServerNotActiveException, InterruptedException, SocketTimeoutException, MalformedURLException, RemoteException, NotBoundException {
		Menu menu = new Menu();
		System.out.println(menu.menu());
		
		//CONNECTION TO SERVERS TO BE IMPLEMENTED HERE /////
		///////////////////////////////////////////////////
		
		
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
        	int command =  x.Parse(input);
        	if (command == 0 ) {
        		System.out.println("Must enter an integer between 1 and 5");
        	}
        	command = Integer.parseInt(input);
        	if (command == 1) {
        		input = "";
        		System.out.print("To create your account, we will need a first and last name");
        		System.out.print("Please enter First Name:");
        		String first = sc.nextLine();
        		System.out.print("Please enter Last Name:");
        		String last = sc.nextLine();
        		
        		//PUT FIRST and LAST name into DB // GET BACK ACCOUNT NUMBER
        		///////////////////////////////////////////////////
        		
        	}
        	if (command == 2) {
        		input = "";
        		System.out.print("To get your balance, we will need your First name, Last name, and Account number");
        		System.out.print("Please enter First Name:");
        		String first = sc.nextLine();
        		System.out.print("Please enter Last Name:");
        		String last = sc.nextLine();
        		System.out.print("Please enter Account Number:");
        		String acctNum = sc.nextLine();
        		
        		//LOGIC CHECK HERE TO SEE IF FIRST/LAST/ACCTNUM MATCH A ROW IN DB ///
        		//GET BALANCE FROM HERE /////////////////////////////
        		///////////////////////////////////////////////////
        	}
        	if (command == 3) {
        		input = "";
        		System.out.println("To withdraw we will need your First name, Last name, and Account number");
        		System.out.println("Please enter First Name:");
        		String first = sc.nextLine();
        		System.out.println("Please enter Last Name:");
        		String last = sc.nextLine();
        		System.out.println("Please enter Account Number:");
        		String acctNum = sc.nextLine();
        		
        		//LOGIC CHECK HERE TO SEE IF FIRST/LAST/ACCTNUM MATCH A ROW IN DB ///
        		///////////////////////////////////////////////////
        		
        		System.out.println("Please enter Amount to withdraw:");
        		double withdrawAmount = 0;
        		while (withdrawAmount == 0) {
        			acctNum = sc.nextLine();
        			try {
        				withdrawAmount = Double.parseDouble(acctNum);
        				if (withdrawAmount <= 0) {
        					System.out.println("Amount must be positive");
        					withdrawAmount = 0;
        					System.out.println("Please enter amount to withdraw");
        				}
        			}catch (Exception e) {
        				System.out.println("Please enter a valid $$ amount in $$.$$ format.");
        			}
        		}
        		
        		//////////WITHDRAW LOGIC HERE ///////
        		///////////////////////////////////////////////////
        		
        		
        
        	}
        	if (command == 4) {
        		input = "";
        		System.out.println("To Deposit we will need your First name, Last name, and Account number");
        		System.out.println("Please enter First Name:");
        		String first = sc.nextLine();
        		System.out.println("Please enter Last Name:");
        		String last = sc.nextLine();
        		System.out.println("Please enter Account Number:");
        		String acctNum = sc.nextLine();
        		
        		//LOGIC CHECK HERE TO SEE IF FIRST/LAST/ACCTNUM MATCH A ROW IN DB ///
        		///////////////////////////////////////////////////
        		
        		System.out.println("Please enter Amount to deposit:");
        		double depositAmount = 0;
        		while (depositAmount == 0) {
        			acctNum = sc.nextLine();
        			try {
        				depositAmount = Double.parseDouble(acctNum);
        				if (depositAmount <= 0) {
        					System.out.println("Amount must be positive");
        					depositAmount = 0;
        					System.out.println("Please enter amount to deposit:");
        				}	
        			}catch (Exception e) {
        				System.out.println("Please enter a valid $$ amount in $$.$$ format.");
        			}
        		}
        		
        		//////////DEPOSIT LOGIC HERE ///////
        		///////////////////////////////////////////////////

        	}
        	if (command == 5) {
        		System.out.println(menu.menu());
        	}
        }
	}
}
   

