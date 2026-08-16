/**
 * StrInvColDesc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.StrInvColDesc.v1;

public class StrInvColDesc implements java.io.Serializable
{
  private com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvDesc[] strInvDesc;

  private int collection_size;

  public StrInvColDesc()
  {
  }

  public StrInvColDesc(com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvDesc[] strInvDesc, int collection_size)
  {
    this.strInvDesc = strInvDesc;
    this.collection_size = collection_size;
  }

  /**
   * Gets the strInvDesc value for this StrInvColDesc.
   * 
   * @return strInvDesc
   */
  public com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvDesc[] getStrInvDesc()
  {
    return strInvDesc;
  }

  /**
   * Sets the strInvDesc value for this StrInvColDesc.
   * 
   * @param strInvDesc
   */
  public void setStrInvDesc(com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvDesc[] strInvDesc)
  {
    this.strInvDesc = strInvDesc;
  }

  public com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvDesc getStrInvDesc(int i)
  {
    return this.strInvDesc[i];
  }

  public void setStrInvDesc(int i, com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvDesc _value)
  {
    this.strInvDesc[i] = _value;
  }

  /**
   * Gets the collection_size value for this StrInvColDesc.
   * 
   * @return collection_size
   */
  public int getCollection_size()
  {
    return collection_size;
  }

  /**
   * Sets the collection_size value for this StrInvColDesc.
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
    if (!(obj instanceof StrInvColDesc))
      return false;
    StrInvColDesc other = (StrInvColDesc) obj;
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
    _equals = true && ((this.strInvDesc == null && other.getStrInvDesc() == null) || (this.strInvDesc != null && java.util.Arrays.equals(this.strInvDesc, other.getStrInvDesc())))
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
    if (getStrInvDesc() != null)
    {
      for (int i = 0; i < java.lang.reflect.Array.getLength(getStrInvDesc()); i++)
      {
        java.lang.Object obj = java.lang.reflect.Array.get(getStrInvDesc(), i);
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
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(StrInvColDesc.class, true);

  static
  {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", ">StrInvColDesc"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("strInvDesc");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "StrInvDesc"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "StrInvDesc"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    elemField.setMaxOccursUnbounded(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("collection_size");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1", "collection_size"));
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
