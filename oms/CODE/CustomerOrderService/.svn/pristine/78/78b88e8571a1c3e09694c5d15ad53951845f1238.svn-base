
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for authorization_method.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="authorization_method">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AUTO"/>
 *     &lt;enumeration value="SYST"/>
 *     &lt;enumeration value="MANU"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "authorization_method")
@XmlEnum
public enum AuthorizationMethod {

    AUTO,
    SYST,
    MANU;

    public String value() {
        return name();
    }

    public static AuthorizationMethod fromValue(String v) {
        return valueOf(v);
    }

}
