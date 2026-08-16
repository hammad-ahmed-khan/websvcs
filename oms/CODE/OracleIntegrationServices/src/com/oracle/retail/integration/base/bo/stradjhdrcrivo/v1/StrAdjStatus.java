
package com.oracle.retail.integration.base.bo.stradjhdrcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for str_adj_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="str_adj_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="CANCELED"/>
 *     &lt;enumeration value="UNKNOWN"/>
 *     &lt;enumeration value="NO_VALUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "str_adj_status")
@XmlEnum
public enum StrAdjStatus {

    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    UNKNOWN,
    NO_VALUE;

    public String value() {
        return name();
    }

    public static StrAdjStatus fromValue(String v) {
        return valueOf(v);
    }

}
