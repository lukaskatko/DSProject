import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Util class to server configuration from a property file.
 */
public class Util {

	
	static Properties prop;
	
	/**
	 * Method to read the properties and load the server values.
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

	
}
