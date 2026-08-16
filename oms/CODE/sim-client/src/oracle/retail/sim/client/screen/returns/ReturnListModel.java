package oracle.retail.sim.client.screen.returns;

import java.util.ArrayList;
import java.util.Date;
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
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.FinisherVO;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.source.SourceVO;
import oracle.retail.sim.common.source.SupplierVO;
import oracle.retail.sim.common.source.WarehouseVO;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;
import oracle.retail.sim.common.stockreturn.ReturnQueryFilter;
import oracle.retail.sim.common.stockreturn.ReturnQueryStatus;
import oracle.retail.sim.common.stockreturn.ReturnStatus;
import oracle.retail.sim.common.stockreturn.ReturnVO;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.stockreturn.ReturnServices;

/********************************************************************************************************
 * Return List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnListModel extends SimScreenModel {

    public Store getReturnStore() {
        return getStore();
    }

    public List<ReturnVO> findReturnVOs() throws Exception {
        return ClientServiceFactory.getReturnServices().findReturnVOs(getFilter());
    }

    public ReturnQueryFilter getFilter() {
        ReturnQueryFilter filter = (ReturnQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.RETURN_FILTER);
        if (filter == null) {
            filter = BOFactory.createStockReturnQueryFilter();
            filter.doSetStatus(ReturnQueryStatus.ACTIVE);
            filter.doSetStoreId(getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.RETURN_FILTER, filter);
        }
        return filter;
    }

    public void printReturns(List<ReturnVO> returnVOs) throws Exception {
        List<ReportFormat> formats = new ArrayList<ReportFormat>();
        formats.add(ReportFormat.STOCK_RETURN);
        formats.add(ReportFormat.STOCK_RETURN_BOL);
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(getStoreId(), formats);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            List<ReportRequest> reportRequests = new ArrayList<ReportRequest>();
            for (ReturnVO returnVO : returnVOs) {
                reportRequests.add(BOFactory.createReturnReportRequest(returnVO.getId()));
            }
            SimClientPrintUtility.printReportRequests(reportRequests, formatPrinters, ReturnMessageText.REPORT_PRINTED);
        }
    }

    private Return loadReturn(ReturnVO returnVO) throws Exception {
        return ClientServiceFactory.getReturnServices().readReturn(returnVO.getId());
    }

    public void deleteReturns(List<ReturnVO> returnVOs) throws Exception {
        boolean hasSupplierPermission = hasSupplierPermissions();
        boolean hasWarehousePermission = hasWarehousePermissions();
        boolean hasFinisherPermission = hasFinisherPermissions();

        List<Long> returnIds = new ArrayList<>();
        for (ReturnVO returnVO : returnVOs) {
            SourceVO sourceVO = returnVO.getDestination();
            if (sourceVO instanceof SupplierVO && !hasSupplierPermission) {
                throw new BusinessException(ReturnMessageText.NO_DELETE_PERMISSION);
            }
            if (sourceVO instanceof WarehouseVO && !hasWarehousePermission) {
                throw new BusinessException(ReturnMessageText.NO_DELETE_PERMISSION);
            }
            if (sourceVO instanceof FinisherVO && !hasFinisherPermission) {
                throw new BusinessException(ReturnMessageText.NO_DELETE_PERMISSION);
            }
            ReturnStatus status = returnVO.getStatus();
            if (status != ReturnStatus.REQUESTED && status != ReturnStatus.PENDING) {
                throw new BusinessException(ReturnMessageText.INVALID_DELETE_STATE);
            }
            returnIds.add(returnVO.getId());
        }
        ReturnServices returnServices = ClientServiceFactory.getReturnServices();

        Set<Long> invalidReturnIds = getPermissionFailedReturnIds(returnIds);
        returnIds.removeAll(invalidReturnIds);

        for (Long returnId : returnIds) {
            if (obtainLock(ActivityLockType.RETURN, returnId.toString())) {
                returnServices.cancelReturn(returnId);
                releaseLock(ActivityLockType.RETURN, returnId.toString());
            }
        }
        if (invalidReturnIds.size() > 0) {
            throw new BusinessException(CommonMessageText.REASON_PERMISSION_ERROR);
        }
    }

    private void validateStatus(ReturnStatus status) throws Exception {
        boolean isDispatchShipDirect = !getStoreBoolean(StoreConfigKeys.RETURN_VALIDATE_DISPATCH);
        if (isDispatchShipDirect) {
            if (status != ReturnStatus.PENDING) {
                throw new BusinessException(ReturnMessageText.INVALID_DISPATCH_STATE);
            }
        } else {
            if (status != ReturnStatus.SUBMITTED) {
                throw new BusinessException(ReturnMessageText.INVALID_DISPATCH_STATE_VALIDATE);
            }
        }
    }

    public void dispatchReturns(List<ReturnVO> returnVOs) throws Exception {
        boolean hasSupplierPermission = hasSupplierPermissions();
        boolean hasWarehousePermission = hasWarehousePermissions();
        boolean hasFinisherPermission = hasFinisherPermissions();

        List<Long> returnIds = new ArrayList<>();
        for (ReturnVO returnVO : returnVOs) {
            SourceVO sourceVO = returnVO.getDestination();
            if (sourceVO instanceof SupplierVO && !hasSupplierPermission) {
                throw new BusinessException(ReturnMessageText.NO_DISPATCH_PERMISSION);
            }
            if (sourceVO instanceof WarehouseVO && !hasWarehousePermission) {
                throw new BusinessException(ReturnMessageText.NO_DISPATCH_PERMISSION);
            }
            if (sourceVO instanceof FinisherVO && !hasFinisherPermission) {
                throw new BusinessException(ReturnMessageText.NO_DISPATCH_PERMISSION);
            }
            ReturnStatus status = returnVO.getStatus();
            validateStatus(status);
            Date notAfterDate = returnVO.getNotAfterDate();
            if (notAfterDate != null && notAfterDate.compareTo(SimDateUtil.getCurrentDateAtStartOfDay(getTimeZone())) < 0) {
                throw new BusinessException(ReturnMessageText.BEYOND_END_DATE_ERROR);
            }

            if (sourceVO instanceof SupplierVO) {
                if (((SupplierVO) sourceVO).isInactive()) {
                    if (!RConfirmUtility.confirm("Inactive supplier confirmation", ReturnMessageText.INACTIVE_SUPPLIER_CONFIRM)) {
                        continue;
                    }
                }
            }
            returnIds.add(returnVO.getId());
        }
        ReturnServices returnServices = ClientServiceFactory.getReturnServices();

        Set<Long> invalidReturnIds = getPermissionFailedReturnIds(returnIds);
        returnIds.removeAll(invalidReturnIds);

        for (Long returnId : returnIds) {
            if (obtainLock(ActivityLockType.RETURN, returnId.toString())) {
                returnServices.dispatchReturn(returnId);
                releaseLock(ActivityLockType.RETURN, returnId.toString());
            }
        }
        if (invalidReturnIds.size() > 0) {
            throw new BusinessException(CommonMessageText.REASON_PERMISSION_ERROR);
        }
    }

    private Set<Long> getPermissionFailedReturnIds(List<Long> returnIds) throws Exception {
        Set<Long> failedReturnIds = new HashSet<>();
        Map<Long, Set<Long>> reasonMap = ClientServiceFactory.getReturnServices().findReturnReasons(returnIds);
        for (Long returnId : returnIds) {
            Set<Long> reasonIds = reasonMap.get(returnId);
            if (reasonIds != null) {
                for (Long reasonId : reasonIds) {
                    if (!hasDataPermission(PermissionKey.DATA_RETURN_REASON_CODE, reasonId)) {
                        failedReturnIds.add(returnId);
                    }
                }
            }
        }
        return failedReturnIds;
    }

    public boolean isDeleteNotAvailable() {
        return !hasSupplierPermissions() && !hasWarehousePermissions();
    }

    public boolean isDispatchNotAvailable() {
        return !hasSupplierPermissions() && !hasWarehousePermissions();
    }

    private boolean hasSupplierPermissions() {
        return hasDataPermission(PermissionKey.DATA_RETURN_SOURCE, SourceType.SUPPLIER.getCode());
    }

    private boolean hasWarehousePermissions() {
        return hasDataPermission(PermissionKey.DATA_RETURN_SOURCE, SourceType.WAREHOUSE.getCode());
    }

    private boolean hasFinisherPermissions() {
        return hasDataPermission(PermissionKey.DATA_RETURN_SOURCE, SourceType.FINISHER.getCode());
    }

    public void storeReturn(ReturnVO returnVO) throws Exception {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_RETURN, loadReturn(returnVO));
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        ReturnQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getReturnId() != null) {
            descriptionMap.put("Return Number", String.valueOf(filter.getReturnId()));
        }
        if (filter.getSupplierId() != null) {
            descriptionMap.put("Supplier", filter.getSupplierId());
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getWarehouseId() != null) {
            descriptionMap.put("Warehouse", filter.getWarehouseId());
        }
        if (filter.getFinisherId() != null) {
            descriptionMap.put("Finisher", filter.getFinisherId());
        }
        if (filter.getAuthCode() != null) {
            descriptionMap.put("Authorization Number", filter.getAuthCode());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
        }
        if (filter.getReason() != null) {
            descriptionMap.put("Reason", Translator.getText(filter.getReason().getDescription()));
        }
        if (filter.getUserId() != null) {
            descriptionMap.put("User", filter.getUserId());
        }
        if (filter.getExternalId() != null) {
            descriptionMap.put("External Id", filter.getExternalId());
        }
        if (filter.getContextType() != null) {
            descriptionMap.put("Context Type", filter.getContextType().getName());
        }
        if (filter.getContextValue() != null) {
            descriptionMap.put("Context Value", filter.getContextValue());
        }
        return descriptionMap;
    }
}
