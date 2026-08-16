
package com.oracle.retail.integration.base.bo.forpdesc.v1;

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
 *         &lt;element name="reverse_pick_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/ForpDesc/v1}forp_status"/&gt;
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="create_user_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpDesc/v1}ForpItm" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "createDate",
    "createUserName",
    "forpItm"
})
@XmlRootElement(name = "ForpDesc")
public class ForpDesc {

    @XmlElement(name = "reverse_pick_id")
    protected long reversePickId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ForpStatus status;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "create_user_name", required = true)
    protected String createUserName;
    @XmlElement(name = "ForpItm")
    protected List<ForpItm> forpItm;

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
     * Gets the value of the forpItm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the forpItm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getForpItm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ForpItm }
     * 
     * 
     */
    public List<ForpItm> getForpItm() {
        if (forpItm == null) {
            forpItm = new ArrayList<ForpItm>();
        }
        return this.forpItm;
    }

}
