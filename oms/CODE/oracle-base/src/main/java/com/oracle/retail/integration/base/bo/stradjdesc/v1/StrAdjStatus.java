
package com.oracle.retail.integration.base.bo.stradjdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for str_adj_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="str_adj_status"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IN_PROGRESS"/&gt;
 *     &lt;enumeration value="COMPLETED"/&gt;
 *     &lt;enumeration value="CANCELED"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *     &lt;enumeration value="NO_VALUE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
