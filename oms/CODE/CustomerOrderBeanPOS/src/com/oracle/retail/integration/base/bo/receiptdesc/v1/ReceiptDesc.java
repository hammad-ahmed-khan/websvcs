
package com.oracle.retail.integration.base.bo.receiptdesc.v1;

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
 *         &lt;element name="schedule_nbr" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="appt_nbr" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1}Receipt" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1}ReceiptOverage" maxOccurs="unbounded" minOccurs="0"/>
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
    "scheduleNbr",
    "apptNbr",
    "receipt",
    "receiptOverage"
})
@XmlRootElement(name = "ReceiptDesc")
public class ReceiptDesc {

    @XmlElement(name = "schedule_nbr")
    protected Integer scheduleNbr;
    @XmlElement(name = "appt_nbr")
    protected Integer apptNbr;
    @XmlElement(name = "Receipt", required = true)
    protected List<Receipt> receipt;
    @XmlElement(name = "ReceiptOverage")
    protected List<ReceiptOverage> receiptOverage;

    /**
     * Gets the value of the scheduleNbr property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getScheduleNbr() {
        return scheduleNbr;
    }

    /**
     * Sets the value of the scheduleNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setScheduleNbr(Integer value) {
        this.scheduleNbr = value;
    }

    /**
     * Gets the value of the apptNbr property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getApptNbr() {
        return apptNbr;
    }

    /**
     * Sets the value of the apptNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setApptNbr(Integer value) {
        this.apptNbr = value;
    }

    /**
     * Description is not available.Gets the value of the receipt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receipt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceipt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Receipt }
     * 
     * 
     */
    public List<Receipt> getReceipt() {
        if (receipt == null) {
            receipt = new ArrayList<Receipt>();
        }
        return this.receipt;
    }

    /**
     * Contains the Receipt Overage Information Gets the value of the receiptOverage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiptOverage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiptOverage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceiptOverage }
     * 
     * 
     */
    public List<ReceiptOverage> getReceiptOverage() {
        if (receiptOverage == null) {
            receiptOverage = new ArrayList<ReceiptOverage>();
        }
        return this.receiptOverage;
    }

}
