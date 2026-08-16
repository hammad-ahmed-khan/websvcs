
package com.oracle.retail.integration.base.bo.fulfilordref.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;


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
 *         &lt;element name="source_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1}source_loc_type" minOccurs="0"/&gt;
 *         &lt;element name="source_loc_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="fulfill_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1}fulfill_loc_type"/&gt;
 *         &lt;element name="fulfill_loc_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtlRef/v1}FulfilOrdDtlRef" maxOccurs="unbounded"/&gt;
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
    "sourceLocType",
    "sourceLocId",
    "fulfillLocType",
    "fulfillLocId",
    "fulfilOrdDtlRef"
})
@XmlRootElement(name = "FulfilOrdRef")
public class FulfilOrdRef {

    @XmlElement(name = "customer_order_no", required = true)
    protected String customerOrderNo;
    @XmlElement(name = "fulfill_order_no", required = true)
    protected String fulfillOrderNo;
    @XmlElement(name = "source_loc_type")
    @XmlSchemaType(name = "string")
    protected SourceLocType sourceLocType;
    @XmlElement(name = "source_loc_id")
    protected Long sourceLocId;
    @XmlElement(name = "fulfill_loc_type", required = true)
    @XmlSchemaType(name = "string")
    protected FulfillLocType fulfillLocType;
    @XmlElement(name = "fulfill_loc_id")
    protected long fulfillLocId;
    @XmlElement(name = "FulfilOrdDtlRef", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtlRef/v1", required = true)
    protected List<FulfilOrdDtlRef> fulfilOrdDtlRef;

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
     * Gets the value of the sourceLocType property.
     * 
     * @return
     *     possible object is
     *     {@link SourceLocType }
     *     
     */
    public SourceLocType getSourceLocType() {
        return sourceLocType;
    }

    /**
     * Sets the value of the sourceLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceLocType }
     *     
     */
    public void setSourceLocType(SourceLocType value) {
        this.sourceLocType = value;
    }

    /**
     * Gets the value of the sourceLocId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSourceLocId() {
        return sourceLocId;
    }

    /**
     * Sets the value of the sourceLocId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSourceLocId(Long value) {
        this.sourceLocId = value;
    }

    /**
     * Gets the value of the fulfillLocType property.
     * 
     * @return
     *     possible object is
     *     {@link FulfillLocType }
     *     
     */
    public FulfillLocType getFulfillLocType() {
        return fulfillLocType;
    }

    /**
     * Sets the value of the fulfillLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FulfillLocType }
     *     
     */
    public void setFulfillLocType(FulfillLocType value) {
        this.fulfillLocType = value;
    }

    /**
     * Gets the value of the fulfillLocId property.
     * 
     */
    public long getFulfillLocId() {
        return fulfillLocId;
    }

    /**
     * Sets the value of the fulfillLocId property.
     * 
     */
    public void setFulfillLocId(long value) {
        this.fulfillLocId = value;
    }

    /**
     * Gets the value of the fulfilOrdDtlRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fulfilOrdDtlRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFulfilOrdDtlRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FulfilOrdDtlRef }
     * 
     * 
     */
    public List<FulfilOrdDtlRef> getFulfilOrdDtlRef() {
        if (fulfilOrdDtlRef == null) {
            fulfilOrdDtlRef = new ArrayList<FulfilOrdDtlRef>();
        }
        return this.fulfilOrdDtlRef;
    }

}
