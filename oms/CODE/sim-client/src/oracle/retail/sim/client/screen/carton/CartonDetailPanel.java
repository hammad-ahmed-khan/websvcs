package oracle.retail.sim.client.screen.carton;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.screen.warehousedelivery.WarehouseDeliveryUinDialog;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;

/********************************************************************************************************
 * Carton Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CartonDetailPanel extends ScreenPanel {
    private static final long serialVersionUID = 7416786169003840715L;

    private CartonDetailModel model = new CartonDetailModel();

    private RDisplayLabelEditor sourceEditor = new RDisplayLabelEditor("From");
    private RDisplayLabelEditor asnIdEditor = new RDisplayLabelEditor("ASN ID");
    private RDisplayLabelEditor cartonIdEditor = new RDisplayLabelEditor("Container ID");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor etaEditor = new RDisplayLabelEditor("ETA");
    private RDisplayLabelEditor receiveDateEditor = new RDisplayLabelEditor("Receive Date");
    private RDisplayLabelEditor expectedCasesEditor = new RDisplayLabelEditor("Expected Cases");
    private RDisplayLabelEditor receivedCasesEditor = new RDisplayLabelEditor("Received Cases");
    private RDisplayLabelEditor damagedLinesEditor = new RDisplayLabelEditor("Damaged Lines");

    private SimTable lineItemTable = new SimTable(new CartonItemDefinition());

    public CartonDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        sourceEditor.setDisplayer(new IdNameDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        etaEditor.setDataType(DataTypeConstants.DATE_SHORT);
        receiveDateEditor.setDisplayer(new DateTimeDisplayer());
        expectedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        receivedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        damagedLinesEditor.setDataType(DataTypeConstants.INTEGER);
        if (model.isSerialNumberProcessingEnabled() && model.getWarehouseDeliveryCarton().isSerialNumberRequired()) {
            lineItemTable.setColumnSize(WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT, SimTable.LABEL_WIDTH);
        }
    }

    private void layoutScreen() {
        REditorPanel detailPanel = new REditorPanel(3, 3);
        detailPanel.add(sourceEditor);
        detailPanel.add(asnIdEditor);
        detailPanel.add(cartonIdEditor);
        detailPanel.add(statusEditor);
        detailPanel.add(etaEditor);
        detailPanel.add(receiveDateEditor);
        detailPanel.add(expectedCasesEditor);
        detailPanel.add(receivedCasesEditor);
        detailPanel.add(damagedLinesEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);
        SimTablePane lineItemPane = new SimTablePane(lineItemTable);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(detailPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return lineItemTable;
    }

    public void start() {
        try {
            populateScreen();
        } catch (Throwable t) {
            displayException(t);
        }
    }

    public void stop() {
        model.clearState();
    }

    private void populateScreen() {
        WarehouseDeliveryCarton carton = model.getWarehouseDeliveryCarton();
        WarehouseDeliveryCartonVO cartonVO = model.getWarehouseDeliveryCartonVO();
        sourceEditor.setData(cartonVO.getSource());
        asnIdEditor.setData(cartonVO.getAsnId());
        cartonIdEditor.setData(cartonVO.getExternalId());
        statusEditor.setData(cartonVO.getStatus());
        etaEditor.setData(cartonVO.getExpectedArrivalDate());
        receiveDateEditor.setData(cartonVO.getCompleteDate());
        expectedCasesEditor.setData(carton.getNumberOfCasesExpected().intValue());
        receivedCasesEditor.setData(carton.getNumberOfCasesReceived().intValue());
        damagedLinesEditor.setData(carton.getNumberOfLineItemsDamaged());

        lineItemTable.setRows(model.getLineItemWrappers(carton));
    }

    /****************************************************************************************************
     * Carton Item Definition
     ***************************************************************************************************/

    private class CartonItemDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return CartonLineItemWrapper.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList(WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT);
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Item", WarehouseDeliveryProperty.ITEM_ID));
            attributes.add(new SimTableAttribute("Description", WarehouseDeliveryProperty.STOCK_ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("UOM", WarehouseDeliveryProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Expected", WarehouseDeliveryProperty.QUANTITY_EXPECTED_UOM));
            attributes.add(new SimTableAttribute("Received", WarehouseDeliveryProperty.QUANTITY_RECEIVED_UOM));
            attributes.add(new SimTableAttribute("Damaged", WarehouseDeliveryProperty.QUANTITY_DAMAGED_UOM));
            attributes.add(new SimTableAttribute("Variance", WarehouseDeliveryProperty.QUANTITY_VARIANCE_UOM));
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT, new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {
        public void popupDialog(Object data) {
            WarehouseDeliveryUinDialog dialog = new WarehouseDeliveryUinDialog(FunctionalArea.MANUAL);
            dialog.setSerialNumberLineItemWrapper((SerialNumberLineItemWrapper) data);
            dialog.setVisible(true);
            lineItemTable.updateRow(data);
        }
    }
}
