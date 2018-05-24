package PubSub;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.Naming;

public class ServerApplication {

	public static void main(String[] args) throws RemoteException, MalformedURLException{
		try {
			GroupServer gs = new GroupServerImpl();
			Server server = new Server(args[0]);
			Naming.rebind(server.getUserId(), gs);
			RegistryServer rs = (RegistryServer) Naming.lookup("registry_server");
			System.out.println(rs.registerServer(server.getUserId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
