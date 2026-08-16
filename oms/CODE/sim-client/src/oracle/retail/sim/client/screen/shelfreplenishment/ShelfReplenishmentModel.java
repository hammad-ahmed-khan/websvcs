package oracle.retail.sim.client.screen.shelfreplenishment;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentMessageText;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentQueryFilter;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentStatus;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentVO;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.shelfreplenishment.ShelfReplenishmentServices;

/********************************************************************************************************
 * Shelf Replenishment List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentModel extends SimScreenModel {
    public List<ShelfReplenishmentVO> findShelfReplenishmentVOs() throws Exception {
        return ClientServiceFactory.getShelfReplenishmentServices().findShelfReplenishmentVOs(getFilter());
    }

    public ShelfReplenishmentQueryFilter getFilter() throws BusinessException {
        ShelfReplenishmentQueryFilter filter = (ShelfReplenishmentQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.SHELF_REPLENISHMENT_FILTER);
        if (filter == null) {
            Date today = SimDateUtil.getCurrentDate();
            Date startDate = SimDateUtil.getDateAtStartOfDay(getTimeZone(), today);
            Date endDate = SimDateUtil.getDateAtEndOfDay(getTimeZone(), today);
            filter = BOFactory.createShelfReplenishmentQueryFilter();
            filter.doSetStatus(ShelfReplenishmentStatus.NEW);
            filter.doSetStoreId(getStoreId());
            filter.doSetDateRange(startDate, endDate);
            RepositoryManager.addStateObject(SimClientStateKey.SHELF_REPLENISHMENT_FILTER, filter);
        }
        return filter;
    }

    public void storeShelfReplenishment(ShelfReplenishmentVO shelfReplenishmentVO) throws Exception {
        ShelfReplenishment shelfReplenishment = ClientServiceFactory.getShelfReplenishmentServices().readShelfReplenishment(shelfReplenishmentVO.getId());
        RepositoryManager.addStateObject(SimClientStateKey.SHELF_REPLENISHMENT_DETAIL, shelfReplenishment);
    }

    public void cancelShelfReplenishments(List<ShelfReplenishmentVO> shelfReplenishmentVOs) throws Exception {
        if (checkForNonPendingShelfReplenishment(shelfReplenishmentVOs)) {
            throw new BusinessException(ShelfReplenishmentMessageText.ONLY_PENDED_CAN_CANCEL);
        }
        ShelfReplenishmentServices replenishmentServices = ClientServiceFactory.getShelfReplenishmentServices();
        for (ShelfReplenishmentVO shelfReplenishmentVO : shelfReplenishmentVOs) {
            if (!StringUtility.isNullOrEmpty(shelfReplenishmentVO.getIdAsString())) {
                if (obtainLock(ActivityLockType.SHELF_REPLENISHMENT, shelfReplenishmentVO.getIdAsString())) {
                    replenishmentServices.cancelShelfReplenishment(shelfReplenishmentVO.getId());
                    releaseLock(ActivityLockType.SHELF_REPLENISHMENT, shelfReplenishmentVO.getIdAsString());
                }
            }
        }
    }

    private boolean checkForNonPendingShelfReplenishment(List<ShelfReplenishmentVO> shelfReplenishmentVOs) {
        for (ShelfReplenishmentVO shelfReplenishmentVO : shelfReplenishmentVOs) {
            ShelfReplenishmentStatus status = shelfReplenishmentVO.getStatus();
            if (status != ShelfReplenishmentStatus.NEW && status != ShelfReplenishmentStatus.PENDING_ALTERED) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getDescriptionMap() throws BusinessException {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        ShelfReplenishmentQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getShelfReplenishmentId() != null) {
            descriptionMap.put("Shelf Replenishment List ID", String.valueOf(filter.getShelfReplenishmentId()));
        }
        if (filter.getProductGroupID() != null) {
            descriptionMap.put("Product Group", String.valueOf(filter.getProductGroupID()));
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
        }
        if (filter.getType() != null) {
            descriptionMap.put("Type", Translator.getText(filter.getType().toString()));
        }
        if (filter.getUserId() != null) {
            descriptionMap.put("User", filter.getUserId());
        }
        return descriptionMap;
    }
}
