package extra.retail.sim.client.core;

import java.math.BigDecimal;

import extra.retail.sim.client.screen.fulfillmentorder.ExtraFulfillmentOrderLineItemWrapper;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;

public class ExtraClientWrapperFactoryImpl implements ExtraClientWrapperFactoryInterface {

	@Override
	public ExtraFulfillmentOrderLineItemWrapper createFulfillmentOrderLineItemWrapper(ExtraFulfillmentOrder paramFulfillmentOrder, ExtraFulfillmentOrderLineItem paramFulfillmentOrderLineItem,
			BigDecimal paramBigDecimal) {

		return new ExtraFulfillmentOrderLineItemWrapper(paramFulfillmentOrder, paramFulfillmentOrderLineItem, paramBigDecimal);
	}
}
