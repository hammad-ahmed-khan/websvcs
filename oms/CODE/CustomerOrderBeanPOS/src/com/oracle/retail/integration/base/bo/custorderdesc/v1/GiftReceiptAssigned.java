
package com.oracle.retail.integration.base.bo.custorderdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gift_receipt_assigned.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gift_receipt_assigned">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gift_receipt_assigned")
@XmlEnum
public enum GiftReceiptAssigned {

    Y,
    N;

    public String value() {
        return name();
    }

    public static GiftReceiptAssigned fromValue(String v) {
        return valueOf(v);
    }

}
