import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RoundRobinInterface extends Remote {

	public String getServer() throws RemoteException;
}
