
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for certificate_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="certificate_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="STORE"/>
 *     &lt;enumeration value="CORP"/>
 *     &lt;enumeration value="FOREIGN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "certificate_type")
@XmlEnum
public enum CertificateType {

    STORE,
    CORP,
    FOREIGN;

    public String value() {
        return name();
    }

    public static CertificateType fromValue(String v) {
        return valueOf(v);
    }

}
