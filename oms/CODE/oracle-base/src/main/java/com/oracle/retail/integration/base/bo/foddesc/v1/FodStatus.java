
package com.oracle.retail.integration.base.bo.foddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fod_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fod_status"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IN_PROGRESS"/&gt;
 *     &lt;enumeration value="SUBMITTED"/&gt;
 *     &lt;enumeration value="COMPLETED"/&gt;
 *     &lt;enumeration value="CANCELED"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
