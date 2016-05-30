package MonitorNetworkTraffic;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.Node;

import server.CIService;
import server.CIServiceImplService;
import server.CIServiceImplServiceLocator;

public class Register implements Runnable {
	private static String nodeID;
	private Thread memory_manager;
	private static states currentState = Register.states.RUN;		/* run by the time this thread is created */
	public static enum states { RUN, WAIT, TERMINATE };

	private static void create_id() {
	    try {
            InetAddress ip = InetAddress.getLocalHost();
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();

                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i],
                                     (i < mac.length - 1) ? "-" : ""));
                    }
                    Register.nodeID = sb.toString();	/* use MAC address as id for registration */
                    return;
                }

                System.out.println("Unable to create ID for registration for this pc.");
                System.out.println("Exiting...");
                System.out.println("____________________________________________________________________________________________________");
                Runtime.getRuntime().halt(2);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }

		//Register.nodeID = UUID.randomUUID().toString();
	}

	public synchronized static String get_nodeID() {
		return Register.nodeID;
	}

	private void terminate() throws InterruptedException {
		MemoryManagement.setCurrentState(MemoryManagement.states.TERMINATE);
		memory_manager.interrupt();
		memory_manager.join();
			/* send message to the added for disconnecting */
		System.out.println("Unregistration request was sent to the adder.");
		try {
			MainClass.get_server().unregister(Register.get_nodeID());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public Register(Thread memory_manager){
		this.memory_manager = memory_manager;
	}

	public synchronized static void setCurrentState(states state) {
		currentState = state;
	}

	public void run() {
		Register.create_id();
		System.out.println("New thread to register the device to the network was created...\n");
		System.out.println("Request for registration was sent to the adder, with ID = " + Register.get_nodeID() + ".");

		boolean flag_registered = false;

		try {
			flag_registered = MainClass.get_server().register_pc(Register.get_nodeID());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (!flag_registered) {
			System.out.println("Registration was unsuccessfull!\n");
			System.out.println("Try again later...");
			System.out.println("Exiting...");
			System.out.println("____________________________________________________________________________________________________");
			Runtime.getRuntime().halt(1);
		}

		System.out.println("Registration was successfull.\n");

		MemoryManagement.setCurrentState(MemoryManagement.states.RUN);
		memory_manager.interrupt();

		synchronized(this) {
			if (Register.currentState == Register.states.TERMINATE) {
				try {
					terminate();
				} catch (InterruptedException ie) {
					//ie.printStackTrace();
				}
			}
		}

		Register.setCurrentState(Register.states.WAIT);			/* now just wait for Ctrl+C signal to unregister from the adder */

		try {
			synchronized(this) {
				while (Register.currentState == Register.states.WAIT) {	/* wait until there is an interrupted signal */
					wait();
				}
			}
		} catch (InterruptedException ie) {
			/* thread was awaken by MemoryManager */
			// ie.printStackTrace();
		}

		try {
			terminate();
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}

}
