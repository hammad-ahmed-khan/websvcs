
package com.oracle.retail.integration.base.bo.forphdrcoldesc.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.forphdrdesc.v1.ForpHdrDesc;


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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpHdrDesc/v1}ForpHdrDesc" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "forpHdrDesc",
    "collectionSize"
})
@XmlRootElement(name = "ForpHdrColDesc")
public class ForpHdrColDesc {

    @XmlElement(name = "ForpHdrDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/ForpHdrDesc/v1")
    protected List<ForpHdrDesc> forpHdrDesc;
    @XmlElement(name = "collection_size")
    protected int collectionSize;

    /**
     * Gets the value of the forpHdrDesc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the forpHdrDesc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getForpHdrDesc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ForpHdrDesc }
     * 
     * 
     */
    public List<ForpHdrDesc> getForpHdrDesc() {
        if (forpHdrDesc == null) {
            forpHdrDesc = new ArrayList<ForpHdrDesc>();
        }
        return this.forpHdrDesc;
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
