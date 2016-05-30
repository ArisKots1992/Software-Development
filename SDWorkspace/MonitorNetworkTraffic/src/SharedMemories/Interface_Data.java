package SharedMemories;

import java.util.*;

	/* holds the name and the IP address to characterize an interface */
public class Interface_Data {
	public String name;
	public String ip_addr;

	Interface_Data(String name, String ip_addr) {
		this.name = name;
		this.ip_addr = ip_addr;
	}

	Interface_Data(Interface_Data inter_data) {
		this.name = inter_data.name;
		this.ip_addr = inter_data.ip_addr;
	}
}