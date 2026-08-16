
package retail.siebel.com.integration;

import java.math.BigDecimal;
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
 * <p>Java class for ItemLevelDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ItemLevelDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="line_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="link_line_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="shipping_classification">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="substitute_allow_ind" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="backorder_ind">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="backorder_dly_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="qty_ordered_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="standard_uom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="transaction_uom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="unit_retail">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="orig_unit_retail" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="retail_curr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="comments" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfillmentDetails" type="{http://com.siebel.retail/integration/}fulfillmentDetails" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemLevelDetails", propOrder = {
    "item",
    "lineNo",
    "linkLineNo",
    "shippingClassification",
    "substituteAllowInd",
    "backorderInd",
    "backorderDlyDate",
    "qtyOrderedSuom",
    "standardUom",
    "transactionUom",
    "unitRetail",
    "origUnitRetail",
    "retailCurr",
    "comments",
    "fulfillmentDetails"
})
@XmlRootElement
public class ItemLevelDetails {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "line_no")
    protected long lineNo;
    @XmlElement(name = "link_line_no")
    protected Long linkLineNo;
    @XmlElement(name = "shipping_classification", required = true)
    protected String shippingClassification;
    @XmlElement(name = "substitute_allow_ind")
    protected String substituteAllowInd;
    @XmlElement(name = "backorder_ind", required = true)
    protected String backorderInd;
    @XmlElement(name = "backorder_dly_date", nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar backorderDlyDate;
    @XmlElement(name = "qty_ordered_suom", required = true)
    protected BigDecimal qtyOrderedSuom;
    @XmlElement(name = "standard_uom", required = true)
    protected String standardUom;
    @XmlElement(name = "transaction_uom", required = true)
    protected String transactionUom;
    @XmlElement(name = "unit_retail", required = true)
    protected BigDecimal unitRetail;
    @XmlElement(name = "orig_unit_retail", nillable = true)
    protected BigDecimal origUnitRetail;
    @XmlElement(name = "retail_curr")
    protected String retailCurr;
    @XmlElement(nillable = true)
    protected String comments;
    @XmlElement(nillable = true)
    protected List<FulfillmentDetails> fulfillmentDetails;

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem(String value) {
        this.item = value;
    }

    /**
     * Gets the value of the lineNo property.
     * 
     */
    public long getLineNo() {
        return lineNo;
    }

    /**
     * Sets the value of the lineNo property.
     * 
     */
    public void setLineNo(long value) {
        this.lineNo = value;
    }

    /**
     * Gets the value of the linkLineNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLinkLineNo() {
        return linkLineNo;
    }

    /**
     * Sets the value of the linkLineNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLinkLineNo(Long value) {
        this.linkLineNo = value;
    }

    /**
     * Gets the value of the shippingClassification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShippingClassification() {
        return shippingClassification;
    }

    /**
     * Sets the value of the shippingClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShippingClassification(String value) {
        this.shippingClassification = value;
    }

    /**
     * Gets the value of the substituteAllowInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubstituteAllowInd() {
        return substituteAllowInd;
    }

    /**
     * Sets the value of the substituteAllowInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubstituteAllowInd(String value) {
        this.substituteAllowInd = value;
    }

    /**
     * Gets the value of the backorderInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackorderInd() {
        return backorderInd;
    }

    /**
     * Sets the value of the backorderInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackorderInd(String value) {
        this.backorderInd = value;
    }

    /**
     * Gets the value of the backorderDlyDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBackorderDlyDate() {
        return backorderDlyDate;
    }

    /**
     * Sets the value of the backorderDlyDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBackorderDlyDate(XMLGregorianCalendar value) {
        this.backorderDlyDate = value;
    }

    /**
     * Gets the value of the qtyOrderedSuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQtyOrderedSuom() {
        return qtyOrderedSuom;
    }

    /**
     * Sets the value of the qtyOrderedSuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQtyOrderedSuom(BigDecimal value) {
        this.qtyOrderedSuom = value;
    }

    /**
     * Gets the value of the standardUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardUom() {
        return standardUom;
    }

    /**
     * Sets the value of the standardUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardUom(String value) {
        this.standardUom = value;
    }

    /**
     * Gets the value of the transactionUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionUom() {
        return transactionUom;
    }

    /**
     * Sets the value of the transactionUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionUom(String value) {
        this.transactionUom = value;
    }

    /**
     * Gets the value of the unitRetail property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitRetail() {
        return unitRetail;
    }

    /**
     * Sets the value of the unitRetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitRetail(BigDecimal value) {
        this.unitRetail = value;
    }

    /**
     * Gets the value of the origUnitRetail property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrigUnitRetail() {
        return origUnitRetail;
    }

    /**
     * Sets the value of the origUnitRetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrigUnitRetail(BigDecimal value) {
        this.origUnitRetail = value;
    }

    /**
     * Gets the value of the retailCurr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetailCurr() {
        return retailCurr;
    }

    /**
     * Sets the value of the retailCurr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetailCurr(String value) {
        this.retailCurr = value;
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
     * Gets the value of the fulfillmentDetails property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fulfillmentDetails property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getFulfillmentDetails().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .retail.siebel.com.integration.FulfillmentDetails}
     *
     *
     */
    public List<FulfillmentDetails> getFulfillmentDetails() {
        if (fulfillmentDetails == null) {
            fulfillmentDetails = new ArrayList<FulfillmentDetails>();
        }
        return this.fulfillmentDetails;
    }

}
