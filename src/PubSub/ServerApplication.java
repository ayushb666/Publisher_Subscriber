package PubSub;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.rmi.Naming;

public class ServerApplication {

	public static void main(String[] args) throws RemoteException, MalformedURLException{
		try {
			GroupServer gs = new GroupServerImpl();
			Server server = new Server(args[0]);
			Naming.rebind(server.getUserId(), gs);
			RegistryServer rs = (RegistryServer) Naming.lookup("registry_server");
			boolean cond = true;
			Scanner reader = new Scanner(System.in);
			while(true){
				System.out.println("\nSelect an option: \n1) Register \n2) DeRegister");
				int s = reader.nextInt();
				switch (s) {
				case 1:
					System.out.println(rs.registerServer(server.getUserId()));
					break;
				case 2:
					System.out.println(rs.deRegisterServer(server.getUserId()));
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
