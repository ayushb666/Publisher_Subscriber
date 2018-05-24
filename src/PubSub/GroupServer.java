package PubSub;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GroupServer extends Remote{
	
	public String join(String ipAddress, int port) throws RemoteException;
	public String leave(String ipAddress, int port) throws RemoteException;
	public String subscribe(String ipAddress, int port, String article) throws RemoteException;
	public String unsubscribe(String ipAddress, int port, String article) throws RemoteException;
	public String publish(String article, String ipAddress, int port) throws RemoteException;
	public boolean ping() throws RemoteException;
	
}
