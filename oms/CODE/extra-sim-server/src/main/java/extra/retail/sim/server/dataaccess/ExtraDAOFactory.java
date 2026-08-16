package extra.retail.sim.server.dataaccess;

import extra.retail.sim.server.configutil.ExtraServerConfigManager;
import extra.retail.sim.server.dataaccess.dao.BaseLVDao;
import extra.retail.sim.server.dataaccess.dao.ExtraConfigurationDao;
import extra.retail.sim.server.dataaccess.dao.ExtraFulfillmentOrderDao;
import extra.retail.sim.server.dataaccess.dao.ItemBinLocationDao;
import extra.retail.sim.server.dataaccess.dao.ReturnRequestDao;
import extra.retail.sim.server.dataaccess.dao.ShipmentOrderDao;
import extra.retail.sim.server.dataaccess.dao.StockRequestReturnDao;
import extra.retail.sim.server.dataaccess.dao.StoreBinLocationDao;

public class ExtraDAOFactory {

	private static ExtraDAOFactoryInterface factory = createDefaultFactory();

	public static ExtraDAOFactoryInterface getFactory() {
		return factory;
	}

	public static void setFactory(ExtraDAOFactoryInterface paramDAOFactoryInterface) {
		if (paramDAOFactoryInterface == null)
			throw new IllegalArgumentException("DAOFactory cannot be null!");
		factory = paramDAOFactoryInterface;
	}

	private static ExtraDAOFactoryInterface createDefaultFactory() {
		return ExtraServerConfigManager.getDAOFactoryImpl();
	}

	public static ExtraFulfillmentOrderDao getFulfillmentOrderDao() {
		return factory.getFulfillmentOrderDao();
	}

	public static BaseLVDao getBaseLVDao() {
		return factory.getBaseLVDao();
	}

	public static ShipmentOrderDao getShipmentOrderDao() {
		return factory.getShipmentOrderDao();
	}

	public static ExtraConfigurationDao getExtraConfigurationDao() {
		return factory.getExtraConfigurationDao();
	}

	public static ItemBinLocationDao getItemBinLocationDao() {
		return factory.getItemBinLocationDao();
	}

	public static StoreBinLocationDao getStoreBinLocationDao() {
		return factory.getStoreBinLocationDao();
	}

	public static StockRequestReturnDao getTransferReturnApprovalDao() {
		return factory.getTransferReturnApprovalDao();
	}

	public static ReturnRequestDao getReturnRequestDao() {
		return factory.getReturnRequestDao();
	}
}
