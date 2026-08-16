
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.bolmtvcoldesc.v1.BolMtvColDesc;


/**
 * <p>Java class for lookupBillOfLadingMotivesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupBillOfLadingMotivesResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/BolMtvColDesc/v1}BolMtvColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupBillOfLadingMotivesResponse", propOrder = {
    "bolMtvColDesc"
})
public class LookupBillOfLadingMotivesResponse {

    @XmlElement(name = "BolMtvColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/BolMtvColDesc/v1")
    protected BolMtvColDesc bolMtvColDesc;

    /**
     * Gets the value of the bolMtvColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link BolMtvColDesc }
     *     
     */
    public BolMtvColDesc getBolMtvColDesc() {
        return bolMtvColDesc;
    }

    /**
     * Sets the value of the bolMtvColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link BolMtvColDesc }
     *     
     */
    public void setBolMtvColDesc(BolMtvColDesc value) {
        this.bolMtvColDesc = value;
    }

}
