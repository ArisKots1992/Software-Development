package memory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;


public class ActiveUsers {
	private static ActiveUsers act;
	private Map<String, UserRecord> activeUsers;

	private ActiveUsers() {				// use Singleton Pattern
		activeUsers = new HashMap<String, UserRecord>();
	}

	public synchronized static ActiveUsers getInstance() {
		if (act == null)
			act = new ActiveUsers();
		return act;
	}

	public synchronized static void erase() {
		if (act != null) {
			act.activeUsers = null;
			act = null;					// release memory
		}
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public synchronized boolean isInactive(String nodeID) {
		return activeUsers.get(nodeID).isInactive();
	}

	public synchronized void add(String nodeID, int countdown) {
			// new user is unaware of any malicious IPs and patterns
		activeUsers.put(nodeID, new UserRecord(countdown, null, null));
	}

	public synchronized void remove(String nodeID) {
		activeUsers.remove(nodeID);
	}

	public synchronized UserRecord get_user(String nodeID) {
		return activeUsers.get(nodeID);
	}

	public synchronized boolean userExists(String nodeID) {
		return activeUsers.containsKey(nodeID);
	}

	public synchronized Set<String> get_active_users() {
		return activeUsers.keySet();
	}

	public synchronized void update_known_ips(String nodeID, List<String> current_ips) {
		activeUsers.get(nodeID).set_known_ips(current_ips);
	}

	public synchronized void update_known_patterns(String nodeID, List<String> current_patterns) {
		activeUsers.get(nodeID).set_known_patterns(current_patterns);
	}

}