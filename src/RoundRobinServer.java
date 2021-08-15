import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Load balancing server on port 2011.
 * @author alitz
 *
 */
public class RoundRobinServer {

	public static void main(String[] args) {
		try {
			RoundRobinInterface loadbalancer = new RoundRobin();
			Registry registry = LocateRegistry.createRegistry(2011);
			registry.bind("loadbalancer", loadbalancer);	
			System.out.println("test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}