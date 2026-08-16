
package com.oracle.retail.integration.base.bo.invavaildesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pack_calculate_ind.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="pack_calculate_ind">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "pack_calculate_ind")
@XmlEnum
public enum PackCalculateInd {

    Y,
    N;

    public String value() {
        return name();
    }

    public static PackCalculateInd fromValue(String v) {
        return valueOf(v);
    }

}
