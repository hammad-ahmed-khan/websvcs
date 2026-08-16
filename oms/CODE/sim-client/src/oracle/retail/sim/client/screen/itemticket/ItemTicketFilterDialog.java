package oracle.retail.sim.client.screen.itemticket;

import java.awt.GridBagLayout;
import java.math.BigInteger;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemHierarchyPanel;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.editor.RLongFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.itemticket.ItemTicketQueryFilter;
import oracle.retail.sim.common.itemticket.ItemTicketStatus;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

/********************************************************************************************************
 * This dialog handles entering the filter information for item tickets.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -7574894904743322975L;

    private ItemTicketFilterDialogModel model = new ItemTicketFilterDialogModel();

    private RDateFieldEditor startDateEditor = new RDateFieldEditor("Effective From Date");
    private RDateFieldEditor finalDateEditor = new RDateFieldEditor("Effective To Date");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(true);
    private RTextFieldEditor externalPOIdEditor = new RTextFieldEditor("PO Number");
    private RLongFieldEditor promotionEditor = new RLongFieldEditor("Promotion ID");
    private RComboBoxEditor formatTypeEditor = new RComboBoxEditor("Format Name");
    private RComboBoxEditor labelTypeEditor = new RComboBoxEditor("Label Type");
    private RComboBoxEditor ticketStatusEditor = new RComboBoxEditor("Status");
    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ItemTicketFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Item Tickets Filter");
        setSize(450, 430);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        externalPOIdEditor.setIdentifier(SimName.PURCHASE_ORDER_EXTERNAL_ID);
        promotionEditor.setIdentifier(SimName.PROMOTION_ID);
        hierarchyPanel.setTitleBorder("Hierarchy Filters");
        hierarchyPanel.setEmptyDescriptionToAll();
        labelTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        formatTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        ticketStatusEditor.setDisplayer(new TranslatedObjectDisplayer());

        labelTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        formatTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        ticketStatusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        itemEditor.setSearchListener(buildItemSearchListener());
        startDateEditor.setSizeType(EditorConstants.LARGE);
        finalDateEditor.setSizeType(EditorConstants.LARGE);
        RDateFieldRangeUtility.setDateRangeEditors(startDateEditor, finalDateEditor);

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
        datePanel.add(startDateEditor);
        datePanel.add(finalDateEditor);

        REditorPanel filterPanel = new REditorPanel(6);
        filterPanel.setTitleBorder("Additional Filters");
        filterPanel.add(itemEditor);
        filterPanel.add(externalPOIdEditor);
        filterPanel.add(labelTypeEditor);
        filterPanel.add(formatTypeEditor);
        filterPanel.add(promotionEditor);
        filterPanel.add(ticketStatusEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(datePanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(hierarchyPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(filterPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignPanels(hierarchyPanel, filterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(ItemTicketQueryFilter filter) throws Exception {
        model.setFilter(filter);
        labelTypeEditor.setItems(model.getTicketLabelTypes());

        formatTypeEditor.setItems(model.getItemTicketLabelFormats());
        ticketStatusEditor.setItems(model.getTicketStatus());

        startDateEditor.setDate(filter.getFromEffectiveDate());
        finalDateEditor.setDate(filter.getToEffectiveDate());
        itemEditor.setText(filter.getItemId());
        externalPOIdEditor.setText(filter.getExternalPoId());

        labelTypeEditor.setSelectedItem(filter.getLabelType());
        formatTypeEditor.setSelectedItem(model.getTicketTypeFormat(filter.getTicketTypeFormatId()));
        ticketStatusEditor.setSelectedItem(filter.getStatus());

        hierarchyPanel.loadDepartments();
        hierarchyPanel.setHierarchyNode(filter.getDepartmentId(), filter.getClassId(), filter.getSubclassId());

        BigInteger promotionId = filter.getPromotionId();
        if (promotionId != null) {
            promotionEditor.setLong(promotionId.longValue());
        } else {
            promotionEditor.clear();
        }
        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
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
        ItemTicketQueryFilter filter = model.getFilter();

        filter.setEffectiveDateRange(startDateEditor.getDateAtStartOfDay(), finalDateEditor.getDateAtEndOfDay());
        filter.setLabelType((TicketType) labelTypeEditor.getSelectedItem());

        filter.setStatus((ItemTicketStatus) ticketStatusEditor.getSelectedItem());

        TicketTypeFormat ticketTypeFormat = (TicketTypeFormat) formatTypeEditor.getSelectedItem();
        if (ticketTypeFormat != null) {
            filter.setTicketTypeFormatId(ticketTypeFormat.getId());

        } else {
            filter.setTicketTypeFormatId(null);
        }

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        MdseHierarchyNode node = hierarchyPanel.getHierarchyNode();
        if (node != null) {
            filter.setDepartmentId(node.getDepartmentId());
            filter.setClassId(node.getClassId());
            filter.setSubclassId(node.getSubclassId());
        } else {
            filter.setDepartmentId(null);
            filter.setClassId(null);
            filter.setSubclassId(null);
        }

        if (promotionEditor.getLongValue() > 0) {
            filter.setPromotionId(new BigInteger(promotionEditor.getText()));
        } else {
            filter.setPromotionId(null);
        }

        String externalPOId = externalPOIdEditor.getTextOrNull();
        if (externalPOId != null) {
            model.validateAndAssignExternalPoId(externalPOId);
        }

        RepositoryManager.addStateObject(SimClientStateKey.ITEM_TICKET_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.ITEM_TICKET_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
