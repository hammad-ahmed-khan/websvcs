
package com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="transfer_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="sending_store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="receiving_store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="external_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="context_type_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="context_value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfShpModVo/v1}StsTsfBolMod" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfShpModVo/v1}StsTsfShpItmMod" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="removed_line_id_col" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
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
    "sendingStoreId",
    "receivingStoreId",
    "externalId",
    "comments",
    "contextTypeId",
    "contextValue",
    "stsTsfBolMod",
    "stsTsfShpItmMod",
    "removedLineIdCol"
})
@XmlRootElement(name = "StsTsfShpModVo")
public class StsTsfShpModVo {

    @XmlElement(name = "transfer_id")
    protected Long transferId;
    @XmlElement(name = "sending_store_id")
    protected long sendingStoreId;
    @XmlElement(name = "receiving_store_id")
    protected long receivingStoreId;
    @XmlElement(name = "external_id")
    protected String externalId;
    protected String comments;
    @XmlElement(name = "context_type_id")
    protected String contextTypeId;
    @XmlElement(name = "context_value")
    protected String contextValue;
    @XmlElement(name = "StsTsfBolMod")
    protected StsTsfBolMod stsTsfBolMod;
    @XmlElement(name = "StsTsfShpItmMod")
    protected List<StsTsfShpItmMod> stsTsfShpItmMod;
    @XmlElement(name = "removed_line_id_col", type = Long.class)
    protected List<Long> removedLineIdCol;

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
     * Gets the value of the stsTsfBolMod property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1.StsTsfBolMod}
     *
     */
    public StsTsfBolMod getStsTsfBolMod() {
        return stsTsfBolMod;
    }

    /**
     * Sets the value of the stsTsfBolMod property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1.StsTsfBolMod}
     *
     */
    public void setStsTsfBolMod(StsTsfBolMod value) {
        this.stsTsfBolMod = value;
    }

    /**
     * Gets the value of the stsTsfShpItmMod property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stsTsfShpItmMod property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getStsTsfShpItmMod().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1.StsTsfShpItmMod}
     *
     *
     */
    public List<StsTsfShpItmMod> getStsTsfShpItmMod() {
        if (stsTsfShpItmMod == null) {
            stsTsfShpItmMod = new ArrayList<StsTsfShpItmMod>();
        }
        return this.stsTsfShpItmMod;
    }

    /**
     * Gets the value of the removedLineIdCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the removedLineIdCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemovedLineIdCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getRemovedLineIdCol() {
        if (removedLineIdCol == null) {
            removedLineIdCol = new ArrayList<Long>();
        }
        return this.removedLineIdCol;
    }

}
