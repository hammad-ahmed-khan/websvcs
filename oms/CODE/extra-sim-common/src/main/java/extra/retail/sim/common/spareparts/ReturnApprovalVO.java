package extra.retail.sim.common.spareparts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReturnApprovalVO implements Serializable {

	private static final long serialVersionUID = -8184775783643346907L;

	private long returnId;

	private long storeId;

	private String sourceId;

	private String supplierName;

	private String createUser;

	private Date createDate;

	private List<ReturnApprovalDetailVO> returnApprovalDetailVO = new ArrayList<>();

	public long getReturnId() {
		return returnId;
	}

	public void setReturnId(long returnId) {
		this.returnId = returnId;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<ReturnApprovalDetailVO> getReturnApprovalDetailVO() {
		return returnApprovalDetailVO;
	}

	public void setReturnApprovalDetailVO(List<ReturnApprovalDetailVO> returnApprovalDetailVO) {
		this.returnApprovalDetailVO = returnApprovalDetailVO;
	}

}