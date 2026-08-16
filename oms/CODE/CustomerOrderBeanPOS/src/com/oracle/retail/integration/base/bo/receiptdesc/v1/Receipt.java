
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
 *         &lt;element name="dc_dest_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="po_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cust_order_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fulfill_order_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="document_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ref_doc_no" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="asn_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1}ReceiptDtl" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1}ReceiptCartonDtl" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="receipt_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="from_loc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="from_loc_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "dcDestId",
    "poNbr",
    "custOrderNbr",
    "fulfillOrderNbr",
    "documentType",
    "refDocNo",
    "asnNbr",
    "receiptDtl",
    "receiptCartonDtl",
    "receiptType",
    "fromLoc",
    "fromLocType"
})
@XmlRootElement(name = "Receipt")
public class Receipt {

    @XmlElement(name = "dc_dest_id", required = true)
    protected String dcDestId;
    @XmlElement(name = "po_nbr")
    protected String poNbr;
    @XmlElement(name = "cust_order_nbr")
    protected String custOrderNbr;
    @XmlElement(name = "fulfill_order_nbr")
    protected String fulfillOrderNbr;
    @XmlElement(name = "document_type", required = true)
    protected String documentType;
    @XmlElement(name = "ref_doc_no")
    protected Long refDocNo;
    @XmlElement(name = "asn_nbr")
    protected String asnNbr;
    @XmlElement(name = "ReceiptDtl")
    protected List<ReceiptDtl> receiptDtl;
    @XmlElement(name = "ReceiptCartonDtl")
    protected List<ReceiptCartonDtl> receiptCartonDtl;
    @XmlElement(name = "receipt_type")
    protected String receiptType;
    @XmlElement(name = "from_loc")
    protected String fromLoc;
    @XmlElement(name = "from_loc_type")
    protected String fromLocType;

    /**
     * Gets the value of the dcDestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDcDestId() {
        return dcDestId;
    }

    /**
     * Sets the value of the dcDestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDcDestId(String value) {
        this.dcDestId = value;
    }

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
     * Gets the value of the fulfillOrderNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillOrderNbr() {
        return fulfillOrderNbr;
    }

    /**
     * Sets the value of the fulfillOrderNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillOrderNbr(String value) {
        this.fulfillOrderNbr = value;
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
     * Gets the value of the refDocNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRefDocNo() {
        return refDocNo;
    }

    /**
     * Sets the value of the refDocNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRefDocNo(Long value) {
        this.refDocNo = value;
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
     * Description is not available.Gets the value of the receiptDtl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiptDtl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiptDtl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceiptDtl }
     * 
     * 
     */
    public List<ReceiptDtl> getReceiptDtl() {
        if (receiptDtl == null) {
            receiptDtl = new ArrayList<ReceiptDtl>();
        }
        return this.receiptDtl;
    }

    /**
     * Description is not available.Gets the value of the receiptCartonDtl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiptCartonDtl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiptCartonDtl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceiptCartonDtl }
     * 
     * 
     */
    public List<ReceiptCartonDtl> getReceiptCartonDtl() {
        if (receiptCartonDtl == null) {
            receiptCartonDtl = new ArrayList<ReceiptCartonDtl>();
        }
        return this.receiptCartonDtl;
    }

    /**
     * Gets the value of the receiptType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptType() {
        return receiptType;
    }

    /**
     * Sets the value of the receiptType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptType(String value) {
        this.receiptType = value;
    }

    /**
     * Gets the value of the fromLoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromLoc() {
        return fromLoc;
    }

    /**
     * Sets the value of the fromLoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromLoc(String value) {
        this.fromLoc = value;
    }

    /**
     * Gets the value of the fromLocType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromLocType() {
        return fromLocType;
    }

    /**
     * Sets the value of the fromLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromLocType(String value) {
        this.fromLocType = value;
    }

}
