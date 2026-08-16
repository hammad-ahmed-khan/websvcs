package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemHierarchyPanel;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.productgroup.StockCountGroupVO;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountQueryFilter;

/********************************************************************************************************
 * This dialog handles entering the filter information for stock counts..
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 2620595763393588714L;

    private StockCountFilterDialogModel model = new StockCountFilterDialogModel();

    private RDateFieldEditor scheduleDateEditor = SimEditorFactory.createDateEditor("Schedule Date");
    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();
    private RComboBoxEditor productGroupEditor = new RComboBoxEditor("Count Group");
    private RComboBoxEditor countPhaseEditor = new RComboBoxEditor("Type");
    private RComboBoxEditor countStatusEditor = new RComboBoxEditor("Status");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Stock Count Filter");
        setSize(425, 365);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        scheduleDateEditor.setSizeType(EditorConstants.LARGE);
        productGroupEditor.setSizeType(EditorConstants.LARGE);
        countPhaseEditor.setSizeType(EditorConstants.LARGE);
        countStatusEditor.setSizeType(EditorConstants.LARGE);

        countPhaseEditor.setDisplayer(new TranslatedObjectDisplayer());
        countStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        productGroupEditor.setDisplayer(new DualAttributeDisplayer("id", "description"));
        productGroupEditor.setEmptyType(RComboBoxEmptyType.ALL);
        countPhaseEditor.setEmptyType(RComboBoxEmptyType.ALL);
        countStatusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        hierarchyPanel.setEmptyDescriptionToAll();

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel dateFilterPanel = new REditorPanel(1);
        dateFilterPanel.setTitleBorder("Date Filters");
        dateFilterPanel.add(scheduleDateEditor);

        hierarchyPanel.setTitleBorder("Hierarchy Filters");

        REditorPanel miscFilterPanel = new REditorPanel(3);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(productGroupEditor);
        miscFilterPanel.add(countPhaseEditor);
        miscFilterPanel.add(countStatusEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(3, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(hierarchyPanel);
        mainPanel.add(miscFilterPanel);

        List<REditorPanel> panels = new ArrayList<>();
        panels.add(dateFilterPanel);
        panels.add(hierarchyPanel);
        panels.add(miscFilterPanel);

        LayoutUtility.alignPanels(panels);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(StockCountQueryFilter filter) throws Exception {
        model.setFilter(filter);

        setActionsEnabled(false);

        countPhaseEditor.setItems(model.findAllStockCountPhases());
        countPhaseEditor.setSelectedItem(filter.getPhase());

        countStatusEditor.setItems(model.findStockCountStatus());
        countStatusEditor.setSelectedItem(filter.getDisplayStatus());

        productGroupEditor.setItems(model.findStockCountGroups());
        productGroupEditor.setSelectedItem(model.findStockCountGroup(filter.getProductGroupId()));

        hierarchyPanel.loadDepartments();
        hierarchyPanel.setHierarchyNode(filter.getDepartmentId(), filter.getClassId(), filter.getSubclassId());

        Date date = filter.getScheduleDate();
        if (date != null) {
            scheduleDateEditor.setDate(date);
        } else {
            scheduleDateEditor.clear();
        }
        setActionsEnabled(true);

        setDefaultButton(applyButton);
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
        StockCountQueryFilter filter = model.getFilter();
        filter.setProductGroupId(null);
        filter.setDepartmentId(null);
        filter.setClassId(null);
        filter.setSubclassId(null);
        filter.setScheduleDate(null);
        filter.setPhase((StockCountPhase) countPhaseEditor.getSelectedItem());
        filter.setDisplayStatus((StockCountDisplayStatus) countStatusEditor.getSelectedItem());

        StockCountGroupVO groupVO = (StockCountGroupVO) productGroupEditor.getSelectedItem();
        if (groupVO != null) {
            filter.setProductGroupId(groupVO.getId());
        }

        MdseHierarchyNode node = hierarchyPanel.getHierarchyNode();
        if (node != null) {
            filter.setDepartmentId(node.getDepartmentId());
            filter.setClassId(node.getClassId());
            filter.setSubclassId(node.getSubclassId());
        }

        Date date = scheduleDateEditor.getGMTDateAtNoon();
        if (date != null) {
            filter.setScheduleDate(date);
        }

        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.STOCK_COUNT_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
