
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for entry_method.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="entry_method">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MANUAL"/>
 *     &lt;enumeration value="SCAN"/>
 *     &lt;enumeration value="MICR"/>
 *     &lt;enumeration value="SWIPE"/>
 *     &lt;enumeration value="ICC"/>
 *     &lt;enumeration value="WAVE"/>
 *     &lt;enumeration value="AUTO"/>
 *     &lt;enumeration value="ICCFLBK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "entry_method")
@XmlEnum
public enum EntryMethod {

    MANUAL,
    SCAN,
    MICR,
    SWIPE,
    ICC,
    WAVE,
    AUTO,
    ICCFLBK;

    public String value() {
        return name();
    }

    public static EntryMethod fromValue(String v) {
        return valueOf(v);
    }

}
