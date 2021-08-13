import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoundRobin {
	private static Integer pos = 0;

	public static String getServer() {

		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		ServerMap server = new ServerMap();
		serverMap.putAll(server.getServerConfig());

		Set<String> keySet = serverMap.keySet();
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.addAll(keySet);

		String currentServer = null;
		synchronized (pos) {
			if (pos > keySet.size())
				pos = 0;
			currentServer = keyList.get(pos);
			pos++;
		}

		return currentServer;
	}
}