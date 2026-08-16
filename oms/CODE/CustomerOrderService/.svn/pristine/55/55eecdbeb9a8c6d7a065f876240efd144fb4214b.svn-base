
package com.oracle.retail.integration.base.bo.discntlinedesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_discount_method.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_discount_method">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PCT"/>
 *     &lt;enumeration value="AMT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_discount_method")
@XmlEnum
public enum EnumDiscountMethod {

    PCT,
    AMT;

    public String value() {
        return name();
    }

    public static EnumDiscountMethod fromValue(String v) {
        return valueOf(v);
    }

}
