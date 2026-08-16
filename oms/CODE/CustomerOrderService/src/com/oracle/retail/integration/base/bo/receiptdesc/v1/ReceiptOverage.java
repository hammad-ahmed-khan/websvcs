
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
 *         &lt;element name="po_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="document_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asn_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1}ReceiptOverageDtl" maxOccurs="unbounded" minOccurs="0"/>
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
    "poNbr",
    "documentType",
    "asnNbr",
    "receiptOverageDtl"
})
@XmlRootElement(name = "ReceiptOverage")
public class ReceiptOverage {

    @XmlElement(name = "po_nbr")
    protected String poNbr;
    @XmlElement(name = "document_type")
    protected String documentType;
    @XmlElement(name = "asn_nbr")
    protected String asnNbr;
    @XmlElement(name = "ReceiptOverageDtl")
    protected List<ReceiptOverageDtl> receiptOverageDtl;

    /**
     * Gets the value of the poNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoNbr() {
        return poNbr;
    }

    /**
     * Sets the value of the poNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoNbr(String value) {
        this.poNbr = value;
    }

    /**
     * Gets the value of the documentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Sets the value of the documentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentType(String value) {
        this.documentType = value;
    }

    /**
     * Gets the value of the asnNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsnNbr() {
        return asnNbr;
    }

    /**
     * Sets the value of the asnNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsnNbr(String value) {
        this.asnNbr = value;
    }

    /**
     * Contains Overage Receipt Detail Information Gets the value of the receiptOverageDtl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiptOverageDtl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiptOverageDtl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceiptOverageDtl }
     * 
     * 
     */
    public List<ReceiptOverageDtl> getReceiptOverageDtl() {
        if (receiptOverageDtl == null) {
            receiptOverageDtl = new ArrayList<ReceiptOverageDtl>();
        }
        return this.receiptOverageDtl;
    }

}
