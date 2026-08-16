package oracle.retail.sim.client.screen.invadjustment;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.invadjustment.InventoryAdjustmentServices;

/********************************************************************************************************
 * Inventory Template List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateListModel extends SimScreenModel {

    public InventoryAdjustmentTemplateQueryFilter getFilter() {
        InventoryAdjustmentTemplateQueryFilter filter = (InventoryAdjustmentTemplateQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_FILTER);
        if (filter == null) {
            filter = BOFactory.createInventoryAdjustmentTemplateQueryFilter();
            filter.doSetStoreId(getStoreId());
            filter.doSetStatus(InventoryAdjustmentTemplateStatus.IN_PROGRESS);
            RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_FILTER, filter);
        }
        return filter;
    }

    public List<InventoryAdjustmentTemplateVO> findTemplates() throws Exception {
        return ClientServiceFactory.getInventoryAdjustmentServices().findTemplateVOs(getFilter());
    }

    public Map<String, String> getDescriptionMap() throws Exception {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        InventoryAdjustmentTemplateQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getTemplateId() != null) {
            descriptionMap.put("Template", filter.getTemplateId().toString());
        }
        if (filter.getDescription() != null) {
            descriptionMap.put("Description", filter.getDescription());
        }
        if (filter.getReasonId() != null) {
            descriptionMap.put("Reason", Translator.getText(getReasonDescription(filter.getReasonId())));
        }
        if (filter.getUsername() != null) {
            descriptionMap.put("User", filter.getUsername());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", new TranslatedObjectDisplayer().getDisplayText(filter.getStatus()));
        }
        return descriptionMap;
    }

    private String getReasonDescription(Long reasonId) throws Exception {
        for (InventoryAdjustmentReason reason : ClientDataCacheUtility.getDisplayableInventoryAdjustmentReasons()) {
            if (reason.getId().equals(reasonId)) {
                return reason.getDescription();
            }
        }
        return null;
    }

    public void storeTemplate(InventoryAdjustmentTemplateVO templateVO) throws Exception {
        InventoryAdjustmentTemplate template = ClientServiceFactory.getInventoryAdjustmentServices().readTemplate(templateVO.getId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT_TEMPLATE, template);
    }

    public void cancelTemplates(List<InventoryAdjustmentTemplateVO> templateVOs) throws Exception {
        InventoryAdjustmentServices services = ClientServiceFactory.getInventoryAdjustmentServices();

        Set<Long> templateIds = new HashSet<>();
        for (InventoryAdjustmentTemplateVO templateVO : templateVOs) {
            templateIds.add(templateVO.getId());
        }

        Set<Long> invalidTemplateIds = getPermissionFailedTemplateIds(templateIds);

        templateIds.removeAll(invalidTemplateIds);

        for (Long templateId : templateIds) {
            if (obtainLock(ActivityLockType.INVENTORY_ADJUSTMENT_TEMPLATE, String.valueOf(templateId))) {
                services.cancelTemplate(templateId);
            }
        }
        if (invalidTemplateIds.size() > 0) {
            throw new BusinessException(CommonMessageText.REASON_PERMISSION_ERROR);
        }
    }

    private Set<Long> getPermissionFailedTemplateIds(Set<Long> templateIds) throws Exception {
        Set<Long> failedTemplateIds = new HashSet<>();
        Map<Long, Set<Long>> reasonMap = ClientServiceFactory.getInventoryAdjustmentServices().findTemplateReasons(templateIds);
        for (Long templateId : templateIds) {
            Set<Long> reasonIds = reasonMap.get(templateId);
            if (reasonIds != null) {
                for (Long reasonId : reasonIds) {
                    if (!hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, reasonId.toString())) {
                        failedTemplateIds.add(templateId);
                    }
                }
            }
        }
        return failedTemplateIds;
    }
}
