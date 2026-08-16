package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.invadjustment.InventoryAdjustmentServices;

/********************************************************************************************************
 * Inventory Adjustment List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentListModel extends SimScreenModel {

    public InventoryAdjustmentQueryFilter getFilter() {
        InventoryAdjustmentQueryFilter filter = (InventoryAdjustmentQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER);
        if (filter == null) {
            filter = BOFactory.createInventoryAdjustmentQueryFilter();
            filter.doSetStatus(InventoryAdjustmentStatus.IN_PROGRESS);
            filter.doSetStoreId(getStoreId());
            filter.doSetSearchLimit(getDefaultSearchLimit());
            RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER, filter);
        }
        return filter;
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_INV_ADJUSTMENT);
    }

    public boolean isDeleteFunctionAvailable() {
        return hasPermission(PermissionKey.PC_DELETE_INVENTORY_ADJUSTMENTS);
    }

    public List<InventoryAdjustmentVO> findInventoryAdjustmentVOs() throws Exception {
        return ClientServiceFactory.getInventoryAdjustmentServices().findInventoryAdjustmentVOs(getFilter());
    }

    public Map<String, String> getDescriptionMap() throws Exception {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        InventoryAdjustmentQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getNonSellableTypeId() != null) {
            descriptionMap.put("Sub-bucket", Translator.getText(filter.getNonSellableTypeDescription()));
        }
        if (filter.getReasonId() != null) {
            descriptionMap.put("Reason", Translator.getText(filter.getReasonDescription()));
        }
        if (filter.getUsername() != null) {
            descriptionMap.put("User", filter.getUsername());
        }
        if (filter.getInventoryAdjustmentId() != null) {
            descriptionMap.put("Adjustment ID", filter.getInventoryAdjustmentId().toString());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", new TranslatedObjectDisplayer().getDisplayText(filter.getStatus()));
        }
        if (filter.getTemplateId() != null) {
            descriptionMap.put("Template", Translator.getText(filter.getTemplateDescription()));
        }
        return descriptionMap;
    }

    public void storeAdjustment(InventoryAdjustmentVO vo) throws Exception {
        InventoryAdjustment adjustment = ClientServiceFactory.getInventoryAdjustmentServices().readInventoryAdjustment(vo.getId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT, adjustment);
    }

    public void printInventoryAdjustments(List<InventoryAdjustmentVO> adjustmentVOs) throws Exception {
        Long storeId = getStoreId();
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, ReportFormat.INVENTORY_ADJUSTMENT);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            List<ReportRequest> reportRequests = new ArrayList<ReportRequest>();
            for (InventoryAdjustmentVO adjustmentVO : adjustmentVOs) {
                reportRequests.add(BOFactory.createInventoryAdjustmentReportRequest(adjustmentVO.getId()));
            }
            SimClientPrintUtility.printReportRequests(reportRequests, formatPrinters, InventoryAdjustmentMessageText.REPORT_PRINTED);
        }
    }

    public void cancelInventoryAdjustments(List<InventoryAdjustmentVO> adjustmentVOs) throws Exception {
        InventoryAdjustmentServices services = ClientServiceFactory.getInventoryAdjustmentServices();

        Set<Long> adjustmentIds = new HashSet<>();
        for (InventoryAdjustmentVO adjustmentVO : adjustmentVOs) {
            adjustmentIds.add(adjustmentVO.getId());
        }

        Set<Long> invalidAdjustmentIds = getPermissionFailedAdjustmentIds(adjustmentIds);

        adjustmentIds.removeAll(invalidAdjustmentIds);

        try {
            for (Long adjustmentId : adjustmentIds) {
                if (obtainLock(ActivityLockType.INVENTORY_ADJUSTMENT, String.valueOf(adjustmentId))) {
                    services.cancelInventoryAdjustment(adjustmentId);
                }
            }
        } finally {
            for (Long adjustmentId : adjustmentIds) {
                releaseLock(ActivityLockType.INVENTORY_ADJUSTMENT, String.valueOf(adjustmentId));
            }
        }
        if (invalidAdjustmentIds.size() > 0) {
            throw new BusinessException(CommonMessageText.REASON_PERMISSION_ERROR);
        }
    }

    private Set<Long> getPermissionFailedAdjustmentIds(Set<Long> adjustmentIds) throws Exception {
        Set<Long> failedAdjustmentIds = new HashSet<>();
        Map<Long, Set<Long>> reasonMap = ClientServiceFactory.getInventoryAdjustmentServices().findInventoryAdjustmentReasons(adjustmentIds);
        for (Long adjustmentId : adjustmentIds) {
            Set<Long> reasonIds = reasonMap.get(adjustmentId);
            if (reasonIds != null) {
                for (Long reasonId : reasonIds) {
                    if (!hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, reasonId.toString())) {
                        failedAdjustmentIds.add(adjustmentId);
                    }
                }
            }
        }
        return failedAdjustmentIds;
    }
}
