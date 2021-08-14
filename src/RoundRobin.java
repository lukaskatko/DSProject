import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoundRobin extends Thread {
	private static Integer pos = 0;	
	public static String getServer() {
		Map<String, String> serverMap = new HashMap<String, String>();
		Util server = new Util();
		serverMap.putAll(server.getPropValue());

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