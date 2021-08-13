import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ServerMap {
	
	Properties prop = null;
	
	public HashMap<String, Integer> getServerConfig() {
		HashMap<String, Integer> serverMap =    
	            new HashMap<String, Integer>();  
		if (prop == null) {
			prop = new Properties();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("project2.properties");
			try {
				prop.load(in);
				serverMap.put(prop.getProperty("Server1"), 1);
				serverMap.put(prop.getProperty("Server2"), 2);
				serverMap.put(prop.getProperty("Server3"), 3);
				serverMap.put(prop.getProperty("Server4"), 4);
				serverMap.put(prop.getProperty("Server5"), 5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return serverMap;
	}
}
