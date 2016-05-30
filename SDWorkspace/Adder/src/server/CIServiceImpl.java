package server;

import java.sql.SQLException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import main.Admin;
import memory.ActiveUsers;
import memory.AndroidUsers;
import memory.IPMemory;
import memory.PatternMemory;
import database.DBfunctions;
import types.AvailableNodes;
import types.MaliciousPatterns;
import types.StatisticalReports;

@WebService(endpointInterface="server.CIService")
public class CIServiceImpl implements CIService {

	DBfunctions dbfunctions = new DBfunctions();

	public static final int countdown = 20;						// start the timer for active nodes

	// methods for PCs/laptops

	@Override
	@WebMethod(operationName="register_pc")			// for overloading of web method
	public boolean register_pc(String nodeID) {
		if (ActiveUsers.getInstance().userExists(nodeID))
			return false;							// reject connection
		try {
			if (!dbfunctions.userExists(nodeID))	// check if the user has never registered before
				dbfunctions.insert(nodeID);			// register user
		} catch (SQLException e) {
			return false;							// reject connection
		}

		ActiveUsers.getInstance().add(nodeID, CIServiceImpl.countdown);
		return true;								// connection established
	}

	@Override
	@WebMethod
	public MaliciousPatterns maliciousPatternRequest(String nodeID) {
		List<String> known_ips = null;
		List<String> known_patterns = null;
		
		if (!ActiveUsers.getInstance().userExists(nodeID))	// user not in memory
			return null;

		if (ActiveUsers.getInstance().get_user(nodeID).is_deleted()) {	// user is deleted by admin
			ActiveUsers.getInstance().remove(nodeID);			// remove deleted node and inform it to exit
			return new MaliciousPatterns(null, null, true);		// node gets a message to exit
		}

		known_ips = ActiveUsers.getInstance().get_user(nodeID).get_known_ips();
		known_patterns = ActiveUsers.getInstance().get_user(nodeID).get_known_patterns();
	
		String[] ips_update = null;
		String[] patterns_update = null;
	
	// find updates if there is a different number of ips than known malicious ips for this node
		List<String> malicious_ips = IPMemory.getInstance().get_ips();
		int mal_ips_num = malicious_ips.size();
		if (known_ips.size() != mal_ips_num) {
			ips_update = new String[mal_ips_num - known_ips.size()];
			int i = 0;
			for (String s : malicious_ips) {
				if (!known_ips.contains(s))
					ips_update[i++] = s;
			}
			ActiveUsers.getInstance().update_known_ips(nodeID, malicious_ips);
		}
			
	// find updates if there is a different number of patterns than known malicious patterns for this node
		List<String> malicious_patterns = PatternMemory.getInstance().get_patterns();
		int mal_pat_num = malicious_patterns.size();
		if (known_patterns.size() != mal_pat_num) {
			patterns_update = new String[mal_pat_num - known_patterns.size()];
			int i = 0;
			for (String s : malicious_patterns) {
				if (!known_patterns.contains(s))
					patterns_update[i++] = s;
			}
			ActiveUsers.getInstance().update_known_patterns(nodeID, malicious_patterns);
		}

		return new MaliciousPatterns(ips_update, patterns_update, false);
	}

	@Override
	@WebMethod
	public void maliciousPatternsStatisticalReport(String nodeID, StatisticalReports m) {
		String ips_statistics = m.getIps_statistics();
		String pattern_statistics = m.getPattern_statistics();
		if (ips_statistics == null && pattern_statistics == null)
			return;				// there are no malicious IPs/patterns known to this node at all

			// modify the new statistics to contain the nodeID too
		String modified_ip_statistics = null;
		if (ips_statistics != null) {
			modified_ip_statistics = "";
			String[] modified_records = ips_statistics.split("%%");
			for (int i=0; i < modified_records.length-1; i++)
				modified_ip_statistics += nodeID + "#" + modified_records[i] + "%%";
			modified_ip_statistics += nodeID + "#" + modified_records[modified_records.length-1];
		}

		String modified_pattern_statistics = null;
		if (pattern_statistics != null) {
			modified_pattern_statistics = "";
			String[] modified_records = pattern_statistics.split("%%");
			for (int i=0; i < modified_records.length-1; i++)
				modified_pattern_statistics += nodeID + "#" + modified_records[i] + "%%";
			modified_pattern_statistics += nodeID + "#" + modified_records[modified_records.length-1];
		}


////////////////////////////////////////////////////////////////////////////////////////////
//		synchronized (AndroidUsers.getInstance()) {		// synchronize memory access

				// add statistics to the data base
			if (ips_statistics != null) {			
				String []records = ips_statistics.split("%%");
				for (String record : records) {
					String []types = record.split("#");
					dbfunctions.insertIPStatistics(nodeID, types[0], types[1], types[2], Integer.parseInt(types[3]));
				}
			}

			if (pattern_statistics != null) {
				String []records = pattern_statistics.split("%%");
				for (String record : records) {
					String []types = record.split("#");
					dbfunctions.insertPatternStatistics(nodeID, types[0],types[1], types[2], Integer.parseInt(types[3]));
				}
			}

				// none of the android users is aware of these statistics yet
				// so add them in every user
			for (String username : AndroidUsers.getInstance().get_androidUsers())
				AndroidUsers.getInstance().add_new_statistics(username, new StatisticalReports(modified_ip_statistics, modified_pattern_statistics));

//		}	// end of synchronization
////////////////////////////////////////////////////////////////////////////////////////////
	}

	@Override
	@WebMethod
	public boolean unregister(String nodeID) {
		ActiveUsers.getInstance().remove(nodeID);		// remove user from the memory									
		return true;
	}


	// methods for smartphones/tablets

	@Override
	@WebMethod
	public int register_smartphone(String username, String password, AvailableNodes nodes) {
		try {
			if (dbfunctions.get_password(username) != null)		// android user already exists
				return -2;										// user exists error code
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

/////////////////////////////////////////////////////////////////////////
//System.out.println("Arxika: " + nodes.ids);		/////////////////////
/////////////////////////////////////////////////////////////////////////

		String[] devices = null;
		if (nodes.ids == null || nodes.ids.length() != 0) {
			devices = nodes.ids.split(",,");				// take devices for the android user

			for (int i=0; i < devices.length; i++) {
				try {
					if (!dbfunctions.userExists(devices[i]))
						return i;									// nodeID i does not exist
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

			// the new user is not aware of the current situation of DB, so he must get informed
//		synchronized (AndroidUsers.getInstance()) {		// synchronize memory access
			AndroidUsers.getInstance().add(username);	// put an empty record for this user

//			List<String> full_ip_statistics = null;
//			try {
//				full_ip_statistics = dbfunctions.get_fullIPStatistics();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			List<String> full_pattern_statistics = null;
//			try {
//				full_pattern_statistics = dbfunctions.get_fullPatternStatistics();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//				// get memories in string, to fit with StatisticalReports format
//			String ips_in_string = "";
//			for (int i=0; i < full_ip_statistics.size()-1; i++)
//				ips_in_string += full_ip_statistics.get(i) + "%%";
//			ips_in_string += full_ip_statistics.get(full_ip_statistics.size()-1);
//
//			String patterns_in_string = "";
//			for(int i=0; i < full_pattern_statistics.size()-1; i++)
//				patterns_in_string += full_pattern_statistics + "%%";
//			patterns_in_string += full_pattern_statistics.get(full_pattern_statistics.size()-1);
//
//				// put the whole DB as what user doesn't know in the form of StatisiticalReports
//			AndroidUsers.getInstance().add_new_statistics(username, new StatisticalReports(ips_in_string, patterns_in_string));
//		}		// end of synchronization

				// insert a record for this user in data base, and records for all the devices he possesses
		dbfunctions.insert_android_user(username, password);
		if (devices != null) {
			for (int i=0; i < devices.length; i++) 
				dbfunctions.insert_device(username, devices[i]);
		}

		return -1;		// successful registration
	}

	@Override
	@WebMethod
	public List<StatisticalReports> retrieveStatistics(String username, String password) {
		String user_pswrd = null;
		try {
			user_pswrd = dbfunctions.get_password(username);	// null if the user does not exist
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (user_pswrd == null || !user_pswrd.equals(password))
			return null;		// wrong user name or wrong password

			// take all statistics that user is not aware of
		List<StatisticalReports> statistics = null;

//		synchronized (AndroidUsers.getInstance()) {		// synchronize memory access
			statistics = AndroidUsers.getInstance().get_statistics(username);
	

		AndroidUsers.getInstance().add(username);	// If the map previously contained a mapping for the key, the old value is replaced so now we have an empty list
//		}	// end of synchronization

		return statistics;
	}

	@Override
	@WebMethod
	public String retrieveMaliciousPatterns(String username, String password) {
		if (!username.equals(Admin.get_username()) || !password.equals(Admin.get_password()))
			return null;			// user is not admin

		String memories = "";
		List<String> ips = IPMemory.getInstance().get_ips();
		List<String> patterns = PatternMemory.getInstance().get_patterns();

			// get memories in sting
		for (int i=0; i < ips.size()-1; i++)
			memories += ips.get(i) + "--";
		if (ips.size() > 0)
			memories += ips.get(ips.size()-1) + "____";
		else if (ips.size() == 0 && patterns.size() > 0 )
			memories = " ____";
		else if (ips.size() == 0 && patterns.size() == 0)
			return " ____ ";

		if (patterns.size() == 0)
			return memories + " ";

		for (int i=0; i < patterns.size()-1; i++)
			memories += patterns.get(i) + "--";
		if (patterns.size() > 0)
			memories += patterns.get(patterns.size()-1);

		return memories;		// use "____" to split the two memories and "--" to split the records
	}

	@Override
	@WebMethod
	public void insertMaliciousPatterns(String username, String password, String maliciousIP, String maliciousPattern) {
		if (!username.equals(Admin.get_username()) || !password.equals(Admin.get_password()))
			return;						// user is not admin

		if (maliciousIP != null && maliciousIP.length() != 0)			// add malicious IP
			IPMemory.getInstance().add(maliciousIP);
		if (maliciousPattern != null && maliciousPattern.length() != 0)	// add malicious pattern
			PatternMemory.getInstance().add(maliciousPattern);
	}

	@Override
	@WebMethod
	public int login(String username, String password) {
		String user_pswrd = null;
		try {
			user_pswrd = dbfunctions.get_password(username);	// null if the user does not exist
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (user_pswrd == null || !user_pswrd.equals(password))
			return 0;

		if (username.equals(Admin.get_username()))
			return 1;		// user is admin
		else
			return 2;		// user is not admin
	}

	@Override
	@WebMethod
	public boolean logout(String username, String password) {
		String user_pswrd = null;
		try {
			user_pswrd = dbfunctions.get_password(username);	// null if the user does not exist
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (user_pswrd == null || !user_pswrd.equals(password))
			return false;		// wrong user name or wrong password

		return true;			// successful logout
	}

	@Override
	@WebMethod
	public boolean delete(String username, String password, String nodeID) {
		if (!username.equals(Admin.get_username()) || !password.equals(Admin.get_password()))
			return false;			// user is not admin
	
		if (!ActiveUsers.getInstance().userExists(nodeID))
			return false;			// node is not logged in
	
		ActiveUsers.getInstance().get_user(nodeID).deleteUser();
		return true;				// node deleted
	}
}

