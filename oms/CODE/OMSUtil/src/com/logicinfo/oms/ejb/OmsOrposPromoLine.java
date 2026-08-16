package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposPromoLine.findAll", query = "select o from OmsOrposPromoLine o") ,
                 @NamedQuery(name = "OmsOrposPromoLine.findByOmsOrposCustOrdId", query = "select o from OmsOrposPromoLine o where o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_PROMO_LINE")
@IdClass(OmsOrposPromoLinePK.class)
public class OmsOrposPromoLine implements Serializable {
    @Id
    @Column(name = "CAPTURED_LINE_ITEM_NO", nullable = false)
    private BigDecimal capturedLineItemNo;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Id
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PROMOTION_COMPONENT_DETAIL_ID")
    private BigDecimal promotionComponentDetailId;
    @Column(name = "PROMOTION_COMPONENT_ID")
    private BigDecimal promotionComponentId;
    @Column(name = "PROMOTION_ID")
    private BigDecimal promotionId;
    @Column(name = "PROMOTION_NAME", length = 250)
    private String promotionName;
    @Column(name = "UNIT_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal unitDiscountAmount;

    public OmsOrposPromoLine() {
    }

    public OmsOrposPromoLine(BigDecimal capturedLineItemNo, String currencyCode, String itemId,
                             BigDecimal omsOrposCustOrderId, BigDecimal promotionComponentDetailId,
                             BigDecimal promotionComponentId, BigDecimal promotionId, String promotionName,
                             BigDecimal unitDiscountAmount) {
        this.capturedLineItemNo = capturedLineItemNo;
        this.currencyCode = currencyCode;
        this.itemId = itemId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.promotionComponentDetailId = promotionComponentDetailId;
        this.promotionComponentId = promotionComponentId;
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.unitDiscountAmount = unitDiscountAmount;
    }

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPromotionComponentDetailId() {
        return promotionComponentDetailId;
    }

    public void setPromotionComponentDetailId(BigDecimal promotionComponentDetailId) {
        this.promotionComponentDetailId = promotionComponentDetailId;
    }

    public BigDecimal getPromotionComponentId() {
        return promotionComponentId;
    }

    public void setPromotionComponentId(BigDecimal promotionComponentId) {
        this.promotionComponentId = promotionComponentId;
    }

    public BigDecimal getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(BigDecimal promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }

    public void setUnitDiscountAmount(BigDecimal unitDiscountAmount) {
        this.unitDiscountAmount = unitDiscountAmount;
    }
}
