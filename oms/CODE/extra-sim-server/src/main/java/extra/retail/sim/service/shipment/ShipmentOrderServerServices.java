package extra.retail.sim.service.shipment;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;

import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.common.shipment.ShipmentOrderMessage;
import extra.retail.sim.server.dataaccess.ExtraDAOFactory;
import extra.retail.sim.server.process.ExtraProcessFactory;

/**
 * ShipmentOrderServerServices.java
 * aibrahim
 * 2023
 */
public class ShipmentOrderServerServices extends ShipmentOrderServices {

	@Override
	public FulfillmentOrderDelivery getPendingDeliveryItems(Long fulfillmentOrderId) throws Exception {
		return ExtraDAOFactory.getShipmentOrderDao().getPendingDeliveryItems(fulfillmentOrderId);
	}

	@Override
	public String requestAWB(AWBRequest awbRequest) throws SimServerException {
		return ExtraDAOFactory.getShipmentOrderDao().requestAWB(awbRequest);
	}

	@Override
	public void cancelAWB(List<String> trackingNumbers) throws SimServerException {
		ExtraDAOFactory.getShipmentOrderDao().cancelAWB(trackingNumbers);
	}

	@Override
	public void printAWB(Long storeId, String trackingNumber) throws Exception {
		Entry<String, List<String>> queEntries = ExtraDAOFactory.getShipmentOrderDao().getQueueDetail(storeId, trackingNumber);
		if (queEntries == null) {
			throw new BusinessException(ShipmentOrderMessage.NO_LABEL_PATH_FOUND);
		}
		ExtraProcessFactory.getShipmentOrderProcess().printAWBLabels(queEntries);
	}

	@Override
	public List<String> lookupIMEIEnableItems(Long storeId, Collection<String> items) throws SimServerException {
		return ExtraDAOFactory.getShipmentOrderDao().lookupIMEIEnableItems(storeId, items);
	}

	@Override
	public AWBRequest getAWBDetail(Long pickId) throws Exception {
		return ExtraDAOFactory.getShipmentOrderDao().getAWBDetail(pickId);
	}

	@Override
	public List<UniqueSerialNumber> getIMEIDetail(Collection<Long> dlvLineItemIds) throws Exception {
		return ExtraDAOFactory.getShipmentOrderDao().getIMEIDetail(dlvLineItemIds);
	}

	@Override
	public void updateHTC(String awbNumber) throws Exception {
		ExtraDAOFactory.getShipmentOrderDao().updateHTC(awbNumber);
	}
}
