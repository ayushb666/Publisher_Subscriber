package PubSub;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Server {

	private String my_ipAddress;
	private int port;
	private String userId;
	
	public Server(String port) throws UnknownHostException {
		this.my_ipAddress = Inet4Address.getLocalHost().getHostName().trim();
		try{
			this.port = Integer.parseInt(port);
		}catch (Exception e) {
			System.out.println("Invalid or no port given so taking port 10000 by default");
			this.port = 9999;
		}
		this.userId = this.my_ipAddress + "_" + this.port;
	}
	
	public String getMy_ipAddress() {
		return my_ipAddress;
	}

	public int getPort() {
		return port;
	}
	
	public String getUserId() {
		return this.userId;
	}

	@Override
	public String toString() {
		return "Server [my_ipAddress=" + my_ipAddress + ", port=" + port + "]";
	}
	
}
