
package com.oracle.retail.integration.base.bo.discntlinedesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_discount_assign_basis.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_discount_assign_basis">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MANU"/>
 *     &lt;enumeration value="CUST"/>
 *     &lt;enumeration value="ITEM"/>
 *     &lt;enumeration value="CPON"/>
 *     &lt;enumeration value="EMPL"/>
 *     &lt;enumeration value="OTHR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_discount_assign_basis")
@XmlEnum
public enum EnumDiscountAssignBasis {

    MANU,
    CUST,
    ITEM,
    CPON,
    EMPL,
    OTHR;

    public String value() {
        return name();
    }

    public static EnumDiscountAssignBasis fromValue(String v) {
        return valueOf(v);
    }

}
