
package com.oracle.retail.integration.base.bo.strinvgpcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StrInvUomType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StrInvUomType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="STANDARD"/>
 *     &lt;enumeration value="SELLING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StrInvUomType")
@XmlEnum
public enum StrInvUomType {

    STANDARD,
    SELLING;

    public String value() {
        return name();
    }

    public static StrInvUomType fromValue(String v) {
        return valueOf(v);
    }

}
