package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
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
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.InventoryAdjustmentReportRequest;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Inventory Adjustment Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentDetailModel extends SimScreenModel {

    private InventoryAdjustment inventoryAdjustment;
    private boolean isPermissionsValid = true;
    private boolean isAdjustmentEditable = true;
    private List<InventoryAdjustmentTemplateVO> templateVOs;

    public void loadInventoryAdjustment() throws Exception {
        inventoryAdjustment = (InventoryAdjustment) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT);
        if (inventoryAdjustment == null) {
            inventoryAdjustment = BOFactory.createInventoryAdjustment();
            inventoryAdjustment.doSetStoreId(getStoreId());
            inventoryAdjustment.doSetCreateDate(SimDateUtil.getCurrentDate());
            inventoryAdjustment.doSetCreateUser(getUserName());
            return;
        }
        assignPermissionsState();
        assignAdjustmentViewState();
    }

    private void assignPermissionsState() throws Exception {
        if (!hasPermission(PermissionKey.PC_EDIT_INVENTORY_ADJUSTMENT)) {
            isPermissionsValid = false;
            return;
        }
        for (InventoryAdjustmentLineItem lineItem : inventoryAdjustment.getLineItems()) {
            if (!hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, lineItem.getReason().getId())) {
                isPermissionsValid = false;
                break;
            }
        }
    }

    private void assignAdjustmentViewState() throws Exception {
        if (inventoryAdjustment.getStatus() != InventoryAdjustmentStatus.IN_PROGRESS) {
            isAdjustmentEditable = false;
            return;
        }
        if (inventoryAdjustment.isNew()) {
            return;
        }
        isAdjustmentEditable = obtainLock(ActivityLockType.INVENTORY_ADJUSTMENT, inventoryAdjustment.getIdAsString());
    }

    public InventoryAdjustment getInventoryAdjustment() {
        return inventoryAdjustment;
    }

    public boolean isPrintFunctionAvailable() {
        return !inventoryAdjustment.isNew();
    }

    public boolean isAdjustmentEditable() {
        return isPermissionsValid && isAdjustmentEditable;
    }

    public boolean isTemplateEditable() {
        if (isPermissionsValid && isAdjustmentEditable) {
            return inventoryAdjustment.isNew() && inventoryAdjustment.getLineItems().isEmpty() && hasPermission(PermissionKey.PC_APPLY_TEMPLATES);
        }
        return false;
    }

    public boolean isConfirmFunctionAvailable() {
        return isPermissionsValid && hasPermission(PermissionKey.PC_COMPLETE_INVENTORY_ADJUSTMENTS);
    }

    public boolean isCopyFunctionAvailable() {
        for (InventoryAdjustmentLineItem lineItem : inventoryAdjustment.getLineItems()) {
            if (lineItem.getSerialNumbers().size() > 0) {
                return false;
            }
        }
        return isPermissionsValid && inventoryAdjustment.getStatus() == InventoryAdjustmentStatus.COMPLETED;
    }

    public boolean isScannerAvailable() {
        return isAdjustmentEditable();
    }

    public boolean isStockCountValidationNeeded() throws Exception {
        if (getStoreBoolean(StoreConfigKeys.DISPLAY_LATE_ADJUSTMENT_MESSAGE)) {
            Set<String> itemIds = new HashSet<>(inventoryAdjustment.getAssociatedStockItemIds());
            return ClientServiceFactory.getStockCountServices().hasOpenStockCountItems(inventoryAdjustment.getStoreId(), itemIds);
        }
        return false;
    }

    public List<InventoryAdjustmentReason> getInventoryAdjustmentReasons() throws Exception {
        List<InventoryAdjustmentReason> availableReasons = new ArrayList<>();
        for (InventoryAdjustmentReason reason : ClientDataCacheUtility.getDisplayableInventoryAdjustmentReasons()) {
            if (hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, reason.getId())) {
                availableReasons.add(reason);
            }
        }
        return availableReasons;
    }

    public List<InventoryAdjustmentTemplateVO> getAllInventoryTemplates() throws Exception {
        if (templateVOs == null) {
            InventoryAdjustmentTemplateQueryFilter filter = BOFactory.createInventoryAdjustmentTemplateQueryFilter();
            filter.doSetStatus(InventoryAdjustmentTemplateStatus.COMPLETED);
            filter.doSetStoreId(getStoreId());

            templateVOs = ClientServiceFactory.getInventoryAdjustmentServices().findTemplateVOs(filter);
        }
        return templateVOs;
    }

    public List<InventoryAdjustmentTemplateVO> getSingleInventoryTemplate() throws Exception {
        if (templateVOs == null) {
            InventoryAdjustmentTemplateQueryFilter filter = BOFactory.createInventoryAdjustmentTemplateQueryFilter();
            filter.doSetTemplateId(inventoryAdjustment.getTemplateId());
            filter.doSetStoreId(getStoreId());

            templateVOs = ClientServiceFactory.getInventoryAdjustmentServices().findTemplateVOs(filter);
        }
        return templateVOs;
    }

    public InventoryAdjustmentTemplateVO findTemplate(Long templateId) throws Exception {
        for (InventoryAdjustmentTemplateVO templateVO : templateVOs) {
            if (templateVO.getId().equals(templateId)) {
                return templateVO;
            }
        }
        return null;
    }

    public void applyTemplate(InventoryAdjustmentTemplateVO templateVO, int multiplier) throws Exception {
        InventoryAdjustmentTemplate template = ClientServiceFactory.getInventoryAdjustmentServices().readTemplate(templateVO.getId());
        for (InventoryAdjustmentTemplateLineItem templateLineItem : template.getLineItems()) {
            if (!hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, templateLineItem.getReason().getId())) {
                throw new BusinessException(InventoryAdjustmentMessageText.APPLY_TEMPLATE_ERROR);
            }
        }
        for (InventoryAdjustmentTemplateLineItem templateLineItem : template.getLineItems()) {
            InventoryAdjustmentLineItem lineItem = inventoryAdjustment.createLineItem(templateLineItem.getStockItem());
            lineItem.setReason(templateLineItem.getReason());
            lineItem.setCaseSize(templateLineItem.getCaseSize());
            lineItem.setQuantity(templateLineItem.getQuantityOrZero().multiply(multiplier));
        }
        inventoryAdjustment.doSetTemplateId(template.getId());
        inventoryAdjustment.doSetTemplateMultiplier(multiplier);
    }

    public List<InventoryAdjustmentLineItemWrapper> getLineItemWrappers() {
        List<InventoryAdjustmentLineItemWrapper> wrappers = new ArrayList<>();
        for (InventoryAdjustmentLineItem lineItem : inventoryAdjustment.getLineItems()) {
            wrappers.add(ClientWrapperFactory.createInventoryAdjustmentLineItemWrapper(inventoryAdjustment, lineItem));
        }
        return wrappers;
    }

    public InventoryAdjustmentLineItemWrapper createNewLineItemWrapper(InventoryAdjustmentReason reason) {
        return ClientWrapperFactory.createInventoryAdjustmentLineItemWrapper(inventoryAdjustment, reason);
    }

    /*
     * Since the wrapper throws a SimTableResetFocusException to reset the table, we must deal with the exception here as well.
     */
    public void updateExistingLineItem(InventoryAdjustmentLineItemWrapper wrapper, BarcodeItem barcodeItem, InventoryAdjustmentReason reason) throws Exception {
        if (wrapper.getStockItem() == null) {
            wrapper.setStockItem(barcodeItem.getStockItem());
        }
        if (wrapper.getStockItem() == null) {
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        }
        if (wrapper.getReason() == null) {
            wrapper.setReason(reason);
        }
        if (barcodeItem.isSerialNumberRequired()) {
            updateExistingLineItemUin(wrapper, barcodeItem);
        } else {
            updateExistingLineItemQty(wrapper, barcodeItem);
        }
    }

    private void updateExistingLineItemQty(InventoryAdjustmentLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.getQuantity().isZero()) {
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        }
        try {
            Quantity quantity = wrapper.getQuantity();
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                quantity = quantity.add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize()));
            } else {
                quantity = quantity.add(barcodeItem.getQuantity());
            }
            if (wrapper.isQtyGreaterThanNonSellable(quantity)) {
                throw new UIException(InventoryAdjustmentMessageText.UNAVAILABLE_QTY_ERROR, RErrorSeverity.WARNING);
            }
            wrapper.setQuantity(quantity);
        } catch (SimTableResetFocusException exception) {
            UILog.debug(getClass(), exception);
        }
    }

    private void updateExistingLineItemUin(InventoryAdjustmentLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        StockItem stockItem = barcodeItem.getStockItem();
        String itemId = barcodeItem.getId();
        String uin = barcodeItem.getUin();
        UINType uinType = stockItem.getUINType();
        String uinLabel = stockItem.getUINLabel();
        InventoryAdjustmentReason reason = wrapper.getReason();
        if (uinType == UINType.AGSN && reason.getDisposition() == InventoryDisposition.OUT_TO_AVAILABLE) {
            updateExistingLineItemQty(wrapper, barcodeItem);
            return;
        }
        if (StringHelper.isNullOrEmpty(uin)) {
            throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel);
        }
        SerialNumberValue serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValue(itemId, uin);
        if (serialNumber == null && reason.getDisposition() == InventoryDisposition.OUT_TO_AVAILABLE) {
            serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValueOrCreate(getStoreId(), itemId, uin, uinType, FunctionalArea.INVENTORY_ADJUSTMENT);
        }
        if (serialNumber == null) {
            Object[] values = new Object[] { uinLabel, uin, itemId };
            throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
        }

        InventoryAdjustmentIsValidSerialNumberRule.execute(inventoryAdjustment, stockItem, reason, serialNumber);

        if (serialNumber.getStoreId().equals(getStoreId()) || serialNumber.getStatus() != UINStatus.IN_STOCK) {
            wrapper.addSerialNumber(serialNumber);
            return;
        }
        if (!SimConfigManager.getBoolean(SimConfigManager.ALLOW_UNEXPECTED_UINS)) {
            Object[] values = new Object[] { uinLabel, uin, serialNumber.getStatus().toString() };
            throw new BusinessException(UINMessageText.UIN_AT_ANOTHER_STORE, values);
        }
        if (RConfirmUtility.confirm("Confirmation", UINMessageText.MOVE_STORE_CONFIRM, uinLabel)) {
            wrapper.addSerialNumber(serialNumber);
            return;
        }
        throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel);
    }

    public void removeLineItem(InventoryAdjustmentLineItemWrapper wrapper) throws BusinessException {
        if (wrapper.getLineItem() != null) {
            inventoryAdjustment.removeLineItem(wrapper.getLineItem());
        }
    }

    public void saveInventoryAdjustment() throws Exception {
        if ((inventoryAdjustment.isNew() || inventoryAdjustment.isDirty()) && inventoryAdjustment.isCoherent()) {
            ClientServiceFactory.getInventoryAdjustmentServices().updateInventoryAdjustment(inventoryAdjustment);
            RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_MODIFIED, Boolean.TRUE);
        }
        releaseInventoryAdjustmentLock();
    }

    public void confirmInventoryAdjustment(boolean applyToStockCounts) throws Exception {
        ClientServiceFactory.getInventoryAdjustmentServices().confirmInventoryAdjustment(inventoryAdjustment, applyToStockCounts);
        RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_MODIFIED, Boolean.TRUE);
        releaseInventoryAdjustmentLock();
    }

    public void cancelInventoryAdjustment() throws Exception {
        ClientServiceFactory.getInventoryAdjustmentServices().cancelInventoryAdjustment(inventoryAdjustment.getId());
        RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_MODIFIED, Boolean.TRUE);
        releaseInventoryAdjustmentLock();
    }

    public boolean confirmInventoryAdjustmentLock() throws Exception {
        if (inventoryAdjustment.isNew()) {
            return true;
        }
        if (isAdjustmentEditable) {
            return confirmLock(ActivityLockType.INVENTORY_ADJUSTMENT, inventoryAdjustment.getIdAsString());
        }
        return true;
    }

    public void releaseInventoryAdjustmentLock() {
        if (inventoryAdjustment.isNew()) {
            return;
        }
        try {
            releaseLock(ActivityLockType.INVENTORY_ADJUSTMENT, inventoryAdjustment.getIdAsString());
        } catch (Throwable e) {
            UILog.debug(getClass(), e);
        }
    }

    // Can only occur on completed inventory adjustments
    public void copyInventoryAdjustment() throws Exception {
        InventoryAdjustment newInventoryAdjustment = BOFactory.createInventoryAdjustment();
        newInventoryAdjustment.doSetStoreId(getStoreId());
        newInventoryAdjustment.doSetCreateDate(SimDateUtil.getCurrentDate());
        newInventoryAdjustment.doSetCreateUser(getUserName());
        newInventoryAdjustment.doSetReferenceId(inventoryAdjustment.getId());
        newInventoryAdjustment.doSetComments(inventoryAdjustment.getComments());

        for (InventoryAdjustmentLineItem lineItem : inventoryAdjustment.getLineItems()) {
            InventoryAdjustmentLineItem newLineItem = newInventoryAdjustment.createLineItem(lineItem.getStockItem());
            newLineItem.setReason(lineItem.getReason());
            newLineItem.setCaseSize(lineItem.getCaseSize());
            newLineItem.setQuantity(lineItem.getQuantityOrZero());
        }

        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT, newInventoryAdjustment);
    }

    public void printAdjustmentReport() throws Exception {
        Long storeId = getStoreId();
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, ReportFormat.INVENTORY_ADJUSTMENT);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            InventoryAdjustmentReportRequest reportRequest = BOFactory.createInventoryAdjustmentReportRequest(inventoryAdjustment.getId());
            SimClientPrintUtility.printReportRequest(reportRequest, formatPrinters, InventoryAdjustmentMessageText.REPORT_PRINTED);
        }
    }

    public void clearState() {
        inventoryAdjustment = null;
        isPermissionsValid = true;
        isAdjustmentEditable = true;
    }
}
