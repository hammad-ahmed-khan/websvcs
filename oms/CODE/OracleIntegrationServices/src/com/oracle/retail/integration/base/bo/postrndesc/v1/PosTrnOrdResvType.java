
package com.oracle.retail.integration.base.bo.postrndesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PosTrnOrdResvType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PosTrnOrdResvType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="WEB_ORDER"/>
 *     &lt;enumeration value="SPECIAL_ORDER"/>
 *     &lt;enumeration value="PICKUP_OR_DELIVERY"/>
 *     &lt;enumeration value="LAYAWAY"/>
 *     &lt;enumeration value="NO_VALUE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PosTrnOrdResvType")
@XmlEnum
public enum PosTrnOrdResvType {

    WEB_ORDER,
    SPECIAL_ORDER,
    PICKUP_OR_DELIVERY,
    LAYAWAY,
    NO_VALUE;

    public String value() {
        return name();
    }

    public static PosTrnOrdResvType fromValue(String v) {
        return valueOf(v);
    }

}
