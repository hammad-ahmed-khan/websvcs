package extra.retail.sim.client.screen.spareparts;

import java.util.Date;
import java.util.Objects;

import extra.retail.sim.common.spareparts.StockRequestReturnDetailVO;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import oracle.retail.sim.common.business.Quantity;

/**
 * StockRequestReturnWrapper.java
 * aibrahim
 * 2024
 */
public class StockRequestReportWrapper {

	private Boolean selected = false;

	private StockRequestReturnVO approvalVO;
	
	private StockRequestReturnDetailVO detailVO;

	public StockRequestReportWrapper(StockRequestReturnVO approvalVO, StockRequestReturnDetailVO detailVO) {
		this.approvalVO = approvalVO;
		this.detailVO = detailVO;
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
	
	public Date getappRejTime() {
		return approvalVO.getAppRejTime();
	}

	public void setComments(String comments) {
		approvalVO.setComments(comments);
	}
	

	public String getComments() {
		return approvalVO.getComments();
	}
	
	public String getItem() {
		return detailVO.getItem();
	}
	
	
	public String getItemDescription() {
		return detailVO.getItemDescription();
	}
	
	public String getBrandName() {
		return detailVO.getBrandName();
	}

	public Quantity getQty() {
		return detailVO.getQty();
	}

	public Quantity getAvailableQty() {
		return detailVO.getAvailable();
	}

	public Quantity getTechSubQty() {
		return detailVO.getTechSubBucket();
	}
	public String getBin() {
		return detailVO.getBin();
	}

	@Override
	public int hashCode() {
		return Objects.hash(approvalVO.getSequenceId());
	}
	
	public String getStatus() {
		return approvalVO.getStatus();
	}
	
	public String getRequestType() {
		return approvalVO.getRequestType();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StockRequestReportWrapper)) {
			return false;
		}
		StockRequestReportWrapper other = (StockRequestReportWrapper) obj;
		return Objects.equals(approvalVO.getSequenceId(), other.approvalVO.getSequenceId());
	}
}
