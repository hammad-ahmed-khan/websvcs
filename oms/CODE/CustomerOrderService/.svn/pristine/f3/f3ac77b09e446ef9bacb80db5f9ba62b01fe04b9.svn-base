
package com.oracle.retail.integration.base.bo.custorditmdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for giftcard_req_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="giftcard_req_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ISSUE"/>
 *     &lt;enumeration value="RELOAD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "giftcard_req_type")
@XmlEnum
public enum GiftcardReqType {

    ISSUE,
    RELOAD;

    public String value() {
        return name();
    }

    public static GiftcardReqType fromValue(String v) {
        return valueOf(v);
    }

}
