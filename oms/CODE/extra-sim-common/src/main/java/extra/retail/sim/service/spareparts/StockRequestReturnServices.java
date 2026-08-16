package extra.retail.sim.service.spareparts;

import java.util.List;

import oracle.retail.sim.common.store.Store;

import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;

/**
 * StockRequestReturnServices.java
 * aibrahim
 * 2024
 */
public abstract class StockRequestReturnServices {

	public abstract List<StockRequestReturnVO> getPendingRequestReturn(String userName, String requestType, Long storeId) throws Exception;

	public abstract List<Store> getUserApprovalLocations(String userName) throws Exception;

	public abstract void rejectRequest(List<StockRequestReturnVO> selectedVOs) throws Exception;

	public abstract List<StockRequestReturnVO> approveRequest(List<StockRequestReturnVO> selectedVOs, String reqRetType) throws Exception;

	public abstract List<StockRequestReturnVO> StockRequestReturnVOs(Long storeId , StockRequestReportQueryFilter stockRequestReportQueryFilter) throws Exception;

	public abstract void saveReturnRequest(List<ReturnRequestItem> requestItems) throws Exception;

	public abstract String getReturnRequestStatus(Long returnId) throws Exception;

	public abstract List<ReturnApprovalVO> getPendingReturnApproval(Long storeId) throws Exception;

	public abstract void approveReturn(List<Long> returnIds) throws Exception;

	public abstract void rejectReturn(List<Long> returnIds) throws Exception;
}
