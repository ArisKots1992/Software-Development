/**
 * CIService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package server;

public interface CIService extends java.rmi.Remote {
    public boolean register_pc(java.lang.String arg0) throws java.rmi.RemoteException;
    public server.MaliciousPatterns maliciousPatternRequest(java.lang.String arg0) throws java.rmi.RemoteException;
    public void maliciousPatternsStatisticalReport(java.lang.String arg0, server.StatisticalReports arg1) throws java.rmi.RemoteException;
    public boolean unregister(java.lang.String arg0) throws java.rmi.RemoteException;
    public int register_smartphone(java.lang.String arg0, java.lang.String arg1, server.AvailableNodes arg2) throws java.rmi.RemoteException;
    public server.StatisticalReports[] retrieveStatistics(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public java.lang.String retrieveMaliciousPatterns(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public void insertMaliciousPatterns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws java.rmi.RemoteException;
    public int login(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public boolean logout(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public boolean delete(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException;
}
