
package com.oracle.retail.integration.base.bo.custorditmdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for product_group.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="product_group">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GCRD"/>
 *     &lt;enumeration value="ALTR"/>
 *     &lt;enumeration value="GCRT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "product_group")
@XmlEnum
public enum ProductGroup {

    GCRD,
    ALTR,
    GCRT;

    public String value() {
        return name();
    }

    public static ProductGroup fromValue(String v) {
        return valueOf(v);
    }

}
