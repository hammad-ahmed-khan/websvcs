package oracle.retail.sim.client.screen.productgroup;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.productgroup.ProductGroupMessageText;
import oracle.retail.sim.common.schedule.ProductGroupSchedule;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;
import oracle.retail.sim.common.schedule.ScheduleStatus;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Product Group Schedule List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleListModel extends SimScreenModel {
    public ProductGroupScheduleQueryFilter getFilter() {
        ProductGroupScheduleQueryFilter filter = (ProductGroupScheduleQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_FILTER);
        if (filter == null) {
            filter = BOFactory.createProductGroupScheduleQueryFilter();
            filter.doSetStatus(ScheduleStatus.OPEN);
            filter.doSetStoreId(getStoreId());

            RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_FILTER, filter);
        }
        return filter;
    }

    public List<ProductGroupScheduleWrapper> getScheduleWrappers() throws Exception {
        List<ProductGroupScheduleVO> scheduleVOs = ClientServiceFactory.getProductGroupScheduleServices().findProductGroupScheduleVOs(getFilter());
        List<ProductGroupScheduleWrapper> wrappers = new ArrayList<>(scheduleVOs.size());
        for (ProductGroupScheduleVO scheduleVO : scheduleVOs) {
            wrappers.add(ClientWrapperFactory.createProductGroupScheduleWrapper(scheduleVO, getTimeZone()));
        }
        return wrappers;
    }

    public void deleteProductGroupSchedules(List<ProductGroupScheduleWrapper> wrappers) throws Exception {
        for (ProductGroupScheduleWrapper scheduleWrapper : wrappers) {
            ProductGroupScheduleVO scheduleVO = scheduleWrapper.getScheduleVO();
            if (!obtainLock(ActivityLockType.PRODUCT_GROUP_SCHEDULE, scheduleVO.getIdAsString())) {
                return;
            }
            if (!hasDataPermission(PermissionKey.DATA_PRODUCT_GROUP_TYPE, scheduleVO.getGroupType().getCode())) {
                throw new BusinessException(ProductGroupMessageText.DELETE_DENIED);
            }
            ClientServiceFactory.getProductGroupScheduleServices().delete(scheduleVO.getId());
        }
    }

    public void storeProductGroupSchedule(ProductGroupScheduleWrapper scheduleWrapper) throws Exception {
        ProductGroupSchedule schedule = ClientServiceFactory.getProductGroupScheduleServices().readProductGroupSchedule(scheduleWrapper.getScheduleVO().getId());
        RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_DETAIL, schedule);
        RepositoryManager.removeStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_DETAIL_MODIFIED);
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        ProductGroupScheduleQueryFilter queryFilter = getFilter();
        DateFormat formatter = LocaleManager.getShortDateFormatter();
        if (queryFilter.getNextDate() != null) {
            descriptionMap.put("Next Schedule Date", formatter.format(queryFilter.getNextDate()));
        }
        if (queryFilter.getLastDate() != null) {
            descriptionMap.put("Final Schedule Date", formatter.format(queryFilter.getLastDate()));
        }
        if (queryFilter.getGroupType() != null) {
            descriptionMap.put("Type", Translator.getText(queryFilter.getGroupType().toString()));
        }
        if (queryFilter.getDescription() != null) {
            descriptionMap.put("Description", queryFilter.getDescription());
        }
        if (queryFilter.getStoreId() != null) {
            descriptionMap.put("Store", queryFilter.getStoreId().toString());
        }
        if (queryFilter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(queryFilter.getStatus().toString()));
        }
        return descriptionMap;
    }
}
