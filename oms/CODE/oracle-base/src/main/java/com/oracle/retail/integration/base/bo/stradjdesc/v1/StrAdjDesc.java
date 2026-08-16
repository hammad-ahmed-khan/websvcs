
package com.oracle.retail.integration.base.bo.stradjdesc.v1;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adjustment_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="reference_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="template_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StrAdjDesc/v1}str_adj_status"/&gt;
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="create_user_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="approve_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="approve_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjDesc/v1}StrAdjItm" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "storeId",
    "referenceId",
    "templateId",
    "status",
    "comments",
    "createDate",
    "createUserName",
    "approveDate",
    "approveUserName",
    "strAdjItm"
})
@XmlRootElement(name = "StrAdjDesc")
public class StrAdjDesc {

    @XmlElement(name = "adjustment_id")
    protected long adjustmentId;
    @XmlElement(name = "store_id")
    protected long storeId;
    @XmlElement(name = "reference_id")
    protected Long referenceId;
    @XmlElement(name = "template_id")
    protected Long templateId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StrAdjStatus status;
    protected String comments;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "create_user_name", required = true)
    protected String createUserName;
    @XmlElement(name = "approve_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar approveDate;
    @XmlElement(name = "approve_user_name")
    protected String approveUserName;
    @XmlElement(name = "StrAdjItm")
    protected List<StrAdjItm> strAdjItm;

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
     * Gets the value of the referenceId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the value of the referenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReferenceId(Long value) {
        this.referenceId = value;
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
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
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
     * Gets the value of the approveDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getApproveDate() {
        return approveDate;
    }

    /**
     * Sets the value of the approveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setApproveDate(XMLGregorianCalendar value) {
        this.approveDate = value;
    }

    /**
     * Gets the value of the approveUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApproveUserName() {
        return approveUserName;
    }

    /**
     * Sets the value of the approveUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApproveUserName(String value) {
        this.approveUserName = value;
    }

    /**
     * Gets the value of the strAdjItm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the strAdjItm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStrAdjItm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StrAdjItm }
     * 
     * 
     */
    public List<StrAdjItm> getStrAdjItm() {
        if (strAdjItm == null) {
            strAdjItm = new ArrayList<StrAdjItm>();
        }
        return this.strAdjItm;
    }

}
