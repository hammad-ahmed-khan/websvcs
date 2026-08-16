package com.logicinfo.oms.beans;

import java.util.Date;

public class BackOrderResponse {
    public BackOrderResponse() {
        super();
    }
    Date futInvAvlDate;
    long futureAvlQty;

    public void setFutInvAvlDate(Date futInvAvlDate) {
        this.futInvAvlDate = futInvAvlDate;
    }

    public Date getFutInvAvlDate() {
        return futInvAvlDate;
    }

    public void setFutureAvlQty(long futureAvlQty) {
        this.futureAvlQty = futureAvlQty;
    }

    public long getFutureAvlQty() {
        return futureAvlQty;
    }
}
