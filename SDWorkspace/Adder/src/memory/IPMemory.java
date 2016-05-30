package memory;

import java.util.ArrayList;
import java.util.List;

public class IPMemory {
	private static IPMemory ip_mem;
	private List<String> ips;

	private IPMemory() {			// use Singleton Pattern
		ips = new ArrayList<String>();
	}

	public synchronized static IPMemory getInstance() {
		if (ip_mem == null)
			ip_mem = new IPMemory();
		return ip_mem;
	}

	public synchronized static void erase() {
		if (ip_mem != null) {
			ip_mem.ips = null;
			ip_mem = null;
		}
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public synchronized void add(String ip) {
		if (!ips.contains(ip))
			ips.add(ip);
	}

	public synchronized List<String> get_ips() {
		return ips;
	}

}
