
package com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fodhdrdesc.v1.FodHdrDesc;


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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodHdrDesc/v1}FodHdrDesc" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="collection_size" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
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
    "fodHdrDesc",
    "collectionSize"
})
@XmlRootElement(name = "FodHdrColDesc")
public class FodHdrColDesc {

    @XmlElement(name = "FodHdrDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/FodHdrDesc/v1")
    protected List<FodHdrDesc> fodHdrDesc;
    @XmlElement(name = "collection_size")
    protected int collectionSize;

    /**
     * Gets the value of the fodHdrDesc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fodHdrDesc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFodHdrDesc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FodHdrDesc }
     * 
     * 
     */
    public List<FodHdrDesc> getFodHdrDesc() {
        if (fodHdrDesc == null) {
            fodHdrDesc = new ArrayList<FodHdrDesc>();
        }
        return this.fodHdrDesc;
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
