
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;


/**
 * <p>Java class for createFulfillmentOrderDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createFulfillmentOrderDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdCfmCol/v1}FulfilOrdCfmCol" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createFulfillmentOrderDetailResponse", propOrder = {
    "fulfilOrdCfmCol"
})
public class CreateFulfillmentOrderDetailResponse {

    @XmlElement(name = "FulfilOrdCfmCol", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdCfmCol/v1")
    protected FulfilOrdCfmCol fulfilOrdCfmCol;

    /**
     * Gets the value of the fulfilOrdCfmCol property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol}
     *
     */
    public FulfilOrdCfmCol getFulfilOrdCfmCol() {
        return fulfilOrdCfmCol;
    }

    /**
     * Sets the value of the fulfilOrdCfmCol property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol}
     *
     */
    public void setFulfilOrdCfmCol(FulfilOrdCfmCol value) {
        this.fulfilOrdCfmCol = value;
    }

}
