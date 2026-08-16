package oracle.retail.sim.client.screen.uin;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.UINDetail;
import oracle.retail.sim.common.uin.UINHistoryVO;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;

/********************************************************************************************************
 * UIN Update Status Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINHistoryPanel extends ScreenPanel {
    private static final long serialVersionUID = -2477541217746249827L;

    private UINHistoryModel model = new UINHistoryModel();

    private RTextFieldEditor itemEditor = new RTextFieldEditor("Item");
    private RTextFieldEditor itemDescriptionEditor = new RTextFieldEditor("Item Description");
    private RTextFieldEditor uinLabelEditor = new RTextFieldEditor("UIN Label");
    private RComboBoxEditor oldStatusEditor = new RComboBoxEditor("Current Status");
    private RComboBoxEditor newStatusEditor = new RComboBoxEditor("Update Status");

    private SimTable uinStatusTable = new SimTable(new UINStatusTableDefinition());
    private SimTablePane uinStatusPane = new SimTablePane(uinStatusTable);

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public UINHistoryPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        itemEditor.setIdentifier(SimName.ITEM_ID);
        itemEditor.setSizeType(EditorConstants.MEDIUM);
        itemEditor.setEnabled(false);

        itemDescriptionEditor.setIdentifier(SimName.ITEM_DESCRIPTION);
        itemDescriptionEditor.setEnabled(false);

        oldStatusEditor.setSizeType(EditorConstants.LARGE);
        oldStatusEditor.setItems(UINStatus.values());
        oldStatusEditor.removeItem(UINStatus.UNCONFIRMED);
        oldStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        oldStatusEditor.setEnabled(false);

        newStatusEditor.setSizeType(EditorConstants.LARGE);
        newStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        newStatusEditor.setItems(UINStatus.values());
        newStatusEditor.removeItem(UINStatus.UNCONFIRMED);
        
        uinLabelEditor.setSizeType(EditorConstants.LARGE);

        uinStatusTable.setTableEditable(false);
        uinStatusTable.setSingleRowSelectionMode();
        uinStatusTable.setSortingEnabled(false);

        uinStatusPane.setTitleBorder("Audit Information");
    }

    private void layoutPanel() {
        RHeaderPanel headerPanel = new RHeaderPanel(1, 2);
        headerPanel.add(itemEditor);
        headerPanel.add(itemDescriptionEditor);

        REditorPanel secondPanel = new REditorPanel(3, 2);
        secondPanel.add(uinLabelEditor);
        secondPanel.add(oldStatusEditor);
        if (model.hasPermission(PermissionKey.PC_UPDATE_UIN_STATUS)) {
            secondPanel.add(newStatusEditor);
        }
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(secondPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(uinStatusPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return uinStatusTable;
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public boolean isSerialNumberEditable() {
        return model.isSerialNumberEditable();
    }

    public void start() throws Exception {
        model.initializeModel();

        ItemDetailVO detailVO = model.getItemDetailVO();
        UINDetail uinDetail = model.getUINDetail();

        itemEditor.setText(detailVO.getId());
        if (SimConfigManager.isItemShortDescription()) {
            itemDescriptionEditor.setText(detailVO.getShortDescription());
        } else {
            itemDescriptionEditor.setText(detailVO.getLongDescription());
        }
        uinLabelEditor.setTitle(uinDetail.getType().toString());
        uinLabelEditor.setText(uinDetail.getUin());

        oldStatusEditor.setSelectedItem(uinDetail.getStatus());
        newStatusEditor.setVisible(isSerialNumberEditable());

        uinStatusTable.setRows(model.findUINHistoryVOs());
    }

    public void assignFocusInScreen() {
        assignFocusInScreen(newStatusEditor);
    }

    /****************************************************************************************************
     * Handle Done
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        if (model.hasPermission(PermissionKey.PC_UPDATE_UIN_STATUS)) {
            UINStatus newStatus = (UINStatus) newStatusEditor.getSelectedItem();
            if (newStatus == null) {
                displayError(UINMessageText.UIN_SELECTED_STATUS_ERROR, model.getUINLabel());
                assignFocusInScreen();
                return false;
            }
            UINStatus currentStatus = (UINStatus) oldStatusEditor.getSelectedItem();
            if (newStatus == currentStatus) {
                displayError(UINMessageText.UIN_NEW_SAME_AS_CURRENT, model.getUINLabel());
                assignFocusInScreen();
                return false;
            }
            if (currentStatus == UINStatus.UNCONFIRMED) {
                displayError(UINMessageText.UIN_STATUS_UNCONFIRMED_ERROR, model.getUINLabel());
                assignFocusInScreen();
                return false;
            }
            try {
                RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), "Confirmation");
                dialog.setMessage(UINMessageText.UIN_UPDATE_STATUS_CONFIRM);
                dialog.setYesNoType();
                if (dialog.getConfirmation()) {
                    model.updateStatus(newStatus);
                    return true;
                }
            } catch (Throwable exception) {
                displayException(exception);
            }
            return false;
        }
        return true;
    }

    /****************************************************************************************************
     * UIN Update Status TABLE DEFINITION
     ***************************************************************************************************/

    private static class UINStatusTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return UINHistoryVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("updateDate", false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(6);
            attributes.add(new SimTableAttribute("Store", "fullName"));
            attributes.add(new SimTableAttribute("Date", "updateDate", new DateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Functional Area", "functionalArea", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Identifier", "functAreaIdentifier", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("User", "updateUser"));
            return attributes;
        }
    }
}
