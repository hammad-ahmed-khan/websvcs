/**
 * POSTransactionPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.sim.integration.services.POSTransactionService.v1;

public interface POSTransactionPortType extends java.rmi.Remote {
    public com.oracle.www.retail.integration.base.bo.InvocationSuccess.v1.InvocationSuccess processPOSTransactions(com.oracle.www.retail.integration.base.bo.PosTrnColDesc.v1.PosTrnColDesc posTrnColDesc) throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException, com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException;
    public java.lang.String ping(java.lang.String arg0) throws java.rmi.RemoteException;
}
