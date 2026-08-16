package extra.retail.sim.client.screen.spareparts;

import java.util.Date;
import java.util.Objects;

import extra.retail.sim.common.spareparts.StockRequestReturnVO;

/**
 * StockRequestReturnWrapper.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnWrapper {

	private Boolean selected = false;

	private StockRequestReturnVO approvalVO;

	public StockRequestReturnWrapper(StockRequestReturnVO approvalVO) {
		this.approvalVO = approvalVO;
	}

	public Boolean getSelected() {
		return selected;
	}

	public String getTsfSequenceNo() {
		return approvalVO.getTsfSequenceNo();
	}

	public String getSrNo() {
		return approvalVO.getSerialNo();
	}

	public String getTechnicianId() {
		return approvalVO.getTechnicianId();
	}

	public Long getReqLocation() {
		return approvalVO.getRequestLoc();
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public StockRequestReturnVO getApprovalVO() {
		return approvalVO;
	}

	public String getType() {
		return approvalVO.getType();
	}

	public Date getRequestDate() {
		return approvalVO.getRequestDate();
	}

	public void setComments(String comments) {
		approvalVO.setComments(comments);
	}

	public String getComments() {
		return approvalVO.getComments();
	}

	@Override
	public int hashCode() {
		return Objects.hash(approvalVO.getSequenceId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StockRequestReturnWrapper)) {
			return false;
		}
		StockRequestReturnWrapper other = (StockRequestReturnWrapper) obj;
		return Objects.equals(approvalVO.getSequenceId(), other.approvalVO.getSequenceId());
	}
}
