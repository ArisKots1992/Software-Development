package memory;

import java.util.ArrayList;
import java.util.List;

public class PatternMemory {
	private static PatternMemory pattern_mem;
	private List<String> patterns;

	private PatternMemory() {			// use Singleton Pattern
		patterns = new ArrayList<String>();
	}

	public synchronized static PatternMemory getInstance() {
		if (pattern_mem == null)
			pattern_mem = new PatternMemory();
		return pattern_mem;
	}

	public synchronized static void erase() {
		if (pattern_mem != null) {
			pattern_mem.patterns = null;
			pattern_mem = null;
		}
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public synchronized void add(String pattern) {
		if (!patterns.contains(pattern))
			patterns.add(pattern);
	}

	public synchronized List<String> get_patterns() {
		return patterns;
	}
}
