package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Stateless;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.core.BaseServiceSessionBean;

import extra.retail.sim.service.core.ExtraServerServiceFactory;

/**
 * ExtraConfigurationBean.java
 * aibrahim
 * 2024
 */
@Stateless(mappedName = "ExtraConfigurationBean")
public class ExtraConfigurationBean extends BaseServiceSessionBean implements ExtraConfigurationInterface {

	@Override
	public CompressedObject<List<Long>> getTsfRestrictStores(CompressedObject<SimSession> session) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ExtraConfigurationBean.getTsfRestrictStores()");
		}
		try {
			UniversalContext.setSession((SimSession) session.recoverObject());
			return new CompressedObject<List<Long>>(
					ExtraServerServiceFactory.getExtraConfigurationServices().getTsfRestrictStores());
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ExtraConfigurationBean.getTsfRestrictStores()");
			}
		}
	}
}
