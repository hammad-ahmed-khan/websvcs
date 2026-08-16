package oracle.retail.sim.client.screen.productgroup;

import java.awt.GridBagLayout;
import java.math.BigDecimal;
import javax.swing.SwingConstants;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.displayer.StandardUomDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDecimalFieldEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RPercentFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RRadioButton;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupMessageText;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.stockcount.StockCountBreakdownType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;

/********************************************************************************************************
 * Product Group Attribute Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupAttributeTab extends SimTab implements REventListener {
    private static final long serialVersionUID = -7087882536116679456L;

    private ProductGroupDetailModel model;

    private RDisplayLabelEditor groupIdEditor = new RDisplayLabelEditor("Group ID");
    private RComboBoxEditor groupTypeEditor = new RComboBoxEditor("Type");
    private RLongTextFieldEditor groupDescEditor = new RLongTextFieldEditor("Group Description");
    private RComboBoxEditor groupUomEditor = new RComboBoxEditor("Group UOM");

    private RRadioButton idStoreRadioButton = new RRadioButton("Null");
    private RRadioButton allStoreRadioButton = new RRadioButton("All Stores");

    private RIntegerFieldEditor varianceUomEditor = new RIntegerFieldEditor("Variance SUOM");
    private RDecimalFieldEditor varianceValueEditor = new RDecimalFieldEditor("Variance Value");
    private RPercentFieldEditor variancePercentEditor = new RPercentFieldEditor("Variance %");
    private RComboBoxEditor breakdownTypeEditor = new RComboBoxEditor("Hierarchy Breakdown");
    private RComboBoxEditor countMethodEditor = new RComboBoxEditor("Counting Method");
    private RCheckBoxEditor recountEditor = new RCheckBoxEditor("Re-Count Discrepancies");
    private RCheckBoxEditor autoAuthEditor = new RCheckBoxEditor("Auto Authorize");

    private RCheckBoxEditor statusActiveEditor = new RCheckBoxEditor("Active");
    private RCheckBoxEditor statusInactiveEditor = new RCheckBoxEditor("Inactive");
    private RCheckBoxEditor statusDiscEditor = new RCheckBoxEditor("Discontinued");
    private RCheckBoxEditor statusDeletedEditor = new RCheckBoxEditor("Deleted");

    private RCheckBoxEditor sohZeroEditor = new RCheckBoxEditor("SOH = 0");
    private RCheckBoxEditor sohGreaterEditor = new RCheckBoxEditor("SOH > 0");
    private RCheckBoxEditor sohLesserEditor = new RCheckBoxEditor("SOH < 0");

    private RIntegerFieldEditor expireDaysEditor = new RIntegerFieldEditor("Days Before Expiration");
    private RIntegerFieldEditor deliveryDaysEditor = new RIntegerFieldEditor("Days Before Delivery");

    private RIntegerFieldEditor wastageUomEditor = new RIntegerFieldEditor("Shrinkage SUOM");
    private RPercentFieldEditor wastagePercentEditor = new RPercentFieldEditor("Shrinkage %");

    private RCheckBoxEditor actualReplEditor = new RCheckBoxEditor("Actual Shelf Repl Amount less than Suggested Shelf Repl Amount");
    private RCheckBoxEditor actualPickEditor = new RCheckBoxEditor("Actual Pick Amount less than Suggested Pick Amount");
    private RCheckBoxEditor negativePickEditor = new RCheckBoxEditor("Negative Available Inventory");
    private RCheckBoxEditor uinDiscrepantPickEditor = new RCheckBoxEditor("UIN Discrepancies");

    private REditorPanel groupEditorPanel = new REditorPanel(2, 2);
    private REditorPanel storePanel = new REditorPanel(2);
    private RPanel groupDetailPanel = new RPanel(new GridBagLayout());
    private REditorPanel variancePanel = new REditorPanel(3);
    private REditorPanel countMethodPanel = new REditorPanel(4);
    private REditorPanel itemStatusPanel = new REditorPanel(4);
    private REditorPanel stockOnHandPanel = new REditorPanel(3);
    private REditorPanel autoAuthPanel = new REditorPanel(1);
    private RPanel countDetailPanel = new RPanel(new GridBagLayout());
    private REditorPanel problemLinePanel = new REditorPanel(3);
    private REditorPanel requestDetailPanel = new REditorPanel(2);
    private REditorPanel wastageDetailPanel = new REditorPanel(2);

    private static final String GROUP_TYPE_SELECTED = "GroupType.selected";
    private static final String DESCRIPTION_MODIFIED = "Description.modified";
    private static final String UOM_MODIFIED = "UOM.modified";
    private static final String SINGLE_STORE_SELECTED = "SingleStore.selected";
    private static final String ALL_STORE_SELECTED = "AllStore.selected";
    private static final String COUNT_METHOD_SELECTED = "CountMethod.selected";
    private static final String EXPIRE_DAYS_MODIFIED = "ExpireDays.modified";
    private static final String DELIVERY_DAYS_MODIFIED = "DeliveryDays.modified";
    private static final String VARIANCE_UOM_MODIFIED = "VarianceUom.modified";
    private static final String VARIANCE_PERCENT_MODIFIED = "VariancePercent.modified";
    private static final String VARIANCE_VALUE_MODIFIED = "VarianceValue.modified";
    private static final String WASTAGE_UOM_MODIFIED = "WastageUom.modified";
    private static final String WASTAGE_PERCENT_MODIFIED = "WastagePercent.modified";

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/
    public ProductGroupAttributeTab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        idStoreRadioButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        allStoreRadioButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        idStoreRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
        allStoreRadioButton.setHorizontalAlignment(SwingConstants.LEFT);

        groupDescEditor.setIdentifier(SimName.PRODUCT_GROUP_DESCRIPTION);
        varianceUomEditor.setIdentifier(SimName.PRODUCT_GROUP_VARIANCE_UOM);
        varianceValueEditor.setIdentifier(SimName.PRODUCT_GROUP_VARIANCE_VALUE);
        variancePercentEditor.setIdentifier(SimName.PRODUCT_GROUP_VARIANCE_PERCENT);
        expireDaysEditor.setIdentifier(SimName.DAYS_BEFORE_EXPIRATION);
        deliveryDaysEditor.setIdentifier(SimName.DAYS_BEFORE_DELIVERY);
        wastageUomEditor.setIdentifier(SimName.PRODUCT_GROUP_VARIANCE_UOM);
        wastagePercentEditor.setIdentifier(SimName.PRODUCT_GROUP_VARIANCE_PERCENT);

        groupTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        groupTypeEditor.setSelectionRequired(true);
        groupUomEditor.setDisplayer(new StandardUomDisplayer());
        groupUomEditor.setSelectionRequired(true);
        countMethodEditor.setDisplayer(new TranslatedObjectDisplayer());

        breakdownTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        breakdownTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        breakdownTypeEditor.setSortEnabled(false);

        groupTypeEditor.registerAction(this, GROUP_TYPE_SELECTED);
        groupDescEditor.registerAction(this, DESCRIPTION_MODIFIED);
        groupUomEditor.registerAction(this, UOM_MODIFIED);
        idStoreRadioButton.registerAction(this, SINGLE_STORE_SELECTED);
        allStoreRadioButton.registerAction(this, ALL_STORE_SELECTED);
        countMethodEditor.registerAction(this, COUNT_METHOD_SELECTED);
        varianceUomEditor.registerAction(this, VARIANCE_UOM_MODIFIED);
        variancePercentEditor.registerAction(this, VARIANCE_PERCENT_MODIFIED);
        varianceValueEditor.registerAction(this, VARIANCE_VALUE_MODIFIED);
        expireDaysEditor.registerAction(this, EXPIRE_DAYS_MODIFIED);
        deliveryDaysEditor.registerAction(this, DELIVERY_DAYS_MODIFIED);
        wastageUomEditor.registerAction(this, WASTAGE_UOM_MODIFIED);
        wastagePercentEditor.registerAction(this, WASTAGE_PERCENT_MODIFIED);

        groupTypeEditor.setSizeType(EditorConstants.LARGE);
        groupDescEditor.setSizeType(EditorConstants.LARGE);
        groupUomEditor.setSizeType(EditorConstants.MEDIUM);
        varianceUomEditor.setSizeType(EditorConstants.SMALL);
        varianceValueEditor.setSizeType(EditorConstants.SMALL);
        variancePercentEditor.setSizeType(EditorConstants.SMALL);
        breakdownTypeEditor.setSizeType(EditorConstants.MEDIUM);
        expireDaysEditor.setSizeType(EditorConstants.SMALL);
        deliveryDaysEditor.setSizeType(EditorConstants.SMALL);
        wastageUomEditor.setSizeType(EditorConstants.SMALL);
        wastagePercentEditor.setSizeType(EditorConstants.SMALL);

        groupTypeEditor.setRequired(true);
        groupDescEditor.setRequired(true);
        groupUomEditor.setRequired(true);
        countMethodEditor.setRequired(true);

        setFullDisabledMode();
    }

    private void layoutTab() {
        groupEditorPanel.add(groupTypeEditor);
        groupEditorPanel.add(groupDescEditor);
        groupEditorPanel.add(groupIdEditor);
        groupEditorPanel.add(groupUomEditor);

        storePanel.setTitleBorder("Stores");
        storePanel.add(idStoreRadioButton);
        storePanel.add(allStoreRadioButton);

        groupDetailPanel.setTitleBorder("Product Group Detail");
        groupDetailPanel.add(groupEditorPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        groupDetailPanel.add(storePanel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        variancePanel.setTitleBorder("Variance");
        variancePanel.add(varianceUomEditor);
        variancePanel.add(variancePercentEditor);
        variancePanel.add(varianceValueEditor);

        countMethodPanel.setTitleBorder("Counting Method");
        countMethodPanel.add(countMethodEditor);
        countMethodPanel.add(breakdownTypeEditor);
        countMethodPanel.add(recountEditor);
        countMethodPanel.add(autoAuthEditor);

        itemStatusPanel.setTitleBorder("Item Status");
        itemStatusPanel.add(statusActiveEditor);
        itemStatusPanel.add(statusInactiveEditor);
        itemStatusPanel.add(statusDiscEditor);
        itemStatusPanel.add(statusDeletedEditor);

        stockOnHandPanel.setTitleBorder("Stock On Hand");
        stockOnHandPanel.add(sohZeroEditor);
        stockOnHandPanel.add(sohGreaterEditor);
        stockOnHandPanel.add(sohLesserEditor);

        problemLinePanel.setTitleBorder("Problem Line");
        problemLinePanel.add(actualReplEditor);
        problemLinePanel.add(actualPickEditor);
        problemLinePanel.add(negativePickEditor);
        problemLinePanel.add(uinDiscrepantPickEditor);
        countDetailPanel.setTitleBorder("Stock Count Detail");
        countDetailPanel.add(variancePanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        countDetailPanel.add(countMethodPanel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        countDetailPanel.add(problemLinePanel, GridTool.constraints(0, 1, 2, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        countDetailPanel.add(itemStatusPanel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        countDetailPanel.add(stockOnHandPanel, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        requestDetailPanel.setTitleBorder("Item Request Detail");
        requestDetailPanel.add(expireDaysEditor);
        requestDetailPanel.add(deliveryDaysEditor);

        wastageDetailPanel.setTitleBorder("Wastage Detail");
        wastageDetailPanel.add(wastageUomEditor);
        wastageDetailPanel.add(wastagePercentEditor);

        setLayout(new GridBagLayout());
        add(groupDetailPanel, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 1, 0, 0, 2, 0));
        add(countDetailPanel, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 1, 0, 0, 2, 0));
        add(requestDetailPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 2, 0));
        add(wastageDetailPanel, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 1, 0, 0, 2, 0));
        add(new RLabel(), GridTool.constraints(0, 3, 2, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    public void setModel(ProductGroupDetailModel model) {
        this.model = model;
    }

    public void loadTabInCreateMode() {
        loadComboBoxes();

        groupTypeEditor.setActionsEnabled(false);
        groupTypeEditor.setItems(model.getFilteredProductGroupTypes());
        groupTypeEditor.setActionsEnabled(true);
        groupTypeEditor.setEnabled(true);

        idStoreRadioButton.setActionsEnabled(false);
        idStoreRadioButton.setText(model.getStoreDescription(model.getStore()));
        idStoreRadioButton.setSelected(true);
        idStoreRadioButton.setActionsEnabled(true);

        uinDiscrepantPickEditor.setVisible(model.isSerialNumberProcessingEnabled());
    }

    public void loadTabInEditMode() throws Exception {
        ProductGroup productGroup = model.getProductGroup();
        ProductGroupType groupType = productGroup.getType();
        ProductGroupWrapper wrapper = model.getProductGroupWrapper();

        groupTypeEditor.setActionsEnabled(false);
        groupTypeEditor.setItems(model.getProductGroupTypes());
        groupTypeEditor.setSelectedItem(groupType);
        groupTypeEditor.setActionsEnabled(true);

        loadComboBoxes();

        if (model.isDataEntryAllowed()) {
            refreshAttributeTab();
        }

        groupIdEditor.setData(wrapper.getId());
        groupDescEditor.setText(wrapper.getDescription());
        groupUomEditor.setActionsEnabled(false);
        groupUomEditor.setSelectedItem(wrapper.getUomType());
        groupUomEditor.setActionsEnabled(true);

        idStoreRadioButton.setActionsEnabled(false);
        allStoreRadioButton.setActionsEnabled(false);
        idStoreRadioButton.setText(model.getStoreDescription(wrapper.getStore()));
        idStoreRadioButton.setSelected(wrapper.getStore() != null);
        allStoreRadioButton.setSelected(wrapper.getStore() == null);
        idStoreRadioButton.setActionsEnabled(true);
        allStoreRadioButton.setActionsEnabled(true);

        if (groupType.isItemRequest()) {
            expireDaysEditor.setInteger(productGroup.getDaysToExpire());
            deliveryDaysEditor.setInteger(productGroup.getDaysToRequestDelivery());
            return;
        }
        if (groupType.isWastage()) {
            wastageUomEditor.setInteger(productGroup.getVarianceCount());
            wastagePercentEditor.setPercent(productGroup.getVariancePercent());
            return;
        }
        if (groupType.isStockCountUnit() || groupType.isStockCountUnitAmount() || groupType.isStockCountProblemLine()) {
            varianceUomEditor.setInteger(productGroup.getVarianceCount());
            varianceValueEditor.setDecimal(productGroup.getVarianceValue());
            variancePercentEditor.setPercent(productGroup.getVariancePercent());
            countMethodEditor.setActionsEnabled(false);
            countMethodEditor.setSelectedItem(productGroup.getCountingMethod());
            countMethodEditor.setActionsEnabled(true);
            breakdownTypeEditor.setSelectedItem(productGroup.getBreakdownType());
            breakdownTypeEditor.setEnabled(model.isDataEntryAllowed() && !productGroup.getCountingMethod().isGuided());
            recountEditor.setSelected(productGroup.isRecountRequired());
            recountEditor.setEnabled(!productGroup.getCountingMethod().isThirdParty());
            autoAuthEditor.setSelected(productGroup.isAutoAuthorize());
            statusActiveEditor.setSelected(productGroup.includeActiveItems());
            statusInactiveEditor.setSelected(productGroup.includeInactiveItems());
            statusDiscEditor.setSelected(productGroup.includeDiscontinuedItems());
            statusDeletedEditor.setSelected(productGroup.includeDeletedItems());
            sohZeroEditor.setSelected(productGroup.includeSOHZero());
            sohGreaterEditor.setSelected(productGroup.includeSOHGreaterThanZero());
            sohLesserEditor.setSelected(productGroup.includeSOHLessThanZero());

            if (groupType.isStockCountProblemLine()) {
                actualReplEditor.setSelected(productGroup.isProblemLineReplLessSuggested());
                actualPickEditor.setSelected(productGroup.isProblemLinePickLessSuggested());
                negativePickEditor.setSelected(productGroup.isProblemLineNegativeAvailable());
                uinDiscrepantPickEditor.setSelected(productGroup.isProblemLineUINDiscrepancy());
            }
        }

        uinDiscrepantPickEditor.setVisible(model.isSerialNumberProcessingEnabled());

        productGroup.doSetDirty(false);
    }

    private void loadComboBoxes() {
        groupUomEditor.setActionsEnabled(false);
        groupUomEditor.setItems(model.getUnitOfMeasureModes());
        groupUomEditor.setActionsEnabled(true);

        countMethodEditor.setActionsEnabled(false);
        countMethodEditor.setItems(model.getCountingMethods());
        countMethodEditor.setActionsEnabled(true);

        breakdownTypeEditor.setActionsEnabled(false);
        breakdownTypeEditor.setItems(model.getBreakdownTypes());
        breakdownTypeEditor.removeEmptySelection();
        breakdownTypeEditor.setActionsEnabled(true);

        setFullDisabledMode();
    }

    /****************************************************************************************************
     * Handle Action Events
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(GROUP_TYPE_SELECTED)) {
            doGroupTypeModified();
        } else if (command.equals(DESCRIPTION_MODIFIED)) {
            doDescriptionModified();
        } else if (command.equals(UOM_MODIFIED)) {
            doGroupUomModified();
        } else if (command.equals(SINGLE_STORE_SELECTED)) {
            doSingleStoreSelected();
        } else if (command.equals(ALL_STORE_SELECTED)) {
            doAllStoresSelected();
        } else if (command.equals(COUNT_METHOD_SELECTED)) {
            doCountMethodModified();
        } else if (command.equals(VARIANCE_UOM_MODIFIED)) {
            doVarianceUomModified();
        } else if (command.equals(VARIANCE_PERCENT_MODIFIED)) {
            doVariancePercentModified();
        } else if (command.equals(VARIANCE_VALUE_MODIFIED)) {
            doVarianceValueModified();
        } else if (command.equals(EXPIRE_DAYS_MODIFIED)) {
            doExpirationDaysModified();
        } else if (command.equals(DELIVERY_DAYS_MODIFIED)) {
            doDeliveryDaysModified();
        } else if (command.equals(WASTAGE_UOM_MODIFIED)) {
            doWastageUomModified();
        } else if (command.equals(WASTAGE_PERCENT_MODIFIED)) {
            doWastagePercentModified();
        }
    }

    /****************************************************************************************************
     * Group Type Modified
     ***************************************************************************************************/

    private void doGroupTypeModified() {
        try {
            ProductGroupType productGroupType = (ProductGroupType) groupTypeEditor.getSelectedItem();
            model.createProductGroup(productGroupType);
            clearAllEditors();
            setFullDisabledMode();
            if (productGroupType != null) {
                refreshAttributeTab();
            }
            notifyREventListeners(new RActionEvent(this, SimClientStateKey.PRODUCT_GROUP_SELECTED));
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Description Modified
     ***************************************************************************************************/

    private void doDescriptionModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setDescription(groupDescEditor.getText());
        } catch (Throwable exception) {
            displayException(exception);
            groupDescEditor.setText(productGroup.getDescription());
            groupDescEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * UOM Modified
     ***************************************************************************************************/

    private void doGroupUomModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setUnitOfMeasureMode((UOMMode) groupUomEditor.getSelectedItem());
        } catch (BusinessException exception) {
            displayException(exception);
            groupUomEditor.setSelectedItem(productGroup.getUnitOfMeasureMode());
            groupUomEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Store Type Modified
     ***************************************************************************************************/

    private void doAllStoresSelected() {
        ProductGroup productGroup = model.getProductGroup();
        idStoreRadioButton.setActionsEnabled(false);
        idStoreRadioButton.setSelected(false);
        idStoreRadioButton.setActionsEnabled(true);
        try {
            productGroup.setForAllStores();
            refreshAttributeTab();
            idStoreRadioButton.setText(model.getStoreDescription(model.getStore()));
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSingleStoreSelected() {
        ProductGroup productGroup = model.getProductGroup();
        allStoreRadioButton.setActionsEnabled(false);
        allStoreRadioButton.setSelected(false);
        allStoreRadioButton.setActionsEnabled(true);
        try {
            productGroup.setStore(model.getStore());
            refreshAttributeTab();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Count Method Modified
     ***************************************************************************************************/

    private void doCountMethodModified() {
        StockCountingMethod countingMethod = (StockCountingMethod) countMethodEditor.getSelectedItem();
        ProductGroup productGroup = model.getProductGroup();
        try {
            if (countingMethod == StockCountingMethod.THIRD_PARTY) {
                recountEditor.setEnabled(false);
                recountEditor.setSelected(false);
            } else {
                recountEditor.setEnabled(model.isDataEntryAllowed());
            }
            if (countingMethod == StockCountingMethod.GUIDED) {
                breakdownTypeEditor.setSelectedItem(StockCountBreakdownType.LOCATION);
                breakdownTypeEditor.setEnabled(false);
            } else {
                breakdownTypeEditor.setEnabled(model.isDataEntryAllowed());
            }
            productGroup.setCountingMethod(countingMethod);
        } catch (Exception exception) {
            displayException(exception);
            countMethodEditor.setActionsEnabled(false);
            countMethodEditor.setSelectedItem(productGroup.getCountingMethod());
            countMethodEditor.setActionsEnabled(true);
            countMethodEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Variance UOM Modified
     ***************************************************************************************************/

    private void doVarianceUomModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setVarianceCount(varianceUomEditor.getIntegerOrNull());
        } catch (Exception exception) {
            displayException(exception);
            varianceUomEditor.setInteger(productGroup.getVarianceCount());
            varianceUomEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Variance Percent Modified
     ***************************************************************************************************/

    private void doVariancePercentModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setVariancePercent(variancePercentEditor.getBigDecimal());
        } catch (Exception exception) {
            displayException(exception);
            variancePercentEditor.setPercent(productGroup.getVariancePercent());
            variancePercentEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Variance Value Modified
     ***************************************************************************************************/

    private void doVarianceValueModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            BigDecimal varianceValue = varianceValueEditor.getBigDecimal();
            if (varianceValue != null) {
                varianceValue = varianceValue.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            productGroup.setVarianceValue(varianceValue);
        } catch (Exception exception) {
            displayException(exception);
            varianceValueEditor.setDecimal(productGroup.getVarianceValue());
            varianceValueEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Delivery Days Modified
     ***************************************************************************************************/

    private void doExpirationDaysModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setDaysToExpire(expireDaysEditor.getIntegerOrNull());
        } catch (Exception exception) {
            displayException(exception);
            expireDaysEditor.setInteger(productGroup.getDaysToExpire());
            expireDaysEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Delivery Days Modified
     ***************************************************************************************************/

    private void doDeliveryDaysModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setDaysToRequestDelivery(deliveryDaysEditor.getIntegerOrNull());
        } catch (Exception exception) {
            displayException(exception);
            deliveryDaysEditor.setInteger(productGroup.getDaysToRequestDelivery());
            deliveryDaysEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Wastage UOM Modified
     ***************************************************************************************************/

    private void doWastageUomModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setVarianceCount(wastageUomEditor.getInteger());
        } catch (Exception exception) {
            displayException(exception);
            wastageUomEditor.setInteger(productGroup.getVarianceCount());
            wastageUomEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Wastage Percent Modified
     ***************************************************************************************************/

    private void doWastagePercentModified() {
        ProductGroup productGroup = model.getProductGroup();
        try {
            productGroup.setVariancePercent(wastagePercentEditor.getBigDecimal());
        } catch (Exception exception) {
            displayException(exception);
            wastagePercentEditor.setPercent(productGroup.getVariancePercent());
            wastagePercentEditor.requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Refresh Panel Methods
     ***************************************************************************************************/

    private void refreshAttributeTab() {
        ProductGroupWrapper productGroupWrapper = model.getProductGroupWrapper();

        refreshProductGroupPanel(productGroupWrapper);

        ProductGroupType type = productGroupWrapper.getGroupType();
        if (type == ProductGroupType.ITEM_REQUEST) {
            refreshItemRequestPanel();
        } else if (type == ProductGroupType.STOCK_COUNT_WASTAGE) {
            refreshWastagePanel();
        } else if (type == ProductGroupType.STOCK_COUNT_UNIT) {
            refreshStockCountPanel();
        } else if (type == ProductGroupType.STOCK_COUNT_UNIT_AMOUNT) {
            refreshStockCountUnitAmountPanel();
        } else if (type == ProductGroupType.STOCK_COUNT_PROBLEM_LINE) {
            refreshProblemLinePanel();
        } else if (type == ProductGroupType.STOCK_COUNT_RMS_SYNC) {
            refreshRMSSyncPanel();
        }
    }

    private void refreshProductGroupPanel(ProductGroupWrapper wrapper) {
        groupIdEditor.setData(wrapper.getId());
        groupDescEditor.setText(wrapper.getDescription());
        groupUomEditor.setActionsEnabled(false);
        groupUomEditor.setSelectedItem(wrapper.getUomType());
        groupUomEditor.setActionsEnabled(true);

        idStoreRadioButton.setActionsEnabled(false);
        allStoreRadioButton.setActionsEnabled(false);
        idStoreRadioButton.setText(model.getStoreDescription(wrapper.getStore()));
        idStoreRadioButton.setSelected(wrapper.getStore() != null);
        allStoreRadioButton.setSelected(wrapper.getStore() == null);
        idStoreRadioButton.setActionsEnabled(true);
        allStoreRadioButton.setActionsEnabled(true);

        groupTypeEditor.setEnabled(wrapper.isGroupTypeEnabled());
        groupDescEditor.setEnabled(wrapper.isGroupDescriptionEnabled());
        groupUomEditor.setEnabled(wrapper.isUOMEnabled());
        idStoreRadioButton.setEnabled(wrapper.isSingleStoreEnabled());
        allStoreRadioButton.setEnabled(wrapper.isAllStoresEnabled());
    }

    private void refreshStockCountPanel() {
        countMethodEditor.setActionsEnabled(false);
        countMethodEditor.setSelectedItem(StockCountingMethod.UNGUIDED);
        countMethodEditor.setActionsEnabled(true);
        statusActiveEditor.setSelected(true);
        statusInactiveEditor.setSelected(true);
        statusDiscEditor.setSelected(true);
        statusDeletedEditor.setSelected(false);
        sohZeroEditor.setSelected(true);
        sohGreaterEditor.setSelected(true);
        sohLesserEditor.setSelected(true);

        countDetailPanel.setEnabled(true);
        variancePanel.setEnabled(true);
        varianceUomEditor.setEnabled(true);
        varianceValueEditor.setEnabled(false);
        variancePercentEditor.setEnabled(true);
        countMethodPanel.setEnabled(true);
        countMethodEditor.setEnabled(true);
        breakdownTypeEditor.setEnabled(true);
        recountEditor.setEnabled(true);
        autoAuthEditor.setEnabled(true);
        itemStatusPanel.setEnabled(true);
        statusActiveEditor.setEnabled(true);
        statusInactiveEditor.setEnabled(true);
        statusDiscEditor.setEnabled(true);
        statusDeletedEditor.setEnabled(true);
        stockOnHandPanel.setEnabled(true);
        sohZeroEditor.setEnabled(true);
        sohGreaterEditor.setEnabled(true);
        sohLesserEditor.setEnabled(true);
    }

    private void refreshStockCountUnitAmountPanel() {
        if (model.hasThirdPartyPermission()) {
            countMethodEditor.setActionsEnabled(false);
            countMethodEditor.setSelectedItem(StockCountingMethod.THIRD_PARTY);
            countMethodEditor.setActionsEnabled(true);
            recountEditor.setEnabled(false);
        } else {
            countMethodEditor.setActionsEnabled(false);
            countMethodEditor.setSelectedItem(StockCountingMethod.UNGUIDED);
            countMethodEditor.setActionsEnabled(true);
            recountEditor.setEnabled(true);
        }
        statusActiveEditor.setSelected(true);
        statusInactiveEditor.setSelected(true);
        statusDiscEditor.setSelected(true);
        statusDeletedEditor.setSelected(true);
        sohZeroEditor.setSelected(true);
        sohGreaterEditor.setSelected(true);
        sohLesserEditor.setSelected(true);
        countDetailPanel.setEnabled(true);
        variancePanel.setEnabled(true);
        varianceUomEditor.setEnabled(true);
        varianceValueEditor.setEnabled(true);
        variancePercentEditor.setEnabled(true);
        countMethodPanel.setEnabled(true);
        countMethodEditor.setEnabled(true);
        breakdownTypeEditor.setEnabled(true);
        autoAuthEditor.setEnabled(true);
        itemStatusPanel.setEnabled(false);
        statusActiveEditor.setEnabled(false);
        statusInactiveEditor.setEnabled(false);
        statusDiscEditor.setEnabled(false);
        statusDeletedEditor.setEnabled(false);
        stockOnHandPanel.setEnabled(false);
        sohZeroEditor.setEnabled(false);
        sohGreaterEditor.setEnabled(false);
        sohLesserEditor.setEnabled(false);
    }

    private void refreshProblemLinePanel() {
        refreshStockCountPanel();
        sohLesserEditor.setSelected(true);
        sohGreaterEditor.setSelected(false);
        sohZeroEditor.setSelected(false);
        actualReplEditor.setSelected(true);
        actualPickEditor.setSelected(true);
        negativePickEditor.setSelected(true);
        uinDiscrepantPickEditor.setSelected(true);
        problemLinePanel.setEnabled(true);
        actualReplEditor.setEnabled(true);
        actualPickEditor.setEnabled(true);
        negativePickEditor.setEnabled(true);
        uinDiscrepantPickEditor.setEnabled(true);
        itemStatusPanel.setEnabled(false);
        statusActiveEditor.setEnabled(false);
        statusInactiveEditor.setEnabled(false);
        statusDiscEditor.setEnabled(false);
        statusDeletedEditor.setEnabled(false);
        sohLesserEditor.setEnabled(true);
        sohGreaterEditor.setEnabled(false);
        sohZeroEditor.setEnabled(false);
    }

    private void refreshRMSSyncPanel() {
        countMethodEditor.setActionsEnabled(false);
        countMethodEditor.setEmptySelection();
        countMethodEditor.setActionsEnabled(true);
    }

    private void refreshItemRequestPanel() {
        requestDetailPanel.setEnabled(true);
        expireDaysEditor.setEnabled(true);
        deliveryDaysEditor.setEnabled(true);
    }

    private void refreshWastagePanel() {
        wastageDetailPanel.setEnabled(true);
        wastageUomEditor.setEnabled(true);
        wastagePercentEditor.setEnabled(true);
    }

    /****************************************************************************************************
     * Enabled/Disabled Mode Helper Methods
     ***************************************************************************************************/

    private void clearAllEditors() {
        setComboEnabledMode(false);
        groupUomEditor.setEmptySelection();
        varianceUomEditor.clear();
        varianceValueEditor.clear();
        variancePercentEditor.clear();
        recountEditor.setSelected(false);
        breakdownTypeEditor.setEmptySelection();
        statusActiveEditor.setSelected(false);
        statusInactiveEditor.setSelected(false);
        statusDiscEditor.setSelected(false);
        statusDeletedEditor.setSelected(false);
        sohZeroEditor.setSelected(false);
        sohGreaterEditor.setSelected(false);
        sohLesserEditor.setSelected(false);
        autoAuthEditor.setSelected(false);
        actualReplEditor.setSelected(false);
        actualPickEditor.setSelected(false);
        negativePickEditor.setSelected(false);
        uinDiscrepantPickEditor.setSelected(false);
        expireDaysEditor.clear();
        deliveryDaysEditor.clear();
        wastageUomEditor.clear();
        wastagePercentEditor.clear();
        setComboEnabledMode(true);
    }

    private void setComboEnabledMode(boolean value) {
        groupUomEditor.setActionsEnabled(value);
        breakdownTypeEditor.setActionsEnabled(value);
    }

    private void setFullDisabledMode() {
        groupIdEditor.setEnabled(false);
        groupTypeEditor.setEnabled(false);
        groupDescEditor.setEnabled(false);
        groupUomEditor.setEnabled(false);
        idStoreRadioButton.setEnabled(false);
        allStoreRadioButton.setEnabled(false);
        varianceUomEditor.setEnabled(false);
        varianceValueEditor.setEnabled(false);
        variancePercentEditor.setEnabled(false);
        breakdownTypeEditor.setEnabled(false);
        countMethodEditor.setEnabled(false);
        recountEditor.setEnabled(false);
        statusActiveEditor.setEnabled(false);
        statusInactiveEditor.setEnabled(false);
        statusDiscEditor.setEnabled(false);
        statusDeletedEditor.setEnabled(false);
        sohZeroEditor.setEnabled(false);
        sohGreaterEditor.setEnabled(false);
        sohLesserEditor.setEnabled(false);
        autoAuthEditor.setEnabled(false);
        actualReplEditor.setEnabled(false);
        actualPickEditor.setEnabled(false);
        negativePickEditor.setEnabled(false);
        uinDiscrepantPickEditor.setEnabled(false);
        expireDaysEditor.setEnabled(false);
        deliveryDaysEditor.setEnabled(false);
        wastageUomEditor.setEnabled(false);
        wastagePercentEditor.setEnabled(false);
        groupEditorPanel.setEnabled(false);
        storePanel.setEnabled(false);
        variancePanel.setEnabled(false);
        countMethodPanel.setEnabled(false);
        itemStatusPanel.setEnabled(false);
        stockOnHandPanel.setEnabled(false);
        autoAuthPanel.setEnabled(false);
        countDetailPanel.setEnabled(false);
        problemLinePanel.setEnabled(false);
        requestDetailPanel.setEnabled(false);
        wastageDetailPanel.setEnabled(false);
    }

    /****************************************************************************************************
     * Handle Done Action
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        if (model.isNewProductGroup() && groupTypeEditor.getSelectedItem() == null) {
            displayError(ProductGroupMessageText.MISSING_TYPE);
            return false;
        }
        if (!model.isDataEntryAllowed()) {
            return true;
        }
        ProductGroup productGroup = model.getProductGroup();
        if (productGroup.getType().isStockCount()) {
            productGroup.setCountingMethod((StockCountingMethod) countMethodEditor.getSelectedItem());
            productGroup.setBreakdownType((StockCountBreakdownType) breakdownTypeEditor.getSelectedItem());
            productGroup.setRecountRequired(recountEditor.isSelected());
            productGroup.setAutoAuthorize(autoAuthEditor.isSelected());
            productGroup.setIncludeActiveItem(statusActiveEditor.isSelected());
            productGroup.setIncludeInactiveItems(statusInactiveEditor.isSelected());
            productGroup.setIncludeDiscontinuedItems(statusDiscEditor.isSelected());
            productGroup.setIncludeDeletedItems(statusDeletedEditor.isSelected());
            productGroup.setIncludeSOHZero(sohZeroEditor.isSelected());
            productGroup.setIncludeSOHLessThanZero(sohLesserEditor.isSelected());
            productGroup.setIncludeSOHGreaterThanZero(sohGreaterEditor.isSelected());

            if (productGroup.getType().isStockCountProblemLine()) {
                productGroup.setProblemLineReplLessSuggested(actualReplEditor.isSelected());
                productGroup.setProblemLinePickLessSuggested(actualPickEditor.isSelected());
                productGroup.setProblemLineNegativeAvailable(negativePickEditor.isSelected());
                productGroup.setProblemLineUINDiscrepancy(uinDiscrepantPickEditor.isSelected());
            }
        }
        try {
            productGroup.isCoherent();
        } catch (BusinessException exception) {
            displayException(exception);
            return false;
        }
        if (!model.checkProductGroupLock()) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return false;
        }
        model.saveProductGroup();
        model.releaseProductGroupLock();
        return true;
    }
}
