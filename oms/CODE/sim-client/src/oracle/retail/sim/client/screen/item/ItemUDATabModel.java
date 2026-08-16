package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.uda.ItemUDAVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * User Defined Attributes Detail Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemUDATabModel extends SimScreenModel {

    private ItemDetailVO itemDetailVO = null;
    private List<ItemUDAVO> itemUDAs = new ArrayList<>();

    public List<ItemUDAWrapper> findUserDefinedAttributes(ItemDetailVO detailVO) throws Exception {
        if ((itemDetailVO == null) || !itemDetailVO.getId().equals(detailVO.getId())) {
            itemDetailVO = detailVO;
            itemUDAs = new ArrayList<>();
        }
        if (itemUDAs.isEmpty()) {
            String id = null;
            if (null != itemDetailVO) {
                id = itemDetailVO.getId();
            }
            itemUDAs = ClientServiceFactory.getUDAServices().findItemUDAVOs(id);
        }
        List<ItemUDAWrapper> wrappers = new ArrayList<>(itemUDAs.size());
        for (ItemUDAVO vo : itemUDAs) {
            wrappers.add(new ItemUDAWrapper(vo));
        }
        return wrappers;
    }
}
