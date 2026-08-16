package extra.retail.sim.service.core;

import extra.retail.sim.common.configutil.ExtraCommonConfigManager;
import extra.retail.sim.service.imei.ExtraIMEIServices;

public class ExtraWSClientServiceFactory {
	private static ExtraWebServiceFactoryInterface factory = getDefaultFactory();

	public static ExtraWebServiceFactoryInterface getFactory() {
		return factory;
	}

	public static void setFactory(ExtraWebServiceFactoryInterface paramServiceFactoryInterface) {
		if (paramServiceFactoryInterface == null)
			throw new IllegalArgumentException("ExtraWebServiceFactory cannot be null!");
		factory = paramServiceFactoryInterface;
	}

	private static ExtraWebServiceFactoryInterface getDefaultFactory() {
		return ExtraCommonConfigManager.getClientWebServiceFactoryImpl();
	}

	public static ExtraIMEIServices getIMEIServices() {
		return factory.getIMEIServices();
	}
}
