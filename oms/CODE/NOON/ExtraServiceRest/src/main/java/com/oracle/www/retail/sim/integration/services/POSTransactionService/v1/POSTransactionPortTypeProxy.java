package com.oracle.www.retail.sim.integration.services.POSTransactionService.v1;

public class POSTransactionPortTypeProxy implements com.oracle.www.retail.sim.integration.services.POSTransactionService.v1.POSTransactionPortType {
  private String _endpoint = null;
  private com.oracle.www.retail.sim.integration.services.POSTransactionService.v1.POSTransactionPortType pOSTransactionPortType = null;
  
  public POSTransactionPortTypeProxy() {
    _initPOSTransactionPortTypeProxy();
  }
  
  public POSTransactionPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initPOSTransactionPortTypeProxy();
  }
  
  private void _initPOSTransactionPortTypeProxy() {
    try {
    	System.out.println("Inside _initPOSTransactionPortTypeProxy*");	
      pOSTransactionPortType = (new com.oracle.www.retail.sim.integration.services.POSTransactionService.v1.POSTransactionServiceLocator()).getPOSTransactionPort();
      System.out.println("Inside _initPOSTransactionPortTypeProxy**");
      if (pOSTransactionPortType != null) {
    	  System.out.println("Inside pOSTransactionPortType*");
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pOSTransactionPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pOSTransactionPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      System.out.println("After pOSTransactionPortType*");
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pOSTransactionPortType != null)
      ((javax.xml.rpc.Stub)pOSTransactionPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.oracle.www.retail.sim.integration.services.POSTransactionService.v1.POSTransactionPortType getPOSTransactionPortType() {
    if (pOSTransactionPortType == null)
      _initPOSTransactionPortTypeProxy();
    return pOSTransactionPortType;
  }
  
  public com.oracle.www.retail.integration.base.bo.InvocationSuccess.v1.InvocationSuccess processPOSTransactions(com.oracle.www.retail.integration.base.bo.PosTrnColDesc.v1.PosTrnColDesc posTrnColDesc) throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException, com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException{
	System.out.println("Inside processPOSTransactions***");  
    if (pOSTransactionPortType == null)
      _initPOSTransactionPortTypeProxy();
    
    System.out.println("After _initPOSTransactionPortTypeProxy***");
    return pOSTransactionPortType.processPOSTransactions(posTrnColDesc);
  }
  
  public java.lang.String ping(java.lang.String arg0) throws java.rmi.RemoteException{
    if (pOSTransactionPortType == null)
      _initPOSTransactionPortTypeProxy();
    return pOSTransactionPortType.ping(arg0);
  }
  
  
}