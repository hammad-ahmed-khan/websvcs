
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RMAModifyRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RMAModifyRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rma_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="rma_mod_req_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RMAModifyDetail" type="{http://com.logicinfo.oms/model/}RMAModifyDetail" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RMAModifyRequest", propOrder = {
    "rmaId",
    "rmaModReqId",
    "rmaModifyDetail"
})
public class RMAModifyRequest {

    @XmlElement(name = "rma_id")
    protected long rmaId;
    @XmlElement(name = "rma_mod_req_id", required = true)
    protected String rmaModReqId;
    @XmlElement(name = "RMAModifyDetail", required = true)
    protected List<RMAModifyDetail> rmaModifyDetail;

    /**
     * Gets the value of the rmaId property.
     * 
     */
    public long getRmaId() {
        return rmaId;
    }

    /**
     * Sets the value of the rmaId property.
     * 
     */
    public void setRmaId(long value) {
        this.rmaId = value;
    }

    /**
     * Gets the value of the rmaModReqId property.
     *
     */
    public String getRmaModReqId() {
        return rmaModReqId;
    }

    /**
     * Sets the value of the rmaModReqId property.
     * 
     */
    public void setRmaModReqId(String value) {
        this.rmaModReqId = value;
    }

    /**
     * Gets the value of the rmaModifyDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rmaModifyDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRMAModifyDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RMAModifyDetail }
     * 
     * 
     */
    public List<RMAModifyDetail> getRMAModifyDetail() {
        if (rmaModifyDetail == null) {
            rmaModifyDetail = new ArrayList<RMAModifyDetail>();
        }
        return this.rmaModifyDetail;
    }

}
