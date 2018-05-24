package PubSub;

import java.rmi.Naming;

public class RegistryServerApplication {

	public static void main(String[] args) {
		try {
			RegistryServer rs = new RegistryServerImpl();
			Naming.rebind("registry_server", rs);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
