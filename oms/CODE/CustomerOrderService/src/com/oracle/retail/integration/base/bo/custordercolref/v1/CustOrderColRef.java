
package com.oracle.retail.integration.base.bo.custordercolref.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custorderref.v1.CustOrderRef;


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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1}CustOrderRef" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlRootElement(name = "CustOrderColRef")
@XmlType(name = "", propOrder = {
    "custOrderRef",
    "status",
    "collectionSize"
})
public class CustOrderColRef {

    @XmlElement(name = "CustOrderRef", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1")
    protected List<CustOrderRef> custOrderRef;
    @XmlElement(name = "collection_size")
    protected int collectionSize;
    @XmlElement(required = true)
    protected String status;

    /**
     * A collection of customer order references.Gets the value of the custOrderRef property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the custOrderRef property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustOrderRef().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustOrderRef }
     *
     *
     */
    public List<CustOrderRef> getCustOrderRef() {
        if (custOrderRef == null) {
            custOrderRef = new ArrayList<CustOrderRef>();
        }
        return this.custOrderRef;
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

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

}
