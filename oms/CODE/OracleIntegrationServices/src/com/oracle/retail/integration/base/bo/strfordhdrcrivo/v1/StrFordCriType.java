
package com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StrFordCriType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StrFordCriType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LAYAWAY"/>
 *     &lt;enumeration value="PICKUP_AND_DELIVERY"/>
 *     &lt;enumeration value="CUSTOMER_ORDER"/>
 *     &lt;enumeration value="PENDING_PURCHASE"/>
 *     &lt;enumeration value="SPECIAL_ORDER"/>
 *     &lt;enumeration value="WEB_ORDER"/>
 *     &lt;enumeration value="NO_VALUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StrFordCriType")
@XmlEnum
public enum StrFordCriType {

    LAYAWAY,
    PICKUP_AND_DELIVERY,
    CUSTOMER_ORDER,
    PENDING_PURCHASE,
    SPECIAL_ORDER,
    WEB_ORDER,
    NO_VALUE;

    public String value() {
        return name();
    }

    public static StrFordCriType fromValue(String v) {
        return valueOf(v);
    }

}
