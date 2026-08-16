
package com.oracle.retail.oms.integration.services.customerorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.receiptdesc.v1.ReceiptDesc;


/**
 * <p>Java class for updateReceipt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateReceipt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1}ReceiptDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateReceipt", propOrder = {
    "receiptDesc"
})
public class UpdateReceipt {

    @XmlElement(name = "ReceiptDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1")
    protected ReceiptDesc receiptDesc;

    /**
     * Gets the value of the receiptDesc property.
     * 
     * @return
     *     possible object is
     *     {@link ReceiptDesc }
     *     
     */
    public ReceiptDesc getReceiptDesc() {
        return receiptDesc;
    }

    /**
     * Sets the value of the receiptDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReceiptDesc }
     *     
     */
    public void setReceiptDesc(ReceiptDesc value) {
        this.receiptDesc = value;
    }

}
