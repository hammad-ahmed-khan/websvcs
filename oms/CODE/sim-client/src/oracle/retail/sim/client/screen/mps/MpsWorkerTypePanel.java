package oracle.retail.sim.client.screen.mps;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.DurationDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.mps.MpsWorkerTypeProperty;
import oracle.retail.sim.common.mps.MpsWorkerTypeVO;

/********************************************************************************************************
 * MPS Worker Type Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsWorkerTypePanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 4613840833401913459L;

    private MpsWorkerTypeModel model = new MpsWorkerTypeModel();
    private SimTable workerTypeTable = new SimTable(new MpsWorkerTypeTableDefinition());

    /****************************************************************************************************
     * Initialize Screen
     ***************************************************************************************************/

    public MpsWorkerTypePanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        workerTypeTable.setMultipleRowSelectionMode();
        workerTypeTable.setTableEditable(false);
        workerTypeTable.setColumnSize(MpsWorkerTypeProperty.MESSAGE_DIRECTION, EditorConstants.COLUMN_LABEL_WIDTH);
        workerTypeTable.setColumnSize(MpsWorkerTypeProperty.ENABLED, EditorConstants.COLUMN_LABEL_WIDTH);
        workerTypeTable.setColumnSize(MpsWorkerTypeProperty.PENDING_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);
        workerTypeTable.setColumnSize(MpsWorkerTypeProperty.RETRY_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);
        workerTypeTable.setColumnSize(MpsWorkerTypeProperty.FAILED_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutScreen() {
        SimTablePane workerTypePane = new SimTablePane(workerTypeTable);
        setContentPane(workerTypePane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return workerTypeTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        reloadWorkerTypes();
    }

    /****************************************************************************************************
     * HANDLE REFRESH
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        reloadWorkerTypes();
    }

    /****************************************************************************************************
     * HANDLE START
     ***************************************************************************************************/

    public void handleStart() throws Exception {
        List<MpsWorkerTypeVO> admins = workerTypeTable.getAllSelectedRowData();
        if (admins.isEmpty()) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        model.start(admins);
        reloadWorkerTypes();
    }

    /****************************************************************************************************
     * HANDLE STOP
     ***************************************************************************************************/

    public void handleStop() throws Exception {
        List<MpsWorkerTypeVO> admins = workerTypeTable.getAllSelectedRowData();
        if (admins.isEmpty()) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        model.stop(admins);
        reloadWorkerTypes();
    }

    /****************************************************************************************************
     * HANDLE SAVE
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
    }

    /****************************************************************************************************
     * Method To Reload The Worker Types
     ***************************************************************************************************/

    private void reloadWorkerTypes() throws Exception {
        workerTypeTable.setRows(model.findWorkerTypeVOs());
    }

    /****************************************************************************************************
     * MPS Worker Type Definition
     ***************************************************************************************************/

    private class MpsWorkerTypeTableDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return MpsWorkerTypeVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute(MpsWorkerTypeProperty.MESSAGE_DIRECTION));
            attributes.add(new SimTableSortAttribute(MpsWorkerTypeProperty.MESSAGE_FAMILY));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Worker Type", MpsWorkerTypeProperty.MESSAGE_FAMILY));
            attributes.add(new SimTableAttribute("In/Out", MpsWorkerTypeProperty.MESSAGE_DIRECTION, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Active", MpsWorkerTypeProperty.ENABLED, new SimTableCheckBoxRenderer()));
            attributes.add(new SimTableAttribute("Last Update", MpsWorkerTypeProperty.DURATION_SINCE_LAST_UPDATE, new DurationDisplayer()));
            attributes.add(new SimTableAttribute("Last New Msg", MpsWorkerTypeProperty.DURATION_SINCE_LAST_CREATE, new DurationDisplayer()));
            attributes.add(new SimTableAttribute("Pending", MpsWorkerTypeProperty.PENDING_COUNT));
            attributes.add(new SimTableAttribute("Retry", MpsWorkerTypeProperty.RETRY_COUNT));
            attributes.add(new SimTableAttribute("Fail", MpsWorkerTypeProperty.FAILED_COUNT));
            return attributes;
        }
    }
}
