package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Stateless;

import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.service.core.ExtraServerServiceFactory;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.core.BaseServiceSessionBean;

@Stateless(mappedName = "BaseLVBean")
public class BaseLVBean extends BaseServiceSessionBean implements BaseLVInterface {

	@Override
	public CompressedObject<List<BaseLV>> findBaseListOfValues(CompressedObject<String> listCode, CompressedObject<SimSession> session) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: BaseLVBean.findBaseListOfValues()");
		}
		try {
			UniversalContext.setSession((SimSession) session.recoverObject());
			return new CompressedObject<List<BaseLV>>(
					ExtraServerServiceFactory.getBaseLVServices().findBaseListOfValues(listCode.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: BaseLVBean.findBaseListOfValues()");
			}
		}
	}
}
