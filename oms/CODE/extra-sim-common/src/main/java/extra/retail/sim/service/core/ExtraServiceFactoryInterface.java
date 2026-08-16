package extra.retail.sim.service.core;

import extra.retail.sim.service.baselv.BaseLVServices;
import extra.retail.sim.service.config.ExtraConfigurationServices;
import extra.retail.sim.service.fulfillmentorder.ExtraFulfillmentOrderServices;
import extra.retail.sim.service.item.ItemBinLocationServices;
import extra.retail.sim.service.shipment.ShipmentOrderServices;
import extra.retail.sim.service.spareparts.StockRequestReturnServices;
import extra.retail.sim.service.store.StoreBinLocationServices;

public interface ExtraServiceFactoryInterface {

	ExtraFulfillmentOrderServices getFulfillmentOrderServices();

	BaseLVServices getBaseLVServices();

	ShipmentOrderServices getShipmentOrderServices();

	ExtraConfigurationServices getExtraConfigurationServices();

	ItemBinLocationServices getItemBinLocationServices();

	StoreBinLocationServices getStoreBinLocationServices();

	StockRequestReturnServices getStockRequestReturnService();
}
