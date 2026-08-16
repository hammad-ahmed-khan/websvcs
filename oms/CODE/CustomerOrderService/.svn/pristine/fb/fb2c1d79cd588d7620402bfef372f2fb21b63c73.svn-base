
package com.oracle.retail.integration.base.bo.custorditmdesc.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="alteration_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}alteration_type"/>
 *         &lt;element name="instruction" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
    "alterationType",
    "instruction"
})
@XmlRootElement(name = "AlterationItem")
public class AlterationItem {

    @XmlElement(name = "alteration_type", required = true)
    protected AlterationType alterationType;
    @XmlElement(nillable = true)
    protected List<String> instruction;

    /**
     * Gets the value of the alterationType property.
     * 
     * @return
     *     possible object is
     *     {@link AlterationType }
     *     
     */
    public AlterationType getAlterationType() {
        return alterationType;
    }

    /**
     * Sets the value of the alterationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlterationType }
     *     
     */
    public void setAlterationType(AlterationType value) {
        this.alterationType = value;
    }

    /**
     * Gets the value of the instruction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instruction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstruction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getInstruction() {
        if (instruction == null) {
            instruction = new ArrayList<String>();
        }
        return this.instruction;
    }

}
