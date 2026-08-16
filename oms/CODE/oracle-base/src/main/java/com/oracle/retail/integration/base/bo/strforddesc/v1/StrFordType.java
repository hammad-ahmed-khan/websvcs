
package com.oracle.retail.integration.base.bo.strforddesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for str_ford_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="str_ford_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LAYAWAY"/&gt;
 *     &lt;enumeration value="PICKUP_AND_DELIVERY"/&gt;
 *     &lt;enumeration value="CUSTOMER_ORDER"/&gt;
 *     &lt;enumeration value="PENDING_PURCHASE"/&gt;
 *     &lt;enumeration value="SPECIAL_ORDER"/&gt;
 *     &lt;enumeration value="WEB_ORDER"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "str_ford_type")
@XmlEnum
public enum StrFordType {

    LAYAWAY,
    PICKUP_AND_DELIVERY,
    CUSTOMER_ORDER,
    PENDING_PURCHASE,
    SPECIAL_ORDER,
    WEB_ORDER,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static StrFordType fromValue(String v) {
        return valueOf(v);
    }

}
