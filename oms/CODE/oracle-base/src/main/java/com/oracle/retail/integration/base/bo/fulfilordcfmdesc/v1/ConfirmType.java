
package com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for confirm_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="confirm_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="P"/&gt;
 *     &lt;enumeration value="X"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "confirm_type")
@XmlEnum
public enum ConfirmType {

    P,
    X;

    public String value() {
        return name();
    }

    public static ConfirmType fromValue(String v) {
        return valueOf(v);
    }

}
