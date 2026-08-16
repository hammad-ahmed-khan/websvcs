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
@NamedQueries( { @NamedQuery(name = "OmsOrposPurchaseOrdTender.findAll",
                             query = "select o from OmsOrposPurchaseOrdTender o") })
@Table(name = "OMS_ORPOS_PURCHASE_ORD_TENDER")
public class OmsOrposPurchaseOrdTender implements Serializable {
    @Column(name = "AGENT_NAME", nullable = false, length = 120)
    private String agentName;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Id
    @Column(name = "PURCHASE_ORDER_NUMBER", nullable = false, length = 15)
    private String purchaseOrderNumber;

    public OmsOrposPurchaseOrdTender() {
    }

    public OmsOrposPurchaseOrdTender(String agentName, BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo,
                                     String purchaseOrderNumber) {
        this.agentName = agentName;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPaymentSeqNo() {
        return paymentSeqNo;
    }

    public void setPaymentSeqNo(BigDecimal paymentSeqNo) {
        this.paymentSeqNo = paymentSeqNo;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }
}
