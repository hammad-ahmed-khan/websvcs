package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "VWh.findAll", query = "select o from VWh o"),
                 @NamedQuery(name = "VWh.findPhyWH", query =
                             "select o.physicalWh from VWh o where o.physicalWh=:physicalWh ") })
@Table(name = "V_WH")
public class VWh implements Serializable {
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Column(name = "IB_IND", nullable = false, length = 1)
    private String ibInd;
    @Column(name = "ORG_UNIT_ID")
    private BigDecimal orgUnitId;
    @Column(name = "PHYSICAL_WH", nullable = false)
    private BigDecimal physicalWh;
    @Column(name = "PRIMARY_WH")
    private BigDecimal primaryWh;
    @Column(name = "REPL_IND", nullable = false, length = 1)
    private String replInd;
    @Column(name = "STOCKHOLDING_IND", nullable = false, length = 1)
    private String stockholdingInd;
    @Id
    @Column(nullable = false)
    private BigDecimal wh;
    @Column(name = "WH_NAME", nullable = false, length = 150)
    private String whName;
    @Column(name = "WH_NAME_SECONDARY", length = 150)
    private String whNameSecondary;

    public VWh() {
    }

    public VWh(String currencyCode, String ibInd, BigDecimal orgUnitId, BigDecimal physicalWh, BigDecimal primaryWh,
               String replInd, String stockholdingInd, BigDecimal wh, String whName, String whNameSecondary) {
        this.currencyCode = currencyCode;
        this.ibInd = ibInd;
        this.orgUnitId = orgUnitId;
        this.physicalWh = physicalWh;
        this.primaryWh = primaryWh;
        this.replInd = replInd;
        this.stockholdingInd = stockholdingInd;
        this.wh = wh;
        this.whName = whName;
        this.whNameSecondary = whNameSecondary;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getIbInd() {
        return ibInd;
    }

    public void setIbInd(String ibInd) {
        this.ibInd = ibInd;
    }

    public BigDecimal getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(BigDecimal orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public BigDecimal getPhysicalWh() {
        return physicalWh;
    }

    public void setPhysicalWh(BigDecimal physicalWh) {
        this.physicalWh = physicalWh;
    }

    public BigDecimal getPrimaryWh() {
        return primaryWh;
    }

    public void setPrimaryWh(BigDecimal primaryWh) {
        this.primaryWh = primaryWh;
    }

    public String getReplInd() {
        return replInd;
    }

    public void setReplInd(String replInd) {
        this.replInd = replInd;
    }

    public String getStockholdingInd() {
        return stockholdingInd;
    }

    public void setStockholdingInd(String stockholdingInd) {
        this.stockholdingInd = stockholdingInd;
    }

    public BigDecimal getWh() {
        return wh;
    }

    public void setWh(BigDecimal wh) {
        this.wh = wh;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getWhNameSecondary() {
        return whNameSecondary;
    }

    public void setWhNameSecondary(String whNameSecondary) {
        this.whNameSecondary = whNameSecondary;
    }
}
