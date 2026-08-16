package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Stateless;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.BaseServiceSessionBean;

import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import extra.retail.sim.service.core.ExtraServerServiceFactory;

/**
 * TransferReturnApprovalBean.java
 * aibrahim
 * 2024
 */
@Stateless(mappedName = "SPTransferReturnRequestBean")
public class SPTransferReturnRequestBean extends BaseServiceSessionBean implements SPTransferReturnRequestInterface {

	@Override
	public CompressedObject<List<StockRequestReturnVO>> getPendingApprovalRequest(CompressedObject<String> userNameCompressedObject, CompressedObject<String> reqTypeCompressedObject,
			CompressedObject<Long> storeIdCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.getPendingApprovalRequest()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionCompressedObject.recoverObject());
			return new CompressedObject<List<StockRequestReturnVO>>(ExtraServerServiceFactory.getTransferReturnApprovalServices().getPendingRequestReturn(userNameCompressedObject.recoverObject(),
					reqTypeCompressedObject.recoverObject(), storeIdCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.getPendingApprovalRequest()");
			}
		}
	}

	@Override
	public CompressedObject<List<Store>> getUserApprovalLocations(CompressedObject<String> userNameCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.getUserApprovalLocations()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionCompressedObject.recoverObject());
			return new CompressedObject<List<Store>>(ExtraServerServiceFactory.getTransferReturnApprovalServices().getUserApprovalLocations(userNameCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.getUserApprovalLocations()");
			}
		}
	}

	@Override
	public CompressedObject<List<StockRequestReturnVO>> approveTransfer(CompressedObject<List<StockRequestReturnVO>> approvalCompressedObject, CompressedObject<String> reqRetTypeCompressedObject,
			CompressedObject<SimSession> sessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.approveTransfer()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionCompressedObject.recoverObject());
			return new CompressedObject<List<StockRequestReturnVO>>(
					ExtraServerServiceFactory.getTransferReturnApprovalServices().approveRequest(approvalCompressedObject.recoverObject(), reqRetTypeCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.approveTransfer()");
			}
		}
	}

	@Override
	public void rejectTransfer(CompressedObject<List<StockRequestReturnVO>> approvalCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.rejectTransfer()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionObject.recoverObject());
			ExtraServerServiceFactory.getTransferReturnApprovalServices().rejectRequest(approvalCompressedObject.recoverObject());
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.rejectTransfer()");
			}
		}
	}

	@Override
	public CompressedObject<List<StockRequestReturnVO>> StockRequestReturnVOs(CompressedObject<Long> storeIdCompressedObject,
			CompressedObject<StockRequestReportQueryFilter> stockRequestReportQueryFilter, CompressedObject<SimSession> sessionObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.getPendingApprovalRequest()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionObject.recoverObject());
			return new CompressedObject<List<StockRequestReturnVO>>(
					ExtraServerServiceFactory.getTransferReturnApprovalServices().StockRequestReturnVOs(storeIdCompressedObject.recoverObject(), stockRequestReportQueryFilter.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.getPendingApprovalRequest()");
			}
		}
	}

	@Override
	public void saveReturnRequest(CompressedObject<List<ReturnRequestItem>> requestItemsCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.saveReturnRequest()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionObject.recoverObject());
			ExtraServerServiceFactory.getTransferReturnApprovalServices().saveReturnRequest(requestItemsCompressedObject.recoverObject());
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.saveReturnRequest()");
			}
		}
	}

	@Override
	public CompressedObject<String> getReturnRequestStatus(CompressedObject<Long> returnIdCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.getPendingApprovalRequest()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionObject.recoverObject());
			return new CompressedObject<String>(ExtraServerServiceFactory.getTransferReturnApprovalServices().getReturnRequestStatus(returnIdCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.getPendingApprovalRequest()");
			}
		}
	}

	@Override
	public CompressedObject<List<ReturnApprovalVO>> getPendingReturnApproval(CompressedObject<Long> storeIdCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: SPTransferReturnRequestBean.getPendingApprovalRequest()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionCompressedObject.recoverObject());
			return new CompressedObject<List<ReturnApprovalVO>>(ExtraServerServiceFactory.getTransferReturnApprovalServices().getPendingReturnApproval(storeIdCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: SPTransferReturnRequestBean.getPendingApprovalRequest()");
			}
		}
	}

	@Override
	public void approveReturn(CompressedObject<List<Long>> returnIdsCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.approveReturn()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionObject.recoverObject());
			ExtraServerServiceFactory.getTransferReturnApprovalServices().approveReturn(returnIdsCompressedObject.recoverObject());
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.approveReturn()");
			}
		}
	}

	@Override
	public void rejectReturn(CompressedObject<List<Long>> returnIdsCompressedObject, CompressedObject<SimSession> sessionObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: TransferReturnApprovalBean.rejectReturn()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionObject.recoverObject());
			ExtraServerServiceFactory.getTransferReturnApprovalServices().rejectReturn(returnIdsCompressedObject.recoverObject());
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: TransferReturnApprovalBean.rejectReturn()");
			}
		}
	}
}
