package oracle.retail.sim.client.screen.itemprice;

import java.awt.GridBagLayout;
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
import oracle.retail.sim.client.swing.editor.RDateRangeEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.itemprice.ItemPriceQueryFilter;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.PriceType;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

/********************************************************************************************************
 * This dialog handles entering the filter information for price changes.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -555731259281516480L;

    private ItemPriceFilterDialogModel model = new ItemPriceFilterDialogModel();

    private RDateRangeEditor effectiveDateEditor = new RDateRangeEditor("Effective Date");
    private RDateRangeEditor endDateEditor = new RDateRangeEditor("End Date");
    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(true);
    private RComboBoxEditor priceStatusEditor = new RComboBoxEditor("Status");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");
    private RIntegerFieldEditor promotionEditor = new RIntegerFieldEditor("Promotion ID");
    private RComboBoxEditor priceTypeEditor = new RComboBoxEditor("Price Change Desc");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private static final String STATUS_MODIFIED = "Status.modified";
    private static final String LIMIT_MODIFIED = "Limist.modified";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ItemPriceFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Price Change Filter");
        setSize(450, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        searchLimitEditor.setIdentifier(SimName.ITEM_PRICE_SEARCH_LIMIT);
        promotionEditor.setIdentifier(SimName.PROMOTION_ID);
        hierarchyPanel.setTitleBorder("Hierarchy Filters");
        hierarchyPanel.setEmptyDescriptionToAll();
        priceTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        priceStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        priceStatusEditor.registerAction(this, STATUS_MODIFIED);
        searchLimitEditor.registerAction(this, LIMIT_MODIFIED);
        promotionEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        itemEditor.setSearchListener(buildItemSearchListener());
        priceTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        priceStatusEditor.setEmptyType(RComboBoxEmptyType.ALL);

        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);

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
        datePanel.add(effectiveDateEditor);
        datePanel.add(endDateEditor);

        REditorPanel filterPanel = new REditorPanel(5);
        filterPanel.setTitleBorder("Additional Filters");
        filterPanel.add(itemEditor);
        filterPanel.add(priceStatusEditor);
        filterPanel.add(promotionEditor);
        filterPanel.add(priceTypeEditor);
        filterPanel.add(searchLimitEditor);

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

    public void setFilter(ItemPriceQueryFilter filter) throws Exception {
        model.setFilter(filter);

        effectiveDateEditor.setStartDate(filter.getFromEffectiveDate());
        effectiveDateEditor.setEndDate(filter.getToEffectiveDate());
        endDateEditor.setStartDate(filter.getFromEndDate());
        endDateEditor.setEndDate(filter.getToEndDate());
        itemEditor.setText(filter.getItemId());
        searchLimitEditor.setInteger(filter.getSearchLimit());
        promotionEditor.setInteger(filter.getPromotionId());
        priceStatusEditor.setItems(model.findItemPriceStatus());
        priceStatusEditor.setSelectedItem(filter.getStatus());
        priceTypeEditor.setItems(model.findPriceDescriptions());
        priceTypeEditor.setSelectedItem(filter.getPriceType());

        hierarchyPanel.loadDepartments();
        hierarchyPanel.setHierarchyNode(filter.getDepartmentId(), filter.getClassId(), filter.getSubclassId());

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
        ItemPriceQueryFilter filter = model.getFilter();

        filter.setStoreId(model.getStoreId());
        filter.setEffectiveDateRange(effectiveDateEditor.getStartDate(), effectiveDateEditor.getEndDate());
        filter.setEndDateRange(endDateEditor.getStartDate(), endDateEditor.getEndDate());

        if (priceStatusEditor.getSelectedItem() == null) {
            filter.setStatus(ItemPriceStatus.DEFAULT);
        } else {
            filter.setStatus((ItemPriceStatus) priceStatusEditor.getSelectedItem());
        }

        PriceType priceType = (PriceType) priceTypeEditor.getSelectedItem();
        if (priceType != null) {
            filter.setPriceType(priceType);
        }
        filter.setDepartmentId(null);
        filter.setClassId(null);
        filter.setSubclassId(null);
        filter.setItemId(null);
        filter.setPromotionId(null);

        ItemVO localItem = (ItemVO) itemEditor.getData();
        if (localItem != null) {
            filter.setItemId(localItem.getId());
        }
        Integer searchLimit = searchLimitEditor.getIntegerOrNull();
        if (searchLimit != null) {
            filter.setSearchLimit(searchLimit);
        }
        Integer promotionId = promotionEditor.getIntegerOrNull();
        if (promotionId != null) {
            filter.setPromotionId(promotionId.longValue());
        }

        MdseHierarchyNode node = hierarchyPanel.getHierarchyNode();
        if (node != null) {
            filter.setDepartmentId(node.getDepartmentId());
            filter.setClassId(node.getClassId());
            filter.setSubclassId(node.getSubclassId());
        }
        RepositoryManager.addStateObject(SimClientStateKey.PRICE_CHANGE_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.PRICE_CHANGE_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
