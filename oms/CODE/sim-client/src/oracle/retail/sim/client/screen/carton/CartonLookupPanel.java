package oracle.retail.sim.client.screen.carton;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;

/********************************************************************************************************
 * Carton Lookup Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CartonLookupPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -528347461680621664L;

    private static final String CARTON_SELECTED = "Carton.selected";

    private CartonLookupModel model = new CartonLookupModel();

    private RTextFieldEditor cartonIdEditor = new RTextFieldEditor("Container ID");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");
    private RTextFieldEditor uinEditor = new RTextFieldEditor("UIN");

    private SimTable cartonTable = new SimTable(new CartonTableDefinition());
    private SimTablePane cartonPane = new SimTablePane(cartonTable);

    public CartonLookupPanel() {
        initializeEditors();
        layoutScreen();
    }

    private void initializeEditors() {
        cartonIdEditor.setIdentifier(SimName.CARTON_ID);

        searchLimitEditor.setIdentifier(SimName.CARTON_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setRequired(true);

        cartonTable.setTableEditable(false);
        cartonTable.setSingleRowSelectionMode();
        cartonTable.registerDoubleClickAction(this, CARTON_SELECTED);

        uinEditor.setIdentifier(SimName.SERIAL_NUMBER);
        uinEditor.setSizeType(EditorConstants.MEDIUM);
    }

    private void layoutScreen() {
        REditorPanel editorPanel = new REditorPanel(2, 2);
        editorPanel.add(cartonIdEditor);
        editorPanel.add(searchLimitEditor);
        if (model.isSerialNumberProcessingEnabled()) {
            editorPanel.add(uinEditor);
        }
        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(editorPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(cartonPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return cartonTable;
    }

    public void start() {
    }

    public void stop() {
        model.clearState();
    }

    public void assignFocusInScreen() {
        assignFocusInScreen(cartonIdEditor);
    }

    /****************************************************************************************************
     * Process Search
     ***************************************************************************************************/

    public void handleSearch() throws Exception {
        validateRequiredContent();
        loadWarehouseDeliveryCartonsByFilter();
        if (cartonTable.isEmpty()) {
            displayWarning(CommonMessageText.NO_RECORDS_FOUND);
            assignFocusInScreen();
        } else if (cartonTable.getRowCount() == 1) {
            storeWarehouseDeliveryCartonDetail((WarehouseDeliveryCartonVO) cartonTable.getAllRowData().get(0));
        }
    }

    private void loadWarehouseDeliveryCartonsByFilter() throws Exception {
        WarehouseDeliveryCartonQueryFilter filter = BOFactory.createWarehouseDeliveryCartonQueryFilter();
        filter.setLoadCountData(true);
        filter.setSearchLimit(searchLimitEditor.getIntegerValue(), SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        filter.setExternalId(cartonIdEditor.getText());
        filter.setUinValue(uinEditor.getText());

        List<WarehouseDeliveryCartonVO> cartonVOs = model.findWarehouseDeliveryCartonVOs(filter);
        if (cartonVOs.isEmpty()) {
            cartonTable.clearRows();
        } else {
            cartonTable.setRows(cartonVOs);
        }
    }

    /****************************************************************************************************
     * Handle Screen Actions - Table Double Click
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(CARTON_SELECTED)) {
                storeWarehouseDeliveryCartonDetail((WarehouseDeliveryCartonVO) cartonTable.getSelectedRowData());
            }
        } catch (Throwable e) {
            displayException(e);
        }
    }

    private void storeWarehouseDeliveryCartonDetail(WarehouseDeliveryCartonVO cartonVO) throws Exception {
        model.storeWarehouseDeliveryCarton(cartonVO);
        navigate(SimScreenName.CARTON_DETAIL_SCREEN);
    }

    /****************************************************************************************************
     * CARTON TABLE DEFINITION
     ***************************************************************************************************/

    private class CartonTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return WarehouseDeliveryCartonVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(WarehouseDeliveryProperty.EXTERNAL_ID));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> columns = new ArrayList<SimTableAttribute>();
            columns.add(new SimTableAttribute("Container ID", WarehouseDeliveryProperty.EXTERNAL_ID));
            columns.add(new SimTableAttribute("Status", WarehouseDeliveryProperty.STATUS, new TranslatedObjectDisplayer()));
            columns.add(new SimTableAttribute("From", WarehouseDeliveryProperty.SOURCE, new IdNameDisplayer()));
            columns.add(new SimTableAttribute("Expected Cases", WarehouseDeliveryProperty.NUMBER_OF_CASES_EXPECTED));
            columns.add(new SimTableAttribute("Receive Date", WarehouseDeliveryProperty.COMPLETE_DATE, new DateTimeDisplayer()));
            columns.add(new SimTableAttribute("Customer Order", WarehouseDeliveryProperty.FULFILLMENT_ORDER_RELATED, new BooleanDisplayer()));
            return columns;
        }
    }
}
