
package com.oracle.retail.integration.base.bo.customerdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for prim_addr_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="prim_addr_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "prim_addr_type")
@XmlEnum
public enum PrimAddrType {

    Y,
    N;

    public String value() {
        return name();
    }

    public static PrimAddrType fromValue(String v) {
        return valueOf(v);
    }

}
