package types;

public class StatisticalReports {
	private String ips_statistics;
	private String pattern_statistics;

	public StatisticalReports(){}

	public StatisticalReports(String ips_statistics, String pattern_statistics) {
		this.ips_statistics = ips_statistics;
		this.pattern_statistics = pattern_statistics;
	}

	public String getIps_statistics() {
	return this.ips_statistics;
}

	public void setIps_statistics(String ips_statistics) {
		this.ips_statistics = ips_statistics;
	}

	public String getPattern_statistics() {
		return this.pattern_statistics;
	}

	public void setPattern_statistics(String pattern_statistics) {
		this.pattern_statistics = pattern_statistics;
	}
}
