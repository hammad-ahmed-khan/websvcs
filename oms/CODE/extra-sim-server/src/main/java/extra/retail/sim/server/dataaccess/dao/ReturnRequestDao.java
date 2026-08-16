package extra.retail.sim.server.dataaccess.dao;

import java.util.List;

import oracle.retail.sim.common.core.SimServerException;

import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.common.spareparts.ReturnRequestItem;

/**
 * ReturnRequestDao.java
 * aibrahim
 * 2024
 */
public interface ReturnRequestDao {

	void saveReturnRequest(List<ReturnRequestItem> requestItems) throws SimServerException;

	String getReturnRequestStatus(Long returnId) throws SimServerException;

	List<ReturnApprovalVO> getPendingReturnRequest(Long storeId) throws SimServerException;

	void rejectReturn(List<Long> returnIds) throws SimServerException;

	void approveReturn(List<Long> returnIds) throws SimServerException;
}
