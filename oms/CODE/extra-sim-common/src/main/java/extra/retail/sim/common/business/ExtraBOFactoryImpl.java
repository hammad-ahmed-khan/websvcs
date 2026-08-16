package extra.retail.sim.common.business;

import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportRequest;

import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;
import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.item.ItemBinLocation;
import extra.retail.sim.common.reportrequest.ShipmentOrderReportRequest;
import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.common.shipment.AWBRequestLineItem;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.TransferReturnRequestQueryFilter;
import extra.retail.sim.common.store.StoreBinLocation;

public class ExtraBOFactoryImpl implements ExtraBOFactoryInterface {

	@Override
	public ExtraFulfillmentOrderVO createFulfillmentOrderVO() {
		return new ExtraFulfillmentOrderVO();
	}

	@Override
	public ExtraFulfillmentOrderQueryFilter createFulfillmentOrderQueryFilter() {
		return new ExtraFulfillmentOrderQueryFilter();
	}

	@Override
	public BaseLV createBaseLV() {
		return new BaseLV();
	}

	@Override
	public AWBRequest createAWBRequest() {
		return new AWBRequest();
	}

	@Override
	public AWBRequestLineItem createAWBRequestLineItem() {
		return new AWBRequestLineItem();
	}

	@Override
	public UniqueSerialNumber createUniqueSerialNumber() {
		return new UniqueSerialNumber();
	}

	@Override
	public ReportRequest createODDHandOverToCourierReportRequest(String trackingNumber) {
		return new ShipmentOrderReportRequest(trackingNumber);
	}

	@Override
	public ItemBinLocation createItemBinLocation() {
		return new ItemBinLocation();
	}

	@Override
	public StoreBinLocation createStoreBinLocation() {
		return new StoreBinLocation();
	}

	@Override
	public ExtraFulfillmentOrder createFulfillmentOrder() {
		return new ExtraFulfillmentOrder();
	}

	@Override
	public ExtraFulfillmentOrderLineItem createFulfillmentOrderLineItem(StockItem paramStockItem) {
		return new ExtraFulfillmentOrderLineItem(paramStockItem);
	}

	@Override
	public StockRequestReportQueryFilter createStockRequestReportQueryFilter() {
		return new StockRequestReportQueryFilter();
	}

	@Override
	public ReturnRequestItem createFulfillmentOrderLineItem() {
		return new ReturnRequestItem();
	}

	@Override
	public TransferReturnRequestQueryFilter createTransferReturnRequestQueryFilter() {
		return new TransferReturnRequestQueryFilter();
	}
}
