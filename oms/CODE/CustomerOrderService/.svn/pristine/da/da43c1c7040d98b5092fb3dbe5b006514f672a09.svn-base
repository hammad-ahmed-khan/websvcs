
package com.oracle.retail.integration.base.bo.custorderdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for order_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="order_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="PARTIAL"/>
 *     &lt;enumeration value="CANCELED"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="FILLED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "order_status")
@XmlEnum
public enum OrderStatus {

    NEW,
    PARTIAL,
    CANCELED,
    COMPLETED,
    FILLED;

    public String value() {
        return name();
    }

    public static OrderStatus fromValue(String v) {
        return valueOf(v);
    }

}
