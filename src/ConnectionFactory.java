import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connect to Database
 * @author hany.said
 */
public class ConnectionFactory {

    public static String URL = "jdbc:mysql://localhost:3306/cs6650";
    public static final String USER = "root";
    public static final String PASS = "admin";

    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection(String serverNum)
    {
      try {
    	  URL = URL + "_" + serverNum;
          return DriverManager.getConnection(URL, USER, PASS);
      } catch (SQLException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
    }

    /**
     * Test Connection
     */
    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection("2");
        System.out.println("test");
    }

}