package extra.retail.sim.client.core;

import java.math.BigDecimal;

import extra.retail.sim.client.screen.fulfillmentorder.ExtraFulfillmentOrderLineItemWrapper;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;

public class ExtraClientWrapperFactory {

	private static ExtraClientWrapperFactoryInterface factory = getDefaultFactory();

	private static ExtraClientWrapperFactoryInterface getDefaultFactory() {
		return new ExtraClientWrapperFactoryImpl();
	}

	public static ExtraFulfillmentOrderLineItemWrapper createFulfillmentOrderLineItemWrapper(ExtraFulfillmentOrder order, ExtraFulfillmentOrderLineItem lineItem, BigDecimal uomConversionFactor) {
		return factory.createFulfillmentOrderLineItemWrapper(order, lineItem, uomConversionFactor);
	}
}
