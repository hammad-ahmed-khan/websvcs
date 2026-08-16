
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RMADeleteResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RMADeleteResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rma_del_req_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;maxLength value="10"/>
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
 *         &lt;element name="RMADeleteDetailResponse" type="{http://com.logicinfo.oms/model/}RMADeleteDetailResponse" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RMADeleteResponse", propOrder = {
    "rmaDelReqId",
    "messageStatus",
    "rmaDeleteDetailResponse"
})
public class RMADeleteResponse {

    @XmlElement(name = "rma_del_req_id", required = true, nillable = true)
    protected String rmaDelReqId;
    @XmlElement(name = "RMADeleteDetailResponse", nillable = true)
    protected List<RMADeleteDetailResponse> rmaDeleteDetailResponse;
    @XmlElement(name = "message_status", required = true, nillable = true)
    protected String messageStatus;

    /**
     * Gets the value of the rmaDelReqId property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public String getRmaDelReqId() {
        return rmaDelReqId;
    }

    /**
     * Sets the value of the rmaDelReqId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRmaDelReqId(String value) {
        this.rmaDelReqId = value;
    }


    /**
     * Gets the value of the rmaDeleteDetailResponse property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rmaDeleteDetailResponse property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRMADeleteDetailResponse().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RMADeleteDetailResponse }
     *
     *
     */
    public List<RMADeleteDetailResponse> getRMADeleteDetailResponse() {
        if (rmaDeleteDetailResponse == null) {
            rmaDeleteDetailResponse = new ArrayList<RMADeleteDetailResponse>();
        }
        return this.rmaDeleteDetailResponse;
    }

    /**
     * Gets the value of the messageStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessageStatus() {
        return messageStatus;
    }

    /**
     * Sets the value of the messageStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessageStatus(String value) {
        this.messageStatus = value;
    }

}
