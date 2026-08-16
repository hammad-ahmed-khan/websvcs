package extra.retail.sim.client.screen.spareparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;

import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnDetailVO;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import extra.retail.sim.service.core.ExtraClientServiceFactory;

public class StockRequestReportModel extends SimScreenModel {

	private List<StockRequestReportWrapper> wrappers = null;

	public List<StockRequestReportWrapper> getRequestReportDetails() throws Exception {
		System.out.println("Filter Data: " + getFilter());

		System.out.println("Store ID  :" + SimRepository.getStoreId());
		List<StockRequestReturnVO> list = ExtraClientServiceFactory.getStockRequestReturnService().StockRequestReturnVOs(SimRepository.getStoreId(), getFilter());
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		wrappers = new ArrayList<>();
		for (StockRequestReturnVO approvalVO : list) {
			if (approvalVO.getDetails() != null && !approvalVO.getDetails().isEmpty()) {
				for (StockRequestReturnDetailVO detailVO : approvalVO.getDetails()) {
					wrappers.add(new StockRequestReportWrapper(approvalVO, detailVO));
				}
			} else {
				wrappers.add(new StockRequestReportWrapper(approvalVO, null));
			}
		}
		return wrappers;
	}

	public Integer getDefaultSearchLimit() {
		return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_ITEM_LOOKUP);
	}

	public StockRequestReportQueryFilter getFilter() throws BusinessException {
		StockRequestReportQueryFilter stockRequestReportQueryFilter = (StockRequestReportQueryFilter) RepositoryManager.getStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER);
		if (stockRequestReportQueryFilter == null) {
			stockRequestReportQueryFilter = ExtraBOFactory.createStockRequestReportQueryFilter();
			stockRequestReportQueryFilter.setSearchLimit(getDefaultSearchLimit().intValue());
			System.out.println("Search limit" + stockRequestReportQueryFilter.getSearchLimit());
			RepositoryManager.addStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER, stockRequestReportQueryFilter);
		}
		return stockRequestReportQueryFilter;
	}

	public Map<String, String> getDescriptionMap() throws BusinessException {
		LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
		StockRequestReportQueryFilter stockRequestReportQueryFilter = getFilter();
		if (stockRequestReportQueryFilter.getFromDate() != null) {
			linkedHashMap.put("From Date", LocaleManager.getShortDateFormatter().format(stockRequestReportQueryFilter.getFromDate()));
		}
		if (stockRequestReportQueryFilter.getToDate() != null) {
			linkedHashMap.put("To Date", LocaleManager.getShortDateFormatter().format(stockRequestReportQueryFilter.getToDate()));
		}
		if (stockRequestReportQueryFilter.getSerialNo() != null) {
			linkedHashMap.put("SR Number", stockRequestReportQueryFilter.getSerialNo());
		}
		if (stockRequestReportQueryFilter.getRequestType() != null) {
			linkedHashMap.put("Request Type", stockRequestReportQueryFilter.getRequestType());
		}

		if (stockRequestReportQueryFilter.getItemId() != null) {
			linkedHashMap.put("Item", stockRequestReportQueryFilter.getItemId());
		}

		if (stockRequestReportQueryFilter.getTechnicianId() != null) {
			linkedHashMap.put("Technician ID", stockRequestReportQueryFilter.getTechnicianId());
		}
		if (stockRequestReportQueryFilter.getStatus() != null) {
			linkedHashMap.put("Status", stockRequestReportQueryFilter.getStatus());
		}
		return (Map) linkedHashMap;
	}

}
