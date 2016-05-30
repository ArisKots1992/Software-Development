package server;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import types.AvailableNodes;
import types.MaliciousPatterns;
import types.StatisticalReports;


@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface CIService {

	// interfaces for PCs/laptops

	@WebMethod
	public boolean register_pc(String nodeID);

	@WebMethod
	public MaliciousPatterns maliciousPatternRequest(String nodeID);

	@WebMethod
	public void maliciousPatternsStatisticalReport(String nodeID, StatisticalReports m);

	@WebMethod
	public boolean unregister(String nodeID);


	// interfaces for smartphones, tablets

	@WebMethod
	public int register_smartphone(String username, String password, AvailableNodes nodes);

	@WebMethod
	public List<StatisticalReports> retrieveStatistics(String username, String password);

	@WebMethod
	public String retrieveMaliciousPatterns(String username, String password);

	@WebMethod
	public void insertMaliciousPatterns(String username, String password, String maliciousIP, String maliciousPattern);

	@WebMethod
	public int login(String username, String password);

	@WebMethod
	public boolean logout(String username, String password);

	@WebMethod
	public boolean delete(String username, String password, String nodeID);
}
