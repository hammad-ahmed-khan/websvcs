package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Stateless;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.core.BaseServiceSessionBean;

import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;
import extra.retail.sim.service.core.ExtraServerServiceFactory;

@Stateless(mappedName = "ExtraFulfillmentOrderBean")
public class ExtraFulfillmentOrderBean extends BaseServiceSessionBean implements ExtraFulfillmentOrderInterface {

	@Override
	public CompressedObject<List<ExtraFulfillmentOrderVO>> findFulfillmentOrderVOs(CompressedObject<ExtraFulfillmentOrderQueryFilter> paramCompressedObject,
			CompressedObject<SimSession> paramCompressedObjectSession) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: FulfillmentOrderBean.findFulfillmentOrderVOs()");
		}
		try {
			UniversalContext.setSession((SimSession) paramCompressedObjectSession.recoverObject());
			return new CompressedObject<List<ExtraFulfillmentOrderVO>>(ExtraServerServiceFactory.getFulfillmentOrderServices().findFulfillmentOrderVOs(paramCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: FulfillmentOrderBean.findFulfillmentOrderVOs()");
			}
		}
	}

	@Override
	public CompressedObject<ExtraFulfillmentOrder> readFulfillmentOrder(CompressedObject<Long> compressedObjectId, CompressedObject<SimSession> compressedObjectSession) throws Exception {

		long l = System.currentTimeMillis();

		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ExtraFulfillmentOrderBean.findFulfillmentOrderVOs()");
		}

		try {
			UniversalContext.setSession((SimSession) compressedObjectSession.recoverObject());
			return new CompressedObject<ExtraFulfillmentOrder>(ExtraServerServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(compressedObjectId.recoverObject()));

		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);

		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ExtraFulfillmentOrderBean.readFulfillmentOrder()");
			}
		}
	}
}
