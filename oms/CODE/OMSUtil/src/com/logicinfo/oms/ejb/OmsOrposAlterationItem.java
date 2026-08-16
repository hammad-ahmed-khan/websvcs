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
@NamedQueries( { @NamedQuery(name = "OmsOrposAlterationItem.findAll",
                             query = "select o from OmsOrposAlterationItem o") ,
                 @NamedQuery(name = "OmsOrposAlterationItem.findByOmsOrposCustOrdId", query = "select o from OmsOrposAlterationItem o where o.omsOrposCustOrderId=:omsOrposCustOrderId")
 })
@Table(name = "OMS_ORPOS_ALTERATION_ITEM")
@IdClass(OmsOrposAlterationItemPK.class)
public class OmsOrposAlterationItem implements Serializable {
    @Column(name = "ALTERATION_TYPE", nullable = false, length = 6)
    private String alterationType;
    @Id
    @Column(name = "CAPTURED_LINE_ITEM_NO", nullable = false)
    private BigDecimal capturedLineItemNo;
    @Column(length = 250)
    private String instruction;
    @Id
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;

    public OmsOrposAlterationItem() {
    }

    public OmsOrposAlterationItem(String alterationType, BigDecimal capturedLineItemNo, String instruction,
                                  String itemId, BigDecimal omsOrposCustOrderId) {
        this.alterationType = alterationType;
        this.capturedLineItemNo = capturedLineItemNo;
        this.instruction = instruction;
        this.itemId = itemId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getAlterationType() {
        return alterationType;
    }

    public void setAlterationType(String alterationType) {
        this.alterationType = alterationType;
    }

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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
}
