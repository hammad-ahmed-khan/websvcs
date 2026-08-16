
package com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilordcfmdtl.v1.FulfilOrdCfmDtl;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="customer_order_no" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fulfill_order_no" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="confirm_type" type="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdCfmDesc/v1}confirm_type"/&gt;
 *         &lt;element name="confirm_no" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdCfmDtl/v1}FulfilOrdCfmDtl" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "customerOrderNo",
    "fulfillOrderNo",
    "confirmType",
    "confirmNo",
    "fulfilOrdCfmDtl"
})
@XmlRootElement(name = "FulfilOrdCfmDesc")
public class FulfilOrdCfmDesc {

    @XmlElement(name = "customer_order_no", required = true)
    protected String customerOrderNo;
    @XmlElement(name = "fulfill_order_no", required = true)
    protected String fulfillOrderNo;
    @XmlElement(name = "confirm_type", required = true)
    @XmlSchemaType(name = "string")
    protected ConfirmType confirmType;
    @XmlElement(name = "confirm_no")
    protected Long confirmNo;
    @XmlElement(name = "FulfilOrdCfmDtl", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdCfmDtl/v1")
    protected List<FulfilOrdCfmDtl> fulfilOrdCfmDtl;

    /**
     * Gets the value of the customerOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * Sets the value of the customerOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderNo(String value) {
        this.customerOrderNo = value;
    }

    /**
     * Gets the value of the fulfillOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    /**
     * Sets the value of the fulfillOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillOrderNo(String value) {
        this.fulfillOrderNo = value;
    }

    /**
     * Gets the value of the confirmType property.
     * 
     * @return
     *     possible object is
     *     {@link ConfirmType }
     *     
     */
    public ConfirmType getConfirmType() {
        return confirmType;
    }

    /**
     * Sets the value of the confirmType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfirmType }
     *     
     */
    public void setConfirmType(ConfirmType value) {
        this.confirmType = value;
    }

    /**
     * Gets the value of the confirmNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getConfirmNo() {
        return confirmNo;
    }

    /**
     * Sets the value of the confirmNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setConfirmNo(Long value) {
        this.confirmNo = value;
    }

    /**
     * Gets the value of the fulfilOrdCfmDtl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fulfilOrdCfmDtl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFulfilOrdCfmDtl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FulfilOrdCfmDtl }
     * 
     * 
     */
    public List<FulfilOrdCfmDtl> getFulfilOrdCfmDtl() {
        if (fulfilOrdCfmDtl == null) {
            fulfilOrdCfmDtl = new ArrayList<FulfilOrdCfmDtl>();
        }
        return this.fulfilOrdCfmDtl;
    }

}
