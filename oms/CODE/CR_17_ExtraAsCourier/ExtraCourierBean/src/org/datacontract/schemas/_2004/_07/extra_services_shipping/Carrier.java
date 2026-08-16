
package org.datacontract.schemas._2004._07.extra_services_shipping;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Carrier.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Carrier">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Aramex"/>
 *     &lt;enumeration value="Smsa"/>
 *     &lt;enumeration value="Alshrouq"/>
 *     &lt;enumeration value="Fetchr"/>
 *     &lt;enumeration value="Ups"/>
 *     &lt;enumeration value="Dhl"/>
 *     &lt;enumeration value="Jak"/>
 *     &lt;enumeration value="Extra"/>
 *     &lt;enumeration value="TAMCO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Carrier")
@XmlEnum
public enum Carrier {

    @XmlEnumValue("Aramex")
    ARAMEX("Aramex"),
    @XmlEnumValue("Smsa")
    SMSA("Smsa"),
    @XmlEnumValue("Alshrouq")
    ALSHROUQ("Alshrouq"),
    @XmlEnumValue("Fetchr")
    FETCHR("Fetchr"),
    @XmlEnumValue("Ups")
    UPS("Ups"),
    @XmlEnumValue("Dhl")
    DHL("Dhl"),
    @XmlEnumValue("Jak")
    JAK("Jak"),
    @XmlEnumValue("Extra")
    EXTRA("Extra"),
    TAMCO("TAMCO");
    private final String value;

    Carrier(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Carrier fromValue(String v) {
        for (Carrier c: Carrier.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
