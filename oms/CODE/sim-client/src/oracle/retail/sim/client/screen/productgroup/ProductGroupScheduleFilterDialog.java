package oracle.retail.sim.client.screen.productgroup;

import java.awt.GridBagLayout;
import java.util.Date;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ScheduleStatus;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * This dialog handles entering the filter information for product group schedules.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 8605363272667141678L;

    private ProductGroupScheduleFilterDialogModel model = new ProductGroupScheduleFilterDialogModel();

    private RDateFieldEditor nextDateEditor = new RDateFieldEditor("Next Schedule Date");
    private RDateFieldEditor finalDateEditor = new RDateFieldEditor("Final Schedule Date");
    private RComboBoxEditor groupTypeEditor = new RComboBoxEditor("Type");
    private RLongTextFieldEditor descriptionEditor = new RLongTextFieldEditor("Description");
    private RComboBoxEditor storeEditor = SimEditorFactory.createStoreComboEditor("Store");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private static final String TYPE_SELECTED = "Type.selected";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/

    public ProductGroupScheduleFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Product Group Schedule Filter");
        setSize(450, 300);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        nextDateEditor.setSizeType(EditorConstants.MEDIUM);
        finalDateEditor.setSizeType(EditorConstants.MEDIUM);
        groupTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        groupTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        groupTypeEditor.registerAction(this, TYPE_SELECTED);
        descriptionEditor.setIdentifier(SimName.PRODUCT_GROUP_SCHEDULE_DESCRIPTION);
        descriptionEditor.setEnabled(true);
        storeEditor.setDisplayer(new StoreDisplayer());
        storeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel datePanel = new REditorPanel(2);
        datePanel.setTitleBorder("Date Filter");
        datePanel.add(nextDateEditor);
        datePanel.add(finalDateEditor);

        REditorPanel otherPanel = new REditorPanel(5);
        otherPanel.setTitleBorder("Additional Filters");
        otherPanel.add(groupTypeEditor);
        otherPanel.add(descriptionEditor);
        otherPanel.add(storeEditor);
        otherPanel.add(statusEditor);

        LayoutUtility.alignPanels(datePanel, otherPanel);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(datePanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(otherPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(ProductGroupScheduleQueryFilter filter) throws Exception {
        model.setFilter(filter);

        Date nextDate = filter.getNextDate();
        if (nextDate != null) {
            nextDateEditor.setDate(SimDateUtil.convertDateFromUTC(model.getTimeZone(), nextDate));
        } else {
            nextDateEditor.clear();
        }
        Date lastDate = filter.getLastDate();
        if (lastDate != null) {
            finalDateEditor.setDate(SimDateUtil.convertDateFromUTC(model.getTimeZone(), lastDate));
        } else {
            finalDateEditor.clear();
        }

        groupTypeEditor.setItems(model.getProductGroupTypes());
        groupTypeEditor.setSelectedItem(filter.getGroupType());
        storeEditor.setItems(model.getAllowedStores());
        storeEditor.setSelectedItem(model.getStore(filter.getStoreId()));
        descriptionEditor.setText(filter.getDescription());
        statusEditor.setItems(model.getScheduleStatuses());
        statusEditor.setSelectedItem(filter.getStatus());

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(TYPE_SELECTED)) {
                doGroupTypeSelected();
            } else if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Group Type Selected
     ***************************************************************************************************/

    private void doGroupTypeSelected() {
    }

    /****************************************************************************************************
     * Reset Action
     ***************************************************************************************************/

    private void doReset() throws Exception {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Search Action
     ***************************************************************************************************/

    private void doApply() throws Exception {
        ProductGroupScheduleQueryFilter filter = model.getFilter();
        Date nextDate = nextDateEditor.getDate();
        Date lastDate = finalDateEditor.getDate();
        filter.setNextDate(nextDate, model.getTimeZone());
        filter.setLastDate(lastDate, model.getTimeZone());

        if (!SimDateUtil.isValidDateRange(filter.getNextDate(), filter.getLastDate())) {
            throw new BusinessException(CommonMessageText.DATE_RANGE_ERROR);
        }
        filter.setGroupType((ProductGroupType) groupTypeEditor.getSelectedItem());
        filter.setDescription(descriptionEditor.getTextOrNull());

        Store store = (Store) storeEditor.getSelectedItem();
        if (store != null) {
            filter.setStoreId(store.getId());
            filter.setAuthorizedStoreIds(null);
        } else {
            filter.setStoreId(null);
            filter.setAuthorizedStoreIds(model.getAllowedStoreIds());
        }
        filter.setStatus((ScheduleStatus) statusEditor.getSelectedItem());

        RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.PRODUCT_GROUP_SCHEDULE_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/

    private void doCancel() {
        closeWindow();
    }
}
