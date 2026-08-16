
package com.oracle.retail.integration.base.bo.discntlinedesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_discount_scope.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_discount_scope">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ORD"/>
 *     &lt;enumeration value="ITM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_discount_scope")
@XmlEnum
public enum EnumDiscountScope {

    ORD,
    ITM;

    public String value() {
        return name();
    }

    public static EnumDiscountScope fromValue(String v) {
        return valueOf(v);
    }

}
