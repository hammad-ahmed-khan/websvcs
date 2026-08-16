
package com.oracle.retail.integration.base.bo.ststsfhdrdesc.v1;

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
 *         &lt;element name="transfer_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="external_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sending_store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="receiving_store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StsTsfHdrDesc/v1}StsTsfStatus"/>
 *         &lt;element name="status_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="create_user_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="approval_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receive_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="number_of_line_items" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "transferId",
    "externalId",
    "sendingStoreId",
    "receivingStoreId",
    "status",
    "statusDate",
    "createUserName",
    "approvalUserName",
    "receiveUserName",
    "numberOfLineItems"
})
@XmlRootElement(name = "StsTsfHdrDesc")
public class StsTsfHdrDesc {

    @XmlElement(name = "transfer_id")
    protected long transferId;
    @XmlElement(name = "external_id")
    protected String externalId;
    @XmlElement(name = "sending_store_id")
    protected long sendingStoreId;
    @XmlElement(name = "receiving_store_id")
    protected long receivingStoreId;
    @XmlElement(required = true)
    protected StsTsfStatus status;
    @XmlElement(name = "status_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar statusDate;
    @XmlElement(name = "create_user_name", required = true)
    protected String createUserName;
    @XmlElement(name = "approval_user_name")
    protected String approvalUserName;
    @XmlElement(name = "receive_user_name")
    protected String receiveUserName;
    @XmlElement(name = "number_of_line_items")
    protected long numberOfLineItems;

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
     * Gets the value of the status property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfhdrdesc.v1.StsTsfStatus}
     *
     */
    public StsTsfStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfhdrdesc.v1.StsTsfStatus}
     *
     */
    public void setStatus(StsTsfStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStatusDate() {
        return statusDate;
    }

    /**
     * Sets the value of the statusDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStatusDate(XMLGregorianCalendar value) {
        this.statusDate = value;
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
     * Gets the value of the numberOfLineItems property.
     * 
     */
    public long getNumberOfLineItems() {
        return numberOfLineItems;
    }

    /**
     * Sets the value of the numberOfLineItems property.
     * 
     */
    public void setNumberOfLineItems(long value) {
        this.numberOfLineItems = value;
    }

}
