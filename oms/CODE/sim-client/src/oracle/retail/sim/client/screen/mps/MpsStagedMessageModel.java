package oracle.retail.sim.client.screen.mps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.integration.SimMessageDirection;
import oracle.retail.sim.common.mps.MpsStagedMessage;
import oracle.retail.sim.common.mps.MpsStagedMessageQueryFilter;
import oracle.retail.sim.common.mps.MpsStagedMessageVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * MPS Staged Message Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsStagedMessageModel extends SimScreenModel {
    public MpsStagedMessageQueryFilter getFilter() {
        MpsStagedMessageQueryFilter filter = (MpsStagedMessageQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.STAGED_MESSAGE_FILTER);
        if (filter == null) {
            filter = BOFactory.createMpsStagedMessageQueryFilter();
            Integer searchLimit = SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_STAGED_MESSAGE);
            if (searchLimit != null) {
                filter.doSetSearchLimit(searchLimit);
            }
            RepositoryManager.addStateObject(SimClientStateKey.STAGED_MESSAGE_FILTER, filter);
        }
        return filter;
    }

    public List<MpsStagedMessageVO> getStagedMessageVOs() throws Exception {
        return ClientServiceFactory.getMpsServices().findStagedMessageVOs(getFilter());
    }

    public MpsStagedMessage getStagedMessage(MpsStagedMessageVO stagedMessageVO) throws Exception {
        return ClientServiceFactory.getMpsServices().readStagedMessage(stagedMessageVO.getId());
    }

    public void resetStagedMessages(List<MpsStagedMessageVO> stagedMessageVOs) throws Exception {
        ClientServiceFactory.getMpsServices().resetStagedMessages(getIds(stagedMessageVOs));
    }

    public void deleteStagedMessages(List<MpsStagedMessageVO> stagedMessageVOs) throws Exception {
        ClientServiceFactory.getMpsServices().deleteStagedMessages(getIds(stagedMessageVOs));
    }

    private static List<Long> getIds(List<MpsStagedMessageVO> stagedMessageVOs) {
        List<Long> ids = new ArrayList<>(stagedMessageVOs.size());
        for (MpsStagedMessageVO stagedMessageVO : stagedMessageVOs) {
            ids.add(stagedMessageVO.getId());
        }
        return ids;
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<>();
        MpsStagedMessageQueryFilter filter = getFilter();
        if (filter.getMessageId() != null) {
            descriptionMap.put("Id", filter.getMessageId().toString());
        }
        if (filter.getMessageFamily() != null) {
            descriptionMap.put("Family", filter.getMessageFamily().getCode());
        }
        if (filter.isInbound() != null) {
            descriptionMap.put("In/Out", SimMessageDirection.toValue(filter.isInbound()).getCode());
        }
        descriptionMap.put("Show Pending", filter.isShowPending().toString());
        descriptionMap.put("Show Retry", filter.isShowRetry().toString());
        descriptionMap.put("Search Limit", LocaleManager.getIntegerFormatter().format(filter.getSearchLimit()));
        return descriptionMap;
    }
}
