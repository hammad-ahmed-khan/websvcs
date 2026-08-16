
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for personal_id_swiped_flag.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="personal_id_swiped_flag">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "personal_id_swiped_flag")
@XmlEnum
public enum PersonalIdSwipedFlag {

    Y,
    N;

    public String value() {
        return name();
    }

    public static PersonalIdSwipedFlag fromValue(String v) {
        return valueOf(v);
    }

}
