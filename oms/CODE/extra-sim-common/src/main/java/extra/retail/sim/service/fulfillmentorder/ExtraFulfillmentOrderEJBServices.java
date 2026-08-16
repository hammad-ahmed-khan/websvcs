package extra.retail.sim.service.fulfillmentorder;

import java.util.List;

import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;
import extra.retail.sim.service.ejb.ExtraFulfillmentOrderInterface;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;

public class ExtraFulfillmentOrderEJBServices extends ExtraFulfillmentOrderServices {
	private ExtraFulfillmentOrderInterface lookup() throws Exception {
		try {
			return (ExtraFulfillmentOrderInterface) JndiServiceManager.cachedLookup("ExtraFulfillmentOrderBean", ExtraFulfillmentOrderInterface.class);
		} catch (Throwable throwable) {
			throw new DowntimeException("An error occurred accessing FulfillmentOrderServices. Please contact your system administrator.", throwable);
		}
	}

	private void removeCache() {
		JndiServiceManager.removeCache("ExtraFulfillmentOrderBean");
	}

	public List<ExtraFulfillmentOrderVO> findFulfillmentOrderVOs(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter) throws Exception {
		CompressedObject<SimSession> compressedObject1 = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<ExtraFulfillmentOrderQueryFilter> compressedObject2 = new CompressedObject<ExtraFulfillmentOrderQueryFilter>(paramFulfillmentOrderQueryFilter);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ExtraFulfillmentOrderBean.findFulfillmentOrderVOs(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ExtraFulfillmentOrderInterface fulfillmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<ExtraFulfillmentOrderVO>> compressedObject = fulfillmentOrderInterface.findFulfillmentOrderVOs(compressedObject2, compressedObject1);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ExtraFulfillmentOrderBean.findFulfillmentOrderVOs(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ExtraFulfillmentOrderBean.findFulfillmentOrderVOs(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (List<ExtraFulfillmentOrderVO>) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing FulfillmentOrderServices. Please contact your system administrator.", throwable);
	}
	
	
	public ExtraFulfillmentOrder readFulfillmentOrder(Long id) throws Exception {
		CompressedObject<SimSession> compressedObject1 = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> compressedObject2 = new CompressedObject<Long>(id);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "FulfillmentOrderBean.readFulfillmentOrder(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ExtraFulfillmentOrderInterface fulfillmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<ExtraFulfillmentOrder> compressedObject = fulfillmentOrderInterface.readFulfillmentOrder(compressedObject2, compressedObject1);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "FulfillmentOrderBean.readFulfillmentOrder(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "FulfillmentOrderBean.readFulfillmentOrder(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (ExtraFulfillmentOrder) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing FulfillmentOrderServices. Please contact your system administrator.", throwable);
	}

}
