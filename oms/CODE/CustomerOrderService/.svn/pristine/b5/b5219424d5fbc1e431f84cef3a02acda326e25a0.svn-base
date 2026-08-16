
package com.oracle.retail.integration.base.bo.emaildesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for email_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="email_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HOME"/>
 *     &lt;enumeration value="WORK"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "email_type")
@XmlEnum
public enum EmailType {

    HOME,
    WORK,
    OTHER;

    public String value() {
        return name();
    }

    public static EmailType fromValue(String v) {
        return valueOf(v);
    }

}
