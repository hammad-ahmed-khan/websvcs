package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Remote;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.store.Store;

import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;

/**
 * TransferReturnApprovalInterface.java
 * aibrahim
 * 2024
 */
@Remote
public interface SPTransferReturnRequestInterface {

	CompressedObject<List<StockRequestReturnVO>> getPendingApprovalRequest(CompressedObject<String> userNameCompressedObject, CompressedObject<String> reqTypeCompressedObject,
			CompressedObject<Long> storeIdCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	CompressedObject<List<Store>> getUserApprovalLocations(CompressedObject<String> userNameCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	CompressedObject<List<StockRequestReturnVO>> approveTransfer(CompressedObject<List<StockRequestReturnVO>> approvalCompressedObject, CompressedObject<String> reqRetTypeCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	void rejectTransfer(CompressedObject<List<StockRequestReturnVO>> approvalCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	CompressedObject<List<StockRequestReturnVO>> StockRequestReturnVOs(CompressedObject<Long> storeIdCompressedObject, CompressedObject<StockRequestReportQueryFilter> stockRequestReportQueryFilter,
			CompressedObject<SimSession> sessionObject) throws Exception;

	void saveReturnRequest(CompressedObject<List<ReturnRequestItem>> requestItemsCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	CompressedObject<String> getReturnRequestStatus(CompressedObject<Long> returnIdCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	CompressedObject<List<ReturnApprovalVO>> getPendingReturnApproval(CompressedObject<Long> storeIdCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	void approveReturn(CompressedObject<List<Long>> returnIdsCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;

	void rejectReturn(CompressedObject<List<Long>> returnIdsCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception;
}
