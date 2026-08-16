/**
 * InvAvailCriVo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1;

public class InvAvailCriVo implements java.io.Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -6820382885909800146L;

  private java.lang.String[] items;

  private com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvLocation[] invLocation;

  private com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Store_pickup_ind store_pickup_ind;

  public InvAvailCriVo()
  {
  }

  public InvAvailCriVo(java.lang.String[] items, com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvLocation[] invLocation,
      com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Store_pickup_ind store_pickup_ind)
  {
    this.items = items;
    this.invLocation = invLocation;
    this.store_pickup_ind = store_pickup_ind;
  }

  /**
   * Gets the items value for this InvAvailCriVo.
   * 
   * @return items
   */
  public java.lang.String[] getItems()
  {
    return items;
  }

  /**
   * Sets the items value for this InvAvailCriVo.
   * 
   * @param items
   */
  public void setItems(java.lang.String[] items)
  {
    this.items = items;
  }

  public java.lang.String getItems(int i)
  {
    return this.items[i];
  }

  public void setItems(int i, java.lang.String _value)
  {
    this.items[i] = _value;
  }

  /**
   * Gets the invLocation value for this InvAvailCriVo.
   * 
   * @return invLocation
   */
  public com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvLocation[] getInvLocation()
  {
    return invLocation;
  }

  /**
   * Sets the invLocation value for this InvAvailCriVo.
   * 
   * @param invLocation
   */
  public void setInvLocation(com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvLocation[] invLocation)
  {
    this.invLocation = invLocation;
  }

  public com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvLocation getInvLocation(int i)
  {
    return this.invLocation[i];
  }

  public void setInvLocation(int i, com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.InvLocation _value)
  {
    this.invLocation[i] = _value;
  }

  /**
   * Gets the store_pickup_ind value for this InvAvailCriVo.
   * 
   * @return store_pickup_ind
   */
  public com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Store_pickup_ind getStore_pickup_ind()
  {
    return store_pickup_ind;
  }

  /**
   * Sets the store_pickup_ind value for this InvAvailCriVo.
   * 
   * @param store_pickup_ind
   */
  public void setStore_pickup_ind(com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Store_pickup_ind store_pickup_ind)
  {
    this.store_pickup_ind = store_pickup_ind;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj)
  {
    if (!(obj instanceof InvAvailCriVo))
      return false;
    InvAvailCriVo other = (InvAvailCriVo) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null)
    {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true && ((this.items == null && other.getItems() == null) || (this.items != null && java.util.Arrays.equals(this.items, other.getItems())))
        && ((this.invLocation == null && other.getInvLocation() == null) || (this.invLocation != null && java.util.Arrays.equals(this.invLocation, other.getInvLocation())))
        && ((this.store_pickup_ind == null && other.getStore_pickup_ind() == null) || (this.store_pickup_ind != null && this.store_pickup_ind.equals(other.getStore_pickup_ind())));
    __equalsCalc = null;
    return _equals;
  }

  private boolean __hashCodeCalc = false;

  public synchronized int hashCode()
  {
    if (__hashCodeCalc)
    {
      return 0;
    }
    __hashCodeCalc = true;
    int _hashCode = 1;
    if (getItems() != null)
    {
      for (int i = 0; i < java.lang.reflect.Array.getLength(getItems()); i++)
      {
        java.lang.Object obj = java.lang.reflect.Array.get(getItems(), i);
        if (obj != null && !obj.getClass().isArray())
        {
          _hashCode += obj.hashCode();
        }
      }
    }
    if (getInvLocation() != null)
    {
      for (int i = 0; i < java.lang.reflect.Array.getLength(getInvLocation()); i++)
      {
        java.lang.Object obj = java.lang.reflect.Array.get(getInvLocation(), i);
        if (obj != null && !obj.getClass().isArray())
        {
          _hashCode += obj.hashCode();
        }
      }
    }
    if (getStore_pickup_ind() != null)
    {
      _hashCode += getStore_pickup_ind().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(InvAvailCriVo.class, true);

  static
  {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", ">InvAvailCriVo"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("items");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "items"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    elemField.setMaxOccursUnbounded(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("invLocation");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "InvLocation"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "InvLocation"));
    elemField.setNillable(false);
    elemField.setMaxOccursUnbounded(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("store_pickup_ind");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "store_pickup_ind"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "store_pickup_ind"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }

  /**
   * Return type metadata object
   */
  public static org.apache.axis.description.TypeDesc getTypeDesc()
  {
    return typeDesc;
  }

  /**
   * Get Custom Serializer
   */
  public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType)
  {
    return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
  }

  /**
   * Get Custom Deserializer
   */
  public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType)
  {
    return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
  }

}
