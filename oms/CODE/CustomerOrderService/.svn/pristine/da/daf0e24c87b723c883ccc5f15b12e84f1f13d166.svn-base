
package com.oracle.retail.integration.base.bo.contactdesc.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.phonedesc.v1.PhoneDesc;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PhoneDesc/v1}PhoneDesc" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "phoneDesc"
})
@XmlRootElement(name = "Phones")
public class Phones {

    @XmlElement(name = "PhoneDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/PhoneDesc/v1", required = true)
    protected List<PhoneDesc> phoneDesc;

    /**
     * Contact Phone.Gets the value of the phoneDesc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the phoneDesc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhoneDesc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PhoneDesc }
     * 
     * 
     */
    public List<PhoneDesc> getPhoneDesc() {
        if (phoneDesc == null) {
            phoneDesc = new ArrayList<PhoneDesc>();
        }
        return this.phoneDesc;
    }

}
