package oracle.retail.sim.client.swing.editor;

import java.awt.GridBagLayout;
import java.awt.event.KeyListener;
import java.util.Date;
import javax.swing.JPanel;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * This editor represents a date range (from date/to date). It can only be displayed with the label on
 * the far left and layed out horizontally.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDateRangeEditor extends AbstractEditor implements REventListener {
    private static final long serialVersionUID = -8267733324486242717L;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RDateFieldEditor startDateEditor = new RDateFieldEditor("From");
    private RDateFieldEditor endDateEditor = new RDateFieldEditor("To");
    private RLabel innerLabel = new RLabel();

    private static final String START_DATE_MODIFIED = "StartDate.modified";
    private static final String END_DATE_MODIFIED = "EndDate.modified";

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public RDateRangeEditor() {
        initialize();
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param title The title of the editor.
     ***************************************************************************************************/
    public RDateRangeEditor(String title) {
        initialize();
        setTitle(title);
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param title The title of the editor.
     * @param startTitle The title for the start date editor.
     * @param endTitle The title fof the end date editor.
     ***************************************************************************************************/
    public RDateRangeEditor(String title, String startTitle, String endTitle) {
        initialize();
        setTitle(title);
        startDateEditor.setTitle(startTitle);
        endDateEditor.setTitle(endTitle);
    }

    /****************************************************************************************************
     * Initializes the editor
     ***************************************************************************************************/
    private void initialize() {
        startDateEditor.setSizeType(EditorConstants.SMALL);
        endDateEditor.setSizeType(EditorConstants.SMALL);

        startDateEditor.registerAction(this, START_DATE_MODIFIED);
        endDateEditor.registerAction(this, END_DATE_MODIFIED);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        setOpaque(false);
        setLayout(new GridBagLayout());

        validateInnerLayout();
    }

    /****************************************************************************************************
     * Validates the layout of the editor.
     ***************************************************************************************************/
    private void validateInnerLayout() {
        removeAll();
        add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 0, 0, 0, 2));
        add(startDateEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        add(endDateEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        add(innerLabel, GridTool.constraints(3, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /****************************************************************************************************
     * Called if identifier is altered (empty implementation).
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
    }

    /****************************************************************************************************
     * Displays an exception within the editor (empty implementation).
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
    }

    /****************************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ***************************************************************************************************/
    public String getTitle() {
        return titleLabel.getText();
    }

    /****************************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /****************************************************************************************************
     * Retrieves the label widget associated with this editor.
     * <p>
     * @param The label widget.
     ***************************************************************************************************/
    public REditorLabel getLabel() {
        return titleLabel;
    }

    /****************************************************************************************************
     * Retrieves the editor for the start date.
     ***************************************************************************************************/
    public RDateFieldEditor getStartDateEditor() {
        return startDateEditor;
    }

    /****************************************************************************************************
     * Retrieves the editor for the end date.
     ***************************************************************************************************/
    public RDateFieldEditor getEndDateEditor() {
        return endDateEditor;
    }

    /****************************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal component rather
     * than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ***************************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        startDateEditor.addKeyListener(listener);
        endDateEditor.addKeyListener(listener);
    }

    /****************************************************************************************************
     * Assigns whether or not the editor represents required information.
     * <p>
     * @param required True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(required);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor represents required information.
     * <p>
     * @return True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public boolean isRequired() {
        return titleLabel.isRequired();
    }

    /****************************************************************************************************
     * Returns false (empty implementation).
     ***************************************************************************************************/
    public boolean isEmpty() {
        return false;
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     * @param errorText The text to display along with the editor.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState, String message) {
        startDateEditor.setErrorState(errorState, message);
        endDateEditor.setErrorState(errorState, message);
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState) {
        startDateEditor.setErrorState(errorState);
        endDateEditor.setErrorState(errorState);
    }

    /****************************************************************************************************
     * Retrieves whethor or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     ***************************************************************************************************/
    public boolean isErrorState() {
        return startDateEditor.isErrorState();
    }

    /****************************************************************************************************
     * Enables or disabled the registered action triggers of the editor.
     * <p>
     * @param enabled True if actions should be sent, false if not.
     ***************************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        super.setActionsEnabled(enabled);
        startDateEditor.setActionsEnabled(enabled);
        endDateEditor.setActionsEnabled(enabled);
    }

    /****************************************************************************************************
     * Registers the listener and command to each of the date widgets.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        startDateEditor.registerAction(listener, command);
        endDateEditor.registerAction(listener, command);
    }

    /****************************************************************************************************
     * Does nothing (empty implementation).
     ***************************************************************************************************/
    public void setTitleAlignment(int alignment) {
    }

    /****************************************************************************************************
     * Retrieves the title alignment.
     ***************************************************************************************************/
    public int getTitleAlignment() {
        return EditorConstants.LEFT;
    }

    /****************************************************************************************************
     * Assigns the size type (empty implementation).
     ***************************************************************************************************/
    public void setSizeType(int sizeType) {
    }

    /****************************************************************************************************
     * Retrieves the vertical weight of the editor (used with REditorPanel)
     * <p>
     * @return The vertical weight of the editor.
     ***************************************************************************************************/
    public int getVerticalWeight() {
        return 0;
    }

    /****************************************************************************************************
     * Retrieves the horiztonal weight of the editor (used with REditorPanel)
     * <p>
     * @return The horiztonal weight of the editor.
     ***************************************************************************************************/
    public int getHorizontalWeight() {
        return 0;
    }

    /****************************************************************************************************
     * Retrieves the fill of the editor (used with REditorPanel);
     * <p>
     * @return The fill of the editor.
     ***************************************************************************************************/
    public int getFill() {
        return 1;
    }

    /****************************************************************************************************
     * Assigns the start date to the date range editor.
     * <p>
     * @param date The date
     ***************************************************************************************************/
    public void setStartDate(Date date) {
        startDateEditor.setDate(date);
    }

    /****************************************************************************************************
     * Retrieves the start date to the date range editor.
     * <p>
     * return date The date
     ***************************************************************************************************/
    public Date getStartDate() throws UIException {
        return startDateEditor.getDateAtStartOfDay();
    }

    /****************************************************************************************************
     * Assigns the end date to the date range editor.
     * <p>
     * @param The date
     ***************************************************************************************************/
    public void setEndDate(Date date) {
        endDateEditor.setDate(date);
    }

    /****************************************************************************************************
     * Returnes the end date to the date range editor.
     * <p>
     * return The date
     ***************************************************************************************************/
    public Date getEndDate() throws UIException {
        return endDateEditor.getDateAtEndOfDay();
    }

    /****************************************************************************************************
     * Assigns a valid start date to the calendar field. The calendar will not allow dates before this
     * start date. It is assumed that this date is in the local timezone.
     * <p>
     * @param date The date to assign.
     ***************************************************************************************************/
    public void setValidStartDate(Date date) {
        startDateEditor.setValidStartDate(date);
        endDateEditor.setValidStartDate(date);
    }

    /****************************************************************************************************
     * Assigns a valid end date to the calendar field. The calendar will not allow dates after this end
     * date. It is assumed that this date is in the local timezone.
     * <p>
     * @param date The date to assign.
     ***************************************************************************************************/
    public void setValidEndDate(Date date) {
        startDateEditor.setValidEndDate(date);
        endDateEditor.setValidEndDate(date);
    }

    /****************************************************************************************************
     * Clears the date range editor.
     ***************************************************************************************************/
    public void clear() {
        startDateEditor.clear();
        endDateEditor.clear();
    }

    /****************************************************************************************************
     * Override enabled to enable sub-fields.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        titleLabel.setEnabled(enabled);
        startDateEditor.setEnabled(enabled);
        endDateEditor.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        titleLabel.setEnabled(labelEnabled);
        startDateEditor.setEnabled(fieldEnabled);
        endDateEditor.setEnabled(fieldEnabled);
    }

    /****************************************************************************************************
     * Override enabled to enable sub-fields.
     ***************************************************************************************************/
    public void setStartDateEnabled(boolean enabled) {
        startDateEditor.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override enabled to enable sub-fields.
     ***************************************************************************************************/
    public void setEndDateEnabled(boolean enabled) {
        endDateEditor.setEnabled(enabled);
    }

    /****************************************************************************************************
     * On request focus, move to start date
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return startDateEditor.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Handle Events
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(START_DATE_MODIFIED)) {
            doStartDateModified();
        } else if (command.equals(END_DATE_MODIFIED)) {
            doEndDateModified();
        }
    }

    /****************************************************************************************************
     * If start date modified, alter valid start date on the end date widget.
     ***************************************************************************************************/
    private void doStartDateModified() {
        Date startDate = null;
        try {
            startDate = startDateEditor.getDate();
        } catch (UIException e) {
            startDate = null;
        }
        endDateEditor.setValidStartDate(startDate);
    }

    /****************************************************************************************************
     * If end date modified, alter valid end date on the start date widget.
     ***************************************************************************************************/
    private void doEndDateModified() {
        Date endDate = null;
        try {
            endDate = endDateEditor.getDate();
        } catch (UIException e) {
            endDate = null;
        }
        startDateEditor.setValidEndDate(endDate);
    }
}
