
package com.oracle.retail.integration.base.bo.customerdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for receipt_preference.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="receipt_preference">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NONE"/>
 *     &lt;enumeration value="PRINT"/>
 *     &lt;enumeration value="EMAIL"/>
 *     &lt;enumeration value="BOTH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "receipt_preference")
@XmlEnum
public enum ReceiptPreference {

    NONE,
    PRINT,
    EMAIL,
    BOTH;

    public String value() {
        return name();
    }

    public static ReceiptPreference fromValue(String v) {
        return valueOf(v);
    }

}
