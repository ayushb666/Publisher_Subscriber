package PubSub;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientReceiveData implements Runnable {

	private boolean isConnected = false;
	private int port;
	private DatagramSocket ds = null;
	private DatagramPacket DpReceive = null;


	public ClientReceiveData(int port) {
		this.isConnected = true;
		this.port = port;
	}

	@Override
	public void run() {
		byte[] receive = new byte[65535];
		try{
			ds = new DatagramSocket(this.port);
			while (this.isConnected)
			{ 
				DpReceive = new DatagramPacket(receive, receive.length); 
				ds.receive(DpReceive);
				System.out.println(data(receive).toString());
				receive = new byte[65535];
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}finally {
			if(ds.isClosed()) {
				ds.close();
			}
		}

	}

	public void shutDown(){
		this.isConnected = false;
		try{
			if(this.ds != null) {
				this.ds.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	private StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
	


}
