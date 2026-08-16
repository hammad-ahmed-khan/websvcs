
package com.oracle.retail.integration.base.bo.stradjtplref.v1;

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
 *         &lt;element name="template_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
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
    "templateId"
})
@XmlRootElement(name = "StrAdjTplRef")
public class StrAdjTplRef {

    @XmlElement(name = "template_id")
    protected long templateId;

    /**
     * Gets the value of the templateId property.
     * 
     */
    public long getTemplateId() {
        return templateId;
    }

    /**
     * Sets the value of the templateId property.
     * 
     */
    public void setTemplateId(long value) {
        this.templateId = value;
    }

}
