package oracle.retail.sim.client.swing.editor;

import java.awt.GridBagLayout;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RDateField;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.date.SimDateUtil;
import com.rsa.cryptoj.c.A;

/********************************************************************************************************
 * This class represents a calendar field with a title label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDateFieldEditor extends AbstractEditor implements PropertyChangeListener {
    private static final long serialVersionUID = 1535325910588795243L;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RDateField valueField = new RDateField();

    private REventListener eventListener;
    private String eventCommand;
    private String lastTextValue = StringConstants.EMPTY;
    private String lastParsedValue = StringConstants.EMPTY;

    /****************************************************************************************************
     * Creates a new RCalendarFieldEditor with no title.
     ***************************************************************************************************/
    public RDateFieldEditor() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RCalendarFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public RDateFieldEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RCalendarFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RDateFieldEditor(String title, boolean required) {
        titleLabel.setText(title);
        setRequired(required);
        initialize();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initialize() {
        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);
        spaceLabel.setOpaque(false);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        titleLabel.setEnabled(true);

        valueField.addPropertyChangeListener(this);
        valueField.setIncludeTime(false);

        validateToolTip();
        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
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
     * Retrieves the RDateField object of the editor. Many helpful methods have been added to the editor,
     * but the developer can always retrieve the actual RCalendarField widget to get access to methods
     * that are not provided.
     * <p>
     * @return The RDateField widget.
     ***************************************************************************************************/
    public RDateField getDateField() {
        return valueField;
    }

    /****************************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal component rather
     * than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ***************************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueField.addKeyListener(listener);
    }

    /****************************************************************************************************
     * This method is called when the identifier is altered in an editor.
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueField.setIdentifier(identifier);
        titleLabel.setEnabled(true);
    }

    /****************************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ***************************************************************************************************/
    public String getTitle() {
        return titleLabel.getOriginalText();
    }

    /****************************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            titleLabel.clear();
        } else {
            titleLabel.setText(title);
        }
        validateTitle();
    }

    /****************************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM. The
     * label suffix feature is turned off for all alignments except for LEFT.
     * <p>
     * @param alignment The alignment to assign.
     ***************************************************************************************************/
    public void setTitleAlignment(int alignment) {
        titleLabel.setTitleAlignment(alignment);
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title alignment (see
     * EditorConstants).
     * <p>
     * @return The title alignment.
     ***************************************************************************************************/
    public int getTitleAlignment() {
        return titleLabel.getTitleAlignment();
    }

    /****************************************************************************************************
     * Assigns whether or not the editor represents required information.
     * <p>
     * @param required True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(required);
        markRequiredAssigned();
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
     * Validates the layout of the editor.
     ***************************************************************************************************/
    private void validateInnerLayout() {
        removeAll();

        validateInnerPanelLayout();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 0, 0, 0, 2));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 5, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 0, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 5, 1, 0, 0, 0, 0));
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 2, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RCalendarFieldEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /****************************************************************************************************
     * Validates the inner panel widget/error label.
     ***************************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.RIGHT:
                    innerPanel.add(valueField, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(valueField, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
        }
    }

    /****************************************************************************************************
     * Assigns the tool tip text to the label.
     ***************************************************************************************************/
    private void validateToolTip() {
        String date = LocaleManager.getShortDateFormatter().format(SimDateUtil.getCurrentDate());
        titleLabel.setToolTipText(Translator.getText("Example") + " " + date);
    }

    /****************************************************************************************************
     * Validates the visible of the title.
     ***************************************************************************************************/
    private void validateTitle() {
        if (isVisibleDenied()) {
            return;
        }
        titleLabel.setVisible(!StringUtility.isNullOrEmpty(titleLabel.getText()));
    }

    /****************************************************************************************************
     * Assigns a minimum and preferred size to the editor. Values sizes include EditorConstants.SMALL,
     * EditorConstants.MEDIUM and EditorConstants.LARGE.
     * <p>
     * @param sizeType The size type (SMALL, MEDIUM, or LARGE).
     ***************************************************************************************************/
    public void setSizeType(int sizeType) {
        switch (sizeType) {
            case EditorConstants.SMALL:
                setMinimumWidth(UIManager.getInt(UIThemeName.CALENDAR_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.CALENDAR_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.CALENDAR_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width of the field.
     * <p>
     * @param width The width in pixels.
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        valueField.setMinimumWidth(width);
    }

    /****************************************************************************************************
     * The number of milliseconds the system time is inaccurate.
     * <p>
     * @param long The number of milliseconds the system time is inaccurate.
     ***************************************************************************************************/
    public void setOffset(long offset) {
        valueField.setOffset(offset);
    }

    /****************************************************************************************************
     * Assigns whether or not the date display should include time.
     * <p>
     * @param include True if the date format should include time, false if not.
     ***************************************************************************************************/
    public void setIncludeTime(boolean include) {
        valueField.setIncludeTime(include);
    }

    /****************************************************************************************************
     * Assigns a valid date range to the calendar field. The calendar will not allow dates outside of
     * this date range. It is assumed that these dates are in the local timezone.
     * <p>
     * @param startDate A start date of the date range.
     * @param endDate An end date of the date range.
     ***************************************************************************************************/
    public void setValidDateRange(Date startDate, Date endDate) {
        valueField.setValidDateRange(startDate, endDate);
    }

    /****************************************************************************************************
     * Assigns a valid start date to the calendar field. The calendar will not allow dates before this
     * start date. It is assumed that this date is in the local timezone.
     * <p>
     * @param date The date to assign.
     ***************************************************************************************************/
    public void setValidStartDate(Date date) {
        valueField.setValidStartDate(date);
    }

    /****************************************************************************************************
     * Assigns a valid end date to the calendar field. The calendar will not allow dates after this end
     * date. It is assumed that this date is in the local timezone.
     * <p>
     * @param date The date to assign.
     ***************************************************************************************************/
    public void setValidEndDate(Date date) {
        valueField.setValidEndDate(date);
    }

    /****************************************************************************************************
     * Retrieves the unmodified date using the current timezone.
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getDate() throws UIException {
        return valueField.getDate();
    }

    /****************************************************************************************************
     * Retrieves the unmodified date ignoring the current timezone.
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getGMTDate() throws UIException {
        return valueField.getGMTDate();
    }

    /****************************************************************************************************
     * Retrieves the date as the start of date. Retrieves the assigned year, month and day as noon for
     * Greenwich Mean Time (regardless of the TimeZone assigned to the date field editor).
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getGMTDateAtNoon() throws UIException {
        return valueField.getGMTDateAtNoon();
    }

    /****************************************************************************************************
     * Retrieves the date as the start of date. The hours, minutes, seconds and milliseconds are removed.
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getDateAtStartOfDay() throws UIException {
        return valueField.getDateAtStartOfDay();
    }

    /****************************************************************************************************
     * Retrieves the date as the end of date. In other words, the date at 11:59:59.999PM.
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getDateAtEndOfDay() throws UIException {
        return valueField.getDateAtEndOfDay();
    }

    /****************************************************************************************************
     * Assigns a date in the field.
     * <p>
     * @param date The date to assign. This will clear the field if no date is assigned.
     ***************************************************************************************************/
    public void setDate(Date date) {
        valueField.setDate(date);
        lastParsedValue = valueField.getText();
    }

    /****************************************************************************************************
     * Retrieves the text string from the field and returns the unformatted and trimmed version of the
     * text.
     * <p>
     * @return The unformatted and trimmed text in the field.
     ***************************************************************************************************/
    public String getText() {
        return valueField.getText();
    }

    /****************************************************************************************************
     * Assigns the text in the field. The text will be formatted if a mask exists. Text longer than the
     * allowed length is, interestingly, not allowed.
     * <p>
     * @param date The text to place in the date field.
     ***************************************************************************************************/
    public void setText(String date) throws UIException {
        valueField.setText(date, null);
        if (isErrorState) {
            setErrorState(false);
        }
        lastParsedValue = valueField.getText();
    }

    /****************************************************************************************************
     * Assigns the text in the field. The text will be formatted if a mask exists. Text longer than the
     * allowed length is, interestingly, not allowed.
     * <p>
     * @param date The text to place in the date field.
     * @param time The text to place in the time field.
     ***************************************************************************************************/
    public void setText(String date, String time) throws UIException {
        valueField.setText(date, time);
        if (isErrorState) {
            setErrorState(false);
        }
        lastParsedValue = valueField.getText();
    }

    /****************************************************************************************************
     * Clears the field of input.
     ***************************************************************************************************/
    public void clear() {
        valueField.clear();
    }

    /****************************************************************************************************
     * Retrieves whether or not the field is empty.
     * <p>
     * @return True if the field is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return valueField.isEmpty();
    }

    /****************************************************************************************************
     * Override the setVisible() to validate against permissions first.
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        if (isVisibleDenied()) {
            visible = false;
        }
        titleLabel.setVisible(visible);
        innerPanel.setVisible(visible);
    }

    /****************************************************************************************************
     * Override the setEnabled() of JPanel to call through to the label and to the RCalendarField.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueField.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        valueField.setEnabled(fieldEnabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isEnabled() {
        return valueField.isEnabled();
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState) {
        setErrorState(errorState, StringConstants.EMPTY);
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     * @param errorText The text to display along with the editor.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState, String errorText) {
        isErrorState = errorState;

        if (isErrorState) {
            errorLabel.setIcon(errorIcon);
            errorLabel.setToolTipText(errorText);
        } else {
            errorLabel.setIcon(null);
            errorLabel.setToolTipText(StringConstants.EMPTY);
        }
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     ***************************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /****************************************************************************************************
     * Registers an action with the editor. When the focus is lost and the state of the text field has
     * changed, the command will be sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the notification.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        eventListener = listener;
        eventCommand = command;
    }

    /****************************************************************************************************
     * Implements the property change listener method to disable and enable the title along with the
     * actual field.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();
        if (name.equals(UIPropertyName.CALENDAR_DATE_VALIDATE_DATE)) {
            validateDate();
        } else if (name.equals(UIPropertyName.CALENDAR_DATE_FOCUS_GAINED)) {
            firePropertyChange(UIPropertyName.EDITOR_FOCUS_GAINED, true, false);
            lastTextValue = getText();
        } else if (name.equals(UIPropertyName.CALENDAR_DATE_FOCUS_LOST)) {
            firePropertyChange(UIPropertyName.EDITOR_FOCUS_LOST, false, true);
            performActionTrigger();
        } else if (name.equals(UIPropertyName.CALENDAR_DATE_ASSIGNED)) {
            validateErrorState(name);
            performActionTrigger();
        } else {
            validateErrorState(name);
        }
    }

    /****************************************************************************************************
     * Performs an action trigger if one is needed.
     ***************************************************************************************************/
    private void performActionTrigger() {
        String value = valueField.getText();
        if (lastTextValue.equals(value)) {
            return;
        }
        if (isActionEnabled && isEnabled() && isVisible() && eventListener != null && eventCommand != null) {
            eventListener.performActionEvent(new RActionEvent(this, eventCommand, value));
        }
        lastTextValue = value;
    }

    /****************************************************************************************************
     * Validates the error state if a property changes with following property names.
     ***************************************************************************************************/
    private void validateErrorState(String name) {
        if (name.equals(UIPropertyName.CALENDAR_DATE_ASSIGNED)) {
            setErrorState(false);
        } else if (name.equals(UIPropertyName.CALENDAR_DATE_CLEARED)) {
            setErrorState(false);
        } else if (name.equals(UIPropertyName.CALENDAR_DATE_TYPED)) {
            setErrorState(false);
        }
    }

    /****************************************************************************************************
     * Validates the date when the date field looses focus.
     ***************************************************************************************************/
    private void validateDate() {
        try {
            valueField.setDate(valueField.getDate());
            if (isErrorState) {
                UIStatusUtility.clearException(this);
            }
            setErrorState(false);
        } catch (UIException exception) {
            if (isErrorState) {
                return;
            }
            displayException(exception);
            setErrorState(true);
            try {
                valueField.setText(lastParsedValue, null);
            } catch (UIException e) {
                valueField.clear();
            }
            valueField.requestDateFocus();
        }
    }

    /****************************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return valueField.isFocusOwner();
    }

    /****************************************************************************************************
     * Requests the focus move the date field within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely to succeed.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Handles displaying an exception on the application.
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueField, exception);
    }
}
