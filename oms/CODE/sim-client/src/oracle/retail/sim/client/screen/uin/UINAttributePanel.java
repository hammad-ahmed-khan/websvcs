package oracle.retail.sim.client.screen.uin;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.AttributeComparator;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.tableeditor.CaptureTimeTableEditor;
import oracle.retail.sim.client.tableeditor.TicketTypeFormatTableEditor;
import oracle.retail.sim.client.tableeditor.UINLabelTableEditor;
import oracle.retail.sim.client.tableeditor.UINTypeTableEditor;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.uin.UINCaptureTime;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * UIN Attribute Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINAttributePanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -2648705992673915448L;

    private UINAttributeModel model = new UINAttributeModel();

    private RComboBoxEditor departmentEditor = new RComboBoxEditor("Dept");
    private RComboBoxEditor classEditor = new RComboBoxEditor("Class");

    private CaptureTimeTableEditor captureTimeTableEditor = new CaptureTimeTableEditor();
    private UINTypeTableEditor typeTableEditor = new UINTypeTableEditor();
    private UINLabelTableEditor labelTableEditor = new UINLabelTableEditor();

    private TicketTypeFormatTableEditor ticketTypeFormatEditor = new TicketTypeFormatTableEditor();

    private SimTable attributeTable = new SimTable(new UINAttributeDefinition());
    private SimTablePane attributePane = new SimTablePane(attributeTable);

    private static final String TICKET_FORMAT_PROPERTY = "ticketTypeFormat";
    private static final String TYPE_PROPERTY = "type";
    private static final String LABEL_PROPERTY = "label";
    private static final String CAPTURE_TIME_PROPERTY = "captureTime";

    private static final String DEPT_SELECTION = "Dept.selection";
    private static final String CLASS_SELECTION = "Class.selection";
    private static final String ITEM_SELECTED = "Item.selected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public UINAttributePanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        departmentEditor.setDisplayer(new DualAttributeDisplayer("departmentId", "departmentName"));
        departmentEditor.setComparator(new AttributeComparator("departmentId"));
        departmentEditor.setSizeType(EditorConstants.LARGE);
        departmentEditor.registerAction(this, DEPT_SELECTION);

        classEditor.setDisplayer(new DualAttributeDisplayer("classId", "className"));
        classEditor.setComparator(new AttributeComparator("classId"));
        classEditor.registerAction(this, CLASS_SELECTION);

        attributeTable.setTableEditable(true);
        attributeTable.setSingleRowSelectionMode();
        attributeTable.registerSingleClickAction(this, ITEM_SELECTED);

        ticketTypeFormatEditor.setDisplayer(new TranslatedObjectDisplayer());



    }

    private void layoutPanel() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(departmentEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(attributePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return attributeTable;
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        loadDepartments();
        populationScreen();
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        for (int i = 0; i < attributeTable.getRowCount(); i++) {
            UINStoreDeptWrapper storeDeptWrapper = (UINStoreDeptWrapper) attributeTable.getRowData(i);
            if (!model.isValidUinStoreDept(storeDeptWrapper)) {
                boolean isTypeNull = storeDeptWrapper.getType() == null;
                boolean isLabelNull = storeDeptWrapper.getLabel() == null;
                boolean isCaptureTimeNull = storeDeptWrapper.getCaptureTime() == null;
                boolean isTicketTypeFormatNull = storeDeptWrapper.getType() == UINType.AGSN && storeDeptWrapper.getTicketTypeFormat() == null;

                displayError(UIMessageText.MISSING_REQUIRED_FIELDS, buildFailedColumnsList(isTypeNull, isLabelNull, isCaptureTimeNull, isTicketTypeFormatNull));

                attributeTable.setRowSelectionInterval(i, i);

                if (isTypeNull) {
                    attributeTable.editCellInRow(TYPE_PROPERTY, i);
                } else if (isLabelNull) {
                    attributeTable.editCellInRow(LABEL_PROPERTY, i);
                } else if (isCaptureTimeNull) {
                    attributeTable.editCellInRow(CAPTURE_TIME_PROPERTY, i);
                } else if (storeDeptWrapper.getType() == UINType.AGSN && storeDeptWrapper.getTicketTypeFormat() == null) {
                    attributeTable.editCellInRow(TICKET_FORMAT_PROPERTY, i);
                }
                return false;
            }
        }
        model.updateStoreDepartments();
        return true;
    }

    private String buildFailedColumnsList(boolean isTypeNull, boolean isLabelNull, boolean isCaptureTimeNull, boolean isTicketTypeFormatNull) {
        StringBuilder builder = new StringBuilder();
        if (isTypeNull) {
            builder.append(Translator.getText("UIN Type"));
        }
        if (isLabelNull) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(Translator.getText("UIN Label"));
        }
        if (isCaptureTimeNull) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(Translator.getText("Capture Time"));
        }
        if (isTicketTypeFormatNull) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(Translator.getText("Ticket Type"));
        }
        return builder.toString();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(DEPT_SELECTION)) {
                loadClasses();
            }
        } catch (Throwable exception) {
            UILog.debug(getClass(), UIMessageText.UNABLE_TO_LOAD_HIERARCHY);
        }
    }

    /****************************************************************************************************
     * Helper methods to populate screen and load initial data.
     ***************************************************************************************************/

    private void populationScreen() throws Exception {
        attributeTable.setRows(model.getStoreDepartments(null));
        attributeTable.setTableEditable(true);
        typeTableEditor.setTypes(Arrays.asList(UINType.values()));
        labelTableEditor.setLabels(model.getLabels());
        captureTimeTableEditor.setCaptureTimes(Arrays.asList(UINCaptureTime.values()));
        ticketTypeFormatEditor.setTicketTypeFormats(model.getTicketTypeFormats());




    }

    private void loadDepartments() throws Exception {
        departmentEditor.setActionsEnabled(false);
        departmentEditor.setItems(model.findDepartments());
        departmentEditor.setActionsEnabled(true);
        if (departmentEditor.isEmpty()) {
            UILog.info(getClass(), UIMessageText.DEPARTMENTS_NOT_FOUND);
        }
        classEditor.clear();
        classEditor.setEnabled(false);
    }

    private void loadClasses() throws Exception {
        attributeTable.stopEditing();
        String departmentName = null;
        MdseHierarchyNode departmentNode = (MdseHierarchyNode) departmentEditor.getSelectedItem();
        if (departmentNode != null) {
            departmentName = departmentNode.getDepartmentName();
        }
        attributeTable.setRows(model.getStoreDepartments(departmentName));
    }

    /****************************************************************************************************
     * UIN Attribute Table Definition
     ***************************************************************************************************/

    private class UINAttributeDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return UINStoreDeptWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<SimTableSortAttribute>(2);
            sortAttributes.add(new SimTableSortAttribute("fullDepartmentName"));
            sortAttributes.add(new SimTableSortAttribute("fullClassName"));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(7);
            attributes.add(new SimTableAttribute("Department", "fullDepartmentName"));
            attributes.add(new SimTableAttribute("Class", "fullClassName"));
            attributes.add(new SimTableAttribute("UIN Type", TYPE_PROPERTY, new TranslatedObjectDisplayer(), typeTableEditor));
            attributes.add(new SimTableAttribute("UIN Label", LABEL_PROPERTY, new TranslatedObjectDisplayer(), labelTableEditor));
            attributes.add(new SimTableAttribute("Ticket Type", TICKET_FORMAT_PROPERTY, new TranslatedObjectDisplayer(), ticketTypeFormatEditor));
            attributes.add(new SimTableAttribute("Capture Time", CAPTURE_TIME_PROPERTY, new TranslatedObjectDisplayer(), captureTimeTableEditor));
            attributes.add(new SimTableAttribute("External System Create UIN", "externalCreateAllowed", new SimTableCheckBoxRenderer()));
            return attributes;
        }
    }
}
