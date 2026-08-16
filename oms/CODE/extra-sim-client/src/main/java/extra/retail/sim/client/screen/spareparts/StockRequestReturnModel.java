package extra.retail.sim.client.screen.spareparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.store.Store;

import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.spareparts.StockRequestReturnDetailVO;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import extra.retail.sim.common.spareparts.TransferReturnRequestQueryFilter;
import extra.retail.sim.service.core.ExtraClientServiceFactory;

/**
 * TransferReturnApprovalModel.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnModel extends SimScreenModel {

	private List<StockRequestReturnItemWrapper> wrappers = null;

	public List<StockRequestReturnItemWrapper> getPendingApprovalRequest(String requestType, Long storeId) throws Exception {
		List<StockRequestReturnVO> list = ExtraClientServiceFactory.getStockRequestReturnService().getPendingRequestReturn(getUser().getUserName(), requestType, storeId);
		Set<String> brandNames = new HashSet<String>();
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		wrappers = new ArrayList<>(list.size());
		for (StockRequestReturnVO approvalVO : list) {
			for (StockRequestReturnDetailVO detailVO : approvalVO.getDetails()) {
				wrappers.add(new StockRequestReturnItemWrapper(approvalVO, detailVO));
				if (!brandNames.contains(detailVO.getBrandName())) {
					brandNames.add(detailVO.getBrandName());
				}
			}
		}
		RepositoryManager.addStateObject(ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER_BRANDS, brandNames);
		return wrappers;
	}

	public List<StockRequestReturnItemWrapper> getPendingApprovals() {
		return wrappers;
	}

	public List<Store> getUserApprovalLocations() throws Exception {
		return ExtraClientServiceFactory.getStockRequestReturnService().getUserApprovalLocations(getUserName());
	}

	public void rejectRequest(List<StockRequestReturnVO> selectedVOs) throws Exception {
		ExtraClientServiceFactory.getStockRequestReturnService().rejectRequest(selectedVOs);
	}

	public void approveRequest(List<StockRequestReturnVO> selectedVOs, String reqRetType) throws Exception {
		ExtraClientServiceFactory.getStockRequestReturnService().approveRequest(selectedVOs, reqRetType);
	}

	public TransferReturnRequestQueryFilter getFilter() throws BusinessException {
		TransferReturnRequestQueryFilter queryFilter = (TransferReturnRequestQueryFilter) RepositoryManager.getStateObject(ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER);
		if (queryFilter == null) {
			queryFilter = ExtraBOFactory.createTransferReturnRequestQueryFilter();
			RepositoryManager.addStateObject(ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER, queryFilter);
		}
		return queryFilter;
	}

	public Map<String, String> getDescriptionMap() throws Exception {
		HashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
		TransferReturnRequestQueryFilter filter = getFilter();
		if (filter.getFromDate() != null) {
			linkedHashMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
		}
		if (filter.getToDate() != null) {
			linkedHashMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
		}
		if (filter.getSrNumber() != null) {
			linkedHashMap.put("SR Number", filter.getSrNumber());
		}
		if (filter.getItemId() != null) {
			linkedHashMap.put("Item", filter.getItemId());
		}
		if (filter.getBrand() != null) {
			linkedHashMap.put("Brand", filter.getBrand());
		}
		return linkedHashMap;
	}
}
