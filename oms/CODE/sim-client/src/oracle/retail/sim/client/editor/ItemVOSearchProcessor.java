package oracle.retail.sim.client.editor;

import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for item value objects.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ItemVOSearchProcessor extends BaseItemSearchProcessor {
    private boolean allowNonInventoryItems;

    public ItemVOSearchProcessor(boolean allowNonInventoryItems) {
        this.allowNonInventoryItems = allowNonInventoryItems;
    }

    public Object searchById(String identifier) throws Exception {
        List<ItemVO> itemVOs = ClientServiceFactory.getItemServices().findItemVOs(identifier, SimRepository.getStoreId());
        if (itemVOs.isEmpty()) {
            throw new BusinessException(ItemMessageText.ITEM_NOT_FOUND);
        }
        ItemVO item = determineItemVO(itemVOs);
        if (item.isRanged() || isNonRangedAllowed()) {
            return item;
        }
        throw new BusinessException(ItemMessageText.ITEM_NOT_RANGED_ERROR);
    }

    private ItemVO determineItemVO(List<ItemVO> itemVOs) {
        if (itemVOs.size() == 1) {
            return itemVOs.get(0);
        }
        ItemSelectDialog dialog = new ItemSelectDialog();
        dialog.setItemVOs(itemVOs);
        dialog.setVisible(true);
        return (ItemVO) dialog.getSelectedItem();
    }

    public Object validateData(Object data) throws Exception {
        if (data instanceof ItemVO) {
            ItemVO item = (ItemVO) data;
            ItemType itemType = item.getItemType();
            if (!allowNonInventoryItems) {
                if (itemType == ItemType.CONSIGNMENT) {
                    throw new BusinessException(CommonMessageText.CONSIGNMENT_ITEM_ERROR);
                }
                if (itemType == ItemType.CONCESSION) {
                    throw new BusinessException(CommonMessageText.CONCESSION_ITEM_ERROR);
                }
                if (itemType == ItemType.NON_INVENTORY) {
                    throw new BusinessException(CommonMessageText.NON_INVENTORY_ITEM_ERROR);
                }
            }
            if (item.isRanged() || isNonRangedAllowed()) {
                return item;
            }
            displayError(ItemMessageText.ITEM_NOT_RANGED_ERROR);
        }
        return null;
    }
}
