
package com.oracle.retail.integration.base.bo.invavailcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for store_pickup_ind.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="store_pickup_ind">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "store_pickup_ind")
@XmlEnum
public enum StorePickupInd {

    Y,
    N;

    public String value() {
        return name();
    }

    public static StorePickupInd fromValue(String v) {
        return valueOf(v);
    }

}
