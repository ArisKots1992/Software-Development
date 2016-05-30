package SharedMemories;

import java.util.*;

public class MPSM_IP {

		/* we use data structure "Set" for the memories to assure uniqueness of entries */
	private static Set <String> mal_IPs = null;				/* MPSM only for malicious IPs */

	public MPSM_IP() {}							/* empty constructor */

	public synchronized static void CreateSharedMemory() {
		System.out.println( "MPSM for IPs was created..." );
		mal_IPs = new HashSet <String> ();
	}

	public synchronized static void DeleteSharedMemory() {
		mal_IPs.clear();				/* erase all elements from the set */
		mal_IPs = null;					/* release the set data structure */
	}

	public synchronized static void add_IP(String new_IP) {			/* new malicious IP in the MPSM */
		mal_IPs.add( new_IP );
	}

	public synchronized static Set <String> returnCurrentState() {		/* returns the exact state at the time the method is called */
		Set <String> currentState = new HashSet <String> ();
		for (String s : mal_IPs)
			currentState.add(s);		/* no need to create a clone of every String as Strings in the set cannot be modified */
							/* the structure that gets modified is the Set */
		return currentState;						/* return deep clone */
	}

}
