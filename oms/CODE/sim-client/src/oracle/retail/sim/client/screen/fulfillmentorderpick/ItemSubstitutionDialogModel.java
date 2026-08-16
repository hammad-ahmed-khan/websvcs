package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.item.ItemDiffVO;
import oracle.retail.sim.common.item.ItemUtility;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.common.item.RelatedItemType;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Substitution Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ItemSubstitutionDialogModel extends SimScreenModel {

    private FulfillmentOrderPickLineItemWrapper wrapper;

    /**
     * Set the original line item to the input CustomerOrderPickLineItemWrapper.
     * @param wrapper A CustomerOrderPickLineItemWrapper representing the line item being substituted for.
     */
    public void setWrapper(FulfillmentOrderPickLineItemWrapper wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * Returns the CustomerOrderPickLineItemWrapper which represents the original Pick Line Item being substituted for.
     * @return A CustomerOrderPickLineItemWrapper representing the original Pick Line Item.
     */
    public FulfillmentOrderPickLineItemWrapper getWrapper() {
        return wrapper;
    }

    /**
     * Returns whether the Item Substitution Dialog is in a view only state.
     * @return True if the dialog is in a view only state, otherwise false;
     */
    public boolean isViewOnly() {
        if (!hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_PICK)) {
            return true;
        }
        return wrapper.getPick().getStatus() == FulfillmentOrderPickStatus.COMPLETED || wrapper.getPick().getStatus() == FulfillmentOrderPickStatus.CANCELED;
    }

    /**
     * Loads possible substitutes as well as current substitutes for the current line item.
     * @return A List of ItemSubstitutionWrappers representing either available items for substitution or items already substituted.
     */
    public List<ItemSubstitutionWrapper> loadSubstitutes() throws Exception {
        List<ItemSubstitutionWrapper> wrappers = new ArrayList<ItemSubstitutionWrapper>();

        ItemDiffVO itemDiffVO = ClientServiceFactory.getItemServices().readItemDiffVO(wrapper.getItemId());
        RelatedItem originalItem = ItemUtility.convertStockItem(wrapper.getStockItem(), itemDiffVO);

        ItemSubstitutionWrapper originalWrapper = ClientWrapperFactory.createItemSubstitutionWrapper(wrapper.getLineItem(), originalItem, wrapper.getPreferredUomConversionFactor());
        originalWrapper.setActualQuantity(wrapper.getQuantityOrZero());
        wrappers.add(originalWrapper);

        //Get related items
        List<RelatedItem> relatedItems = ClientServiceFactory.getItemServices().findRelatedItems(null, wrapper.getItemId(), getStoreId());
        for (RelatedItem relatedItem : relatedItems) {

            //Only care about substitutes
            if (relatedItem.isSubtitute()) {
                ItemSubstitutionWrapper wrapper = ClientWrapperFactory.createItemSubstitutionWrapper(this.wrapper.getLineItem(), relatedItem, this.wrapper.getPreferredUomConversionFactor());

                //Check to see if an existing line item is this substitute
                for (FulfillmentOrderPickLineItem lineItem : this.wrapper.getPick().getLineItems()) {
                    if (lineItem.isSubstitute() && lineItem.getSubstituteLineItemId().equals(this.wrapper.getLineItem().getId()) && lineItem.getStockItem().getId().equals(wrapper.getItemId())) {
                        wrapper.setActualQuantity(lineItem.getQuantityOrZero());
                        wrapper.setCaseSize(lineItem.getCaseSize());
                        break;
                    }
                }

                //Skip any items that have no available SOH and weren't already added
                if (!relatedItem.getAvailableStockOnHand().isPositive() && wrapper.getActualQuantity().isZero()) {
                    continue;
                }

                wrappers.add(wrapper);
            }
        }

        //Add current substituted items that were not included in the 'get related items'.
        for (FulfillmentOrderPickLineItem lineItem : wrapper.getPick().getLineItems()) {

            //Skip any original item or substitute for other line items
            if (!lineItem.isSubstitute() || !lineItem.getSubstituteLineItemId().equals(wrapper.getLineItem().getId())) {
                continue;
            }
            boolean substitutePresent = false;
            for (ItemSubstitutionWrapper wrapper : wrappers) {
                if (wrapper.getItemId().equals(lineItem.getStockItem().getId())) {
                    substitutePresent = true;
                    break;
                }
            }

            //If the item is no longer a valid sub, but was when it was added, we have to manually create it.
            //This should rarely occur.
            if (!substitutePresent) {
                ItemDiffVO newItemDiffVO = ClientServiceFactory.getItemServices().readItemDiffVO(wrapper.getItemId());
                RelatedItem relatedItem = ItemUtility.convertStockItem(lineItem.getStockItem(), newItemDiffVO, RelatedItemType.SUBSTITUTE);

                ItemSubstitutionWrapper missingSubWrapper = ClientWrapperFactory.createItemSubstitutionWrapper(wrapper.getLineItem(), relatedItem, wrapper.getPreferredUomConversionFactor());
                missingSubWrapper.setActualQuantity(lineItem.getQuantityOrZero());
                wrappers.add(missingSubWrapper);
            }
        }

        return wrappers;
    }

    /**
     * Returns a new ItemSubstitutionWrapper for the input StockItem.
     * @param stockItem The StockItem for which to create an ItemSubstitutionWrapper.
     * @return A new ItemSubstitutionWrapper for the input StockItem.
     */
    public ItemSubstitutionWrapper getItemSubstitutionWrapper(StockItem stockItem) throws Exception {
        ItemDiffVO itemDiffVO = ClientServiceFactory.getItemServices().readItemDiffVO(wrapper.getItemId());
        RelatedItem relatedItem = ItemUtility.convertStockItem(stockItem, itemDiffVO, RelatedItemType.SUBSTITUTE);
        return ClientWrapperFactory.createItemSubstitutionWrapper(wrapper.getLineItem(), relatedItem, wrapper.getPreferredUomConversionFactor());
    }

    /**
     * Checks that the input ItemSubstitutionWrapper represents an item with the same UOM as the original Pick item's.
     * @param wrapper The substitute item whose UOM to check for consistency with the original.
     */
    public void verifyUom(ItemSubstitutionWrapper wrapper) throws Exception {
        if (!wrapper.getRelatedItem().getUnitOfMeasure().equals(this.wrapper.getStockItem().getUnitOfMeasure())) {
            throw new BusinessException(FulfillmentOrderMessageText.SUBSTITUTION_UOM_MISMATCH, this.wrapper.getStockItem().getUnitOfMeasure());
        }
    }

    /**
     * Add substitutes for the current line item with quantities to the respective FulfillmentOrderPick.
     * @param wrappers A List of ItemSubstitutionWrappers representing substitutes for the current line item. Only substitutes with
     * quantities will be added to the FulfillmentOrderPick. Any substitutes that had quantities but now have a quantity of zero
     * will be removed from the FulfillmentOrderPick.
     */
    public void addApplySubstituteItems(List<ItemSubstitutionWrapper> wrappers) throws Exception {
        FulfillmentOrderPick pick = wrapper.getPick();

        Quantity quantity = Quantity.ZERO;

        //Get the quantity for the original item and substitutes
        for (ItemSubstitutionWrapper subWrapper : wrappers) {
            quantity = quantity.add(subWrapper.getActualQuantity());
        }

        //If the Original Item is an Each and the user enters more than the suggested quantity
        if (wrapper.getStockItem().isEachesUom()) {
            if (quantity.subtract(wrapper.getSuggestedQuantity()).isPositive()) {
                throw new BusinessException(FulfillmentOrderMessageText.CUSTOMER_ORDER_QUANTITY_EXCEEDED);
            }
        } else {
            //Otherwise, check the tolerances for going over
            if (wrapper.getToleranceAdmin().getVarianceCount() != null) {
                if (quantity.subtract(wrapper.getSuggestedQuantity().add(wrapper.getToleranceAdmin().getVarianceCount())).isPositive()) {
                    throw new BusinessException(FulfillmentOrderMessageText.TOLERANCE_VALUES_EXCEEDED);
                }
            }
            if (wrapper.getToleranceAdmin().getVariancePercent() != null) {
                if (quantity.subtract(wrapper.getSuggestedQuantity().add(wrapper.getSuggestedQuantity().multiply(wrapper.getToleranceAdmin().getVariancePercent().movePointLeft(2)))).isPositive()) {
                    throw new BusinessException(FulfillmentOrderMessageText.TOLERANCE_VALUES_EXCEEDED);
                }
            }
        }

        Map<String, StockItem> stockItemMap = new HashMap<String, StockItem>();
        List<String> itemIds = new ArrayList<String>();
        for (ItemSubstitutionWrapper subWrapper : wrappers) {
            if (!itemIds.contains(subWrapper.getItemId())) {
                itemIds.add(subWrapper.getItemId());
            }
        }

        stockItemMap = ClientServiceFactory.getItemServices().readStockItems(itemIds, getStoreId());

        List<FulfillmentOrderPickLineItem> itemsToRemove = new ArrayList<FulfillmentOrderPickLineItem>();

        //Add all substitutes with quantities to the pick, and delete if necessary all those without
        for (ItemSubstitutionWrapper subWrapper : wrappers) {
            if (subWrapper.getActualQuantity().isPositive()) {
                boolean itemExists = false;
                for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
                    if (lineItem.isSubstitute() && lineItem.getStockItem().getId().equals(subWrapper.getItemId()) && lineItem.getSubstituteLineItemId().equals(wrapper.getLineItem().getId())) {
                        itemExists = true;
                        //If the quantity substituted has changed
                        if (!subWrapper.getActualQuantity().subtract(lineItem.getQuantityOrZero()).isZero()) {
                            lineItem.setQuantity(subWrapper.getActualQuantity());
                        }
                        //If the case size has changed
                        if (!subWrapper.getCaseSize().subtract(lineItem.getCaseSize()).isZero()) {
                            lineItem.setCaseSize(subWrapper.getCaseSize());
                        }
                    } else if (!lineItem.isSubstitute() && subWrapper.getItemId().equals(lineItem.getStockItem().getId()) && lineItem.getId().equals(wrapper.getLineItem().getId())) {
                        itemExists = true;
                        if (!subWrapper.getActualQuantity().subtract(lineItem.getQuantityOrZero()).isZero()) {
                            lineItem.setQuantity(subWrapper.getActualQuantity());
                        }
                        if (!subWrapper.getCaseSize().subtract(lineItem.getCaseSize()).isZero()) {
                            lineItem.setCaseSize(subWrapper.getCaseSize());
                        }
                    }
                }
                if (!itemExists) {
                    FulfillmentOrderPickLineItem lineItem = BOFactory.createFulfillmentOrderPickLineItem(stockItemMap.get(subWrapper.getItemId()));
                    lineItem.doSetBin(wrapper.getLineItem().getBin());
                    lineItem.doSetCaseSize(subWrapper.getCaseSize());
                    lineItem.doSetFulfillmentOrderId(wrapper.getSimCustomerOrderId());
                    lineItem.doSetPickId(pick.getId());
                    lineItem.doSetQuantity(subWrapper.getActualQuantity());
                    lineItem.doSetSubstituteLineItemId(wrapper.getLineItem().getId());
                    lineItem.doSetPreferredUom(wrapper.getLineItem().getPreferredUom());
                    pick.addLineItem(lineItem);
                    pick.doSetDirty();
                }

            } else {
                for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
                    if (lineItem.isSubstitute() && lineItem.getStockItem().getId().equals(subWrapper.getItemId()) && lineItem.getSubstituteLineItemId().equals(wrapper.getLineItem().getId())) {
                        itemsToRemove.add(lineItem);
                    } else if (!lineItem.isSubstitute() && subWrapper.getItemId().equals(lineItem.getStockItem().getId()) && lineItem.getId().equals(wrapper.getLineItem().getId())) {
                        lineItem.setQuantity(subWrapper.getActualQuantity());
                        lineItem.setCaseSize(subWrapper.getCaseSize());
                    }
                }
            }
        }

        for (FulfillmentOrderPickLineItem lineItem : itemsToRemove) {
            pick.removeLineItem(lineItem);
        }
    }

    /**
     * Returns true if substitution selection is not restricted to those defined by RMS.
     * @return True if substitution selection is not restricted to those defined by RMS, otherwise false.
     */
    public boolean isStoreDiscretionSubstitution() {
        return getStoreBoolean(StoreConfigKeys.ITEM_SUBSTITUTION_STORE_DISCRETION);
    }

}
