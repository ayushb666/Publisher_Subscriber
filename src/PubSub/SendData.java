package PubSub;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;

public class SendData implements Runnable{
	
	private String ipAddress;
	private int port;
	private Set<String> data;

	public SendData(String ipAddress, int port, Set<String> data) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.data = data;
	}
	
	@Override
	public void run() {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			byte[] buff = data.toString().getBytes();
			DatagramPacket dp = new DatagramPacket(buff, buff.length, InetAddress.getByName(ipAddress), port);
			ds.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ds.close();
		}
	}
	
}
