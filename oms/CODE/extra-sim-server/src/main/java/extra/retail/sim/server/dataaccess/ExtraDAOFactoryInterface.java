package extra.retail.sim.server.dataaccess;

import extra.retail.sim.server.dataaccess.dao.BaseLVDao;
import extra.retail.sim.server.dataaccess.dao.ExtraConfigurationDao;
import extra.retail.sim.server.dataaccess.dao.ExtraFulfillmentOrderDao;
import extra.retail.sim.server.dataaccess.dao.ShipmentOrderDao;
import extra.retail.sim.server.dataaccess.dao.StoreBinLocationDao;
import extra.retail.sim.server.dataaccess.dao.StockRequestReturnDao;
import extra.retail.sim.server.dataaccess.dao.ItemBinLocationDao;
import extra.retail.sim.server.dataaccess.dao.ReturnRequestDao;

public interface ExtraDAOFactoryInterface {

	ExtraFulfillmentOrderDao getFulfillmentOrderDao();

	BaseLVDao getBaseLVDao();

	ShipmentOrderDao getShipmentOrderDao();

	ExtraConfigurationDao getExtraConfigurationDao();

	ItemBinLocationDao getItemBinLocationDao();

	StoreBinLocationDao getStoreBinLocationDao();

	StockRequestReturnDao getTransferReturnApprovalDao();

	ReturnRequestDao getReturnRequestDao();
}
