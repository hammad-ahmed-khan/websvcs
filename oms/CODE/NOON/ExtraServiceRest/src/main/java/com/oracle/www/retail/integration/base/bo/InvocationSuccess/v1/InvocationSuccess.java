/**
 * InvocationSuccess.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.base.bo.InvocationSuccess.v1;

public class InvocationSuccess  implements java.io.Serializable {
    private java.lang.String success_message;

    public InvocationSuccess() {
    }

    public InvocationSuccess(
           java.lang.String success_message) {
           this.success_message = success_message;
    }


    /**
     * Gets the success_message value for this InvocationSuccess.
     * 
     * @return success_message
     */
    public java.lang.String getSuccess_message() {
        return success_message;
    }


    /**
     * Sets the success_message value for this InvocationSuccess.
     * 
     * @param success_message
     */
    public void setSuccess_message(java.lang.String success_message) {
        this.success_message = success_message;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InvocationSuccess)) return false;
        InvocationSuccess other = (InvocationSuccess) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.success_message==null && other.getSuccess_message()==null) || 
             (this.success_message!=null &&
              this.success_message.equals(other.getSuccess_message())));
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
        if (getSuccess_message() != null) {
            _hashCode += getSuccess_message().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InvocationSuccess.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1", ">InvocationSuccess"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("success_message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1", "success_message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
