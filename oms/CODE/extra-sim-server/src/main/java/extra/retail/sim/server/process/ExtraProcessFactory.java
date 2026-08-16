package extra.retail.sim.server.process;

import extra.retail.sim.server.configutil.ExtraServerConfigManager;

/**
 * ExtraProcessFactory.java
 * aibrahim
 * 2024
 */
public class ExtraProcessFactory {

	private static ExtraProcessFactoryInterface factory = createDefaultFactory();

	private static ExtraProcessFactoryInterface createDefaultFactory() {
		return ExtraServerConfigManager.getProcessFactoryImpl();
	}

	public static ShipmentOrderProcess getShipmentOrderProcess() {
		return factory.getShipmentOrderProcess();
	}
}
