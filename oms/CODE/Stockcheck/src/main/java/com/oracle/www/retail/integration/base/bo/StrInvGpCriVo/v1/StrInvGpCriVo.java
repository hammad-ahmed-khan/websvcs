/**
 * StrInvGpCriVo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1;

public class StrInvGpCriVo implements java.io.Serializable
{
  private java.lang.String[] item_id_col;

  private long store_id;

  private com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvUomType uom_type;

  public StrInvGpCriVo()
  {
  }

  public StrInvGpCriVo(java.lang.String[] item_id_col, long store_id, com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvUomType uom_type)
  {
    this.item_id_col = item_id_col;
    this.store_id = store_id;
    this.uom_type = uom_type;
  }

  /**
   * Gets the item_id_col value for this StrInvGpCriVo.
   * 
   * @return item_id_col
   */
  public java.lang.String[] getItem_id_col()
  {
    return item_id_col;
  }

  /**
   * Sets the item_id_col value for this StrInvGpCriVo.
   * 
   * @param item_id_col
   */
  public void setItem_id_col(java.lang.String[] item_id_col)
  {
    this.item_id_col = item_id_col;
  }

  public java.lang.String getItem_id_col(int i)
  {
    return this.item_id_col[i];
  }

  public void setItem_id_col(int i, java.lang.String _value)
  {
    this.item_id_col[i] = _value;
  }

  /**
   * Gets the store_id value for this StrInvGpCriVo.
   * 
   * @return store_id
   */
  public long getStore_id()
  {
    return store_id;
  }

  /**
   * Sets the store_id value for this StrInvGpCriVo.
   * 
   * @param store_id
   */
  public void setStore_id(long store_id)
  {
    this.store_id = store_id;
  }

  /**
   * Gets the uom_type value for this StrInvGpCriVo.
   * 
   * @return uom_type
   */
  public com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvUomType getUom_type()
  {
    return uom_type;
  }

  /**
   * Sets the uom_type value for this StrInvGpCriVo.
   * 
   * @param uom_type
   */
  public void setUom_type(com.oracle.www.retail.integration.base.bo.StrInvGpCriVo.v1.StrInvUomType uom_type)
  {
    this.uom_type = uom_type;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj)
  {
    if (!(obj instanceof StrInvGpCriVo))
      return false;
    StrInvGpCriVo other = (StrInvGpCriVo) obj;
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
        && ((this.item_id_col == null && other.getItem_id_col() == null) || (this.item_id_col != null && java.util.Arrays.equals(this.item_id_col, other.getItem_id_col())))
        && this.store_id == other.getStore_id() && ((this.uom_type == null && other.getUom_type() == null) || (this.uom_type != null && this.uom_type.equals(other.getUom_type())));
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
    if (getItem_id_col() != null)
    {
      for (int i = 0; i < java.lang.reflect.Array.getLength(getItem_id_col()); i++)
      {
        java.lang.Object obj = java.lang.reflect.Array.get(getItem_id_col(), i);
        if (obj != null && !obj.getClass().isArray())
        {
          _hashCode += obj.hashCode();
        }
      }
    }
    _hashCode += new Long(getStore_id()).hashCode();
    if (getUom_type() != null)
    {
      _hashCode += getUom_type().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(StrInvGpCriVo.class, true);

  static
  {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", ">StrInvGpCriVo"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("item_id_col");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", "item_id_col"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    elemField.setMaxOccursUnbounded(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("store_id");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", "store_id"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("uom_type");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", "uom_type"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1", "StrInvUomType"));
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
