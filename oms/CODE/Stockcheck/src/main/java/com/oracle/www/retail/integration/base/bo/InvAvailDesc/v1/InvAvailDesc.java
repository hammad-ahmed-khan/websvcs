/**
 * InvAvailDesc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1;

public class InvAvailDesc implements java.io.Serializable
{
  private java.lang.String item;

  private long location;

  private com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Loc_type loc_type;

  private java.lang.Integer channel_id;

  private java.math.BigDecimal available_qty;

  private java.lang.String unit_of_measure;

  private java.util.Calendar available_date;

  private com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Pack_calculate_ind pack_calculate_ind;

  public InvAvailDesc()
  {
  }

  public InvAvailDesc(java.lang.String item, long location, com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Loc_type loc_type, java.lang.Integer channel_id,
      java.math.BigDecimal available_qty, java.lang.String unit_of_measure, java.util.Calendar available_date,
      com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Pack_calculate_ind pack_calculate_ind)
  {
    this.item = item;
    this.location = location;
    this.loc_type = loc_type;
    this.channel_id = channel_id;
    this.available_qty = available_qty;
    this.unit_of_measure = unit_of_measure;
    this.available_date = available_date;
    this.pack_calculate_ind = pack_calculate_ind;
  }

  /**
   * Gets the item value for this InvAvailDesc.
   * 
   * @return item
   */
  public java.lang.String getItem()
  {
    return item;
  }

  /**
   * Sets the item value for this InvAvailDesc.
   * 
   * @param item
   */
  public void setItem(java.lang.String item)
  {
    this.item = item;
  }

  /**
   * Gets the location value for this InvAvailDesc.
   * 
   * @return location
   */
  public long getLocation()
  {
    return location;
  }

  /**
   * Sets the location value for this InvAvailDesc.
   * 
   * @param location
   */
  public void setLocation(long location)
  {
    this.location = location;
  }

  /**
   * Gets the loc_type value for this InvAvailDesc.
   * 
   * @return loc_type
   */
  public com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Loc_type getLoc_type()
  {
    return loc_type;
  }

  /**
   * Sets the loc_type value for this InvAvailDesc.
   * 
   * @param loc_type
   */
  public void setLoc_type(com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Loc_type loc_type)
  {
    this.loc_type = loc_type;
  }

  /**
   * Gets the channel_id value for this InvAvailDesc.
   * 
   * @return channel_id
   */
  public java.lang.Integer getChannel_id()
  {
    return channel_id;
  }

  /**
   * Sets the channel_id value for this InvAvailDesc.
   * 
   * @param channel_id
   */
  public void setChannel_id(java.lang.Integer channel_id)
  {
    this.channel_id = channel_id;
  }

  /**
   * Gets the available_qty value for this InvAvailDesc.
   * 
   * @return available_qty
   */
  public java.math.BigDecimal getAvailable_qty()
  {
    return available_qty;
  }

  /**
   * Sets the available_qty value for this InvAvailDesc.
   * 
   * @param available_qty
   */
  public void setAvailable_qty(java.math.BigDecimal available_qty)
  {
    this.available_qty = available_qty;
  }

  /**
   * Gets the unit_of_measure value for this InvAvailDesc.
   * 
   * @return unit_of_measure
   */
  public java.lang.String getUnit_of_measure()
  {
    return unit_of_measure;
  }

  /**
   * Sets the unit_of_measure value for this InvAvailDesc.
   * 
   * @param unit_of_measure
   */
  public void setUnit_of_measure(java.lang.String unit_of_measure)
  {
    this.unit_of_measure = unit_of_measure;
  }

  /**
   * Gets the available_date value for this InvAvailDesc.
   * 
   * @return available_date
   */
  public java.util.Calendar getAvailable_date()
  {
    return available_date;
  }

  /**
   * Sets the available_date value for this InvAvailDesc.
   * 
   * @param available_date
   */
  public void setAvailable_date(java.util.Calendar available_date)
  {
    this.available_date = available_date;
  }

  /**
   * Gets the pack_calculate_ind value for this InvAvailDesc.
   * 
   * @return pack_calculate_ind
   */
  public com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Pack_calculate_ind getPack_calculate_ind()
  {
    return pack_calculate_ind;
  }

  /**
   * Sets the pack_calculate_ind value for this InvAvailDesc.
   * 
   * @param pack_calculate_ind
   */
  public void setPack_calculate_ind(com.oracle.www.retail.integration.base.bo.InvAvailDesc.v1.Pack_calculate_ind pack_calculate_ind)
  {
    this.pack_calculate_ind = pack_calculate_ind;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj)
  {
    if (!(obj instanceof InvAvailDesc))
      return false;
    InvAvailDesc other = (InvAvailDesc) obj;
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
    _equals = true && ((this.item == null && other.getItem() == null) || (this.item != null && this.item.equals(other.getItem()))) && this.location == other.getLocation()
        && ((this.loc_type == null && other.getLoc_type() == null) || (this.loc_type != null && this.loc_type.equals(other.getLoc_type())))
        && ((this.channel_id == null && other.getChannel_id() == null) || (this.channel_id != null && this.channel_id.equals(other.getChannel_id())))
        && ((this.available_qty == null && other.getAvailable_qty() == null) || (this.available_qty != null && this.available_qty.equals(other.getAvailable_qty())))
        && ((this.unit_of_measure == null && other.getUnit_of_measure() == null) || (this.unit_of_measure != null && this.unit_of_measure.equals(other.getUnit_of_measure())))
        && ((this.available_date == null && other.getAvailable_date() == null) || (this.available_date != null && this.available_date.equals(other.getAvailable_date())))
        && ((this.pack_calculate_ind == null && other.getPack_calculate_ind() == null)
            || (this.pack_calculate_ind != null && this.pack_calculate_ind.equals(other.getPack_calculate_ind())));
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
    if (getItem() != null)
    {
      _hashCode += getItem().hashCode();
    }
    _hashCode += new Long(getLocation()).hashCode();
    if (getLoc_type() != null)
    {
      _hashCode += getLoc_type().hashCode();
    }
    if (getChannel_id() != null)
    {
      _hashCode += getChannel_id().hashCode();
    }
    if (getAvailable_qty() != null)
    {
      _hashCode += getAvailable_qty().hashCode();
    }
    if (getUnit_of_measure() != null)
    {
      _hashCode += getUnit_of_measure().hashCode();
    }
    if (getAvailable_date() != null)
    {
      _hashCode += getAvailable_date().hashCode();
    }
    if (getPack_calculate_ind() != null)
    {
      _hashCode += getPack_calculate_ind().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(InvAvailDesc.class, true);

  static
  {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", ">InvAvailDesc"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("item");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "item"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("location");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "location"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("loc_type");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "loc_type"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "loc_type"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("channel_id");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "channel_id"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("available_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "available_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unit_of_measure");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "unit_of_measure"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("available_date");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "available_date"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("pack_calculate_ind");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "pack_calculate_ind"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1", "pack_calculate_ind"));
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
