package oracle.retail.sim.client.uom;

import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComponent;
import oracle.retail.sim.client.core.SimFatalManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.tableeditor.SimSearchTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * A table editor for choosing a order item from a search style component
 * regardless of whether or not the system config settings allow non ranged
 * item. Displays only the ID of the item in the field.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class OrderItemTableEditor extends SimSearchTableEditor implements SimTableEditor {
    private static final long serialVersionUID = -8048967421564041367L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private boolean isNullable = true;
    private boolean isStoreOrder = false;
    protected String lastCheckedId = "";

    public OrderItemTableEditor() {
        this(true);
    }

    public OrderItemTableEditor(boolean nullable) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new AttributeDisplayer("id"));
        setIdentifier(SimName.ITEM_ID);
        isNullable = nullable;
    }

    public Class getValueClass() {
        return OrderItem.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public Object getValue() {
        if (isErrorState()) {
            return null;
        }
        return getSearchData();
    }

    public void setData(Object value) {
        if (value instanceof OrderItem) {
            setSearchData(value);
            checkValue();
            eventAdaptor.fireTypeEditorEvent();
            return;
        }
        if (value == null && isNullable) {
            setSearchData(null);
            return;
        }
        throw new IllegalArgumentException("OrderItemTableEditor only edits OrderItem!");
    }

    public void setValue(Object value) {
        if (value instanceof String) {
            getTextField().setText((String) value);
            checkValue();
        } else {
            setSearchData(value);
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    public JComponent getComponent() {
        return this;
    }

    /****************************************************************************************************
     * Validates the stockable information in the table editor. It will find the
     * stock item and verify its isRanged value. If non-ranged are allowed, then
     * a window will ask the user if they would like to range the item.
     ***************************************************************************************************/
    public boolean checkValue() {
        OrderItem orderItem = (OrderItem) getSearchData();
        String identifier = getText();

        setErrorState(false);

        if (StringUtility.isNullOrEmpty(identifier)) {
            return checkEmptyItemId(orderItem);
        }

        try {
            // Check If Value Has Not Been Modified
            if (orderItem != null && identifier.equals(orderItem.getId()) && identifier.equals(lastCheckedId)) {
                return true;
            }
            if (!StringUtility.isNullOrEmpty(identifier)) {
                List<OrderItem> orderItems = ClientServiceFactory.getItemServices().findOrderItem(identifier, SimRepository.getStoreId(), true);
                if (orderItems.size() > 0) {
                    orderItem = determineItem(orderItems);
                }
            }
            if (orderItem == null) {
                displayErrorWithReset(ItemMessageText.ITEM_NOT_FOUND);
                return false;
            }
            if (orderItem.getItemType() == ItemType.CONSIGNMENT) {
                throw new UIException(CommonMessageText.CONSIGNMENT_ITEM_ERROR);
            }
            if (orderItem.getItemType() == ItemType.CONCESSION) {
                throw new UIException(CommonMessageText.CONCESSION_ITEM_ERROR);
            }
            if (orderItem.getItemType() == ItemType.NON_INVENTORY) {
                throw new UIException(CommonMessageText.NON_INVENTORY_ITEM_ERROR);
            }
            if (isStoreOrder && !orderItem.isOrderable() && orderItem.isOrderDateValidationRequired()) {
                throw new UIException(CommonMessageText.NON_ORDERABLE_ITEM_ERROR);
            }
            if (!orderItem.isRanged() || orderItem.getStatus() == ItemStatus.AUTO_STOCKABLE) {
                displayErrorWithClear(ItemMessageText.ITEM_NOT_RANGED_ERROR);
                return false;
            }
            setSearchData(orderItem);
            lastCheckedId = orderItem.getId();
            return true;
        } catch (UIException uiException) {
            displayError(uiException);
        } catch (BusinessException ruleException) {
            displayError(ruleException);
        } catch (Throwable exception) {
            SimFatalManager.resetApplication(getClass(), exception);
        }
        lastCheckedId = StringConstants.EMPTY;
        setSearchData(null);
        return false;
    }

    /****************************************************************************************************
     * Determine Item
     ***************************************************************************************************/
    private OrderItem determineItem(List<OrderItem> items) {
        if (items.size() == 1) {
            return items.get(0);
        }
        ItemSelectDialog dialog = new ItemSelectDialog();
        dialog.setOrderItems(items);
        dialog.setVisible(true);
        return (OrderItem) dialog.getSelectedItem();
    }

    /****************************************************************************************************
     * Validates empty identifier id and clear out the information. If nullable
     * is not allowed, it display an error.
     ***************************************************************************************************/
    protected boolean checkEmptyItemId(OrderItem orderItem) {
        if (isNullable) {
            setSearchData(null);
            return true;
        }
        if (orderItem == null) {
            return false;
        }
        displayErrorWithReset(ItemMessageText.ITEM_BLANK_ERROR);
        return true;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    /****************************************************************************************************
     * Id Value IS Modified
     ***************************************************************************************************/
    protected boolean doValueModified() {
        if (checkValue()) {
            eventAdaptor.fireTypeEditorEvent();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for input into the field.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }

    /****************************************************************************************************
     * Determines if the orderItem belongs to storeOrder
     ***************************************************************************************************/
    public boolean isStoreOrder() {
        return isStoreOrder;
    }

    public void setStoreOrder(boolean isStoreOrder) {
        this.isStoreOrder = isStoreOrder;
    }
}
