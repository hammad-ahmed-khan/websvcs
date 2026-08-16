package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import java.util.Date;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickStatus;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickWrapper extends Wrapper {

    private FulfillmentOrderReversePickVO reversePickVO;

    public FulfillmentOrderReversePickWrapper(FulfillmentOrderReversePickVO reversePickVO) {
        this.reversePickVO = reversePickVO;
    }

    public FulfillmentOrderReversePickVO getFulfillmentOrderReversePickVO() {
        return reversePickVO;
    }

    public Long getId() {
        return reversePickVO.getId();
    }
    
    public String getIdAsString() {
        if (reversePickVO.getId() != null) {
            return reversePickVO.getId().toString();
        }
        return null;
    }

    public Date getCreateDate() {
        return reversePickVO.getCreateDate();
    }

    public FulfillmentOrderReversePickStatus getStatus() {
        return reversePickVO.getStatus();
    }

    public String getCreateUser() {
        return reversePickVO.getCreateUser();
    }
}
