package PubSub;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientApplication {
	
	public static void main(String[] args) {
		try {
			RegistryServer regServer = (RegistryServer) Naming.lookup("registry_server");
			Client client = new Client(args[0]);
			clientRun(regServer, client.getMy_ipAddress(), client.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static GroupServer getGroupServer(RegistryServer regServer, String client_ipAddress, int port)
			throws RemoteException, NotBoundException, MalformedURLException {
		String serverName = regServer.joinNetworkClient(client_ipAddress, port);
		GroupServer server = (GroupServer) Naming.lookup("rmi://localhost:1099" + "/" + serverName);
		return server;
	}

	public static void clientRun(RegistryServer regServer, String client_ipAddress, int port) throws RemoteException, MalformedURLException, NotBoundException {
		boolean cond = true;
		GroupServer server = null;
		ClientReceiveData crd = null;
		Scanner reader = new Scanner(System.in);
		Scanner optionReader = new Scanner(System.in);
		try{
			while(cond){
				System.out.println("\nSelect an option: \n1) Join \n2) Leave \n3) Subscribe \n4) Unsubscribe \n5) Publish \n6) Ping Server \n7) Close");
				int s = optionReader.nextInt();
				switch (s) {
				case 1:
					server = getGroupServer(regServer, client_ipAddress, port);
					System.out.println(server.join(client_ipAddress, port));
					crd = new ClientReceiveData(port);
					new Thread(crd).start();
					break;
				case 2:
					crd.shutDown();
					regServer.leaveNetworkClient(client_ipAddress, port);
					System.out.println(server.leave(client_ipAddress, port));
					break;
				case 3:
					System.out.println("Enter Subscribe String: ");
					String subscription = reader.nextLine();
					System.out.println(server.subscribe(client_ipAddress, port, subscription));
					break;
				case 4:
					System.out.println("Enter UnSubscription String: ");
					String unsubscription = reader.nextLine();
					System.out.println(server.unsubscribe(client_ipAddress, port, unsubscription));
					break;
				case 5:
					System.out.println("Enter Publishing Article: ");
					String article = reader.nextLine();
					System.out.println(server.publish(article, client_ipAddress, port));
					break;
				case 6:
					System.out.println(server.ping());
					break;
				case 7:
					regServer.leaveNetworkClient(client_ipAddress, port);
					System.out.println(server.leave(client_ipAddress, port));
					cond = false;
					break;
				default:
					break;
				}
			}
		}finally {
			reader.close();
			optionReader.close();
		}
	}

}
