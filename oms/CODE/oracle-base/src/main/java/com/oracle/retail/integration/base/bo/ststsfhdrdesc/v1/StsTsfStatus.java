
package com.oracle.retail.integration.base.bo.ststsfhdrdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StsTsfStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StsTsfStatus"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEW"/&gt;
 *     &lt;enumeration value="PENDING"/&gt;
 *     &lt;enumeration value="REJECTED"/&gt;
 *     &lt;enumeration value="CANCELED_REQUEST"/&gt;
 *     &lt;enumeration value="IN_PROGRESS"/&gt;
 *     &lt;enumeration value="DISPATCHED"/&gt;
 *     &lt;enumeration value="SUBMITTED"/&gt;
 *     &lt;enumeration value="RECEIVING"/&gt;
 *     &lt;enumeration value="RECEIVED"/&gt;
 *     &lt;enumeration value="CANCELED_TRANSFER"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StsTsfStatus")
@XmlEnum
public enum StsTsfStatus {

    NEW,
    PENDING,
    REJECTED,
    CANCELED_REQUEST,
    IN_PROGRESS,
    DISPATCHED,
    SUBMITTED,
    RECEIVING,
    RECEIVED,
    CANCELED_TRANSFER,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static StsTsfStatus fromValue(String v) {
        return valueOf(v);
    }

}
