
package com.oracle.retail.integration.base.bo.fulfilordcolref.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;


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
 *         &lt;element name="collection_size" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1}FulfilOrdRef" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "collectionSize",
    "fulfilOrdRef"
})
@XmlRootElement(name = "FulfilOrdColRef")
public class FulfilOrdColRef {

    @XmlElement(name = "collection_size")
    protected int collectionSize;
    @XmlElement(name = "FulfilOrdRef", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdRef/v1")
    protected List<FulfilOrdRef> fulfilOrdRef;

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

    /**
     * Gets the value of the fulfilOrdRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fulfilOrdRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFulfilOrdRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FulfilOrdRef }
     * 
     * 
     */
    public List<FulfilOrdRef> getFulfilOrdRef() {
        if (fulfilOrdRef == null) {
            fulfilOrdRef = new ArrayList<FulfilOrdRef>();
        }
        return this.fulfilOrdRef;
    }

}
