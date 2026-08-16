
package com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1;

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
 *         &lt;element name="other_store_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StsTsfHdrCriVo/v1}StsTsfCriStatus"/>
 *         &lt;element name="transfer_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="external_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="from_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="to_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="context_type_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "otherStoreId",
    "status",
    "transferId",
    "externalId",
    "itemId",
    "fromDate",
    "toDate",
    "contextTypeId"
})
@XmlRootElement(name = "StsTsfHdrCriVo")
public class StsTsfHdrCriVo {

    @XmlElement(name = "store_id")
    protected long storeId;
    @XmlElement(name = "other_store_id")
    protected Long otherStoreId;
    @XmlElement(required = true)
    protected StsTsfCriStatus status;
    @XmlElement(name = "transfer_id")
    protected Long transferId;
    @XmlElement(name = "external_id")
    protected Long externalId;
    @XmlElement(name = "item_id")
    protected String itemId;
    @XmlElement(name = "from_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fromDate;
    @XmlElement(name = "to_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar toDate;
    @XmlElement(name = "context_type_id")
    protected String contextTypeId;

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
     * Gets the value of the otherStoreId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOtherStoreId() {
        return otherStoreId;
    }

    /**
     * Sets the value of the otherStoreId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOtherStoreId(Long value) {
        this.otherStoreId = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfCriStatus}
     *
     */
    public StsTsfCriStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfCriStatus}
     *
     */
    public void setStatus(StsTsfCriStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the transferId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTransferId() {
        return transferId;
    }

    /**
     * Sets the value of the transferId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTransferId(Long value) {
        this.transferId = value;
    }

    /**
     * Gets the value of the externalId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExternalId() {
        return externalId;
    }

    /**
     * Sets the value of the externalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExternalId(Long value) {
        this.externalId = value;
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

}
