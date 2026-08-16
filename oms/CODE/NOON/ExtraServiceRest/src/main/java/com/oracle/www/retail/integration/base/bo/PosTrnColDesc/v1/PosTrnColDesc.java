/**
 * PosTrnColDesc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.PosTrnColDesc.v1;

public class PosTrnColDesc  implements java.io.Serializable {
    private com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnDesc[] posTrnDesc;

    private int collection_size;

    public PosTrnColDesc() {
    }

    public PosTrnColDesc(
           com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnDesc[] posTrnDesc,
           int collection_size) {
           this.posTrnDesc = posTrnDesc;
           this.collection_size = collection_size;
    }


    /**
     * Gets the posTrnDesc value for this PosTrnColDesc.
     * 
     * @return posTrnDesc
     */
    public com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnDesc[] getPosTrnDesc() {
        return posTrnDesc;
    }


    /**
     * Sets the posTrnDesc value for this PosTrnColDesc.
     * 
     * @param posTrnDesc
     */
    public void setPosTrnDesc(com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnDesc[] posTrnDesc) {
        this.posTrnDesc = posTrnDesc;
    }

    public com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnDesc getPosTrnDesc(int i) {
        return this.posTrnDesc[i];
    }

    public void setPosTrnDesc(int i, com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnDesc _value) {
        this.posTrnDesc[i] = _value;
    }


    /**
     * Gets the collection_size value for this PosTrnColDesc.
     * 
     * @return collection_size
     */
    public int getCollection_size() {
        return collection_size;
    }


    /**
     * Sets the collection_size value for this PosTrnColDesc.
     * 
     * @param collection_size
     */
    public void setCollection_size(int collection_size) {
        this.collection_size = collection_size;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PosTrnColDesc)) return false;
        PosTrnColDesc other = (PosTrnColDesc) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.posTrnDesc==null && other.getPosTrnDesc()==null) || 
             (this.posTrnDesc!=null &&
              java.util.Arrays.equals(this.posTrnDesc, other.getPosTrnDesc()))) &&
            this.collection_size == other.getCollection_size();
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
        if (getPosTrnDesc() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPosTrnDesc());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPosTrnDesc(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getCollection_size();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PosTrnColDesc.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnColDesc/v1", ">PosTrnColDesc"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("posTrnDesc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "PosTrnDesc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "PosTrnDesc"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("collection_size");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnColDesc/v1", "collection_size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
