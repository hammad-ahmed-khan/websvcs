/**
 * PosTrnDesc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1;

public class PosTrnDesc  implements java.io.Serializable {
    private long store_id;

    private java.lang.String transaction_id;

    private java.util.Calendar transaction_timestamp;

    private java.lang.String cust_order_id;

    private java.lang.String cust_order_comment;

    private com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItm[] posTrnItm;

    public PosTrnDesc() {
    }

    public PosTrnDesc(
           long store_id,
           java.lang.String transaction_id,
           java.util.Calendar transaction_timestamp,
           java.lang.String cust_order_id,
           java.lang.String cust_order_comment,
           com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItm[] posTrnItm) {
           this.store_id = store_id;
           this.transaction_id = transaction_id;
           this.transaction_timestamp = transaction_timestamp;
           this.cust_order_id = cust_order_id;
           this.cust_order_comment = cust_order_comment;
           this.posTrnItm = posTrnItm;
    }


    /**
     * Gets the store_id value for this PosTrnDesc.
     * 
     * @return store_id
     */
    public long getStore_id() {
        return store_id;
    }


    /**
     * Sets the store_id value for this PosTrnDesc.
     * 
     * @param store_id
     */
    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }


    /**
     * Gets the transaction_id value for this PosTrnDesc.
     * 
     * @return transaction_id
     */
    public java.lang.String getTransaction_id() {
        return transaction_id;
    }


    /**
     * Sets the transaction_id value for this PosTrnDesc.
     * 
     * @param transaction_id
     */
    public void setTransaction_id(java.lang.String transaction_id) {
        this.transaction_id = transaction_id;
    }


    /**
     * Gets the transaction_timestamp value for this PosTrnDesc.
     * 
     * @return transaction_timestamp
     */
    public java.util.Calendar getTransaction_timestamp() {
        return transaction_timestamp;
    }


    /**
     * Sets the transaction_timestamp value for this PosTrnDesc.
     * 
     * @param transaction_timestamp
     */
    public void setTransaction_timestamp(java.util.Calendar transaction_timestamp) {
        this.transaction_timestamp = transaction_timestamp;
    }


    /**
     * Gets the cust_order_id value for this PosTrnDesc.
     * 
     * @return cust_order_id
     */
    public java.lang.String getCust_order_id() {
        return cust_order_id;
    }


    /**
     * Sets the cust_order_id value for this PosTrnDesc.
     * 
     * @param cust_order_id
     */
    public void setCust_order_id(java.lang.String cust_order_id) {
        this.cust_order_id = cust_order_id;
    }


    /**
     * Gets the cust_order_comment value for this PosTrnDesc.
     * 
     * @return cust_order_comment
     */
    public java.lang.String getCust_order_comment() {
        return cust_order_comment;
    }


    /**
     * Sets the cust_order_comment value for this PosTrnDesc.
     * 
     * @param cust_order_comment
     */
    public void setCust_order_comment(java.lang.String cust_order_comment) {
        this.cust_order_comment = cust_order_comment;
    }


    /**
     * Gets the posTrnItm value for this PosTrnDesc.
     * 
     * @return posTrnItm
     */
    public com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItm[] getPosTrnItm() {
        return posTrnItm;
    }


    /**
     * Sets the posTrnItm value for this PosTrnDesc.
     * 
     * @param posTrnItm
     */
    public void setPosTrnItm(com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItm[] posTrnItm) {
        this.posTrnItm = posTrnItm;
    }

    public com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItm getPosTrnItm(int i) {
        return this.posTrnItm[i];
    }

    public void setPosTrnItm(int i, com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItm _value) {
        this.posTrnItm[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PosTrnDesc)) return false;
        PosTrnDesc other = (PosTrnDesc) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.store_id == other.getStore_id() &&
            ((this.transaction_id==null && other.getTransaction_id()==null) || 
             (this.transaction_id!=null &&
              this.transaction_id.equals(other.getTransaction_id()))) &&
            ((this.transaction_timestamp==null && other.getTransaction_timestamp()==null) || 
             (this.transaction_timestamp!=null &&
              this.transaction_timestamp.equals(other.getTransaction_timestamp()))) &&
            ((this.cust_order_id==null && other.getCust_order_id()==null) || 
             (this.cust_order_id!=null &&
              this.cust_order_id.equals(other.getCust_order_id()))) &&
            ((this.cust_order_comment==null && other.getCust_order_comment()==null) || 
             (this.cust_order_comment!=null &&
              this.cust_order_comment.equals(other.getCust_order_comment()))) &&
            ((this.posTrnItm==null && other.getPosTrnItm()==null) || 
             (this.posTrnItm!=null &&
              java.util.Arrays.equals(this.posTrnItm, other.getPosTrnItm())));
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
        _hashCode += new Long(getStore_id()).hashCode();
        if (getTransaction_id() != null) {
            _hashCode += getTransaction_id().hashCode();
        }
        if (getTransaction_timestamp() != null) {
            _hashCode += getTransaction_timestamp().hashCode();
        }
        if (getCust_order_id() != null) {
            _hashCode += getCust_order_id().hashCode();
        }
        if (getCust_order_comment() != null) {
            _hashCode += getCust_order_comment().hashCode();
        }
        if (getPosTrnItm() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPosTrnItm());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPosTrnItm(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PosTrnDesc.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", ">PosTrnDesc"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("store_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "store_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "transaction_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction_timestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "transaction_timestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cust_order_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "cust_order_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cust_order_comment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "cust_order_comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("posTrnItm");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "PosTrnItm"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1", "PosTrnItm"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
