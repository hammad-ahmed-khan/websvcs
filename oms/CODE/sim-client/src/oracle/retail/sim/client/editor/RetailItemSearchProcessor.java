package oracle.retail.sim.client.editor;

import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for return item objects.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class RetailItemSearchProcessor extends BaseItemSearchProcessor {
    private boolean isItemPrice;

    public RetailItemSearchProcessor(boolean isItemPrice) {
        this.isItemPrice = isItemPrice;
    }

    public Object searchById(String text) throws Exception {
        List<RetailItem> retailItems = ClientServiceFactory.getItemServices().findRetailItems(text, SimRepository.getStoreId());
        if (retailItems.isEmpty()) {
            throw new BusinessException(ItemMessageText.ITEM_NOT_FOUND);
        }
        RetailItem retailItem = determineItem(retailItems);
        if (isItemPrice) {
            if (!retailItem.isRanged() || ItemStatus.AUTO_STOCKABLE.equals(retailItem.getItemStatus())) {
                throw new BusinessException(CommonMessageText.ITEM_PRICE_CHANGE_NOT_ALLOWED);
            }
        }
        if (retailItem.isRanged() || isNonRangedAllowed()) {
            return retailItem;
        }
        throw new BusinessException(ItemMessageText.ITEM_NOT_RANGED_ERROR);
    }

    private RetailItem determineItem(List<RetailItem> items) {
        if (items.size() == 1) {
            return items.get(0);
        }
        ItemSelectDialog dialog = new ItemSelectDialog();
        dialog.setRetailItems(items);
        dialog.setVisible(true);
        return (RetailItem) dialog.getSelectedItem();
    }

    /**
     * If ItemVO, convert to Retail item and then perform logic
     */
    public Object validateData(Object data) {
        try {
            if (data instanceof ItemVO) {
                data = searchById(((ItemVO) data).getId());
            }
            if (data instanceof RetailItem) {
                RetailItem item = (RetailItem) data;
                if (item.isRanged()) {
                    return item;
                }
                if (isItemPrice) {
                    if (!item.isRanged() || ItemStatus.AUTO_STOCKABLE.equals(item.getItemStatus())) {
                        displayError(CommonMessageText.ITEM_PRICE_CHANGE_NOT_ALLOWED);
                        return null;
                    }
                }
                if (!SimConfigManager.getBoolean(SimConfigManager.ALLOW_NON_RANGE_ITEM)) {
                    displayError(ItemMessageText.ITEM_NOT_RANGED_ERROR);
                    return null;
                }
                if (RConfirmUtility.confirm("Non-Ranged Item Confirmation", ItemMessageText.ITEM_ADD_NON_RANGED)) {
                    item = ClientServiceFactory.getItemServices().readRetailItemOrCreate(item.getId(), SimRepository.getStoreId());
                    if (item != null) {
                        return item;
                    }
                }
            }
        } catch (Throwable exception) {
            displayError(exception);
        }
        return null;
    }
}
