
package com.oracle.retail.integration.base.bo.stradjhdrdesc.v1;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adjustment_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StrAdjHdrDesc/v1}str_adj_status"/&gt;
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="create_user_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="template_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="item_count" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
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
    "adjustmentId",
    "status",
    "createDate",
    "createUserName",
    "templateId",
    "itemCount"
})
@XmlRootElement(name = "StrAdjHdrDesc")
public class StrAdjHdrDesc {

    @XmlElement(name = "adjustment_id")
    protected long adjustmentId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StrAdjStatus status;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "create_user_name", required = true)
    protected String createUserName;
    @XmlElement(name = "template_id")
    protected Long templateId;
    @XmlElement(name = "item_count")
    protected long itemCount;

    /**
     * Gets the value of the adjustmentId property.
     * 
     */
    public long getAdjustmentId() {
        return adjustmentId;
    }

    /**
     * Sets the value of the adjustmentId property.
     * 
     */
    public void setAdjustmentId(long value) {
        this.adjustmentId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StrAdjStatus }
     *     
     */
    public StrAdjStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrAdjStatus }
     *     
     */
    public void setStatus(StrAdjStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the createUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * Sets the value of the createUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateUserName(String value) {
        this.createUserName = value;
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
     * Gets the value of the itemCount property.
     * 
     */
    public long getItemCount() {
        return itemCount;
    }

    /**
     * Sets the value of the itemCount property.
     * 
     */
    public void setItemCount(long value) {
        this.itemCount = value;
    }

}
