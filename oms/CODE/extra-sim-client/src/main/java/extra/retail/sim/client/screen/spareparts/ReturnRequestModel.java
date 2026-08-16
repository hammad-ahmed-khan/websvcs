package extra.retail.sim.client.screen.spareparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;

import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.service.core.ExtraClientServiceFactory;

public class ReturnRequestModel extends SimScreenModel {

	private List<ReturnApprovalReportWrapper> wrappers = null;

	public List<ReturnApprovalReportWrapper> getPendingApprovalRequest() throws Exception {
		List<ReturnApprovalVO> list = ExtraClientServiceFactory.getStockRequestReturnService().getPendingReturnApproval(SimRepository.getStoreId());
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		wrappers = new ArrayList<>(list.size());
		for (ReturnApprovalVO approvalVO : list) {
			wrappers.add(new ReturnApprovalReportWrapper(approvalVO));
		}
		return wrappers;
	}

	public void rejectRequest(List<Long> returnId) throws Exception {
		ExtraClientServiceFactory.getStockRequestReturnService().rejectReturn(returnId);
	}

	public void approveRequest(List<Long> returnId) throws Exception {
		ExtraClientServiceFactory.getStockRequestReturnService().approveReturn(returnId);
	}
}
