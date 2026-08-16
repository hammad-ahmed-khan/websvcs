
package com.oracle.retail.integration.base.bo.fulfilordref.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for source_loc_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="source_loc_type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SU"/&gt;
 *     &lt;enumeration value="ST"/&gt;
 *     &lt;enumeration value="WH"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
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
