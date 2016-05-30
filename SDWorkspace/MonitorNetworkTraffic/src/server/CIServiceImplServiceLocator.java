/**
 * CIServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package server;

public class CIServiceImplServiceLocator extends org.apache.axis.client.Service implements server.CIServiceImplService {

    public CIServiceImplServiceLocator() {
    }


    public CIServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CIServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CIServiceImplPort
    private java.lang.String CIServiceImplPort_address = "http://localhost:9999/CIService/";

    public java.lang.String getCIServiceImplPortAddress() {
        return CIServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CIServiceImplPortWSDDServiceName = "CIServiceImplPort";

    public java.lang.String getCIServiceImplPortWSDDServiceName() {
        return CIServiceImplPortWSDDServiceName;
    }

    public void setCIServiceImplPortWSDDServiceName(java.lang.String name) {
        CIServiceImplPortWSDDServiceName = name;
    }

    public server.CIService getCIServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CIServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCIServiceImplPort(endpoint);
    }

    public server.CIService getCIServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            server.CIServiceImplPortBindingStub _stub = new server.CIServiceImplPortBindingStub(portAddress, this);
            _stub.setPortName(getCIServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCIServiceImplPortEndpointAddress(java.lang.String address) {
        CIServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (server.CIService.class.isAssignableFrom(serviceEndpointInterface)) {
                server.CIServiceImplPortBindingStub _stub = new server.CIServiceImplPortBindingStub(new java.net.URL(CIServiceImplPort_address), this);
                _stub.setPortName(getCIServiceImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CIServiceImplPort".equals(inputPortName)) {
            return getCIServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server/", "CIServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server/", "CIServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CIServiceImplPort".equals(portName)) {
            setCIServiceImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
