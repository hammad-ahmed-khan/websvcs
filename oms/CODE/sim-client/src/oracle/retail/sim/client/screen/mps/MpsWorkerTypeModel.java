package oracle.retail.sim.client.screen.mps;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.mps.MpsWorkerTypeVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * MPS Worker Type Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsWorkerTypeModel extends SimScreenModel {
    public List<MpsWorkerTypeVO> findWorkerTypeVOs() throws Exception {
        return ClientServiceFactory.getMpsServices().readWorkerTypeVOs();
    }

    public void start(List<MpsWorkerTypeVO> workerTypeVOs) throws Exception {
        ClientServiceFactory.getMpsServices().startWorkerTypes(getIds(workerTypeVOs));
    }

    public void stop(List<MpsWorkerTypeVO> workerTypeVOs) throws Exception {
        ClientServiceFactory.getMpsServices().stopWorkerTypes(getIds(workerTypeVOs));
    }

    private static List<Long> getIds(List<MpsWorkerTypeVO> workerTypeVOs) {
        List<Long> ids = new ArrayList<>(workerTypeVOs.size());
        for (MpsWorkerTypeVO workerTypeVO : workerTypeVOs) {
            ids.add(workerTypeVO.getId());
        }
        return ids;
    }
}
