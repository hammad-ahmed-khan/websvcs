
package com.oracle.retail.integration.base.bo.custorddeldesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_delivery_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_delivery_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="S"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_delivery_type")
@XmlEnum
public enum EnumDeliveryType {

    C,
    S;

    public String value() {
        return name();
    }

    public static EnumDeliveryType fromValue(String v) {
        return valueOf(v);
    }

}
