
package com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StsTsfCriStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StsTsfCriStatus"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ACTIVE"/&gt;
 *     &lt;enumeration value="ACTIVE_INBOUND"/&gt;
 *     &lt;enumeration value="ACTIVE_OUTBOUND"/&gt;
 *     &lt;enumeration value="NEW"/&gt;
 *     &lt;enumeration value="PENDING_REQUEST"/&gt;
 *     &lt;enumeration value="AWAITING_RESPONSE"/&gt;
 *     &lt;enumeration value="OUTBOUND_REJECTED"/&gt;
 *     &lt;enumeration value="INBOUND_REJECTED"/&gt;
 *     &lt;enumeration value="IN_PROGRESS"/&gt;
 *     &lt;enumeration value="SUBMITTED"/&gt;
 *     &lt;enumeration value="INBOUND_PICKING"/&gt;
 *     &lt;enumeration value="IN_TRANSIT"/&gt;
 *     &lt;enumeration value="DISPATCHED"/&gt;
 *     &lt;enumeration value="RECEIVING"/&gt;
 *     &lt;enumeration value="CLOSED"/&gt;
 *     &lt;enumeration value="RECEIVED"/&gt;
 *     &lt;enumeration value="CANCELED_INBOUND"/&gt;
 *     &lt;enumeration value="CANCELED_REQUEST"/&gt;
 *     &lt;enumeration value="CANCELED_TRANSFER"/&gt;
 *     &lt;enumeration value="NO_VALUE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StsTsfCriStatus")
@XmlEnum
public enum StsTsfCriStatus {

    ACTIVE,
    ACTIVE_INBOUND,
    ACTIVE_OUTBOUND,
    NEW,
    PENDING_REQUEST,
    AWAITING_RESPONSE,
    OUTBOUND_REJECTED,
    INBOUND_REJECTED,
    IN_PROGRESS,
    SUBMITTED,
    INBOUND_PICKING,
    IN_TRANSIT,
    DISPATCHED,
    RECEIVING,
    CLOSED,
    RECEIVED,
    CANCELED_INBOUND,
    CANCELED_REQUEST,
    CANCELED_TRANSFER,
    NO_VALUE;

    public String value() {
        return name();
    }

    public static StsTsfCriStatus fromValue(String v) {
        return valueOf(v);
    }

}
