package memory;

import java.util.ArrayList;
import java.util.List;


public class UserRecord {
	private boolean isDeleted;			// node is deleted from admin
	private int countdown;				// time remaining till the user becomes inactive
	private List<String> ips;			// malicious IPs that the user is aware of
	private List<String> patterns;		// malicious patterns that the user is aware of
//	private StatisticalReports last_statistics;		// last statistics that the node sent,
													// they help us determinate if the node is inactive
	
	UserRecord(int countdown, List<String> ips, List<String> patterns) {
		this.isDeleted = false;
		this.countdown = countdown;
		this.ips = new ArrayList<String>();
		this.patterns = new ArrayList<String>();
//		this.last_statistics = last_statistics;
		if (ips != null) {
			for (String s : ips)
				this.ips.add(s);
		}
		if (patterns != null) {
			for (String s : patterns)
				this.patterns.add(s);
		}
	}

	public void deleteUser() {
		isDeleted = true;
	}

	public boolean is_deleted() {
		return isDeleted;
	}

	public boolean isInactive() {
		return (countdown == 0);
	}

	public void decrease() {
		if (countdown > 0)	countdown--;
	}

	public void restore(int countdown) {
		this.countdown = countdown;
	}

	public List<String> get_known_ips() {
		return ips;
	}

	public void set_known_ips(List<String> current_ips) {
		this.ips = new ArrayList<String>();
		for (String s : current_ips)
			this.ips.add(s);
	}

	public List<String> get_known_patterns() {
		return patterns;
	}

	public void set_known_patterns(List<String> current_patterns) {
		this.patterns = new ArrayList<String>();
		for (String s : current_patterns)
			this.patterns.add(s);
	}
}
