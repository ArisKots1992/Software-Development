package MonitorNetworkTraffic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import server.CIService;
import server.CIServiceImplServiceLocator;


public class MainClass {
	private static CIServiceImplServiceLocator service;
	private static CIService server;

	public synchronized static CIService get_server() {
		return server;
	}

	 public static void main(String[] args) throws InterruptedException, UnknownHostException, SocketException {
		 if (args.length != 0 && args.length != 4) {
			System.err.println("____________________________________________________________________________________________________");
			System.err.println("Command line arguements missing.");
			System.err.println("Please specify times (period) for the following periodic functionallities (in seconds <integers>):\n");
			System.err.println("\t<seek-interfaces> <package-checking> <request-malicious-patterns> <send-statistics>");
			System.err.println("____________________________________________________________________________________________________");
			System.exit(1);
		}

		int si_period, pc_period, rmp_period, sst_period;
		si_period = pc_period = rmp_period = sst_period = 1;
		if (args.length == 0) {
			System.out.println("____________________________________________________________________________________________________");
			System.out.println("Default mode used. All times are set to 1 second.");
		}
		else {
			try {
				si_period  = Integer.parseInt(args[0]);
				pc_period  = Integer.parseInt(args[1]);
				rmp_period = Integer.parseInt(args[2]);
				sst_period = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.err.println("Wrong input format. Expected integer.");
				System.exit(2);
			}
			System.out.println("____________________________________________________________________________________________________");
		}

		String server_addr = "";
		String port = "";
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
			input = new FileInputStream("config.properties");	 
			prop.load(input);			// load a properties file
			server_addr = prop.getProperty("server_ip");
			port = prop.getProperty("port");
		} catch (IOException ex) {
			//ex.printStackTrace();
			System.out.println("No server IP was given!");
			System.out.println("Exiting...");
			System.out.println("____________________________________________________________________________________________________");
			System.exit(1);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
	 
			/* create an instance of the server for the communication */
		if(server_addr.length() == 0 || port.length() == 0) {
			System.out.println("No server IP was given!");
			System.out.println("Exiting...");
			System.out.println("____________________________________________________________________________________________________");
			System.exit(1);
		}

		try {
			service = new CIServiceImplServiceLocator("http://" + server_addr + ":" + port + "/CIService/CIService?WSDL", new QName("http://server/", "CIServiceImplService"));
			server = service.getCIServiceImplPort();
		} catch(ServiceException e) {
			//e.printStackTrace();
		}
		if (server == null) {
			System.out.println("Server is unreachable, try again later.");
			System.out.println("Exiting...");
			System.out.println("____________________________________________________________________________________________________");
			System.exit(1);
		}

		System.out.println( "Starting!" );
		final Thread seek_inter = new Thread( new SeekInterfaces(si_period, pc_period) );
		final Thread update_mal = new Thread( new UpdateMalicious(rmp_period) );
		final Thread mem_handl  = new Thread( new MemoryManagement(sst_period, seek_inter, update_mal) );
		final Thread reg_pc     = new Thread( new Register(mem_handl) );

		seek_inter.start();
		reg_pc.start();
		update_mal.start();
		mem_handl.start();

		Runtime.getRuntime().addShutdownHook(new Thread()
	        {
	            @Override
	            public void run()
	            {
	            	Register.setCurrentState(Register.states.TERMINATE);
	            	reg_pc.interrupt();
	            	System.out.println();
	            	try {
				reg_pc.join();
			} catch (InterruptedException ie) {
				//ie.printStackTrace();
			}
	            	System.out.println( "Disconnecting..." );
	        		System.out.println("____________________________________________________________________________________________________\n");
	            }
	        });
	}
	    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
	        System.out.printf("Display name: %s\n", netint.getDisplayName());
	        System.out.printf("Name: %s\n", netint.getName());
	        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
	        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        	System.out.printf("InetAddress: %s\n", inetAddress);
	        }
	        System.out.printf("\n");
	     }
}
