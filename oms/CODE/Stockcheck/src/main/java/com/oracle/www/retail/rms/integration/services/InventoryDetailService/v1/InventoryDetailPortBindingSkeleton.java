/**
 * InventoryDetailPortBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.rms.integration.services.InventoryDetailService.v1;

public class InventoryDetailPortBindingSkeleton
    implements com.oracle.www.retail.rms.integration.services.InventoryDetailService.v1.InventoryDetailPortType, org.apache.axis.wsdl.Skeleton
{
  private com.oracle.www.retail.rms.integration.services.InventoryDetailService.v1.InventoryDetailPortType impl;
  private static java.util.Map _myOperations = new java.util.Hashtable();
  private static java.util.Collection _myOperationsList = new java.util.ArrayList();

  /**
   * Returns List of OperationDesc objects with this name
   */
  public static java.util.List getOperationDescByName(java.lang.String methodName)
  {
    return (java.util.List) _myOperations.get(methodName);
  }

  /**
   * Returns Collection of OperationDescs
   */
  public static java.util.Collection getOperationDescs()
  {
    return _myOperationsList;
  }

  static
  {
    org.apache.axis.description.OperationDesc _oper;
    org.apache.axis.description.FaultDesc _fault;
    org.apache.axis.description.ParameterDesc[] _params;
    _params = new org.apache.axis.description.ParameterDesc[]
    {
        new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "InvAvailCriVo"),
            org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", ">InvAvailCriVo"),
            com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvAvailCriVo.class, false, false),
    };
    _oper = new org.apache.axis.description.OperationDesc("lookupInvAvailCriVo", _params,
        new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1", "InvAvailColDesc"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1", ">InvAvailColDesc"));
    _oper.setElementQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/rms/integration/services/InventoryDetailService/v1", "lookupInvAvailCriVo"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    if (_myOperations.get("lookupInvAvailCriVo") == null)
    {
      _myOperations.put("lookupInvAvailCriVo", new java.util.ArrayList());
    }
    ((java.util.List) _myOperations.get("lookupInvAvailCriVo")).add(_oper);
    _fault = new org.apache.axis.description.FaultDesc();
    _fault.setName("IllegalArgumentWSFaultException");
    _fault.setQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "IllegalArgumentWSFaultException"));
    _fault.setClassName("com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException");
    _fault.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", ">IllegalArgumentWSFaultException"));
    _oper.addFault(_fault);
    _fault = new org.apache.axis.description.FaultDesc();
    _fault.setName("IllegalStateWSFaultException");
    _fault.setQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "IllegalStateWSFaultException"));
    _fault.setClassName("com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException");
    _fault.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", ">IllegalStateWSFaultException"));
    _oper.addFault(_fault);
    _fault = new org.apache.axis.description.FaultDesc();
    _fault.setName("ValidationWSFaultException");
    _fault.setQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "ValidationWSFaultException"));
    _fault.setClassName("com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException");
    _fault.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", ">ValidationWSFaultException"));
    _oper.addFault(_fault);
    _params = new org.apache.axis.description.ParameterDesc[]
    {
        new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN,
            new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
    };
    _oper = new org.apache.axis.description.OperationDesc("ping", _params, new javax.xml.namespace.QName("", "return"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    _oper.setElementQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/rms/integration/services/InventoryDetailService/v1", "ping"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    if (_myOperations.get("ping") == null)
    {
      _myOperations.put("ping", new java.util.ArrayList());
    }
    ((java.util.List) _myOperations.get("ping")).add(_oper);
  }

  public InventoryDetailPortBindingSkeleton()
  {
    this.impl = new com.oracle.www.retail.rms.integration.services.InventoryDetailService.v1.InventoryDetailPortBindingImpl();
  }

  public InventoryDetailPortBindingSkeleton(com.oracle.www.retail.rms.integration.services.InventoryDetailService.v1.InventoryDetailPortType impl)
  {
    this.impl = impl;
  }

  public com.oracle.www.retail.integration.base.bo.InvAvailColDesc.v1.InvAvailColDesc lookupInvAvailCriVo(
      com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvAvailCriVo invAvailCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException
  {
    com.oracle.www.retail.integration.base.bo.InvAvailColDesc.v1.InvAvailColDesc ret = impl.lookupInvAvailCriVo(invAvailCriVo);
    return ret;
  }

  public java.lang.String ping(java.lang.String arg0) throws java.rmi.RemoteException
  {
    java.lang.String ret = impl.ping(arg0);
    return ret;
  }

}
