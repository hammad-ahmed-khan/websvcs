
package com.oracle.retail.integration.base.bo.fulfilorddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for yes_no_ind.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="yes_no_ind">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "yes_no_ind")
@XmlEnum
public enum YesNoInd {

    Y,
    N;

    public String value() {
        return name();
    }

    public static YesNoInd fromValue(String v) {
        return valueOf(v);
    }

}
