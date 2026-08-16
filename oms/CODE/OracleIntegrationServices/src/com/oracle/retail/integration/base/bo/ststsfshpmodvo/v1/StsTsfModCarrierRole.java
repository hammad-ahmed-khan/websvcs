
package com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sts_tsf_mod_carrier_role.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sts_tsf_mod_carrier_role">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="THIRD_PARTY"/>
 *     &lt;enumeration value="SENDER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sts_tsf_mod_carrier_role")
@XmlEnum
public enum StsTsfModCarrierRole {

    THIRD_PARTY,
    SENDER;

    public String value() {
        return name();
    }

    public static StsTsfModCarrierRole fromValue(String v) {
        return valueOf(v);
    }

}
