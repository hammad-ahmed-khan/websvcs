package extra.retail.sim.service.spareparts;

import java.util.List;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;

import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import extra.retail.sim.service.ejb.SPTransferReturnRequestInterface;

/**
 * StockRequestReturnEJBServices.java aibrahim 2024
 */
public class StockRequestReturnEJBServices extends StockRequestReturnServices {

	private SPTransferReturnRequestInterface lookup() throws Exception {
		try {
			return (SPTransferReturnRequestInterface) JndiServiceManager.cachedLookup("SPTransferReturnRequestBean", SPTransferReturnRequestInterface.class);
		} catch (Throwable throwable) {
			throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
		}
	}

	private void removeCache() {
		JndiServiceManager.removeCache("SPTransferReturnRequestBean");
	}

	@Override
	public List<StockRequestReturnVO> getPendingRequestReturn(String userName, String requestType, Long storeId) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<String> userNameCompressedObject = new CompressedObject<String>(userName);
		CompressedObject<String> reqTypeCompressedObject = new CompressedObject<String>(requestType);
		CompressedObject<Long> storeIdCompressedObject = new CompressedObject<Long>(storeId);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.getPendingApprovalRequest(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { userNameCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<StockRequestReturnVO>> compressedObject = transferReturnApprovalInterface.getPendingApprovalRequest(userNameCompressedObject, reqTypeCompressedObject,
						storeIdCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.getPendingApprovalRequest(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.getPendingApprovalRequest(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return compressedObject.recoverObject();
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}

	@Override
	public List<Store> getUserApprovalLocations(String userName) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<String> userNameCompressedObject = new CompressedObject<String>(userName);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.getUserApprovalLocations(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { userNameCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<Store>> compressedObject = transferReturnApprovalInterface.getUserApprovalLocations(userNameCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.getUserApprovalLocations(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.getUserApprovalLocations(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return compressedObject.recoverObject();
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}

	@Override
	public void rejectRequest(List<StockRequestReturnVO> selectedVOs) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<List<StockRequestReturnVO>> approvalCompressedObject = new CompressedObject<List<StockRequestReturnVO>>(selectedVOs);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.rejectTransfer(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { approvalCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				transferReturnApprovalInterface.rejectTransfer(approvalCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.rejectTransfer(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				return;
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}

	@Override
	public List<StockRequestReturnVO> approveRequest(List<StockRequestReturnVO> selectedVOs, String reqRetType) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<List<StockRequestReturnVO>> approvalCompressedObject = new CompressedObject<List<StockRequestReturnVO>>(selectedVOs);
		CompressedObject<String> reqRetTypeCompressedObject = new CompressedObject<String>(reqRetType);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.approveTransfer(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { approvalCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<StockRequestReturnVO>> compressedObject = transferReturnApprovalInterface.approveTransfer(approvalCompressedObject, reqRetTypeCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.approvalCompressedObject(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.approveTransfer(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return compressedObject.recoverObject();
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}

	@Override
	public List<StockRequestReturnVO> StockRequestReturnVOs(Long storeId, StockRequestReportQueryFilter stockRequestReportQuery) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> storeIdCompressedObject = new CompressedObject<Long>(storeId);
		CompressedObject<StockRequestReportQueryFilter> stockRequestReportQueryFilter = new CompressedObject<StockRequestReportQueryFilter>(stockRequestReportQuery);

		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.StockRequestReturnVOs(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { storeIdCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<StockRequestReturnVO>> compressedObject = transferReturnApprovalInterface.StockRequestReturnVOs(storeIdCompressedObject, stockRequestReportQueryFilter,
						sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.StockRequestReturnVOs(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.getPendingApprovalRequest(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return compressedObject.recoverObject();
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);

	}

	@Override
	public void saveReturnRequest(List<ReturnRequestItem> requestItems) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<List<ReturnRequestItem>> requestItemsCompressedObject = new CompressedObject<List<ReturnRequestItem>>(requestItems);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "SPTransferReturnRequestBean.saveReturnRequest(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { requestItemsCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				transferReturnApprovalInterface.saveReturnRequest(requestItemsCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.saveReturnRequest(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				return;
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}

	@Override
	public String getReturnRequestStatus(Long returnId) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> returnIdCompressedObject = new CompressedObject<Long>(returnId);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "SPTransferReturnRequestBean.getReturnRequestStatus(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { returnIdCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<String> compressedObject = transferReturnApprovalInterface.getReturnRequestStatus(returnIdCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "SPTransferReturnRequestBean.getReturnRequestStatus(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				return compressedObject.recoverObject();
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing SPTransferReturnRequestBean. Please contact your system administrator.", throwable);
	}

	@Override
	public List<ReturnApprovalVO> getPendingReturnApproval(Long storeId) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> storeIdCompressedObject = new CompressedObject<Long>(storeId);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ReturnApprovalBean.getPendingApprovalRequest(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { storeIdCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<ReturnApprovalVO>> compressedObject = transferReturnApprovalInterface.getPendingReturnApproval(storeIdCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.getPendingApprovalRequest(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.getPendingApprovalRequest(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return compressedObject.recoverObject();
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}

	@Override
	public void approveReturn(List<Long> returnIds) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<List<Long>> returnIdsCompressedObject = new CompressedObject<List<Long>>(returnIds);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.approveReturn(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { returnIdsCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				transferReturnApprovalInterface.approveReturn(returnIdsCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.approveReturn(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				return;
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}

	@Override
	public void rejectReturn(List<Long> returnIds) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<List<Long>> returnIdsCompressedObject = new CompressedObject<List<Long>>(returnIds);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "TransferReturnApprovalBean.rejectReturn(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { returnIdsCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			SPTransferReturnRequestInterface transferReturnApprovalInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				transferReturnApprovalInterface.rejectReturn(returnIdsCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "TransferReturnApprovalBean.rejectReturn(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				return;
			} catch (SimServerException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing TransferReturnApprovalServices. Please contact your system administrator.", throwable);
	}
}
