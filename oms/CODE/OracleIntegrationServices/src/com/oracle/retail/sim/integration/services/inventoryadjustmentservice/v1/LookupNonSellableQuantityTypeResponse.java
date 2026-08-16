
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.nslqtytypcoldesc.v1.NslQtyTypColDesc;


/**
 * <p>Java class for lookupNonSellableQuantityTypeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupNonSellableQuantityTypeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/NslQtyTypColDesc/v1}NslQtyTypColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupNonSellableQuantityTypeResponse", propOrder = {
    "nslQtyTypColDesc"
})
public class LookupNonSellableQuantityTypeResponse {

    @XmlElement(name = "NslQtyTypColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/NslQtyTypColDesc/v1")
    protected NslQtyTypColDesc nslQtyTypColDesc;

    /**
     * Gets the value of the nslQtyTypColDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.nslqtytypcoldesc.v1.NslQtyTypColDesc}
     *
     */
    public NslQtyTypColDesc getNslQtyTypColDesc() {
        return nslQtyTypColDesc;
    }

    /**
     * Sets the value of the nslQtyTypColDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.nslqtytypcoldesc.v1.NslQtyTypColDesc}
     *
     */
    public void setNslQtyTypColDesc(NslQtyTypColDesc value) {
        this.nslQtyTypColDesc = value;
    }

}
