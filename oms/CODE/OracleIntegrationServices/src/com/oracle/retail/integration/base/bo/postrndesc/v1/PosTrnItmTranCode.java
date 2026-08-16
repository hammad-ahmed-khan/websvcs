
package com.oracle.retail.integration.base.bo.postrndesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PosTrnItmTranCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PosTrnItmTranCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SALE"/>
 *     &lt;enumeration value="RETURN"/>
 *     &lt;enumeration value="VOID_SALE"/>
 *     &lt;enumeration value="VOID_RETURN"/>
 *     &lt;enumeration value="ORDER_NEW"/>
 *     &lt;enumeration value="ORDER_FULFILL"/>
 *     &lt;enumeration value="ORDER_CANCEL"/>
 *     &lt;enumeration value="ORDER_CANCEL_FULFILL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PosTrnItmTranCode")
@XmlEnum
public enum PosTrnItmTranCode {

    SALE,
    RETURN,
    VOID_SALE,
    VOID_RETURN,
    ORDER_NEW,
    ORDER_FULFILL,
    ORDER_CANCEL,
    ORDER_CANCEL_FULFILL;

    public String value() {
        return name();
    }

    public static PosTrnItmTranCode fromValue(String v) {
        return valueOf(v);
    }

}
