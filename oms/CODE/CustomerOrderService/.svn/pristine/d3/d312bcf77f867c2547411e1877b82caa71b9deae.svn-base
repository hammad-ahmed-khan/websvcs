
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for card_type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="card_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VISA"/>
 *     &lt;enumeration value="MASTER"/>
 *     &lt;enumeration value="AMEX"/>
 *     &lt;enumeration value="DISCOVER"/>
 *     &lt;enumeration value="DINERS"/>
 *     &lt;enumeration value="HOUSECARD"/>
 *     &lt;enumeration value="JCB"/>
 *     &lt;enumeration value="GIFTCARD"/>
 *     &lt;enumeration value="SPAN"/>
 *
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "card_type")
@XmlEnum
public enum CardType {

    VISA,
    MASTER,
    AMEX,
    DISCOVER,
    DINERS,
    HOUSECARD,
    JCB,
    GIFTCARD,
    SPAN;

    public String value() {
        return name();
    }

    public static CardType fromValue(String v) {
        return valueOf(v);
    }

}
