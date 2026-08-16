package extra.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferQueryFilter;
import oracle.retail.sim.common.transfer.TransferQueryStatus;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.common.transfer.TransferVO;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.transfer.TransferServices;

import extra.retail.sim.client.core.ExtraSimScreenName;
import extra.retail.sim.webservice.shipTrailer.client.DMShipTrailerCaptureClient;

/********************************************************************************************************
 * Transfer List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraTransferListModel extends SimScreenModel {

    /********************************************************************************************************
     * Basic Methods
     *******************************************************************************************************/

    public List<TransferVO> findTransfers() throws Exception {
        return ClientServiceFactory.getTransferServices().findTransferVOs(getFilter());
    }

    public TransferQueryFilter getFilter() {
        TransferQueryFilter filter = (TransferQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.TRANSFER_FILTER);
        if (filter == null) {
            filter = BOFactory.createTransferQueryFilter();
            filter.doSetStatus(TransferQueryStatus.ACTIVE);
            filter.doSetStoreId(getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_FILTER, filter);
        }
        return filter;
    }

    /********************************************************************************************************
     * STORE TRANSFER METHOD - Lock transfer and determine where to navigate by status.
     *******************************************************************************************************/

    public String storeTransfer(TransferVO transferVO) throws Exception {
        if (transferVO.getReceivingStoreId().equals(getStoreId())) {
            return loadReceivingStoreTransfer(transferVO);
        }
        if (transferVO.getSendingStoreId().equals(getStoreId())) {
            return loadSendingStoreTransfer(transferVO);
        }
        return storeTransferForView(transferVO);
    }

    private String loadReceivingStoreTransfer(TransferVO transferVO) throws Exception {
        if (transferVO.getStatus() == TransferStatus.NEW && hasPermission(PermissionKey.PC_EDIT_TRANSFER_REQUEST)) {
            return storeTransferForEdit(transferVO, ExtraSimScreenName.TRANSFER_REQUEST_SCREEN);
        }
        if (transferVO.getStatus() == TransferStatus.DISPATCHED && hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
            return storeTransferForEdit(transferVO, SimScreenName.TRANSFER_RECEIVE_SCREEN);
        }
        if (transferVO.getStatus() == TransferStatus.RECEIVING && hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
            return storeTransferForEdit(transferVO, SimScreenName.TRANSFER_RECEIVE_SCREEN);
        }
        return storeTransferForView(transferVO);
    }

    private String loadSendingStoreTransfer(TransferVO transferVO) throws Exception {
        if (transferVO.getStatus() == TransferStatus.PENDING && hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
            return storeTransferForEdit(transferVO, SimScreenName.TRANSFER_APPROVE_SCREEN);
        }
        if (transferVO.getStatus() == TransferStatus.IN_PROGRESS && hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
            return storeTransferForEdit(transferVO, ExtraSimScreenName.TRANSFER_DISPATCH_SCREEN);
        }
        return storeTransferForView(transferVO);
    }

    private String storeTransferForEdit(TransferVO transferVO, String screenToReturn) throws Exception {
        Transfer transfer = ClientServiceFactory.getTransferServices().readTransfer(transferVO.getId());
        if (transfer == null) {
            throw new BusinessException(TransferMessageText.ALREADY_CANCELLED_ERROR);
        }
        if (transferVO.getStatus() == transfer.getStatus()) {
            if (obtainLock(ActivityLockType.TRANSFER, transferVO.getIdAsString())) {
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_TRANSFER, transfer);
                return screenToReturn;
            }
        }
        return storeTransferForView(transferVO);
    }

    private String storeTransferForView(TransferVO transferVO) throws Exception {
        Transfer transfer = ClientServiceFactory.getTransferServices().readTransferForViewOnly(getStoreId(), transferVO.getId());
        if (transfer == null) {
            throw new BusinessException(TransferMessageText.ALREADY_CANCELLED_ERROR);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_TRANSFER, transfer);
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_VIEW_ONLY_MODE, true);

        return ExtraSimScreenName.TRANSFER_VIEW_SCREEN;
    }

    /********************************************************************************************************
     * Print Methods
     *******************************************************************************************************/

    public List<ReportRequest> getPrintRequests(List<TransferVO> transfers) {
        List<ReportRequest> reportRequests = new ArrayList<ReportRequest>();
        for (TransferVO transferVO : transfers) {
            reportRequests.add(BOFactory.createTransferReportRequest(transferVO.getId()));
        }
        return reportRequests;
    }

    /********************************************************************************************************
     * Delete Methods
     *******************************************************************************************************/

    public boolean cancelTransfers(List<TransferVO> transfers) throws Exception {
        for (TransferVO transferVO : transfers) {
            if (!obtainLock(ActivityLockType.TRANSFER, transferVO.getIdAsString())) {
                return false;
            }
            TransferStatus status = transferVO.getStatus();
            if (transferVO.getSendingStoreId().equals(getStoreId())) {
                if (status == TransferStatus.NEW || status == TransferStatus.PENDING) {
                    throw new BusinessException(TransferMessageText.ONLY_REJECT_ACCEPT_ALLOWED);
                }
                if (status == TransferStatus.REJECTED) {
                    throw new BusinessException(TransferMessageText.DELETE_REQUEST_NOT_ALLOWED);
                }

                if (status == TransferStatus.SUBMITTED) {
                    throw new BusinessException(TransferMessageText.DELETE_SUBMITTED_NOT_ALLOWED);
                }

                if (status == TransferStatus.IN_PROGRESS) {
                    ClientServiceFactory.getTransferServices().cancelTransfer(getStoreId(), transferVO.getId());
                }
            }
            if (transferVO.getReceivingStoreId().equals(getStoreId())) {
                if ((status == TransferStatus.IN_PROGRESS) || (status == TransferStatus.SUBMITTED)) {
                    throw new BusinessException(TransferMessageText.DELETE_INBOUND_PICKING_NOT_ALLOWED);
                }

                if ((status == TransferStatus.REJECTED)) {
                    throw new BusinessException(TransferMessageText.DELETE_REQUEST_NOT_ALLOWED);
                }
                if ((status == TransferStatus.DISPATCHED) || (status == TransferStatus.RECEIVING) || (status == TransferStatus.RECEIVED)) {
                    throw new BusinessException(TransferMessageText.CANNOT_DELETE_DISPATCHED_TRANSFERS);
                }
                if ((status == TransferStatus.NEW) || (status == TransferStatus.PENDING)) {
                    ClientServiceFactory.getTransferServices().cancelTransfer(getStoreId(), transferVO.getId());
                }
            }
        }
        return true;
    }

    /********************************************************************************************************
     * Dispatch Methods
     *******************************************************************************************************/

    public void dispatchTransfers(List<TransferVO> transfers) throws Exception {
        TransferServices transferServices = ClientServiceFactory.getTransferServices();
        List<SessionPrinter> sessionPrinters = (List<SessionPrinter>) SimRepository.getSessionPrinters();

        for (TransferVO transferVO : transfers) {
            if (obtainLock(ActivityLockType.TRANSFER, transferVO.getIdAsString())) {
                if (isPrintingRequired(transferVO.getSendingStoreId())) {
                    transferServices.dispatchTransfer(transferVO.getId(), sessionPrinters);
                } else {
                    transferServices.dispatchTransfer(transferVO.getId());
                }
                releaseLock(ActivityLockType.TRANSFER, transferVO.getIdAsString());
            }
        }
    }

    /********************************************************************************************************
     * Filter Description
     *******************************************************************************************************/

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap();
        TransferQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getBuddyStoreId() != null) {
            descriptionMap.put("Transfer Location", filter.getBuddyStoreId().toString());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", filter.getStatus().toString());
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getUserId() != null) {
            descriptionMap.put("User", filter.getUserId());
        }
        if (filter.getTransferId() != null) {
            descriptionMap.put("ID", String.valueOf(filter.getTransferId()));
        }
        if (filter.getTransferPhase() != null) {
            descriptionMap.put("Type", filter.getTransferPhase().toString());
        }
        if (filter.getExternalId() != null) {
            descriptionMap.put("External Id", filter.getExternalId().toString());
        }
        if (filter.getContextType() != null) {
            descriptionMap.put("Context Type", filter.getContextType().getName());
        }

        if (filter.getContextValue() != null) {
            descriptionMap.put("Context Value", filter.getContextValue());
        }
        if (filter.getFulfillmentOrderExternalId() != null) {
            descriptionMap.put("Fulfillment Order Id", filter.getFulfillmentOrderExternalId());
        }
        if (filter.getCustomerOrderId() != null) {
            descriptionMap.put("Customer Order Id", filter.getCustomerOrderId());
        }
        return descriptionMap;
    }

    public boolean hasShipTrailer(Long transferId) {
		return !isShipTrailerEnabled() || DMShipTrailerCaptureClient.getInstance().hasShipTrailer(transferId);
	}

	public boolean isShipTrailerEnabled() {
		return DMShipTrailerCaptureClient.getInstance().isShipTrailerEnabled(getStoreId());
	}

    /*******************************************************************
     * Helper methods
     * *****************************************************************/

    private boolean isPrintingRequired(Long sendingStoreId) {
        return !isDispatchShipDirect(sendingStoreId) || isManifestRequired(sendingStoreId);
    }

    public boolean isValidForDispatch(TransferStatus status, Long sendingStoreId) throws BusinessException {
        return isDispatchShipDirect(sendingStoreId) ? status == TransferStatus.IN_PROGRESS : status == TransferStatus.SUBMITTED;
    }

    public TransferMessageText getDispatchWarning(Long sendingStoreId) {
        return isDispatchShipDirect(sendingStoreId) ? TransferMessageText.DISPATCH_WARNING : TransferMessageText.ONLY_SUBMITTED_CAN_DISPATCH;
    }

    private boolean isDispatchShipDirect(Long sendingStoreId) {
        return !SimConfigManager.getStoreBoolean(StoreConfigKeys.TRANSFER_DISPATCH_VALIDATE, sendingStoreId);
    }

    private boolean isManifestRequired(Long sendingStoreId) {
        return SimConfigManager.getStoreBoolean(StoreConfigKeys.MANIFEST_STORE_TO_STORE_TRANSFER, sendingStoreId);
    }
}
