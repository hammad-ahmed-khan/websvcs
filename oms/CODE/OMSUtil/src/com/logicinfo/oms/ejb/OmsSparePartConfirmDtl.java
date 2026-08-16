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
@NamedQueries( { @NamedQuery(name = "OmsSparePartConfirmDtl.findAll",
                             query = "select o from OmsSparePartConfirmDtl o") })
@Table(name = "OMS_SPARE_PART_CONFIRM_DTL")
@IdClass(OmsSparePartConfirmDtlPK.class)
public class OmsSparePartConfirmDtl implements Serializable {
    @Column(name = "CONFIRM_QTY", nullable = false)
    private BigDecimal confirmQty;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "OMS_SERVICE_REQ_SEQ_ID", nullable = false, length = 20)
    private String omsServiceReqSeqId;
    @Column(name = "REASON_CODE", length = 4)
    private String reasonCode;
    @Id
    @Column(name = "SERVICE_CONFIRM_ID", nullable = false, length = 20)
    private String serviceConfirmId;

    public OmsSparePartConfirmDtl() {
    }

    public OmsSparePartConfirmDtl(BigDecimal confirmQty, String item, String omsServiceReqSeqId, String reasonCode,
                                  String serviceConfirmId) {
        this.confirmQty = confirmQty;
        this.item = item;
        this.omsServiceReqSeqId = omsServiceReqSeqId;
        this.reasonCode = reasonCode;
        this.serviceConfirmId = serviceConfirmId;
    }

    public BigDecimal getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(BigDecimal confirmQty) {
        this.confirmQty = confirmQty;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOmsServiceReqSeqId() {
        return omsServiceReqSeqId;
    }

    public void setOmsServiceReqSeqId(String omsServiceReqSeqId) {
        this.omsServiceReqSeqId = omsServiceReqSeqId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getServiceConfirmId() {
        return serviceConfirmId;
    }

    public void setServiceConfirmId(String serviceConfirmId) {
        this.serviceConfirmId = serviceConfirmId;
    }
}
