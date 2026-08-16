package oracle.retail.sim.client.screen.itemticket;

import java.util.Date;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketStatus;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;

/********************************************************************************************************
 * Item Ticket Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketWrapper {

    private ItemTicket itemTicket;
    private TicketTypeFormat typeFormat;

    public ItemTicketWrapper(ItemTicket itemTicket) {
        this.itemTicket = itemTicket;
        typeFormat = itemTicket.getTicketTypeFormat();
    }

    public ItemTicket getItemTicket() {
        return itemTicket;
    }

    public String getItemId() {
        return itemTicket.getRetailItem().getId();
    }

    public String getItemDescription() {
        if (SimConfigManager.isItemShortDescription()) {
            return itemTicket.getRetailItem().getShortDescription();
        }
        return itemTicket.getRetailItem().getLongDescription();
    }

    public TicketType getLabelType() {
        if (typeFormat != null) {
            TicketType type = typeFormat.getTicketType();

            if (type != null) {
                return type;
            }
        }
        return null;
    }

    public String getFormat() {
        if (typeFormat != null) {
            return typeFormat.getFormatName();
        }
        return StringConstants.EMPTY;
    }

    public Integer getQuantity() {
        return itemTicket.getQuantity();
    }

    public SimMoney getPrice() {
        if (itemTicket.getOverridePrice() != null) {
            return itemTicket.getOverridePrice();
        }
        if (itemTicket.getLabelPrice() != null) {
            return itemTicket.getLabelPrice();
        }
        return itemTicket.getRetailItem().getRetailPrice();
    }

    public ItemTicketStatus getStatus() {
        return itemTicket.getStatus();
    }

    /** Gets the ticket's suggested ticket type code. */
    public String getSuggestedTicketTypeCode() {
        TicketType ticketType = getLabelType();
        if (ticketType == null || ticketType.getId().equals(TicketTypeId.SHELF_LABEL_ID)) {
            return null;
        }
        return itemTicket.getRetailItem().getSuggestedTicketTypeCode();
    }

    public String getPromotionId() {
        return itemTicket.getPromotionId();
    }

    /** Gets the date that the price change will take effect. */
    public Date getEffectiveDate() {
        return itemTicket.getEffectiveDate();
    }

    public String getExternalPurchaseOrderId() {
        return itemTicket.getExternalPoId();
    }

    public String getUserId() {
        return itemTicket.getUserId();
    }

    public boolean isMultiUnitPriceChange() {
        return itemTicket.isMultiUnitPriceChange();
    }
}
