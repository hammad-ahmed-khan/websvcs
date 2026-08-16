
package com.oracle.retail.integration.base.bo.strinvcrivo.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="item_id_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="store_id_col" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded"/>
 *         &lt;element name="uom_type" type="{http://www.oracle.com/retail/integration/base/bo/StrInvCriVo/v1}StrInvUomType"/>
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
    "itemIdCol",
    "storeIdCol",
    "uomType"
})
@XmlRootElement(name = "StrInvCriVo")
public class StrInvCriVo {

    @XmlElement(name = "item_id_col", required = true)
    protected List<String> itemIdCol;
    @XmlElement(name = "store_id_col", type = Long.class)
    protected List<Long> storeIdCol;
    @XmlElement(name = "uom_type", required = true)
    protected StrInvUomType uomType;

    /**
     * Gets the value of the itemIdCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemIdCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemIdCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getItemIdCol() {
        if (itemIdCol == null) {
            itemIdCol = new ArrayList<String>();
        }
        return this.itemIdCol;
    }

    /**
     * Gets the value of the storeIdCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the storeIdCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStoreIdCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getStoreIdCol() {
        if (storeIdCol == null) {
            storeIdCol = new ArrayList<Long>();
        }
        return this.storeIdCol;
    }

    /**
     * Gets the value of the uomType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvUomType}
     *
     */
    public StrInvUomType getUomType() {
        return uomType;
    }

    /**
     * Sets the value of the uomType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvUomType}
     *
     */
    public void setUomType(StrInvUomType value) {
        this.uomType = value;
    }

}
