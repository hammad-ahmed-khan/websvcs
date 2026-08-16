
package com.oracle.retail.integration.base.bo.strforddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for str_ford_delivery_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="str_ford_delivery_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PICKUP"/&gt;
 *     &lt;enumeration value="SHIPMENT"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "str_ford_delivery_type")
@XmlEnum
public enum StrFordDeliveryType {

    PICKUP,
    SHIPMENT,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static StrFordDeliveryType fromValue(String v) {
        return valueOf(v);
    }

}
