package SharedMemories;

import java.util.*;

public class MalPattern_Entry {
	public String mal_pattern;
	public int frequency;
	public MalPattern_Entry(String mal_pattern) {
		this.frequency = 0;				/* new entry, no data for it yet */
		this.mal_pattern = mal_pattern;
	}

        public MalPattern_Entry(MalPattern_Entry mp) {
                this.frequency = mp.frequency;
                this.mal_pattern = mp.mal_pattern;		/* no need for clone here as strings are immutable */
        }

    	public void update(int n) {				/* found n more malicious references */
    		frequency += n;
    	}
}
