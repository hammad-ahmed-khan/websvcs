/**
 * BusinessProblemDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.oracle.www.retail.integration.services.exception.v1;

public class BusinessProblemDetail  implements java.io.Serializable {
    private java.lang.String[] problemDescription;

    private com.oracle.www.retail.integration.services.exception.v1.ProblemDetailEntry[] problemDetailEntry;

    public BusinessProblemDetail() {
    }

    public BusinessProblemDetail(
           java.lang.String[] problemDescription,
           com.oracle.www.retail.integration.services.exception.v1.ProblemDetailEntry[] problemDetailEntry) {
           this.problemDescription = problemDescription;
           this.problemDetailEntry = problemDetailEntry;
    }


    /**
     * Gets the problemDescription value for this BusinessProblemDetail.
     * 
     * @return problemDescription
     */
    public java.lang.String[] getProblemDescription() {
        return problemDescription;
    }


    /**
     * Sets the problemDescription value for this BusinessProblemDetail.
     * 
     * @param problemDescription
     */
    public void setProblemDescription(java.lang.String[] problemDescription) {
        this.problemDescription = problemDescription;
    }

    public java.lang.String getProblemDescription(int i) {
        return this.problemDescription[i];
    }

    public void setProblemDescription(int i, java.lang.String _value) {
        this.problemDescription[i] = _value;
    }


    /**
     * Gets the problemDetailEntry value for this BusinessProblemDetail.
     * 
     * @return problemDetailEntry
     */
    public com.oracle.www.retail.integration.services.exception.v1.ProblemDetailEntry[] getProblemDetailEntry() {
        return problemDetailEntry;
    }


    /**
     * Sets the problemDetailEntry value for this BusinessProblemDetail.
     * 
     * @param problemDetailEntry
     */
    public void setProblemDetailEntry(com.oracle.www.retail.integration.services.exception.v1.ProblemDetailEntry[] problemDetailEntry) {
        this.problemDetailEntry = problemDetailEntry;
    }

    public com.oracle.www.retail.integration.services.exception.v1.ProblemDetailEntry getProblemDetailEntry(int i) {
        return this.problemDetailEntry[i];
    }

    public void setProblemDetailEntry(int i, com.oracle.www.retail.integration.services.exception.v1.ProblemDetailEntry _value) {
        this.problemDetailEntry[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BusinessProblemDetail)) return false;
        BusinessProblemDetail other = (BusinessProblemDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.problemDescription==null && other.getProblemDescription()==null) || 
             (this.problemDescription!=null &&
              java.util.Arrays.equals(this.problemDescription, other.getProblemDescription()))) &&
            ((this.problemDetailEntry==null && other.getProblemDetailEntry()==null) || 
             (this.problemDetailEntry!=null &&
              java.util.Arrays.equals(this.problemDetailEntry, other.getProblemDetailEntry())));
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
        if (getProblemDescription() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProblemDescription());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProblemDescription(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getProblemDetailEntry() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProblemDetailEntry());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProblemDetailEntry(), i);
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
        new org.apache.axis.description.TypeDesc(BusinessProblemDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", ">BusinessProblemDetail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("problemDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "problemDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("problemDetailEntry");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "ProblemDetailEntry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.oracle.com/retail/integration/services/exception/v1", "ProblemDetailEntry"));
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

}
