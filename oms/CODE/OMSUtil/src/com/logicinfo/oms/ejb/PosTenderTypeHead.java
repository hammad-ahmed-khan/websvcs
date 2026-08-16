package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "PosTenderTypeHead.findAll", query = "select o from PosTenderTypeHead o"),
                 @NamedQuery(name = "PosTenderTypeHead.findTenderTypeGroup",query = "select o.tenderTypeGroup from PosTenderTypeHead o where o.tenderTypeId=:tenderTypeId")
                 })
@Table(name = "POS_TENDER_TYPE_HEAD")
public class PosTenderTypeHead implements Serializable {
    @Column(name = "ACCUMULATE_CASH_INTAKE_IND", nullable = false, length = 1)
    private String accumulateCashIntakeInd;
    @Column(name = "ASK_FOR_INVOICE_IND", nullable = false, length = 1)
    private String askForInvoiceInd;
    @Column(name = "AUTHORIZE_MIN_AMT")
    private BigDecimal authorizeMinAmt;
    @Column(name = "AUTOMATIC_DEPOSIT_IND", nullable = false, length = 1)
    private String automaticDepositInd;
    @Column(name = "CASH_EQUIV_IND", nullable = false, length = 1)
    private String cashEquivInd;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;
    @Column(name = "DEPOSIT_IN_BANK_IND", nullable = false, length = 1)
    private String depositInBankInd;
    @Column(name = "DEPOSIT_OVERRIDE_IND", nullable = false, length = 1)
    private String depositOverrideInd;
    @Column(name = "DISCREPANCY_DISPLAY_TYPE", length = 6)
    private String discrepancyDisplayType;
    @Column(name = "DISPLAY_IND", nullable = false, length = 1)
    private String displayInd;
    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Date effectiveDate;
    @Column(name = "EXACT_CHANGE_IND", nullable = false, length = 1)
    private String exactChangeInd;
    @Column(name = "EXPORT_CODE", length = 6)
    private String exportCode;
    @Column(name = "EXTRACT_REQ_IND", nullable = false, length = 1)
    private String extractReqInd;
    @Column(name = "IMPRINT_IND", nullable = false, length = 1)
    private String imprintInd;
    @Column(name = "IMPRINT_TENDER_TYPE", length = 20)
    private String imprintTenderType;
    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;
    @Column(name = "MODIFY_ID", length = 30)
    private String modifyId;
    @Column(name = "NEXT_DOLLAR_IND", nullable = false, length = 1)
    private String nextDollarInd;
    @Column(name = "OPEN_DRAWER_IND", nullable = false, length = 1)
    private String openDrawerInd;
    @Column(name = "PAY_IN_DEPOSIT_IND", nullable = false, length = 1)
    private String payInDepositInd;
    @Column(name = "PHONE_AUTHORIZE_TYPE", length = 6)
    private String phoneAuthorizeType;
    @Column(name = "POS_CONFIG_STATUS", length = 1)
    private String posConfigStatus;
    @Column(name = "PRESET_AMT")
    private BigDecimal presetAmt;
    @Column(name = "PROCESSOR_TYPE", nullable = false, length = 6)
    private String processorType;
    @Column(name = "PROFIT_CENTER", length = 6)
    private String profitCenter;
    @Column(name = "SHOW_IN_BREAKDOWN_IND", nullable = false, length = 1)
    private String showInBreakdownInd;
    @Column(name = "SYSTEM_REQ_IND", nullable = false, length = 1)
    private String systemReqInd;
    @Column(name = "TENDER_TYPE_DESC", nullable = false, length = 120)
    private String tenderTypeDesc;
    @Column(name = "TENDER_TYPE_GROUP", nullable = false, length = 6)
    private String tenderTypeGroup;
    @Id
    @Column(name = "TENDER_TYPE_ID", nullable = false)
    private BigDecimal tenderTypeId;

    public PosTenderTypeHead() {
    }

    public PosTenderTypeHead(String accumulateCashIntakeInd, String askForInvoiceInd, BigDecimal authorizeMinAmt,
                             String automaticDepositInd, String cashEquivInd, Date createDate, String createId,
                             String currencyCode, String depositInBankInd, String depositOverrideInd,
                             String discrepancyDisplayType, String displayInd, Date effectiveDate,
                             String exactChangeInd, String exportCode, String extractReqInd, String imprintInd,
                             String imprintTenderType, Date modifyDate, String modifyId, String nextDollarInd,
                             String openDrawerInd, String payInDepositInd, String phoneAuthorizeType,
                             String posConfigStatus, BigDecimal presetAmt, String processorType, String profitCenter,
                             String showInBreakdownInd, String systemReqInd, String tenderTypeDesc,
                             String tenderTypeGroup, BigDecimal tenderTypeId) {
        this.accumulateCashIntakeInd = accumulateCashIntakeInd;
        this.askForInvoiceInd = askForInvoiceInd;
        this.authorizeMinAmt = authorizeMinAmt;
        this.automaticDepositInd = automaticDepositInd;
        this.cashEquivInd = cashEquivInd;
        this.createDate = createDate;
        this.createId = createId;
        this.currencyCode = currencyCode;
        this.depositInBankInd = depositInBankInd;
        this.depositOverrideInd = depositOverrideInd;
        this.discrepancyDisplayType = discrepancyDisplayType;
        this.displayInd = displayInd;
        this.effectiveDate = effectiveDate;
        this.exactChangeInd = exactChangeInd;
        this.exportCode = exportCode;
        this.extractReqInd = extractReqInd;
        this.imprintInd = imprintInd;
        this.imprintTenderType = imprintTenderType;
        this.modifyDate = modifyDate;
        this.modifyId = modifyId;
        this.nextDollarInd = nextDollarInd;
        this.openDrawerInd = openDrawerInd;
        this.payInDepositInd = payInDepositInd;
        this.phoneAuthorizeType = phoneAuthorizeType;
        this.posConfigStatus = posConfigStatus;
        this.presetAmt = presetAmt;
        this.processorType = processorType;
        this.profitCenter = profitCenter;
        this.showInBreakdownInd = showInBreakdownInd;
        this.systemReqInd = systemReqInd;
        this.tenderTypeDesc = tenderTypeDesc;
        this.tenderTypeGroup = tenderTypeGroup;
        this.tenderTypeId = tenderTypeId;
    }

    public String getAccumulateCashIntakeInd() {
        return accumulateCashIntakeInd;
    }

    public void setAccumulateCashIntakeInd(String accumulateCashIntakeInd) {
        this.accumulateCashIntakeInd = accumulateCashIntakeInd;
    }

    public String getAskForInvoiceInd() {
        return askForInvoiceInd;
    }

    public void setAskForInvoiceInd(String askForInvoiceInd) {
        this.askForInvoiceInd = askForInvoiceInd;
    }

    public BigDecimal getAuthorizeMinAmt() {
        return authorizeMinAmt;
    }

    public void setAuthorizeMinAmt(BigDecimal authorizeMinAmt) {
        this.authorizeMinAmt = authorizeMinAmt;
    }

    public String getAutomaticDepositInd() {
        return automaticDepositInd;
    }

    public void setAutomaticDepositInd(String automaticDepositInd) {
        this.automaticDepositInd = automaticDepositInd;
    }

    public String getCashEquivInd() {
        return cashEquivInd;
    }

    public void setCashEquivInd(String cashEquivInd) {
        this.cashEquivInd = cashEquivInd;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDepositInBankInd() {
        return depositInBankInd;
    }

    public void setDepositInBankInd(String depositInBankInd) {
        this.depositInBankInd = depositInBankInd;
    }

    public String getDepositOverrideInd() {
        return depositOverrideInd;
    }

    public void setDepositOverrideInd(String depositOverrideInd) {
        this.depositOverrideInd = depositOverrideInd;
    }

    public String getDiscrepancyDisplayType() {
        return discrepancyDisplayType;
    }

    public void setDiscrepancyDisplayType(String discrepancyDisplayType) {
        this.discrepancyDisplayType = discrepancyDisplayType;
    }

    public String getDisplayInd() {
        return displayInd;
    }

    public void setDisplayInd(String displayInd) {
        this.displayInd = displayInd;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExactChangeInd() {
        return exactChangeInd;
    }

    public void setExactChangeInd(String exactChangeInd) {
        this.exactChangeInd = exactChangeInd;
    }

    public String getExportCode() {
        return exportCode;
    }

    public void setExportCode(String exportCode) {
        this.exportCode = exportCode;
    }

    public String getExtractReqInd() {
        return extractReqInd;
    }

    public void setExtractReqInd(String extractReqInd) {
        this.extractReqInd = extractReqInd;
    }

    public String getImprintInd() {
        return imprintInd;
    }

    public void setImprintInd(String imprintInd) {
        this.imprintInd = imprintInd;
    }

    public String getImprintTenderType() {
        return imprintTenderType;
    }

    public void setImprintTenderType(String imprintTenderType) {
        this.imprintTenderType = imprintTenderType;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getNextDollarInd() {
        return nextDollarInd;
    }

    public void setNextDollarInd(String nextDollarInd) {
        this.nextDollarInd = nextDollarInd;
    }

    public String getOpenDrawerInd() {
        return openDrawerInd;
    }

    public void setOpenDrawerInd(String openDrawerInd) {
        this.openDrawerInd = openDrawerInd;
    }

    public String getPayInDepositInd() {
        return payInDepositInd;
    }

    public void setPayInDepositInd(String payInDepositInd) {
        this.payInDepositInd = payInDepositInd;
    }

    public String getPhoneAuthorizeType() {
        return phoneAuthorizeType;
    }

    public void setPhoneAuthorizeType(String phoneAuthorizeType) {
        this.phoneAuthorizeType = phoneAuthorizeType;
    }

    public String getPosConfigStatus() {
        return posConfigStatus;
    }

    public void setPosConfigStatus(String posConfigStatus) {
        this.posConfigStatus = posConfigStatus;
    }

    public BigDecimal getPresetAmt() {
        return presetAmt;
    }

    public void setPresetAmt(BigDecimal presetAmt) {
        this.presetAmt = presetAmt;
    }

    public String getProcessorType() {
        return processorType;
    }

    public void setProcessorType(String processorType) {
        this.processorType = processorType;
    }

    public String getProfitCenter() {
        return profitCenter;
    }

    public void setProfitCenter(String profitCenter) {
        this.profitCenter = profitCenter;
    }

    public String getShowInBreakdownInd() {
        return showInBreakdownInd;
    }

    public void setShowInBreakdownInd(String showInBreakdownInd) {
        this.showInBreakdownInd = showInBreakdownInd;
    }

    public String getSystemReqInd() {
        return systemReqInd;
    }

    public void setSystemReqInd(String systemReqInd) {
        this.systemReqInd = systemReqInd;
    }

    public String getTenderTypeDesc() {
        return tenderTypeDesc;
    }

    public void setTenderTypeDesc(String tenderTypeDesc) {
        this.tenderTypeDesc = tenderTypeDesc;
    }

    public String getTenderTypeGroup() {
        return tenderTypeGroup;
    }

    public void setTenderTypeGroup(String tenderTypeGroup) {
        this.tenderTypeGroup = tenderTypeGroup;
    }

    public BigDecimal getTenderTypeId() {
        return tenderTypeId;
    }

    public void setTenderTypeId(BigDecimal tenderTypeId) {
        this.tenderTypeId = tenderTypeId;
    }
}
