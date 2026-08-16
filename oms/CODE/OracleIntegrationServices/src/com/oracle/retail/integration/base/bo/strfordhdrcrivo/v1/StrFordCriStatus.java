
package com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StrFordCriStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StrFordCriStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="CANCELED"/>
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="NO_VALUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StrFordCriStatus")
@XmlEnum
public enum StrFordCriStatus {

    NEW,
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    ACTIVE,
    NO_VALUE;

    public String value() {
        return name();
    }

    public static StrFordCriStatus fromValue(String v) {
        return valueOf(v);
    }

}
