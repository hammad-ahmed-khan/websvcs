package oracle.retail.sim.client.screen.uin;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.uin.UINDetail;
import oracle.retail.sim.common.uin.UINHistoryVO;
import oracle.retail.sim.common.uin.UINProblemDetail;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * UIN Update Status Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINHistoryModel extends SimScreenModel {
    private ItemDetailVO itemDetailVO;
    private UINDetail uinDetail;

    public void initializeModel() {
        itemDetailVO = (ItemDetailVO) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
        uinDetail = (UINDetail) RepositoryManager.getStateObject(SimClientStateKey.ITEM_UIN);
    }

    public ItemDetailVO getItemDetailVO() {
        return itemDetailVO;
    }

    public UINDetail getUINDetail() {
        return uinDetail;
    }

    public List<UINHistoryVO> findUINHistoryVOs() throws Exception {
        return ClientServiceFactory.getUINServices().findUINHistoryVOs(itemDetailVO.getId(), uinDetail.getUin());
    }

    public void updateStatus(UINStatus newStatus) throws Exception {
        ClientServiceFactory.getUINServices().updateUINDetailStatus(uinDetail.getId(), newStatus);
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_UIN_STATUS_MODIFIED, Boolean.TRUE);
    }

    public boolean isSerialNumberEditable() {
        if (uinDetail == null) {
            return false;
        }
        UINProblemDetail problemDetail = (UINProblemDetail) RepositoryManager.getStateObject(SimClientStateKey.ITEM_UIN_PROBLEM);
        if (problemDetail != null) {
            if (problemDetail.isResolved()) {
                return false;
            }
        }
        return uinDetail.getStoreId().equals(getStoreId());
    }

    public String getUINLabel() throws Exception {
        return ClientServiceFactory.getUINServices().readUINLabel(uinDetail.getItemId(), getStoreId());
    }
}
