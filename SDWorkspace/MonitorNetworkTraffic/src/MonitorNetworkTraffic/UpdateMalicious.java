package MonitorNetworkTraffic;

import java.rmi.RemoteException;

import SharedMemories.*;

public class UpdateMalicious implements Runnable {

	int secs;				/* requesting new data every "secs" seconds */

	private static states currentState = UpdateMalicious.states.WAIT;		/* run by the time this thread is created */
	public static enum states { RUN, WAIT, TERMINATE };

	public synchronized static void setCurrentState(UpdateMalicious.states state) {
		currentState = state;
	}

	public UpdateMalicious(int secs) {

		this.secs = secs;
	}

	private void update_memories() {

			/* ask if there are new malicious IPs or patterns */
		server.MaliciousPatterns mpat = null;
		try {
			mpat = MainClass.get_server().maliciousPatternRequest(Register.get_nodeID());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (mpat == null) {
			return;
		}

		if (mpat.isIsDeleted() == true) {	/* node is deleted by admin, exit program */
			System.out.println("\nATTENTION: This node was deleted from the administrator of the network!");
			System.out.println("Forcing exit...\n");
			System.out.println("____________________________________________________________________________________________________");
			System.exit(1);					/* exit program */
		}


		String[] update_ips =  mpat.getMal_ips();
		String[] update_patterns =  mpat.getMal_patterns();

		if (update_ips != null) {					/* there are updates in malicious IPs */
			System.out.println();
			for (String ip : update_ips) {
				MPSM_IP.add_IP(ip);
				System.out.println("IP " + ip + " is malicious!");
			}
		}
		if (update_patterns != null) {				/* there are updates in malicious patterns */
			System.out.println();
			for (String pattern : update_patterns) {
				MPSM_Pattern.add_Pattern(pattern);
				System.out.println("Pattern " + pattern + " is malicious!");
			}
			System.out.println();
		}
		
	}

	public void run() {
		System.out.println("New thread to update the MPSMs was created...");
		try {
			synchronized (this) {
				while (UpdateMalicious.currentState == UpdateMalicious.states.WAIT)
					wait();
			}
		} catch (InterruptedException ie) {
			//ie.printStackTrace();
		}

		while (true) {
			synchronized (this) {
				if (UpdateMalicious.currentState == UpdateMalicious.states.TERMINATE)
					return;
			}
			update_memories();
			try {
				Thread.sleep( secs * 1000 );		/* periodic request happens every "secs" seconds */
			} catch (InterruptedException ie) {
				// ie.printStackTrace();
			}
		}
	}

}
