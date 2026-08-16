package extra.retail.sim.client.screen.imeifulfillmentorderdelivery;


import java.util.Date;

import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;

public class IMEIFulfillmentOrderDeliveryWrapper extends Wrapper {

    private FulfillmentOrderDeliveryVO fulfillmentOrderDeliveryVO;

    public IMEIFulfillmentOrderDeliveryWrapper(FulfillmentOrderDeliveryVO vo) {
        fulfillmentOrderDeliveryVO = vo;
    }

    /**
     * Returns the FulfillmentOrderDeliveryVO for this wrapper.
     * @return The FulfillmentOrderDeliveryVO for this wrapper.
     */
    public FulfillmentOrderDeliveryVO getFulfillmentOrderDeliveryVO() {
        return fulfillmentOrderDeliveryVO;
    }

    /**
     * Returns the delivery ID from the FulfillmentOrderDeliveryVO.
     * @return The delivery ID from the FulfillmentOrderDeliveryVO.
     */
    public Long getDeliveryId() {
        return fulfillmentOrderDeliveryVO.getId();
    }

    /**
     * Returns the delivery status of the FulfillmentOrderDeliveryVO.
     * @return The delivery Status of the FulfillmentOrderDeliveryVO.
     */
    public FulfillmentOrderDeliveryStatus getStatus() {
        return fulfillmentOrderDeliveryVO.getStatus();
    }

    /**
     * Returns the create user from the FulfillmentOrderDeliveryVO.
     * @return The create user from the FulfillmentOrderDeliveryVO.
     */
    public String getCreateUser() {
        return fulfillmentOrderDeliveryVO.getCreateUser();
    }

    /**
     * Returns the create date from the FulfillmentOrderDeliveryVO.
     * @return The create date from the FulfillmentOrderDeliveryVO.
     */
    public Date getCreateDate() {
        return fulfillmentOrderDeliveryVO.getCreateDate();
    }
}
