
package com.oracle.retail.integration.base.bo.taxlinedesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_tax_mod_scope.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_tax_mod_scope">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ORD"/>
 *     &lt;enumeration value="ITM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_tax_mod_scope")
@XmlEnum
public enum EnumTaxModScope {

    ORD,
    ITM;

    public String value() {
        return name();
    }

    public static EnumTaxModScope fromValue(String v) {
        return valueOf(v);
    }

}
