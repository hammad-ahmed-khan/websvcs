/**
 * InventoryDetailPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.rms.integration.services.InventoryDetailService.v1;

public interface InventoryDetailPortType extends java.rmi.Remote
{
  public com.oracle.www.retail.integration.base.bo.InvAvailColDesc.v1.InvAvailColDesc lookupInvAvailCriVo(
      com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvAvailCriVo invAvailCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException;

  public java.lang.String ping(java.lang.String arg0) throws java.rmi.RemoteException;
}
