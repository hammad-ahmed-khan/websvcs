package oracle.retail.sim.client.screen.itemrequest;

import java.awt.GridBagLayout;
import java.util.Date;
import javax.swing.JLabel;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateRangeEditor;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.itemrequest.ItemRequestQueryFilter;
import oracle.retail.sim.common.itemrequest.ItemRequestStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/*******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
 * This dialog handles entering the filter information for item requests.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

public class ItemRequestFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -1891695314944477466L;

    private ItemRequestFilterDialogModel model = new ItemRequestFilterDialogModel();

    private RDateRangeEditor deliveryDateEditor = new RDateRangeEditor("Request Delivery");
    private RDateRangeEditor expirationDateEditor = new RDateRangeEditor("Expiration");

    private RNumericIdEditor requestEditor = new RNumericIdEditor("Request ID", "Request");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RComboBoxEditor timeSlotEditor = new RComboBoxEditor("Delivery Timeslot");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor userEditor = new RComboBoxEditor("User");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private static final String DELIVERY_DATE_MODIFIED = "DELIVERY_DATE_MODIFIED";
    private static final String ITEM_ID_MODIFIED = "ITEM_ID_MODIFIED";

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Build Dialog
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    public ItemRequestFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Item Request Filter");
        setSize(450, 300);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        deliveryDateEditor.registerAction(this, DELIVERY_DATE_MODIFIED);

        requestEditor.setIdentifier(SimName.ITEM_REQUEST_ID);
        requestEditor.setSizeType(EditorConstants.MEDIUM);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        itemEditor.setSearchListener(buildItemSearchListener());
        itemEditor.registerAction(this, ITEM_ID_MODIFIED);

        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        userEditor.setEmptyType(RComboBoxEmptyType.ALL);

        timeSlotEditor.setVisible(model.displayItemRequestDeliveryTimeSlot());
        timeSlotEditor.setDisplayer(new AttributeDisplayer("description"));
        timeSlotEditor.setEmptyType(RComboBoxEmptyType.ALL);
        timeSlotEditor.setSortEnabled(false);
        timeSlotEditor.setEnabled(false);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel datePanel = new REditorPanel(2);
        datePanel.setTitleBorder("Date Filters");
        datePanel.add(deliveryDateEditor);
        datePanel.add(expirationDateEditor);

        REditorPanel filterPanel = new REditorPanel(5);
        filterPanel.setTitleBorder("Additional Filters");
        filterPanel.add(requestEditor);
        filterPanel.add(itemEditor);
        filterPanel.add(timeSlotEditor);
        filterPanel.add(statusEditor);
        filterPanel.add(userEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(datePanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(filterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 1, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Assign Filter To Dialog
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public void setFilter(ItemRequestQueryFilter filter) throws Exception {
        model.setFilter(filter);
        userEditor.setItems(model.findEmployees());
        statusEditor.setItems(model.findItemRequestStatus());
        deliveryDateEditor.setStartDate(filter.getFromRequestDate());
        deliveryDateEditor.setEndDate(filter.getToRequestDate());
        expirationDateEditor.setStartDate(filter.getFromExpirationDate());
        expirationDateEditor.setEndDate(filter.getToExpirationDate());
        requestEditor.setLong(filter.getItemRequestId());
        itemEditor.setText(filter.getItemId());
        userEditor.setSelectedItem(filter.getUserId());
        statusEditor.setSelectedItem(filter.getStatus());
        enableTimeSlotEditorBasedOnExistingValues();
        timeSlotEditor.setSelectedItem(filter.getDeliveryTimeSlot());
        setDefaultButton(applyButton);
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                }
            }
        };
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Handle Actions
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            } else if (command.equals(DELIVERY_DATE_MODIFIED)) {
                doDeliveryDateModified();
            } else if (command.equals(ITEM_ID_MODIFIED)) {
                doItemIdModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Delivery Date Change Action
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    private void doDeliveryDateModified() throws Exception {
        timeSlotEditor.clear();
        timeSlotEditor.setEnabled(false);
        Object value = itemEditor.getData();
        if (value instanceof ItemVO) {
            if (!ClientServiceFactory.getItemServices().isMultipleDeliveryAllowed(((ItemVO) value).getId(), model.getStoreId())) {
                return;
            }
        }

        if (SimDateUtil.isSameDay(SimRepository.getStore().getTimeZone(), deliveryDateEditor.getStartDate(), deliveryDateEditor.getEndDate())) {
            timeSlotEditor.setItems(model.findDeliveryTimeSlots());
            timeSlotEditor.setEnabled(true);
        }
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Item Id Change Action
     *
     * This is highly inefficient!!!! StockItem needs to be refactored. See StockItem.
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    private void doItemIdModified() throws Exception {
        Object value = itemEditor.getData();
        if (value instanceof ItemVO) {
            if (ClientServiceFactory.getItemServices().isMultipleDeliveryAllowed(((ItemVO) value).getId(), model.getStoreId())) {
                Date startDate = deliveryDateEditor.getStartDate();
                Date endDate = deliveryDateEditor.getStartDate();
                if (SimDateUtil.isSameDay(SimRepository.getStore().getTimeZone(), startDate, endDate)) {
                    timeSlotEditor.setItems(model.findDeliveryTimeSlots());
                    timeSlotEditor.setEnabled(true);
                    return;
                }
            }
        }
        timeSlotEditor.clear();
        timeSlotEditor.setEnabled(false);
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Enable Time slot editor when the filter is pressed again
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    private void enableTimeSlotEditorBasedOnExistingValues() throws Exception {
        Date startDate = deliveryDateEditor.getStartDate();
        Date endDate = deliveryDateEditor.getStartDate();

        if (itemEditor.getData() == null && startDate != null && endDate != null && timeSlotEditor.isVisible()) {
            if (SimDateUtil.isSameDay(SimRepository.getStore().getTimeZone(), startDate, endDate)) {
                timeSlotEditor.setItems(model.findDeliveryTimeSlots());
                timeSlotEditor.setEnabled(true);
            }
        }
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Reset Action
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    private void doReset() throws Exception {
        setFilter(model.resetFilter());
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Apply Action
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    private void doApply() throws Exception {
        ItemRequestQueryFilter filter = model.getFilter();
        filter.setFromRequestDate(deliveryDateEditor.getStartDate());
        filter.setToRequestDate(deliveryDateEditor.getEndDate());
        filter.setFromExpirationDate(expirationDateEditor.getStartDate());
        filter.setToExpirationDate(expirationDateEditor.getEndDate());
        filter.setItemRequestId(requestEditor.getLongOrNull());
        filter.setStatus((ItemRequestStatus) statusEditor.getSelectedItem());
        filter.setUserId((String) userEditor.getSelectedItem());

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        DeliveryTimeSlot deliveryTimeslot = (DeliveryTimeSlot) timeSlotEditor.getSelectedItem();
        if (deliveryTimeslot != null) {
            filter.setDeliveryTimeSlot(deliveryTimeslot);
        } else {
            filter.setDeliveryTimeSlot(null);
        }

        RepositoryManager.addStateObject(SimClientStateKey.ITEM_REQUEST_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.ITEM_REQUEST_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Cancel Action
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
