
package retail.siebel.com.integration;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SiebelStatusUpdateDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SiebelStatusUpdateDetails">
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
 *         &lt;element name="qty">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="event_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="event_comments">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="oms_dlv_conf_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="oms_cancel_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="source_loc_type" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="source_loc" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_loc_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_loc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="update_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiebelStatusUpdateDetails", propOrder = {
    "item",
    "lineNo",
    "qty",
    "eventId",
    "eventComments",
    "omsDlvConfId",
    "omsCancelId",
    "sourceLocType",
    "sourceLoc",
    "fulfillLocType",
    "fulfillLoc",
    "updateDatetime"
})
public class SiebelStatusUpdateDetails {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "line_no")
    protected long lineNo;
    @XmlElement(required = true)
    protected BigDecimal qty;
    @XmlElement(name = "event_id", required = true)
    protected String eventId;
    @XmlElement(name = "event_comments", required = true, nillable = true)
    protected String eventComments;
    @XmlElement(name = "oms_dlv_conf_id", nillable = true)
    protected Long omsDlvConfId;
    @XmlElement(name = "oms_cancel_id", nillable = true)
    protected Long omsCancelId;
    @XmlElement(name = "source_loc_type", nillable = true)
    protected String sourceLocType;
    @XmlElement(name = "source_loc", nillable = true)
    protected Long sourceLoc;
    @XmlElement(name = "fulfill_loc_type", required = true)
    protected String fulfillLocType;
    @XmlElement(name = "fulfill_loc")
    protected long fulfillLoc;
    @XmlElement(name = "update_datetime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateDatetime;

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
     * Gets the value of the qty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * Sets the value of the qty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQty(BigDecimal value) {
        this.qty = value;
    }

    /**
     * Gets the value of the eventId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the value of the eventId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventId(String value) {
        this.eventId = value;
    }

    /**
     * Gets the value of the eventComments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventComments() {
        return eventComments;
    }

    /**
     * Sets the value of the eventComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventComments(String value) {
        this.eventComments = value;
    }

    /**
     * Gets the value of the omsDlvConfId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOmsDlvConfId() {
        return omsDlvConfId;
    }

    /**
     * Sets the value of the omsDlvConfId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOmsDlvConfId(Long value) {
        this.omsDlvConfId = value;
    }

    /**
     * Gets the value of the omsCancelId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOmsCancelId() {
        return omsCancelId;
    }

    /**
     * Sets the value of the omsCancelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOmsCancelId(Long value) {
        this.omsCancelId = value;
    }

    /**
     * Gets the value of the sourceLocType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceLocType() {
        return sourceLocType;
    }

    /**
     * Sets the value of the sourceLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceLocType(String value) {
        this.sourceLocType = value;
    }

    /**
     * Gets the value of the sourceLoc property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSourceLoc() {
        return sourceLoc;
    }

    /**
     * Sets the value of the sourceLoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSourceLoc(Long value) {
        this.sourceLoc = value;
    }

    /**
     * Gets the value of the fulfillLocType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillLocType() {
        return fulfillLocType;
    }

    /**
     * Sets the value of the fulfillLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillLocType(String value) {
        this.fulfillLocType = value;
    }

    /**
     * Gets the value of the fulfillLoc property.
     * 
     */
    public long getFulfillLoc() {
        return fulfillLoc;
    }

    /**
     * Sets the value of the fulfillLoc property.
     * 
     */
    public void setFulfillLoc(long value) {
        this.fulfillLoc = value;
    }

    /**
     * Gets the value of the updateDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateDatetime() {
        return updateDatetime;
    }

    /**
     * Sets the value of the updateDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateDatetime(XMLGregorianCalendar value) {
        this.updateDatetime = value;
    }

}
