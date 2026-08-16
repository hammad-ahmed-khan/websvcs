
package com.oracle.retail.integration.base.bo.fodhdrdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fod_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fod_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="SUBMITTED"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="CANCELED"/>
 *     &lt;enumeration value="UNKNOWN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fod_status")
@XmlEnum
public enum FodStatus {

    IN_PROGRESS,
    SUBMITTED,
    COMPLETED,
    CANCELED,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static FodStatus fromValue(String v) {
        return valueOf(v);
    }

}
