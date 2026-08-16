package extra.retail.sim.service.spareparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.server.business.CommandFactory;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.stockreturn.ReturnCancelCommand;

import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnDetailVO;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import extra.retail.sim.server.dataaccess.ExtraDAOFactory;

/**
 * StockRequestReturnServerService.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnServerService extends StockRequestReturnServices {

	private Map<String, Integer> spReasonMap = new HashMap<>();

	private Map<Integer, InventoryAdjustmentReason> reasonMap = new HashMap<>();

	@Override
	public List<StockRequestReturnVO> getPendingRequestReturn(String userName, String requestType, Long storeId) throws SimServerException {
		return ExtraDAOFactory.getTransferReturnApprovalDao().getPendingRequestReturn(userName, requestType, storeId);
	}

	@Override
	public List<Store> getUserApprovalLocations(String userName) throws SimServerException {
		return ExtraDAOFactory.getTransferReturnApprovalDao().getUserApprovalLocations(userName);
	}

	@Override
	public void rejectRequest(List<StockRequestReturnVO> selectedVOs) throws Exception {
		ExtraDAOFactory.getTransferReturnApprovalDao().rejectTransfer(selectedVOs);
	}

	@Override
	public List<StockRequestReturnVO> approveRequest(List<StockRequestReturnVO> selectedVOs, String reqRetType) throws Exception {
		for (StockRequestReturnVO requestVO : selectedVOs) {
			Long invAdjId = createInventoryAdjustment(requestVO, reqRetType);
			InventoryAdjustment inventoryAdjustment = ClientServiceFactory.getInventoryAdjustmentServices().readInventoryAdjustment(invAdjId);
			confirmInventoryAdjustment(inventoryAdjustment);
		}
		ExtraDAOFactory.getTransferReturnApprovalDao().approveTransfer(selectedVOs);
		return null;
	}

	public void confirmInventoryAdjustment(InventoryAdjustment paramInventoryAdjustment) throws Exception {
		boolean bool = SimConfigManager.getStoreBoolean("DISPLAY_LATE_ADJUSTMENT_MESSAGE", paramInventoryAdjustment.getStoreId());
		ClientServiceFactory.getInventoryAdjustmentServices().confirmInventoryAdjustment(paramInventoryAdjustment, bool);
	}

	private Long createInventoryAdjustment(StockRequestReturnVO requestReturnVO, String reqRetType) throws Exception {
		InventoryAdjustment inventoryAdjustment = BOFactory.createInventoryAdjustment();
		inventoryAdjustment.doSetStoreId(requestReturnVO.getRequestLoc());
		inventoryAdjustment.doSetComments(StringHelper.trimToNull("SP"));

		inventoryAdjustment.doSetReferenceId(requestReturnVO.getSequenceId());
		List<String> arrayList = new ArrayList<>();
		for (StockRequestReturnDetailVO detail : requestReturnVO.getDetails()) {
			arrayList.add(detail.getItem());
		}

		Map<String, StockItem> map = null;
		if (SimConfigManager.getBoolean("ALLOW_NON_RANGE_ITEM")) {
			map = ClientServiceFactory.getItemServices().readStockItemsOrCreate(arrayList, requestReturnVO.getRequestLoc());
		} else {
			map = ClientServiceFactory.getItemServices().readStockItems(arrayList, requestReturnVO.getRequestLoc());
		}

		for (StockRequestReturnDetailVO detail : requestReturnVO.getDetails()) {
			createLineItem(inventoryAdjustment, requestReturnVO, (StockItem) map.get(detail.getItem()), detail, reqRetType);
		}
		inventoryAdjustment.isCoherent();
		return ClientServiceFactory.getInventoryAdjustmentServices().updateInventoryAdjustment(inventoryAdjustment);
	}

	private void createLineItem(InventoryAdjustment paramInventoryAdjustment, StockRequestReturnVO requestVO, StockItem paramStockItem, StockRequestReturnDetailVO detailVO, String reqRetType) throws Exception {
		if (paramStockItem == null && SimConfigManager.getBoolean("ALLOW_NON_RANGE_ITEM")) {
			paramStockItem = ClientServiceFactory.getItemServices().readStockItemOrCreate(detailVO.getItem(), paramInventoryAdjustment.getStoreId());
		}
		InventoryAdjustmentReason inventoryAdjustmentReason = findReason(requestVO.getTechnicianId(), reqRetType);
		InventoryAdjustmentLineItem inventoryAdjustmentLineItem = paramInventoryAdjustment.createLineItem(paramStockItem);
		inventoryAdjustmentLineItem.setReason(inventoryAdjustmentReason);
		inventoryAdjustmentLineItem.setQuantity(detailVO.getQty());
		inventoryAdjustmentLineItem.setCaseSize(new Quantity(1));
	}

	private InventoryAdjustmentReason findReason(String technician, String reqRetType) throws Exception {
		if (this.reasonMap.isEmpty()) {
			List<InventoryAdjustmentReason> list = ClientServiceFactory.getInventoryAdjustmentServices().findAllInventoryAdjustmentReasons();
			for (InventoryAdjustmentReason inventoryAdjustmentReason : list) {
				this.reasonMap.put(inventoryAdjustmentReason.getCode(), inventoryAdjustmentReason);
			}
		}
		Integer reasonCode = null;
		if ("Stock Request".equals(reqRetType)) {
			reasonCode = 283;
		} else {
			reasonCode = this.spReasonMap.get(technician);
			if (reasonCode == null) {
				reasonCode = getTechBucketCode(technician);
				this.spReasonMap.put(technician, reasonCode);
			}
		}
		return this.reasonMap.get(reasonCode);
	}

	private Integer getTechBucketCode(String technician) throws Exception {
		return ExtraDAOFactory.getTransferReturnApprovalDao().getTechBucketCode(technician);
	}

	@Override
	public List<StockRequestReturnVO> StockRequestReturnVOs(Long storeId,StockRequestReportQueryFilter stockRequestReportQueryFilter) throws Exception {
		return ExtraDAOFactory.getTransferReturnApprovalDao().selectStockRequestReport(storeId, stockRequestReportQueryFilter);
	}

	@Override
	public void saveReturnRequest(List<ReturnRequestItem> requestItems) throws Exception {
		ExtraDAOFactory.getReturnRequestDao().saveReturnRequest(requestItems);
	}

	@Override
	public String getReturnRequestStatus(Long returnId) throws Exception {
		return ExtraDAOFactory.getReturnRequestDao().getReturnRequestStatus(returnId);
	}

	@Override
	public List<ReturnApprovalVO> getPendingReturnApproval(Long storeId) throws Exception {
		return ExtraDAOFactory.getReturnRequestDao().getPendingReturnRequest(storeId);
	}

	@Override
	public void approveReturn(List<Long> returnIds) throws Exception {
		ExtraDAOFactory.getReturnRequestDao().approveReturn(returnIds);
	}

	@Override
	public void rejectReturn(List<Long> returnIds) throws Exception {
		ExtraDAOFactory.getReturnRequestDao().rejectReturn(returnIds);
		for (Long returnId: returnIds) {
			ReturnCancelCommand returnCancelCommand = CommandFactory.createReturnCancelCommand();
		    returnCancelCommand.setReturnId(returnId);
		    returnCancelCommand.execute();
		}
	}
}
