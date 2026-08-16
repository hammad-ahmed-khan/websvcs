package extra.retail.sim.service.ejb;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.core.BaseServiceSessionBean;

import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.service.core.ExtraServerServiceFactory;

/**
 * ShipmentOrderBean.java
 * aibrahim
 * 2023
 */
@Stateless(mappedName = "ShipmentOrderBean")
public class ShipmentOrderBean extends BaseServiceSessionBean implements ShipmentOrderInterface {

	@Override
	public CompressedObject<FulfillmentOrderDelivery> getPendingDeliveryItems(CompressedObject<Long> fulfillmentOrderId, CompressedObject<SimSession> paramCompressedObjectSession) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.getPendingDeliveryItems()");
		}
		try {
			UniversalContext.setSession((SimSession) paramCompressedObjectSession.recoverObject());
			return new CompressedObject<FulfillmentOrderDelivery>(
					ExtraServerServiceFactory.getShipmentOrderServices().getPendingDeliveryItems(fulfillmentOrderId.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.getPendingDeliveryItems()");
			}
		}
	}

	@Override
	public CompressedObject<AWBRequest> getAWBDetail(CompressedObject<Long> pickId, CompressedObject<SimSession> paramCompressedObjectSession) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.getAWBDetail()");
		}
		try {
			UniversalContext.setSession((SimSession) paramCompressedObjectSession.recoverObject());
			return new CompressedObject<AWBRequest>(
					ExtraServerServiceFactory.getShipmentOrderServices().getAWBDetail(pickId.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.getAWBDetail()");
			}
		}
	}

	@Override
	public CompressedObject<String> requestAWB(CompressedObject<AWBRequest> awbRequestCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.requestAWB()");
		}
		try {
			UniversalContext.setSession((SimSession) simSessionCompressedObject.recoverObject());
			return new CompressedObject<String>(
					ExtraServerServiceFactory.getShipmentOrderServices().requestAWB(awbRequestCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.requestAWB()");
			}
		}
	}

	@Override
	public CompressedObject<Void> cancelAWB(CompressedObject<List<String>> awbCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.cancelAWB()");
		}
		try {
			UniversalContext.setSession((SimSession) simSessionCompressedObject.recoverObject());
			ExtraServerServiceFactory.getShipmentOrderServices().cancelAWB(awbCompressedObject.recoverObject());
			return new CompressedObject<Void>(null);
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.cancelAWB()");
			}
		}
	}

	@Override
	public CompressedObject<Void> printAWB(CompressedObject<Long> storeIdCompressedObject, CompressedObject<String> awbCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.printAWB()");
		}
		try {
			UniversalContext.setSession((SimSession) simSessionCompressedObject.recoverObject());
			ExtraServerServiceFactory.getShipmentOrderServices().printAWB(storeIdCompressedObject.recoverObject(), awbCompressedObject.recoverObject());
			return new CompressedObject<Void>(null);
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.cancelAWB()");
			}
		}
	}

	@Override
	public CompressedObject<List<String>> lookupIMEIEnableItems(CompressedObject<Long> storeId, CompressedObject<Collection<String>> itemsCompressedObject,
			CompressedObject<SimSession> simSessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.lookupIMEIEnableItems()");
		}
		try {
			UniversalContext.setSession((SimSession) simSessionCompressedObject.recoverObject());
			return new CompressedObject<List<String>>(ExtraServerServiceFactory.getShipmentOrderServices().lookupIMEIEnableItems(storeId.recoverObject(), itemsCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.lookupIMEIEnableItems()");
			}
		}
	}

	@Override
	public CompressedObject<List<UniqueSerialNumber>> getIMEIDetail(CompressedObject<Collection<Long>> dlvLineItemIds, CompressedObject<SimSession> simSessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.getIMEIDetail(Collection<Long>> dlvLineItemIds)");
		}
		try {
			UniversalContext.setSession((SimSession) simSessionCompressedObject.recoverObject());
			return new CompressedObject<List<UniqueSerialNumber>>(ExtraServerServiceFactory.getShipmentOrderServices().getIMEIDetail(dlvLineItemIds.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.getIMEIDetail(Collection<Long>> dlvLineItemIds)");
			}
		}
	}

	@Override
	public CompressedObject<Void> updateHTC(CompressedObject<String> awbCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ShipmentOrderBean.updateHTC()");
		}
		try {
			UniversalContext.setSession((SimSession) simSessionCompressedObject.recoverObject());
			ExtraServerServiceFactory.getShipmentOrderServices().updateHTC(awbCompressedObject.recoverObject());
			return new CompressedObject<Void>(null);
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ShipmentOrderBean.updateHTC()");
			}
		}
	}
}
