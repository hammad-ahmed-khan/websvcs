
package com.oracle.retail.integration.base.bo.bolmtvdesc.v1;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="motive_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "motiveId",
    "description"
})
@XmlRootElement(name = "BolMtvDesc")
public class BolMtvDesc {

    @XmlElement(name = "motive_id", required = true)
    protected String motiveId;
    @XmlElement(required = true)
    protected String description;

    /**
     * Gets the value of the motiveId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotiveId() {
        return motiveId;
    }

    /**
     * Sets the value of the motiveId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotiveId(String value) {
        this.motiveId = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
