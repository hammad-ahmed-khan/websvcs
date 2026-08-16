
package com.oracle.retail.integration.base.bo.taxlinedesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_tax_mode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_tax_mode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="STANDARD"/>
 *     &lt;enumeration value="NONTAXABLE"/>
 *     &lt;enumeration value="OVERRIDERATE"/>
 *     &lt;enumeration value="OVERRIDEAMT"/>
 *     &lt;enumeration value="TOGGLEON"/>
 *     &lt;enumeration value="TOGGLEOFF"/>
 *     &lt;enumeration value="EXEMPT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_tax_mode")
@XmlEnum
public enum EnumTaxMode {

    STANDARD,
    NONTAXABLE,
    OVERRIDERATE,
    OVERRIDEAMT,
    TOGGLEON,
    TOGGLEOFF,
    EXEMPT;

    public String value() {
        return name();
    }

    public static EnumTaxMode fromValue(String v) {
        return valueOf(v);
    }

}
