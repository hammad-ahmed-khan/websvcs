package oracle.retail.sim.client.screen.invadjustment;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentValidateUINCommand;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;

/**
 * Validates a serial number status for the client
 * 
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public final class InventoryAdjustmentIsValidSerialNumberRule {

    public static final void execute(InventoryAdjustment adjustment, StockItem stockItem, InventoryAdjustmentReason reason, SerialNumberValue serialNumber) throws Exception {
        String uinLabel = stockItem.getUINLabel();
        InventoryDisposition disposition = reason.getDisposition();

        if (disposition == InventoryDisposition.OUT_TO_AVAILABLE) {
            if (serialNumber.getStatus() == UINStatus.UNCONFIRMED) {
                if (!PermissionManager.hasPermission(PermissionKey.PC_CREATE_INVENTORY_ADJUSTMENT_UIN)) {
                    Object[] values = new Object[] { uinLabel, serialNumber.getUin(), stockItem.getId() };
                    throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
                }
            }
        }

        for (InventoryAdjustmentLineItem lineItem : adjustment.getLineItems()) {
            if (lineItem.getStockItem().getId().equals(stockItem.getId())) {
                for (SerialNumberValue existingSerialNumber : lineItem.getSerialNumbers()) {
                    if (existingSerialNumber.getUinId().equals(serialNumber.getUinId())) {
                        throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
                    }
                }
            }
        }

        InventoryAdjustmentValidateUINCommand command = ClientCommandFactory.createInventoryAdjustmentValidateUINCommand();
        command.setStoreId(SimRepository.getStoreId());
        command.setReason(reason);
        command.setUINLabel(uinLabel);
        command.setSerialNumber(serialNumber);
        command.execute();

        if (serialNumber.getStatus() == UINStatus.UNAVAILABLE) {
            if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
                Long nonSellableQtyTypeId = reason.getFromNonSellableQtyTypeId();
                if (nonSellableQtyTypeId != null) {
                    if (!nonSellableQtyTypeId.equals(serialNumber.getNonSellableQtyTypeId())) {
                        Object[] values = new Object[] { uinLabel, serialNumber.getUin() };
                        throw new BusinessException(InventoryAdjustmentMessageText.UIN_NONSELL_INVALID, values);
                    }
                }
            }
        }
    }
}
