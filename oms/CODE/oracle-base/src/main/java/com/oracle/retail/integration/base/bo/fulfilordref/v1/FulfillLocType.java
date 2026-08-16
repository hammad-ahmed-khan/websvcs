
package com.oracle.retail.integration.base.bo.fulfilordref.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fulfill_loc_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fulfill_loc_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="S"/&gt;
 *     &lt;enumeration value="V"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "fulfill_loc_type")
@XmlEnum
public enum FulfillLocType {

    S,
    V;

    public String value() {
        return name();
    }

    public static FulfillLocType fromValue(String v) {
        return valueOf(v);
    }

}
