package MonitorNetworkTraffic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import SharedMemories.*;

public class CheckPackets implements Runnable {

	private PcapIf device;
	private String interface_name;
	private int secs; /* testing every "secs" seconds */

	private states currentState = states.RUN; /*
											 * currentState here doesn't need to
											 * be static because the only type
											 */

	/*
	 * of thread that need to change its state is SeekInterfaces, which is its
	 * parent
	 */
	public enum states {
		RUN, TERMINATE
	}; /* possible states, this type of thread never needs to wait */

	private PcapPacket get_packet() {
		StringBuilder errbuf = new StringBuilder(); /* For any error messages */

		int snaplen = 64 * 1024; /* Capture all packets, no trucation */
		int flags = Pcap.MODE_PROMISCUOUS; /* capture all packets */
		int timeout = 1 * 1000; /* 3 seconds in millis */
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout,
				errbuf);
		if (pcap == null) /* cannot open the device => interface closed */
			return null; /* return null and wait for the thread to finish */
		Tcp tcp = new Tcp();
		Udp udp = new Udp();
		PcapPacket packet = new PcapPacket(1024);
		if (pcap.nextEx(packet) == Pcap.NEXT_EX_OK) {
			if (packet.hasHeader(tcp))
				return packet;

			if (packet.hasHeader(udp))
				return packet;

			return null;
		} else {
			return null;
		}
	}

	private int contains_pattern(PcapPacket packet, String pat) {
		Tcp tcp = new Tcp();
		Udp udp = new Udp();
		String payloadString = null;
		if (packet.hasHeader(udp)) { /* header is UDP */
			// System.out.println("UDP");
			byte[] print = udp.getPayload();
			payloadString = new String(print); /* paylaod in String form */
		} else if (packet.hasHeader(tcp)) { /* header is TCP */
			// System.out.println("TCP");
			byte[] print = tcp.getPayload();
			payloadString = new String(print); /* payload in String form */
		}

		Pattern pattern = Pattern.compile(pat);// Pattern.quote(pat)); /* search
												// how many times there is the
												// pattern in payload */
		Matcher matcher = pattern.matcher(payloadString);
		int frequency = 0;
		while (matcher.find())
			frequency++;

		return frequency;
	}

	private int contains_ip(PcapPacket packet, String mip){
		Ip4 ip=new Ip4();
		if (packet.hasHeader(ip)) {	//has  IP header
    		byte[] dIP=new byte[4],sIP= new byte[4];
    		dIP=packet.getHeader(ip).destination();
    		sIP=packet.getHeader(ip).source();
    		String sourceIP=FormatUtils.ip(sIP);//IP source String form
    		String destinationIP=FormatUtils.ip(dIP);//IP dest String form

    		if ( mip.equals(sourceIP) || mip.equals(destinationIP)) {
    			System.out.println("Einai malicious h: " + mip + "\n\n");
    			return 1;
    		}
		}
		return 0;
	}

	private void find_malicious_IPs(PcapPacket packet) {
		Set<String> mal_ips = MPSM_IP.returnCurrentState();
		for (String mal_ip : mal_ips) {
			synchronized (this) { /*
								 * synchronize so that nobody will modify
								 * currentState during checks
								 */
				if (currentState == states.TERMINATE) { /*
														 * interface is inactive
														 * so stop the thread
														 */
					return;
				}
			}
			int frequency = 0;
			if ((frequency = contains_ip(packet, mal_ip)) != 0) { // ////////////////////////////////

				// TO ALLA3A EVALTA TO CONTAINS IP ADI GIA CONTAINS
				// PATTERN!!!!!!!!!!!!!!!

				S_MPSM_IP.updateEntry(interface_name, mal_ip, frequency);
				System.out.println("Malicious ip \"" + mal_ip + "\" was found "
						+ String.valueOf(frequency)
						+ " times in a packet coming through " + interface_name
						+ ".");
			}
		}
	}

	private void find_malicious_Patterns(PcapPacket packet) {
		Set<String> mal_patterns = MPSM_Pattern.returnCurrentState();
		for (String mal_pattern : mal_patterns) {
			synchronized (this) { /*
								 * synchronize so that nobody will modify
								 * currentState during checks
								 */
				if (currentState == states.TERMINATE) { /*
														 * interface is inactive
														 * so stop the thread
														 */
					return;
				}
			}
			int frequency = 0;
			if ((frequency = contains_pattern(packet, mal_pattern)) != 0) {
				S_MPSM_Pattern.updateEntry(interface_name, mal_pattern,
						frequency);
				System.out.println("Malicious pattern \"" + mal_pattern
						+ "\" was found " + String.valueOf(frequency)
						+ " times in a packet coming through " + interface_name
						+ ".");
			}
		}
	}

	public CheckPackets(PcapIf device, int secs) {
		this.device = device;
		this.interface_name = device.getName();
		this.secs = secs;
		System.out.println("New thread monitors " + this.interface_name
				+ " interface every " + this.secs + " seconds.");
	}

	public synchronized void setCurrentState(states state) { /*
															 * change the state
															 * of the thread in
															 * a synchronized
															 * way
															 */
		currentState = state;
	}

	public void run() {

		while (true) { /*
						 * ckeck periodically the packets for malicious IPs or
						 * patterns
						 */

			PcapPacket packet = get_packet();
			synchronized (this) { /*
								 * synchronize so that nobody will modify
								 * currentState during checks
								 */
				if (currentState == states.TERMINATE) { /*
														 * interface is inactive
														 * so stop the thread
														 */
					System.out.println("Stopped monitoring interface "
							+ this.interface_name + "...");
					System.out.println("Removing entries for "
							+ this.interface_name + " from the memory...");
					S_MPSM_IP.remove(this.interface_name); // ///////////////////////////
					S_MPSM_Pattern.remove(this.interface_name); // ///////////////////////////
					return;
				}
			}

			if (packet != null) {
				find_malicious_IPs(packet);
				find_malicious_Patterns(packet);
			}

			synchronized (this) {
				if (currentState != states.RUN)
					continue;
			}

			try {
				Thread.sleep(this.secs * 1000);
			} catch (InterruptedException ie) {
				// ie.printStackTrace();
			}

		}
	}

}
