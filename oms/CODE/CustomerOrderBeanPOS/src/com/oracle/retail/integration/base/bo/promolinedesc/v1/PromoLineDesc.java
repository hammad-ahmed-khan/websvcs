
package com.oracle.retail.integration.base.bo.promolinedesc.v1;

import java.math.BigDecimal;
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
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="unit_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="promotion_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotion_component_detail_id" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="promotion_component_id" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="promotion_id" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
    "currencyCode",
    "unitDiscountAmount",
    "promotionName",
    "promotionComponentDetailId",
    "promotionComponentId",
    "promotionId"
})
@XmlRootElement(name = "PromoLineDesc")
public class PromoLineDesc {

    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "unit_discount_amount", required = true)
    protected BigDecimal unitDiscountAmount;
    @XmlElement(name = "promotion_name")
    protected String promotionName;
    @XmlElement(name = "promotion_component_detail_id")
    protected BigDecimal promotionComponentDetailId;
    @XmlElement(name = "promotion_component_id")
    protected BigDecimal promotionComponentId;
    @XmlElement(name = "promotion_id")
    protected BigDecimal promotionId;

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the unitDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }

    /**
     * Sets the value of the unitDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitDiscountAmount(BigDecimal value) {
        this.unitDiscountAmount = value;
    }

    /**
     * Gets the value of the promotionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionName() {
        return promotionName;
    }

    /**
     * Sets the value of the promotionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionName(String value) {
        this.promotionName = value;
    }

    /**
     * Gets the value of the promotionComponentDetailId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPromotionComponentDetailId() {
        return promotionComponentDetailId;
    }

    /**
     * Sets the value of the promotionComponentDetailId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPromotionComponentDetailId(BigDecimal value) {
        this.promotionComponentDetailId = value;
    }

    /**
     * Gets the value of the promotionComponentId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPromotionComponentId() {
        return promotionComponentId;
    }

    /**
     * Sets the value of the promotionComponentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPromotionComponentId(BigDecimal value) {
        this.promotionComponentId = value;
    }

    /**
     * Gets the value of the promotionId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPromotionId() {
        return promotionId;
    }

    /**
     * Sets the value of the promotionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPromotionId(BigDecimal value) {
        this.promotionId = value;
    }

}
