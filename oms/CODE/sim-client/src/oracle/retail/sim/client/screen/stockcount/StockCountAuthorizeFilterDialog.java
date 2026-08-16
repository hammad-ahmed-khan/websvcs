package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JLabel;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.screen.item.ItemHierarchyPanel;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RPercentFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

/********************************************************************************************************
 * This dialog handles entering the filter information for stock counts authorization.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 2644594178782238755L;

    private StockCountAuthorizeFilterModel model = new StockCountAuthorizeFilterModel();

    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();
    private RIntegerFieldEditor varianceUomEditor = new RIntegerFieldEditor("Variance SUOM");
    private RPercentFieldEditor variancePercentEditor = new RPercentFieldEditor("Variance %");

    private RLabel specialLabel = new RLabel("Item Count Quantity Varies (+/-) to SOH:");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountAuthorizeFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Authorization Filter");
        setSize(425, 280);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        hierarchyPanel.setEmptyDescriptionToAll();

        specialLabel.setHorizontalAlignment(JLabel.LEFT);

        varianceUomEditor.setIdentifier(SimName.PRODUCT_GROUP_VARIANCE_UOM);
        variancePercentEditor.setIdentifier(SimName.PRODUCT_GROUP_VARIANCE_PERCENT);

        varianceUomEditor.setSizeType(EditorConstants.SMALL);
        variancePercentEditor.setSizeType(EditorConstants.SMALL);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        hierarchyPanel.setTitleBorder("Hierarchy Filters");

        RPanel variancePanel = new RPanel(new GridBagLayout());
        variancePanel.setTitleBorder("Variance Filters");
        variancePanel.add(specialLabel, GridTool.constraints(0, 0, 2, 1, 1, 0, 2, 3, 0, 0, 5, 0));
        variancePanel.add(varianceUomEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        variancePanel.add(variancePercentEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(hierarchyPanel);
        mainPanel.add(variancePanel);

        LayoutUtility.alignEditorsInGridBag(variancePanel);

        setContentPane(mainPanel);
    }

    public void setDepartments(List<Long> departmentIds) throws Exception {
        if (departmentIds.isEmpty()) {
            hierarchyPanel.loadDepartments();
        } else {
            hierarchyPanel.loadDepartments(departmentIds);
        }
    }

    public void setFilter(AuthorizeQueryFilter filter) throws Exception {
        model.setAuthorizeQueryFilter(filter);

        MdseHierarchyNode node = filter.getHierarchyNode();
        if (node != null) {
            hierarchyPanel.setHierarchyNode(node.getDepartmentId(), node.getClassId(), node.getSubclassId());
        } else {
            hierarchyPanel.clearSelection();
        }
        varianceUomEditor.setInteger(filter.getVarianceUom());
        variancePercentEditor.setPercent(filter.getVariancePercent());
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
        AuthorizeQueryFilter filter = model.getFilter();

        filter.setHierarchyNode(hierarchyPanel.getHierarchyNode());
        filter.setVarianceUom(varianceUomEditor.getIntegerOrNull());
        filter.setVariancePercent(variancePercentEditor.getBigDecimal());

        notifyREventListeners(new RActionEvent(this, SimClientStateKey.STOCK_COUNT_AUTH_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
