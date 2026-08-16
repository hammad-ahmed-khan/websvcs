
package com.oracle.retail.integration.base.bo.custordfuldesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ship_to_fulfill_loc_flag.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ship_to_fulfill_loc_flag">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ship_to_fulfill_loc_flag")
@XmlEnum
public enum ShipToFulfillLocFlag {

    Y,
    N;

    public String value() {
        return name();
    }

    public static ShipToFulfillLocFlag fromValue(String v) {
        return valueOf(v);
    }

}
