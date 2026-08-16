
package com.oracle.retail.integration.base.bo.ststsfdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sts_tsf_carrier_role.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sts_tsf_carrier_role"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="THIRD_PARTY"/&gt;
 *     &lt;enumeration value="SENDER"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
