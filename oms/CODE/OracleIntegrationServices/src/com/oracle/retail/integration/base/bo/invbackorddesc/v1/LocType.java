
package com.oracle.retail.integration.base.bo.invbackorddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loc_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="loc_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="S"/>
 *     &lt;enumeration value="W"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "loc_type")
@XmlEnum
public enum LocType {

    S,
    W;

    public String value() {
        return name();
    }

    public static LocType fromValue(String v) {
        return valueOf(v);
    }

}
