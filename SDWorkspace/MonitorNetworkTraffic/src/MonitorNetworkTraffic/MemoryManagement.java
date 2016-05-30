package MonitorNetworkTraffic;

import java.rmi.RemoteException;
import java.util.*;

import SharedMemories.*;

public class MemoryManagement implements Runnable {

	private int secs;								/* send memories to the adder every "secs" seconds */
	private Thread seek_inter;
	private Thread update_mal;
	private static states currentState = MemoryManagement.states.WAIT;		/* run by the time this thread is created */
	public static enum states { RUN, WAIT, TERMINATE };

	public synchronized static void setCurrentState(MemoryManagement.states state) {
		currentState = state;
	}

	private void send_memory_states_to_adder() {
		String mal_ip_statistics = S_MPSM_IP.memory_toString();
		String mal_patterns_statistics = S_MPSM_Pattern.memory_toString();

			/* Web Service interface to communicate with the server */
		try {
			MainClass.get_server().maliciousPatternsStatisticalReport(Register.get_nodeID(), new server.StatisticalReports(mal_ip_statistics, mal_patterns_statistics));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void terminate() throws InterruptedException {
		SeekInterfaces.setCurrentState(SeekInterfaces.states.TERMINATE);
		UpdateMalicious.setCurrentState(UpdateMalicious.states.TERMINATE);
		seek_inter.interrupt();			/* thread may be sleeping */
		update_mal.interrupt();			/* thread may be sleeping */
		seek_inter.join();
		update_mal.join();
		MPSM_IP.DeleteSharedMemory();
		MPSM_Pattern.DeleteSharedMemory();
		S_MPSM_IP.DeleteSharedMemory();
		S_MPSM_Pattern.DeleteSharedMemory();
		System.out.println("Destroying the memories MSPM, S-MPSM.");
	}

	public MemoryManagement(int secs, Thread seek_inter, Thread update_mal) {
		this.seek_inter = seek_inter;
		this.update_mal = update_mal;
		this.secs = secs;
	}

	public void run() {
		System.out.println( "New thread to manage shared memories was created..." );
		try {
			synchronized(this) {
				while (MemoryManagement.currentState == MemoryManagement.states.WAIT) {	/* wait until there is an interrupted signal */
					wait();
				}
			}
		} catch (InterruptedException ie) {
			/* thread was awaken by MemoryManager */
			// ie.printStackTrace();
		}
		synchronized(this) {
			if (MemoryManagement.currentState == MemoryManagement.states.TERMINATE)
				return;
		}
			/* create shared memory for the threads' communication */
		MPSM_IP.CreateSharedMemory();		/* MPSM for IPs */
		MPSM_Pattern.CreateSharedMemory();	/* MPSM for patterns */
		S_MPSM_IP.CreateSharedMemory();		/* S-MPSM for IPs */
		S_MPSM_Pattern.CreateSharedMemory();	/* S-MPSM for patterns */

		SeekInterfaces.setCurrentState(SeekInterfaces.states.RUN);
		seek_inter.interrupt();
		UpdateMalicious.setCurrentState(UpdateMalicious.states.RUN);
		update_mal.interrupt();

		while (true) {
			synchronized (this) {
				if (MemoryManagement.currentState == MemoryManagement.states.TERMINATE) {
					try {
						terminate();
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
					return;
				}
			}

			send_memory_states_to_adder();

			try {
				Thread.sleep( secs * 5000 );		/* all memories are sent to the adder every "secs" seconds */
			} catch (InterruptedException ie) {
				// ie.printStackTrace();
			}
		}
	}

}
