package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import extra.retail.sim.client.screen.shipTrailer.DMShipTrailer;
import extra.retail.sim.webservice.shipTrailer.client.DMShipTrailerCaptureClient;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class TransferDetailModel extends SimScreenModel {
    private int expectedLineCount = -1;
    private int receivedLineCount = -1;
    private int damagedLineCount = -1;
    private int discrepantLineCount = -1;

    protected Transfer transfer;
    private Boolean hasRequestedQuantity;

    /****************************************************************************************************
     * Basic Methods
     ***************************************************************************************************/

    public Transfer getTransfer() {
        return transfer;
    }

    public void storeTransferForBillOfLading() {
        RepositoryManager.addStateObject(SimClientStateKey.BILL_OF_LADING_TRANSFER, transfer);
    }

    /****************************************************************************************************
     * Line Item Methods
     ***************************************************************************************************/

    public List<TransferLineItemWrapper> getLineItemWrappers() {
        List<TransferLineItemWrapper> transferItems = new ArrayList<>();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            transferItems.add(ClientWrapperFactory.createTransferLineItemWrapper(transfer, lineItem));
        }
        return transferItems;
    }

    public TransferLineItemWrapper createTransferLineItem() {
        return ClientWrapperFactory.createTransferLineItemWrapper(transfer);
    }

    /****************************************************************************************************
     * Buddy Store Methods
     ***************************************************************************************************/

    public Set<BuddyStore> findBuddyStores() {
        return getSimStore().getBuddyStores();
    }

    public Store findStore(Object store) throws Exception {
        if (store instanceof BuddyStore) {
            return ClientDataCacheUtility.getStore(((BuddyStore) store).getId());
        }
        return (Store) store;
    }

    /****************************************************************************************************
     * Context Type Methods
     ***************************************************************************************************/

    public boolean isContextTypeAvailable() {
        return PermissionManager.hasPermission(PermissionKey.PC_ACCESS_TRANSFER_CONTEXT);
    }

    public List<ContextType> findContextTypes() throws Exception {
        return ClientDataCacheUtility.getContextTypes();
    }

    public boolean isContextTypePromotional() {
        ContextType contextType = transfer.getContextType();
        return contextType != null && contextType.isPromotion();
    }

    /****************************************************************************************************
     * Line Count Summary Information
     ***************************************************************************************************/

    public Integer getExpectedLineCount() {
        if (expectedLineCount == -1) {
            calculateLineCounts();
        }
        return expectedLineCount;
    }

    public Integer getReceivedLineCount() {
        if (receivedLineCount == -1) {
            calculateLineCounts();
        }
        return receivedLineCount;
    }

    public Integer getDamagedLineCount() {
        if (damagedLineCount == -1) {
            calculateLineCounts();
        }
        return damagedLineCount;
    }

    public Integer getDiscrepantLineCount() {
        if (discrepantLineCount == -1) {
            calculateLineCounts();
        }
        return discrepantLineCount;
    }

    private void calculateLineCounts() {
        expectedLineCount = 0;
        receivedLineCount = 0;
        damagedLineCount = 0;
        discrepantLineCount = 0;

        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (lineItem.getTransferQuantityOrZero().isPositive()) {
                expectedLineCount++;
            }
            if (lineItem.getReceivedQuantityOrZero().isPositive()) {
                receivedLineCount++;
            }
            if (lineItem.getDamagedQuantityOrZero().isPositive()) {
                damagedLineCount++;
            }
            if (lineItem.getReceivedQuantity() != null || lineItem.getDamagedQuantity() != null) {
                if (lineItem.getTransferQuantityOrZero().compareTo(lineItem.getReceivedQuantityOrZero().add(lineItem.getDamagedQuantityOrZero())) != 0) {
                    discrepantLineCount++;
                }
            }
        }
    }

    /****************************************************************************************************
     * Activity Locking Methods
     ***************************************************************************************************/

    public boolean obtainTransferLock() throws Exception {
        if (transfer.isNew()) {
            return true;
        }
        return obtainLock(ActivityLockType.TRANSFER, transfer.getIdAsString());
    }

    public boolean checkTransferLock() throws Exception {
        if (transfer.isNew()) {
            return true;
        }
        return confirmLock(ActivityLockType.TRANSFER, transfer.getIdAsString());
    }

    public void clearTransferLock() throws Exception {
        if (transfer.isNew()) {
            return;
        }
        releaseLock(ActivityLockType.TRANSFER, transfer.getIdAsString());
    }

    /****************************************************************************************************
     * Miscellaneous Methods
     ***************************************************************************************************/

    protected boolean confirmDefaultShippedQty() {
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (lineItem.getTransferQuantity() == null) {
                return RConfirmUtility.confirm("Confirmation", TransferMessageText.EMPTY_TRANSFER_LINE_ITEM_CONFIRM);
            }
        }
        return true;
    }
    
    protected boolean containsNoTransferQuantities() {
    	transfer.defaultMissingTransferQuantityToZero();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (!lineItem.getTransferQuantityOrZero().isZero()) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasAllZeroQuantities() {
    	transfer.defaultMissingTransferQuantityToZero();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (!lineItem.getTransferQuantity().isZero()) {
                return false;
            }
        }
        return true;
    }

    public boolean isSubmitAvailable() {
        return !isDispatchShipDirect();
    }

    public boolean isDispatchAvailable() {
        if (transfer.getStatus() == TransferStatus.DISPATCHED) {
            return false;
        }
        if (isSubmitAvailable() && (transfer.getStatus() == TransferStatus.IN_PROGRESS)) {
            return false;
        }
        return true;
    }

    public boolean hasRequestedQuantity() {
        if (hasRequestedQuantity == null) {
            hasRequestedQuantity = Boolean.FALSE;
            for (TransferLineItem lineItem : transfer.getLineItems()) {
                if (lineItem.getRequestedQuantity() != null) {
                    hasRequestedQuantity = Boolean.TRUE;
                    break;
                }
            }
        }
        return hasRequestedQuantity;
    }

    public boolean validateUINsRequiredAtReceivingStore() throws Exception {
        List<String> stockItemIds = new ArrayList<>();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            StockItem stockItem = lineItem.getStockItem();
            if (!stockItem.isSerialNumberRequired()) {
                stockItemIds.add(stockItem.getId());
            }
        }
        return ClientServiceFactory.getUINServices().isUINRequiredAtAutoStore(transfer.getReceivingStore().getId(), stockItemIds);
    }

    public boolean isPartialFulfillDeliveryAllowed() {
        return transfer.isPartialFulfillDeliveryAllowed();
    }

    public void validateSubmitAllowed() throws BusinessException {
        transfer.validateSubmitAllowed(isDispatchSubmitRequired());
    }

    public void validateCancelSubmitAllowed() throws BusinessException {
        transfer.validateCancelSubmitAllowed();
    }

    public void validateDispatchAllowed() throws BusinessException {
        transfer.validateDispatchAllowed(isDispatchSubmitRequired());
    }

    public boolean isDispatchSubmitRequired() {
        return SimConfigManager.getStoreBoolean(StoreConfigKeys.TRANSFER_DISPATCH_VALIDATE, transfer.getSendingStore().getId());
    }

    public boolean isPrintingRequired() {
        return isDispatchSubmitRequired() || isManifestRequired();
    }

    private boolean isDispatchShipDirect() {
        return !SimConfigManager.getStoreBoolean(StoreConfigKeys.TRANSFER_DISPATCH_VALIDATE, transfer.getSendingStore().getId());
    }

    private boolean isManifestRequired() {
        return SimConfigManager.getStoreBoolean(StoreConfigKeys.MANIFEST_STORE_TO_STORE_TRANSFER, transfer.getSendingStore().getId());
    }

    private List<SessionPrinter> getSessionPrinters() {
        return (List<SessionPrinter>) SimRepository.getSessionPrinters();
    }

    /****************************************************************************************************
     * Submit Transfer
     ***************************************************************************************************/

    public void submitTransfer() throws Exception {
        transfer.defaultMissingTransferQuantityToZero();
        if (isPrintingRequired()) {
            ClientServiceFactory.getTransferServices().submitTransfer(transfer, getSessionPrinters());
        } else {
            ClientServiceFactory.getTransferServices().submitTransfer(transfer);
        }
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Cancel Submit Transfer
     ***************************************************************************************************/

    public void cancelSubmit() throws Exception {
        ClientServiceFactory.getTransferServices().cancelSubmitTransfer(transfer.getId());
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Dispatch Transfer
     ***************************************************************************************************/

    public void dispatchTransfer() throws Exception {
    	Long transferId = null;
        if (isPrintingRequired()) {
        	transferId = ClientServiceFactory.getTransferServices().dispatchTransfer(transfer, getSessionPrinters());
        } else {
        	transferId = ClientServiceFactory.getTransferServices().dispatchTransfer(transfer);
        }
        if (transfer.isNew() && RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT) != null) {
            DMShipTrailer shipTrailer = (DMShipTrailer) RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
            if (shipTrailer != null) {
            	shipTrailer.setTransferReturnId(transferId);
            	shipTrailer.setTransfer(true);
            	shipTrailer.setCreatedUser(getUser().getId().toString());
            	DMShipTrailerCaptureClient.getInstance().savaShipTrailer(shipTrailer);
            	RepositoryManager.removeStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
            }
        }
        clearTransferLock();
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Find Missing UINs on Line Items and format the error information about the item.
     ***************************************************************************************************/

    protected List<String> findItemsWithMissingSerialNumbers() {
        List<String> invalidItems = new ArrayList<>();
        boolean isShortDescription = SimConfigManager.isItemShortDescription();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            StockItem stockItem = lineItem.getStockItem();
            if (stockItem.isSerialNumberRequired() && lineItem.getSerialNumbers().isEmpty() && lineItem.getRequestedQuantityOrZero().isZero()) {
                if (isShortDescription) {
                    invalidItems.add(stockItem.getId() + " - " + stockItem.getShortDescription());
                } else {
                    invalidItems.add(stockItem.getId() + " - " + stockItem.getLongDescription());
                }
            }
        }
        return invalidItems;
    }

    public void storeTransferForShipTrailer() {
		if (transfer != null && transfer.getId() != null) {
			RepositoryManager.addStateObject(SimClientStateKey.SHIP_TRAILER_TRANSFER_RETURN_ID, transfer.getId());
		}
		RepositoryManager.addStateObject(SimClientStateKey.SHIP_TRAILER_IS_TRANSFER, true);
	}
}
