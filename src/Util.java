import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Util class to server configuration from a property file.
 */
public class Util {

	static Properties prop;

	/**
	 * Method to read the properties and load the server values.
	 * 
	 * @return map of the server configuration.
	 */
	public Map<String, String> getPropValue() {
		Map<String, String> map = new HashMap<String, String>();
		if (prop == null) {
			prop = new Properties();

			InputStream in = this.getClass().getClassLoader().getResourceAsStream("project.properties");
			try {
				prop.load(in);
				Enumeration<?> e = prop.propertyNames();
				while (e.hasMoreElements()) {
					String name = (String) e.nextElement();
					String value = prop.getProperty(name);
					map.put(name, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String value = prop.getProperty(name);
				map.put(name, value);
			}
		}
		return map;
	}

	public AccountDao getActiveServer(int currentPort) {
		Util util = new Util();
		AccountDao stub = null;
		Map<String, String> serverMap = util.getPropValue();
		for(String entry: serverMap.keySet())
		{
			final int port = Integer.parseInt(serverMap.get(entry));
			if (port != currentPort)
			
			try {
				Registry registry = LocateRegistry.getRegistry(port);
				stub = (AccountDao) registry.lookup(entry);
				return stub;
			} catch (Exception e) {
				System.out.println("Exception get the instance of server " + entry);
				e.printStackTrace();
				continue;
			}
		}
		return null;

	}
	
	public List<Account> getUpdatedRecords(Map<Long, Account> sourceList, Map<Long, Account> otherList) {
		List<Account> finalList = new ArrayList<Account> ();
		if (sourceList.size() >= otherList.size()) {
			for(Long entry: sourceList.keySet())
			{
				Account other = otherList.get(entry);
				if (other != null) {
					Account source = sourceList.get(entry);
					if(other.getCreateDate().compareTo(source.getCreateDate()) > 0 ) {
						finalList.add(other);
					}
				}
			
			}
		} 
		if (otherList.size() > sourceList.size()) {
			for(Long entry: otherList.keySet())
			{
				Account source = sourceList.get(entry);				
				if (source == null) {		
					Account temp = otherList.get(entry);
					temp.setFlag(true);
					finalList.add(temp);
				}
			
			}
			for(Long entry: sourceList.keySet())
			{
				Account other = otherList.get(entry);
				if (other != null) {
					Account source = sourceList.get(entry);
					if(other.getCreateDate().compareTo(source.getCreateDate()) > 0 ) {
						finalList.add(other);
					}
				}
			
			}
		}
		return finalList;
	}

}
