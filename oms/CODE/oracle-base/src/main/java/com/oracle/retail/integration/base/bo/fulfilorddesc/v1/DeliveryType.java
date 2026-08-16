
package com.oracle.retail.integration.base.bo.fulfilorddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for delivery_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="delivery_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="S"/&gt;
 *     &lt;enumeration value="C"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
