
package com.oracle.retail.integration.base.bo.foddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fod_address_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fod_address_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BUSINESS"/&gt;
 *     &lt;enumeration value="POSTAL"/&gt;
 *     &lt;enumeration value="RETURNS"/&gt;
 *     &lt;enumeration value="ORDER"/&gt;
 *     &lt;enumeration value="INVOICE"/&gt;
 *     &lt;enumeration value="REMITTANCE"/&gt;
 *     &lt;enumeration value="NO_VALUE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
