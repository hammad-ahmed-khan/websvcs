package oracle.retail.sim.client.screen.itemticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.ItemSuppCtryMfrVO;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketStatus;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Ticket Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketDetailModel extends SimScreenModel {
    private ItemTicket itemTicket;
    private Map<String, RetailItem> retailItemCache = new HashMap<>();
    private Map<String, List<String>> countryOfManufactureMap = new HashMap<>();
    private boolean isViewOnlyMode;
    private String primaryMfrCountry = StringConstants.EMPTY;
    private List<SerialNumberValue> ticketSerialNumbers = new ArrayList<>();

    public void loadItemTicket() {
        itemTicket = (ItemTicket) RepositoryManager.getStateObject(SimClientStateKey.ITEM_TICKET_DETAIL);
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_TICKET_DETAIL);
    }

    public ItemTicket getItemTicket() {
        return itemTicket;
    }

    public boolean isNewTicket() {
        return itemTicket.getId() == null;
    }

    public boolean isItemEditable() {
        return itemTicket == null || itemTicket.getId() == null;
    }

    public boolean isLabelAndFormatEditable() {
        if (itemTicket == null) {
            return false;
        }
        if (itemTicket.getRetailItem() == null) {
            return false;
        }
        if (itemTicket.getStatus() == ItemTicketStatus.PRINTED) {
            return false;
        }
        return itemTicket.getStatus() != ItemTicketStatus.CANCELED;
    }

    public void setSerialNumbers(List<SerialNumberValue> ticketSerialNumbers) {
        this.ticketSerialNumbers = ticketSerialNumbers;
    }

    public List<TicketType> loadLabelTypes() throws Exception {
        List<TicketType> labelTypes = new ArrayList<>();

        List<TicketType> ticketTypes = ClientServiceFactory.getItemTicketServices().findTicketTypes();
        for (TicketType ticketType : ticketTypes) {
            if (ticketType.getId().equals(TicketTypeId.ITEM_TICKET_ID)) {
                labelTypes.add(ticketType);
            } else if (ticketType.getId().equals(TicketTypeId.SHELF_LABEL_ID)) {
                labelTypes.add(ticketType);
            } else if (ticketType.getId().equals(TicketTypeId.AGSN_ID)) {
                if (isAGSNEnabled()) {
                    labelTypes.add(ticketType);
                }
            }
        }

        return labelTypes;
    }

    public List<String> getCountriesOfManufacture() throws Exception {
        List<String> countries = countryOfManufactureMap.get(itemTicket.getRetailItem().getId());
        if (countries != null) {
            return countries;
        }
        List<ItemSuppCtryMfrVO> countryVOs = ClientServiceFactory.getItemServices().findCountriesOfManufacture(itemTicket.getRetailItem().getId(), getStoreId());
        countries = new ArrayList<>(countryVOs.size());
        for (ItemSuppCtryMfrVO countryVO : countryVOs) {
            countries.add(countryVO.getCountryId());
            if (countryVO.isPrimary()) {
                primaryMfrCountry = countryVO.getCountryId();
            }
        }
        countryOfManufactureMap.put(itemTicket.getRetailItem().getId(), countries);
        return countries;
    }

    public String getDefaultCountry() {
        return isNewTicket() ? primaryMfrCountry : itemTicket.getCountryManufacture();
    }

    public boolean isCountryModifiable() {
        try {
            return !getCountriesOfManufacture().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public TicketTypeFormat getTicketTypeFormat() {
        if (itemTicket != null) {
            return itemTicket.getTicketTypeFormat();
        }
        return null;
    }

    public boolean isQuantityModifiable() {
        return itemTicket.isPropertyModifiable("quantity");
    }

    public boolean isOverridePriceModifiable() {
        return itemTicket.isPropertyModifiable("ticketPrice");
    }

    public List<TicketTypeFormat> findTicketTypeFormats(Long ticketTypeId) throws Exception {
        if (ticketTypeId.equals(TicketTypeId.ITEM_TICKET_ID)) {
            return ClientDataCacheUtility.getItemTicketFormats();
        }
        if (ticketTypeId.equals(TicketTypeId.SHELF_LABEL_ID)) {
            return ClientDataCacheUtility.getShelfLabelFormats();
        }
        if (ticketTypeId.equals(TicketTypeId.AGSN_ID)) {
            return ClientDataCacheUtility.getAGSNTicketFormats();
        }
        return Collections.emptyList();
    }

    public void clearItemTicket() {
        itemTicket = null;
    }

    public void createItemTicket(RetailItem retailItem) throws Exception {
        itemTicket = BOFactory.createItemTicket(retailItem);
        itemTicket.setUserId(getUserName());

        List<TicketTypeFormat> formatList = findTicketTypeFormats(TicketTypeId.ITEM_TICKET_ID);
        if (formatList.isEmpty()) {
            throw new BusinessException(CommonMessageText.TICKET_NO_TYPE_FORMATS);
        }
        itemTicket.doSetTicketTypeFormat(null);

        String suggestedFormat = retailItem.getSuggestedTicketTypeCode();
        for (TicketTypeFormat format : formatList) {
            if (format.getFormatName().equalsIgnoreCase(suggestedFormat)) {
                itemTicket.setTicketTypeFormat(format);
                break;
            }
        }
    }

    public boolean obtainItemTicketLock() throws Exception {
        if (itemTicket != null && itemTicket.getId() != null) {
            return obtainLock(ActivityLockType.ITEM_TICKET, itemTicket.getId());
        }
        return true;
    }

    public boolean checkTicketLock() throws Exception {
        return confirmLock(ActivityLockType.ITEM_TICKET, itemTicket.getId());
    }

    public void releaseTicketLock() throws Exception {
        if (itemTicket == null) {
            return;
        }
        releaseLock(ActivityLockType.ITEM_TICKET, itemTicket.getId());
    }

    public void setLockNotBroken(boolean isLockNotBroken) {
        isViewOnlyMode = isLockNotBroken;
    }

    public boolean isTicketValidForUpdate() throws BusinessException {
        return itemTicket != null && itemTicket.isCoherent();
    }

    public void updateTicket(TicketType ticketType) throws Exception {
        itemTicket.setUserId(getUserName());
        itemTicket.getTicketTypeFormat().setTicketType(ticketType);
        if (isNewTicket()) {
            itemTicket = ClientServiceFactory.getItemTicketServices().createItemTicket(itemTicket);
        } else {
            ClientServiceFactory.getItemTicketServices().updateItemTicket(itemTicket);
        }
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_TICKET_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public boolean isItemTicketUnmodifiable() {
        if (!hasPermission(PermissionKey.PC_EDIT_ITEM_TICKET)) {
            return itemTicket != null && !isNewTicket();
        }
        return isViewOnlyMode;
    }

    public boolean isTicketCancelled() {
        if (itemTicket.getId() == null) {
            return false;
        }
        return itemTicket.getStatus() == ItemTicketStatus.CANCELED;
    }

    public boolean isTicketPrinted() {
        if (itemTicket.getId() == null) {
            return false;
        }
        return itemTicket.getStatus() == ItemTicketStatus.CANCELED;
    }

    public boolean isScannerAvailable() {
        return !isItemTicketUnmodifiable() && isItemEditable();
    }

    public ReportResponse printUINs(Map<String, List<String>> itemUins, StorePrinter printer) throws Exception {
        TicketTypeFormat ticketTypeFormat = getItemTicket().getTicketTypeFormat();
        //return ClientServiceFactory.getUINServices().printUINAgsnDetails(itemIdsAndUins, itemTicket.getStoreId(), printer, ticketTypeFormat);
        List<String> itemIds = new ArrayList<String>(itemUins.keySet());
        String itemId = itemIds.get(0);
        return ItemTicketPrintUtility.printUinLabels(itemId, itemUins.get(itemId), ticketTypeFormat.getTemplateURL(), printer, getStoreId());
    }

    public StorePrinter selectPrinter(ItemTicket itemTicket) throws Exception {
        return SimClientPrintUtility.selectItemTicketPrinter(itemTicket, getStore());
    }

    /**
     * Prints the ticket and returns the message to be displayed
     */
    public ReportResponse printTicket(StorePrinter printer) throws Exception {
        return ItemTicketPrintUtility.printTicket(itemTicket, printer);
    }

    public void markTicketSentPrint() throws Exception {
        itemTicket.setStatus(ItemTicketStatus.PRINTED);
        ClientServiceFactory.getItemTicketServices().updateItemTicket(itemTicket);
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_TICKET_DETAIL_MODIFIED, Boolean.TRUE);
        releaseLock(ActivityLockType.ITEM_TICKET, itemTicket.getId());
    }

    public Map<String, List<String>> getItemTicketUins() throws Exception {
        Map<String, List<String>> itemIdsAndUins = new HashMap<String, List<String>>();
        if (ticketSerialNumbers == null || ticketSerialNumbers.size() == 0) {
            return itemIdsAndUins;
        }
        List<String> uins = new ArrayList<>();
        for (SerialNumberValue uinValue : ticketSerialNumbers) {
            uins.add(uinValue.getUin());
        }
        if (uins.size() > 0) {
            itemIdsAndUins.put(itemTicket.getRetailItem().getId(), uins);
        }

        return itemIdsAndUins;
    }

    public Map<String, List<SerialNumberValue>> generateAGSNs() throws Exception {
        Map<String, List<SerialNumberValue>> itemIdsAndUinValues = ClientServiceFactory.getItemTicketServices().generateItemTicketAGSNs(itemTicket);
        return itemIdsAndUinValues;
    }

    public Map<String, List<String>> getItemTicketUinsByValue(Map<String, List<SerialNumberValue>> itemAgsns, StorePrinter printer) throws Exception {
        if (itemAgsns == null || itemAgsns.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        Map<String, List<String>> itemUins = new HashMap<>(itemAgsns.size());
        for (Map.Entry<String, List<SerialNumberValue>> entry : itemAgsns.entrySet()) {
            List<String> uins = new ArrayList<>(entry.getValue().size());
            for (SerialNumberValue serialNumber : entry.getValue()) {
                uins.add(serialNumber.getUin());
            }
            itemUins.put(entry.getKey(), uins);
        }
        return itemUins;
    }

    public boolean isTicketHasUin() {
        return ticketSerialNumbers != null && ticketSerialNumbers.size() > 0;
    }

    public RetailItem getRetailItem(ItemVO itemVO) throws Exception {
        RetailItem retailItem = retailItemCache.get(itemVO.getId());
        if (retailItem == null) {
            retailItem = ClientServiceFactory.getItemServices().readRetailItem(itemVO.getId(), getStoreId());
            retailItemCache.put(retailItem.getId(), retailItem);
        }
        return retailItem;
    }

    public void loadPricePerUom() throws Exception {
        if (itemTicket != null) {
            RetailItem retailItem = itemTicket.getRetailItem();

            SimMoney sellingPrice = null;
            if (itemTicket.getLabelPrice() != null) {
                sellingPrice = itemTicket.getLabelPrice();
            } else if (retailItem.getRetailPrice() != null) {
                sellingPrice = retailItem.getRetailPrice();
            }

            if (sellingPrice != null) {
                itemTicket.setPricePerUom(retailItem.getPricePerUOM());
                itemTicket.setStandardSellingUom(retailItem.getPackageUom());
            }
        }
    }

    public String getUINLabel() {
        return Translator.getText("UINs");
    }

    public boolean isAGSNEnabled() {
        if (itemTicket != null && itemTicket.getRetailItem() != null) {
            return itemTicket.getRetailItem().isAGSNEnabled();
        }

        return false;
    }

    public boolean isAGSNFormatType() {
        if (itemTicket != null) {
            TicketTypeFormat format = itemTicket.getTicketTypeFormat();
            if (format != null) {
                if (format.getTicketType().getId().equals(TicketTypeId.AGSN_ID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getTicktTypeDescription() {
        if (itemTicket != null && itemTicket.getTicketTypeFormat() != null) {
            return itemTicket.getTicketTypeFormat().getTicketType().getDescription();
        }
        return null;
    }

    public ItemTicketLineItemWrapper buildNewLineItemWrapper(ItemTicket itemTicket) {
        return ClientWrapperFactory.createItemTicketLineItemWrapper(itemTicket);
    }
}
