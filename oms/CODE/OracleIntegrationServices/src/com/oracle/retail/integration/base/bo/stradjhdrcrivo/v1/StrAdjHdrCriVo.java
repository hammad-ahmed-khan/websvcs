
package com.oracle.retail.integration.base.bo.stradjhdrcrivo.v1;

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
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="from_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="to_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="adjustment_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="reason_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StrAdjHdrCriVo/v1}str_adj_status"/>
 *         &lt;element name="user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nonsellable_type_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="template_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="search_limit" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "storeId",
    "fromDate",
    "toDate",
    "itemId",
    "adjustmentId",
    "reasonId",
    "status",
    "userName",
    "nonsellableTypeId",
    "templateId",
    "searchLimit"
})
@XmlRootElement(name = "StrAdjHdrCriVo")
public class StrAdjHdrCriVo {

    @XmlElement(name = "store_id")
    protected long storeId;
    @XmlElement(name = "from_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fromDate;
    @XmlElement(name = "to_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar toDate;
    @XmlElement(name = "item_id")
    protected String itemId;
    @XmlElement(name = "adjustment_id")
    protected Long adjustmentId;
    @XmlElement(name = "reason_id")
    protected Long reasonId;
    @XmlElement(required = true)
    protected StrAdjStatus status;
    @XmlElement(name = "user_name")
    protected String userName;
    @XmlElement(name = "nonsellable_type_id")
    protected Long nonsellableTypeId;
    @XmlElement(name = "template_id")
    protected Long templateId;
    @XmlElement(name = "search_limit")
    protected int searchLimit;

    /**
     * Gets the value of the storeId property.
     * 
     */
    public long getStoreId() {
        return storeId;
    }

    /**
     * Sets the value of the storeId property.
     * 
     */
    public void setStoreId(long value) {
        this.storeId = value;
    }

    /**
     * Gets the value of the fromDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFromDate() {
        return fromDate;
    }

    /**
     * Sets the value of the fromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFromDate(XMLGregorianCalendar value) {
        this.fromDate = value;
    }

    /**
     * Gets the value of the toDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getToDate() {
        return toDate;
    }

    /**
     * Sets the value of the toDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setToDate(XMLGregorianCalendar value) {
        this.toDate = value;
    }

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemId(String value) {
        this.itemId = value;
    }

    /**
     * Gets the value of the adjustmentId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAdjustmentId() {
        return adjustmentId;
    }

    /**
     * Sets the value of the adjustmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAdjustmentId(Long value) {
        this.adjustmentId = value;
    }

    /**
     * Gets the value of the reasonId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReasonId() {
        return reasonId;
    }

    /**
     * Sets the value of the reasonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReasonId(Long value) {
        this.reasonId = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.stradjhdrcrivo.v1.StrAdjStatus}
     *
     */
    public StrAdjStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.stradjhdrcrivo.v1.StrAdjStatus}
     *
     */
    public void setStatus(StrAdjStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the nonsellableTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNonsellableTypeId() {
        return nonsellableTypeId;
    }

    /**
     * Sets the value of the nonsellableTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNonsellableTypeId(Long value) {
        this.nonsellableTypeId = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTemplateId() {
        return templateId;
    }

    /**
     * Sets the value of the templateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTemplateId(Long value) {
        this.templateId = value;
    }

    /**
     * Gets the value of the searchLimit property.
     * 
     */
    public int getSearchLimit() {
        return searchLimit;
    }

    /**
     * Sets the value of the searchLimit property.
     * 
     */
    public void setSearchLimit(int value) {
        this.searchLimit = value;
    }

}
