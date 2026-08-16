package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OmsSparePartConfirmDtl;

import com.logicinfo.oms.ejb.OmsSparePartFulfill;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

public class SparePartsConfirmationProcessedObject {
    private OmsSparePartConfirmDtl sparePartsConfirmDtlObj;
    private boolean simUnreserveProcessed;
    private boolean simDeductProcessed;
    private boolean fulfillProcessed;
    private boolean spHeaderProcessed;
    private boolean confirmHdrProcessed;
    private boolean confirmDtlProcessed;
    private boolean auditProcessed;
    private BigDecimal quantityProcessed;
    private String serviceRequestId;
    private List<SparePartsFulfillProcessedObject> processedFulfilledRecs;
    
    public SparePartsConfirmationProcessedObject() {
        super();
        this.simDeductProcessed = false;
        this.simUnreserveProcessed = false;
        this.fulfillProcessed = false;
        this.spHeaderProcessed = false;
        this.auditProcessed = false;
        this.confirmDtlProcessed = false;
        this.confirmHdrProcessed = false;
        
        this.quantityProcessed = null;
        this.serviceRequestId = null;
        this.processedFulfilledRecs= new ArrayList<SparePartsFulfillProcessedObject>();
    }

    public void setFulfillmentProcessed(boolean fulfillProcessed) {
        this.fulfillProcessed = fulfillProcessed;
    }

    public boolean isFulfillmentProcessed() {
        return fulfillProcessed;
    }

    public void setAuditProcessed(boolean auditProcessed) {
        this.auditProcessed = auditProcessed;
    }

    public boolean isAuditProcessed() {
        return auditProcessed;
    }

    public void setQuantityProcessed(BigDecimal quantityProcessed) {
        this.quantityProcessed = quantityProcessed;
    }

    public BigDecimal getQuantityProcessed() {
        return quantityProcessed;
    }

    public void setSparePartsConfirmDtlObj(OmsSparePartConfirmDtl sparePartsConfirmDtlObj) {
        this.sparePartsConfirmDtlObj = sparePartsConfirmDtlObj;
    }

    public OmsSparePartConfirmDtl getSparePartsConfirmDtlObj() {
        return sparePartsConfirmDtlObj;
    }

    public void setSimUnreserveProcessed(boolean simUnreserveProcessed) {
        this.simUnreserveProcessed = simUnreserveProcessed;
    }

    public boolean isSimUnreserveProcessed() {
        return simUnreserveProcessed;
    }

    public void setSimDeductProcessed(boolean simDeductProcessed) {
        this.simDeductProcessed = simDeductProcessed;
    }

    public boolean isSimDeductProcessed() {
        return simDeductProcessed;
    }

    public void setSpHeaderProcessed(boolean spHeaderProcessed) {
        this.spHeaderProcessed = spHeaderProcessed;
    }

    public boolean isSpHeaderProcessed() {
        return spHeaderProcessed;
    }

    public void setConfirmHdrProcessed(boolean confirmHdrProcessed) {
        this.confirmHdrProcessed = confirmHdrProcessed;
    }

    public boolean isConfirmHdrProcessed() {
        return confirmHdrProcessed;
    }

    public void setConfirmDtlProcessed(boolean confirmDtlProcessed) {
        this.confirmDtlProcessed = confirmDtlProcessed;
    }

    public boolean isConfirmDtlProcessed() {
        return confirmDtlProcessed;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setProcessedFulfilledRecs(List<SparePartsFulfillProcessedObject> processedFulfilledRecs) {
        this.processedFulfilledRecs = processedFulfilledRecs;
    }

    public List<SparePartsFulfillProcessedObject> getProcessedFulfilledRecs() {
        return processedFulfilledRecs;
    }
}
