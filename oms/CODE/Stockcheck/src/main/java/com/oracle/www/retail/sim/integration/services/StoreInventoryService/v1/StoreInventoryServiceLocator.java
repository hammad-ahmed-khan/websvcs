/**
 * StoreInventoryServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class StoreInventoryServiceLocator extends org.apache.axis.client.Service
    implements com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryService
{

  private static final Logger logger = LogManager.getLogger(StoreInventoryServiceLocator.class.getName());

  public StoreInventoryServiceLocator()
  {
  }

  public StoreInventoryServiceLocator(org.apache.axis.EngineConfiguration config)
  {
    super(config);
  }

  public StoreInventoryServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException
  {
    super(wsdlLoc, sName);
  }

  // Use to get a proxy class for StoreInventoryPort
  private java.lang.String StoreInventoryPort_address = null;

  public java.lang.String getStoreInventoryPortAddress()
  {
    return StoreInventoryPort_address;
  }

  // The WSDD service name defaults to the port name.
  private java.lang.String StoreInventoryPortWSDDServiceName = "StoreInventoryPort";

  public java.lang.String getStoreInventoryPortWSDDServiceName()
  {
    return StoreInventoryPortWSDDServiceName;
  }

  public void setStoreInventoryPortWSDDServiceName(java.lang.String name)
  {
    StoreInventoryPortWSDDServiceName = name;
  }

  public com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortType getStoreInventoryPort() throws javax.xml.rpc.ServiceException
  {
    java.net.URL endpoint = null;
    try
    {
      Resource resource = new ClassPathResource("/application.properties");
      Properties props = PropertiesLoaderUtils.loadProperties(resource);
      String urlValue = props.getProperty("StoreInventoryPort_address").trim();
      logger.info("urlValue for ST " + urlValue);
      StoreInventoryPort_address = urlValue;
      endpoint = new java.net.URL(StoreInventoryPort_address);
    }
    catch (java.net.MalformedURLException e)
    {
      throw new javax.xml.rpc.ServiceException(e);
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return getStoreInventoryPort(endpoint);
  }

  public com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortType getStoreInventoryPort(java.net.URL portAddress)
      throws javax.xml.rpc.ServiceException
  {
    try
    {
      com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortBindingStub _stub = new com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortBindingStub(
          portAddress, this);
      _stub.setPortName(getStoreInventoryPortWSDDServiceName());
      return _stub;
    }
    catch (org.apache.axis.AxisFault e)
    {
      return null;
    }
  }

  public void setStoreInventoryPortEndpointAddress(java.lang.String address)
  {
    StoreInventoryPort_address = address;
  }

  /**
   * For the given interface, get the stub implementation. If this service has no port for the given interface, then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException
  {
    try
    {
      if (com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortType.class.isAssignableFrom(serviceEndpointInterface))
      {
        com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortBindingStub _stub = new com.oracle.www.retail.sim.integration.services.StoreInventoryService.v1.StoreInventoryPortBindingStub(
            new java.net.URL(StoreInventoryPort_address), this);
        _stub.setPortName(getStoreInventoryPortWSDDServiceName());
        return _stub;
      }
    }
    catch (java.lang.Throwable t)
    {
      throw new javax.xml.rpc.ServiceException(t);
    }
    throw new javax.xml.rpc.ServiceException(
        "There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
  }

  /**
   * For the given interface, get the stub implementation. If this service has no port for the given interface, then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException
  {
    if (portName == null)
    {
      return getPort(serviceEndpointInterface);
    }
    java.lang.String inputPortName = portName.getLocalPart();
    if ("StoreInventoryPort".equals(inputPortName))
    {
      return getStoreInventoryPort();
    }
    else
    {
      java.rmi.Remote _stub = getPort(serviceEndpointInterface);
      ((org.apache.axis.client.Stub) _stub).setPortName(portName);
      return _stub;
    }
  }

  public javax.xml.namespace.QName getServiceName()
  {
    return new javax.xml.namespace.QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "StoreInventoryService");
  }

  private java.util.HashSet ports = null;

  public java.util.Iterator getPorts()
  {
    if (ports == null)
    {
      ports = new java.util.HashSet();
      ports.add(new javax.xml.namespace.QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "StoreInventoryPort"));
    }
    return ports.iterator();
  }

  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException
  {

    if ("StoreInventoryPort".equals(portName))
    {
      setStoreInventoryPortEndpointAddress(address);
    }
    else
    { // Unknown Port Name
      throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
    }
  }

  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException
  {
    setEndpointAddress(portName.getLocalPart(), address);
  }

}
