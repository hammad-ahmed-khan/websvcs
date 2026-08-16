package oracle.retail.sim.client.screen.itemprice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.currency.SimMoneyCalculator;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.itemprice.FuturePriceVO;
import oracle.retail.sim.common.itemprice.ItemPrice;
import oracle.retail.sim.common.itemprice.ItemPriceMessageText;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.PriceChange;
import oracle.retail.sim.common.itemprice.PriceType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Price Change Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceDetailModel extends SimScreenModel {
    private ItemPrice itemPrice;

    public void loadItemPrice() {
        itemPrice = (ItemPrice) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_PRICE_CHANGE);
    }

    public ItemPrice getItemPrice() {
        return itemPrice;
    }

    public boolean obtainItemPriceLock() throws Exception {
        if (isItemPricePendingOrTicketList()) {
            if (itemPrice.getExternalId() != null) {
                return obtainLock(ActivityLockType.PRICE_CHANGE, itemPrice.getExternalId().toString());
            }
        }
        return true;
    }

    public boolean isViewOnly() {
        return itemPrice != null && !itemPrice.isEditable();
    }

    public List<PriceType> findPriceDescriptions() {
        return SimEnumUtility.findAllPriceTypes();
    }

    public void clearItemPrice() throws Exception {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_PRICE_CHANGE);

        if (isItemPricePendingOrTicketList() && itemPrice.getExternalId() != null) {
            releaseLock(ActivityLockType.PRICE_CHANGE, itemPrice.getExternalId());
        }
    }

    private boolean isItemPricePendingOrTicketList() {
        if (itemPrice != null) {
            ItemPriceStatus status = itemPrice.getStatus();
            return status.equals(ItemPriceStatus.PENDING) || status.equals(ItemPriceStatus.TICKET_LIST);
        }
        return false;
    }

    public FuturePriceVO getFuturePrice(Date effectiveDate) throws Exception {
        FuturePriceVO futurePrice = null;
        if (!SimConfigManager.isRslEnabled()) {
            throw new BusinessException(ItemPriceMessageText.SERVICE_UNAVAILABLE);
        }

        if (effectiveDate == null) {
            throw new BusinessException(ItemPriceMessageText.START_DATE_INVALID);
        }
        itemPrice.setEffectiveDate(effectiveDate, getTimeZone());

        // check if the price change in question has an item associated with it yet
        RetailItem retailItem = itemPrice.getRetailItem();
        if (retailItem == null) {
            return null;
        }
        futurePrice = ClientServiceFactory.getItemPriceServices().futurePriceInquryRPM(itemPrice.getStoreId(), retailItem.getId(), itemPrice.getEffectiveDate());

        return futurePrice;
    }

    public void requestItemPrice() throws BusinessException, Exception {
        PriceChange priceChange = null;

        priceChange = ClientServiceFactory.getItemPriceServices().requestNewItemPrice(itemPrice);

        if (priceChange != null) {
            List<PriceChange> priceChanges = new ArrayList<PriceChange>();
            priceChange.doSetCurrencyCode(itemPrice.getCurrencyCode());
            priceChanges.add(priceChange);

            itemPrice.setPrice(new SimMoney(priceChange.getPrice(), Currency.getInstance(itemPrice.getCurrencyCode())));
            itemPrice.doSetRegularPriceChangeId(priceChange.getRegularPriceChangeId());
            itemPrice.doSetClearanceId(priceChange.getClearanceId());
            itemPrice.doSetPromotionId(priceChange.getPromotionId());
            itemPrice.doSetPromotionCompId(priceChange.getPromoCompId());
            itemPrice.doSetPromoCompDetailId(priceChange.getPromoCompDetailId());

            if (priceChange.getPriceUom() != null) {
                itemPrice.setPriceUom(priceChange.getNewPriceUom());
            }
            ClientServiceFactory.getItemPriceServices().savePriceChanges(itemPrice.getPriceType(), priceChanges);

        }
        RepositoryManager.addStateObject(SimClientStateKey.PRICE_CHANGE_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public Currency getDefaultCurrency() {
        return Currency.getInstance(getStore().getCurrencyCode());
    }

    public boolean validateNewPrice(SimMoney newPrice) throws Exception {
        if (newPrice == null) {
            return false;
        }
        RetailItem retailItem = itemPrice.getRetailItem();
        if (retailItem == null) {
            return false;
        }
        List<SupplierItem> supplierItems = ClientServiceFactory.getItemServices().findSupplierItems(retailItem.getId());
        if (supplierItems.isEmpty()) {
            return true;
        }
        SimMoney lowestUnitCost = new SimMoney(BigDecimal.ZERO, newPrice.getCurrency());
        boolean lowestUnitCostAssigned = false;
        for (SupplierItem supplierItem : supplierItems) {
            SimMoney unitCost = supplierItem.getUnitCost();
            if (unitCost.getCurrencyCode().equals(lowestUnitCost.getCurrencyCode())) {
                if (!lowestUnitCostAssigned) {
                    lowestUnitCost = unitCost;
                    lowestUnitCostAssigned = true;
                }
                if (SimMoneyCalculator.lessThan(unitCost, lowestUnitCost)) {
                    lowestUnitCost = supplierItem.getUnitCost();
                }
            }
        }

        return !SimMoneyCalculator.lessThan(newPrice, SimMoneyCalculator.round(lowestUnitCost));
    }
}
