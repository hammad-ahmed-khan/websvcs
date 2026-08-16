
package com.oracle.retail.integration.base.bo.postrndesc.v1;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="transaction_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transaction_timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="cust_order_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cust_order_comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1}PosTrnItm" maxOccurs="unbounded"/>
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
    "transactionId",
    "transactionTimestamp",
    "custOrderId",
    "custOrderComment",
    "posTrnItm"
})
@XmlRootElement(name = "PosTrnDesc")
public class PosTrnDesc {

    @XmlElement(name = "store_id")
    protected long storeId;
    @XmlElement(name = "transaction_id", required = true)
    protected String transactionId;
    @XmlElement(name = "transaction_timestamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionTimestamp;
    @XmlElement(name = "cust_order_id")
    protected String custOrderId;
    @XmlElement(name = "cust_order_comment")
    protected String custOrderComment;
    @XmlElement(name = "PosTrnItm", required = true)
    protected List<PosTrnItm> posTrnItm;

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
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the transactionTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTransactionTimestamp() {
        return transactionTimestamp;
    }

    /**
     * Sets the value of the transactionTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTransactionTimestamp(XMLGregorianCalendar value) {
        this.transactionTimestamp = value;
    }

    /**
     * Gets the value of the custOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustOrderId() {
        return custOrderId;
    }

    /**
     * Sets the value of the custOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustOrderId(String value) {
        this.custOrderId = value;
    }

    /**
     * Gets the value of the custOrderComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustOrderComment() {
        return custOrderComment;
    }

    /**
     * Sets the value of the custOrderComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustOrderComment(String value) {
        this.custOrderComment = value;
    }

    /**
     * Gets the value of the posTrnItm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the posTrnItm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPosTrnItm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PosTrnItm }
     * 
     * 
     */
    public List<PosTrnItm> getPosTrnItm() {
        if (posTrnItm == null) {
            posTrnItm = new ArrayList<PosTrnItm>();
        }
        return this.posTrnItm;
    }

}
