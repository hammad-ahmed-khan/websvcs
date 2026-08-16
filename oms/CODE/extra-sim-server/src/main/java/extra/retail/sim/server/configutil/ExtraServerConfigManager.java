package extra.retail.sim.server.configutil;

import extra.retail.sim.server.dataaccess.ExtraDAOFactoryInterface;
import extra.retail.sim.server.process.ExtraProcessFactoryInterface;

import oracle.retail.sim.common.configutil.ConfigManager;
import oracle.retail.sim.common.logging.LogService;

public class ExtraServerConfigManager {

	public static final String DAO_FACTORY_IMPL = "DAO_FACTORY_IMPL";

	public static final String PROCESS_FACTORY_IMPL = "PROCESS_FACTORY_IMPL";

	public static final String DB_JNDI_NAME = "DB_JNDI_NAME";

	public static final String PRINT_AWB_SCRIPT_FILE = "PRINT_AWB_SCRIPT_FILE";

	private static ConfigManager configManager;

	public static ExtraDAOFactoryInterface getDAOFactoryImpl() {
		return configManager.getObject(DAO_FACTORY_IMPL, ExtraDAOFactoryInterface.class);
	}

	static {
		try {
			configManager = new ConfigManager("extra-server.cfg");
		} catch (Throwable throwable) {
			LogService.error(ExtraServerConfigManager.class, "Failed loading extra-server.cfg", throwable);
		}
	}

	public static String getDatabaseJndiName() {
		return configManager.getString(DB_JNDI_NAME);
	}

	public static String getAWBLabelFilePath() {
		return configManager.getString(PRINT_AWB_SCRIPT_FILE);
	}

	public static ExtraProcessFactoryInterface getProcessFactoryImpl() {
		return configManager.getObject(PROCESS_FACTORY_IMPL, ExtraProcessFactoryInterface.class);
	}
}
