
package com.oracle.retail.integration.base.bo.fulfilordref.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for source_loc_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="source_loc_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SU"/>
 *     &lt;enumeration value="ST"/>
 *     &lt;enumeration value="WH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "source_loc_type")
@XmlEnum
public enum SourceLocType {

    SU,
    ST,
    WH;

    public String value() {
        return name();
    }

    public static SourceLocType fromValue(String v) {
        return valueOf(v);
    }

}
