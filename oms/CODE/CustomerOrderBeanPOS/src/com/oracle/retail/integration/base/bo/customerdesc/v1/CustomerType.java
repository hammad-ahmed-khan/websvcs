
package com.oracle.retail.integration.base.bo.customerdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customer_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="customer_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REGULAR"/>
 *     &lt;enumeration value="BUSINESS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "customer_type")
@XmlEnum
public enum CustomerType {

    REGULAR,
    BUSINESS;

    public String value() {
        return name();
    }

    public static CustomerType fromValue(String v) {
        return valueOf(v);
    }

}
