package oracle.retail.sim.client.screen.itemticket;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.itemticket.ItemTicketQueryFilter;
import oracle.retail.sim.common.itemticket.ItemTicketStatus;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Ticket Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketFilterDialogModel extends SimScreenModel {
    private ItemTicketQueryFilter filter;
    private List<TicketType> ticketTypes;

    public void setFilter(ItemTicketQueryFilter filter) {
        this.filter = filter;
    }

    public ItemTicketQueryFilter getFilter() {
        return filter;
    }

    public ItemTicketQueryFilter resetFilter() {
        filter = BOFactory.createItemTicketQueryFilter(getStoreId());
        return filter;
    }

    public List<TicketTypeFormat> getItemTicketLabelFormats() throws Exception {
        return ClientDataCacheUtility.getItemTicketLabelFormats();
    }

    public TicketTypeFormat getTicketTypeFormat(String formatId) throws Exception {
        List<TicketTypeFormat> formats = this.getItemTicketLabelFormats();
        TicketTypeFormat ticktTypeFormat = null;
        if (formats != null && formats.size() > 0) {
            for (TicketTypeFormat format : formats) {
                if (format.getId().equals(formatId)) {
                    ticktTypeFormat = format;
                    break;
                }
            }
        }
        return ticktTypeFormat;
    }

    public List<TicketType> getTicketLabelTypes() throws Exception {
        if (ticketTypes == null) {
            ticketTypes = ClientServiceFactory.getItemTicketServices().findTicketTypes();
        }
        List<TicketType> typeList = new ArrayList<>();
        //TODO: SIM14.1 remove this
        for (TicketType ticketType : ticketTypes) {
            if (ticketType.getId().equals(TicketTypeId.ITEM_TICKET_ID)) {
                typeList.add(ticketType);
            } else if (ticketType.getId().equals(TicketTypeId.SHELF_LABEL_ID)) {
                typeList.add(ticketType);
            } else if (ticketType.getId().equals(TicketTypeId.AGSN_ID)) {
                typeList.add(ticketType);
            }
        }
        return typeList;
    }

    public List<ItemTicketStatus> getTicketStatus() {
        List<ItemTicketStatus> statusList = new ArrayList<>(3);
        statusList.add(ItemTicketStatus.PENDING);
        statusList.add(ItemTicketStatus.PRINTED);
        statusList.add(ItemTicketStatus.CANCELED);
        return statusList;
    }

    public void validateAndAssignExternalPoId(String externalPOId) throws Exception {
        if (ClientServiceFactory.getDirectDeliveryServices().readPurchaseOrderVO(externalPOId, getStoreId()) == null) {
            throw new BusinessException(DirectDeliveryMessageText.PO_INVALID_ENTRY);
        }
        filter.setExternalPoId(externalPOId);
    }

}
