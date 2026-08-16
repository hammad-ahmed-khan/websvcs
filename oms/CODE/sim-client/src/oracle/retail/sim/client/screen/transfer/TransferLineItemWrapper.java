package oracle.retail.sim.client.screen.transfer;

import java.math.BigDecimal;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferLineItemPropertyModifiableRule;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferSerialNumber;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer Line Item Wrapper for display in the various editing tables of the transfer screens on the PC.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferLineItemWrapper extends StockLineItemWrapper {

    private Transfer transfer;
    private TransferLineItem lineItem;
    private BigDecimal preferredUomConversionFactor;
    private boolean isViewOnly;

    public TransferLineItemWrapper(Transfer transfer) {
        this.transfer = transfer;
    }

    public TransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem) {
        this.transfer = transfer;
        this.lineItem = lineItem;
    }

    public TransferLineItemWrapper(Transfer transfer, TransferLineItem lineItem, boolean isViewOnly) {
        this.transfer = transfer;
        this.lineItem = lineItem;
        this.isViewOnly = isViewOnly;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public TransferLineItem getLineItem() {
        return lineItem;
    }

    public StockItem getStockItem() {
        if (lineItem != null) {
            return lineItem.getStockItem();
        }
        return null;
    }

    /**
     * The stock item carried on a transfer line item will be the sending store's stock item
     * until the transfer is dispatched, and then it becomes the receiving store's item.
     */
    public void setStockItem(StockItem stockItem) throws Exception {
        TransferStatus status = getStatus();
        if (status.getCode() < TransferStatus.PENDING.getCode()) {
            setRequestStockItem(stockItem);
        } else if (status.getCode() < TransferStatus.DISPATCHED.getCode()) {
            setTransferStockItem(stockItem);
        } else {
            setReceivingStockItem(stockItem);
        }
    }

    private void setRequestStockItem(StockItem receivingStockItem) throws Exception {
        if (receivingStockItem == null) {
            return;
        }
        if (!receivingStockItem.getStoreId().equals(transfer.getReceivingStore().getId())) {
            throw new IllegalArgumentException("Stock Item must be for receiving store in request mode!");
        }
        if (receivingStockItem.getStatus() == ItemStatus.DELETED) {
            throw new BusinessException(TransferMessageText.ITEM_DELETED_AT_DESTINATION);
        }
        if (receivingStockItem.getStatus() == ItemStatus.INACTIVE) {
            throw new BusinessException(TransferMessageText.ITEM_INACTIVE_AT_DESTINATION);
        }
        if (receivingStockItem.getStatus() == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Confirmation", TransferMessageText.RECEIVE_ITEM_DISCONTINUED)) {
                return;
            }
        }

        StockItem sendingStockItem = findItemAtStore(receivingStockItem.getId(), transfer.getSendingStore().getId(), TransferMessageText.REQUEST_ITEM_NOT_RANGED);
        if (sendingStockItem == null) {
            return;
        }

        if (lineItem == null) {
            lineItem = transfer.createLineItem(sendingStockItem);
            return;
        }
        if (!sendingStockItem.getId().equals(lineItem.getStockItem().getId())) {
            TransferLineItem originalLineItem = lineItem;
            originalLineItem.setPublishNotNeeded();

            transfer.removeLineItem(originalLineItem);

            lineItem = transfer.createLineItem(sendingStockItem);
            lineItem.doSetRequestedQuantity(originalLineItem.getRequestedQuantity());
        }
    }

    private void setTransferStockItem(StockItem sendingStockItem) throws Exception {
        if (sendingStockItem == null) {
            return;
        }
        if (!sendingStockItem.getStoreId().equals(transfer.getSendingStore().getId())) {
            throw new IllegalArgumentException("Stock Item must be for sending store in transfer mode!");
        }

        StockItem receivingStockItem = findItemAtStore(sendingStockItem.getId(), transfer.getReceivingStore().getId(), TransferMessageText.RECEIVE_ITEM_NOT_RANGED);
        if (receivingStockItem == null) {
            return;
        }
        if (receivingStockItem.getStatus() == ItemStatus.DELETED) {
            throw new BusinessException(TransferMessageText.ITEM_DELETED_AT_DESTINATION);
        }
        if (receivingStockItem.getStatus() == ItemStatus.INACTIVE) {
            throw new BusinessException(TransferMessageText.ITEM_INACTIVE_AT_DESTINATION);
        }
        if (receivingStockItem.getStatus() == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Confirmation", TransferMessageText.RECEIVE_ITEM_DISCONTINUED)) {
                return;
            }
        }
        if (lineItem == null) {
            lineItem = transfer.createLineItem(sendingStockItem);
            return;
        }
        if (!sendingStockItem.equals(lineItem.getStockItem())) {
            TransferLineItem originalLineItem = lineItem;
            originalLineItem.setPublishNotNeeded();

            transfer.removeLineItem(originalLineItem);

            lineItem = transfer.createLineItem(sendingStockItem);
            lineItem.doSetRequestedQuantity(originalLineItem.getRequestedQuantity());
            lineItem.doSetApprovedQuantity(originalLineItem.getApprovedQuantity());
            lineItem.doSetTransferQuantity(originalLineItem.getTransferQuantity());
            lineItem.doSetOriginalApprovedQuantity(originalLineItem.getOriginalApprovedQuantity());
            lineItem.doSetOriginalTransferQuantity(originalLineItem.getOriginalTransferQuantity());
        }
    }

    private void setReceivingStockItem(StockItem receivingStockItem) throws Exception {
        if (receivingStockItem == null) {
            return;
        }
        if (!receivingStockItem.getStoreId().equals(transfer.getReceivingStore().getId())) {
            throw new IllegalArgumentException("Stock Item must be for receiving store in receiving mode!");
        }
        ItemStatus status = receivingStockItem.getStatus();
        if (status == ItemStatus.INACTIVE || status == ItemStatus.DELETED || status == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Confirmation", CommonMessageText.NON_ACTIVE_ITEM_CONFIRM, status.toString())) {
                return;
            }
        }
        if (lineItem == null) {
            lineItem = transfer.createLineItem(receivingStockItem);
            StockItem stockItem = ClientServiceFactory.getItemServices().readStockItem(receivingStockItem.getId(), transfer.getSendingStore().getId());
            if (stockItem == null) {
                stockItem = ClientServiceFactory.getItemServices().readStockItemOrCreate(receivingStockItem.getId(), transfer.getSendingStore().getId());
            }
            return;
        }
        if (receivingStockItem.getStatus() == ItemStatus.DELETED) {
            throw new BusinessException(TransferMessageText.ITEM_DELETED_AT_DESTINATION);
        }
        if (receivingStockItem.getStatus() == ItemStatus.INACTIVE) {
            throw new BusinessException(TransferMessageText.ITEM_INACTIVE_AT_DESTINATION);
        }
        if (receivingStockItem.getStatus() == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Confirmation", TransferMessageText.RECEIVE_ITEM_DISCONTINUED)) {
                return;
            }
        }
        if (!receivingStockItem.equals(lineItem.getStockItem())) {
            TransferLineItem originalLineItem = lineItem;
            originalLineItem.setPublishNotNeeded();

            transfer.removeLineItem(originalLineItem);

            lineItem = transfer.createLineItem(receivingStockItem);
            lineItem.doSetRequestedQuantity(originalLineItem.getRequestedQuantity());
            lineItem.doSetApprovedQuantity(originalLineItem.getApprovedQuantity());
            lineItem.doSetTransferQuantity(originalLineItem.getTransferQuantity());
            lineItem.doSetOriginalApprovedQuantity(originalLineItem.getOriginalApprovedQuantity());
            lineItem.doSetOriginalTransferQuantity(originalLineItem.getOriginalTransferQuantity());
        }
    }

    private StockItem findItemAtStore(String itemId, Long storeId, MessageText confirmMessage) throws Exception {
        StockItem stockItem = ClientServiceFactory.getItemServices().readStockItem(itemId, storeId);
        if (stockItem == null) {
            if (!SimConfigManager.getBoolean(SimConfigManager.ALLOW_NON_RANGE_ITEM)) {
                throw new BusinessException(ItemMessageText.ITEM_NOT_RANGED_ERROR);
            }
            if (RConfirmUtility.confirm("Non-Ranged Item Confirmation", confirmMessage)) {
                stockItem = ClientServiceFactory.getItemServices().readStockItemOrCreate(itemId, storeId);
            }
        }
        return stockItem;
    }

    public String getDescription() {
        StockItem stockItem = getStockItem();
        if (stockItem != null) {
            return SimConfigManager.isItemShortDescription() ? stockItem.getShortDescription() : stockItem.getLongDescription();
        }
        return null;
    }

    public TransferStatus getStatus() {
        return transfer != null ? transfer.getStatus() : null;
    }

    public UOMMode getUnitOfMeasureMode() {
        return lineItem != null ? super.getUnitOfMeasureMode() : getDefaultUomMode();
    }

    public String getPreferredUnitOfMeasure() {
        return lineItem != null ? lineItem.getPreferredUom() : null;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        if (preferredUomConversionFactor == null && lineItem.getStockItem() != null) {
            try {
                preferredUomConversionFactor = UomUtility.getStandardUomToTargetUom(lineItem.getStockItem(), lineItem.getPreferredUom());
            } catch (Exception e) {
                return null;
            }
        }
        return preferredUomConversionFactor;
    }

    public Quantity getCaseSize() {
        if (lineItem != null) {
            return isCasesMode() ? lineItem.getCaseSize() : Quantity.ONE;
        }
        return null;
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (isCasesMode()) {
            lineItem.setCaseSize(caseSize);
        }
    }

    public Quantity getStockOnHandBasedOnUom() {
        StockItem stockItem = getStockItem();
        return stockItem != null ? rationalizeQuantityBasedOnUom(stockItem.getAvailableStockOnHand()) : Quantity.ZERO;
    }

    public Quantity getRequestedQuantity() {
        return lineItem != null ? lineItem.getRequestedQuantity() : null;
    }

    public Quantity getRequestedQuantityOrZero() {
        return lineItem != null ? lineItem.getRequestedQuantityOrZero() : Quantity.ZERO;
    }

    public Quantity getRequestedQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getRequestedQuantity()) : null;
    }

    public void setRequestedQuantity(Quantity quantity) throws Exception {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setRequestedQuantity", quantity);
        boolean assignRequestedQuantity = true;
        if (lineItem.getStockItem().isQtyGreaterThanAvailableStockOnHand(quantity)) {
            assignRequestedQuantity = RConfirmUtility.confirm("Quantity Confirmation", TransferMessageText.CONFIRM_REQUEST_QUANTITY);
        }
        if (assignRequestedQuantity) {
            lineItem.setRequestedQuantity(quantity);
        }
    }

    /**
     * If greater than stock on hand, warn the user first. Note that this is a sending store check.
     */
    public void setRequestedQuantityBasedOnUom(Quantity quantity) throws Exception {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setRequestedQuantityBasedOnUom", quantity);

        Quantity requestedQuantity = quantity;
        if (isCasesMode()) {
            requestedQuantity = quantity.multiply(lineItem.getCaseSize());
        }
        if (isPreferredMode() && isPreferredUomConversionAvailable()) {
            requestedQuantity = requestedQuantity.divide(getPreferredUomConversionFactor());
        }

        boolean assignRequestedQuantity = true;
        if (lineItem.getStockItem().isQtyGreaterThanAvailableStockOnHand(requestedQuantity)) {
            assignRequestedQuantity = RConfirmUtility.confirm("Quantity Confirmation", TransferMessageText.CONFIRM_REQUEST_QUANTITY);
        }
        if (assignRequestedQuantity) {
            lineItem.setRequestedQuantity(requestedQuantity);
            return;
        }
        throw new SimTableResetFocusException();
    }

    public Quantity getApprovedQuantity() {
        return lineItem != null ? lineItem.getApprovedQuantity() : null;
    }

    public Quantity getApprovedQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getApprovedQuantity()) : null;
    }

    public void setApprovedQuantityBasedOnUom(Quantity quantity) throws Exception {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setApprovedQuantityBasedOnUOM", quantity);
        Quantity approvedQuantity = quantity;
        if (isCasesMode()) {
            approvedQuantity = quantity.multiply(lineItem.getCaseSize());
        }
        if (isPreferredMode() && isPreferredUomConversionAvailable()) {
            approvedQuantity = quantity.divide(getPreferredUomConversionFactor());
        }
        lineItem.setApprovedQuantity(approvedQuantity);
    }

    public Quantity getTransferQuantity() {
        return lineItem != null ? lineItem.getTransferQuantity() : null;
    }

    public Quantity getTransferQuantityOrZero() {
        return lineItem != null ? lineItem.getTransferQuantityOrZero() : Quantity.ZERO;
    }

    public Quantity getTransferQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getTransferQuantity()) : null;
    }

    public void setTransferQuantity(Quantity quantity) throws Exception {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setTransferQuantity", quantity);
        doSetTransferQuantity(quantity);
    }

    /**
     * If greater than stock on hand, warn the user first. Note that this is a sending store check.
     */
    public void setTransferQuantityBasedOnUom(Quantity quantity) throws Exception {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setTransferQuantityBasedOnUom", quantity);
        if (isCasesMode()) {
            quantity = quantity.multiply(lineItem.getCaseSize());
        }
        if (isPreferredMode() && isPreferredUomConversionAvailable()) {
            quantity = quantity.divide(getPreferredUomConversionFactor());
        }
        doSetTransferQuantity(quantity);
    }

    private void doSetTransferQuantity(Quantity quantity) throws Exception {
        Quantity validateQuantity = new Quantity(quantity.doubleValue());
        if (lineItem.getOriginalTransferQuantity() != null) {
            validateQuantity = validateQuantity.subtract(lineItem.getOriginalTransferQuantity());
        }
        if (lineItem.getStockItem().isQtyGreaterThanAvailableStockOnHand(validateQuantity.subtract(lineItem.getApprovedQuantityOrZero()))) {
            if (!RConfirmUtility.confirm("Quantity Confirmation", TransferMessageText.CONFIRM_QUANTITY)) {
                throw new SimTableResetFocusException();
            }
        }
        lineItem.setTransferQuantity(quantity);
    }

    public Quantity getReceivedQuantity() {
        return lineItem != null ? lineItem.getReceivedQuantity() : null;
    }

    public Quantity getReceivedQuantityOrZero() {
        return lineItem != null ? lineItem.getReceivedQuantityOrZero() : Quantity.ZERO;
    }

    public Quantity getReceivedQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getReceivedQuantity()) : null;
    }

    public void setReceivedQuantity(Quantity quantity) throws Exception {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setReceivedQuantity", quantity);
        doSetReceivedQuantity(quantity);
    }

    public void setReceivedQuantityBasedOnUom(Quantity quantity) throws Exception {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setReceivedQuantityBasedOnUom", quantity);
        if (isCasesMode()) {
            quantity = quantity.multiply(lineItem.getCaseSize());
        }
        if (isPreferredMode() && isPreferredUomConversionAvailable()) {
            quantity = quantity.divide(getPreferredUomConversionFactor());
        }
        doSetReceivedQuantity(quantity);
    }

    private void doSetReceivedQuantity(Quantity quantity) throws BusinessException {
        if (transfer.isAdjustReceivedTransferMode()) {
            StockItem stockItem = lineItem.getStockItem();
            if (stockItem.isAgsnEnabled() && lineItem.getReceivedQuantity().getBigDecimal().compareTo(quantity.getBigDecimal()) > 0) {
                String[] values = new String[2];
                values[0] = stockItem.getUINType().toString();
                values[1] = stockItem.getId() + " - " + stockItem.getShortDescription();
                RConfirmUtility.showMessage(Application.getFrame(), "Transfer", CommonMessageText.UIN_RECEIVE_QUANTITY_MISMATCH, values);
                return;
            }
        }
        lineItem.setReceivedQuantity(quantity);
    }

    public Quantity getDamagedQuantity() {
        return lineItem != null ? lineItem.getDamagedQuantity() : null;
    }

    public Quantity getDamagedQuantityOrZero() {
        return lineItem != null ? lineItem.getDamagedQuantityOrZero() : Quantity.ZERO;
    }

    public Quantity getDamagedQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getDamagedQuantity()) : null;
    }

    public void setDamagedQuantity(Quantity quantity) throws BusinessException {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setDamagedQuantity", quantity);
        doSetDamagedQuantity(quantity);
    }

    public void setDamagedQuantityBasedOnUom(Quantity quantity) throws BusinessException {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        lineItem.executeRule("setDamagedQuantityBasedOnUom", quantity);
        if (isCasesMode()) {
            quantity = quantity.multiply(lineItem.getCaseSize());
        }
        if (isPreferredMode() && isPreferredUomConversionAvailable()) {
            quantity = quantity.divide(getPreferredUomConversionFactor());
        }
        doSetDamagedQuantity(quantity);
    }

    private void doSetDamagedQuantity(Quantity quantity) throws BusinessException {
        if (isAutoGenerateSerialNumberEnabled() && transfer.isAdjustReceivedTransferMode()
                && lineItem.getReceivedQuantity().getBigDecimal().compareTo(quantity.multiply(getCaseSize()).getBigDecimal()) > 0) {
            StockItem stockItem = lineItem.getStockItem();
            String[] values = new String[2];
            values[0] = stockItem.getUINType().toString();
            values[1] = stockItem.getId() + " - " + stockItem.getShortDescription();
            RConfirmUtility.showMessage(Application.getFrame(), "Transfer", CommonMessageText.UIN_RECEIVE_QUANTITY_MISMATCH, values);
            return;
        }
        lineItem.setDamagedQuantity(quantity);
    }

    public Integer getSerialNumberCount() {
        if (lineItem != null) {
            if (transfer.getStatus().getCode() >= TransferStatus.RECEIVING.getCode()) {
                return lineItem.getReceivedSerialNumbers().size() + lineItem.getDamagedSerialNumbers().size();
            }
            return lineItem.getSerialNumbers().size();
        }
        return 0;
    }

    public void addSerialNumber(SerialNumberValue serialNumberVO) throws BusinessException {
        lineItem.addSerialNumber(convertToSerialNumber(serialNumberVO));
    }

    /**
     * Any transaction line item that includes serial number functionality should override the empty implementation
     * of this method to perform the required functionality for the particular transaction.
     */
    public void addSerialNumber(TransferSerialNumber serialNumber) throws BusinessException {
        lineItem.addSerialNumber(serialNumber);
    }

    /**
     * Any transaction line item that includes serial number functionality should override the empty implementation
     * of this method to perform the required functionality for the particular transaction.
     */
    public void removeSerialNumber(TransferSerialNumber serialNumber) throws BusinessException {
        lineItem.removeSerialNumber(serialNumber.getUin());
    }

    /**
     * Any transaction line item that includes serial number functionality should override the empty implementation
     * of this method to perform the required functionality for the particular transaction.
     */
    public List<TransferSerialNumber> getSerialNumbers() {
        return lineItem.getSerialNumbers();
    }

    public List<TransferSerialNumber> getShippedSerialNumbers() {
        return lineItem.getShippedSerialNumbers();
    }

    public List<TransferSerialNumber> getReceivedSerialNumbers() {
        return lineItem.getReceivedSerialNumbers();
    }

    public List<TransferSerialNumber> getDamagedSerialNumbers() {
        return lineItem.getDamagedSerialNumbers();
    }

    public List<TransferSerialNumber> getRemovedSerialNumbers() {
        return lineItem.getRemovedSerialNumbers();
    }

    /**
     * Resets the shipped quantity based on the number of serial numbers
     */
    public void setShippedQtyBasedOnSerialNumbers() throws BusinessException {
        lineItem.setTransferQuantity(new Quantity(lineItem.getSerialNumbers().size()));
    }

    /**
     * Resets the received quantity(s) based on the number of serial numbers
     */
    public void setReceivedQtyBasedOnSerialNumbers() throws BusinessException {
        int received = 0;
        int damaged = 0;
        for (TransferSerialNumber serialNumber : lineItem.getSerialNumbers()) {
            if (serialNumber.isReceived()) {
                received++;
            }
            if (serialNumber.isDamaged()) {
                damaged++;
            }
        }
        lineItem.setReceivedQuantity(new Quantity(received));
        lineItem.setDamagedQuantity(new Quantity(damaged));
    }

    public void updateShippedSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumberVO = wrapper.getSerialNumberValue();
            if (serialNumberVO != null) {
                TransferSerialNumber serialNumber = findSerialNumber(serialNumberVO.getUin());
                if (serialNumber == null && wrapper.isDeleted()) {
                	continue;
                } else if (serialNumber == null) {
                    serialNumber = convertToSerialNumber(serialNumberVO);
                    serialNumber.doSetReceived(false);
                    serialNumber.doSetDamaged(false);
                    serialNumber.doSetShipped(false);

                    lineItem.addSerialNumber(serialNumber);
                } else if (wrapper.isDeleted()) {
                    lineItem.removeSerialNumber(serialNumber.getUin());
                } else {
                	lineItem.addSerialNumber(serialNumber);
                }
            }
        }
    }

    public void updateReceivedSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumberVO = wrapper.getSerialNumberValue();
            if (serialNumberVO != null) {
                TransferSerialNumber serialNumber = findSerialNumber(serialNumberVO.getUin());
                if (serialNumber == null) {
                    serialNumber = convertToSerialNumber(serialNumberVO);
                    serialNumber.doSetShipped(false);
                    serialNumber.doSetDamaged(wrapper.isDamaged());
                    serialNumber.doSetReceived(!wrapper.isDamaged());

                    lineItem.addSerialNumber(serialNumber);
                } else if (wrapper.isDeleted() && serialNumber.isShipped()) {
                    serialNumber.doSetReceived(false);
                    serialNumber.doSetDamaged(false);
                } else if (wrapper.isDeleted()) {
                    lineItem.removeSerialNumber(serialNumber.getUin());
                } else {
                    serialNumber.setDamaged(wrapper.isDamaged());
                    serialNumber.setReceived(!wrapper.isDamaged());
                }
            }
        }
    }

    public void removeSerialNumber(SerialNumberWrapper wrapper) throws BusinessException {
        SerialNumberValue value = wrapper.getSerialNumberValue();
        if (value != null && value.getUin() != null) {
            lineItem.removeSerialNumber(value.getUin());
        }
    }

    private TransferSerialNumber findSerialNumber(String uin) {
        for (TransferSerialNumber serialNumber : lineItem.getSerialNumbers()) {
            if (serialNumber.getUin().equals(uin)) {
                return serialNumber;
            }
        }
        for (TransferSerialNumber serialNumber : lineItem.getRemovedSerialNumbers()) {
            if (serialNumber.getUin().equals(uin)) {
                return serialNumber;
            }
        }
        return null;
    }

    private TransferSerialNumber convertToSerialNumber(SerialNumberValue serialNumberVO) {
        TransferSerialNumber serialNumber = BOFactory.createTransferSerialNumber();
        serialNumber.doSetUin(serialNumberVO.getUin());
        serialNumber.doSetStatus(serialNumberVO.getStatus());
        serialNumber.doSetNonSellableQtyTypeId(serialNumberVO.getNonSellableQtyTypeId());
        serialNumber.doSetStoreId(serialNumberVO.getStoreId());
        serialNumber.doSetUinId(serialNumberVO.getUinId());
        return serialNumber;
    }

    /**
     * Determines if the given property is modifiable. If this returns false, then this property is not modifiable.
     */
    public boolean isPropertyModifiable(String property) throws Exception {
        if (isViewOnly) {
            return false;
        }
        if (transfer.getStatus() == TransferStatus.DISPATCHED || transfer.getStatus() == TransferStatus.RECEIVING) {
            if (SimConfigManager.getBoolean(SimConfigManager.RECEIVE_ENTIRE_TRANSFER)) {
                return false;
            }
        }
        if (TransferProperty.SERIAL_NUMBER_COUNT.equals(property)) {
            if (isSerialNumberRequired()) {
                if (isAutoGenerateSerialNumberEnabled()) {
                    if (transfer.getStatus() == TransferStatus.IN_PROGRESS || lineItem.getSerialNumbers().size() > 0) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
            return false;
        }
        if (TransferProperty.getQuantityMethodsSet().contains(property)) {
            if (isSerialNumberRequired()) {
                if (!lineItem.getShippedSerialNumbers().isEmpty()) {
                    return false;
                }
                if (isAutoGenerateSerialNumberEnabled()) {
                    return transfer.getStatus() == TransferStatus.DISPATCHED || transfer.getStatus() == TransferStatus.RECEIVING;
                }
                return false;
            }
        }
        try {
            executeRule("isPropertyModifiable", property);
        } catch (BusinessException bre) {
            return false;
        }
        if (property.equals(TransferProperty.CASE_SIZE) && isStandardMode()) {
            return false;
        }
        return TransferLineItemPropertyModifiableRule.isPropertyModifiable(transfer, lineItem, property);
    }

    /**
     * Return true if UIN values are required (see StockItem.isUINRequired());
     */
    public boolean isSerialNumberRequired() {
        StockItem stockItem = getStockItem();
        return stockItem != null ? stockItem.isSerialNumberRequired() : false;
    }

    private boolean isAutoGenerateSerialNumberEnabled() {
        StockItem stockItem = getStockItem();
        return stockItem != null ? stockItem.isAgsnEnabled() : false;
    }

    public boolean equals(Object object) {
        if (object instanceof TransferLineItemWrapper) {
            TransferLineItemWrapper other = (TransferLineItemWrapper) object;
            if (transfer == null && other.transfer != null) {
                return false;
            }
            if (transfer != null && other.transfer == null) {
                return false;
            }
            if (transfer != null && other.transfer != null) {
                if (!transfer.equals(other.transfer)) {
                    return false;
                }
            }
            if (lineItem == null && other.lineItem == null) {
                return true;
            }
            if (lineItem == null || other.lineItem == null) {
                return false;
            }
            return lineItem.equals(other.lineItem);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }
}
