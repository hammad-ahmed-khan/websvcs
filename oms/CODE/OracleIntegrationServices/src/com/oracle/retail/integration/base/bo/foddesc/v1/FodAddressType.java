
package com.oracle.retail.integration.base.bo.foddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fod_address_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fod_address_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BUSINESS"/>
 *     &lt;enumeration value="POSTAL"/>
 *     &lt;enumeration value="RETURNS"/>
 *     &lt;enumeration value="ORDER"/>
 *     &lt;enumeration value="INVOICE"/>
 *     &lt;enumeration value="REMITTANCE"/>
 *     &lt;enumeration value="NO_VALUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fod_address_type")
@XmlEnum
public enum FodAddressType {

    BUSINESS,
    POSTAL,
    RETURNS,
    ORDER,
    INVOICE,
    REMITTANCE,
    NO_VALUE;

    public String value() {
        return name();
    }

    public static FodAddressType fromValue(String v) {
        return valueOf(v);
    }

}
