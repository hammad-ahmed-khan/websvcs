package oracle.retail.sim.client.screen.returns;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;
import oracle.retail.sim.common.stockreturn.ReturnValidateUINCommand;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public final class ReturnIsValidSerialNumberRule {

    public static final void execute(Return stockReturn, ReturnLineItemWrapper wrapper, SerialNumberValue serialNumber) throws Exception {
        StockItem stockItem = wrapper.getStockItem();
        String uinLabel = stockItem.getUINLabel();

        ReturnValidateUINCommand command = ClientCommandFactory.createReturnValidateUINCommand();
        command.setFunctionalArea(FunctionalArea.CREATE_RETURN);
        command.setUINLabel(uinLabel);
        command.setSerialNumber(serialNumber);
        command.execute();

        if (!serialNumber.getStoreId().equals(stockReturn.getStoreId())) {
            String[] params = { stockItem.getUINType().toString(), serialNumber.getUin(), Translator.getText("Return") };
            throw new BusinessException(UINMessageText.UIN_STORE_MISMATCH, params);
        }
        if (stockReturn.isUseUnavailable() && serialNumber.getStatus() != UINStatus.UNAVAILABLE) {
            throw new BusinessException(ReturnMessageText.INVENTORY_STATUS_MISMATCH, uinLabel);
        }
        if (stockReturn.isUseUnavailable() && serialNumber.getStatus() == UINStatus.UNAVAILABLE) {
            if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
                Long nonSellableQtyTypeId = wrapper.getReason().getNonSellableQtyTypeId();
                if (nonSellableQtyTypeId != null) {
                    if (!nonSellableQtyTypeId.equals(serialNumber.getNonSellableQtyTypeId())) {
                        throw new BusinessException(ReturnMessageText.INVENTORY_STATUS_MISMATCH, uinLabel);
                    }
                }
            }
        }
        if (stockReturn.isUseAvailable() && serialNumber.getStatus() != UINStatus.IN_STOCK) {
            throw new BusinessException(ReturnMessageText.INVENTORY_STATUS_MISMATCH, uinLabel);
        }
    }
}
