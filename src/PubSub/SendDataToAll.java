package PubSub;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;

public class SendDataToAll implements Runnable{

	private Set<String> subscribers;
	String message;

	public SendDataToAll(Set<String> subscribers, String message) {
		this.subscribers = subscribers;
		this.message = message;
	}

	@Override
	public void run() {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			byte[] buff = this.message.getBytes();
			for(String sub: this.subscribers) {
				String ipAddress = sub.split(":")[0];
				int port = Integer.parseInt(sub.split(":")[1]);
				System.out.println(ipAddress + ", " + port);
				DatagramPacket dp = new DatagramPacket(buff, buff.length, InetAddress.getByName(ipAddress), port);
				ds.send(dp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ds.close();
		}
	}
}
