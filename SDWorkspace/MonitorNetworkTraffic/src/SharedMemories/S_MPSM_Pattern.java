package SharedMemories;

import java.util.*;

public class S_MPSM_Pattern {

		/* we use data structure "Set" for the memories to assure uniqueness of entries */
	private static Map <Interface_Data, Set <MalPattern_Entry> > mal_patterns_statistics = null;	/* S-MPSM only for malicious patterns */

	public S_MPSM_Pattern() {}				/* empty constructor */

	public synchronized static void CreateSharedMemory() {
		System.out.println( "S-MPSM for patterns was created..." );
		mal_patterns_statistics = new HashMap <Interface_Data, Set <MalPattern_Entry> > ();
	}

	public synchronized static void DeleteSharedMemory() {
		mal_patterns_statistics.clear();		/* erase all elements from the map */
		mal_patterns_statistics = null;			/* release the map data structure */
	}

		/* interface with name "name" already exists in the S-MPSM memory */
	public synchronized static boolean contains_Interface(String name) {
		Iterator <Map.Entry <Interface_Data, Set <MalPattern_Entry> > > iter = mal_patterns_statistics.entrySet().iterator();
		while (iter.hasNext()) {
			if (iter.next().getKey().name.equals(name))
				return true;
		}
		return false;
	}

	public synchronized static void add_Pattern_Entry(String new_pattern) {		/* new malicious pattern in the S-MPSM */

			/* iterate over the whole Map and add an empty entry for every interface */
		Iterator < Map.Entry <Interface_Data, Set <MalPattern_Entry> > > iter = mal_patterns_statistics.entrySet().iterator();
		while ( iter.hasNext() )
			iter.next().getValue().add( new MalPattern_Entry(new_pattern) );
	}

			/* a new interface was detected, add it in the S-MPSM with 0 frequency for all malicious patterns */
	public synchronized static void add_Interface(String inter_name, String inter_ip) {
		Interface_Data inter = new Interface_Data(inter_name, inter_ip);	/* data of the new interface */
		Set <MalPattern_Entry> new_entries = new HashSet <MalPattern_Entry> ();	/* a set to store the statistics for every malicious pattern for the new interface */
		Set <String> mal_patterns = MPSM_Pattern.returnCurrentState();		/* take a set of all the malicious patterns */
		for (String s : mal_patterns) {
			new_entries.add( new MalPattern_Entry(s) );		/* one entry for every malicious pattern with frequency 0 so far */
		}

		mal_patterns_statistics.put(inter, new_entries);		/* add the new interface to the S-MPSM */
	}

		/* found packet with malicious IP from interface inter */
	public synchronized static void updateEntry(String inter, String pattern, int n) {
		Iterator <Map.Entry <Interface_Data, Set <MalPattern_Entry> > > iter = mal_patterns_statistics.entrySet().iterator();
		while (iter.hasNext()) {					/* iterate through the interfaces */
			Map.Entry <Interface_Data, Set <MalPattern_Entry> > pair = iter.next();
			if (pair.getKey().name.equals(inter)) {			/* find the correct interface */
				Set <MalPattern_Entry> inter_entries = pair.getValue();
				for (MalPattern_Entry mal_entry : inter_entries) {	/* iterate through the entry set of the interface */
					if (mal_entry.mal_pattern.equals(pattern)) {	/* find the correct entry (malicious pattern), update its frequency and return */
						mal_entry.update(n);
						return;
					}
				}
			}
		}
	}

	public synchronized static Map <Interface_Data, Set <MalPattern_Entry> > returnCurrentState() {
		Map <Interface_Data, Set <MalPattern_Entry> > currentState = new HashMap <Interface_Data, Set <MalPattern_Entry> > ();

		Iterator <Map.Entry <Interface_Data, Set <MalPattern_Entry> > > iter = mal_patterns_statistics.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry <Interface_Data, Set <MalPattern_Entry> > pair = iter.next();	/* get every interface and its entry set */
			Interface_Data inter = pair.getKey();					/* no need for copy construction as interface key is immutable */
			Set <MalPattern_Entry> inter_entries = new HashSet <MalPattern_Entry> ();
			Set <MalPattern_Entry> current_entries = pair.getValue();
			for (MalPattern_Entry mal_entry : current_entries) {		/* add all entries for this interface to a new set to save the current state */
				inter_entries.add( new MalPattern_Entry(mal_entry) );	/* here we use copy construction because frequency is not immutable */
			}

			currentState.put(inter, inter_entries);				/* add the interface and its entries to the new map to save the current state */
		}

		return currentState;							/* return deep clone */
	}

	public synchronized static void remove(String inter_name) {
		for (Interface_Data in_data : mal_patterns_statistics.keySet())
			if (in_data.name.equals(inter_name)) {
				mal_patterns_statistics.remove(in_data);
				return;
			}
	}

	public synchronized static String memory_toString() {
		if (!mal_patterns_statistics.keySet().iterator().hasNext())		/* there are no entries */
			return null;
		Interface_Data temp = mal_patterns_statistics.keySet().iterator().next();	/* one entry of the memory */
		int num_mal_patterns = mal_patterns_statistics.get(temp).size();	/* we use it to get the number of malicious IPs */

		if (num_mal_patterns == 0)						/* there are entries for interfaces but no ips */
			return null;
		 String records = "";	/* total number of records in the memory */
		 int outer_counter = 0;
		 for (Interface_Data inter_data : mal_patterns_statistics.keySet() ) {
			 if (outer_counter != 0)
				 records += "%%";
			 outer_counter++;
			 String inter = inter_data.name + "#" + inter_data.ip_addr + "#";
			 int inner_counter = 0;
			 for (MalPattern_Entry mal_pattern_entry : mal_patterns_statistics.get(inter_data)) {
				 if (inner_counter != 0)
					 records += "%%";
				 inner_counter++;
				 records += inter + mal_pattern_entry.mal_pattern + "#" + Integer.toString(mal_pattern_entry.frequency);
			 }
		}
		return records;
	}

}
