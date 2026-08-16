
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for coupon_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="coupon_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MANUFACTURER"/>
 *     &lt;enumeration value="STORE"/>
 *     &lt;enumeration value="ELECTRONIC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "coupon_type")
@XmlEnum
public enum CouponType {

    MANUFACTURER,
    STORE,
    ELECTRONIC;

    public String value() {
        return name();
    }

    public static CouponType fromValue(String v) {
        return valueOf(v);
    }

}
