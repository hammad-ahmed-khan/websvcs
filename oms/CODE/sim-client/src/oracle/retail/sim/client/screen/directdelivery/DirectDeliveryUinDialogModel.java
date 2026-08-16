package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryCarton;
import oracle.retail.sim.common.directdelivery.DirectDeliveryLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * The business logic model for the UIN Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DirectDeliveryUinDialogModel extends SimScreenModel {
    private static final String NEW_AGSN = "New";

    private DirectDeliveryLineItem deliveryLineItem;
    private boolean discrepantQuantity;
    private boolean hasPurchaseOrder;

    public void setDirectDeliveryLineItemWrapper(DirectDeliveryLineItemWrapper wrapper) {
        if (wrapper == null) {
            deliveryLineItem = null;
            discrepantQuantity = false;
            return;
        }
        deliveryLineItem = wrapper.getLineItem();
        hasPurchaseOrder = wrapper.hasPurchaseOrder();
        discrepantQuantity = deliveryLineItem.getQuantityReceivedOrZero().add(deliveryLineItem.getQuantityDamagedOrZero()).intValue() != deliveryLineItem.getSerialNumbers().size();
    }

    public String getItemId() {
        return deliveryLineItem.getStockItem().getId();
    }

    public String getItemDescription() {
        return SimConfigManager.isItemShortDescription() ? deliveryLineItem.getStockItem().getShortDescription() : deliveryLineItem.getStockItem().getLongDescription();
    }

    public List<SerialNumberWrapper> getSerialNumberWrappers() {
        List<SerialNumberWrapper> wrappers = new ArrayList<SerialNumberWrapper>();
        boolean damagesEditable = isDamagesEditable();
        List<SerialNumberValue> serialNumbers = deliveryLineItem.getSerialNumbers();
        int receivedSerialNumbers = 0;
        int damagedSerialNumbers = 0;
        for (SerialNumberValue serialNumber : serialNumbers) {
            if (serialNumber.isDamaged()) {
                damagedSerialNumbers++;
            } else {
                receivedSerialNumbers++;
            }
            SerialNumberWrapper wrapper = createUINWrapper(damagesEditable, serialNumber);
            wrapper.setDefaultAction();
            wrapper.setValidated();
            wrappers.add(wrapper);
        }
        if (deliveryLineItem.getStockItem().isAgsnEnabled()) {
            int receivedRemaining = deliveryLineItem.getQuantityReceivedOrZero().intValue() - receivedSerialNumbers;
            int damagedRemaining = deliveryLineItem.getQuantityDamagedOrZero().intValue() - damagedSerialNumbers;
            String newAgsn = StringConstants.DASH + Translator.getText(NEW_AGSN) + StringConstants.DASH;
            for (int i = 0; i < receivedRemaining; i++) {
                SerialNumberValue serialNumber = BOFactory.createSerialNumberValue();
                serialNumber.doSetUin(newAgsn);
                SerialNumberWrapper wrapper = createUINWrapper(damagesEditable, serialNumber);
                wrapper.setDefaultAction();
                wrappers.add(wrapper);
            }
            for (int i = 0; i < damagedRemaining; i++) {
                SerialNumberValue serialNumber = BOFactory.createSerialNumberValue();
                serialNumber.doSetUin(newAgsn);
                serialNumber.doSetDamaged(true);
                SerialNumberWrapper wrapper = createUINWrapper(damagesEditable, serialNumber);
                wrapper.setDefaultAction();
                wrappers.add(wrapper);
            }
        }
        for (SerialNumberValue serialNumber : deliveryLineItem.getRemovedSerialNumbers()) {
            SerialNumberWrapper wrapper = createUINWrapper(damagesEditable, serialNumber);
            wrapper.setUserAction(UINUserAction.REMOVED);
            wrapper.setValidated();
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    public SerialNumberWrapper createUINWrapper() {
        return createUINWrapper(isDamagesEditable(), null);
    }

    private SerialNumberWrapper createUINWrapper(boolean damagesEditable, SerialNumberValue serialNumber) {
        StockItem stockItem = deliveryLineItem.getStockItem();
        SerialNumberWrapper wrapper = ClientWrapperFactory.createSerialNumberWrapper(FunctionalArea.DIRECT_DELIVERY_RECEIPT, stockItem.getId(), stockItem.getUINType(), stockItem.getUINLabel());
        wrapper.setDamagedEditable(damagesEditable);
        if (serialNumber != null) {
            wrapper.setSerialNumberValue(serialNumber);
        } else if (stockItem.isAgsnEnabled()) {
            serialNumber = BOFactory.createSerialNumberValue();
            serialNumber.doSetUin(StringConstants.DASH + Translator.getText(NEW_AGSN) + StringConstants.DASH);
            wrapper.setSerialNumberValue(serialNumber);
        }
        return wrapper;
    }

    public String getUINLabel() {
        return deliveryLineItem.getStockItem().getUINLabel();
    }

    public boolean isDamagesEditable() {
        return PermissionManager.hasPermission(PermissionKey.PC_RECEIVE_DAMAGES_DIRECT_DELIVERY) && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_DAMAGES);
    }

    public boolean isValuesEditable() {
        return !DirectDeliveryStatus.getClosedSet().contains(deliveryLineItem.getCarton().getDelivery().getStatus());
    }

    public boolean isAdjustment() {
        return deliveryLineItem.getCarton().getDelivery().isAllowAdjustment();
    }

    public boolean isDuplicateSerialNumber(SerialNumberValue serialNumber) {
        DirectDelivery delivery = deliveryLineItem.getCarton().getDelivery();
        Long cartonId = deliveryLineItem.getCarton().getId();
        String itemId = deliveryLineItem.getStockItem().getId();
        String uin = serialNumber.getUin();
        for (DirectDeliveryCarton carton : delivery.getCartons()) {
            if (carton.getId() != null && carton.getId().equals(cartonId)) {
                continue;
            }
            for (DirectDeliveryLineItem lineItem : carton.getLineItems()) {
                if (!itemId.equals(lineItem.getStockItem().getId())) {
                    continue;
                }
                for (SerialNumberValue lineItemSerialNumber : lineItem.getSerialNumbers()) {
                    if (uin.equals(lineItemSerialNumber.getUin())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean saveSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        Set<SerialNumberValue> wrapperValues = new HashSet<SerialNumberValue>();
        int receivedSerialNumbers = 0;
        int damagedSerialNumbers = 0;
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
            if (serialNumber == null || StringHelper.isNullOrEmpty(serialNumber.getUin())) {
                continue;
            }
            if (wrapper.isDeleted()) {
                deliveryLineItem.removeSerialNumber(serialNumber.getUin());
                continue;
            }
            serialNumber.setDamaged(wrapper.isDamaged());
            if (serialNumber.isDamaged()) {
                damagedSerialNumbers++;
            } else {
                receivedSerialNumbers++;
            }
            if (serialNumber.getUinId() != null) {
                wrapperValues.add(serialNumber);
            }
        }
        Quantity received = new Quantity(receivedSerialNumbers);
        Quantity damaged = new Quantity(damagedSerialNumbers);
        //Check if quantity doesn't match for non-AGSN UINs (quantity defaulted before user started receiving, damages are not defaulted)
        if (discrepantQuantity && !deliveryLineItem.getStockItem().isAgsnEnabled()) {
            Quantity quantityReceived = deliveryLineItem.getQuantityReceivedOrZero();
            if (quantityReceived.isPositive() && quantityReceived.compareTo(received) != 0) {
                StockItem stockItem = deliveryLineItem.getStockItem();
                StringBuilder itemDescription = new StringBuilder(stockItem.getId());
                itemDescription.append(" - ").append(stockItem.getItem().getId()).append(StringConstants.DASH);
                itemDescription.append(SimConfigManager.isItemShortDescription() ? stockItem.getShortDescription() : stockItem.getLongDescription());
                String[] values = new String[3];
                values[0] = stockItem.getUINLabel() + StringConstants.SPACE + Translator.getText("UINs");
                values[1] = itemDescription.toString();
                values[2] = LocaleManager.getIntegerFormatter().format(receivedSerialNumbers);
                if (!RConfirmUtility.confirm("Quantity MisMatch Error", DirectDeliveryMessageText.UIN_DISPATCH_QTY_RECEIVED_MISMATCH, values)) {
                    //Don't close dialog
                    return false;
                }
            }
        }
        //Restrict over receiving
        if (hasPurchaseOrder && !PermissionManager.hasPermission(PermissionKey.PC_OVER_RECEIVE_DIRECT_DELIVERY)) {
            if (received.add(damaged).compareTo(deliveryLineItem.getQuantityExpectedOrZero()) > 0) {
                throw new BusinessException(DirectDeliveryMessageText.OVER_RECEIVING_NOT_ALLOWED);
            }
        }
        List<SerialNumberValue> originalValues = new ArrayList<SerialNumberValue>(deliveryLineItem.getSerialNumbers());
        for (SerialNumberValue value : originalValues) {
            if (!wrapperValues.contains(value)) {
                deliveryLineItem.removeSerialNumber(value.getUin());
            }
        }
        for (SerialNumberValue value : wrapperValues) {
            deliveryLineItem.addSerialNumber(value);
        }
        deliveryLineItem.setQuantityReceived(received);
        deliveryLineItem.setQuantityDamaged(damaged);
        return true;
    }
}
