
package retail.siebel.com.integration;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TenderDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TenderDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tender_seq_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tender_type_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tender_type_group">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tender_amt">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_auth_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_auth_src" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_cardholder_verf" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_exp_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="cc_entry_mode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_term_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_spec_cond" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tender_ref_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TenderDetails", propOrder = {
    "tenderSeqNo",
    "tenderTypeId",
    "tenderTypeGroup",
    "tenderAmt",
    "ccNo",
    "ccAuthNo",
    "ccAuthSrc",
    "ccCardholderVerf",
    "ccExpDate",
    "ccEntryMode",
    "ccTermId",
    "ccSpecCond",
    "tenderRefId"
})
public class TenderDetails {

    @XmlElement(name = "tender_seq_no")
    protected long tenderSeqNo;
    @XmlElement(name = "tender_type_id")
    protected long tenderTypeId;
    @XmlElement(name = "tender_type_group", required = true)
    protected String tenderTypeGroup;
    @XmlElement(name = "tender_amt", required = true)
    protected BigDecimal tenderAmt;
    @XmlElement(name = "cc_no", nillable = true)
    protected String ccNo;
    @XmlElement(name = "cc_auth_no", nillable = true)
    protected String ccAuthNo;
    @XmlElement(name = "cc_auth_src", nillable = true)
    protected String ccAuthSrc;
    @XmlElement(name = "cc_cardholder_verf", nillable = true)
    protected String ccCardholderVerf;
    @XmlElement(name = "cc_exp_date", nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar ccExpDate;
    @XmlElement(name = "cc_entry_mode", nillable = true)
    protected String ccEntryMode;
    @XmlElement(name = "cc_term_id", nillable = true)
    protected String ccTermId;
    @XmlElement(name = "cc_spec_cond", nillable = true)
    protected String ccSpecCond;
    @XmlElement(name = "tender_ref_id", nillable = true)
    protected Long tenderRefId;

    /**
     * Gets the value of the tenderSeqNo property.
     * 
     */
    public long getTenderSeqNo() {
        return tenderSeqNo;
    }

    /**
     * Sets the value of the tenderSeqNo property.
     * 
     */
    public void setTenderSeqNo(long value) {
        this.tenderSeqNo = value;
    }

    /**
     * Gets the value of the tenderTypeId property.
     * 
     */
    public long getTenderTypeId() {
        return tenderTypeId;
    }

    /**
     * Sets the value of the tenderTypeId property.
     * 
     */
    public void setTenderTypeId(long value) {
        this.tenderTypeId = value;
    }

    /**
     * Gets the value of the tenderTypeGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTenderTypeGroup() {
        return tenderTypeGroup;
    }

    /**
     * Sets the value of the tenderTypeGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTenderTypeGroup(String value) {
        this.tenderTypeGroup = value;
    }

    /**
     * Gets the value of the tenderAmt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTenderAmt() {
        return tenderAmt;
    }

    /**
     * Sets the value of the tenderAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTenderAmt(BigDecimal value) {
        this.tenderAmt = value;
    }

    /**
     * Gets the value of the ccNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcNo() {
        return ccNo;
    }

    /**
     * Sets the value of the ccNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcNo(String value) {
        this.ccNo = value;
    }

    /**
     * Gets the value of the ccAuthNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcAuthNo() {
        return ccAuthNo;
    }

    /**
     * Sets the value of the ccAuthNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcAuthNo(String value) {
        this.ccAuthNo = value;
    }

    /**
     * Gets the value of the ccAuthSrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcAuthSrc() {
        return ccAuthSrc;
    }

    /**
     * Sets the value of the ccAuthSrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcAuthSrc(String value) {
        this.ccAuthSrc = value;
    }

    /**
     * Gets the value of the ccCardholderVerf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcCardholderVerf() {
        return ccCardholderVerf;
    }

    /**
     * Sets the value of the ccCardholderVerf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcCardholderVerf(String value) {
        this.ccCardholderVerf = value;
    }

    /**
     * Gets the value of the ccExpDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCcExpDate() {
        return ccExpDate;
    }

    /**
     * Sets the value of the ccExpDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCcExpDate(XMLGregorianCalendar value) {
        this.ccExpDate = value;
    }

    /**
     * Gets the value of the ccEntryMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcEntryMode() {
        return ccEntryMode;
    }

    /**
     * Sets the value of the ccEntryMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcEntryMode(String value) {
        this.ccEntryMode = value;
    }

    /**
     * Gets the value of the ccTermId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcTermId() {
        return ccTermId;
    }

    /**
     * Sets the value of the ccTermId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcTermId(String value) {
        this.ccTermId = value;
    }

    /**
     * Gets the value of the ccSpecCond property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcSpecCond() {
        return ccSpecCond;
    }

    /**
     * Sets the value of the ccSpecCond property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcSpecCond(String value) {
        this.ccSpecCond = value;
    }

    /**
     * Gets the value of the tenderRefId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTenderRefId() {
        return tenderRefId;
    }

    /**
     * Sets the value of the tenderRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTenderRefId(Long value) {
        this.tenderRefId = value;
    }

}
