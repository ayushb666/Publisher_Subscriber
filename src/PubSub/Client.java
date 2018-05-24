package PubSub;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Client {
	
	private String my_ipAddress;
	private int port;
	
	public Client(String port) throws UnknownHostException {
		this.my_ipAddress = Inet4Address.getLocalHost().getHostAddress().trim();
		try{
			this.port = Integer.parseInt(port);
		}catch (Exception e) {
			System.out.println("Invalid or no port given so taking port 10000 by default");
			this.port = 10000;
		}
	}
	
	public String getMy_ipAddress() {
		return my_ipAddress;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "Client [my_ipAddress=" + my_ipAddress + ", port=" + port + "]";
	}
	
}
