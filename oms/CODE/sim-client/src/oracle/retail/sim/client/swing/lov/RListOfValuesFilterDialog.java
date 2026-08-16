package oracle.retail.sim.client.swing.lov;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.EnterKeyAdapter;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.filter.FilterElement;
import oracle.retail.sim.client.swing.filter.FilterType;
import oracle.retail.sim.client.swing.filter.TableFilterElement;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.core.type.Displayable;

/*************************************************************************************************
 * Filter Dialog that handles the assignment of filters to a list of values editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *************************************************************************************************/

public class RListOfValuesFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -4271144245103863398L;

    private RComboBoxEditor columnEditor = new RComboBoxEditor("Column", true);
    private RComboBoxEditor filterTypeEditor = new RComboBoxEditor("Filter Type", true);
    private RTextFieldEditor filterTextEditor = new RTextFieldEditor("Filter");

    private static final String OKAY = "Ok";
    private static final String CANCEL = "Cancel";

    private RButton okayButton = new RButton(OKAY);
    private RButton cancelButton = new RButton(CANCEL);

    /*************************************************************************************************
     * Constructs a new RTableOfValuesFilterDialog on a JFrame.
     *************************************************************************************************/
    public RListOfValuesFilterDialog(JFrame frame) {
        super(frame);
        initializeFilterDialog();
        layoutFilterDialog();
    }

    /*************************************************************************************************
     * Constructs a new RTableOfValuesFilterDialog on a JDialog.
     *************************************************************************************************/
    public RListOfValuesFilterDialog(JDialog dialog) {
        super(dialog);
        initializeFilterDialog();
        layoutFilterDialog();
    }

    /*************************************************************************************************
     * Initializes the filter dialog and its components.
     *************************************************************************************************/
    private void initializeFilterDialog() {
        setTitle("Select Filter");
        setSize(300, 140);
        setStatusBarVisible(false);

        columnEditor.setSelectionRequired(true);

        filterTypeEditor.setSelectionRequired(true);
        filterTypeEditor.addItem(FilterType.CONTAINS);
        filterTypeEditor.removeEmptySelection();

        filterTextEditor.getTextField().addKeyListener(createEntryKeyListener());
        filterTextEditor.setLength(30);

        okayButton.registerAction(this, OKAY);
        cancelButton.registerAction(this, CANCEL);
    }

    /*************************************************************************************************
     * Creates an entry key listener that allow the box to close when hit enter on filter field.
     *************************************************************************************************/
    private KeyListener createEntryKeyListener() {
        return new EnterKeyAdapter() {
            public void enterKeyPressed(KeyEvent event) {
                applyFilter();
            }
        };
    }

    /*************************************************************************************************
     * Lays out the filter dialog.
     *************************************************************************************************/
    private void layoutFilterDialog() {
        addButton(okayButton);
        addButton(cancelButton);

        REditorPanel matrixPanel = new REditorPanel(3, 1);
        matrixPanel.setEmptyBorder(5);
        matrixPanel.add(columnEditor);
        matrixPanel.add(filterTypeEditor);
        matrixPanel.add(filterTextEditor);

        setContentPane(matrixPanel);
    }

    /*************************************************************************************************
     * Initializes the tab to the configuration data.
     * <p>
     * @param columnHeader The column header of the column clicked on.
     * @param allColumnHeaders An array of all column headers.
     *************************************************************************************************/
    protected void setColumn(String columnHeader, String[] allColumnHeaders) {
        LocalFilterTableColumn initialColumn = null;
        LocalFilterTableColumn temporaryColumn = null;
        List columnList = new ArrayList<>();
        for (String allColumnHeader : allColumnHeaders) {
            temporaryColumn = new LocalFilterTableColumn(allColumnHeader);
            if (allColumnHeader.equals(columnHeader)) {
                initialColumn = temporaryColumn;
            }
            columnList.add(temporaryColumn);
        }
        columnEditor.setItems(columnList);
        columnEditor.setSelectedItem(initialColumn);
    }

    /*************************************************************************************************
     * Implements the event listener method to handle the two button actions.
     *************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(CANCEL)) {
            closeWindow();
        } else if (command.equals(OKAY)) {
            applyFilter();
        }
    }

    /*************************************************************************************************
     * Applies the filter to the list of values.
     *************************************************************************************************/
    private void applyFilter() {
        LocalFilterTableColumn column = (LocalFilterTableColumn) columnEditor.getSelectedItem();
        FilterType filterType = (FilterType) filterTypeEditor.getSelectedItem();
        FilterElement filterElement = new FilterElement(filterType, filterTextEditor.getText());
        TableFilterElement tableFilterElement = new TableFilterElement(column.header, filterElement);

        closeWindow();

        firePropertyChange(UIPropertyName.LIST_OF_VALUES_FILTER, null, tableFilterElement);
    }

    /*************************************************************************************************
     *
     * INNER CLASS - Location wrapper for a column header that does the translation for the combo box.
     *
     *************************************************************************************************/
    private class LocalFilterTableColumn implements Displayable {
        private String header;
        private String translatedHeader;

        public LocalFilterTableColumn(String name) {
            header = name;
            translatedHeader = Translator.getText(name);
        }

        public String toDisplayString() {
            return translatedHeader;
        }
    }
}
