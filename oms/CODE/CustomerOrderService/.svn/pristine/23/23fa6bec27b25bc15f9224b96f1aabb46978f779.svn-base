
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RMADeleteRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RMADeleteRequest">
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
 *         &lt;element name="rma_del_req_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RMADeleteDetail" type="{http://com.logicinfo.oms/model/}RMADeleteDetail" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RMADeleteRequest", propOrder = {
    "rmaId",
    "rmaDelReqId",
    "rmaDeleteDetail"
})
public class RMADeleteRequest {

    @XmlElement(name = "rma_id")
    protected long rmaId;
    @XmlElement(name = "rma_del_req_id", required = true)
    protected String rmaDelReqId;
    @XmlElement(name = "RMADeleteDetail", required = true)
    protected List<RMADeleteDetail> rmaDeleteDetail;

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
     * Gets the value of the rmaDelReqId property.
     *
     */
    public String getRmaDelReqId() {
        return rmaDelReqId;
    }

    /**
     * Sets the value of the rmaDelReqId property.
     * 
     */
    public void setRmaDelReqId(String value) {
        this.rmaDelReqId = value;
    }

    /**
     * Gets the value of the rmaDeleteDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rmaDeleteDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRMADeleteDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RMADeleteDetail }
     * 
     * 
     */
    public List<RMADeleteDetail> getRMADeleteDetail() {
        if (rmaDeleteDetail == null) {
            rmaDeleteDetail = new ArrayList<RMADeleteDetail>();
        }
        return this.rmaDeleteDetail;
    }

}
