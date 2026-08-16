
package com.oracle.retail.integration.services.exception.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="shortErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/services/exception/v1}BusinessProblemDetail" maxOccurs="unbounded" minOccurs="0"/>
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
    "shortErrorMessage",
    "errorDescription",
    "businessProblemDetail"
})
public class IllegalStateWSFaultException {

    @XmlElement(required = true)
    protected String shortErrorMessage;
    protected String errorDescription;
    @XmlElement(name = "BusinessProblemDetail")
    protected List<BusinessProblemDetail> businessProblemDetail;

    /**
     * Gets the value of the shortErrorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortErrorMessage() {
        return shortErrorMessage;
    }

    /**
     * Sets the value of the shortErrorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortErrorMessage(String value) {
        this.shortErrorMessage = value;
    }

    /**
     * Gets the value of the errorDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Sets the value of the errorDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorDescription(String value) {
        this.errorDescription = value;
    }

    /**
     * Gets the value of the businessProblemDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the businessProblemDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusinessProblemDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BusinessProblemDetail }
     * 
     * 
     */
    public List<BusinessProblemDetail> getBusinessProblemDetail() {
        if (businessProblemDetail == null) {
            businessProblemDetail = new ArrayList<BusinessProblemDetail>();
        }
        return this.businessProblemDetail;
    }

}
