
package com.oracle.retail.integration.base.bo.pendrtrndesc.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="physical_wh" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="rma_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cust_order_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="special_instructions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expected_receipt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PendRtrnDesc/v1}PendRtrnDtlDesc" maxOccurs="unbounded"/>
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
    "physicalWh",
    "rmaNbr",
    "custOrderNbr",
    "specialInstructions",
    "expectedReceipt",
    "pendRtrnDtlDesc"
})
@XmlRootElement(name = "PendRtrnDesc")
public class PendRtrnDesc {

    @XmlElement(name = "physical_wh")
    protected long physicalWh;
    @XmlElement(name = "rma_nbr")
    protected String rmaNbr;
    @XmlElement(name = "cust_order_nbr")
    protected String custOrderNbr;
    @XmlElement(name = "special_instructions")
    protected String specialInstructions;
    @XmlElement(name = "expected_receipt")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expectedReceipt;
    @XmlElement(name = "PendRtrnDtlDesc", required = true)
    protected List<PendRtrnDtlDesc> pendRtrnDtlDesc;

    /**
     * Gets the value of the physicalWh property.
     * 
     */
    public long getPhysicalWh() {
        return physicalWh;
    }

    /**
     * Sets the value of the physicalWh property.
     * 
     */
    public void setPhysicalWh(long value) {
        this.physicalWh = value;
    }

    /**
     * Gets the value of the rmaNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmaNbr() {
        return rmaNbr;
    }

    /**
     * Sets the value of the rmaNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmaNbr(String value) {
        this.rmaNbr = value;
    }

    /**
     * Gets the value of the custOrderNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustOrderNbr() {
        return custOrderNbr;
    }

    /**
     * Sets the value of the custOrderNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustOrderNbr(String value) {
        this.custOrderNbr = value;
    }

    /**
     * Gets the value of the specialInstructions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecialInstructions() {
        return specialInstructions;
    }

    /**
     * Sets the value of the specialInstructions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecialInstructions(String value) {
        this.specialInstructions = value;
    }

    /**
     * Gets the value of the expectedReceipt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpectedReceipt() {
        return expectedReceipt;
    }

    /**
     * Sets the value of the expectedReceipt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpectedReceipt(XMLGregorianCalendar value) {
        this.expectedReceipt = value;
    }

    /**
     * Gets the value of the pendRtrnDtlDesc property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pendRtrnDtlDesc property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getPendRtrnDtlDesc().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlDesc}
     *
     *
     */
    public List<PendRtrnDtlDesc> getPendRtrnDtlDesc() {
        if (pendRtrnDtlDesc == null) {
            pendRtrnDtlDesc = new ArrayList<PendRtrnDtlDesc>();
        }
        return this.pendRtrnDtlDesc;
    }

}
