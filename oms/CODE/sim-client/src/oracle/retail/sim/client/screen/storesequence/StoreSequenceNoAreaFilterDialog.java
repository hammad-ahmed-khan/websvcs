package oracle.retail.sim.client.screen.storesequence;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.storesequence.StoreSequenceItemQueryFilter;

/********************************************************************************************************
 * This dialog handles entering the filter information for store sequencing.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceNoAreaFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 617020424872334325L;

    private StoreSequenceNoAreaFilterModel model = new StoreSequenceNoAreaFilterModel();

    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(true);
    private RTextFieldEditor descriptionEditor = new RTextFieldEditor("Item Description");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StoreSequenceNoAreaFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("No Location Filter");
        setSize(400, 160);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        descriptionEditor.setIdentifier(SimName.ITEM_DESCRIPTION);

        itemEditor.setSizeType(EditorConstants.LARGE);
        descriptionEditor.setSizeType(EditorConstants.LARGE);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setLength(3);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());

        itemEditor.setSearchListener(buildItemSearchListener());
        
        applyButton.addMouseFocusGrabber();

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel editorPanel = new REditorPanel(3);
        editorPanel.add(itemEditor);
        editorPanel.add(descriptionEditor);
        editorPanel.add(searchLimitEditor);

        setContentPane(editorPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(StoreSequenceItemQueryFilter filter) throws Exception {
        model.setFilter(filter);
        itemEditor.setText(filter.getItemId());
        descriptionEditor.setText(filter.getItemDescription());
        searchLimitEditor.setInteger(filter.getSearchLimit());
        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                }
            }
        };
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
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
     * Reset Action
     ***************************************************************************************************/
    private void doReset() throws Exception {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        StoreSequenceItemQueryFilter filter = model.getFilter();

        ItemVO itemVO = (ItemVO) itemEditor.getData();
        if (itemVO != null) {
            filter.setItemId(itemVO.getId());
        } else {
            filter.setItemId(null);
        }

        filter.setItemDescription(descriptionEditor.getTextOrNull());

        if (searchLimitEditor.isEmpty()) {
            filter.setSearchLimit(model.getDefaultSearchLimit());
        } else {
            filter.setSearchLimit(searchLimitEditor.getIntegerValue());
        }
        RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
