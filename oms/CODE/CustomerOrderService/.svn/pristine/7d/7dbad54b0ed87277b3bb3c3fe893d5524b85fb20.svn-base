
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for payment_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="payment_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CASH"/>
 *     &lt;enumeration value="CREDIT"/>
 *     &lt;enumeration value="CHECK"/>
 *     &lt;enumeration value="TRAVCHECK"/>
 *     &lt;enumeration value="GIFTCERT"/>
 *     &lt;enumeration value="MAILCHECK"/>
 *     &lt;enumeration value="DEBIT"/>
 *     &lt;enumeration value="COUPON"/>
 *     &lt;enumeration value="GIFTCARD"/>
 *     &lt;enumeration value="STORECREDIT"/>
 *     &lt;enumeration value="MALLCERT"/>
 *     &lt;enumeration value="PURCHASEORDER"/>
 *     &lt;enumeration value="MONEYORDER"/>
 *     &lt;enumeration value="ECHECK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "payment_type")
@XmlEnum
public enum PaymentType {

    CASH,
    CREDIT,
    CHECK,
    TRAVCHECK,
    GIFTCERT,
    MAILCHECK,
    DEBIT,
    COUPON,
    GIFTCARD,
    STORECREDIT,
    MALLCERT,
    PURCHASEORDER,
    MONEYORDER,
    ECHECK;

    public String value() {
        return name();
    }

    public static PaymentType fromValue(String v) {
        return valueOf(v);
    }

}
