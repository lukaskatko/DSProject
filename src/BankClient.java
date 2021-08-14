
public class BankClient {
	public static void main(String[] args) {

		try {

			String serverName = RoundRobin.getServer();
			System.out.println(serverName);
		} catch (Exception e) {
			System.out.println(java.time.LocalTime.now() + " Exception in RPCClient " + e.getMessage());

		}
	}
}
