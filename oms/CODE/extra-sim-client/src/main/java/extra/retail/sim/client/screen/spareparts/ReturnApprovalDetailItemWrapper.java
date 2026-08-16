package extra.retail.sim.client.screen.spareparts;

import extra.retail.sim.common.spareparts.ReturnApprovalDetailVO;
import oracle.retail.sim.common.business.Quantity;

public class ReturnApprovalDetailItemWrapper {

    private ReturnApprovalDetailVO returnApprovalDetailVO;

    public ReturnApprovalDetailItemWrapper(ReturnApprovalDetailVO returnApprovalDetailVO) {
        this.returnApprovalDetailVO = returnApprovalDetailVO;
    }

    public long getReturnId() {
        return returnApprovalDetailVO.getReturnId();
    }

    public String getItemId() {
        return returnApprovalDetailVO.getItemId();
    }

    public Quantity getQuantity() {
        return returnApprovalDetailVO.getQty();
    }
    
    public String getItemDescription() {
        return returnApprovalDetailVO.getItemDescription();
    }
    
    public String getBrandName() {
        return returnApprovalDetailVO.getBrandName();
    }


}
