package extra.retail.sim.client.screen.spareparts;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;

import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.spareparts.TransferReturnRequestQueryFilter;

public class TranferReturnRequestFilterDialogModel extends SimScreenModel {

	private TransferReturnRequestQueryFilter filter;

	public void setFilter(TransferReturnRequestQueryFilter stockRequestReportQueryFilter) {
		filter = stockRequestReportQueryFilter;
	}

	public TransferReturnRequestQueryFilter getFilter() {
		return filter;
	}

	public TransferReturnRequestQueryFilter resetFilter() {
		this.filter = ExtraBOFactory.createTransferReturnRequestQueryFilter();
		return filter;
	}
	
	public ItemVO loadItem() {
        return (ItemVO) RepositoryManager.getStateObject(ExtraSimClientStateKey.TRANSFER_REQUEST_RETURN_FILTER_ITEM_VO);
    }
}
