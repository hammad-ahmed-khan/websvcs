package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.carton.CartonLineItemWrapper;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINUserAction;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * The business logic model for the UIN Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class WarehouseDeliveryUinDialogModel extends SimScreenModel {
    private static final String NEW_AGSN = "New";

    private FunctionalArea functionalArea;
    private WarehouseDeliveryLineItem deliveryLineItem;

    public FunctionalArea getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(FunctionalArea functionalArea) {
        this.functionalArea = functionalArea;
    }

    public void setSerialNumberLineItemWrapper(SerialNumberLineItemWrapper wrapper) {
        if (wrapper instanceof WarehouseDeliveryLineItemWrapper) {
            deliveryLineItem = ((WarehouseDeliveryLineItemWrapper) wrapper).getLineItem();
        } else if (wrapper instanceof CartonLineItemWrapper) {
            deliveryLineItem = ((CartonLineItemWrapper) wrapper).getLineItem();
        } else {
            deliveryLineItem = null;
        }
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
        if (deliveryLineItem.getStockItem().isAgsnEnabled() && !isFinisherDelivery()) {
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
        SerialNumberWrapper wrapper = ClientWrapperFactory.createSerialNumberWrapper(functionalArea, stockItem.getId(), stockItem.getUINType(), stockItem.getUINLabel());
        if (serialNumber == null && stockItem.isAgsnEnabled() && !isFinisherDelivery()) {
            serialNumber = BOFactory.createSerialNumberValue();
            serialNumber.doSetUin(StringConstants.DASH + Translator.getText(NEW_AGSN) + StringConstants.DASH);
        }
        wrapper.setDamagedEditable(damagesEditable);
        wrapper.setSerialNumberValue(serialNumber);
        return wrapper;
    }

    public String getUINLabel() {
        return deliveryLineItem.getStockItem().getUINLabel();
    }

    public boolean isFinisherDelivery() {
        WarehouseDelivery delivery = deliveryLineItem.getCarton().getDelivery();
        return delivery != null && delivery.isFinisherDelivery();
    }

    public boolean isDamagesEditable() {
        switch (functionalArea) {
            case WAREHOUSE_DELIVERY_RECEIPT:
                return !SimConfigManager.getBoolean(SimConfigManager.DISABLE_DAMAGES);
        }
        return false;
    }

    public boolean isValuesEditable() {
        switch (functionalArea) {
            case WAREHOUSE_DELIVERY_RECEIPT:
                return !WarehouseDeliveryStatus.getClosedSet().contains(deliveryLineItem.getCarton().getDelivery().getStatus());
        }
        return false;
    }

    public boolean isAdjustment() {
        switch (functionalArea) {
            case WAREHOUSE_DELIVERY_RECEIPT:
                return deliveryLineItem.getCarton().getDelivery().isAllowAdjustment();
        }
        return false;
    }

    public boolean isDuplicateSerialNumber(SerialNumberValue serialNumber) {
        switch (functionalArea) {
            case WAREHOUSE_DELIVERY_RECEIPT:
                Long cartonId = deliveryLineItem.getCarton().getId();
                String itemId = deliveryLineItem.getStockItem().getId();
                String uin = serialNumber.getUin();
                WarehouseDelivery delivery = deliveryLineItem.getCarton().getDelivery();
                for (WarehouseDeliveryCarton carton : delivery.getCartons()) {
                    if (cartonId.equals(carton.getId())) {
                        continue;
                    }
                    for (WarehouseDeliveryLineItem lineItem : carton.getLineItems()) {
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
                break;
        }
        return false;
    }

    public boolean isSerialNumberAtFinisher(SerialNumberValue serialNumber) throws Exception {
        String receiptParentDocumentId = deliveryLineItem.getReceiptParentDocumentId();
        if (StringHelper.isNullOrEmpty(receiptParentDocumentId) || !NumberHelper.isIdentifierNumeric(receiptParentDocumentId)) {
            return false;
        }
        return ClientServiceFactory.getReturnServices().isSerialNumberOnReturn(receiptParentDocumentId, SourceType.FINISHER, serialNumber.getUinId());
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
