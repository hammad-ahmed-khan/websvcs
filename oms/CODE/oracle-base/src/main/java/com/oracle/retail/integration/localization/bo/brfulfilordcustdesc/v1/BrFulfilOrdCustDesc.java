
package com.oracle.retail.integration.localization.bo.brfulfilordcustdesc.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.custom.bo.eofbrfulfilordcustdesc.v1.EOfBrFulfilOrdCustDesc;


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
 *         &lt;element name="cpf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="neighborhood" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tax_exempt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/custom/bo/EOfBrFulfilOrdCustDesc/v1}EOfBrFulfilOrdCustDesc" minOccurs="0"/&gt;
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
    "cpf",
    "neighborhood",
    "taxExempt",
    "eOfBrFulfilOrdCustDesc"
})
@XmlRootElement(name = "BrFulfilOrdCustDesc")
public class BrFulfilOrdCustDesc {

    protected String cpf;
    protected String neighborhood;
    @XmlElement(name = "tax_exempt")
    protected String taxExempt;
    @XmlElement(name = "EOfBrFulfilOrdCustDesc", namespace = "http://www.oracle.com/retail/integration/custom/bo/EOfBrFulfilOrdCustDesc/v1")
    protected EOfBrFulfilOrdCustDesc eOfBrFulfilOrdCustDesc;

    /**
     * Gets the value of the cpf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Sets the value of the cpf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpf(String value) {
        this.cpf = value;
    }

    /**
     * Gets the value of the neighborhood property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Sets the value of the neighborhood property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNeighborhood(String value) {
        this.neighborhood = value;
    }

    /**
     * Gets the value of the taxExempt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxExempt() {
        return taxExempt;
    }

    /**
     * Sets the value of the taxExempt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxExempt(String value) {
        this.taxExempt = value;
    }

    /**
     * Gets the value of the eOfBrFulfilOrdCustDesc property.
     * 
     * @return
     *     possible object is
     *     {@link EOfBrFulfilOrdCustDesc }
     *     
     */
    public EOfBrFulfilOrdCustDesc getEOfBrFulfilOrdCustDesc() {
        return eOfBrFulfilOrdCustDesc;
    }

    /**
     * Sets the value of the eOfBrFulfilOrdCustDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link EOfBrFulfilOrdCustDesc }
     *     
     */
    public void setEOfBrFulfilOrdCustDesc(EOfBrFulfilOrdCustDesc value) {
        this.eOfBrFulfilOrdCustDesc = value;
    }

}
