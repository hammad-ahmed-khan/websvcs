package extra.retail.sim.service.core;

import oracle.retail.sim.common.core.JvmLocation;

import extra.retail.sim.common.configutil.ExtraCommonConfigManager;
import extra.retail.sim.service.baselv.BaseLVServices;
import extra.retail.sim.service.config.ExtraConfigurationServices;
import extra.retail.sim.service.fulfillmentorder.ExtraFulfillmentOrderServices;
import extra.retail.sim.service.item.ItemBinLocationServices;
import extra.retail.sim.service.shipment.ShipmentOrderServices;
import extra.retail.sim.service.spareparts.StockRequestReturnServices;
import extra.retail.sim.service.store.StoreBinLocationServices;

public class ExtraClientServiceFactory {
	private static ExtraServiceFactoryInterface factory = getDefaultFactory();

	public static ExtraServiceFactoryInterface getFactory() {
		return factory;
	}

	public static void setFactory(ExtraServiceFactoryInterface paramServiceFactoryInterface) {
		if (paramServiceFactoryInterface == null)
			throw new IllegalArgumentException("ExtraServiceFactory cannot be null!");
		factory = paramServiceFactoryInterface;
	}

	private static ExtraServiceFactoryInterface getDefaultFactory() {
		return JvmLocation.isServer() ? ExtraCommonConfigManager.getClientServiceFactoryImpl() : ExtraNativeServiceFactory.getFactory();
	}

	public static ExtraFulfillmentOrderServices getFulfillmentOrderServices() {
		return factory.getFulfillmentOrderServices();
	}

	public static BaseLVServices getBaseLVServices() {
		return factory.getBaseLVServices();
	}

	public static ShipmentOrderServices getShipmentOrderServices() {
		return factory.getShipmentOrderServices();
	}

	public static ExtraConfigurationServices getExtraConfigurationServices() {
		return factory.getExtraConfigurationServices();
	}

	public static ItemBinLocationServices getItemBinLocationServices() {
		return factory.getItemBinLocationServices();
	}

	public static StoreBinLocationServices getStoreBinLocationServices() {
		return factory.getStoreBinLocationServices();
	}

	public static StockRequestReturnServices getStockRequestReturnService() {
		return factory.getStockRequestReturnService();
	}
}
