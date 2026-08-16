/**
 * InvLocation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1;

public class InvLocation implements java.io.Serializable
{
  private long location;

  private com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Loc_type loc_type;

  private java.lang.Integer channel_id;

  public InvLocation()
  {
  }

  public InvLocation(long location, com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Loc_type loc_type, java.lang.Integer channel_id)
  {
    this.location = location;
    this.loc_type = loc_type;
    this.channel_id = channel_id;
  }

  /**
   * Gets the location value for this InvLocation.
   * 
   * @return location
   */
  public long getLocation()
  {
    return location;
  }

  /**
   * Sets the location value for this InvLocation.
   * 
   * @param location
   */
  public void setLocation(long location)
  {
    this.location = location;
  }

  /**
   * Gets the loc_type value for this InvLocation.
   * 
   * @return loc_type
   */
  public com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Loc_type getLoc_type()
  {
    return loc_type;
  }

  /**
   * Sets the loc_type value for this InvLocation.
   * 
   * @param loc_type
   */
  public void setLoc_type(com.oracle.www.retail.integration.base.bo.InvAvailCriVo.v1.Loc_type loc_type)
  {
    this.loc_type = loc_type;
  }

  /**
   * Gets the channel_id value for this InvLocation.
   * 
   * @return channel_id
   */
  public java.lang.Integer getChannel_id()
  {
    return channel_id;
  }

  /**
   * Sets the channel_id value for this InvLocation.
   * 
   * @param channel_id
   */
  public void setChannel_id(java.lang.Integer channel_id)
  {
    this.channel_id = channel_id;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj)
  {
    if (!(obj instanceof InvLocation))
      return false;
    InvLocation other = (InvLocation) obj;
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
    _equals = true && this.location == other.getLocation()
        && ((this.loc_type == null && other.getLoc_type() == null) || (this.loc_type != null && this.loc_type.equals(other.getLoc_type())))
        && ((this.channel_id == null && other.getChannel_id() == null) || (this.channel_id != null && this.channel_id.equals(other.getChannel_id())));
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
    _hashCode += new Long(getLocation()).hashCode();
    if (getLoc_type() != null)
    {
      _hashCode += getLoc_type().hashCode();
    }
    if (getChannel_id() != null)
    {
      _hashCode += getChannel_id().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(InvLocation.class, true);

  static
  {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", ">InvLocation"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("location");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "location"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("loc_type");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "loc_type"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "loc_type"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("channel_id");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1", "channel_id"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
    elemField.setMinOccurs(0);
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
