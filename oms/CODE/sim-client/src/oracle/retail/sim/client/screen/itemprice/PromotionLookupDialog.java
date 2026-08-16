package oracle.retail.sim.client.screen.itemprice;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.itemprice.PromotionQueryFilter;
import oracle.retail.sim.common.itemprice.PromotionVO;

/********************************************************************************************************
 * Promotion Lookup Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PromotionLookupDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -8377834951830198040L;

    private PromotionLookupDialogModel model = new PromotionLookupDialogModel();

    private RTextFieldEditor promotionIdEditor = new RTextFieldEditor("Promotion ID");
    private RTextFieldEditor promotionNameEditor = new RTextFieldEditor("Promotion Name");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private SimTable promotionTable = new SimTable(new PromotionTableDefinition());
    private SimTablePane promotionPane = new SimTablePane(promotionTable);

    private RButton searchButton = new RButton(SimNavigation.DIALOG_SEARCH);
    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private SearchListener searchListener;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public PromotionLookupDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Promotion Lookup");
        setSize(750, 550);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        promotionIdEditor.setIdentifier(SimName.PROMOTION_ID);
        promotionNameEditor.setIdentifier(SimName.PROMOTION_NAME);

        searchLimitEditor.setIdentifier(SimName.PROMOTION_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());

        promotionTable.setTableEditable(false);
        promotionTable.setSingleRowSelectionMode();

        searchButton.registerAction(this, SimNavigation.DIALOG_SEARCH);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(searchButton);
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel editorPanel = new REditorPanel(1, 3);
        editorPanel.add(promotionIdEditor);
        editorPanel.add(promotionNameEditor);
        editorPanel.add(searchLimitEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(editorPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(promotionPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Helper method to establish the search listener
     ***************************************************************************************************/

    public void setSearchListener(SearchListener listener) {
        searchListener = listener;
    }
    
    public void stopEditing() {
        promotionTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_SEARCH)) {
                doSearch();
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
     * SEARCH
     ***************************************************************************************************/

    private void doSearch() throws Exception {
        PromotionQueryFilter filter = BOFactory.createPromotionQueryFilter();
        filter.setStoreId(model.getStoreId());
        filter.setPromotionName(promotionNameEditor.getTextOrNull());
        filter.setSearchLimit(searchLimitEditor.getInteger());

        String promotionId = promotionIdEditor.getTextOrNull();
        if (promotionId != null) {
            if (!NumberHelper.isIdentifierNumeric(promotionId)) {
                throw new BusinessException(CommonMessageText.VALUE_NOT_VALID);
            }
            filter.setPromotionId(Long.valueOf(promotionId));
        }

        promotionTable.setRows(model.findPromotions(filter));

        if (promotionTable.isEmpty()) {
            displayWarning(CommonMessageText.NO_RECORDS_FOUND);

            if (promotionNameEditor.isEmpty()) {
                promotionIdEditor.requestFocusInWindow();
            } else {
                promotionNameEditor.requestFocusInWindow();
            }
        }
    }

    /****************************************************************************************************
     * RESET
     ***************************************************************************************************/

    private void doReset() {
        promotionTable.clearRows();
        promotionIdEditor.clear();
        promotionNameEditor.clear();
        searchLimitEditor.setInteger(99);
        promotionIdEditor.requestFocusInWindow();
    }

    /****************************************************************************************************
     * APPLY
     ***************************************************************************************************/

    private void doApply() throws Exception {
        PromotionVO promotionVO = (PromotionVO) promotionTable.getSelectedRowData();
        if (promotionVO == null) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        searchListener.assign(promotionVO);
        closeWindow();
    }

    /****************************************************************************************************
     * CANCEL
     ***************************************************************************************************/

    private void doCancel() {
        closeWindow();
    }

    /****************************************************************************************************
     * PROMOTION TABLE DEFINITION
     ***************************************************************************************************/

    private class PromotionTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return PromotionVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> columns = new ArrayList<SimTableAttribute>(2);
            columns.add(new SimTableAttribute("Promotion ID", "id"));
            columns.add(new SimTableAttribute("Promotion Name", "name"));
            return columns;
        }
    }
}
