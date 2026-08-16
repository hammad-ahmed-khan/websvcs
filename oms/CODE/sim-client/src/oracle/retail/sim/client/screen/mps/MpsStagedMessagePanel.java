package oracle.retail.sim.client.screen.mps;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.mps.MpsStagedMessage;
import oracle.retail.sim.common.mps.MpsStagedMessageProperty;
import oracle.retail.sim.common.mps.MpsStagedMessageVO;

/********************************************************************************************************
 * MPS Staged Message Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsStagedMessagePanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -8438136905853098482L;

    private static final String STAGING_RECORD_SELECTED = "MpsStagedMessage.selected";
    private static final String STAGING_FILTER_SELECTED = "MpsStagedMessage.filterSelected";

    private MpsStagedMessageModel model = new MpsStagedMessageModel();
    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();
    private SimTable stagedMessageTable = new SimTable(new MpsStagedMessageTableDefinition());
    private MpsStagedMessageFilterDialog filterDialog = new MpsStagedMessageFilterDialog();

    /****************************************************************************************************
     * Initialize Screen
     ***************************************************************************************************/

    public MpsStagedMessagePanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, STAGING_FILTER_SELECTED);
        filterDialog.addREventListener(this);

        stagedMessageTable.setColumnSize(MpsStagedMessageProperty.ID, EditorConstants.COLUMN_LABEL_WIDTH);
        stagedMessageTable.setColumnSize(MpsStagedMessageProperty.RETRY_COUNT, EditorConstants.COLUMN_LABEL_WIDTH);
        stagedMessageTable.registerDoubleClickAction(this, STAGING_RECORD_SELECTED);
        stagedMessageTable.setTableEditable(false);
    }

    private void layoutScreen() {
        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        SimTablePane stagedMessagePane = new SimTablePane(stagedMessageTable);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(stagedMessagePane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return stagedMessageTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        doStagingFilterModified();
    }

    /****************************************************************************************************
     * HANDLE DELETE
     ***************************************************************************************************/

    public void handleDelete() throws Exception {
        List<MpsStagedMessageVO> stagedMessageVOs = stagedMessageTable.getAllSelectedRowData();
        if (stagedMessageVOs.isEmpty()) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (!RConfirmUtility.confirm("Delete Confirmation", CommonMessageText.STAGED_MESSAGE_DELETE_CONFIRM)) {
            return;
        }
        model.deleteStagedMessages(stagedMessageVOs);
        handleRefresh();
    }

    /****************************************************************************************************
     * HANDLE REFRESH
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        reloadStagedMessageVOs();
    }

    /****************************************************************************************************
     * HANDLE RESET
     ***************************************************************************************************/

    public void handleReset() throws Exception {
        List<MpsStagedMessageVO> stagedMessageVOs = stagedMessageTable.getAllSelectedRowData();
        if (stagedMessageVOs.isEmpty()) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        resetStagedMessageVOs(stagedMessageVOs);
        handleRefresh();
    }

    /****************************************************************************************************
     * Method To Reload The Staged Messages
     ***************************************************************************************************/

    private void reloadStagedMessageVOs() throws Exception {
        stagedMessageTable.setRows(model.getStagedMessageVOs());
    }

    /****************************************************************************************************
     * Method To Reset The Staged Messages
     ***************************************************************************************************/

    private void resetStagedMessageVOs(List<MpsStagedMessageVO> stagedMessageVOs) throws Exception {
        model.resetStagedMessages(stagedMessageVOs);
        handleRefresh();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        try {
            switch (event.getEventCommand()) {
                case STAGING_RECORD_SELECTED:
                    doStagedMessageSelected();
                    break;
                case STAGING_FILTER_SELECTED:
                    doStagingFilterSelected();
                    break;
                case SimClientStateKey.STAGED_MESSAGE_FILTER_MODIFIED:
                    doStagingFilterModified();
                    break;
                case SimClientStateKey.STAGED_MESSAGE_MODIFIED:
                    handleRefresh();
                    break;
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Staging Message Selected
     ***************************************************************************************************/

    private void doStagingFilterSelected() {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    /****************************************************************************************************
     * Staging Message Selected
     ***************************************************************************************************/

    private void doStagedMessageSelected() throws Exception {
        MpsStagedMessageVO stagedMessageVO = (MpsStagedMessageVO) stagedMessageTable.getSelectedRowData();
        MpsStagedMessage stagedMessage = model.getStagedMessage(stagedMessageVO);
        if (stagedMessage != null) {
            MpsStagedMessageDialog dialog = new MpsStagedMessageDialog();
            dialog.addREventListener(this);
            dialog.setStagedMessage(stagedMessage);
            dialog.setVisible(true);
        } else {
            handleRefresh();
        }
    }

    /****************************************************************************************************
     * Staging Message filter Modified
     ***************************************************************************************************/

    private void doStagingFilterModified() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        stagedMessageTable.setRows(model.getStagedMessageVOs());
    }

    /****************************************************************************************************
     * Staged Message Table Definition
     ***************************************************************************************************/

    private class MpsStagedMessageTableDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return MpsStagedMessageVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(MpsStagedMessageProperty.ID));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Record Id", MpsStagedMessageProperty.ID));
            attributes.add(new SimTableAttribute("In/Out", MpsStagedMessageProperty.MESSAGE_DIRECTION, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Type", MpsStagedMessageProperty.MESSAGE_TYPE));
            attributes.add(new SimTableAttribute("Family", MpsStagedMessageProperty.MESSAGE_FAMILY));
            attributes.add(new SimTableAttribute("Create Time", MpsStagedMessageProperty.CREATE_TIME, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Update Time", MpsStagedMessageProperty.UPDATE_TIME, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Retry Count", MpsStagedMessageProperty.RETRY_COUNT));
            attributes.add(new SimTableAttribute("Business Id", MpsStagedMessageProperty.BUSINESS_ID));
            attributes.add(new SimTableAttribute("Job Id", MpsStagedMessageProperty.JOB_ID));
            attributes.add(new SimTableAttribute("Message Desc.", MpsStagedMessageProperty.MESSAGE_DESCRIPTION));
            return attributes;
        }
    }
}
