
package com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StsTsfCriStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StsTsfCriStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="ACTIVE_INBOUND"/>
 *     &lt;enumeration value="ACTIVE_OUTBOUND"/>
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="PENDING_REQUEST"/>
 *     &lt;enumeration value="AWAITING_RESPONSE"/>
 *     &lt;enumeration value="OUTBOUND_REJECTED"/>
 *     &lt;enumeration value="INBOUND_REJECTED"/>
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="SUBMITTED"/>
 *     &lt;enumeration value="INBOUND_PICKING"/>
 *     &lt;enumeration value="IN_TRANSIT"/>
 *     &lt;enumeration value="DISPATCHED"/>
 *     &lt;enumeration value="RECEIVING"/>
 *     &lt;enumeration value="CLOSED"/>
 *     &lt;enumeration value="RECEIVED"/>
 *     &lt;enumeration value="CANCELED_INBOUND"/>
 *     &lt;enumeration value="CANCELED_REQUEST"/>
 *     &lt;enumeration value="CANCELED_TRANSFER"/>
 *     &lt;enumeration value="NO_VALUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
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
