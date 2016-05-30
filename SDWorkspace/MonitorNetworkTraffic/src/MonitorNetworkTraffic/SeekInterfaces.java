package MonitorNetworkTraffic;
import MonitorNetworkTraffic.Register.states;
import SharedMemories.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class SeekInterfaces implements Runnable {

	int secs;			/* search for new interfaces every "secs" seconds */
	int children_secs;		/* children threads monitor every interface every "children_secs" seconds */
	private Map <String, MonitorInter> interfaces;
	private static states currentState = SeekInterfaces.states.WAIT;		/* run by the time this thread is created */
	public static enum states { RUN, WAIT, TERMINATE };

	public synchronized static void setCurrentState(SeekInterfaces.states state) {
		currentState = state;
	}

	private boolean real_inter(String inter) {
		return ( inter.startsWith("eth") || inter.startsWith("wlan") );
	}

	private Set<PcapIf> get_interfaces() {
		Set<PcapIf> new_interfaces = new HashSet<PcapIf>();

	        List<PcapIf> alldevs = new ArrayList<PcapIf>();		/* Will be filled with NICs */
        	StringBuilder errbuf = new StringBuilder();		/* For any error msgs */

			/* First get a list of devices on this system */
	        int r = Pcap.findAllDevs(alldevs, errbuf);
        	if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
			System.exit(3);
        	}
	        for (PcapIf device : alldevs) {
        		if (real_inter(device.getName()))		/* ignore virtual interfaces */
        			new_interfaces.add(device);
        	}

        	return new_interfaces;
	}

	private void stop_monitoring_interface(MonitorInter m_inter) {
		m_inter.cp.setCurrentState(CheckPackets.states.TERMINATE);
		try {
			m_inter.t.join();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	private void terminate() {
		Iterator <Map.Entry <String, MonitorInter> > iter = interfaces.entrySet().iterator();
		while (iter.hasNext()) {
			stop_monitoring_interface(iter.next().getValue());
		}
		interfaces = null;
		System.out.println("Stopped monitoring changes in interfaces...");
	}

	public SeekInterfaces(int secs, int children_secs) {
		this.secs = secs;
		this.children_secs = children_secs;
		interfaces = new HashMap <String, MonitorInter> ();
	}

	public void run() {

		System.out.println( "New thread to monitor interfaces was created..." );
		try {
			synchronized(this) {
				while (SeekInterfaces.currentState == SeekInterfaces.states.WAIT) {	/* wait until there is an interrupted signal */
					wait();
				}
			}
		} catch (InterruptedException ie) {
			/* thread was awaken by MemoryManager */
			// ie.printStackTrace();
		}

		System.out.println( "Monitoring of interfaces started!" );
		Set <String> old_interfaces = new HashSet <String> ();

		while (true) {

			synchronized(this) {
				if (SeekInterfaces.currentState == SeekInterfaces.states.TERMINATE) {
					terminate();
					return;
				}
			}

			Set <PcapIf> new_interfaces = get_interfaces();
			Set <String> new_names = new HashSet <String> ();
			for (PcapIf pci : new_interfaces)
				new_names.add(pci.getName());

				/* remove inactive interfaces */
			for (String s : old_interfaces) {
				if (!new_names.contains(s)) {			/* interface not active anymore */
					System.out.println( "Interface inactive: " + s );
					MonitorInter m_inter = interfaces.remove(s);	/* remove it from the array */
					stop_monitoring_interface(m_inter);
				}
			}

				/* add new active interfaces */
			for (PcapIf s : new_interfaces) {
				if (!old_interfaces.contains(s.getName())) {		/* new interface became active */
					System.out.println( "New interface detected: " + s.getName() );
					CheckPackets cp = new CheckPackets(s, children_secs);
					Thread t = new Thread(cp);
					interfaces.put(s.getName(), new MonitorInter(t, cp));			/* add the new interface to the list of active ones */

				/* update memories that there is a new interface, only if there is not already an entry for it */
					String interface_ip = "0.0.0.0";
			        Enumeration<NetworkInterface> nets = null;
					try {
						nets = NetworkInterface.getNetworkInterfaces();
					} catch (SocketException e) {
						e.printStackTrace();
					}
			        for (NetworkInterface netint : Collections.list(nets))
			        	if( netint.getName().equals(s.getName()) ){
			        		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			        								
			        		// KOITA3E TO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			        							
			        //		interface_ip = Collections.list(inetAddresses).get(1).toString().replace("/", "");
			        	}

					if (!S_MPSM_IP.contains_Interface(s.getName())){
						S_MPSM_IP.add_Interface(s.getName(), interface_ip);
					}
					if (!S_MPSM_Pattern.contains_Interface(s.getName()))
						S_MPSM_Pattern.add_Interface(s.getName(), interface_ip);

					t.start();				/* start a new thead to monitor the new interface */
				}
			}

			old_interfaces = new_names;

			try {
				Thread.sleep( secs * 1000 );			/* periodic check happens every "secs" seconds */
			} catch (InterruptedException ie) {
				// ie.printStackTrace();
			}
		}

	}

}


	/* thread that monitors the interface */
class MonitorInter {
	Thread t;
	CheckPackets cp;

	MonitorInter(Thread t, CheckPackets cp) {
		this.t = t;
		this.cp = cp;
	}
}
