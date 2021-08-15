import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoundRobin extends UnicastRemoteObject implements RoundRobinInterface {
	
	protected RoundRobin() throws RemoteException {
		super();

	}

	private static Integer pos = 0;

	@Override
	public String getServer() throws RemoteException {
		Map<String, String> serverMap = new HashMap<String, String>();
		Util server = new Util();
		for(String entry: server.getPropValue().keySet())
		{
			final int port = Integer.parseInt(server.getPropValue().get(entry));			
			
			try {
				Registry registry = LocateRegistry.getRegistry(port);
				AccountDao account = (AccountDao) registry.lookup(entry);
				serverMap.put(entry, serverMap.get(entry));
			}catch (Exception e) {
				System.out.println("Server is not available " + e.getMessage());
			}
		}
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.addAll(keySet);

		String currentServer = null;
		synchronized (pos) {
			if (pos >= keySet.size())
				pos = 0;
			currentServer = keyList.get(pos);
			pos++;
		}

		return currentServer;
	}

}