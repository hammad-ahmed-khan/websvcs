package extra.retail.sim.service.core;

import extra.retail.sim.common.configutil.ExtraCommonConfigManager;
import extra.retail.sim.service.baselv.BaseLVServices;
import extra.retail.sim.service.fulfillmentorder.ExtraFulfillmentOrderServices;
import oracle.retail.sim.common.core.JvmLocation;

public class ExtraNativeServiceFactory {
	private static ExtraServiceFactoryInterface factory = getDefaultFactory();

	public static ExtraServiceFactoryInterface getFactory() {
		return factory;
	}

	public static void setFactory(ExtraServiceFactoryInterface paramServiceFactoryInterface) {
		if (paramServiceFactoryInterface == null)
			throw new IllegalArgumentException("ServiceFactory cannot be null!");
		factory = paramServiceFactoryInterface;
	}

	private static ExtraServiceFactoryInterface getDefaultFactory() {
		return JvmLocation.isServer() ? ExtraCommonConfigManager.getServerServiceFactoryImpl() : ExtraCommonConfigManager.getClientServiceFactoryImpl();
	}

	public static ExtraFulfillmentOrderServices getFulfillmentOrderServices() {
		return factory.getFulfillmentOrderServices();
	}

	public static BaseLVServices getBaseLVServices() {
		return factory.getBaseLVServices();
	}
}
