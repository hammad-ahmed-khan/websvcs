package extra.retail.sim.server.dataaccess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oracle.retail.sim.common.logging.LogService;

import extra.retail.sim.server.dataaccess.dao.BaseLVDao;
import extra.retail.sim.server.dataaccess.dao.ExtraConfigurationDao;
import extra.retail.sim.server.dataaccess.dao.ExtraFulfillmentOrderDao;
import extra.retail.sim.server.dataaccess.dao.ItemBinLocationDao;
import extra.retail.sim.server.dataaccess.dao.ReturnRequestDao;
import extra.retail.sim.server.dataaccess.dao.ShipmentOrderDao;
import extra.retail.sim.server.dataaccess.dao.StockRequestReturnDao;
import extra.retail.sim.server.dataaccess.dao.StoreBinLocationDao;
import extra.retail.sim.server.dataaccess.daoimpl.BaseLVOracleDao;
import extra.retail.sim.server.dataaccess.daoimpl.ExtraConfigurationOracleDao;
import extra.retail.sim.server.dataaccess.daoimpl.ExtraFulfillmentOrderOracleDao;
import extra.retail.sim.server.dataaccess.daoimpl.ItemBinLocationOracleDao;
import extra.retail.sim.server.dataaccess.daoimpl.ReturnRequestOracleDao;
import extra.retail.sim.server.dataaccess.daoimpl.ShipmentOrderOracleDao;
import extra.retail.sim.server.dataaccess.daoimpl.StockRequestReturnOracleDao;
import extra.retail.sim.server.dataaccess.daoimpl.StoreBinLocationOracleDao;

public class ExtraDAOFactoryImpl implements ExtraDAOFactoryInterface {

	private static Map<Class<?>, Object> cache = new ConcurrentHashMap<>();

	private static <T> T getDao(Class<T> paramClass) {
		Object object = cache.get(paramClass);
		if (object == null)
			try {
				object = paramClass.newInstance();
				cache.put(paramClass, object);
			} catch (Throwable throwable) {
				LogService.error(ExtraDAOFactoryImpl.class, "Could not create extra DAO: " + paramClass, throwable);
				throw new RuntimeException("Could not create extra DAO: " + paramClass);
			}
		return (T) object;
	}
	
	@Override
	public ExtraFulfillmentOrderDao getFulfillmentOrderDao() {
		return getDao(ExtraFulfillmentOrderOracleDao.class);
	}

	@Override
	public BaseLVDao getBaseLVDao() {
		return getDao(BaseLVOracleDao.class);
	}

	@Override
	public ShipmentOrderDao getShipmentOrderDao() {
		return getDao(ShipmentOrderOracleDao.class);
	}

	@Override
	public ExtraConfigurationDao getExtraConfigurationDao() {
		return getDao(ExtraConfigurationOracleDao.class);
	}

	@Override
	public ItemBinLocationDao getItemBinLocationDao() {
		return getDao(ItemBinLocationOracleDao.class);
	}

	@Override
	public StoreBinLocationDao getStoreBinLocationDao() {
		return getDao(StoreBinLocationOracleDao.class);
	}

	@Override
	public StockRequestReturnDao getTransferReturnApprovalDao() {
		return getDao(StockRequestReturnOracleDao.class);
	}

	@Override
	public ReturnRequestDao getReturnRequestDao() {
		return getDao(ReturnRequestOracleDao.class);
	}
}
