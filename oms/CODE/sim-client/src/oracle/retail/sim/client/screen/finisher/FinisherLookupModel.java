package oracle.retail.sim.client.screen.finisher;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.FinisherVO;
import oracle.retail.sim.common.source.SourceQueryFilter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Finisher Lookup Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherLookupModel extends SimScreenModel {
    public List<FinisherVO> findFinishers(SourceQueryFilter filter) throws Exception {
        if (filter.getSearchLimit() < 1) {
            throw new BusinessException(CommonMessageText.FINISHER_UNDER_SEARCH_LIMIT);
        }
        if (filter.getSearchLimit() > SimConfigManager.SEARCH_LIMIT_MAX_VALUE) {
            throw new BusinessException(CommonMessageText.FINISHER_OVER_SEARCH_LIMIT);
        }
        return ClientServiceFactory.getSourceServices().findFinisherVOs(filter);
    }

    public void storeFinisher(FinisherVO finisherVO) throws Exception {
        if (finisherVO != null) {
            Finisher finisher = ClientServiceFactory.getSourceServices().readFinisher(finisherVO.getId(), SimRepository.getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FINISHER, finisher);
        }
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_SUPPLIER_LOOKUP);
    }
}
