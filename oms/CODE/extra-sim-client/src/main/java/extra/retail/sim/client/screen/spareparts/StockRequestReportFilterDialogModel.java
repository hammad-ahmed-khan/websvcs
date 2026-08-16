package extra.retail.sim.client.screen.spareparts;

import java.util.ArrayList;
import java.util.List;

import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;

public class StockRequestReportFilterDialogModel extends SimScreenModel {
	private StockRequestReportQueryFilter filter;

	public void setFilter(StockRequestReportQueryFilter stockRequestReportQueryFilter) {
		filter = stockRequestReportQueryFilter;
	}

	public StockRequestReportQueryFilter getFilter() {
		return filter;
	}

	public StockRequestReportQueryFilter resetFilter() {
		this.filter = ExtraBOFactory.createStockRequestReportQueryFilter();
		return filter;
	}

	public Integer getDefaultSearchLimit() {
		return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_ITEM_LOOKUP);
	}

	public List<String> getrequestTypeData() {
		List<String> requestTypeData = new ArrayList<>();
		requestTypeData.add("Stock Request");
		requestTypeData.add("Return Request");
		return requestTypeData;
	}

	public List<String> getStatusData() {
		List<String> statusData = new ArrayList<>();
		statusData.add("Approved");
		statusData.add("Rejected");
		return statusData;
	}
	
	public ItemVO loadItem() {
        return (ItemVO) RepositoryManager.getStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER_ITEM_VO);
    }

}
