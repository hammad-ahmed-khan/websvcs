package extra.retail.sim.service.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oracle.retail.sim.common.logging.LogService;

import extra.retail.sim.service.baselv.BaseLVEJBServices;
import extra.retail.sim.service.baselv.BaseLVServices;
import extra.retail.sim.service.config.ExtraConfigurationEJBServices;
import extra.retail.sim.service.config.ExtraConfigurationServices;
import extra.retail.sim.service.fulfillmentorder.ExtraFulfillmentOrderEJBServices;
import extra.retail.sim.service.fulfillmentorder.ExtraFulfillmentOrderServices;
import extra.retail.sim.service.item.ItemBinLocationEJBServices;
import extra.retail.sim.service.item.ItemBinLocationServices;
import extra.retail.sim.service.shipment.ShipmentOrderEJBServices;
import extra.retail.sim.service.shipment.ShipmentOrderServices;
import extra.retail.sim.service.spareparts.StockRequestReturnEJBServices;
import extra.retail.sim.service.spareparts.StockRequestReturnServices;
import extra.retail.sim.service.store.StoreBinLocationEJBServices;
import extra.retail.sim.service.store.StoreBinLocationServices;

public class ExtraClientServiceFactoryImpl implements ExtraServiceFactoryInterface {

	private static Map<Class<?>, Object> cache = new ConcurrentHashMap<Class<?>, Object>();

	private static <T> T getService(Class<T> paramClass) {
		Object object = cache.get(paramClass);
		if (object == null)
			try {
				object = paramClass.newInstance();
				cache.put(paramClass, object);
			} catch (Throwable throwable) {
				LogService.error(ExtraClientServiceFactoryImpl.class, "Could not create extra client service: " + paramClass, throwable);
				throw new RuntimeException("Could not create extra client service: " + paramClass);
			}
		return (T) object;
	}

	@Override
	public ExtraFulfillmentOrderServices getFulfillmentOrderServices() {
		return getService(ExtraFulfillmentOrderEJBServices.class);
	}

	@Override
	public BaseLVServices getBaseLVServices() {
		return getService(BaseLVEJBServices.class);
	}

	@Override
	public ShipmentOrderServices getShipmentOrderServices() {
		return getService(ShipmentOrderEJBServices.class);
	}

	@Override
	public ExtraConfigurationServices getExtraConfigurationServices() {
		return getService(ExtraConfigurationEJBServices.class);
	}

	@Override
	public ItemBinLocationServices getItemBinLocationServices() {
		return getService(ItemBinLocationEJBServices.class);
	}

	@Override
	public StoreBinLocationServices getStoreBinLocationServices() {
		return getService(StoreBinLocationEJBServices.class);
	}

	@Override
	public StockRequestReturnServices getStockRequestReturnService() {
		return getService(StockRequestReturnEJBServices.class);
	}
}
