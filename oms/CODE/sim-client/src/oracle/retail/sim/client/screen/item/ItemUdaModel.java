package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.uda.ItemUDAVO;

/********************************************************************************************************
 * UDA Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemUdaModel extends SimScreenModel {

    public List<ItemUDAWrapper> getUserDefinedAttributes() {
        List<ItemUDAVO> itemUDAs = (List<ItemUDAVO>) RepositoryManager.getStateObject(SimClientStateKey.ITEM_UDA_VOS);
        if ((itemUDAs == null) || itemUDAs.isEmpty()) {
            return Collections.emptyList();
        }
        List<ItemUDAWrapper> wrappers = new ArrayList<>(itemUDAs.size());
        for (ItemUDAVO vo : itemUDAs) {
            wrappers.add(new ItemUDAWrapper(vo));
        }
        return wrappers;
    }
}
