package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.stockcount.StockCount;

/********************************************************************************************************
 * This dialog handles displaying the product group detail information for a stock count.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountDetailDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -7844091308987520908L;

    private RDisplayLabelEditor groupTypeEditor = new RDisplayLabelEditor("Type");
    private RDisplayLabelEditor groupDescEditor = new RDisplayLabelEditor("Group Description");
    private RDisplayLabelEditor varianceUomEditor = new RDisplayLabelEditor("Variance SUOM");
    private RDisplayLabelEditor varianceValueEditor = new RDisplayLabelEditor("Variance Value");
    private RDisplayLabelEditor variancePercentEditor = new RDisplayLabelEditor("Variance %");
    private RDisplayLabelEditor hierarchyEditor = new RDisplayLabelEditor("Hierarchy Breakdown");
    private RDisplayLabelEditor countMethodEditor = new RDisplayLabelEditor("Counting Method");
    private RCheckBoxEditor recountEditor = new RCheckBoxEditor("Re-Count Discrepancies");
    private RCheckBoxEditor autoAuthEditor = new RCheckBoxEditor("Auto Authorize");
    private RCheckBoxEditor statusActiveEditor = new RCheckBoxEditor("Active");
    private RCheckBoxEditor statusInactiveEditor = new RCheckBoxEditor("Inactive");
    private RCheckBoxEditor statusDiscEditor = new RCheckBoxEditor("Discontinued");
    private RCheckBoxEditor statusDeletedEditor = new RCheckBoxEditor("Deleted");
    private RCheckBoxEditor sohZeroEditor = new RCheckBoxEditor("SOH = 0");
    private RCheckBoxEditor sohGreaterEditor = new RCheckBoxEditor("SOH > 0");
    private RCheckBoxEditor sohLesserEditor = new RCheckBoxEditor("SOH < 0");
    private RCheckBoxEditor actualPickEditor = new RCheckBoxEditor("Actual Pick Amount less than Suggested Pick Amount");
    private RCheckBoxEditor replenPickEditor = new RCheckBoxEditor("Actual Shelf Repl Amount less than Suggested Shelf Repl Amount");
    private RCheckBoxEditor negativePickEditor = new RCheckBoxEditor("Negative Available Inventory");
    private RCheckBoxEditor uinDiscrepantEditor = new RCheckBoxEditor("UIN Discrepancies");

    private TranslatedObjectDisplayer objectDisplayer = new TranslatedObjectDisplayer();

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountDetailDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Product Group Detail");
        setSize(650, 390);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        groupTypeEditor.setEnabled(false);
        groupDescEditor.setEnabled(false);
        varianceUomEditor.setDataType(DataTypeConstants.INTEGER_LEFT);
        variancePercentEditor.setDataType(DataTypeConstants.DECIMAL_LEFT);
        varianceValueEditor.setDataType(DataTypeConstants.INTEGER_LEFT);
        hierarchyEditor.setEnabled(false);
        countMethodEditor.setEnabled(false);
        recountEditor.setEnabled(true, false);
        autoAuthEditor.setEnabled(true, false);
        statusActiveEditor.setEnabled(true, false);
        statusInactiveEditor.setEnabled(true, false);
        statusDiscEditor.setEnabled(true, false);
        statusDeletedEditor.setEnabled(true, false);
        sohZeroEditor.setEnabled(true, false);
        sohGreaterEditor.setEnabled(true, false);
        sohLesserEditor.setEnabled(true, false);
        actualPickEditor.setEnabled(true, false);
        replenPickEditor.setEnabled(true, false);
        negativePickEditor.setEnabled(true, false);
        uinDiscrepantEditor.setEnabled(true, false);

        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);

        setDefaultButton(closeButton);
    }

    private void layoutContent() {
        addButton(closeButton);

        REditorPanel headerPanel = new REditorPanel(1, 2);
        headerPanel.setTitleBorder("Product Group Detail");
        headerPanel.add(groupTypeEditor);
        headerPanel.add(groupDescEditor);

        REditorPanel variancePanel = new REditorPanel(3);
        variancePanel.setTitleBorder("Variance");
        variancePanel.add(varianceUomEditor);
        variancePanel.add(variancePercentEditor);
        variancePanel.add(varianceValueEditor);

        REditorPanel countMethodPanel = new REditorPanel(4);
        countMethodPanel.setTitleBorder("Counting Method");
        countMethodPanel.add(countMethodEditor);
        countMethodPanel.add(hierarchyEditor);
        countMethodPanel.add(recountEditor);
        countMethodPanel.add(autoAuthEditor);

        REditorPanel itemStatusPanel = new REditorPanel(4);
        itemStatusPanel.setTitleBorder("Item Status");
        itemStatusPanel.add(statusActiveEditor);
        itemStatusPanel.add(statusInactiveEditor);
        itemStatusPanel.add(statusDiscEditor);
        itemStatusPanel.add(statusDeletedEditor);

        REditorPanel stockOnHandPanel = new REditorPanel(3);
        stockOnHandPanel.setTitleBorder("Stock On Hand");
        stockOnHandPanel.add(sohZeroEditor);
        stockOnHandPanel.add(sohGreaterEditor);
        stockOnHandPanel.add(sohLesserEditor);

        REditorPanel problemLinePanel = new REditorPanel(4);
        problemLinePanel.setTitleBorder("Problem Line");
        problemLinePanel.add(actualPickEditor);
        problemLinePanel.add(replenPickEditor);
        problemLinePanel.add(negativePickEditor);
        problemLinePanel.add(uinDiscrepantEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 3, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(variancePanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(countMethodPanel, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(problemLinePanel, GridTool.constraints(0, 2, 2, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(itemStatusPanel, GridTool.constraints(2, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(stockOnHandPanel, GridTool.constraints(2, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Set Stock Count
     ***************************************************************************************************/
    public void setStockCount(StockCount stockCount) {
        groupTypeEditor.setData(objectDisplayer.getDisplayText(stockCount.getType()));
        groupDescEditor.setData(stockCount.getGroupDescription());
        varianceUomEditor.setData(stockCount.getVarianceCount());
        variancePercentEditor.setData(stockCount.getVariancePercent());
        varianceValueEditor.setData(stockCount.getVarianceValue());
        hierarchyEditor.setData(objectDisplayer.getDisplayText(stockCount.getBreakdownType()));
        countMethodEditor.setData(objectDisplayer.getDisplayText(stockCount.getCountingMethod()));
        recountEditor.setSelected(stockCount.isRecountRequired());
        autoAuthEditor.setSelected(stockCount.isAutoAuthorize());
        statusActiveEditor.setSelected(stockCount.includeActiveItems());
        statusInactiveEditor.setSelected(stockCount.includeInactiveItems());
        statusDiscEditor.setSelected(stockCount.includeDiscontinuedItems());
        statusDeletedEditor.setSelected(stockCount.includeDeletedItems());
        sohZeroEditor.setSelected(stockCount.includeSOHZero());
        sohGreaterEditor.setSelected(stockCount.includeSOHGreaterThanZero());
        sohLesserEditor.setSelected(stockCount.includeSOHLessThanZero());
        actualPickEditor.setSelected(stockCount.isProblemLinePickLessSuggested());
        replenPickEditor.setSelected(stockCount.isProblemLineReplLessSuggested());
        negativePickEditor.setSelected(stockCount.isProblemLineReplNegativeAvailable());
        uinDiscrepantEditor.setSelected(stockCount.isProblemLineReplDiscrepantUin());
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(SimNavigation.DIALOG_CLOSE)) {
            closeWindow();
        }
    }

}
