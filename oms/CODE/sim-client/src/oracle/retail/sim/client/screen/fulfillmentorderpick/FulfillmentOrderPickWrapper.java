package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.util.Date;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickType;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;

/********************************************************************************************************
 * Fulfillment Order Pick Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickWrapper extends Wrapper {

    private FulfillmentOrderPickVO pickVO;

    public FulfillmentOrderPickWrapper(FulfillmentOrderPickVO pickVO) {
        this.pickVO = pickVO;
    }

    public FulfillmentOrderPickVO getFulfillmentOrderPickVO() {
        return pickVO;
    }

    public Long getId() {
        return pickVO.getId();
    }

    public Date getCreateDate() {
        return pickVO.getCreateDate();
    }

    public FulfillmentOrderPickStatus getStatus() {
        return pickVO.getStatus();
    }

    public Quantity getActualQuantity() {
        return pickVO.getActualQuantity();
    }

    public Quantity getTotalQuantity() {
        return pickVO.getTotalQuantity();
    }

    public FulfillmentOrderPickType getType() {
        return pickVO.getType();
    }

    public String getCreateUser() {
        return pickVO.getCreateUser();
    }

    public String getCompleteUser() {
        return pickVO.getCompleteUser();
    }

    public String getUser() {
        if (pickVO.getStatus() == FulfillmentOrderPickStatus.COMPLETED) {
            return pickVO.getCompleteUser();
        } else {
            return pickVO.getCreateUser();
        }
    }
}
