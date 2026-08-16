/**
 * PosTrnOrdResvType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1;

public class PosTrnOrdResvType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected PosTrnOrdResvType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _WEB_ORDER = "WEB_ORDER";
    public static final java.lang.String _SPECIAL_ORDER = "SPECIAL_ORDER";
    public static final java.lang.String _PICKUP_OR_DELIVERY = "PICKUP_OR_DELIVERY";
    public static final java.lang.String _LAYAWAY = "LAYAWAY";
    public static final java.lang.String _NO_VALUE = "NO_VALUE";
    public static final PosTrnOrdResvType WEB_ORDER = new PosTrnOrdResvType(_WEB_ORDER);
    public static final PosTrnOrdResvType SPECIAL_ORDER = new PosTrnOrdResvType(_SPECIAL_ORDER);
    public static final PosTrnOrdResvType PICKUP_OR_DELIVERY = new PosTrnOrdResvType(_PICKUP_OR_DELIVERY);
    public static final PosTrnOrdResvType LAYAWAY = new PosTrnOrdResvType(_LAYAWAY);
    public static final PosTrnOrdResvType NO_VALUE = new PosTrnOrdResvType(_NO_VALUE);
    public java.lang.String getValue() { return _value_;}
    public static PosTrnOrdResvType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        PosTrnOrdResvType enumeration = (PosTrnOrdResvType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static PosTrnOrdResvType fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PosTrnOrdResvType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "PosTrnOrdResvType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
