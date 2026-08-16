package oracle.retail.sim.client.screen.uin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.uin.UINDetail;
import oracle.retail.sim.common.uin.UINProblemDetail;
import oracle.retail.sim.common.uin.UINProblemDetailQueryFilter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * UIN Resolution List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class UINResolutionListModel extends SimScreenModel {
    public UINProblemDetailQueryFilter getFilter() throws BusinessException {
        UINProblemDetailQueryFilter filter = (UINProblemDetailQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.UIN_ATTRIBUTE_FILTER);
        if (filter == null) {
            filter = BOFactory.createUINResolutionQueryFilter();
            filter.setStoreId(getStoreId());
            filter.setResolved(Boolean.FALSE);
            filter.setSearchLimit(getDefaultSearchLimit());
            RepositoryManager.addStateObject(SimClientStateKey.UIN_ATTRIBUTE_FILTER, filter);
        }
        return filter;
    }

    private int getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_UIN_RESOLUTION);
    }

    public List<UINProblemDetail> findUINProblemDetails() throws Exception {
        return ClientServiceFactory.getUINServices().findUINProblemDetails(getFilter());
    }

    public void resolve(List<UINProblemDetail> problemDetails) throws Exception {
        for (UINProblemDetail detail : problemDetails) {
            detail.setUpdateDate(SimDateUtil.getCurrentDate());
            detail.setUpdateUser(getUserName());
        }
        ClientServiceFactory.getUINServices().updateProblemDetailsToResolved(problemDetails);
    }

    public boolean storeUINDetail(UINProblemDetail detail) throws Exception {
        UINDetail uinDetail = ClientServiceFactory.getUINServices().findUINDetail(detail.getItemId(), detail.getUin());
        if (uinDetail != null) {
            ItemDetailVO detailVO = ClientServiceFactory.getItemServices().readItemDetailVO(detail.getItemId(), getStoreId());
            if (detailVO != null) {
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, detailVO);
                RepositoryManager.addStateObject(SimClientStateKey.ITEM_UIN, uinDetail);
                RepositoryManager.addStateObject(SimClientStateKey.ITEM_UIN_PROBLEM, detail);
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getDescriptionMap() throws BusinessException {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        UINProblemDetailQueryFilter filter = getFilter();
        if (!StringUtility.isNullOrEmpty(filter.getItemId())) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (!StringUtility.isNullOrEmpty(filter.getUinValue())) {
            descriptionMap.put("UIN", filter.getUinValue());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Existing Status", Translator.getText(filter.getStatus().toString()));
        }
        if (filter.getFromDate() != null) {
            descriptionMap.put("Create From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("Create To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getResolved() != null) {
            descriptionMap.put("Resolved", new BooleanDisplayer().getDisplayText(filter.getResolved()));
        }
        if (filter.getSearchLimit() > 0) {
            descriptionMap.put("Search Limit", String.valueOf(filter.getSearchLimit()));
        }
        return descriptionMap;
    }
}
