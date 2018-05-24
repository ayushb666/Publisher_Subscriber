package PubSub;

public class Ping_Server implements Runnable {
	
	GroupServer gs;
	
	public  Ping_Server(GroupServer gs) {
		this.gs = gs;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try {
			while(this.gs.ping()){
				Thread.currentThread().sleep(1000);
			}
		} catch (Exception e) {
				System.out.println(e.getMessage());
		}
	}
	
}