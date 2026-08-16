
package com.oracle.retail.integration.base.bo.custordercrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_request_order_item_flag.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_request_order_item_flag">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NONE"/>
 *     &lt;enumeration value="FIRST"/>
 *     &lt;enumeration value="ALL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_request_order_item_flag")
@XmlEnum
public enum EnumRequestOrderItemFlag {

    NONE,
    FIRST,
    ALL;

    public String value() {
        return name();
    }

    public static EnumRequestOrderItemFlag fromValue(String v) {
        return valueOf(v);
    }

}
