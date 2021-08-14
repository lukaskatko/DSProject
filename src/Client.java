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
		
		//CONNECTION TO SERVERS TO BE IMPLEMENTED
		Scanner sc = new Scanner(System.in);
        String input = "";
        while (!input.equals("end")) {
        	System.out.print("Enter command: ");
        	input = sc.nextLine();
        	Parser x = new Parser();
        	int command =  x.Parse(input);
        	if (command == 0 ) {
        		System.out.println("Must enter an integer between 1 and 5");
        	}
        	command = Integer.parseInt(input);
        	if (command == 1) {
        		//CREATE ACCOUNT
        	}
        	if (command == 2) {
        		//GET BALANCE
        	}
        	if (command == 3) {
        		//WITHDRAW FUNDS
        	}
        	if (command == 4) {
        		//DEPOSIT FUNDS
        	}
        	if (command == 5) {
        		System.out.println(menu.menu());
        	}
        }
	}
}
   

