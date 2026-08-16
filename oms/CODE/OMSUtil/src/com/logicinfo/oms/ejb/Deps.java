package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "Deps.findAll", query = "select o from Deps o"),
                 @NamedQuery(name = "Deps.findGroupNo", query = "select o.groupNo from Deps o where o.dept=:dept")})
public class Deps implements Serializable {
    @Column(name = "AVG_TOLERANCE_PCT")
    private BigDecimal avgTolerancePct;
    @Column(name = "BUD_INT", nullable = false)
    private BigDecimal budInt;
    @Column(name = "BUD_MKUP", nullable = false)
    private BigDecimal budMkup;
    private BigDecimal buyer;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createDatetime;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Id
    @Column(nullable = false)
    private BigDecimal dept;
    @Column(name = "DEPT_NAME", nullable = false, length = 120)
    private String deptName;
    @Column(name = "DEPT_VAT_INCL_IND", nullable = false, length = 1)
    private String deptVatInclInd;
    @Column(name = "GROUP_NO", nullable = false)
    private BigDecimal groupNo;
    @Column(name = "MARKUP_CALC_TYPE", nullable = false, length = 2)
    private String markupCalcType;
    @Column(name = "MAX_AVG_COUNTER")
    private BigDecimal maxAvgCounter;
    private BigDecimal merch;
    @Column(name = "OTB_CALC_TYPE", nullable = false, length = 1)
    private String otbCalcType;
    @Column(name = "PROFIT_CALC_TYPE", nullable = false)
    private BigDecimal profitCalcType;
    @Column(name = "PURCHASE_TYPE", nullable = false)
    private BigDecimal purchaseType;
    @Column(name = "TOTAL_MARKET_AMT")
    private BigDecimal totalMarketAmt;

    public Deps() {
    }

    public Deps(BigDecimal avgTolerancePct, BigDecimal budInt, BigDecimal budMkup, BigDecimal buyer,
                Date createDatetime, String createId, BigDecimal dept, String deptName, String deptVatInclInd,
                BigDecimal groupNo, String markupCalcType, BigDecimal maxAvgCounter, BigDecimal merch,
                String otbCalcType, BigDecimal profitCalcType, BigDecimal purchaseType, BigDecimal totalMarketAmt) {
        this.avgTolerancePct = avgTolerancePct;
        this.budInt = budInt;
        this.budMkup = budMkup;
        this.buyer = buyer;
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.dept = dept;
        this.deptName = deptName;
        this.deptVatInclInd = deptVatInclInd;
        this.groupNo = groupNo;
        this.markupCalcType = markupCalcType;
        this.maxAvgCounter = maxAvgCounter;
        this.merch = merch;
        this.otbCalcType = otbCalcType;
        this.profitCalcType = profitCalcType;
        this.purchaseType = purchaseType;
        this.totalMarketAmt = totalMarketAmt;
    }

    public BigDecimal getAvgTolerancePct() {
        return avgTolerancePct;
    }

    public void setAvgTolerancePct(BigDecimal avgTolerancePct) {
        this.avgTolerancePct = avgTolerancePct;
    }

    public BigDecimal getBudInt() {
        return budInt;
    }

    public void setBudInt(BigDecimal budInt) {
        this.budInt = budInt;
    }

    public BigDecimal getBudMkup() {
        return budMkup;
    }

    public void setBudMkup(BigDecimal budMkup) {
        this.budMkup = budMkup;
    }

    public BigDecimal getBuyer() {
        return buyer;
    }

    public void setBuyer(BigDecimal buyer) {
        this.buyer = buyer;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public BigDecimal getDept() {
        return dept;
    }

    public void setDept(BigDecimal dept) {
        this.dept = dept;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptVatInclInd() {
        return deptVatInclInd;
    }

    public void setDeptVatInclInd(String deptVatInclInd) {
        this.deptVatInclInd = deptVatInclInd;
    }

    public BigDecimal getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(BigDecimal groupNo) {
        this.groupNo = groupNo;
    }

    public String getMarkupCalcType() {
        return markupCalcType;
    }

    public void setMarkupCalcType(String markupCalcType) {
        this.markupCalcType = markupCalcType;
    }

    public BigDecimal getMaxAvgCounter() {
        return maxAvgCounter;
    }

    public void setMaxAvgCounter(BigDecimal maxAvgCounter) {
        this.maxAvgCounter = maxAvgCounter;
    }

    public BigDecimal getMerch() {
        return merch;
    }

    public void setMerch(BigDecimal merch) {
        this.merch = merch;
    }

    public String getOtbCalcType() {
        return otbCalcType;
    }

    public void setOtbCalcType(String otbCalcType) {
        this.otbCalcType = otbCalcType;
    }

    public BigDecimal getProfitCalcType() {
        return profitCalcType;
    }

    public void setProfitCalcType(BigDecimal profitCalcType) {
        this.profitCalcType = profitCalcType;
    }

    public BigDecimal getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(BigDecimal purchaseType) {
        this.purchaseType = purchaseType;
    }

    public BigDecimal getTotalMarketAmt() {
        return totalMarketAmt;
    }

    public void setTotalMarketAmt(BigDecimal totalMarketAmt) {
        this.totalMarketAmt = totalMarketAmt;
    }
}
