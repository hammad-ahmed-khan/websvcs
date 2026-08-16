/**
 * ValidationWSFaultException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.services.exception.v1;

public class ValidationWSFaultException  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private java.lang.String shortErrorMessage;

    private java.lang.String errorDescription;

    private com.oracle.www.retail.integration.services.exception.v1.BusinessProblemDetail[] businessProblemDetail;

    public ValidationWSFaultException() {
    }

    public ValidationWSFaultException(
           java.lang.String shortErrorMessage,
           java.lang.String errorDescription,
           com.oracle.www.retail.integration.services.exception.v1.BusinessProblemDetail[] businessProblemDetail) {
        this.shortErrorMessage = shortErrorMessage;
        this.errorDescription = errorDescription;
        this.businessProblemDetail = businessProblemDetail;
    }


    /**
     * Gets the shortErrorMessage value for this ValidationWSFaultException.
     * 
     * @return shortErrorMessage
     */
    public java.lang.String getShortErrorMessage() {
        return shortErrorMessage;
    }


    /**
     * Sets the shortErrorMessage value for this ValidationWSFaultException.
     * 
     * @param shortErrorMessage
     */
    public void setShortErrorMessage(java.lang.String shortErrorMessage) {
        this.shortErrorMessage = shortErrorMessage;
    }


    /**
     * Gets the errorDescription value for this ValidationWSFaultException.
     * 
     * @return errorDescription
     */
    public java.lang.String getErrorDescription() {
        return errorDescription;
    }


    /**
     * Sets the errorDescription value for this ValidationWSFaultException.
     * 
     * @param errorDescription
     */
    public void setErrorDescription(java.lang.String errorDescription) {
        this.errorDescription = errorDescription;
    }


    /**
     * Gets the businessProblemDetail value for this ValidationWSFaultException.
     * 
     * @return businessProblemDetail
     */
    public com.oracle.www.retail.integration.services.exception.v1.BusinessProblemDetail[] getBusinessProblemDetail() {
        return businessProblemDetail;
    }


    /**
     * Sets the businessProblemDetail value for this ValidationWSFaultException.
     * 
     * @param businessProblemDetail
     */
    public void setBusinessProblemDetail(com.oracle.www.retail.integration.services.exception.v1.BusinessProblemDetail[] businessProblemDetail) {
        this.businessProblemDetail = businessProblemDetail;
    }

    public com.oracle.www.retail.integration.services.exception.v1.BusinessProblemDetail getBusinessProblemDetail(int i) {
        return this.businessProblemDetail[i];
    }

    public void setBusinessProblemDetail(int i, com.oracle.www.retail.integration.services.exception.v1.BusinessProblemDetail _value) {
        this.businessProblemDetail[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ValidationWSFaultException)) return false;
        ValidationWSFaultException other = (ValidationWSFaultException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.shortErrorMessage==null && other.getShortErrorMessage()==null) || 
             (this.shortErrorMessage!=null &&
              this.shortErrorMessage.equals(other.getShortErrorMessage()))) &&
            ((this.errorDescription==null && other.getErrorDescription()==null) || 
             (this.errorDescription!=null &&
              this.errorDescription.equals(other.getErrorDescription()))) &&
            ((this.businessProblemDetail==null && other.getBusinessProblemDetail()==null) || 
             (this.businessProblemDetail!=null &&
              java.util.Arrays.equals(this.businessProblemDetail, other.getBusinessProblemDetail())));
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
        if (getShortErrorMessage() != null) {
            _hashCode += getShortErrorMessage().hashCode();
        }
        if (getErrorDescription() != null) {
            _hashCode += getErrorDescription().hashCode();
        }
        if (getBusinessProblemDetail() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBusinessProblemDetail());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBusinessProblemDetail(), i);
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
        new org.apache.axis.description.TypeDesc(ValidationWSFaultException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", ">ValidationWSFaultException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortErrorMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "shortErrorMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "errorDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessProblemDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "BusinessProblemDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "BusinessProblemDetail"));
        elemField.setMinOccurs(0);
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
