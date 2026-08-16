package extra.retail.sim.server.dataaccess.dao;

import java.util.List;

import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.store.Store;

import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;

/**
 * TransferReturnApprovalDao.java
 * aibrahim
 * 2024
 */
public interface StockRequestReturnDao {

	List<StockRequestReturnVO> getPendingRequestReturn(String userName, String requestType, Long storeId) throws SimServerException;

	List<Store> getUserApprovalLocations(String userName) throws SimServerException;

	void rejectTransfer(List<StockRequestReturnVO> selectedVOs) throws SimServerException;

	void approveTransfer(List<StockRequestReturnVO> selectedVOs) throws SimServerException;

	Integer getTechBucketCode(String technician) throws SimServerException;

	List<StockRequestReturnVO> selectStockRequestReport(long storeId,StockRequestReportQueryFilter stockRequestReportQueryFilter) throws SimServerException;
}
