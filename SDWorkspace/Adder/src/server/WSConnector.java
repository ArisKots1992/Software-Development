package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.xml.ws.Endpoint;

public class WSConnector {
	private static WSConnector wscon;
	private static Endpoint ep;

	private WSConnector() {			// Singleton Pattern
		String port = "9999";
		String ip_addr = "0.0.0.0";

		String ws_addr = "http://" + ip_addr + ":" + port + "/CIService/";
		ep = Endpoint.publish(ws_addr, new CIServiceImpl());
		// http://localhost:9999/CIService/CIService?WSDL
		
		
	}

	public synchronized static WSConnector getInstance() {
		if (wscon == null)
			wscon = new WSConnector();
		return wscon;
	}

	public synchronized static void terminate() {
		if (wscon != null) {
			ep.stop();
			wscon = null;
		}
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
