/**
 * InvAvailColDesc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.InvAvailColDesc.v1;

public class InvAvailColDesc implements java.io.Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5323783089344993557L;

  private com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.InvAvailDesc[] invAvailDesc;

  private int collection_size;

  public InvAvailColDesc()
  {
  }

  public InvAvailColDesc(com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.InvAvailDesc[] invAvailDesc, int collection_size)
  {
    this.invAvailDesc = invAvailDesc;
    this.collection_size = collection_size;
  }

  /**
   * Gets the invAvailDesc value for this InvAvailColDesc.
   * 
   * @return invAvailDesc
   */
  public com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.InvAvailDesc[] getInvAvailDesc()
  {
    return invAvailDesc;
  }

  /**
   * Sets the invAvailDesc value for this InvAvailColDesc.
   * 
   * @param invAvailDesc
   */
  public void setInvAvailDesc(com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.InvAvailDesc[] invAvailDesc)
  {
    this.invAvailDesc = invAvailDesc;
  }

  public com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.InvAvailDesc getInvAvailDesc(int i)
  {
    return this.invAvailDesc[i];
  }

  public void setInvAvailDesc(int i, com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.InvAvailDesc _value)
  {
    this.invAvailDesc[i] = _value;
  }

  /**
   * Gets the collection_size value for this InvAvailColDesc.
   * 
   * @return collection_size
   */
  public int getCollection_size()
  {
    return collection_size;
  }

  /**
   * Sets the collection_size value for this InvAvailColDesc.
   * 
   * @param collection_size
   */
  public void setCollection_size(int collection_size)
  {
    this.collection_size = collection_size;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj)
  {
    if (!(obj instanceof InvAvailColDesc))
      return false;
    InvAvailColDesc other = (InvAvailColDesc) obj;
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
    _equals = true
        && ((this.invAvailDesc == null && other.getInvAvailDesc() == null) || (this.invAvailDesc != null && java.util.Arrays.equals(this.invAvailDesc, other.getInvAvailDesc())))
        && this.collection_size == other.getCollection_size();
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
    if (getInvAvailDesc() != null)
    {
      for (int i = 0; i < java.lang.reflect.Array.getLength(getInvAvailDesc()); i++)
      {
        java.lang.Object obj = java.lang.reflect.Array.get(getInvAvailDesc(), i);
        if (obj != null && !obj.getClass().isArray())
        {
          _hashCode += obj.hashCode();
        }
      }
    }
    _hashCode += getCollection_size();
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(InvAvailColDesc.class, true);

  static
  {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1", ">InvAvailColDesc"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("invAvailDesc");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "InvAvailDesc"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "InvAvailDesc"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    elemField.setMaxOccursUnbounded(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("collection_size");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1", "collection_size"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
