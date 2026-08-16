
package com.oracle.retail.integration.base.bo.custorditmdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for item_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="item_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="STCK"/>
 *     &lt;enumeration value="SERV"/>
 *     &lt;enumeration value="CPON"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "item_type")
@XmlEnum
public enum ItemType {

    STCK,
    SERV,
    CPON;

    public String value() {
        return name();
    }

    public static ItemType fromValue(String v) {
        return valueOf(v);
    }

}
