package oracle.retail.sim.client.screen.itemprice;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.itemprice.ItemPrice;
import oracle.retail.sim.common.itemprice.ItemPriceQueryFilter;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.ItemPriceVO;
import oracle.retail.sim.common.itemticket.TicketReason;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Price Change List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceListModel extends SimScreenModel {

    public ItemPriceQueryFilter getFilter() throws BusinessException {
        ItemPriceQueryFilter filter = (ItemPriceQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.PRICE_CHANGE_FILTER);
        if (filter == null) {
            filter = BOFactory.createItemPriceQueryFilter();
            filter.doSetStoreId(getStoreId());
            filter.doSetStatus(ItemPriceStatus.DEFAULT);
            filter.doSetSearchLimit(getDefaultSearchLimit());
            Date startDate = SimDateUtil.getCurrentDateAtStartOfDay(getTimeZone());
            Date twoWeeks = SimDateUtil.addDays(getTimeZone(), startDate, 7);
            Date endDate = SimDateUtil.getDateAtEndOfDay(getTimeZone(), twoWeeks);
            filter.setEffectiveDateRange(startDate, endDate);
            RepositoryManager.addStateObject(SimClientStateKey.PRICE_CHANGE_FILTER, filter);
        }
        return filter;
    }

    private Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_PRICE_CHANGE);
    }

    public List<ItemPriceVO> findItemPriceVOs() throws Exception {
        return ClientServiceFactory.getItemPriceServices().findItemPriceVOs(getFilter());
    }

    public void storeSelectedItemPrice(ItemPriceVO itemPriceVO) throws Exception {
        ItemPrice itemPrice = ClientServiceFactory.getItemPriceServices().readItemPrice(itemPriceVO.getId());
        if (itemPrice != null) {
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_PRICE_CHANGE, itemPrice);
        }
    }

    public void storeNewItemPrice() {
        Store store = getStore();
        ItemPrice itemPrice = BOFactory.createItemPrice(store.getId());
        itemPrice.doSetCurrencyCode(store.getCurrencyCode());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_PRICE_CHANGE, itemPrice);
    }

    public void createItemTickets(List<ItemPriceVO> itemPriceVOs) throws Exception {
        List<ItemPrice> itemPrices = convertToItemPrices(itemPriceVOs);
        ClientServiceFactory.getItemTicketServices().createPriceChangeTickets(getStoreId(), itemPrices, TicketTypeId.ITEM_TICKET_ID, TicketReason.TICKET_ITEM_PRICE);
    }

    public void createShelfLabels(List<ItemPriceVO> itemPriceVOs) throws Exception {
        List<ItemPrice> itemPrices = convertToItemPrices(itemPriceVOs);
        ClientServiceFactory.getItemTicketServices().createPriceChangeTickets(getStoreId(), itemPrices, TicketTypeId.SHELF_LABEL_ID, TicketReason.TICKET_ITEM_PRICE);
    }

    public Map<String, String> getDescriptionMap() throws BusinessException {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        ItemPriceQueryFilter filter = getFilter();
        DateFormat dateFormatter = LocaleManager.getShortDateFormatter();
        if (filter.getFromEffectiveDate() != null) {
            descriptionMap.put("Effective Date From", dateFormatter.format(filter.getFromEffectiveDate()));
        }
        if (filter.getToEffectiveDate() != null) {
            descriptionMap.put("Effective Date To", dateFormatter.format(filter.getToEffectiveDate()));
        }
        if (filter.getFromEndDate() != null) {
            descriptionMap.put("End Date From", dateFormatter.format(filter.getFromEndDate()));
        }
        if (filter.getToEndDate() != null) {
            descriptionMap.put("End Date To", dateFormatter.format(filter.getToEndDate()));
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
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
        }
        if (filter.getPromotionId() != null) {
            descriptionMap.put("Promotion ID", String.valueOf(filter.getPromotionId()));
        }
        if (filter.getPriceType() != null) {
            descriptionMap.put("Price Change Desc", filter.getPriceType().toString());
        }
        if (filter.getSearchLimit() != null) {
            descriptionMap.put("Search Limit", String.valueOf(filter.getSearchLimit()));
        }
        return descriptionMap;
    }

    private List<ItemPrice> convertToItemPrices(List<ItemPriceVO> itemPriceVOs) throws Exception {
        Store store = getStore();

        List<ItemPrice> itemPrices = new ArrayList<ItemPrice>();
        List<String> itemIds = new ArrayList<String>();

        if (itemPriceVOs != null && itemPriceVOs.size() > 0) {
            for (ItemPriceVO itemPriceVO : itemPriceVOs) {
                itemIds.add(itemPriceVO.getItemId());
            }

            Map<String, RetailItem> retailItemMap = ClientServiceFactory.getItemServices().readRetailItems(itemIds, store.getId());
            for (ItemPriceVO itemPriceVO : itemPriceVOs) {

                ItemPrice itemPrice = BOFactory.createItemPrice(itemPriceVO.getStoreId());

                //retailItem contains the current price
                RetailItem retailItem = retailItemMap.get(itemPriceVO.getItemId());
                itemPrice.doSetRetailItem(retailItem);

                //get non-current retail price information for the price id
                itemPrice.doSetId(itemPriceVO.getId());
                itemPrice.doSetStatus(itemPriceVO.getStatus());
                itemPrice.doSetEffectiveDate(itemPriceVO.getEffectiveDate());
                itemPrice.doSetEndDate(itemPriceVO.getEndDate());
                itemPrice.doSetPriceType(itemPriceVO.getPriceType());
                itemPrice.doSetPrice(itemPriceVO.getPrice());

                itemPrice.doSetPriceUom(itemPriceVO.getSellingUOM());
                itemPrice.doSetMultiUnitUom(itemPriceVO.getMultiUnitUOM());
                if (itemPriceVO.isMultiUnitPriceChange()) {
                    itemPrice.doSetMultiPriceChange(true);
                    itemPrice.doSetMultiUnitPrice(itemPriceVO.getMultiUnitPrice());
                }
                itemPrice.doSetMultiUnits(itemPriceVO.getMultiUnits());
                if (itemPriceVO.getRegularPriceChangeId() != null) {
                    itemPrice.doSetRegularPriceChangeId(itemPriceVO.getRegularPriceChangeId());
                }
                if (itemPriceVO.getClearanceId() != null) {
                    itemPrice.doSetClearanceId(itemPriceVO.getClearanceId());
                }
                if (itemPriceVO.getPromotionId() != null) {
                    itemPrice.doSetPromotionId(Long.valueOf(itemPriceVO.getPromotionId()));
                    itemPrice.doSetPromotionName(itemPriceVO.getPromotionName());
                    itemPrice.doSetPromoDurationType(itemPriceVO.getPromotionDurationType());
                }

                itemPrices.add(itemPrice);
            }
        }
        return itemPrices;
    }

}
