package oracle.retail.sim.client.screen.itemticket;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketQueryFilter;
import oracle.retail.sim.common.itemticket.ItemTicketStatus;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Ticket List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketListModel extends SimScreenModel {
    public List<ItemTicketWrapper> findItemTickets() throws Exception {
        List<ItemTicket> tickets = ClientServiceFactory.getItemTicketServices().findItemTickets(getFilter());
        List<ItemTicketWrapper> itemTickets = new ArrayList<>(tickets.size());
        for (ItemTicket ticket : tickets) {
            itemTickets.add(ClientWrapperFactory.createItemTicketWrapper(ticket));
        }
        return itemTickets;
    }

    public ItemTicketQueryFilter getFilter() {
        ItemTicketQueryFilter filter = (ItemTicketQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.ITEM_TICKET_FILTER);
        if (filter == null) {
            filter = BOFactory.createItemTicketQueryFilter(getStoreId());
            filter.doSetStatus(ItemTicketStatus.PENDING);
            RepositoryManager.addStateObject(SimClientStateKey.ITEM_TICKET_FILTER, filter);
        }
        return filter;
    }

    public void storeItemTicket(ItemTicketWrapper wrapper) {
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_TICKET_DETAIL, wrapper.getItemTicket());
    }

    public boolean deleteTickets(List<ItemTicketWrapper> wrappers) throws Exception {
        for (ItemTicketWrapper wrapper : wrappers) {
            ItemTicket itemTicket = wrapper.getItemTicket();
            if (!obtainLock(ActivityLockType.ITEM_TICKET, itemTicket.getId())) {
                return false;
            }
            itemTicket.setStatus(ItemTicketStatus.CANCELED);
            ClientServiceFactory.getItemTicketServices().updateItemTicket(itemTicket);
        }
        return true;
    }

    public void updateStockOnHand(ItemTicket itemTicket) throws Exception {
        Quantity stockOnHand = itemTicket.getRetailItem().getAvailableStockOnHand();

        if (stockOnHand.intValue() < 1) {
            throw new BusinessException(ItemMessageText.ITEM_SOH_NOT_UPDATED);
        }

        itemTicket.setQuantity(stockOnHand.intValue());

        if (!obtainLock(ActivityLockType.ITEM_TICKET, itemTicket.getId())) {
            throw new BusinessException(CommonMessageText.LOCK_TAKEN_OVER);
        }

        ClientServiceFactory.getItemTicketServices().updateItemTicket(itemTicket);

        releaseLock(ActivityLockType.ITEM_TICKET, itemTicket.getId());
    }

    public StorePrinter selectPrinter(ItemTicket itemTicket) throws Exception {
        return SimClientPrintUtility.selectItemTicketPrinter(itemTicket, getStore());
    }

    public ReportResponse printTickets(List<ItemTicketWrapper> itemTicketWrappers, StorePrinter printer) throws Exception {
        List<ItemTicket> itemTickets = new ArrayList<ItemTicket>();
        for (ItemTicketWrapper itemTicketWrapper : itemTicketWrappers) {
            ItemTicket itemTicket = itemTicketWrapper.getItemTicket();
            itemTickets.add(itemTicket);
        }
        return ItemTicketPrintUtility.printTickets(itemTickets, printer);
    }

    public void markTicketSentPrint(List<ItemTicketWrapper> itemTicketWrappers) throws Exception {
        List<ItemTicket> itemTickets = new ArrayList<ItemTicket>();
        for (ItemTicketWrapper itemTicketWrapper : itemTicketWrappers) {
            ItemTicket itemTicket = itemTicketWrapper.getItemTicket();
            itemTicket.setStatus(ItemTicketStatus.PRINTED);
            itemTickets.add(itemTicket);

        }
        if (itemTickets.size() > 0) {
            ClientServiceFactory.getItemTicketServices().updateItemTickets(itemTickets);
        }

    }

    public Map<String, String> getDescriptionMap() throws Exception {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        DateFormat formatter = LocaleManager.getShortDateFormatter();
        ItemTicketQueryFilter filter = getFilter();
        if (filter.getFromEffectiveDate() != null) {
            descriptionMap.put("Effective From Date", formatter.format(filter.getFromEffectiveDate()));
        }
        if (filter.getToEffectiveDate() != null) {
            descriptionMap.put("Effective To Date", formatter.format(filter.getToEffectiveDate()));
        }
        if (filter.getDepartmentId() != null) {
            descriptionMap.put("Dept", String.valueOf(filter.getDepartmentId()));
        }
        if (filter.getClassId() != null) {
            descriptionMap.put("Class", String.valueOf(filter.getClassId()));
        }
        if (filter.getSubclassId() != null) {
            descriptionMap.put("Sub-Class", String.valueOf(filter.getSubclassId()));
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getExternalPoId() != null) {
            descriptionMap.put("PO Number", filter.getExternalPoId());
        }
        if (filter.getLabelType() != null) {
            descriptionMap.put("Label Type", filter.getLabelType().getDescription());
        }
        if (filter.getTicketTypeFormatId() != null) {
            TicketTypeFormat format = getTicketTypeFormat(filter.getTicketTypeFormatId());
            if (format != null) {
                descriptionMap.put("Format Name", format.getFormatName());
            }

        }

        if (filter.getPromotionId() != null) {
            descriptionMap.put("Promotion ID", String.valueOf(filter.getPromotionId()));
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
        }
        return descriptionMap;
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

}
