
package com.oracle.retail.integration.base.bo.fulfilorddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for delivery_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="delivery_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="C"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "delivery_type")
@XmlEnum
public enum DeliveryType {

    S,
    C;

    public String value() {
        return name();
    }

    public static DeliveryType fromValue(String v) {
        return valueOf(v);
    }

}
