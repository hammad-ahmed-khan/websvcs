package oracle.retail.sim.client.screen.config;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.BooleanEnabledDisplayer;
import oracle.retail.sim.client.displayer.SimDisplayerFactory;
import oracle.retail.sim.client.displayer.StandardUomDisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.client.swing.displayer.KeyValuePairDisplayer;
import oracle.retail.sim.client.swing.displayer.NumberDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.DisplayerTableCellEditor;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.KeyValuePairTableEditor;
import oracle.retail.sim.client.swing.tableeditor.PositiveIntegerTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.tableeditor.BooleanEnabledTableEditor;
import oracle.retail.sim.client.tableeditor.ItemDescriptionConfigTableEditor;
import oracle.retail.sim.client.tableeditor.SalesProcessingConfigTableEditor;
import oracle.retail.sim.client.tableeditor.SimEnumTableEditor;
import oracle.retail.sim.client.tableeditor.StandardUomTableEditor;
import oracle.retail.sim.client.tableeditor.StockCountUpdateSOHTableDisplayer;
import oracle.retail.sim.client.tableeditor.StockCountUpdateSOHTableEditor;
import oracle.retail.sim.client.tableeditor.TransferForceCloseIndicatorTableEditor;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.directdelivery.DirectDeliveryPreferredCurrency;
import oracle.retail.sim.common.security.UserSecurityMode;
import oracle.retail.sim.common.stockcount.StockCountSalesProcess;
import oracle.retail.sim.common.store.MultiSetOfBooksIndicator;

/********************************************************************************************************
 * System Administration Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SystemAdminPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 8160333234914338365L;

    private static final String TOPIC_FILTER_MODIFIED = "TopicFilter.modified";

    private final SystemAdminModel model = new SystemAdminModel();

    private final RComboBoxEditor topicEditor = new RComboBoxEditor("Topic");

    private final KeyValuePairDisplayer keyValueDisplayer = new KeyValuePairDisplayer();
    private final KeyValuePairTableEditor keyValueEditor = new KeyValuePairTableEditor(SimName.CONFIG_VALUE);
    private final SimTable configTable = new SimTable(new SystemAdminDefinition());
    private final SimTablePane configPane = new SimTablePane(configTable);

    public SystemAdminPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        topicEditor.setSizeType(EditorConstants.MEDIUM);
        topicEditor.setDisplayer(new TranslatedObjectDisplayer());
        topicEditor.setEmptyType(RComboBoxEmptyType.ALL);
        topicEditor.registerAction(this, TOPIC_FILTER_MODIFIED);

        keyValueDisplayer.addKeyDisplayer(SimConfigManager.DEFAULT_UOM, new StandardUomDisplayer());
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.SECURITY_AUTHENTICATION_METHOD, SimDisplayerFactory.createSimEnumDisplayer(UserSecurityMode.class));
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.DAYS_TO_HOLD_DISPATCHED_TRANSFER_BEFORE_SENDING_EMAIL_ALERT, new NumberDisplayer());
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.DIRECT_DELIVERY_PREFERRED_CURRENCY, SimDisplayerFactory.createSimEnumDisplayer(DirectDeliveryPreferredCurrency.class));
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.MULTI_SET_OF_BOOKS, SimDisplayerFactory.createSimEnumDisplayer(MultiSetOfBooksIndicator.class));
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.EXTERNAL_FINISHER_ENABLED, new BooleanEnabledDisplayer());
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.STOCK_COUNT_UPDATE_ALL_SOH, new StockCountUpdateSOHTableDisplayer());
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UNIT, SimDisplayerFactory.createSimEnumDisplayer(StockCountSalesProcess.class));
        keyValueDisplayer.addKeyDisplayer(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UA, SimDisplayerFactory.createSimEnumDisplayer(StockCountSalesProcess.class));

        int maxProductGroupLineItems = model.getMaxProductGroupLineItems();

        keyValueEditor.addClassEditor(Integer.class, new PositiveIntegerTableEditor(999));
        keyValueEditor.addClassEditor(Number.class, new PositiveIntegerTableEditor(999));
        keyValueEditor.addKeyEditor(SimConfigManager.DAYS_TO_HOLD_COMPLETED_STAGING_RECORDS, new PositiveIntegerTableEditor(999));
        keyValueEditor.addKeyEditor(SimConfigManager.DAYS_TO_HOLD_COMPLETED_UINS, new PositiveIntegerTableEditor(999));
        keyValueEditor.addKeyEditor(SimConfigManager.DAYS_TO_HOLD_UIN_AUDIT_INFORMATION, new PositiveIntegerTableEditor(999));
        keyValueEditor.addKeyEditor(SimConfigManager.DAYS_TO_HOLD_RESOLVED_UIN_EXCEPTIONS, new PositiveIntegerTableEditor(999));
        keyValueEditor.addKeyEditor(SimConfigManager.DAYS_TO_HOLD_RELATED_ITEMS, new PositiveIntegerTableEditor(999));
        keyValueEditor.addKeyEditor(SimConfigManager.DEFAULT_UOM, new StandardUomTableEditor());
        keyValueEditor.addKeyEditor(SimConfigManager.MULTI_SET_OF_BOOKS, new SimEnumTableEditor(MultiSetOfBooksIndicator.class));
        keyValueEditor.addKeyEditor(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UNIT, new SalesProcessingConfigTableEditor());
        keyValueEditor.addKeyEditor(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UA, new SalesProcessingConfigTableEditor());
        keyValueEditor.addKeyEditor(SimConfigManager.STOCK_COUNT_UPDATE_ALL_SOH, new StockCountUpdateSOHTableEditor());
        keyValueEditor.addKeyEditor(SimConfigManager.TSF_FORCE_CLOSE_IND, new TransferForceCloseIndicatorTableEditor());
        keyValueEditor.addKeyEditor(SimConfigManager.DISPLAY_ITEM_DESCRIPTION, new ItemDescriptionConfigTableEditor());
        keyValueEditor.addKeyEditor(SimConfigManager.DISPLAY_LENGTH_DIFF1, new PositiveIntegerTableEditor(100));
        keyValueEditor.addKeyEditor(SimConfigManager.DISPLAY_LENGTH_DIFF2, new PositiveIntegerTableEditor(100));
        keyValueEditor.addKeyEditor(SimConfigManager.DISPLAY_LENGTH_DIFF2, new PositiveIntegerTableEditor(100));
        keyValueEditor.addKeyEditor(SimConfigManager.DISPLAY_LENGTH_DIFF3, new PositiveIntegerTableEditor(100));
        keyValueEditor.addKeyEditor(SimConfigManager.DISPLAY_LENGTH_DIFF4, new PositiveIntegerTableEditor(100));
        keyValueEditor.addKeyEditor(SimConfigManager.DAYS_TO_SEND_EMAIL_ALERT_BEFORE_NOT_AFTER_DATE_FOR_RETURN_REQUESTS, new PositiveIntegerTableEditor(999));
        keyValueEditor.addKeyEditor(SimConfigManager.DIRECT_DELIVERY_PREFERRED_CURRENCY, new SimEnumTableEditor(DirectDeliveryPreferredCurrency.class));
        keyValueEditor.addKeyEditor(SimConfigManager.EXTERNAL_FINISHER_ENABLED, new BooleanEnabledTableEditor());
        keyValueEditor.addKeyEditor(SimConfigManager.PRODUCT_GROUP_ITEM_REQUEST_LIMIT, new PositiveIntegerTableEditor(1, maxProductGroupLineItems));
        keyValueEditor.addKeyEditor(SimConfigManager.PRODUCT_GROUP_SHELF_REPLENISHMENT_LIMIT, new PositiveIntegerTableEditor(1, maxProductGroupLineItems));
        keyValueEditor.addKeyEditor(SimConfigManager.PRODUCT_GROUP_PROBLEM_LINE_LIMIT, new PositiveIntegerTableEditor(1, maxProductGroupLineItems));
        keyValueEditor.addKeyEditor(SimConfigManager.PRODUCT_GROUP_UNIT_AMOUNT_LIMIT, new PositiveIntegerTableEditor(1, maxProductGroupLineItems));
        keyValueEditor.addKeyEditor(SimConfigManager.PRODUCT_GROUP_UNIT_LIMIT, new PositiveIntegerTableEditor(1, maxProductGroupLineItems));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_CONTAINER_LOOKUP, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_INV_ADJUSTMENT, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_ITEM_LOOKUP, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_PRICE_CHANGE, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_SUPPLIER_LOOKUP, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_STORE_SEQUENCE, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_STAGED_MESSAGE, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_TRANSACTION_HISTORY, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_UIN_RESOLUTION, new PositiveIntegerTableEditor(1, 999));
        keyValueEditor.addKeyEditor(SimConfigManager.SEARCH_LIMIT_CUSTOMER_ORDER_MGMT, new PositiveIntegerTableEditor(1, 999));

        configTable.setTableEditable(true);
        configTable.setDefaultEditor(Object.class, new DisplayerTableCellEditor(keyValueEditor));
        configTable.setColumnSortable(2, false);
    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topicEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 10, 0));
        mainPanel.add(configPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return configTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        topicEditor.setItems(model.getSystemAdminTopics());
        configTable.setRows(model.findAvailableConfigurationOptions(null));
    }

    public void stop() {
        configTable.stopEditing();
    }

    public void assignFocusInScreen() {
        assignFocusInScreen(topicEditor);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(TOPIC_FILTER_MODIFIED)) {
                doTopicFilterModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doTopicFilterModified() throws Exception {
        configTable.stopEditing();
        configTable.setRows(model.findAvailableConfigurationOptions((String) topicEditor.getSelectedItem()));
    }

    public boolean handleSave() throws Exception {
        configTable.stopEditing();
        showScreenBusy(true);
        if (model.validateConfigurationOptions()) {
            RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), "Confirmation");
            dialog.setMessage(CommonMessageText.ADMIN_CONFIRM);
            dialog.setYesNoType();
            if (dialog.getConfirmation()) {
                model.saveConfigurationOptions();
                return true;
            }
        }
        showScreenBusy(false);
        return false;
    }

    /****************************************************************************************************
     * System Admin Table Definition
     ***************************************************************************************************/

    private class SystemAdminDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return ConfigurationOption.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<>(2);
            sortAttributes.add(new SimTableSortAttribute("configTopic"));
            sortAttributes.add(new SimTableSortAttribute("configKey"));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(3);
            attributes.add(new SimTableAttribute("Topic", "configTopic", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Option", "configKey", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Value", "value", keyValueDisplayer, keyValueEditor, true));
            return attributes;
        }
    }
}
