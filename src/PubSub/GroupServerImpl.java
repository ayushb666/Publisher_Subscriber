package PubSub;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<String> clients = new HashSet<>();
	private ConcurrentHashMap<String, Set<String>> subscribers = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Category, Set<String>> type = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Set<String>> originator = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Set<String>> org = new ConcurrentHashMap<>();

	public GroupServerImpl() throws RemoteException {
		super();
	}

	@Override
	public String join(String ipAddress, int port) throws RemoteException {
		try{
			String userId = ipAddress + ":" + port; 
			if (!this.clients.contains(userId)){
				System.out.println("New Client Joining with UserID: " + userId);
				this.clients.add(userId);
				return "Connected";
			}else if(this.clients.contains(userId)){
				return "Already Connected";
			}else {
				System.out.println("Max Client Limit Reached");
				return "Connection Failed, Server Overloaded";
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "Connection Failed";
		}
	}

	@Override
	public String leave(String ipAddress, int port) throws RemoteException {
		try{
			String userId = ipAddress + ":" + port; 
			if(this.clients.contains(userId)){
				System.out.println("Client with UserID: " + userId + " leaving");
				this.clients.remove(userId);
				return "Disconnected";
			}else{
				return "Not Connected";
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "Not able to leave";
		}
	}

	@Override
	public String subscribe(String ipAddress, int port, String article) throws RemoteException {
		try {
			String userId = ipAddress + ":" + port;
			Article message = new Article(article);
			Set<String> result = new HashSet<>();
			if(this.clients.contains(userId) && message.isValid && message.getContent() == null){
				if (message.getType() != Category.None){
					Set<String> temp = subscribers.getOrDefault(userId, new HashSet<String>());
					temp.add(message.getType().toString());
					subscribers.put(userId, temp);
					result.addAll(type.get(message.getType()));
				}
				if (message.getOriginator() != null){
					Set<String> temp = subscribers.getOrDefault(userId, new HashSet<String>());
					temp.add(message.getOriginator());
					subscribers.put(userId, temp);
					result.addAll(originator.get(message.getOriginator()));
				}
				if (message.getOrg() != null){
					Set<String> temp = subscribers.getOrDefault(userId, new HashSet<String>());
					temp.add(message.getOrg());
					subscribers.put(userId, temp);
					result.addAll(org.get(message.getOrg()));
				}
				new Thread(new SendData(ipAddress, port, result)).start();
				return "Subscribed to: " + message.toString();
			}else if(!this.clients.contains(userId)){
				return "Not Connected";
			}else{
				return "Invalid Message";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Network Issues";
		}
	}

	@Override
	public String unsubscribe(String ipAddress, int port, String article) throws RemoteException {
		try {
			String userId = ipAddress + ":" + port;
			Article message = new Article(article);
			if(this.clients.contains(userId) && message.isValid && message.getContent() == null){ 
				if (message.getType() != Category.None && subscribers.get(userId) != null && subscribers.get(userId).contains(message.getType().toString())){
					subscribers.get(userId).remove(message.getType().toString());
				}
				if (message.getOriginator() != null && subscribers.get(userId) != null && subscribers.get(userId).contains(message.getOriginator())){
					subscribers.get(userId).remove(message.getOriginator());
				}
				if (message.getOrg() != null && subscribers.get(userId) != null && subscribers.get(userId).contains(message.getOrg())){
					subscribers.get(userId).remove(message.getOrg());
				}
				System.out.println("Unsubscibed: " + article.toString());
				return "Unsubcribed to: " + message.toString();
			}else if(!this.clients.contains(userId)){
				return "Not Connected";
			}else{
				return "Invalid Message";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Network Issues";
		}		
	}

	@Override
	public String publish(String article, String ipAddress, int port) throws RemoteException {
		try {
			String userId = ipAddress + ":" + port;
			Article message = new Article(article);
			if(this.clients.contains(userId) && message.isValid && message.getContent() != null){ 
				if(message.getType() != Category.None){
					Set<String> hs = type.getOrDefault(message.getType(), new HashSet<String>());
					hs.add(message.getContent());
					type.put(message.getType(), hs);
				}
				if(message.getOriginator() != null){
					Set<String> hs = originator.getOrDefault(message.getOriginator(), new HashSet<String>());
					hs.add(message.getContent());
					originator.put(message.getOriginator(), hs);
				}
				if(message.getOrg() != null){
					Set<String> hs = org.getOrDefault(message.getOrg(), new HashSet<String>());
					hs.add(message.getContent());
					org.put(message.getOrg(), hs);
				}
				new Thread(new SendDataToAll(getSubs(message), message.getContent())).start();
				System.out.println("Message for User: " + userId + " published");
				return "Article: " + message.toString() + " Published";
			}else if (!this.clients.contains(userId)) {
				return "Not Connected";
			}else {
				return "Invalid Message";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Network Issues";
		}
	}

	@Override
	public boolean ping() throws RemoteException {
		try {
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	private Set<String> getSubs(Article message) {
		Set<String> result = new HashSet<>();
		for(String user: this.subscribers.keySet()){
			Set<String> items = this.subscribers.get(user);
			if(items.contains(message.getType().toString()) || items.contains(message.getOrg()) || items.contains(message.getOriginator())) {
				result.add(user);
			}
		}
		return result;
	}
}
