
package com.oracle.retail.integration.base.bo.ststsfdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sts_tsf_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sts_tsf_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="PENDING"/>
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="CANCELED_REQUEST"/>
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="DISPATCHED"/>
 *     &lt;enumeration value="SUBMITTED"/>
 *     &lt;enumeration value="RECEIVING"/>
 *     &lt;enumeration value="RECEIVED"/>
 *     &lt;enumeration value="CANCELED_TRANSFER"/>
 *     &lt;enumeration value="UNKNOWN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sts_tsf_status")
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
