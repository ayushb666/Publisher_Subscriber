package PubSub;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegistryServer extends Remote{
	public String registerServer(String userId) throws RemoteException;
	public String deRegisterServer(String userId) throws RemoteException;
	public String joinNetworkClient(String client_ipAddress, int port) throws RemoteException;
	public String leaveNetworkClient(String client_ipAddress, int port) throws RemoteException;
}
