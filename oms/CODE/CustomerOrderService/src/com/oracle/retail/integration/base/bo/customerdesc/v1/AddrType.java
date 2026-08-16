
package com.oracle.retail.integration.base.bo.customerdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for addr_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="addr_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HOME"/>
 *     &lt;enumeration value="WORK"/>
 *     &lt;enumeration value="MAIL"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "addr_type")
@XmlEnum
public enum AddrType {

    HOME,
    WORK,
    MAIL,
    OTHER;

    public String value() {
        return name();
    }

    public static AddrType fromValue(String v) {
        return valueOf(v);
    }

}
