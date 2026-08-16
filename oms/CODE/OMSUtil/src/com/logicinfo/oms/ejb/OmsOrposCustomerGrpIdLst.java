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
@NamedQueries( { @NamedQuery(name = "OmsOrposCustomerGrpIdLst.findAll",
                             query = "select o from OmsOrposCustomerGrpIdLst o") })
@Table(name = "OMS_ORPOS_CUSTOMER_GRP_ID_LST")
public class OmsOrposCustomerGrpIdLst implements Serializable {
    @Id
    @Column(name = "CUSTOMER_GROUP_ID", nullable = false)
    private BigDecimal customerGroupId;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;

    public OmsOrposCustomerGrpIdLst() {
    }

    public OmsOrposCustomerGrpIdLst(BigDecimal customerGroupId, BigDecimal omsOrposCustOrderId) {
        this.customerGroupId = customerGroupId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(BigDecimal customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
