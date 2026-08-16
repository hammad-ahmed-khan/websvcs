package oracle.retail.sim.client.screen.notes;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.notes.Note;

/********************************************************************************************************
 * Dialog window for handling notes associated with the transaction.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NotesDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2544046001843179677L;

    private NotesDialogModel model = new NotesDialogModel();

    private RTextAreaEditor notesEntryEditor = new RTextAreaEditor();
    private RTextAreaEditor notesDisplayEditor = new RTextAreaEditor();

    private SimTable notesTable = new SimTable(new NotesTableDefinition());
    private SimTablePane notesPane = new SimTablePane(notesTable);

    private RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);
    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    private static final String NOTE_SELECTED = "Note.selected";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public NotesDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setSize(850, 420);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        notesEntryEditor.setMinimumHeight(60);
        notesEntryEditor.setEnabled(true);
        notesEntryEditor.setIdentifier(SimName.NOTES_TEXT);

        notesDisplayEditor.setMinimumHeight(60);
        notesDisplayEditor.setEnabled(false);

        notesTable.registerSingleClickAction(this, NOTE_SELECTED);
        notesTable.setColumnSize("date", 200);
        notesTable.setColumnSize("user", 100);

        addButton.registerAction(this, SimNavigation.DIALOG_ADD);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
    }

    private void layoutContent() {
        addButton(addButton);
        addButton(applyButton);
        addButton(cancelButton);
        addButton(closeButton);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(notesDisplayEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 5, 10, 10, 0));
        mainPanel.add(notesPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 10, 10, 10));
        mainPanel.add(notesEntryEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 10, 10, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void loadNotes(FunctionalArea functionalArea, Long functionalId, boolean isEnabled) throws Exception {
        model.setFunctionalArea(functionalArea);
        model.setFunctionalId(functionalId);
        notesTable.setRows(model.findNotes());
        if (isEnabled) {
            closeButton.setVisible(false);
        } else {
            addButton.setVisible(false);
            applyButton.setVisible(false);
            cancelButton.setVisible(false);
            notesEntryEditor.setEnabled(false);
        }
    }
    
    public void stopEditing() {
        notesTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(SimNavigation.DIALOG_ADD)) {
                doAdd();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            } else if (command.equals(SimNavigation.DIALOG_CLOSE)) {
                closeWindow();
            } else if (command.equals(NOTE_SELECTED)) {
                doNoteSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Add Note Action
     ***************************************************************************************************/
    private void doAdd() {
        try {
            if (!StringHelper.isNullOrEmpty(notesEntryEditor.getText())) {
                notesTable.addRow(model.createNote(notesEntryEditor.getText()));
                notesEntryEditor.clear();
            }
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
      * Apply Action
      ***************************************************************************************************/
    private void doApply() {
        try {
            if (!StringHelper.isNullOrEmpty(notesEntryEditor.getText())) {
                notesTable.addRow(model.createNote(notesEntryEditor.getText()));
                notesEntryEditor.clear();
            }
            model.saveNotes((List<Note>) notesTable.getAllRowData());
            closeWindow();
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default stops editing and closes window.
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }

    /****************************************************************************************************
     * Note Selection Action
     ***************************************************************************************************/
    private void doNoteSelected() {
        try {
            Note note = (Note) notesTable.getSelectedRowData();
            if (note != null) {
                notesDisplayEditor.setText(note.getText());
            } else {
                notesDisplayEditor.setText(null);
            }
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class NotesTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return Note.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("date", false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(3);
            attributes.add(new SimTableAttribute("Date", "date", new DateTimeDisplayer()));
            attributes.add(new SimTableAttribute("User", "user"));
            attributes.add(new SimTableAttribute("Note", "text"));
            return attributes;
        }
    }
}
