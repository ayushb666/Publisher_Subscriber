package PubSub;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class RegistryServerImpl extends UnicastRemoteObject implements RegistryServer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> servers = new HashMap<>();
	private HashMap<String, String> clientServer = new HashMap<>();
	private TreeMap<Integer, Set<String>> registeredServers = new TreeMap<>();
	private int MAX_CLIENT = 5;
	
	protected RegistryServerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String registerServer(String userId) throws RemoteException {
		try {
			if (!servers.containsKey(userId)){
				servers.put(userId, 0);
				Set<String> temp = registeredServers.getOrDefault(0, new HashSet<String>());
				temp.add(userId);
				registeredServers.put(0, temp);
				System.out.println("Server Registered with UserID: " + userId);
				return "Server Registered";
			}else{
				return "Server already Registered";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Network Issues";
		}
	}

	@Override
	public String deRegisterServer(String userId) throws RemoteException {
		try {
			if (servers.containsKey(userId)){
				int val = servers.get(userId);
				servers.remove(userId);
				registeredServers.get(val).remove(userId);
				System.out.println("Server Deregistered with UserID: " + userId);
				return "Server DeRegistered";
			}else{
				return "Server Not Registered";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Network Issues";
		}
	}

	@Override
	public String joinNetworkClient(String client_ipAddress, int port) throws RemoteException {
		try {
			String clientUserdId = client_ipAddress + ":" + port;
			if(servers.size() == 0){
				return "No Server Present";
			}
			System.out.println(registeredServers);
			String userId = registeredServers.get(registeredServers.firstKey()).iterator().next();
			if (servers.get(userId) >= this.MAX_CLIENT){
				return "System is Busy";
			}else{
				registeredServers.get(servers.get(userId)).remove(userId);
				servers.put(userId, servers.get(userId) + 1);
				Set<String> temp = registeredServers.getOrDefault(servers.get(userId), new HashSet<String>());
				temp.add(userId);
				registeredServers.put(servers.get(userId), temp);
				clientServer.put(clientUserdId, userId);
				System.out.println("Client Added with ClientId: " + clientUserdId + " and ServerID: " + userId);
				return userId;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Network Error";
		}
	}

	@Override
	public String leaveNetworkClient(String client_ipAddress, int port) throws RemoteException {
		try {
			String clientUserdId = client_ipAddress + ":" + port;
			if (clientServer.containsKey(clientUserdId)) {
				String serverUserId = clientServer.get(clientUserdId);
				clientServer.remove(clientUserdId);
				registeredServers.get(servers.get(serverUserId)).remove(serverUserId);
				servers.put(serverUserId, servers.get(serverUserId) - 1);
				Set<String> temp = registeredServers.getOrDefault(servers.get(serverUserId) , new HashSet<String>());
				temp.remove(serverUserId);
				registeredServers.put(servers.get(serverUserId), temp);
				return "Network Left";
			}else{
				return "Not Connected to Network";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Network Errors";
		}		
	}
	
	
	
}
