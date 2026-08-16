
package oms.logicinfo.com.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customerOrderResponseItems complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerOrderResponseItems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="delivery_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="150"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="product_sku">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="150"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="quantity">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="total_value">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="05"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="total_weight">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="05"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerOrderResponseItems", propOrder = {
    "deliveryId",
    "name",
    "lineNo",
    "productSku",
    "quantity",
    "totalValue",
    "retailCurrencyCode",
    "totalWeight",
    "weightUom"
})
public class CustomerOrderResponseItems {

    @XmlElement(name = "delivery_id")
    protected int deliveryId;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "product_sku", required = true)
    protected String productSku;
    @XmlElement(required = true)
    protected int quantity;
    @XmlElement(name = "total_value", required = true, nillable = true)
    protected BigDecimal totalValue;
    @XmlElement(name = "total_weight", required = true, nillable = true)
    protected BigDecimal totalWeight;
    @XmlElement(name = "retail_currency_code", required = true, nillable = true)
    protected String retailCurrencyCode;
    @XmlElement(name = "weight_uom", required = true, nillable = true)
    protected String weightUom;
    @XmlElement(name = "line_no")
    protected long lineNo;

    /**
     * Gets the value of the deliveryId property.
     *
     */
    public int getDeliveryId() {
        return deliveryId;
    }

    /**
     * Sets the value of the deliveryId property.
     * 
     */
    public void setDeliveryId(int value) {
        this.deliveryId = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the productSku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductSku() {
        return productSku;
    }

    /**
     * Sets the value of the productSku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductSku(String value) {
        this.productSku = value;
    }

    /**
     * Gets the value of the quantity property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the totalValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    /**
     * Sets the value of the totalValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalValue(BigDecimal value) {
        this.totalValue = value;
    }

    /**
     * Gets the value of the totalWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    /**
     * Sets the value of the totalWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalWeight(BigDecimal value) {
        this.totalWeight = value;
    }

    /**
     * Gets the value of the retailCurrencyCode property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public String getRetailCurrencyCode() {
        return retailCurrencyCode;
    }

    /**
     * Gets the value of the weightUom property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public String getWeightUom() {
        return weightUom;
    }

    /**
     * Sets the value of the retailCurrencyCode property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setRetailCurrencyCode(String value) {
        this.retailCurrencyCode = value;
    }

    /**
     * Sets the value of the weightUom property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setWeightUom(String value) {
        this.weightUom = value;
    }

    /**
     * Gets the value of the lineNo property.
     *
     */
    public long getLineNo() {
        return lineNo;
    }

    /**
     * Sets the value of the lineNo property.
     *
     */
    public void setLineNo(long value) {
        this.lineNo = value;
    }

}
