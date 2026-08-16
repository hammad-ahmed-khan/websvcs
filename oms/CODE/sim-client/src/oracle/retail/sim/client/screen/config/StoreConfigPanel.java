package oracle.retail.sim.client.screen.config;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.BooleanDirectShipValidateDisplayer;
import oracle.retail.sim.client.displayer.SimDisplayerFactory;
import oracle.retail.sim.client.swing.displayer.KeyValuePairDisplayer;
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
import oracle.retail.sim.client.swing.tableeditor.PositiveNumberTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.tableeditor.AutoReceiveOptionsFinisherTableEditor;
import oracle.retail.sim.client.tableeditor.AutoReceiveOptionsWarehouseTableEditor;
import oracle.retail.sim.client.tableeditor.DefaultCustomerOrderPickingOptionsTableEditor;
import oracle.retail.sim.client.tableeditor.DefaultShipmentCarrierRoleTableEditor;
import oracle.retail.sim.client.tableeditor.DirectShipValidateTableEditor;
import oracle.retail.sim.client.tableeditor.GenerateBinsOptionsTableEditor;
import oracle.retail.sim.client.tableeditor.HandheldPickingModeOptionsTableEditor;
import oracle.retail.sim.client.tableeditor.ItemBasketTableEditor;
import oracle.retail.sim.client.tableeditor.ShipmentWeightUomTableEditor;
import oracle.retail.sim.client.tableeditor.SimEnumTableEditor;
import oracle.retail.sim.client.tableeditor.StockCountTimeframeTableEditor;
import oracle.retail.sim.client.tableeditor.StoreAutoReceiveOptionsTableEditor;
import oracle.retail.sim.client.tableeditor.StoreToStoreCarrierDefaultOptionsTableEditor;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.directdelivery.DirectDeliveryInvoiceEntryType;

/********************************************************************************************************
 * Store Configuration Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreConfigPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -7477582607551610360L;

    private static final String TOPIC_FILTER_MODIFIED = "TopicFilter.modified";

    private StoreConfigModel model = new StoreConfigModel();

    private RComboBoxEditor topicEditor = new RComboBoxEditor("Topic");

    private KeyValuePairDisplayer keyValueDisplayer = new KeyValuePairDisplayer();
    private KeyValuePairTableEditor keyValueEditor = new KeyValuePairTableEditor(SimName.CONFIG_VALUE);
    private SimTable configTable = new SimTable(new StoreConfigDefinition());
    private SimTablePane configPane = new SimTablePane(configTable);

    public StoreConfigPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        topicEditor.setSizeType(EditorConstants.MEDIUM);
        topicEditor.setDisplayer(new TranslatedObjectDisplayer());
        topicEditor.setEmptyType(RComboBoxEmptyType.ALL);
        topicEditor.registerAction(this, TOPIC_FILTER_MODIFIED);

        keyValueEditor.addClassEditor(Number.class, new PositiveNumberTableEditor(true));
        keyValueEditor.addKeyEditor(StoreConfigKeys.DEFAULT_CUSTOMER_ORDER_PICKING_METHOD, new DefaultCustomerOrderPickingOptionsTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.FUL_ORDER_DISPATCH_VALIDATE, new DirectShipValidateTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.GENERATE_BINS, new GenerateBinsOptionsTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.HANDHELD_PICKING_MODE, new HandheldPickingModeOptionsTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.ITEM_BASKET_PRINTING, new ItemBasketTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.STOCK_COUNT_DEFAULT_TIMEFRAME, new StockCountTimeframeTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.EXTERNAL_FINISHER_AUTO_RECEIVE, new AutoReceiveOptionsFinisherTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.STORE_AUTO_RECEIVE, new StoreAutoReceiveOptionsTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.STORE_TO_STORE_CARRIER_DEFAULT, new StoreToStoreCarrierDefaultOptionsTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.WAREHOUSE_AUTO_RECEIVE, new AutoReceiveOptionsWarehouseTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.WAREHOUSE_AUTO_RECEIVE_NUMBER_OF_DAYS, new PositiveNumberTableEditor(true, 99d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.STORE_AUTO_RECEIVE_NUMBER_OF_DAYS, new PositiveNumberTableEditor(true, 99d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.EXTERNAL_FINISHER_AUTO_RECEIVE_NUMBER_OF_DAYS, new PositiveNumberTableEditor(true, 99d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.REPLENISHMENT_END_OF_DAY_MAX_FILL_PCT, new PositiveNumberTableEditor(true, 100d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.REPLENISHMENT_ITEM_OUT_OF_STOCK_PCT, new PositiveNumberTableEditor(true, 100d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.REPLENISHMENT_WITHIN_DAY_MAX_FILL_PCT, new PositiveNumberTableEditor(true, 100d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.REPLENISHMENT_ITEM_OUT_OF_STOCK_STANDARD_UOM, new PositiveNumberTableEditor(true, true, 9999d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.RETURN_VALIDATE_DISPATCH, new DirectShipValidateTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.TRANSFER_DISPATCH_VALIDATE, new DirectShipValidateTableEditor());
        keyValueEditor.addKeyEditor(StoreConfigKeys.DEFAULT_NUMBER_OF_BINS, new PositiveNumberTableEditor(false, 1d, 999d));
        keyValueEditor.addKeyEditor(StoreConfigKeys.DIRECT_DELIVERY_INVOICE_ENTRY, new SimEnumTableEditor(DirectDeliveryInvoiceEntryType.class));
        keyValueEditor.addKeyEditor(StoreConfigKeys.RETURN_CARRIER_DEFAULT, new DefaultShipmentCarrierRoleTableEditor());

        keyValueDisplayer.addKeyDisplayer(StoreConfigKeys.DIRECT_DELIVERY_INVOICE_ENTRY, SimDisplayerFactory.createSimEnumDisplayer(DirectDeliveryInvoiceEntryType.class));
        keyValueDisplayer.addKeyDisplayer(StoreConfigKeys.FUL_ORDER_DISPATCH_VALIDATE, new BooleanDirectShipValidateDisplayer());
        keyValueDisplayer.addKeyDisplayer(StoreConfigKeys.RETURN_VALIDATE_DISPATCH, new BooleanDirectShipValidateDisplayer());
        keyValueDisplayer.addKeyDisplayer(StoreConfigKeys.TRANSFER_DISPATCH_VALIDATE, new BooleanDirectShipValidateDisplayer());

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

    public void start() throws Exception {
        topicEditor.setItems(model.getStoreAdminTopics());
        keyValueEditor.addKeyEditor(StoreConfigKeys.MANIFEST_WEIGHT_UOM, new ShipmentWeightUomTableEditor(model.getWeightUoms()));
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
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doTopicFilterModified() throws Exception {
        configTable.stopEditing();
        configTable.setRows(model.findAvailableConfigurationOptions((String) topicEditor.getSelectedItem()));
    }

    protected void handleSave() throws Exception {
        configTable.stopEditing();
        model.saveConfigurationOptions();
    }

    /****************************************************************************************************
     * Table Definition
     ***************************************************************************************************/

    private class StoreConfigDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return ConfigurationOption.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<SimTableSortAttribute>(2);
            sortAttributes.add(new SimTableSortAttribute("configTopic"));
            sortAttributes.add(new SimTableSortAttribute("configKey"));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(3);
            attributes.add(new SimTableAttribute("Topic", "configTopic", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Option", "configKey", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Value", "value", keyValueDisplayer, keyValueEditor, true));
            return attributes;
        }
    }
}
