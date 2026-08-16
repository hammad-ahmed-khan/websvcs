package oracle.retail.sim.client.screen.tolerances;

import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.NumberDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.PositiveNumberTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;
import oracle.retail.sim.common.tolerance.ToleranceTopic;


/********************************************************************************************************
 * Tolerances Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TolerancesAdminPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -8689124692810482722L;

    private TolerancesAdminModel model = new TolerancesAdminModel();

    private RComboBoxEditor topicEditor = new RComboBoxEditor("Topic");

    private static final String TOPIC_MODIFIED = "Topic.modified";

    private SimTable adhocAdminTable = new SimTable(new ToleranceDefinition());
    private SimTablePane adhocAdminPane = new SimTablePane(adhocAdminTable);

    public TolerancesAdminPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        topicEditor.setDisplayer(new TranslatedObjectDisplayer());
        topicEditor.registerAction(this, TOPIC_MODIFIED);
        topicEditor.setSizeType(EditorConstants.LARGE);
        topicEditor.setItems(SimEnumUtility.findAllToleranceTopics());
        topicEditor.setSelectionRequired(true);
        topicEditor.setSelectedItem(ToleranceTopic.ADHOC_STOCK_COUNT);

        adhocAdminTable.setTableEditable(true);
    }

    private void layoutScreen() {
        REditorPanel topicPanel = new REditorPanel(1);
        topicPanel.add(topicEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topicPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(adhocAdminPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return adhocAdminTable;
    }

    /****************************************************************************************************
     * Load Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadData();
        populateScreen();
    }

    private void populateScreen() throws Exception {
        ToleranceTopic topic = (ToleranceTopic) topicEditor.getSelectedItem();
        if(topic == ToleranceTopic.ADHOC_STOCK_COUNT){
            adhocAdminTable.setRows(model.getAdhocAdminData());
        } else {
            adhocAdminTable.setRows(model.getPickAdminData());
        }
    }

    protected void handleSave() throws Exception {
        model.updateAdhocAdminData();
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(TOPIC_MODIFIED)) {
                doTopicModified();
            }
        } catch (Throwable e) {
            displayException(e);
        }
    }

    private void doTopicModified() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * AD Hoc Stock Count Table Definition
     ***************************************************************************************************/

    private class ToleranceDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ToleranceAdmin.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("fullDepartmentName"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(4);
            attributes.add(new SimTableAttribute("Dept", "fullDepartmentName"));
            attributes.add(new SimTableAttribute("Class", "fullClassName"));
            attributes.add(new SimTableAttribute("Variance %", "variancePercent", new NumberDisplayer(), new PositiveNumberTableEditor(true, true, 99999999)));
            attributes.add(new SimTableAttribute("Variance Standard UOM", "varianceCount", new NumberDisplayer(), new PositiveNumberTableEditor(true, true, 99999999)));
            return attributes;
        }
    }
}
