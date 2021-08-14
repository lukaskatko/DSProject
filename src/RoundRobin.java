import java.rmi.RemoteException;
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
		serverMap.putAll(server.getPropValue());

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