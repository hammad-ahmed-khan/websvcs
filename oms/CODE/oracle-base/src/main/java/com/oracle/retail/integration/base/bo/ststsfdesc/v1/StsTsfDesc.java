
package com.oracle.retail.integration.base.bo.ststsfdesc.v1;

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
 *         &lt;element name="transfer_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="sending_store_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="receiving_store_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="create_store_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="external_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StsTsfDesc/v1}sts_tsf_status"/&gt;
 *         &lt;element name="create_user_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="update_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="approval_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ship_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="receive_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="update_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="approve_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="ship_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="receive_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="request_comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transfer_comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="delivery_slot_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="context_type_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="context_value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfDesc/v1}StsTsfBol" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfDesc/v1}StsTsfItm" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "transferId",
    "sendingStoreId",
    "receivingStoreId",
    "createStoreId",
    "externalId",
    "status",
    "createUserName",
    "updateUserName",
    "approvalUserName",
    "shipUserName",
    "receiveUserName",
    "createDate",
    "updateDate",
    "approveDate",
    "shipDate",
    "receiveDate",
    "requestComments",
    "transferComments",
    "deliverySlotId",
    "contextTypeId",
    "contextValue",
    "stsTsfBol",
    "stsTsfItm"
})
@XmlRootElement(name = "StsTsfDesc")
public class StsTsfDesc {

    @XmlElement(name = "transfer_id")
    protected long transferId;
    @XmlElement(name = "sending_store_id")
    protected long sendingStoreId;
    @XmlElement(name = "receiving_store_id")
    protected long receivingStoreId;
    @XmlElement(name = "create_store_id")
    protected long createStoreId;
    @XmlElement(name = "external_id")
    protected String externalId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected StsTsfStatus status;
    @XmlElement(name = "create_user_name", required = true)
    protected String createUserName;
    @XmlElement(name = "update_user_name")
    protected String updateUserName;
    @XmlElement(name = "approval_user_name")
    protected String approvalUserName;
    @XmlElement(name = "ship_user_name")
    protected String shipUserName;
    @XmlElement(name = "receive_user_name")
    protected String receiveUserName;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "update_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateDate;
    @XmlElement(name = "approve_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar approveDate;
    @XmlElement(name = "ship_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar shipDate;
    @XmlElement(name = "receive_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receiveDate;
    @XmlElement(name = "request_comments")
    protected String requestComments;
    @XmlElement(name = "transfer_comments")
    protected String transferComments;
    @XmlElement(name = "delivery_slot_id")
    protected String deliverySlotId;
    @XmlElement(name = "context_type_id")
    protected String contextTypeId;
    @XmlElement(name = "context_value")
    protected String contextValue;
    @XmlElement(name = "StsTsfBol")
    protected StsTsfBol stsTsfBol;
    @XmlElement(name = "StsTsfItm")
    protected List<StsTsfItm> stsTsfItm;

    /**
     * Gets the value of the transferId property.
     * 
     */
    public long getTransferId() {
        return transferId;
    }

    /**
     * Sets the value of the transferId property.
     * 
     */
    public void setTransferId(long value) {
        this.transferId = value;
    }

    /**
     * Gets the value of the sendingStoreId property.
     * 
     */
    public long getSendingStoreId() {
        return sendingStoreId;
    }

    /**
     * Sets the value of the sendingStoreId property.
     * 
     */
    public void setSendingStoreId(long value) {
        this.sendingStoreId = value;
    }

    /**
     * Gets the value of the receivingStoreId property.
     * 
     */
    public long getReceivingStoreId() {
        return receivingStoreId;
    }

    /**
     * Sets the value of the receivingStoreId property.
     * 
     */
    public void setReceivingStoreId(long value) {
        this.receivingStoreId = value;
    }

    /**
     * Gets the value of the createStoreId property.
     * 
     */
    public long getCreateStoreId() {
        return createStoreId;
    }

    /**
     * Sets the value of the createStoreId property.
     * 
     */
    public void setCreateStoreId(long value) {
        this.createStoreId = value;
    }

    /**
     * Gets the value of the externalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Sets the value of the externalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalId(String value) {
        this.externalId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfStatus }
     *     
     */
    public StsTsfStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfStatus }
     *     
     */
    public void setStatus(StsTsfStatus value) {
        this.status = value;
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
     * Gets the value of the updateUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * Sets the value of the updateUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateUserName(String value) {
        this.updateUserName = value;
    }

    /**
     * Gets the value of the approvalUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprovalUserName() {
        return approvalUserName;
    }

    /**
     * Sets the value of the approvalUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprovalUserName(String value) {
        this.approvalUserName = value;
    }

    /**
     * Gets the value of the shipUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipUserName() {
        return shipUserName;
    }

    /**
     * Sets the value of the shipUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipUserName(String value) {
        this.shipUserName = value;
    }

    /**
     * Gets the value of the receiveUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveUserName() {
        return receiveUserName;
    }

    /**
     * Sets the value of the receiveUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveUserName(String value) {
        this.receiveUserName = value;
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
     * Gets the value of the updateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the value of the updateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateDate(XMLGregorianCalendar value) {
        this.updateDate = value;
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
     * Gets the value of the shipDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getShipDate() {
        return shipDate;
    }

    /**
     * Sets the value of the shipDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setShipDate(XMLGregorianCalendar value) {
        this.shipDate = value;
    }

    /**
     * Gets the value of the receiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReceiveDate() {
        return receiveDate;
    }

    /**
     * Sets the value of the receiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReceiveDate(XMLGregorianCalendar value) {
        this.receiveDate = value;
    }

    /**
     * Gets the value of the requestComments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestComments() {
        return requestComments;
    }

    /**
     * Sets the value of the requestComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestComments(String value) {
        this.requestComments = value;
    }

    /**
     * Gets the value of the transferComments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferComments() {
        return transferComments;
    }

    /**
     * Sets the value of the transferComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferComments(String value) {
        this.transferComments = value;
    }

    /**
     * Gets the value of the deliverySlotId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverySlotId() {
        return deliverySlotId;
    }

    /**
     * Sets the value of the deliverySlotId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverySlotId(String value) {
        this.deliverySlotId = value;
    }

    /**
     * Gets the value of the contextTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextTypeId() {
        return contextTypeId;
    }

    /**
     * Sets the value of the contextTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextTypeId(String value) {
        this.contextTypeId = value;
    }

    /**
     * Gets the value of the contextValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextValue() {
        return contextValue;
    }

    /**
     * Sets the value of the contextValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextValue(String value) {
        this.contextValue = value;
    }

    /**
     * Gets the value of the stsTsfBol property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfBol }
     *     
     */
    public StsTsfBol getStsTsfBol() {
        return stsTsfBol;
    }

    /**
     * Sets the value of the stsTsfBol property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfBol }
     *     
     */
    public void setStsTsfBol(StsTsfBol value) {
        this.stsTsfBol = value;
    }

    /**
     * Gets the value of the stsTsfItm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stsTsfItm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStsTsfItm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StsTsfItm }
     * 
     * 
     */
    public List<StsTsfItm> getStsTsfItm() {
        if (stsTsfItm == null) {
            stsTsfItm = new ArrayList<StsTsfItm>();
        }
        return this.stsTsfItm;
    }

}
