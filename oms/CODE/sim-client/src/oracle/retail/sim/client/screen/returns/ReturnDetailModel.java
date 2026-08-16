package oracle.retail.sim.client.screen.returns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import extra.retail.sim.client.screen.shipTrailer.DMShipTrailer;
import extra.retail.sim.webservice.shipTrailer.client.DMShipTrailerCaptureClient;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierRole;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnLineItem;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;
import oracle.retail.sim.common.stockreturn.ReturnProperty;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.stockreturn.ReturnStatus;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Return Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnDetailModel extends SimScreenModel {
    private Return stockReturn;
    private boolean viewOnly;

    public void loadStockReturn() {
        stockReturn = (Return) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_RETURN);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_RETURN);
    }

    public Return getStockReturn() {
        return stockReturn;
    }

    // If type changed, must clear destination from repository
    public void createReturn(SourceType type) throws Exception {
        stockReturn = BOFactory.createReturn(getStoreId(), type);
        stockReturn.doSetCreateUser(getUserName());
        if (stockReturn.getBillOfLading().getCarrierRole() == ShipmentCarrierRole.THIRD_PARTY) {
            stockReturn.getBillOfLading().doSetCarrier(ClientServiceFactory.getShipmentServices().findCarrier(ShipmentCarrier.OTHER_CARRIER_CODE));
        }
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_WAREHOUSE);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_FINISHER);
    }

    public List<SourceType> loadReturnTypes() {
        List<SourceType> validTypes = new ArrayList<>(3);
        if (hasWarehousePermissions()) {
            validTypes.add(SourceType.WAREHOUSE);
        }
        if (hasSupplierPermissions()) {
            validTypes.add(SourceType.SUPPLIER);
        }
        if (isFinishersEnabled() && hasReturnToFinisherPermissions() && hasFinisherPermissions()) {
            validTypes.add(SourceType.FINISHER);
        }
        return validTypes;
    }

    public boolean isReturnUnmodifiable() {
        ReturnStatus status = stockReturn.getStatus();
        if (status == ReturnStatus.CANCELED || status == ReturnStatus.DISPATCHED || status == ReturnStatus.SUBMITTED) {
            return true;
        }
        if (!stockReturn.isNew() && !hasPermission(PermissionKey.PC_EDIT_RETURN)) {
            return true;
        }
        if (!hasPermissionForSource()) {
            return true;
        }
        for (ReturnLineItem lineItem : stockReturn.getLineItems()) {
            if ((lineItem.getReason() != null) && (lineItem.getReason().getCode() != null)) {
                if (!hasDataPermission(PermissionKey.DATA_RETURN_REASON_CODE, lineItem.getReason().getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isViewOnly() {
        return viewOnly;
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
    }

    public boolean hasNotAfterDatePast() {
        Date notAfterDate = stockReturn.getNotAfterDate();
        return notAfterDate != null && notAfterDate.compareTo(SimDateUtil.getCurrentDateAtStartOfDay(getTimeZone())) < 0;
    }

    public boolean isSupplierReturn() {
        return stockReturn.isVendorReturn();
    }

    public boolean isWarehouseReturn() {
        return stockReturn.isWarehouseReturn();
    }

    public boolean isFinisherReturn() {
        return stockReturn.isFinisherReturn();
    }

    public boolean isReturnRequest() {
        return stockReturn.isRequest();
    }

    public boolean isStatusSubmitted() {
        return stockReturn.getStatus() == ReturnStatus.SUBMITTED;
    }

    public boolean isPromotion() {
        ContextType contextType = stockReturn.getContextType();
        return contextType != null && contextType.isPromotion();
    }

    public boolean isAddItemNotAvailable() {
        if (!stockReturn.isNew() && !hasPermission(PermissionKey.PC_ADD_ITEM_RETURN)) {
            return true;
        }
        if (stockReturn.isRequest()) {
            if (!SimConfigManager.getBoolean(SimConfigManager.ADD_ITEM_TO_RETURN_REQUESTS)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRemoveItemNotAvailable() {
        return !hasPermissionForSource();
    }

    public boolean isSubmitNotAvailable() {
        if (isDispatchShipDirect()) {
            return true;
        }
        ReturnStatus status = stockReturn.getStatus();
        if (status == ReturnStatus.SUBMITTED || status == ReturnStatus.CANCELED || status == ReturnStatus.DISPATCHED) {
            return true;
        }
        return false;
    }

    public boolean isCancelSubmitNotAvailable() {
        ReturnStatus status = stockReturn.getStatus();
        if (status != ReturnStatus.SUBMITTED) {
            return true;
        }
        return false;
    }

    public boolean isDispatchNotAvailable() {
        if (!hasPermissionForSource()) {
            return false;
        }
        ReturnStatus status = stockReturn.getStatus();
        if (isDispatchShipDirect()) {
            return status != ReturnStatus.PENDING && status != ReturnStatus.REQUESTED;
        }
        return status != ReturnStatus.SUBMITTED;
    }

    public boolean hasSourcePermissionForCreate() {
        if (RepositoryManager.getStateObject(SimClientStateKey.SELECTED_RETURN) == null) {
            return hasSupplierPermissions() || hasWarehousePermissions() || hasFinisherPermissions();
        }
        return true;
    }

    private boolean hasPermissionForSource() {
        if (isSupplierReturn()) {
            return hasSupplierPermissions();
        }
        if (isWarehouseReturn()) {
            return hasWarehousePermissions();
        }
        return hasFinisherPermissions();
    }

    public boolean isFinishersEnabled() {
        return SimConfigManager.getBoolean(SimConfigManager.EXTERNAL_FINISHER_ENABLED);
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

    private boolean hasReturnToFinisherPermissions() {
        return hasPermission(PermissionKey.PC_RETURN_TO_FINISHERS);
    }

    public boolean isContextFieldEditable() {
        return hasPermission(PermissionKey.PC_RETURN_CONTEXT_FIELD_EDITABLE);
    }

    public List<Warehouse> getAllWarehouses() throws Exception {
        return new ArrayList<Warehouse>(ClientDataCacheUtility.getAllWarehouses().values());
    }

    public List<ContextType> getAllContextTypes() throws Exception {
        return ClientDataCacheUtility.getContextTypes();
    }

    public List<ReturnLineItemWrapper> getReturnLineItems() {
        List<ReturnLineItemWrapper> returnItems = new ArrayList<>();
        for (ReturnLineItem returnLineItem : stockReturn.getLineItems()) {
            returnItems.add(ClientWrapperFactory.createReturnLineItemWrapper(stockReturn, returnLineItem));
        }
        return returnItems;
    }

    public boolean isContextTypeModifiable() {
        return stockReturn.isPropertyModifiable(ReturnProperty.CONTEXT_TYPE);
    }

    public boolean isDestinationModifiable() {
        return stockReturn.isPropertyModifiable(ReturnProperty.DESTINATION);
    }

    public boolean isAuthorizationModifiable() {
        return stockReturn.isPropertyModifiable(ReturnProperty.AUTHORIZATION_CODE);
    }

    public boolean isCommentsModifiable() {
        return stockReturn.isPropertyModifiable(ReturnProperty.COMMENTS);
    }

    private boolean isLineItemModifiable() {
        return stockReturn.isPropertyModifiable("returnLineItem");
    }

    public boolean isScannerAvailable() {
        if (viewOnly || isReturnUnmodifiable()) {
            return false;
        }
        return true;
    }

    public boolean isAuthorizationMissing() {
        if (stockReturn.isVendorReturn()) {
            Supplier supplier = (Supplier) stockReturn.getDestination();
            if (supplier.isAuthorizationRequired()) {
                return StringHelper.isNullOrEmpty(stockReturn.getAuthorizationCode());
            }
        }
        return false;
    }

    public ReturnLineItemWrapper buildNewLineItemWrapper() {
        return ClientWrapperFactory.createReturnLineItemWrapper(stockReturn);
    }

    public ReturnLineItemWrapper buildNewLineItemWrapper(ReturnReason defaultReason) {
        return ClientWrapperFactory.createReturnLineItemWrapper(stockReturn, defaultReason);
    }

    public void setDestination(Source destination) throws BusinessException {
        stockReturn.setDestination(destination);
    }

    public void setComments(String comments) throws BusinessException {
        stockReturn.setComments(comments);
    }

    public void setAuthorization(String authCode) throws BusinessException {
        stockReturn.setAuthorizationCode(authCode);
    }

    public void setContextType(ContextType contextType) throws BusinessException {
        stockReturn.setContextType(contextType);
    }

    public void setContextValue(String contextValue) throws BusinessException {
        stockReturn.setContextValue(contextValue);
    }

    public List<ReturnReason> findWarehouseReturnReasons(boolean isUseAvailable) throws Exception {
        List<ReturnReason> availableReasons = new ArrayList<>();
        for (ReturnReason returnReason : ClientDataCacheUtility.getWarehouseReturnReasons()) {
            if (returnReason.isUseAvailable() == isUseAvailable) {
                if (hasDataPermission(PermissionKey.DATA_RETURN_REASON_CODE, returnReason.getId())) {
                    availableReasons.add(returnReason);
                }
            }
        }
        return availableReasons;
    }

    public List<ReturnReason> findSupplierReturnReasons() throws Exception {
        List<ReturnReason> availableReasons = new ArrayList<>();
        for (ReturnReason returnReason : ClientDataCacheUtility.getSupplierReturnReasons()) {
            if (hasDataPermission(PermissionKey.DATA_RETURN_REASON_CODE, returnReason.getId())) {
                availableReasons.add(returnReason);
            }
        }
        return availableReasons;
    }

    public List<ReturnReason> findFinisherReturnReasons(boolean isUseAvailable) throws Exception {
        List<ReturnReason> availableReasons = new ArrayList<>();
        for (ReturnReason returnReason : ClientDataCacheUtility.getFinisherReturnReasons()) {
            if (returnReason.isUseAvailable() == isUseAvailable) {
                if (hasDataPermission(PermissionKey.DATA_RETURN_REASON_CODE, returnReason.getId())) {
                    availableReasons.add(returnReason);
                }
            }
        }
        return availableReasons;
    }

    public void storeDestinationInRepository() {
        Source source = stockReturn.getDestination();
        if (source != null) {
            if (stockReturn.isVendorReturn()) {
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_SUPPLIER, source);
            } else if (stockReturn.isWarehouseReturn()) {
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_WAREHOUSE, source);
            } else if (stockReturn.isFinisherReturn()) {
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FINISHER, source);
            }
        }
    }

    public boolean isAddItemAllowed(List<ReturnLineItemWrapper> returnItems) throws BusinessException {
        if (isDispatchedOrCancelled() || !isLineItemModifiable()) {
            return false;
        }
        if (containsNewLineItemWrapper(returnItems)) {
            return false;
        }
        if (stockReturn.getNumberOfLineItems() > 0) {
            stockReturn.isAddLineItemAllowed();
        }
        return true;
    }

    private boolean isDispatchedOrCancelled() {
        return stockReturn.getStatus() == ReturnStatus.DISPATCHED || stockReturn.getStatus() == ReturnStatus.CANCELED;
    }

    private boolean containsNewLineItemWrapper(List<ReturnLineItemWrapper> wrappers) {
        for (int i = wrappers.size() - 1; i >= 0; i--) {
            if (wrappers.get(i).isNewWrapper()) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeleteItemAllowed() {
        if (isDispatchedOrCancelled() || !isLineItemModifiable()) {
            return false;
        }
        return true;
    }

    // If the user tries to delete a row that was added with a use unavailable conflict the lineItem is null so skip it.
    public boolean includesExternallyCreatedItems(List<ReturnLineItemWrapper> wrappers) {
        for (ReturnLineItemWrapper wrapper : wrappers) {
            ReturnLineItem lineItem = wrapper.getLineItem();
            if (lineItem != null && lineItem.isRequest()) {
                return !lineItem.isSimCreated();
            }
        }
        return false;
    }

    public void deleteReturnLineItem(ReturnLineItemWrapper wrapper) throws BusinessException {
        if (wrapper.isNewWrapper()) {
            return;
        }
        ReturnLineItem lineItem = wrapper.getLineItem();
        lineItem.removeQuantities();
        stockReturn.removeLineItem(lineItem);
    }

    /*
     * ReturnLineItemWrapper throws a reset table exception in order to reset focus in the table. Since this
     * usage is outside the table, we must catch the exception.
     */
    public void updateExistingLineItem(ReturnLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (wrapper.getReason() == null) {
            throw new BusinessException(ReturnMessageText.MISSING_REASON);
        }
        if (barcodeItem.isSerialNumberRequired()) {
            updateExistingLineItemUin(wrapper, barcodeItem);
        } else {
            updateExistingLineItemQty(wrapper, barcodeItem);
        }
    }

    private void updateExistingLineItemQty(ReturnLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.getQuantity().isZero()) {
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        }
        try {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setQuantity(wrapper.getQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setQuantity(wrapper.getQuantityOrZero().add(barcodeItem.getQuantity()));
            }
        } catch (SimTableResetFocusException exception) {
            UILog.debug(getClass(), exception);
        }
    }

    private void updateExistingLineItemUin(ReturnLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        String uinLabel = barcodeItem.getStockItem().getUINLabel();
        if (StringHelper.isNullOrEmpty(barcodeItem.getUin())) {
            throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel, RErrorSeverity.WARNING);
        }
        SerialNumberValue serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValue(barcodeItem.getId(), barcodeItem.getUin());
        if (serialNumber == null) {
            Object[] values = new Object[3];
            values[0] = uinLabel;
            values[1] = barcodeItem.getUin();
            values[2] = barcodeItem.getId();
            throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
        }
        for (SerialNumberValue removedValue : wrapper.getRemovedSerialNumbers()) {
            if (removedValue.getUin().equals(serialNumber.getUin())) {
                wrapper.addSerialNumber(removedValue);
                return;
            }
        }
        if (isDuplicateSerialNumber(barcodeItem.getId(), serialNumber)) {
            throw new UIException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel, RErrorSeverity.WARNING);
        }
        ReturnIsValidSerialNumberRule.execute(stockReturn, wrapper, serialNumber);
        wrapper.addSerialNumber(serialNumber);
    }

    private boolean isDuplicateSerialNumber(String itemId, SerialNumberValue newSerialNumber) {
        for (ReturnLineItem lineItem : stockReturn.getLineItems()) {
            if (!lineItem.getStockItem().getId().equals(itemId)) {
                continue;
            }
            for (SerialNumberValue existingSerialNumber : lineItem.getSerialNumbers()) {
                if (existingSerialNumber.getUinId().equals(newSerialNumber.getUinId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean stockReturnHasNoQuantity() {
        if (stockReturn.getStatus() != ReturnStatus.CANCELED) {
            return stockReturn.areLineItemQuantitiesAllZero();
        }
        return false;
    }

    public void storeReturnForBillOfLading() {
        RepositoryManager.addStateObject(SimClientStateKey.BILL_OF_LADING_RETURN, stockReturn);
    }

    public boolean isValidForDone() throws BusinessException {
        ReturnStatus status = stockReturn.getStatus();
        return status != ReturnStatus.DISPATCHED && status != ReturnStatus.CANCELED && stockReturn.isCoherent();
    }

    public boolean isValidForDispatch() throws BusinessException {
        if (!stockReturn.isCoherent()) {
            return false;
        }

        ReturnStatus status = stockReturn.getStatus();
        if (status == ReturnStatus.CANCELED || status == ReturnStatus.DISPATCHED) {
            return false;
        }
        if (isDispatchShipDirect()) {
            return status == ReturnStatus.PENDING || status == ReturnStatus.REQUESTED;
        }
        return status == ReturnStatus.SUBMITTED;
    }

    private boolean isDispatchShipDirect() {
        return !getStoreBoolean(StoreConfigKeys.RETURN_VALIDATE_DISPATCH);
    }

    public boolean isValidForSubmit() throws BusinessException {
        ReturnStatus status = stockReturn.getStatus();
        return status != ReturnStatus.DISPATCHED && status != ReturnStatus.CANCELED && status != ReturnStatus.SUBMITTED && stockReturn.isCoherent();
    }

    public boolean isValidForCancelSubmitReturn() {
        return stockReturn.getStatus() == ReturnStatus.SUBMITTED;
    }

    public boolean isQtyPopulatedForAllItems(List<ReturnLineItemWrapper> wrappers) {
        if (stockReturn.isRequest()) {
            return true;
        }
        for (ReturnLineItemWrapper lineItem : wrappers) {
            Quantity quantity = lineItem.getQuantityBasedOnUom();
            if (quantity != null && quantity.equals(Quantity.ZERO)) {
                return false;
            }
        }
        return true;
    }

    public void cancelReturn() throws Exception {
        if (stockReturn.isNew()) {
            return;
        }
        ClientServiceFactory.getReturnServices().cancelReturn(stockReturn.getId());
        RepositoryManager.addStateObject(SimClientStateKey.RETURN_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public void updateReturn() throws Exception {
        if (stockReturn.getStatus() == ReturnStatus.REQUESTED) {
            stockReturn.setStatus(ReturnStatus.PENDING);
        }
        if (stockReturn.isNew()) {
            Long returnId = ClientServiceFactory.getReturnServices().insertReturn(stockReturn);
            DMShipTrailer shipTrailer = (DMShipTrailer) RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
            if (shipTrailer != null) {
            	shipTrailer.setTransferReturnId(returnId);
            	shipTrailer.setTransfer(false);
            	shipTrailer.setCreatedUser(getUser().getId().toString());
            	DMShipTrailerCaptureClient.getInstance().savaShipTrailer(shipTrailer);
            }
        } else {
            ClientServiceFactory.getReturnServices().updateReturn(stockReturn);
        }
        RepositoryManager.addStateObject(SimClientStateKey.RETURN_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public void dispatchStockReturn() throws Exception {
        List<SessionPrinter> sessionPrinters = getSessionPrinters();
        DMShipTrailer shipTrailer = (DMShipTrailer) RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
        if (stockReturn.isNew() && shipTrailer != null) {
        	Long returnId = ClientServiceFactory.getReturnServices().insertReturn(stockReturn);
        	shipTrailer.setTransferReturnId(returnId);
        	shipTrailer.setTransfer(false);
        	shipTrailer.setCreatedUser(getUser().getId().toString());
        	DMShipTrailerCaptureClient.getInstance().savaShipTrailer(shipTrailer);
        	stockReturn = ClientServiceFactory.getReturnServices().readReturn(returnId);
        }
        ClientServiceFactory.getReturnServices().updateAndDispatchReturn(stockReturn, sessionPrinters);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_RETURN);
        RepositoryManager.addStateObject(SimClientStateKey.RETURN_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public void submitStockReturn() throws Exception {
        List<SessionPrinter> sessionPrinters = getSessionPrinters();
        ClientServiceFactory.getReturnServices().submitStockReturn(stockReturn, sessionPrinters);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_RETURN);
        RepositoryManager.addStateObject(SimClientStateKey.RETURN_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public void cancelSubmittedStockReturn() throws Exception {
        ClientServiceFactory.getReturnServices().cancelSubmittedStockReturn(stockReturn);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_RETURN);
        RepositoryManager.addStateObject(SimClientStateKey.RETURN_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public Store findStore(Long storeId) throws Exception {
        return ClientDataCacheUtility.getStore(storeId);
    }

    public List<ReturnLineItemWrapper> getLineItemWrappersMissingRequiredUins(List<ReturnLineItemWrapper> wrappers) {
        List<ReturnLineItemWrapper> wrappersMissingUins = new ArrayList<ReturnLineItemWrapper>();
        for (ReturnLineItemWrapper wrapper : wrappers) {
            if (wrapper.isMissingRequiredUins()) {
                wrappersMissingUins.add(wrapper);
            }
        }
        return wrappersMissingUins;
    }

    public boolean isValidDestinationStatus() throws Exception {
        Source source = stockReturn.getDestination();
        if (source instanceof Supplier) {
            Supplier supplier = (Supplier) source;
            if (supplier.isInactive()) {
                throw new BusinessException(CommonMessageText.SUPPLIER_SITE_INACTIVE);
            }
        }
        return true;
    }

    public boolean isValidForBillOfLading() throws BusinessException {
        if (stockReturn.getDestination() == null) {
            throw new BusinessException(ReturnMessageText.MISSING_DESTINATION);
        }
        return true;
    }

    private boolean isActivityLockNeeded() {
        Boolean isReturnSubmitted = stockReturn.getStatus() == ReturnStatus.SUBMITTED;
        if (stockReturn == null || stockReturn.isNew() || isReturnUnmodifiable() && !isReturnSubmitted) {
            return false;
        }
        return ReturnStatus.getActivityLockNeededSet().contains(stockReturn.getStatus());
    }

    public void clearStockReturn() throws Exception {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_RETURN);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_FINISHER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_WAREHOUSE);
        RepositoryManager.removeStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
        releaseReturnLock();
        stockReturn = null;
    }

    public boolean confirmReturnLock() throws Exception {
        if (stockReturn != null && stockReturn.getId() != null) {
            return confirmLock(ActivityLockType.RETURN, stockReturn.getIdAsString());
        }
        return true;
    }

    public boolean obtainReturnLock() throws Exception {
        if (stockReturn == null || !isActivityLockNeeded()) {
            return true;
        }
        return obtainLock(ActivityLockType.RETURN, stockReturn.getIdAsString());
    }

    public void releaseReturnLock() throws Exception {
        if (stockReturn != null) {
            releaseLock(ActivityLockType.RETURN, stockReturn.getIdAsString());
        }
    }

    private List<SessionPrinter> getSessionPrinters() {
        return (List<SessionPrinter>) SimRepository.getSessionPrinters();
    }

	public void storeReturnForShipTrailer() {
		if (stockReturn != null && stockReturn.getId() != null) {
			RepositoryManager.addStateObject(SimClientStateKey.SHIP_TRAILER_TRANSFER_RETURN_ID, stockReturn.getId());
		}
		RepositoryManager.addStateObject(SimClientStateKey.SHIP_TRAILER_IS_TRANSFER, false);
	}

	public boolean validateShipTrailer() {
		return !isShipTrailerEnabled() || stockReturn.getId() != null || !stockReturn.getType().equals(SourceType.WAREHOUSE) || RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT) != null;
	}

	public boolean isShipTrailerEnabled() {
		return DMShipTrailerCaptureClient.getInstance().isShipTrailerEnabled(getStoreId());
	}
}
