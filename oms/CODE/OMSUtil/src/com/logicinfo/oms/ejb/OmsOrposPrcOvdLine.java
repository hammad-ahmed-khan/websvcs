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
@NamedQueries( { @NamedQuery(name = "OmsOrposPrcOvdLine.findAll", query = "select o from OmsOrposPrcOvdLine o"),
                 @NamedQuery(name = "OmsOrposPrcOvdLine.findByOmsOrposCustOrdId", query = "select o from OmsOrposPrcOvdLine o where o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_PRC_OVD_LINE")
@IdClass(OmsOrposPrcOvdLinePK.class)
public class OmsOrposPrcOvdLine implements Serializable {
    @Column(name = "AUTHORIZING_EMPLOYEE_ID", length = 10)
    private String authorizingEmployeeId;
    @Id
    @Column(name = "CAPTURED_LINE_ITEM_NO", nullable = false)
    private BigDecimal capturedLineItemNo;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Column(name = "ENTRY_METHOD", length = 8)
    private String entryMethod;
    @Id
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "OVERRIDE_REASON_CODE", length = 20)
    private String overrideReasonCode;
    @Column(name = "UNIT_OVERRIDDEN_PRICE", nullable = false)
    private BigDecimal unitOverriddenPrice;

    public OmsOrposPrcOvdLine() {
    }

    public OmsOrposPrcOvdLine(String authorizingEmployeeId, BigDecimal capturedLineItemNo, String currencyCode,
                              String entryMethod, String itemId, BigDecimal omsOrposCustOrderId,
                              String overrideReasonCode, BigDecimal unitOverriddenPrice) {
        this.authorizingEmployeeId = authorizingEmployeeId;
        this.capturedLineItemNo = capturedLineItemNo;
        this.currencyCode = currencyCode;
        this.entryMethod = entryMethod;
        this.itemId = itemId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.overrideReasonCode = overrideReasonCode;
        this.unitOverriddenPrice = unitOverriddenPrice;
    }

    public String getAuthorizingEmployeeId() {
        return authorizingEmployeeId;
    }

    public void setAuthorizingEmployeeId(String authorizingEmployeeId) {
        this.authorizingEmployeeId = authorizingEmployeeId;
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

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
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

    public String getOverrideReasonCode() {
        return overrideReasonCode;
    }

    public void setOverrideReasonCode(String overrideReasonCode) {
        this.overrideReasonCode = overrideReasonCode;
    }

    public BigDecimal getUnitOverriddenPrice() {
        return unitOverriddenPrice;
    }

    public void setUnitOverriddenPrice(BigDecimal unitOverriddenPrice) {
        this.unitOverriddenPrice = unitOverriddenPrice;
    }
}
