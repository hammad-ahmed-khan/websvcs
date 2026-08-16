
package com.oracle.retail.integration.base.bo.taxlinertcolvo.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.taxlinertvo.v1.TaxLineRtVo;


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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/TaxLineRtVo/v1}TaxLineRtVo" maxOccurs="unbounded"/>
 *         &lt;element name="collection_size" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "taxLineRtVo",
    "collectionSize"
})
@XmlRootElement(name = "TaxLineRtColVo")
public class TaxLineRtColVo {

    @XmlElement(name = "TaxLineRtVo", namespace = "http://www.oracle.com/retail/integration/base/bo/TaxLineRtVo/v1", required = true)
    protected List<TaxLineRtVo> taxLineRtVo;
    @XmlElement(name = "collection_size")
    protected int collectionSize;

    /**
     * A collection of tax applied to item units returned
     * 	                           in this transaction for this order line item.Gets the value of the taxLineRtVo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taxLineRtVo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaxLineRtVo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaxLineRtVo }
     * 
     * 
     */
    public List<TaxLineRtVo> getTaxLineRtVo() {
        if (taxLineRtVo == null) {
            taxLineRtVo = new ArrayList<TaxLineRtVo>();
        }
        return this.taxLineRtVo;
    }

    /**
     * Gets the value of the collectionSize property.
     * 
     */
    public int getCollectionSize() {
        return collectionSize;
    }

    /**
     * Sets the value of the collectionSize property.
     * 
     */
    public void setCollectionSize(int value) {
        this.collectionSize = value;
    }

}
