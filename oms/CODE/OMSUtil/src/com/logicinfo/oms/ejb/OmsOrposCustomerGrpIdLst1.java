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
@NamedQueries( { @NamedQuery(name = "OmsOrposCustomerGrpIdLst1.findAll",
                             query = "select o from OmsOrposCustomerGrpIdLst1 o") })
@Table(name = "OMS_ORPOS_CUSTOMER_GRP_ID_LST")
@IdClass(OmsOrposCustomerGrpIdLst1PK.class)
public class OmsOrposCustomerGrpIdLst1 implements Serializable {
    @Id
    @Column(name = "CUSTOMER_ID", nullable = false)
    private BigDecimal customerId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;

    public OmsOrposCustomerGrpIdLst1() {
    }

    public OmsOrposCustomerGrpIdLst1(BigDecimal customerId, BigDecimal omsOrposCustOrderId) {
        this.customerId = customerId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
