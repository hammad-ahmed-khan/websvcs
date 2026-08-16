
package com.oracle.retail.integration.base.bo.phonedesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for phone_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="phone_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HOME"/>
 *     &lt;enumeration value="WORK"/>
 *     &lt;enumeration value="MOBILE"/>
 *     &lt;enumeration value="FAX"/>
 *     &lt;enumeration value="PAGER"/>
 *     &lt;enumeration value="TELEX"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "phone_type")
@XmlEnum
public enum PhoneType {

    HOME,
    WORK,
    MOBILE,
    FAX,
    PAGER,
    TELEX,
    OTHER;

    public String value() {
        return name();
    }

    public static PhoneType fromValue(String v) {
        return valueOf(v);
    }

}
