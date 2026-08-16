/**
 * StrInvNslQty.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.StrInvDesc.v1;

public class StrInvNslQty  implements java.io.Serializable {
    private long nonsell_qty_type_id;

    private java.math.BigDecimal quantity;

    public StrInvNslQty() {
    }

    public StrInvNslQty(
           long nonsell_qty_type_id,
           java.math.BigDecimal quantity) {
           this.nonsell_qty_type_id = nonsell_qty_type_id;
           this.quantity = quantity;
    }


    /**
     * Gets the nonsell_qty_type_id value for this StrInvNslQty.
     * 
     * @return nonsell_qty_type_id
     */
    public long getNonsell_qty_type_id() {
        return nonsell_qty_type_id;
    }


    /**
     * Sets the nonsell_qty_type_id value for this StrInvNslQty.
     * 
     * @param nonsell_qty_type_id
     */
    public void setNonsell_qty_type_id(long nonsell_qty_type_id) {
        this.nonsell_qty_type_id = nonsell_qty_type_id;
    }


    /**
     * Gets the quantity value for this StrInvNslQty.
     * 
     * @return quantity
     */
    public java.math.BigDecimal getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity value for this StrInvNslQty.
     * 
     * @param quantity
     */
    public void setQuantity(java.math.BigDecimal quantity) {
        this.quantity = quantity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StrInvNslQty)) return false;
        StrInvNslQty other = (StrInvNslQty) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.nonsell_qty_type_id == other.getNonsell_qty_type_id() &&
            ((this.quantity==null && other.getQuantity()==null) || 
             (this.quantity!=null &&
              this.quantity.equals(other.getQuantity())));
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
        _hashCode += new Long(getNonsell_qty_type_id()).hashCode();
        if (getQuantity() != null) {
            _hashCode += getQuantity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StrInvNslQty.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", ">StrInvNslQty"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nonsell_qty_type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "nonsell_qty_type_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1", "quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
