
package com.oracle.retail.integration.base.bo.custordfuldesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for partial_delivery_ind.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="partial_delivery_ind">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "partial_delivery_ind")
@XmlEnum
public enum PartialDeliveryInd {

    Y,
    N;

    public String value() {
        return name();
    }

    public static PartialDeliveryInd fromValue(String v) {
        return valueOf(v);
    }

}
