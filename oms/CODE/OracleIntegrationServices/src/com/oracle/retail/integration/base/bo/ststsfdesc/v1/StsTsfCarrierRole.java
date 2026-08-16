
package com.oracle.retail.integration.base.bo.ststsfdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sts_tsf_carrier_role.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sts_tsf_carrier_role">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="THIRD_PARTY"/>
 *     &lt;enumeration value="SENDER"/>
 *     &lt;enumeration value="UNKNOWN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sts_tsf_carrier_role")
@XmlEnum
public enum StsTsfCarrierRole {

    THIRD_PARTY,
    SENDER,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static StsTsfCarrierRole fromValue(String v) {
        return valueOf(v);
    }

}
