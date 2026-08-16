
package com.oracle.retail.integration.base.bo.discntlinedesc.v1;

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
 *         &lt;element name="line_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="accounting_method" type="{http://www.oracle.com/retail/integration/base/bo/DiscntLineDesc/v1}enum_discount_acct_method"/>
 *         &lt;element name="advanced_pricing_rule_flag" type="{http://www.oracle.com/retail/integration/base/bo/DiscntLineDesc/v1}flag"/>
 *         &lt;element name="assignment_basis" type="{http://www.oracle.com/retail/integration/base/bo/DiscntLineDesc/v1}enum_discount_assign_basis"/>
 *         &lt;element name="damage_discount_flag" type="{http://www.oracle.com/retail/integration/base/bo/DiscntLineDesc/v1}flag"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="completed_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="returned_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="unit_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="discount_employee_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="discount_method" type="{http://www.oracle.com/retail/integration/base/bo/DiscntLineDesc/v1}enum_discount_method"/>
 *         &lt;element name="discount_rate" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="discount_rule_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="discount_scope" type="{http://www.oracle.com/retail/integration/base/bo/DiscntLineDesc/v1}enum_discount_scope"/>
 *         &lt;element name="included_in_bestdeal_flag" type="{http://www.oracle.com/retail/integration/base/bo/DiscntLineDesc/v1}flag"/>
 *         &lt;element name="discount_reason_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="store_coupon_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "lineNo",
    "accountingMethod",
    "advancedPricingRuleFlag",
    "assignmentBasis",
    "damageDiscountFlag",
    "currencyCode",
    "discountAmount",
    "completedDiscountAmount",
    "cancelledDiscountAmount",
    "returnedDiscountAmount",
    "unitDiscountAmount",
    "discountEmployeeId",
    "discountMethod",
    "discountRate",
    "discountRuleId",
    "discountScope",
    "includedInBestdealFlag",
    "discountReasonCode",
    "storeCouponId",
    "promotionComponentDetailId",
    "promotionComponentId",
    "promotionId"
})
@XmlRootElement(name = "DiscntLineDesc")
public class DiscntLineDesc {

    @XmlElement(name = "line_no")
    protected int lineNo;
    @XmlElement(name = "accounting_method", required = true)
    protected EnumDiscountAcctMethod accountingMethod;
    @XmlElement(name = "advanced_pricing_rule_flag", required = true)
    protected Flag advancedPricingRuleFlag;
    @XmlElement(name = "assignment_basis", required = true)
    protected EnumDiscountAssignBasis assignmentBasis;
    @XmlElement(name = "damage_discount_flag", required = true)
    protected Flag damageDiscountFlag;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "discount_amount", required = true)
    protected BigDecimal discountAmount;
    @XmlElement(name = "completed_discount_amount", required = true)
    protected BigDecimal completedDiscountAmount;
    @XmlElement(name = "cancelled_discount_amount", required = true)
    protected BigDecimal cancelledDiscountAmount;
    @XmlElement(name = "returned_discount_amount", required = true)
    protected BigDecimal returnedDiscountAmount;
    @XmlElement(name = "unit_discount_amount")
    protected BigDecimal unitDiscountAmount;
    @XmlElement(name = "discount_employee_id")
    protected String discountEmployeeId;
    @XmlElement(name = "discount_method", required = true)
    protected EnumDiscountMethod discountMethod;
    @XmlElement(name = "discount_rate")
    protected BigDecimal discountRate;
    @XmlElement(name = "discount_rule_id")
    protected String discountRuleId;
    @XmlElement(name = "discount_scope", required = true)
    protected EnumDiscountScope discountScope;
    @XmlElement(name = "included_in_bestdeal_flag", required = true)
    protected Flag includedInBestdealFlag;
    @XmlElement(name = "discount_reason_code")
    protected String discountReasonCode;
    @XmlElement(name = "store_coupon_id")
    protected String storeCouponId;
    @XmlElement(name = "promotion_component_detail_id")
    protected BigDecimal promotionComponentDetailId;
    @XmlElement(name = "promotion_component_id")
    protected BigDecimal promotionComponentId;
    @XmlElement(name = "promotion_id")
    protected BigDecimal promotionId;

    /**
     * Gets the value of the lineNo property.
     * 
     */
    public int getLineNo() {
        return lineNo;
    }

    /**
     * Sets the value of the lineNo property.
     * 
     */
    public void setLineNo(int value) {
        this.lineNo = value;
    }

    /**
     * Gets the value of the accountingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link EnumDiscountAcctMethod }
     *     
     */
    public EnumDiscountAcctMethod getAccountingMethod() {
        return accountingMethod;
    }

    /**
     * Sets the value of the accountingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDiscountAcctMethod }
     *     
     */
    public void setAccountingMethod(EnumDiscountAcctMethod value) {
        this.accountingMethod = value;
    }

    /**
     * Gets the value of the advancedPricingRuleFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getAdvancedPricingRuleFlag() {
        return advancedPricingRuleFlag;
    }

    /**
     * Sets the value of the advancedPricingRuleFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setAdvancedPricingRuleFlag(Flag value) {
        this.advancedPricingRuleFlag = value;
    }

    /**
     * Gets the value of the assignmentBasis property.
     * 
     * @return
     *     possible object is
     *     {@link EnumDiscountAssignBasis }
     *     
     */
    public EnumDiscountAssignBasis getAssignmentBasis() {
        return assignmentBasis;
    }

    /**
     * Sets the value of the assignmentBasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDiscountAssignBasis }
     *     
     */
    public void setAssignmentBasis(EnumDiscountAssignBasis value) {
        this.assignmentBasis = value;
    }

    /**
     * Gets the value of the damageDiscountFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getDamageDiscountFlag() {
        return damageDiscountFlag;
    }

    /**
     * Sets the value of the damageDiscountFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setDamageDiscountFlag(Flag value) {
        this.damageDiscountFlag = value;
    }

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
     * Gets the value of the discountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Sets the value of the discountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDiscountAmount(BigDecimal value) {
        this.discountAmount = value;
    }

    /**
     * Gets the value of the completedDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedDiscountAmount() {
        return completedDiscountAmount;
    }

    /**
     * Sets the value of the completedDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedDiscountAmount(BigDecimal value) {
        this.completedDiscountAmount = value;
    }

    /**
     * Gets the value of the cancelledDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelledDiscountAmount() {
        return cancelledDiscountAmount;
    }

    /**
     * Sets the value of the cancelledDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelledDiscountAmount(BigDecimal value) {
        this.cancelledDiscountAmount = value;
    }

    /**
     * Gets the value of the returnedDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedDiscountAmount() {
        return returnedDiscountAmount;
    }

    /**
     * Sets the value of the returnedDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedDiscountAmount(BigDecimal value) {
        this.returnedDiscountAmount = value;
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
     * Gets the value of the discountEmployeeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscountEmployeeId() {
        return discountEmployeeId;
    }

    /**
     * Sets the value of the discountEmployeeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscountEmployeeId(String value) {
        this.discountEmployeeId = value;
    }

    /**
     * Gets the value of the discountMethod property.
     * 
     * @return
     *     possible object is
     *     {@link EnumDiscountMethod }
     *     
     */
    public EnumDiscountMethod getDiscountMethod() {
        return discountMethod;
    }

    /**
     * Sets the value of the discountMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDiscountMethod }
     *     
     */
    public void setDiscountMethod(EnumDiscountMethod value) {
        this.discountMethod = value;
    }

    /**
     * Gets the value of the discountRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    /**
     * Sets the value of the discountRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDiscountRate(BigDecimal value) {
        this.discountRate = value;
    }

    /**
     * Gets the value of the discountRuleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscountRuleId() {
        return discountRuleId;
    }

    /**
     * Sets the value of the discountRuleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscountRuleId(String value) {
        this.discountRuleId = value;
    }

    /**
     * Gets the value of the discountScope property.
     * 
     * @return
     *     possible object is
     *     {@link EnumDiscountScope }
     *     
     */
    public EnumDiscountScope getDiscountScope() {
        return discountScope;
    }

    /**
     * Sets the value of the discountScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDiscountScope }
     *     
     */
    public void setDiscountScope(EnumDiscountScope value) {
        this.discountScope = value;
    }

    /**
     * Gets the value of the includedInBestdealFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getIncludedInBestdealFlag() {
        return includedInBestdealFlag;
    }

    /**
     * Sets the value of the includedInBestdealFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setIncludedInBestdealFlag(Flag value) {
        this.includedInBestdealFlag = value;
    }

    /**
     * Gets the value of the discountReasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscountReasonCode() {
        return discountReasonCode;
    }

    /**
     * Sets the value of the discountReasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscountReasonCode(String value) {
        this.discountReasonCode = value;
    }

    /**
     * Gets the value of the storeCouponId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreCouponId() {
        return storeCouponId;
    }

    /**
     * Sets the value of the storeCouponId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreCouponId(String value) {
        this.storeCouponId = value;
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
