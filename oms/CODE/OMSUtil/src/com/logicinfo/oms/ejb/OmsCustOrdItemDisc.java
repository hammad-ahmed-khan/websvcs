
package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsCustOrdItemDisc.findAll", query = "select o from OmsCustOrdItemDisc o"), 
                 @NamedQuery(name = "OmsCustOrdItemDisc.FindByOmsCustordNoLineNo", query = "select o from OmsCustOrdItemDisc o where o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo"),
                 @NamedQuery(name = "OmsCustOrdItemDisc.FindByOmsCustordNoLineNoUnitDiscnt", query = "select sum(o.unitDiscountAmount) from OmsCustOrdItemDisc o where o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo")})
@Table(name = "OMS_CUST_ORD_ITEM_DISC")
@IdClass(OmsCustOrdItemDiscPK.class)
public class OmsCustOrdItemDisc implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createDatetime;
    @Id
    @Column(name = "DISC_LINE_NO", nullable = false)
    private BigDecimal discLineNo;
    @Column(name = "DISC_REF_NO")
    private BigDecimal discRefNo;
    @Column(name = "DISCOUNT_TYPE", length = 6)
    private String discountType;
    @Column(name = "EMPLOYEE_ID", length = 10)
    private String employeeId;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "PROMO_COMP_ID")
    private BigDecimal promoCompId;
    @Column(name = "RMS_PROMO_TYPE", nullable = false, length = 6)
    private String rmsPromoType;
    @Column(name = "SIMPLE_PROMO_IND", length = 12)
    private String simplePromoInd;
    @Column(name = "UNIT_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal unitDiscountAmount;

    public OmsCustOrdItemDisc() {
    }

    public OmsCustOrdItemDisc(Date createDatetime, BigDecimal discLineNo, BigDecimal discRefNo, String discountType,
                              String employeeId, BigDecimal lineNo, BigDecimal omsCustOrdNo, BigDecimal promoCompId,
                              String rmsPromoType, String simplePromoInd, BigDecimal unitDiscountAmount) {
        this.createDatetime = createDatetime;
        this.discLineNo = discLineNo;
        this.discRefNo = discRefNo;
        this.discountType = discountType;
        this.employeeId = employeeId;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
        this.promoCompId = promoCompId;
        this.rmsPromoType = rmsPromoType;
        this.simplePromoInd = simplePromoInd;
        this.unitDiscountAmount = unitDiscountAmount;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getDiscLineNo() {
        return discLineNo;
    }

    public void setDiscLineNo(BigDecimal discLineNo) {
        this.discLineNo = discLineNo;
    }

    public BigDecimal getDiscRefNo() {
        return discRefNo;
    }

    public void setDiscRefNo(BigDecimal discRefNo) {
        this.discRefNo = discRefNo;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getPromoCompId() {
        return promoCompId;
    }

    public void setPromoCompId(BigDecimal promoCompId) {
        this.promoCompId = promoCompId;
    }

    public String getRmsPromoType() {
        return rmsPromoType;
    }

    public void setRmsPromoType(String rmsPromoType) {
        this.rmsPromoType = rmsPromoType;
    }

    public String getSimplePromoInd() {
        return simplePromoInd;
    }

    public void setSimplePromoInd(String simplePromoInd) {
        this.simplePromoInd = simplePromoInd;
    }

    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }

    public void setUnitDiscountAmount(BigDecimal unitDiscountAmount) {
        this.unitDiscountAmount = unitDiscountAmount;
    }
}
