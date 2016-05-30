package memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import types.StatisticalReports;

public class AndroidUsers {

	private static AndroidUsers memory;	
	private Map<String, List<StatisticalReports> > andr_users;	// statistics that the user is not yet aware of

	private AndroidUsers() {						// use Singleton Pattern
		andr_users = new HashMap<String, List<StatisticalReports> >();
		andr_users.put("admin", new ArrayList<StatisticalReports>());
	}

	public synchronized static AndroidUsers getInstance() {
		if (memory == null)
			memory = new AndroidUsers();
		return memory;
	}

	public synchronized static void erase() {
		if (memory != null) {
			memory.andr_users = null;
			memory = null;					// release memory
		}
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public synchronized Set<String> get_androidUsers() {
		return andr_users.keySet();
	}

	public synchronized void add(String username) {
		andr_users.put(username, new ArrayList<StatisticalReports>());	// if an entry already exists, the old value is replaced
	}

	public synchronized void add_new_statistics(String username, StatisticalReports m) {
		List<StatisticalReports> statistics = andr_users.get(username);
		statistics.add(m);
		andr_users.put(username, statistics);	// if an entry already exists, the old value is replaced
	}

	public synchronized List<StatisticalReports> get_statistics(String username) {
		return andr_users.get(username);
	}
}
