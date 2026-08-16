
package com.oracle.retail.integration.base.bo.custorditmdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for alteration_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="alteration_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="COAT"/>
 *     &lt;enumeration value="DRESS"/>
 *     &lt;enumeration value="PANTS"/>
 *     &lt;enumeration value="SHIRT"/>
 *     &lt;enumeration value="SKIRT"/>
 *     &lt;enumeration value="REPAIR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "alteration_type")
@XmlEnum
public enum AlterationType {

    COAT,
    DRESS,
    PANTS,
    SHIRT,
    SKIRT,
    REPAIR;

    public String value() {
        return name();
    }

    public static AlterationType fromValue(String v) {
        return valueOf(v);
    }

}
