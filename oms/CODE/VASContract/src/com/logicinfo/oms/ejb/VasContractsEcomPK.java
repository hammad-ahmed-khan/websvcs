package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class VasContractsEcomPK implements Serializable{
    public BigDecimal srvId;
    
    public VasContractsEcomPK() {
    }
    
    public VasContractsEcomPK(BigDecimal srvId) {
        this.srvId=srvId;
    }


    public void setSrvId(BigDecimal srvId) {
        this.srvId = srvId;
    }

    public BigDecimal getSrvId() {
        return srvId;
    }
}