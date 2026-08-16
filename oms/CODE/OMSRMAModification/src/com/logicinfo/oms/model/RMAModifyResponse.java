
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RMAModifyResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RMAModifyResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rma_mod_req_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RMADModifyDetailResponse" type="{http://com.logicinfo.oms/model/}RMADModifyDetailResponse" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RMAModifyResponse", propOrder = {
    "rmaModReqId",
    "status",
    "messageCode",
    "messageDesc",
    "rmaModifyDetailResponse"
})
public class RMAModifyResponse {

    @XmlElement(name = "rma_mod_req_id", required = true, nillable = true)
    protected String rmaModReqId;
    @XmlElement(required = true, nillable = true)
    protected String status;
    @XmlElement(name = "RMAModifyDetailResponse", nillable = true)
    protected List<RMAModifyDetailResponse> rmaModifyDetailResponse;
    @XmlElement(name = "message_code", required = true, nillable = true)
    protected String messageCode;
    @XmlElement(name = "message_desc", required = true, nillable = true)
    protected String messageDesc;

    /**
     * Gets the value of the rmaModReqId property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public String getRmaModReqId() {
        return rmaModReqId;
    }

    /**
     * Sets the value of the rmaModReqId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRmaModReqId(String value) {
        this.rmaModReqId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the rmaModifyDetailResponse property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rmaModifyDetailResponse property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRMAModifyDetailResponse().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RMAModifyDetailResponse }
     *
     *
     */
    public List<RMAModifyDetailResponse> getRMAModifyDetailResponse() {
        if (rmaModifyDetailResponse == null) {
            rmaModifyDetailResponse = new ArrayList<RMAModifyDetailResponse>();
        }
        return this.rmaModifyDetailResponse;
    }

    /**
     * Gets the value of the messageCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * Gets the value of the messageDesc property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessageDesc() {
        return messageDesc;
    }

    /**
     * Sets the value of the messageCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessageCode(String value) {
        this.messageCode = value;
    }

    /**
     * Sets the value of the messageDesc property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessageDesc(String value) {
        this.messageDesc = value;
    }


}
