package extra.retail.sim.common.fulfillmentorder;

import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.item.StockItem;

public class ExtraFulfillmentOrderLineItem extends FulfillmentOrderLineItem {

	private static final long serialVersionUID = 9172764100437180575L;

	private String serviceRequired;

	public ExtraFulfillmentOrderLineItem(StockItem stockItem) {
		super(stockItem);
	}

	public String getServiceRequired() {
		return this.serviceRequired;
	}

	public void doSetServiceRequired(String paramString) {
		this.setServiceRequired(paramString);
	}

	public void setServiceRequired(String serviceRequired) {
		this.serviceRequired = serviceRequired;
	}
}
