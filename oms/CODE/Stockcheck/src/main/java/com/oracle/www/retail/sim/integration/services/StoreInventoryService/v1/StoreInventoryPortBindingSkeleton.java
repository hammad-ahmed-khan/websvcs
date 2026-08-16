/**
 * StoreInventoryPortBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1;

public class StoreInventoryPortBindingSkeleton
    implements com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortType, org.apache.axis.wsdl.Skeleton
{
  private com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortType impl;
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
        new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvCriVo/v1", "StrInvCriVo"),
            org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvCriVo/v1", ">StrInvCriVo"),
            com.oracle.www.retail.integration.base.bo.StrInvCriVo.v1.StrInvCriVo.class, false, false),
    };
    _oper = new org.apache.axis.description.OperationDesc("lookupInventoryInStore", _params,
        new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", "StrInvColDesc"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", ">StrInvColDesc"));
    _oper.setElementQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryInStore"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    if (_myOperations.get("lookupInventoryInStore") == null)
    {
      _myOperations.put("lookupInventoryInStore", new java.util.ArrayList());
    }
    ((java.util.List) _myOperations.get("lookupInventoryInStore")).add(_oper);
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
        new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "InvAvailCriVo"),
            org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", ">InvAvailCriVo"),
            com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvAvailCriVo.class, false, false),
    };
    _oper = new org.apache.axis.description.OperationDesc("lookupAvailableInventory", _params,
        new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1", "InvAvailColDesc"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1", ">InvAvailColDesc"));
    _oper.setElementQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupAvailableInventory"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    if (_myOperations.get("lookupAvailableInventory") == null)
    {
      _myOperations.put("lookupAvailableInventory", new java.util.ArrayList());
    }
    ((java.util.List) _myOperations.get("lookupAvailableInventory")).add(_oper);
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
        new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", "StrInvGpCriVo"),
            org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", ">StrInvGpCriVo"),
            com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvGpCriVo.class, false, false),
    };
    _oper = new org.apache.axis.description.OperationDesc("lookupInventoryInTransferZone", _params,
        new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", "StrInvColDesc"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", ">StrInvColDesc"));
    _oper.setElementQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryInTransferZone"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    if (_myOperations.get("lookupInventoryInTransferZone") == null)
    {
      _myOperations.put("lookupInventoryInTransferZone", new java.util.ArrayList());
    }
    ((java.util.List) _myOperations.get("lookupInventoryInTransferZone")).add(_oper);
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
        new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", "StrInvGpCriVo"),
            org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", ">StrInvGpCriVo"),
            com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvGpCriVo.class, false, false),
    };
    _oper = new org.apache.axis.description.OperationDesc("lookupInventoryForBuddyStores", _params,
        new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", "StrInvColDesc"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", ">StrInvColDesc"));
    _oper.setElementQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryForBuddyStores"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    if (_myOperations.get("lookupInventoryForBuddyStores") == null)
    {
      _myOperations.put("lookupInventoryForBuddyStores", new java.util.ArrayList());
    }
    ((java.util.List) _myOperations.get("lookupInventoryForBuddyStores")).add(_oper);
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
    _oper.setElementQName(new javax.xml.namespace.QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "ping"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    if (_myOperations.get("ping") == null)
    {
      _myOperations.put("ping", new java.util.ArrayList());
    }
    ((java.util.List) _myOperations.get("ping")).add(_oper);
  }

  public StoreInventoryPortBindingSkeleton()
  {
    this.impl = new com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortBindingImpl();
  }

  public StoreInventoryPortBindingSkeleton(com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortType impl)
  {
    this.impl = impl;
  }

  public com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc lookupInventoryInStore(
      com.oracle.www.retail.integration.base.bo.StrInvCriVo.v1.StrInvCriVo strInvCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException
  {
    com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc ret = impl.lookupInventoryInStore(strInvCriVo);
    return ret;
  }

  public com.oracle.www.retail.integration.base.bo.InvAvailColDesc.v1.InvAvailColDesc lookupAvailableInventory(
      com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvAvailCriVo invAvailCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException
  {
    com.oracle.www.retail.integration.base.bo.InvAvailColDesc.v1.InvAvailColDesc ret = impl.lookupAvailableInventory(invAvailCriVo);
    return ret;
  }

  public com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc lookupInventoryInTransferZone(
      com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvGpCriVo strInvGpCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException
  {
    com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc ret = impl.lookupInventoryInTransferZone(strInvGpCriVo);
    return ret;
  }

  public com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc lookupInventoryForBuddyStores(
      com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvGpCriVo strInvGpCriVo)
      throws java.rmi.RemoteException, com.oracle.www.retail.integration.services.exception.v1.IllegalArgumentWSFaultException,
      com.oracle.www.retail.integration.services.exception.v1.IllegalStateWSFaultException, com.oracle.www.retail.integration.services.exception.v1.ValidationWSFaultException
  {
    com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1.StrInvColDesc ret = impl.lookupInventoryForBuddyStores(strInvGpCriVo);
    return ret;
  }

  public java.lang.String ping(java.lang.String arg0) throws java.rmi.RemoteException
  {
    java.lang.String ret = impl.ping(arg0);
    return ret;
  }

}
