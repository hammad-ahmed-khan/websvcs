
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for echeck_conversion_code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="echeck_conversion_code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CONV"/>
 *     &lt;enumeration value="VERIFY"/>
 *     &lt;enumeration value="GUARENTEE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "echeck_conversion_code")
@XmlEnum
public enum EcheckConversionCode {

    CONV,
    VERIFY,
    GUARENTEE;

    public String value() {
        return name();
    }

    public static EcheckConversionCode fromValue(String v) {
        return valueOf(v);
    }

}
