package extra.retail.sim.service.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oracle.retail.sim.common.logging.LogService;

import extra.retail.sim.service.baselv.BaseLVServerServices;
import extra.retail.sim.service.baselv.BaseLVServices;
import extra.retail.sim.service.config.ExtraConfigurationServerServices;
import extra.retail.sim.service.config.ExtraConfigurationServices;
import extra.retail.sim.service.fulfillmentorder.ExtraFulfillmentOrderServerServices;
import extra.retail.sim.service.fulfillmentorder.ExtraFulfillmentOrderServices;
import extra.retail.sim.service.item.ItemBinLocationServerServices;
import extra.retail.sim.service.item.ItemBinLocationServices;
import extra.retail.sim.service.shipment.ShipmentOrderServerServices;
import extra.retail.sim.service.shipment.ShipmentOrderServices;
import extra.retail.sim.service.spareparts.StockRequestReturnServerService;
import extra.retail.sim.service.spareparts.StockRequestReturnServices;
import extra.retail.sim.service.store.StoreBinLocationServerServices;
import extra.retail.sim.service.store.StoreBinLocationServices;

public class ExtraServerServiceFactoryImpl implements ExtraServiceFactoryInterface {

	private static Map<Class<?>, Object> cache = new ConcurrentHashMap<>();

	private static <T> T getService(Class<T> paramClass) {
		Object object = cache.get(paramClass);
		if (object == null)
			try {
				object = paramClass.newInstance();
				cache.put(paramClass, object);
			} catch (Throwable throwable) {
				LogService.error(ExtraServerServiceFactoryImpl.class, "Could not create extra server service: " + paramClass, throwable);
				throw new RuntimeException("Could not create extra server service: " + paramClass);
			}
		return (T) object;
	}

	@Override
	public ExtraFulfillmentOrderServices getFulfillmentOrderServices() {
		return getService(ExtraFulfillmentOrderServerServices.class);
	}

	@Override
	public BaseLVServices getBaseLVServices() {
		return getService(BaseLVServerServices.class);
	}

	@Override
	public ShipmentOrderServices getShipmentOrderServices() {
		return getService(ShipmentOrderServerServices.class);
	}

	@Override
	public ExtraConfigurationServices getExtraConfigurationServices() {
		return getService(ExtraConfigurationServerServices.class);
	}

	@Override
	public ItemBinLocationServices getItemBinLocationServices() {
		return getService(ItemBinLocationServerServices.class);
	}

	@Override
	public StoreBinLocationServices getStoreBinLocationServices() {
		return getService(StoreBinLocationServerServices.class);
	}

	@Override
	public StockRequestReturnServices getStockRequestReturnService() {
		return getService(StockRequestReturnServerService.class);
	}
}
