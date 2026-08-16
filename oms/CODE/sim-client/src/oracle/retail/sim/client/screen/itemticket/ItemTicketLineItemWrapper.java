package oracle.retail.sim.client.screen.itemticket;

import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.uin.SerialNumberValue;

/**
 * Wraps the Item Ticket Line Item for the PC UI. Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ItemTicketLineItemWrapper {
    private ItemTicket itemTicket;

    public ItemTicketLineItemWrapper(ItemTicket itemTicket) {
        this.itemTicket = itemTicket;
    }

    public RetailItem getRetailItem() {
        return itemTicket != null ? itemTicket.getRetailItem() : null;
    }

    public String getItemId() {
        if (itemTicket == null) {
            return null;
        }
        return itemTicket.getRetailItem().getId();
    }

    public String getDescription() {
        if (itemTicket == null) {
            return null;
        }
        if (SimConfigManager.isItemShortDescription()) {
            return itemTicket.getRetailItem().getShortDescription();
        }
        return itemTicket.getRetailItem().getLongDescription();
    }

    public boolean isNewWrapper() {
        return itemTicket == null;
    }

    public List<SerialNumberValue> getSerialNumbers() {
        return itemTicket.getSerialNumbers();
    }

    public List<SerialNumberValue> getRemovedSerialNumbers() {
        return itemTicket.getRemovedSerialNumbers();
    }

    public void addSerialNumber(SerialNumberValue value) throws BusinessException {
        itemTicket.addSerialNumber(value);
    }

    public void removeSerialNumber(SerialNumberValue value) throws BusinessException {
        itemTicket.removeSerialNumber(value.getUin());
    }

    public Integer getSerialNumberCount() {
        return itemTicket != null ? itemTicket.getSerialNumbers().size() : 0;
    }

}
