
package com.oracle.retail.integration.base.bo.custordercrivo.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enum_search_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enum_search_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ORDERID"/>
 *     &lt;enumeration value="CUSTOMER"/>
 *     &lt;enumeration value="CHARGECARD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enum_search_type")
@XmlEnum
public enum EnumSearchType {

    ORDERID,
    CUSTOMER,
    CHARGECARD;

    public String value() {
        return name();
    }

    public static EnumSearchType fromValue(String v) {
        return valueOf(v);
    }

}
