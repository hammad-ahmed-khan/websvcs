
package com.oracle.retail.integration.base.bo.strfordhdrdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for str_ford_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="str_ford_status">
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
@XmlType(name = "str_ford_status")
@XmlEnum
public enum StrFordStatus {

    NEW,
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static StrFordStatus fromValue(String v) {
        return valueOf(v);
    }

}
