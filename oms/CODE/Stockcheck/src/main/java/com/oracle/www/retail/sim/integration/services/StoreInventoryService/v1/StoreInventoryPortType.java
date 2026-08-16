/**
 * StoreInventoryPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1;

public interface StoreInventoryPortType extends java.rmi.Remote
{
  public com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc lookupInventoryInStore(
      com.oracle.www.retail.integration.base.bo.StrInvCriVo.v1.StrInvCriVo strInvCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException;

  public com.oracle.www.retail.integration.base.bo.InvAvailColDesc.v1.InvAvailColDesc lookupAvailableInventory(
      com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvAvailCriVo invAvailCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException;

  public com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc lookupInventoryInTransferZone(
      com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvGpCriVo strInvGpCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException;

  public com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc lookupInventoryForBuddyStores(
      com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvGpCriVo strInvGpCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException;

  public java.lang.String ping(java.lang.String arg0) throws java.rmi.RemoteException;
}
