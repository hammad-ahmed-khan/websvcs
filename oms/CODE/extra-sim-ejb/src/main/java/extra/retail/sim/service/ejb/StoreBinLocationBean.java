package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Stateless;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.core.BaseServiceSessionBean;

import extra.retail.sim.service.core.ExtraServerServiceFactory;

@Stateless(mappedName = "StoreBinLocationBean")
public class StoreBinLocationBean extends BaseServiceSessionBean implements StoreBinLocationInterface {

	@Override
	public CompressedObject<List<String>> getStoreBinLocations(CompressedObject<Long> storeIdCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: StoreBinLocationBean.getStoreBinLocations()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionCompressedObject.recoverObject());
			return new CompressedObject<List<String>>(
					ExtraServerServiceFactory.getStoreBinLocationServices().getStoreBinLocations(storeIdCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: StoreBinLocationBean.getStoreBinLocations()");
			}
		}
	}
}
