package oracle.retail.sim.client.editor;

import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for stock items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StockItemSearchProcessor extends BaseItemSearchProcessor {
    private AttributeDisplayer entryDisplayer = new AttributeDisplayer("id");
    private MessageText rangeItemConfirmMessage;

    public StockItemSearchProcessor() {
        this(ItemMessageText.ITEM_ADD_NON_RANGED);
    }

    public StockItemSearchProcessor(MessageText rangeItemConfirmMessage) {
        this.rangeItemConfirmMessage = rangeItemConfirmMessage;
    }

    public BasicDisplayer getEntryDisplayer() {
        return entryDisplayer;
    }

    public Object searchById(String identifier) throws Exception {
        List<StockItem> stockItems = ClientServiceFactory.getItemServices().findStockItems(identifier, SimRepository.getStoreId());
        if (stockItems.isEmpty()) {
            throw new BusinessException(ItemMessageText.ITEM_NOT_FOUND);
        }
        if (stockItems.size() == 1) {
            return stockItems.get(0);
        }
        ItemSelectDialog dialog = new ItemSelectDialog();
        dialog.setStockItems(stockItems);
        dialog.setVisible(true);
        return dialog.getSelectedItem();
    }

    /**
     * If ItemVO, convert to stock item and then process
     */
    public Object validateData(Object data) {
        try {
            if (data instanceof ItemVO) {
                data = searchById(((ItemVO) data).getId());
            }
            if (data instanceof StockItem) {
                StockItem stockItem = (StockItem) data;
                if (stockItem.getItemType() == ItemType.CONSIGNMENT) {
                    throw new BusinessException(CommonMessageText.CONSIGNMENT_ITEM_ERROR);
                }
                if (stockItem.getItemType() == ItemType.CONCESSION) {
                    throw new BusinessException(CommonMessageText.CONCESSION_ITEM_ERROR);
                }
                if (stockItem.getItemType() == ItemType.NON_INVENTORY) {
                    throw new BusinessException(CommonMessageText.NON_INVENTORY_ITEM_ERROR);
                }
                if (stockItem.isRanged()) {
                    return stockItem;
                }
                if (!isNonRangedAllowed()) {
                    displayError(ItemMessageText.ITEM_NOT_RANGED_ERROR);
                    return null;
                }
                if (RConfirmUtility.confirm("Non-Ranged Item Confirmation", rangeItemConfirmMessage)) {
                    return ClientServiceFactory.getItemServices().readStockItemOrCreate(stockItem.getId(), SimRepository.getStoreId());
                }
            }
        } catch (Throwable exception) {
            displayError(exception);
        }
        return null;
    }
}
