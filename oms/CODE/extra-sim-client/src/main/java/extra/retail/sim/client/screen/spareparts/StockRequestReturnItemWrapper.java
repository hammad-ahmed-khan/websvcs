package extra.retail.sim.client.screen.spareparts;

import java.util.Date;
import java.util.Objects;

import oracle.retail.sim.common.business.Quantity;

import extra.retail.sim.common.spareparts.StockRequestReturnDetailVO;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;

/**
 * StockRequestReturnItemWrapper.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnItemWrapper {

	private Boolean selected = false;

	private StockRequestReturnDetailVO detailVO;

	public StockRequestReturnItemWrapper(StockRequestReturnVO approvalVO, StockRequestReturnDetailVO detailVO) {
		this.detailVO = detailVO;
		detailVO.setRequestReturnVO(approvalVO);
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

	public Boolean getSelected() {
		return selected;
	}

	public String getTsfSequenceNo() {
		return detailVO.getRequestReturnVO().getTsfSequenceNo();
	}

	public String getSrNo() {
		return detailVO.getRequestReturnVO().getSerialNo();
	}

	public String getTechnicianId() {
		return detailVO.getRequestReturnVO().getTechnicianId();
	}

	public Long getReqLocation() {
		return detailVO.getRequestReturnVO().getRequestLoc();
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public String getType() {
		return detailVO.getRequestReturnVO().getType();
	}

	public Date getRequestDate() {
		return detailVO.getRequestReturnVO().getRequestDate();
	}

	public Date getappRejTime() {
		return detailVO.getRequestReturnVO().getAppRejTime();
	}

	public String getComments() {
		return detailVO.getRequestReturnVO().getComments();
	}

	@Override
	public int hashCode() {
		return Objects.hash(detailVO.getRequestReturnVO().getSequenceId()) + Objects.hash(detailVO.getItem());
	}

	public String getStatus() {
		return detailVO.getRequestReturnVO().getStatus();
	}

	public String getRequestType() {
		return detailVO.getRequestReturnVO().getRequestType();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StockRequestReturnItemWrapper)) {
			return false;
		}
		StockRequestReturnItemWrapper other = (StockRequestReturnItemWrapper) obj;
		return Objects.equals(detailVO.getRequestReturnVO().getSequenceId(), other.detailVO.getRequestReturnVO().getSequenceId()) && 
				Objects.equals(detailVO.getItem(), detailVO.getItem());
	}

	public StockRequestReturnDetailVO getDetail() {
		return detailVO;
	}

	public StockRequestReturnVO getApprovalVO() {
		return detailVO.getRequestReturnVO();
	}
}
