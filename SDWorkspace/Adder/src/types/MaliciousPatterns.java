package types;

public class MaliciousPatterns {
	public String[] mal_ips;
	public String[] mal_patterns;
	public boolean isDeleted;

	public MaliciousPatterns(String[] mal_ips, String[] mal_patterns, boolean isDeleted) {
		this.mal_ips = mal_ips;
		this.mal_patterns = mal_patterns;
		this.isDeleted = isDeleted;
	}
}
