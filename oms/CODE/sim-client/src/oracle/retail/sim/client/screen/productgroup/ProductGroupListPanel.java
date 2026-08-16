package oracle.retail.sim.client.screen.productgroup;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.NumericIdDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.productgroup.ProductGroupMessageText;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.security.PermissionKey;

/********************************************************************************************************
 * Product Group List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -306978374645466627L;

    private ProductGroupListModel model = new ProductGroupListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable productGroupTable = new SimTable(new ProductGroupDefinition());
    private SimTablePane productGroupPane = new SimTablePane(productGroupTable);

    private ProductGroupFilterDialog filterDialog = new ProductGroupFilterDialog();

    private static final String PRODUCT_GROUP_FILTER_SELECTED = "ProductGroup.filterSelected";
    private static final String PRODUCT_GROUP_SELECTED = "ProductGroup.selected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public ProductGroupListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, PRODUCT_GROUP_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        productGroupTable.setTableEditable(false);
        productGroupTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        productGroupTable.registerDoubleClickAction(this, PRODUCT_GROUP_SELECTED);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(productGroupPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return productGroupTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleCancelGroup() {
        List<ProductGroupVO> productGroupVOs = productGroupTable.getAllSelectedRowData();
        if (productGroupVOs.isEmpty()) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        try {
            RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), "Product Group Delete Confirmation");
            dialog.setMessage(ProductGroupMessageText.DELETE_CONFIRM);
            dialog.setYesNoType();

            if (dialog.getConfirmation()) {
                List<ProductGroupVO> validToProcessVOs = new ArrayList<>();
                for (ProductGroupVO groupVO : productGroupVOs) {
                    if (!model.hasDataPermission(PermissionKey.DATA_PRODUCT_GROUP_TYPE, groupVO.getType().getCode())) {
                        throw new BusinessException(ProductGroupMessageText.DELETE_DENIED);
                    }
                    if (!model.isAttachedToSchedule(groupVO.getId())) {
                        validToProcessVOs.add(groupVO);
                    }
                }
                if (validToProcessVOs.size() != productGroupVOs.size()) {
                    displayWarning(ProductGroupMessageText.DELETE_ERROR);
                }
                model.cancelProductGroups(validToProcessVOs);
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(PRODUCT_GROUP_SELECTED)) {
                doProductGroupSelected();
            } else if (command.equals(PRODUCT_GROUP_FILTER_SELECTED)) {
                doProductGroupFilterSelected();
            } else if (command.equals(SimClientStateKey.PRODUCT_GROUP_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doProductGroupSelected() throws Exception {
        model.storeProductGroup((ProductGroupVO) productGroupTable.getSelectedRowData());
        navigate(SimScreenName.PRODUCT_GROUP_DETAIL_SCREEN);
    }

    private void doProductGroupFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        productGroupTable.setRows(model.findProductGroups());
    }

    /****************************************************************************************************
     * Product Group Table Definition
     ***************************************************************************************************/

    private class ProductGroupDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ProductGroupVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableSortAttribute("type"));
            attributes.add(new SimTableSortAttribute("id"));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("ID", "id", new NumericIdDisplayer()));
            attributes.add(new SimTableAttribute("Description", "description"));
            attributes.add(new SimTableAttribute("Type", "type", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Store", "store", new GroupStoreDisplayer(), null));
            return attributes;
        }
    }

    /****************************************************************************************************
     * Group Store Displayer - This is a specialized renderer for the stock groups.
     ***************************************************************************************************/

    private class GroupStoreDisplayer extends DualAttributeDisplayer {

        public GroupStoreDisplayer() {
            super("id", "name");
        }

        public String getDisplayText(Object value) {
            if (value == null) {
                return Translator.getText("All Stores");
            }
            return super.getDisplayText(value);
        }
    }
}
