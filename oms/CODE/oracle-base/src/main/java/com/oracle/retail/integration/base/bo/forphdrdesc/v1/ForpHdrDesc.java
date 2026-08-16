
package com.oracle.retail.integration.base.bo.forphdrdesc.v1;

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
 *         &lt;element name="reverse_pick_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/ForpHdrDesc/v1}forp_status"/&gt;
 *         &lt;element name="create_user_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
    "reversePickId",
    "status",
    "createUserName",
    "createDate"
})
@XmlRootElement(name = "ForpHdrDesc")
public class ForpHdrDesc {

    @XmlElement(name = "reverse_pick_id")
    protected long reversePickId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ForpStatus status;
    @XmlElement(name = "create_user_name", required = true)
    protected String createUserName;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;

    /**
     * Gets the value of the reversePickId property.
     * 
     */
    public long getReversePickId() {
        return reversePickId;
    }

    /**
     * Sets the value of the reversePickId property.
     * 
     */
    public void setReversePickId(long value) {
        this.reversePickId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ForpStatus }
     *     
     */
    public ForpStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForpStatus }
     *     
     */
    public void setStatus(ForpStatus value) {
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

}
