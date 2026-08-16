
package com.oracle.retail.integration.base.bo.forpdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for forp_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="forp_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="CANCELED"/>
 *     &lt;enumeration value="UNKNOWN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "forp_status")
@XmlEnum
public enum ForpStatus {

    NEW,
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static ForpStatus fromValue(String v) {
        return valueOf(v);
    }

}
