/**
 * StrInvDesc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.StrInvDesc.v1;

public class StrInvDesc implements java.io.Serializable
{
  private java.lang.String item_id;

  private long store_id;

  private boolean is_ranged;

  private boolean is_estimated;

  private java.lang.String uom_code;

  private java.math.BigDecimal case_size;

  private java.math.BigDecimal stock_on_hand_qty;

  private java.math.BigDecimal back_room_qty;

  private java.math.BigDecimal shop_floor_qty;

  private java.math.BigDecimal delivery_bay_qty;

  private java.math.BigDecimal available_qty;

  private java.math.BigDecimal unavailable_qty;

  private java.math.BigDecimal nonsell_total_qty;

  private java.math.BigDecimal on_order_qty;

  private java.math.BigDecimal in_transit_qty;

  private java.math.BigDecimal cust_reserved_qty;

  private java.math.BigDecimal transfer_reserved_qty;

  private java.math.BigDecimal vendor_return_qty;

  private java.math.BigDecimal allocation_total_qty;

  private com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvNslQty[] strInvNslQty;

  public StrInvDesc()
  {
  }

  public StrInvDesc(java.lang.String item_id, long store_id, boolean is_ranged, boolean is_estimated, java.lang.String uom_code, java.math.BigDecimal case_size,
      java.math.BigDecimal stock_on_hand_qty, java.math.BigDecimal back_room_qty, java.math.BigDecimal shop_floor_qty, java.math.BigDecimal delivery_bay_qty,
      java.math.BigDecimal available_qty, java.math.BigDecimal unavailable_qty, java.math.BigDecimal nonsell_total_qty, java.math.BigDecimal on_order_qty,
      java.math.BigDecimal in_transit_qty, java.math.BigDecimal cust_reserved_qty, java.math.BigDecimal transfer_reserved_qty, java.math.BigDecimal vendor_return_qty,
      java.math.BigDecimal allocation_total_qty, com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvNslQty[] strInvNslQty)
  {
    this.item_id = item_id;
    this.store_id = store_id;
    this.is_ranged = is_ranged;
    this.is_estimated = is_estimated;
    this.uom_code = uom_code;
    this.case_size = case_size;
    this.stock_on_hand_qty = stock_on_hand_qty;
    this.back_room_qty = back_room_qty;
    this.shop_floor_qty = shop_floor_qty;
    this.delivery_bay_qty = delivery_bay_qty;
    this.available_qty = available_qty;
    this.unavailable_qty = unavailable_qty;
    this.nonsell_total_qty = nonsell_total_qty;
    this.on_order_qty = on_order_qty;
    this.in_transit_qty = in_transit_qty;
    this.cust_reserved_qty = cust_reserved_qty;
    this.transfer_reserved_qty = transfer_reserved_qty;
    this.vendor_return_qty = vendor_return_qty;
    this.allocation_total_qty = allocation_total_qty;
    this.strInvNslQty = strInvNslQty;
  }

  /**
   * Gets the item_id value for this StrInvDesc.
   * 
   * @return item_id
   */
  public java.lang.String getItem_id()
  {
    return item_id;
  }

  /**
   * Sets the item_id value for this StrInvDesc.
   * 
   * @param item_id
   */
  public void setItem_id(java.lang.String item_id)
  {
    this.item_id = item_id;
  }

  /**
   * Gets the store_id value for this StrInvDesc.
   * 
   * @return store_id
   */
  public long getStore_id()
  {
    return store_id;
  }

  /**
   * Sets the store_id value for this StrInvDesc.
   * 
   * @param store_id
   */
  public void setStore_id(long store_id)
  {
    this.store_id = store_id;
  }

  /**
   * Gets the is_ranged value for this StrInvDesc.
   * 
   * @return is_ranged
   */
  public boolean isIs_ranged()
  {
    return is_ranged;
  }

  /**
   * Sets the is_ranged value for this StrInvDesc.
   * 
   * @param is_ranged
   */
  public void setIs_ranged(boolean is_ranged)
  {
    this.is_ranged = is_ranged;
  }

  /**
   * Gets the is_estimated value for this StrInvDesc.
   * 
   * @return is_estimated
   */
  public boolean isIs_estimated()
  {
    return is_estimated;
  }

  /**
   * Sets the is_estimated value for this StrInvDesc.
   * 
   * @param is_estimated
   */
  public void setIs_estimated(boolean is_estimated)
  {
    this.is_estimated = is_estimated;
  }

  /**
   * Gets the uom_code value for this StrInvDesc.
   * 
   * @return uom_code
   */
  public java.lang.String getUom_code()
  {
    return uom_code;
  }

  /**
   * Sets the uom_code value for this StrInvDesc.
   * 
   * @param uom_code
   */
  public void setUom_code(java.lang.String uom_code)
  {
    this.uom_code = uom_code;
  }

  /**
   * Gets the case_size value for this StrInvDesc.
   * 
   * @return case_size
   */
  public java.math.BigDecimal getCase_size()
  {
    return case_size;
  }

  /**
   * Sets the case_size value for this StrInvDesc.
   * 
   * @param case_size
   */
  public void setCase_size(java.math.BigDecimal case_size)
  {
    this.case_size = case_size;
  }

  /**
   * Gets the stock_on_hand_qty value for this StrInvDesc.
   * 
   * @return stock_on_hand_qty
   */
  public java.math.BigDecimal getStock_on_hand_qty()
  {
    return stock_on_hand_qty;
  }

  /**
   * Sets the stock_on_hand_qty value for this StrInvDesc.
   * 
   * @param stock_on_hand_qty
   */
  public void setStock_on_hand_qty(java.math.BigDecimal stock_on_hand_qty)
  {
    this.stock_on_hand_qty = stock_on_hand_qty;
  }

  /**
   * Gets the back_room_qty value for this StrInvDesc.
   * 
   * @return back_room_qty
   */
  public java.math.BigDecimal getBack_room_qty()
  {
    return back_room_qty;
  }

  /**
   * Sets the back_room_qty value for this StrInvDesc.
   * 
   * @param back_room_qty
   */
  public void setBack_room_qty(java.math.BigDecimal back_room_qty)
  {
    this.back_room_qty = back_room_qty;
  }

  /**
   * Gets the shop_floor_qty value for this StrInvDesc.
   * 
   * @return shop_floor_qty
   */
  public java.math.BigDecimal getShop_floor_qty()
  {
    return shop_floor_qty;
  }

  /**
   * Sets the shop_floor_qty value for this StrInvDesc.
   * 
   * @param shop_floor_qty
   */
  public void setShop_floor_qty(java.math.BigDecimal shop_floor_qty)
  {
    this.shop_floor_qty = shop_floor_qty;
  }

  /**
   * Gets the delivery_bay_qty value for this StrInvDesc.
   * 
   * @return delivery_bay_qty
   */
  public java.math.BigDecimal getDelivery_bay_qty()
  {
    return delivery_bay_qty;
  }

  /**
   * Sets the delivery_bay_qty value for this StrInvDesc.
   * 
   * @param delivery_bay_qty
   */
  public void setDelivery_bay_qty(java.math.BigDecimal delivery_bay_qty)
  {
    this.delivery_bay_qty = delivery_bay_qty;
  }

  /**
   * Gets the available_qty value for this StrInvDesc.
   * 
   * @return available_qty
   */
  public java.math.BigDecimal getAvailable_qty()
  {
    return available_qty;
  }

  /**
   * Sets the available_qty value for this StrInvDesc.
   * 
   * @param available_qty
   */
  public void setAvailable_qty(java.math.BigDecimal available_qty)
  {
    this.available_qty = available_qty;
  }

  /**
   * Gets the unavailable_qty value for this StrInvDesc.
   * 
   * @return unavailable_qty
   */
  public java.math.BigDecimal getUnavailable_qty()
  {
    return unavailable_qty;
  }

  /**
   * Sets the unavailable_qty value for this StrInvDesc.
   * 
   * @param unavailable_qty
   */
  public void setUnavailable_qty(java.math.BigDecimal unavailable_qty)
  {
    this.unavailable_qty = unavailable_qty;
  }

  /**
   * Gets the nonsell_total_qty value for this StrInvDesc.
   * 
   * @return nonsell_total_qty
   */
  public java.math.BigDecimal getNonsell_total_qty()
  {
    return nonsell_total_qty;
  }

  /**
   * Sets the nonsell_total_qty value for this StrInvDesc.
   * 
   * @param nonsell_total_qty
   */
  public void setNonsell_total_qty(java.math.BigDecimal nonsell_total_qty)
  {
    this.nonsell_total_qty = nonsell_total_qty;
  }

  /**
   * Gets the on_order_qty value for this StrInvDesc.
   * 
   * @return on_order_qty
   */
  public java.math.BigDecimal getOn_order_qty()
  {
    return on_order_qty;
  }

  /**
   * Sets the on_order_qty value for this StrInvDesc.
   * 
   * @param on_order_qty
   */
  public void setOn_order_qty(java.math.BigDecimal on_order_qty)
  {
    this.on_order_qty = on_order_qty;
  }

  /**
   * Gets the in_transit_qty value for this StrInvDesc.
   * 
   * @return in_transit_qty
   */
  public java.math.BigDecimal getIn_transit_qty()
  {
    return in_transit_qty;
  }

  /**
   * Sets the in_transit_qty value for this StrInvDesc.
   * 
   * @param in_transit_qty
   */
  public void setIn_transit_qty(java.math.BigDecimal in_transit_qty)
  {
    this.in_transit_qty = in_transit_qty;
  }

  /**
   * Gets the cust_reserved_qty value for this StrInvDesc.
   * 
   * @return cust_reserved_qty
   */
  public java.math.BigDecimal getCust_reserved_qty()
  {
    return cust_reserved_qty;
  }

  /**
   * Sets the cust_reserved_qty value for this StrInvDesc.
   * 
   * @param cust_reserved_qty
   */
  public void setCust_reserved_qty(java.math.BigDecimal cust_reserved_qty)
  {
    this.cust_reserved_qty = cust_reserved_qty;
  }

  /**
   * Gets the transfer_reserved_qty value for this StrInvDesc.
   * 
   * @return transfer_reserved_qty
   */
  public java.math.BigDecimal getTransfer_reserved_qty()
  {
    return transfer_reserved_qty;
  }

  /**
   * Sets the transfer_reserved_qty value for this StrInvDesc.
   * 
   * @param transfer_reserved_qty
   */
  public void setTransfer_reserved_qty(java.math.BigDecimal transfer_reserved_qty)
  {
    this.transfer_reserved_qty = transfer_reserved_qty;
  }

  /**
   * Gets the vendor_return_qty value for this StrInvDesc.
   * 
   * @return vendor_return_qty
   */
  public java.math.BigDecimal getVendor_return_qty()
  {
    return vendor_return_qty;
  }

  /**
   * Sets the vendor_return_qty value for this StrInvDesc.
   * 
   * @param vendor_return_qty
   */
  public void setVendor_return_qty(java.math.BigDecimal vendor_return_qty)
  {
    this.vendor_return_qty = vendor_return_qty;
  }

  /**
   * Gets the allocation_total_qty value for this StrInvDesc.
   * 
   * @return allocation_total_qty
   */
  public java.math.BigDecimal getAllocation_total_qty()
  {
    return allocation_total_qty;
  }

  /**
   * Sets the allocation_total_qty value for this StrInvDesc.
   * 
   * @param allocation_total_qty
   */
  public void setAllocation_total_qty(java.math.BigDecimal allocation_total_qty)
  {
    this.allocation_total_qty = allocation_total_qty;
  }

  /**
   * Gets the strInvNslQty value for this StrInvDesc.
   * 
   * @return strInvNslQty
   */
  public com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvNslQty[] getStrInvNslQty()
  {
    return strInvNslQty;
  }

  /**
   * Sets the strInvNslQty value for this StrInvDesc.
   * 
   * @param strInvNslQty
   */
  public void setStrInvNslQty(com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvNslQty[] strInvNslQty)
  {
    this.strInvNslQty = strInvNslQty;
  }

  public com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvNslQty getStrInvNslQty(int i)
  {
    return this.strInvNslQty[i];
  }

  public void setStrInvNslQty(int i, com.oracle.www.retail.integration.base.bo.StrInvDesc.v1.StrInvNslQty _value)
  {
    this.strInvNslQty[i] = _value;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj)
  {
    if (!(obj instanceof StrInvDesc))
      return false;
    StrInvDesc other = (StrInvDesc) obj;
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
    _equals = true && ((this.item_id == null && other.getItem_id() == null) || (this.item_id != null && this.item_id.equals(other.getItem_id())))
        && this.store_id == other.getStore_id() && this.is_ranged == other.isIs_ranged() && this.is_estimated == other.isIs_estimated()
        && ((this.uom_code == null && other.getUom_code() == null) || (this.uom_code != null && this.uom_code.equals(other.getUom_code())))
        && ((this.case_size == null && other.getCase_size() == null) || (this.case_size != null && this.case_size.equals(other.getCase_size())))
        && ((this.stock_on_hand_qty == null && other.getStock_on_hand_qty() == null)
            || (this.stock_on_hand_qty != null && this.stock_on_hand_qty.equals(other.getStock_on_hand_qty())))
        && ((this.back_room_qty == null && other.getBack_room_qty() == null) || (this.back_room_qty != null && this.back_room_qty.equals(other.getBack_room_qty())))
        && ((this.shop_floor_qty == null && other.getShop_floor_qty() == null) || (this.shop_floor_qty != null && this.shop_floor_qty.equals(other.getShop_floor_qty())))
        && ((this.delivery_bay_qty == null && other.getDelivery_bay_qty() == null) || (this.delivery_bay_qty != null && this.delivery_bay_qty.equals(other.getDelivery_bay_qty())))
        && ((this.available_qty == null && other.getAvailable_qty() == null) || (this.available_qty != null && this.available_qty.equals(other.getAvailable_qty())))
        && ((this.unavailable_qty == null && other.getUnavailable_qty() == null) || (this.unavailable_qty != null && this.unavailable_qty.equals(other.getUnavailable_qty())))
        && ((this.nonsell_total_qty == null && other.getNonsell_total_qty() == null)
            || (this.nonsell_total_qty != null && this.nonsell_total_qty.equals(other.getNonsell_total_qty())))
        && ((this.on_order_qty == null && other.getOn_order_qty() == null) || (this.on_order_qty != null && this.on_order_qty.equals(other.getOn_order_qty())))
        && ((this.in_transit_qty == null && other.getIn_transit_qty() == null) || (this.in_transit_qty != null && this.in_transit_qty.equals(other.getIn_transit_qty())))
        && ((this.cust_reserved_qty == null && other.getCust_reserved_qty() == null)
            || (this.cust_reserved_qty != null && this.cust_reserved_qty.equals(other.getCust_reserved_qty())))
        && ((this.transfer_reserved_qty == null && other.getTransfer_reserved_qty() == null)
            || (this.transfer_reserved_qty != null && this.transfer_reserved_qty.equals(other.getTransfer_reserved_qty())))
        && ((this.vendor_return_qty == null && other.getVendor_return_qty() == null)
            || (this.vendor_return_qty != null && this.vendor_return_qty.equals(other.getVendor_return_qty())))
        && ((this.allocation_total_qty == null && other.getAllocation_total_qty() == null)
            || (this.allocation_total_qty != null && this.allocation_total_qty.equals(other.getAllocation_total_qty())))
        && ((this.strInvNslQty == null && other.getStrInvNslQty() == null) || (this.strInvNslQty != null && java.util.Arrays.equals(this.strInvNslQty, other.getStrInvNslQty())));
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
    if (getItem_id() != null)
    {
      _hashCode += getItem_id().hashCode();
    }
    _hashCode += new Long(getStore_id()).hashCode();
    _hashCode += (isIs_ranged() ? Boolean.TRUE : Boolean.FALSE).hashCode();
    _hashCode += (isIs_estimated() ? Boolean.TRUE : Boolean.FALSE).hashCode();
    if (getUom_code() != null)
    {
      _hashCode += getUom_code().hashCode();
    }
    if (getCase_size() != null)
    {
      _hashCode += getCase_size().hashCode();
    }
    if (getStock_on_hand_qty() != null)
    {
      _hashCode += getStock_on_hand_qty().hashCode();
    }
    if (getBack_room_qty() != null)
    {
      _hashCode += getBack_room_qty().hashCode();
    }
    if (getShop_floor_qty() != null)
    {
      _hashCode += getShop_floor_qty().hashCode();
    }
    if (getDelivery_bay_qty() != null)
    {
      _hashCode += getDelivery_bay_qty().hashCode();
    }
    if (getAvailable_qty() != null)
    {
      _hashCode += getAvailable_qty().hashCode();
    }
    if (getUnavailable_qty() != null)
    {
      _hashCode += getUnavailable_qty().hashCode();
    }
    if (getNonsell_total_qty() != null)
    {
      _hashCode += getNonsell_total_qty().hashCode();
    }
    if (getOn_order_qty() != null)
    {
      _hashCode += getOn_order_qty().hashCode();
    }
    if (getIn_transit_qty() != null)
    {
      _hashCode += getIn_transit_qty().hashCode();
    }
    if (getCust_reserved_qty() != null)
    {
      _hashCode += getCust_reserved_qty().hashCode();
    }
    if (getTransfer_reserved_qty() != null)
    {
      _hashCode += getTransfer_reserved_qty().hashCode();
    }
    if (getVendor_return_qty() != null)
    {
      _hashCode += getVendor_return_qty().hashCode();
    }
    if (getAllocation_total_qty() != null)
    {
      _hashCode += getAllocation_total_qty().hashCode();
    }
    if (getStrInvNslQty() != null)
    {
      for (int i = 0; i < java.lang.reflect.Array.getLength(getStrInvNslQty()); i++)
      {
        java.lang.Object obj = java.lang.reflect.Array.get(getStrInvNslQty(), i);
        if (obj != null && !obj.getClass().isArray())
        {
          _hashCode += obj.hashCode();
        }
      }
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(StrInvDesc.class, true);

  static
  {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", ">StrInvDesc"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("item_id");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "item_id"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("store_id");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "store_id"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("is_ranged");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "is_ranged"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("is_estimated");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "is_estimated"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("uom_code");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "uom_code"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("case_size");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "case_size"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("stock_on_hand_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "stock_on_hand_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("back_room_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "back_room_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("shop_floor_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "shop_floor_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("delivery_bay_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "delivery_bay_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("available_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "available_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unavailable_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "unavailable_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("nonsell_total_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "nonsell_total_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("on_order_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "on_order_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("in_transit_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "in_transit_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("cust_reserved_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "cust_reserved_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("transfer_reserved_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "transfer_reserved_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("vendor_return_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "vendor_return_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("allocation_total_qty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "allocation_total_qty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("strInvNslQty");
    elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "StrInvNslQty"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "StrInvNslQty"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    elemField.setMaxOccursUnbounded(true);
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
