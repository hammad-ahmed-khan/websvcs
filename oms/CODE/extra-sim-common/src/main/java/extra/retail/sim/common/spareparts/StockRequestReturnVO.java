package extra.retail.sim.common.spareparts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * TransferReturnApprovalVO.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnVO implements Serializable {

	private static final long serialVersionUID = 8355023559765230452L;

	private Long sequenceId;

	private String tsfSequenceNo;

	private String serialNo;

	private Long requestLoc;

	private String technicianId;

	private Date requestDate;

	private Date appRejTime;

	private List<StockRequestReturnDetailVO> details = new ArrayList<>();

	private String comments;

    private String status;

	private String requestType;

	public String getTsfSequenceNo() {
		return tsfSequenceNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public Long getRequestLoc() {
		return requestLoc;
	}

	public String getTechnicianId() {
		return technicianId;
	}

	public void setTsfSequenceNo(String tsfSequenceNo) {
		this.tsfSequenceNo = tsfSequenceNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public void setRequestLoc(Long requestLoc) {
		this.requestLoc = requestLoc;
	}

	public void setTechnicianId(String technicianId) {
		this.technicianId = technicianId;
	}

	public String getType() {
		return null;
	}

	public Long getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}

	public List<StockRequestReturnDetailVO> getDetails() {
		return details;
	}

	public void setDetails(List<StockRequestReturnDetailVO> details) {
		this.details = details;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}

	public Date getAppRejTime() {
		return appRejTime;
	}

	public void setLastDate(Date appRejTime) {
		this.appRejTime = appRejTime;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sequenceId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StockRequestReturnVO))
			return false;
		StockRequestReturnVO other = (StockRequestReturnVO) obj;
		return Objects.equals(sequenceId, other.sequenceId);
	}
}
