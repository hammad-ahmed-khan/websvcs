/**
 * PosTrnItm.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1;

public class PosTrnItm  implements java.io.Serializable {
    private java.lang.String item_id;

    private java.math.BigDecimal quantity;

    private java.lang.String unit_of_measure;

    private java.lang.String uin;

    private java.lang.Integer reason_code;

    private boolean drop_ship;

    private java.lang.String comments;

    private java.lang.String fulfill_order_id;

    private com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnOrdResvType reservation_type;

    private com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItmTranCode transaction_code;

    public PosTrnItm() {
    }

    public PosTrnItm(
           java.lang.String item_id,
           java.math.BigDecimal quantity,
           java.lang.String unit_of_measure,
           java.lang.String uin,
           java.lang.Integer reason_code,
           boolean drop_ship,
           java.lang.String comments,
           java.lang.String fulfill_order_id,
           com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnOrdResvType reservation_type,
           com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItmTranCode transaction_code) {
           this.item_id = item_id;
           this.quantity = quantity;
           this.unit_of_measure = unit_of_measure;
           this.uin = uin;
           this.reason_code = reason_code;
           this.drop_ship = drop_ship;
           this.comments = comments;
           this.fulfill_order_id = fulfill_order_id;
           this.reservation_type = reservation_type;
           this.transaction_code = transaction_code;
    }


    /**
     * Gets the item_id value for this PosTrnItm.
     * 
     * @return item_id
     */
    public java.lang.String getItem_id() {
        return item_id;
    }


    /**
     * Sets the item_id value for this PosTrnItm.
     * 
     * @param item_id
     */
    public void setItem_id(java.lang.String item_id) {
        this.item_id = item_id;
    }


    /**
     * Gets the quantity value for this PosTrnItm.
     * 
     * @return quantity
     */
    public java.math.BigDecimal getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity value for this PosTrnItm.
     * 
     * @param quantity
     */
    public void setQuantity(java.math.BigDecimal quantity) {
        this.quantity = quantity;
    }


    /**
     * Gets the unit_of_measure value for this PosTrnItm.
     * 
     * @return unit_of_measure
     */
    public java.lang.String getUnit_of_measure() {
        return unit_of_measure;
    }


    /**
     * Sets the unit_of_measure value for this PosTrnItm.
     * 
     * @param unit_of_measure
     */
    public void setUnit_of_measure(java.lang.String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }


    /**
     * Gets the uin value for this PosTrnItm.
     * 
     * @return uin
     */
    public java.lang.String getUin() {
        return uin;
    }


    /**
     * Sets the uin value for this PosTrnItm.
     * 
     * @param uin
     */
    public void setUin(java.lang.String uin) {
        this.uin = uin;
    }


    /**
     * Gets the reason_code value for this PosTrnItm.
     * 
     * @return reason_code
     */
    public java.lang.Integer getReason_code() {
        return reason_code;
    }


    /**
     * Sets the reason_code value for this PosTrnItm.
     * 
     * @param reason_code
     */
    public void setReason_code(java.lang.Integer reason_code) {
        this.reason_code = reason_code;
    }


    /**
     * Gets the drop_ship value for this PosTrnItm.
     * 
     * @return drop_ship
     */
    public boolean isDrop_ship() {
        return drop_ship;
    }


    /**
     * Sets the drop_ship value for this PosTrnItm.
     * 
     * @param drop_ship
     */
    public void setDrop_ship(boolean drop_ship) {
        this.drop_ship = drop_ship;
    }


    /**
     * Gets the comments value for this PosTrnItm.
     * 
     * @return comments
     */
    public java.lang.String getComments() {
        return comments;
    }


    /**
     * Sets the comments value for this PosTrnItm.
     * 
     * @param comments
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }


    /**
     * Gets the fulfill_order_id value for this PosTrnItm.
     * 
     * @return fulfill_order_id
     */
    public java.lang.String getFulfill_order_id() {
        return fulfill_order_id;
    }


    /**
     * Sets the fulfill_order_id value for this PosTrnItm.
     * 
     * @param fulfill_order_id
     */
    public void setFulfill_order_id(java.lang.String fulfill_order_id) {
        this.fulfill_order_id = fulfill_order_id;
    }


    /**
     * Gets the reservation_type value for this PosTrnItm.
     * 
     * @return reservation_type
     */
    public com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnOrdResvType getReservation_type() {
        return reservation_type;
    }


    /**
     * Sets the reservation_type value for this PosTrnItm.
     * 
     * @param reservation_type
     */
    public void setReservation_type(com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnOrdResvType reservation_type) {
        this.reservation_type = reservation_type;
    }


    /**
     * Gets the transaction_code value for this PosTrnItm.
     * 
     * @return transaction_code
     */
    public com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItmTranCode getTransaction_code() {
        return transaction_code;
    }


    /**
     * Sets the transaction_code value for this PosTrnItm.
     * 
     * @param transaction_code
     */
    public void setTransaction_code(com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItmTranCode transaction_code) {
        this.transaction_code = transaction_code;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PosTrnItm)) return false;
        PosTrnItm other = (PosTrnItm) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_id==null && other.getItem_id()==null) || 
             (this.item_id!=null &&
              this.item_id.equals(other.getItem_id()))) &&
            ((this.quantity==null && other.getQuantity()==null) || 
             (this.quantity!=null &&
              this.quantity.equals(other.getQuantity()))) &&
            ((this.unit_of_measure==null && other.getUnit_of_measure()==null) || 
             (this.unit_of_measure!=null &&
              this.unit_of_measure.equals(other.getUnit_of_measure()))) &&
            ((this.uin==null && other.getUin()==null) || 
             (this.uin!=null &&
              this.uin.equals(other.getUin()))) &&
            ((this.reason_code==null && other.getReason_code()==null) || 
             (this.reason_code!=null &&
              this.reason_code.equals(other.getReason_code()))) &&
            this.drop_ship == other.isDrop_ship() &&
            ((this.comments==null && other.getComments()==null) || 
             (this.comments!=null &&
              this.comments.equals(other.getComments()))) &&
            ((this.fulfill_order_id==null && other.getFulfill_order_id()==null) || 
             (this.fulfill_order_id!=null &&
              this.fulfill_order_id.equals(other.getFulfill_order_id()))) &&
            ((this.reservation_type==null && other.getReservation_type()==null) || 
             (this.reservation_type!=null &&
              this.reservation_type.equals(other.getReservation_type()))) &&
            ((this.transaction_code==null && other.getTransaction_code()==null) || 
             (this.transaction_code!=null &&
              this.transaction_code.equals(other.getTransaction_code())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getItem_id() != null) {
            _hashCode += getItem_id().hashCode();
        }
        if (getQuantity() != null) {
            _hashCode += getQuantity().hashCode();
        }
        if (getUnit_of_measure() != null) {
            _hashCode += getUnit_of_measure().hashCode();
        }
        if (getUin() != null) {
            _hashCode += getUin().hashCode();
        }
        if (getReason_code() != null) {
            _hashCode += getReason_code().hashCode();
        }
        _hashCode += (isDrop_ship() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getComments() != null) {
            _hashCode += getComments().hashCode();
        }
        if (getFulfill_order_id() != null) {
            _hashCode += getFulfill_order_id().hashCode();
        }
        if (getReservation_type() != null) {
            _hashCode += getReservation_type().hashCode();
        }
        if (getTransaction_code() != null) {
            _hashCode += getTransaction_code().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PosTrnItm.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", ">PosTrnItm"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "item_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unit_of_measure");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "unit_of_measure"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "uin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reason_code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "reason_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drop_ship");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "drop_ship"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comments");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "comments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fulfill_order_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "fulfill_order_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reservation_type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "reservation_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "PosTrnOrdResvType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction_code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "transaction_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "PosTrnItmTranCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
