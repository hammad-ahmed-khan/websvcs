package extra.retail.sim.client.screen.fulfillmentorder;

import java.math.BigDecimal;
import java.util.Date;

import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;

public class ExtraFulfillmentOrderLineItemWrapper extends StockLineItemWrapper {

	private ExtraFulfillmentOrder order;

	private ExtraFulfillmentOrderLineItem lineItem;

	private BigDecimal uomConversionFactor;

	public ExtraFulfillmentOrderLineItemWrapper(ExtraFulfillmentOrder paramFulfillmentOrder, ExtraFulfillmentOrderLineItem paramFulfillmentOrderLineItem, BigDecimal paramBigDecimal) {
		this.order = paramFulfillmentOrder;
		this.lineItem = paramFulfillmentOrderLineItem;
		this.uomConversionFactor = paramBigDecimal;
	}

	public Long getId() {
		return this.lineItem.getId();
	}

	public String getItemId() {
		return this.lineItem.getItemId();
	}

	public String getItemDescription() {
		return this.lineItem.getItemDescription();
	}

	public String getComments() {
		return this.lineItem.getComments();
	}

	public String getSubstituteItemId() {
		Long long_ = this.lineItem.getSubstituteLineItemId();
		if (long_ == null)
			return null;
		for (FulfillmentOrderLineItem fulfillmentOrderLineItem : this.order.getLineItems()) {
			if (fulfillmentOrderLineItem.getId().equals(long_))
				return fulfillmentOrderLineItem.getItemId();
		}
		return null;
	}

	public Date getCreatedDate() {
		return this.lineItem.getCreatedDate();
	}

	public Date getUpdatedDate() {
		return this.lineItem.getUpdatedDate();
	}

	public Quantity getDeliveredQty() throws Exception {
		return rationalizeQuantityBasedOnUom(this.lineItem.getDeliveredQuantity());
	}

	public Quantity getOrderedQty() throws Exception {
		return rationalizeQuantityBasedOnUom(this.lineItem.getOrderedQuantity());
	}

	public Quantity getPickedQty() {
		return rationalizeQuantityBasedOnUom(this.lineItem.getPickedQuantity());
	}

	public Quantity getRemainingQty() {
		return rationalizeQuantityBasedOnUom(this.order.getRemainingQty(this.lineItem.getId()));
	}

	public Quantity getCanceledQty() {
		return rationalizeQuantityBasedOnUom(this.lineItem.getCanceledQuantity());
	}

	public StockItem getStockItem() {
		return this.lineItem.getStockItem();
	}

	public Quantity getCaseSize() {
		return (this.lineItem != null && isCasesMode()) ? this.lineItem.getStockItem().getDefaultCaseSize() : Quantity.ONE;
	}

	public String getPreferredUnitOfMeasure() {
		return this.lineItem.getPreferredUom();
	}

	public BigDecimal getPreferredUomConversionFactor() {
		return this.uomConversionFactor;
	}

	public boolean isPropertyModifiable(String paramString) {
		return "unitOfMeasureMode".equals(paramString);
	}

	public String getServiceRequired() {
		return this.lineItem.getServiceRequired();
	}
}