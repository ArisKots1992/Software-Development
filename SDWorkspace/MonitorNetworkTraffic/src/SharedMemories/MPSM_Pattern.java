package SharedMemories;

import java.util.HashSet;
import java.util.Set;

public class MPSM_Pattern {

		/* we use data structure "Set" for the memories to assure uniqueness of entries */
	private static Set<String> mal_Patterns = null;			/* MPSM only for malicious Patterns */

	public MPSM_Pattern() {}						/* empty constructor */

	public synchronized static void CreateSharedMemory() {
		System.out.println( "MPSM for Patterns was created..." );
		mal_Patterns = new HashSet <String> ();
	}

	public synchronized static void DeleteSharedMemory() {
		mal_Patterns.clear();				/* erase all elements from the set */
		mal_Patterns = null;				/* release the set data structure */
	}

	public synchronized static void add_Pattern(String new_Pattern) {	/* new malicious IP in the MPSM */
		mal_Patterns.add( new_Pattern );
	}

	public synchronized static Set <String> returnCurrentState() {		/* returns the exact state at the time the method is called */
		Set <String> currentState = new HashSet <String> ();
		for (String s : mal_Patterns)
			currentState.add(s);		/* no need to create a clone of every String as Strings in the set cannot be modified */
                                                        /* the structure that gets modified is the Set */
		return currentState;						/* return deep clone */
	}

}
