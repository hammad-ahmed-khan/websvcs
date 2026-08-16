package extra.retail.sim.common.configutil;

import extra.retail.sim.common.business.ExtraBOFactoryInterface;
import extra.retail.sim.service.core.ExtraServiceFactoryInterface;
import extra.retail.sim.service.core.ExtraWebServiceFactoryInterface;

import oracle.retail.sim.common.configutil.ConfigManager;
import oracle.retail.sim.common.logging.LogService;

public class ExtraCommonConfigManager {

	public static final String CLIENT_SERVICE_FACTORY_IMPL = "CLIENT_SERVICE_FACTORY_IMPL";

	public static final String CLIENT_WEB_SERVICE_FACTORY_IMPL = "CLIENT_WEB_SERVICE_FACTORY_IMPL";

	public static final String SERVER_SERVICE_FACTORY_IMPL = "SERVER_SERVICE_FACTORY_IMPL";

	public static final String BO_FACTORY_IMPL = "BO_FACTORY_IMPL";

	private static ConfigManager configManager;

	public static ExtraServiceFactoryInterface getServerServiceFactoryImpl() {
		return configManager.<ExtraServiceFactoryInterface>getObject(SERVER_SERVICE_FACTORY_IMPL, ExtraServiceFactoryInterface.class);
	}

	public static ExtraServiceFactoryInterface getClientServiceFactoryImpl() {
		return configManager.<ExtraServiceFactoryInterface>getObject(CLIENT_SERVICE_FACTORY_IMPL, ExtraServiceFactoryInterface.class);
	}

	public static ExtraBOFactoryInterface getBOFactoryImpl() {
		return configManager.<ExtraBOFactoryInterface>getObject(BO_FACTORY_IMPL, ExtraBOFactoryInterface.class);
	}

	public static ExtraWebServiceFactoryInterface getClientWebServiceFactoryImpl() {
		return configManager.<ExtraWebServiceFactoryInterface>getObject(CLIENT_WEB_SERVICE_FACTORY_IMPL, ExtraWebServiceFactoryInterface.class);
	}

	public static String getString(String property) {
		return configManager.getString(property);
	}
	
	static {
		try {
			configManager = new ConfigManager("extra-common.cfg");
		} catch (Throwable throwable) {
			LogService.error(ExtraCommonConfigManager.class, "Failed loading: extra-common.cfg", throwable);
		}
	}
}
