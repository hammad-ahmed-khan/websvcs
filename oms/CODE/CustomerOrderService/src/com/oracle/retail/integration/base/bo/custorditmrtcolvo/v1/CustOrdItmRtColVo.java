
package com.oracle.retail.integration.base.bo.custorditmrtcolvo.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custorditmrtvo.v1.CustOrdItmRtVo;


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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmRtVo/v1}CustOrdItmRtVo" maxOccurs="unbounded"/>
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
    "custOrdItmRtVo",
    "collectionSize"
})
@XmlRootElement(name = "CustOrdItmRtColVo")
public class CustOrdItmRtColVo {

    @XmlElement(name = "CustOrdItmRtVo", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdItmRtVo/v1", required = true)
    protected List<CustOrdItmRtVo> custOrdItmRtVo;
    @XmlElement(name = "collection_size")
    protected int collectionSize;

    /**
     * A collection of order item return value object.Gets the value of the custOrdItmRtVo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the custOrdItmRtVo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustOrdItmRtVo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustOrdItmRtVo }
     * 
     * 
     */
    public List<CustOrdItmRtVo> getCustOrdItmRtVo() {
        if (custOrdItmRtVo == null) {
            custOrdItmRtVo = new ArrayList<CustOrdItmRtVo>();
        }
        return this.custOrdItmRtVo;
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
