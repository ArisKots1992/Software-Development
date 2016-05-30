package SharedMemories;

import java.util.*;

	/* holds the rest of the data that S-MPSM contains */
public class MalIP_Entry {
	public String mal_ip;
	public int frequency;

	public MalIP_Entry(String mal_ip) {
		this.frequency = 0;			/* new entry, no data for it yet */
		this.mal_ip = mal_ip;
	}

	public MalIP_Entry(MalIP_Entry me) {
		this.frequency = me.frequency;
		this.mal_ip = me.mal_ip;		/* no need for clone here as strings are immutable */
	}

	public void update(int n) {			/* found n more malicious references */
		frequency += n;
	}
}
