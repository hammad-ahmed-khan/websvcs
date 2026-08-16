package extra.retail.sim.client.screen.spareparts;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import extra.retail.sim.common.spareparts.ReturnApprovalDetailVO;
import extra.retail.sim.common.spareparts.ReturnApprovalVO;

/**
 * ReturnApprovalReportWrapper.java
 * aibrahim
 * 2024
 */
public class ReturnApprovalReportWrapper {

    private Boolean selected = false;

    private ReturnApprovalVO approvalVO;
    
    private List<ReturnApprovalDetailVO> returnApprovalDetailVO;

    public ReturnApprovalReportWrapper(ReturnApprovalVO approvalVO) {
        this.approvalVO = approvalVO;
    }

    public ReturnApprovalVO getApprovalVO() {
		return approvalVO;
	}

    public Boolean getSelected() {
        return selected;
    }

    public long getReturnId() {
        return approvalVO.getReturnId();
    }

    public  long getStoreId() {
        return approvalVO.getStoreId();
    }

    public String getSourceId() {
        return approvalVO.getSourceId();
    }
    
    public String getSupplierName() {
        return approvalVO.getSupplierName();
    }
    
    public String getCreateUser() {
        return approvalVO.getCreateUser();
    }
    
    public Date getCreateDate() {
        return approvalVO.getCreateDate();
    }

    public List<ReturnApprovalDetailVO> getReturnApprovalDetailVO() {
		return returnApprovalDetailVO;
	}

	public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(approvalVO.getReturnId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReturnApprovalReportWrapper)) {
            return false;
        }
        ReturnApprovalReportWrapper other = (ReturnApprovalReportWrapper) obj;
        return Objects.equals(approvalVO.getReturnId(), other.approvalVO.getReturnId());
    }
}
