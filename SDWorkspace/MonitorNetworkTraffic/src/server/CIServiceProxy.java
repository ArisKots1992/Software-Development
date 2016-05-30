package server;

public class CIServiceProxy implements server.CIService {
  private String _endpoint = null;
  private server.CIService cIService = null;
  
  public CIServiceProxy() {
    _initCIServiceProxy();
  }
  
  public CIServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCIServiceProxy();
  }
  
  private void _initCIServiceProxy() {
    try {
      cIService = (new server.CIServiceImplServiceLocator()).getCIServiceImplPort();
      if (cIService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cIService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cIService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cIService != null)
      ((javax.xml.rpc.Stub)cIService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public server.CIService getCIService() {
    if (cIService == null)
      _initCIServiceProxy();
    return cIService;
  }
  
  public boolean register_pc(java.lang.String arg0) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.register_pc(arg0);
  }
  
  public server.MaliciousPatterns maliciousPatternRequest(java.lang.String arg0) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.maliciousPatternRequest(arg0);
  }
  
  public void maliciousPatternsStatisticalReport(java.lang.String arg0, server.StatisticalReports arg1) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    cIService.maliciousPatternsStatisticalReport(arg0, arg1);
  }
  
  public boolean unregister(java.lang.String arg0) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.unregister(arg0);
  }
  
  public int register_smartphone(java.lang.String arg0, java.lang.String arg1, server.AvailableNodes arg2) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.register_smartphone(arg0, arg1, arg2);
  }
  
  public server.StatisticalReports[] retrieveStatistics(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.retrieveStatistics(arg0, arg1);
  }
  
  public java.lang.String retrieveMaliciousPatterns(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.retrieveMaliciousPatterns(arg0, arg1);
  }
  
  public void insertMaliciousPatterns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    cIService.insertMaliciousPatterns(arg0, arg1, arg2, arg3);
  }
  
  public int login(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.login(arg0, arg1);
  }
  
  public boolean logout(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.logout(arg0, arg1);
  }
  
  public boolean delete(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException{
    if (cIService == null)
      _initCIServiceProxy();
    return cIService.delete(arg0, arg1, arg2);
  }
  
  
}