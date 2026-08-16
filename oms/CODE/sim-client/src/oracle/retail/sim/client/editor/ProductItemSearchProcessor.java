package oracle.retail.sim.client.editor;

import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ProductItemSearchProcessor extends BaseItemSearchProcessor {
    public Object searchById(String text) throws Exception {
        List<ProductGroupItem> productGroupItems = ClientServiceFactory.getItemServices().findProductGroupItems(text, SimRepository.getStoreId());
        if (productGroupItems.isEmpty()) {
            throw new BusinessException(ItemMessageText.ITEM_NOT_FOUND);
        }
        ProductGroupItem productGroupItem = determineItem(productGroupItems);
        if (productGroupItem.isRanged() || isNonRangedAllowed()) {
            return productGroupItem;
        }
        throw new BusinessException(ItemMessageText.ITEM_NOT_RANGED_ERROR);
    }

    private ProductGroupItem determineItem(List<ProductGroupItem> items) {
        if (items.size() == 1) {
            return items.get(0);
        }
        ItemSelectDialog dialog = new ItemSelectDialog();
        dialog.setProductGroupItems(items);
        dialog.setVisible(true);
        return (ProductGroupItem) dialog.getSelectedItem();
    }

    public Object validateData(Object data) {
        ProductGroupItem productGroupItem = null;
        try {
            if (data instanceof ItemVO) {
                ItemVO itemVO = (ItemVO) data;
                productGroupItem = ClientServiceFactory.getItemServices().readProductGroupItem(itemVO.getId(), SimRepository.getStoreId());
            } else if (data instanceof ProductGroupItem) {
                productGroupItem = (ProductGroupItem) data;
            }
            if (productGroupItem == null) {
                return null;
            }
            if (productGroupItem.getItemType() == ItemType.CONSIGNMENT) {
                throw new BusinessException(CommonMessageText.CONSIGNMENT_ITEM_ERROR);
            }
            if (productGroupItem.getItemType() == ItemType.CONCESSION) {
                throw new BusinessException(CommonMessageText.CONCESSION_ITEM_ERROR);
            }
            if (productGroupItem.getItemType() == ItemType.NON_INVENTORY) {
                throw new BusinessException(CommonMessageText.NON_INVENTORY_ITEM_ERROR);
            }
            if (productGroupItem.isRanged()) {
                return productGroupItem;
            }
            if (!SimConfigManager.getBoolean(SimConfigManager.ALLOW_NON_RANGE_ITEM)) {
                displayError(ItemMessageText.ITEM_NOT_RANGED_ERROR);
                return null;
            }
            if (RConfirmUtility.confirm("Non-Ranged Item Confirmation", ItemMessageText.ITEM_ADD_NON_RANGED)) {
                productGroupItem = ClientServiceFactory.getItemServices().readProductGroupItemOrCreate(productGroupItem.getId(), SimRepository.getStoreId());
                if (productGroupItem != null) {
                    return productGroupItem;
                }
            }
        } catch (Throwable exception) {
            displayError(exception);
        }
        return null;
    }
}
