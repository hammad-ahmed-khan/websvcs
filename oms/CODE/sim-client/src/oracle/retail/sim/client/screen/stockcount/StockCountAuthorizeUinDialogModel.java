package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.StockCountItem;
import oracle.retail.sim.common.lineitem.UOMConstants;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.stockcount.StockCountAuthorizeSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.storesequence.StoreSequenceConstants;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Authorization Multiple Location Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeUinDialogModel extends SimScreenModel {

    private StockCountWrapper stockCountWrapper;
    private StockCountLineItemWrapper lineItemWrapper;
    private QuantityDisplayer quantityDisplayer = new QuantityDisplayer();
    private List<StockCountAuthorizeUinWrapper> activeSerialNumberWrappers = new ArrayList<>();
    private List<StockCountAuthorizeUinWrapper> deletedSerialNumberWrappers = new ArrayList<>();

    public void setStockCount(StockCountWrapper stockCount) {
        stockCountWrapper = stockCount;
    }

    public void setLineItem(StockCountLineItemWrapper lineItem) {
        lineItemWrapper = lineItem;
    }

    public boolean isRecountRequired() {
        if (stockCountWrapper != null) {
            return stockCountWrapper.isRecountRequired();
        }
        return false;
    }

    public boolean isSerialNumbersEditable() {
        switch (stockCountWrapper.getStockCountChild().getStatus()) {
            case APPROVAL_SCHEDULED:
            case APPROVAL_IN_PROGRESS:
                return true;
            default:
                return false;
        }
    }

    public StockCountItem getStockCountItem() {
        return lineItemWrapper.getLineItem().getStockCountItem();
    }

    public UINType getUINType() {
        return lineItemWrapper.getLineItem().getStockCountItem().getUINType();
    }

    public String getUINLabel() {
        if (lineItemWrapper != null) {
            String uinLabel = lineItemWrapper.getUINLabel();
            if (StringUtility.isNullOrEmpty(uinLabel)) {
                try {
                    uinLabel = ClientServiceFactory.getUINServices().readUINLabel(lineItemWrapper.getItemId(), getStoreId());
                    lineItemWrapper.setSerialNumberLabel(uinLabel);
                } catch (Exception e) {
                    LogService.error(this, "No Serial Label!", e);
                    uinLabel = UINType.SERIAL.toString();
                }
            }
            return uinLabel;
        }
        return UINType.SERIAL.toString();
    }

    public List<ItemCountType> getFilterOptions() {
        List<ItemCountType> types = new ArrayList<>(3);
        types.add(ItemCountType.ALL);
        types.add(ItemCountType.COUNTED);
        types.add(ItemCountType.UNCOUNTED);
        return types;
    }

    public List<StockCountAuthorizeUinWrapper> getLineItems() throws Exception {
        if (activeSerialNumberWrappers.isEmpty()) {
            Long storeId = stockCountWrapper.getStoreId();
            Long stockCountId = stockCountWrapper.getId();
            String itemId = lineItemWrapper.getItemId();
            UINType uinType = getUINType();
            String uinLabel = getUINLabel();
            for (StockCountAuthorizeSerialNumber vo : ClientServiceFactory.getStockCountLineItemServices().findAuthorizationLineItemSerialNumbers(storeId, stockCountId, itemId)) {
                activeSerialNumberWrappers.add(ClientWrapperFactory.createStockCountAuthorizeUinWrapper(vo, uinType, uinLabel));
            }
        }
        return activeSerialNumberWrappers;
    }

    public List<StockCountAuthorizeUinWrapper> getActiveLineItems() {
        return activeSerialNumberWrappers;
    }

    public List<StockCountAuthorizeUinWrapper> filterLineItems(ItemCountType filterType) {
        if (filterType == ItemCountType.ALL) {
            return activeSerialNumberWrappers;
        }
        List<StockCountAuthorizeUinWrapper> filteredWrappers = new ArrayList<>();
        for (StockCountAuthorizeUinWrapper lineItemWrapper : activeSerialNumberWrappers) {
            if (filterType == ItemCountType.COUNTED) {
                if (isRecountRequired() && lineItemWrapper.getRecountQuantity().equals(Quantity.ONE)) {
                    filteredWrappers.add(lineItemWrapper);
                } else if (lineItemWrapper.getCountQuantity().equals(Quantity.ONE)) {
                    filteredWrappers.add(lineItemWrapper);
                }
            } else {
                if (isRecountRequired() && lineItemWrapper.getRecountQuantity().equals(Quantity.ZERO)) {
                    filteredWrappers.add(lineItemWrapper);
                } else if (lineItemWrapper.getCountQuantity().equals(Quantity.ZERO)) {
                    filteredWrappers.add(lineItemWrapper);
                }
            }
        }
        return filteredWrappers;
    }

    public String getTotalCountedDisplayValue(List<StockCountAuthorizeUinWrapper> lineItems) {
        int quantity = 0;
        for (StockCountAuthorizeUinWrapper wrapper : lineItems) {
            if (wrapper.getLineItem().isCounted()) {
                quantity++;
            }
        }
        return formatUnitOfMeasureCount(new Quantity(quantity));
    }

    public String getTotalRecountedDisplayValue(List<StockCountAuthorizeUinWrapper> lineItems) {
        int quantity = 0;
        for (StockCountAuthorizeUinWrapper wrapper : lineItems) {
            if (wrapper.getLineItem().isRecounted()) {
                quantity++;
            }
        }
        return formatUnitOfMeasureCount(new Quantity(quantity));
    }

    public String getStockOnHandDisplayValue() {
        return formatUnitOfMeasureCount(lineItemWrapper.getSnapshot());
    }

    public String getAuthorizedQuantityDisplayValue(List<StockCountAuthorizeUinWrapper> lineItems) {
        int quantity = 0;
        for (StockCountAuthorizeUinWrapper wrapper : lineItems) {
            if (wrapper.getLineItem().isApproved()) {
                quantity++;
            }
        }
        return formatUnitOfMeasureCount(new Quantity(quantity));
    }

    private String formatUnitOfMeasureCount(Quantity quantity) {
        String uom = Translator.getText(UOMConstants.UNITS);
        if (quantity == null) {
            quantity = Quantity.ZERO;
        }
        return quantityDisplayer.getDisplayText(quantity) + " " + uom;
    }

    public StockCountAuthorizeUinWrapper createNewWrapper() {
        StockCountAuthorizeSerialNumber lineItem = BOFactory.createStockCountLineItemAuthUINVO();
        lineItem.doSetItemId(lineItemWrapper.getLineItem().getStockCountItem().getId());
        lineItem.doSetLocationDescription(StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION);
        lineItem.doSetUINStatus(UINStatus.UNCONFIRMED);

        StockCountAuthorizeUinWrapper wrapper = ClientWrapperFactory.createStockCountAuthorizeUinWrapper(lineItem, getUINType(), getUINLabel());
        activeSerialNumberWrappers.add(wrapper);
        return wrapper;
    }

    public boolean isValidForDelete(StockCountAuthorizeUinWrapper wrapper) {
        StockCountAuthorizeSerialNumber lineVO = wrapper.getLineItem();
        if (lineVO.getUINStatus() == UINStatus.UNCONFIRMED) {
            return !lineVO.isCounted() && !lineVO.isRecounted();
        }
        return false;
    }

    public void removeSerialNumber(StockCountAuthorizeUinWrapper wrapper) {
        activeSerialNumberWrappers.remove(wrapper);

        if (wrapper.getLineItem().isPersisted()) {
            deletedSerialNumberWrappers.add(wrapper);
        }
    }

    public void saveSerialNumberDetails() throws Exception {
        List<StockCountAuthorizeSerialNumber> lineItemSerialNumbers = new ArrayList<>();
        for (StockCountAuthorizeUinWrapper wrapper : activeSerialNumberWrappers) {
            if (wrapper.getSerialNumber() != null) {
                lineItemSerialNumbers.add(wrapper.getLineItem());
            }
        }
        for (StockCountAuthorizeUinWrapper wrapper : deletedSerialNumberWrappers) {
            StockCountAuthorizeSerialNumber lineItemSerialNumber = wrapper.getLineItem();
            lineItemSerialNumber.doSetIsApproved(false);
            lineItemSerialNumbers.add(lineItemSerialNumber);
        }
        // Sum Up The Quantity Of Approved
        int quantity = 0;
        for (StockCountAuthorizeSerialNumber lineItemSerialNumber : lineItemSerialNumbers) {
            if (lineItemSerialNumber.isApproved()) {
                quantity++;
            }
        }
        // Update Actual Stock Approved
        lineItemWrapper.getLineItem().doSetStockApproved(new Quantity(quantity));

        // Save Information
        ClientServiceFactory.getStockCountLineItemServices().updateAuthorizedSerialNumbers(lineItemWrapper.getLineItem(), lineItemSerialNumbers);
    }

    public void generateNewSerialNumbers(Integer quantity) throws Exception {
        Long storeId = stockCountWrapper.getStoreId();
        UINType uinType = getUINType();
        String uinLabel = getUINLabel();
        StockCountLineItem lineItem = lineItemWrapper.getLineItem();
        for (StockCountAuthorizeSerialNumber vo : ClientServiceFactory.getStockCountLineItemServices().generateAuthorizedSerialNumbers(storeId, lineItem, quantity)) {
            activeSerialNumberWrappers.add(ClientWrapperFactory.createStockCountAuthorizeUinWrapper(vo, uinType, uinLabel));
        }
    }

    public boolean checkStockCountChildLock() throws Exception {
        return confirmLock(ActivityLockType.STOCK_COUNT_CHILD, stockCountWrapper.getStockCountChild().getId());
    }
}
