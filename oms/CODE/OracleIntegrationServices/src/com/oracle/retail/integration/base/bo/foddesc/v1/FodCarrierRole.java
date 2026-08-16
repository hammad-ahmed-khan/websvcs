
package com.oracle.retail.integration.base.bo.foddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fod_carrier_role.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fod_carrier_role">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SENDER"/>
 *     &lt;enumeration value="RECEIVER"/>
 *     &lt;enumeration value="THIRD_PARTY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fod_carrier_role")
@XmlEnum
public enum FodCarrierRole {

    SENDER,
    RECEIVER,
    THIRD_PARTY;

    public String value() {
        return name();
    }

    public static FodCarrierRole fromValue(String v) {
        return valueOf(v);
    }

}
