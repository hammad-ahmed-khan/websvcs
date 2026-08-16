
package com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StrFordCriStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StrFordCriStatus"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEW"/&gt;
 *     &lt;enumeration value="IN_PROGRESS"/&gt;
 *     &lt;enumeration value="COMPLETED"/&gt;
 *     &lt;enumeration value="CANCELED"/&gt;
 *     &lt;enumeration value="ACTIVE"/&gt;
 *     &lt;enumeration value="NO_VALUE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
