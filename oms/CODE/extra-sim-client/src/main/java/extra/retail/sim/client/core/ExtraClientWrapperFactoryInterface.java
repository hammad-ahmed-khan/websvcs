package extra.retail.sim.client.core;

import java.math.BigDecimal;

import extra.retail.sim.client.screen.fulfillmentorder.ExtraFulfillmentOrderLineItemWrapper;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;

public interface ExtraClientWrapperFactoryInterface {

	ExtraFulfillmentOrderLineItemWrapper createFulfillmentOrderLineItemWrapper(ExtraFulfillmentOrder paramFulfillmentOrder, ExtraFulfillmentOrderLineItem paramFulfillmentOrderLineItem,
			BigDecimal paramBigDecimal);
}
