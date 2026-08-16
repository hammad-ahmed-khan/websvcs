package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentValidateUINCommand;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * The business logic model for the Inventory Adjustment Serial Number Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentUinDialogModel extends SimScreenModel {

    private InventoryAdjustmentLineItemWrapper lineItemWrapper;

    public void setLineItemWrapper(InventoryAdjustmentLineItemWrapper lineItemWrapper) {
        this.lineItemWrapper = lineItemWrapper;
    }

    public String getItemId() {
        return lineItemWrapper.getStockItem().getId();
    }

    public String getItemDescription() {
        if (SimConfigManager.isItemShortDescription()) {
            return lineItemWrapper.getStockItem().getShortDescription();
        }
        return lineItemWrapper.getStockItem().getLongDescription();
    }

    public String getNonSellableTypeDescription() throws Exception {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getNonSellableTypeDescription();
        }
        return StringConstants.EMPTY;
    }

    public String getInventoryDisplayText() {
        String quantityText = new QuantityDisplayer().getDisplayText(lineItemWrapper.getInventoryBasedOnUom());
        String uomText = new UomModeDisplayer().getDisplayText(lineItemWrapper.getUnitOfMeasureMode());
        return quantityText + StringConstants.SPACE + uomText;
    }

    public List<SerialNumberWrapper> getSerialNumberWrappers() {
        List<SerialNumberWrapper> wrappers = new ArrayList<>();
        for (SerialNumberValue value : lineItemWrapper.getSerialNumbers()) {
            SerialNumberWrapper wrapper = createSerialNumberWrapper();
            wrapper.setSerialNumberValue(value);
            wrapper.setDefaultAction();
            wrapper.setValidated();

            wrappers.add(wrapper);
        }
        if (wrappers.isEmpty()) {
            wrappers.add(createSerialNumberWrapper());
        }
        return wrappers;
    }

    public SerialNumberWrapper createSerialNumberWrapper() {
        return ClientWrapperFactory.createSerialNumberWrapper(FunctionalArea.INVENTORY_ADJUSTMENT, lineItemWrapper.getStockItem().getId(), getUINType(), getUINLabel());
    }

    private UINType getUINType() {
        return lineItemWrapper.getStockItem().getUINType();
    }

    public String getUINLabel() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getStockItem().getUINLabel();
        }
        return StringConstants.EMPTY;
    }

    public boolean isSerialNumbersEditable() {
        if (lineItemWrapper.getStatus() == InventoryAdjustmentStatus.IN_PROGRESS) {
            if (lineItemWrapper.getReason() != null) {
                if (lineItemWrapper.getReason().getDisposition() == InventoryDisposition.OUT_TO_AVAILABLE) {
                    return lineItemWrapper.getStockItem().getUINType() != UINType.AGSN;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isReasonEditable() {
        return isSerialNumbersEditable() && lineItemWrapper.getLineItem().getSerialNumbers().isEmpty();
    }

    public List<InventoryAdjustmentReason> findAdjustmentReasons() throws Exception {
        return ClientDataCacheUtility.getDisplayableInventoryAdjustmentReasons();
    }

    public InventoryAdjustmentReason getAdjustmentReason() {
        return lineItemWrapper.getReason();
    }

    public void setAdjustmentReason(InventoryAdjustmentReason reason) throws BusinessException {
        lineItemWrapper.setReason(reason);
    }

    public void validateSerialNumber(SerialNumberValue serialNumber) throws Exception {
        InventoryAdjustmentIsValidSerialNumberRule.execute(lineItemWrapper.getAdjustment(), lineItemWrapper.getStockItem(), lineItemWrapper.getReason(), serialNumber);
    }

    public boolean validatePermissions(SerialNumberValue serialNumber) {
        InventoryDisposition disposition = lineItemWrapper.getDisposition();
        if (disposition == InventoryDisposition.OUT_TO_AVAILABLE) {
            if (serialNumber.getStatus() == UINStatus.UNCONFIRMED) {
                return hasPermission(PermissionKey.PC_CREATE_INVENTORY_ADJUSTMENT_UIN);
            }
        }
        return true;
    }

    public void validateUINs(List<SerialNumberWrapper> wrappers) throws Exception {
        InventoryAdjustmentValidateUINCommand command = ClientCommandFactory.createInventoryAdjustmentValidateUINCommand();
        command.setStoreId(SimRepository.getStoreId());
        command.setReason(lineItemWrapper.getReason());

        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
            if (serialNumber != null) {
                command.setUINLabel(wrapper.getUINLabel());
                command.setSerialNumber(serialNumber);
                command.execute();
            }
        }
    }

    public void saveSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
            if (serialNumber != null) {
                if (wrapper.isDeleted()) {
                    lineItemWrapper.removeSerialNumber(serialNumber);
                } else if (wrapper.isAdded()) {
                    lineItemWrapper.addSerialNumber(serialNumber);
                }
            }
        }
    }

    public boolean isNonSellableTypeMismatch(SerialNumberValue serialNumber) {
        if (isNonSellableTypesActive() && serialNumber.getStatus() == UINStatus.UNAVAILABLE) {
            Long nonSellableQtyTypeId = lineItemWrapper.getReason().getFromNonSellableQtyTypeId();
            if (nonSellableQtyTypeId != null) {
                return !nonSellableQtyTypeId.equals(serialNumber.getNonSellableQtyTypeId());
            }
        }
        return false;
    }
}
