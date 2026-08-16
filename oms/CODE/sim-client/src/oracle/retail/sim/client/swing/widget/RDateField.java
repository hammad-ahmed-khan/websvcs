package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.format.TimeMask;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.date.SimDateUtil;
import com.rsa.cryptoj.c.A;

/********************************************************************************************************
 * This class sublcasses an RPanel and supplies a date field that consists of a text field and a button
 * that triggers a popup calendar window. It acts just like any other widget.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDateField extends RPanel implements RetailComponent, FocusListener {
    private static final long serialVersionUID = 5035747131922342231L;

    private String identifier = "";
    private NavigationPermission permission = NavigationPermission.FULL;
    private UIPermissionManager permissionManager;

    private RTextField dateField = new RTextField();
    private RTextField timeField = new RTextField();
    private RIconButton calendarButton = new RIconButton();
    private Date startDate;
    private Date endDate;
    private long offset;
    private boolean includeTime;

    /****************************************************************************************************
     * Return new RDateField object.
     ***************************************************************************************************/
    public RDateField() {
        initialize();
        layoutCalendarField();
    }

    /****************************************************************************************************
     * Initializes the default settings and state of the RDateField.
     ***************************************************************************************************/
    private void initialize() {
        dateField.addKeyListener(createErrorKeyListener());
        dateField.addFocusListener(this);
        dateField.setLength(20);

        timeField.addFocusListener(this);
        timeField.setMinimumWidth(80);
        timeField.setMask(new TimeMask());
        timeField.setMaskLocked(true);
        timeField.setVisible(false);
        timeField.setLength(20);

        calendarButton.addFocusListener(this);
        calendarButton.addActionListener(createCalendarAction());
        calendarButton.setIcon(UIManager.getIcon(UIThemeName.CALENDARFIELD_ICON));
        calendarButton.setDisabledIcon(UIManager.getIcon(UIThemeName.CALENDARFIELD_DISABLED_ICON));
        calendarButton.setToolTipText(LocaleManager.getShortDateFormatter().format(SimDateUtil.getCurrentDate()));

        setEnabled(true);
    }

    /****************************************************************************************************
     * Retrieves the error state key listener.
     ***************************************************************************************************/
    private KeyListener createErrorKeyListener() {
        return new KeyListener() {
            public void keyReleased(KeyEvent event) {
            }

            public void keyPressed(KeyEvent event) {
            }

            public void keyTyped(KeyEvent event) {
                firePropertyChange(UIPropertyName.CALENDAR_DATE_TYPED, true, false);
            }
        };
    }

    /****************************************************************************************************
     * Lays out the text field and calendar button within the widget.
     ***************************************************************************************************/
    private void layoutCalendarField() {
        setLayout(new GridBagLayout());
        add(dateField, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        add(calendarButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
        add(timeField, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Creates action assigned to calendar button.
     ***************************************************************************************************/
    private ActionListener createCalendarAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                selectDateFromCalendar();
            }
        };
    }

    /****************************************************************************************************
     * Sets the identifier of the component.
     * <p>
     * @param identifier The identifier to assign to this component.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            identifier = StringConstants.EMPTY;
        }
        dateField.setIdentifier(identifier, false);
        timeField.setIdentifier(identifier, false);

        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Retrieve the identifier for the component.
     * <p>
     * @return The identifier of the component.
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /****************************************************************************************************
     * Sets the minimum width of the component.
     * <p>
     * @param width The minimum width (number of pixels).
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        dateField.setMinimumWidth(width);
    }

    /****************************************************************************************************
     * Displays the calendar popup so the user can select a date.
     ***************************************************************************************************/
    private void selectDateFromCalendar() {
        Container container = getTopLevelAncestor();
        Date date = getInternalDate();
        if (date == null) {
            date = createDate();
        }
        RCalendarDialog calendarDialog;

        if (container instanceof JDialog) {
            calendarDialog = new RCalendarDialog((JDialog) container, date);
        } else if (container instanceof JFrame) {
            calendarDialog = new RCalendarDialog((JFrame) container, date);
        } else {
            calendarDialog = new RCalendarDialog(date);
        }
        calendarDialog.addPropertyChangeListener(createDialogPropertyListener());
        calendarDialog.setValidDateRange(startDate, endDate);

        WindowPlacer.alignToComponent(ApplicationInternal.getFrame(), calendarButton, calendarDialog, true, false);

        calendarDialog.setVisible(true);
    }

    /****************************************************************************************************
     * The number of milliseconds the system time is inaccurate.
     * <p>
     * @param long The number of milliseconds the system time is inaccurate.
     ***************************************************************************************************/
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /****************************************************************************************************
     * Assigns whether or not the date display should include time.
     * <p>
     * @param include True if the date format should include time, false if not.
     ***************************************************************************************************/
    public void setIncludeTime(boolean include) {
        includeTime = include;
        refreshDate();
        timeField.setVisible(includeTime);
    }

    /****************************************************************************************************
     * Refreshes the date if one exists.
     ***************************************************************************************************/
    private void refreshDate() {
        Date date = getInternalDate();
        if (date != null) {
            setDate(date);
        }
    }

    /****************************************************************************************************
     * Retrieves the date built with timezone information included in the calendar.
     * <p>
     * @return The date.
     ***************************************************************************************************/
    public Date getDate() throws UIException {
        if (dateField.isEmpty()) {
            return null;
        }
        return parseDate();
    }

    /****************************************************************************************************
     * Retrieves the date built with timezone information ignored.
     * <p>
     * @return The date.
     ***************************************************************************************************/
    public Date getGMTDate() throws UIException {
        String dateText = dateField.getText();
        String timeText = timeField.getText();
        if (StringUtility.isNullOrEmpty(dateText)) {
            return null;
        }
        TimeZone timezone = TimeZone.getTimeZone(SimDateUtil.GMT_ID);
        GregorianCalendar calendar = new GregorianCalendar(timezone);
        try {
            if (includeTime && !StringUtility.isNullOrEmpty(timeText)) {
                calendar.setTime(LocaleManager.getDateTimeParser(DateFormat.MEDIUM, timezone).parse(dateText + " " + timeText));
            } else {
                calendar.setTime(LocaleManager.getDateParser(timezone).parse(dateText));
            }
            return calendar.getTime();
        } catch (Throwable exception) {
            throw buildDateParsingException();
        }
    }

    /****************************************************************************************************
     * Retrieves the date as the start of date. Retrieves the assigned year, month and day as noon for
     * Greenwich Mean Time (regardless of the TimeZone assigned to the date field editor).
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getGMTDateAtNoon() throws UIException {
        Date date = getGMTDate();
        if (date == null) {
            return null;
        }
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone(SimDateUtil.GMT_ID));
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /****************************************************************************************************
     * Retrieves the date as the start of date. The hours, minutes, seconds and milliseconds are removed.
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getDateAtStartOfDay() throws UIException {
        Date date = parseDate();
        if (date == null) {
            return null;
        }
        GregorianCalendar calendar = createCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /****************************************************************************************************
     * Retrieves the date as the end of date. In other words, the date at 11:59:59.999PM.
     * <p>
     * @return The date.
     * @throws A UIException if the date is not valid.
     ***************************************************************************************************/
    public Date getDateAtEndOfDay() throws UIException {
        Date date = parseDate();
        if (date == null) {
            return null;
        }
        GregorianCalendar calendar = createCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /****************************************************************************************************
     * Retrieves the date internally.
     ***************************************************************************************************/
    private Date getInternalDate() {
        try {
            return parseDate();
        } catch (Throwable exception) {
            return null;
        }
    }

    /****************************************************************************************************
     * Parses the date from the text in the value field and returns it.
     ***************************************************************************************************/
    private Date parseDate() throws UIException {
        return parseDate(createCalendar());
    }

    /****************************************************************************************************
     * Parses the text in the field and returns a date object. It uses the calendar passed in to get
     * the date. It validates only the date portion of the entry to make sure it matches the SHORT
     * date entry format in the system. Time is not validated and uses the standard JAVA rules. This
     * will not parse a date larger than 9998.
     ***************************************************************************************************/
    private Date parseDate(GregorianCalendar calendar) throws UIException {
        String dateText = dateField.getText();
        String timeText = timeField.getText();
        if (StringUtility.isNullOrEmpty(dateText)) {
            return null;
        }
        if (LocaleManager.isInvalidShortDate(dateText)) {
            throw buildDateParsingException();
        }
        try {
            if (includeTime && !StringUtility.isNullOrEmpty(timeText)) {
                calendar.setTime(LocaleManager.getDateTimeParser(DateFormat.MEDIUM).parse(dateText + " " + timeText));
            } else {
                calendar.setTime(LocaleManager.getDateParser().parse(dateText));
            }
        } catch (Throwable exception) {
            throw buildDateParsingException();
        }
        if (calendar.get(GregorianCalendar.YEAR) > 9998) {
            throw buildDateParsingException();
        }
        return calendar.getTime();
    }

    /****************************************************************************************************
     * Builds Date Parsing Exception
     ***************************************************************************************************/
    private UIException buildDateParsingException() {
        UIException exception = null;
        if (includeTime) {
            String dateText = LocaleManager.getShortDateTimeFormatter().format(createDate());
            exception = new UIException(UIMessageText.DATE_TIME_FORMAT_EXCEPTION, dateText);
        } else {
            String dateText = LocaleManager.getShortDateFormatter().format(createDate());
            exception = new UIException(UIMessageText.DATE_FORMAT_EXCEPTION, dateText);
        }
        return exception;
    }

    /****************************************************************************************************
     * Assigns a valid date range to the calendar field. The calendar will not allow dates outside of
     * this date range.
     * <p>
     * @param startDate A start date of the date range.
     * @param endDate An end date of the date range.
     ***************************************************************************************************/
    public void setValidDateRange(Date startDate, Date endDate) {
        setValidStartDate(startDate);
        setValidEndDate(endDate);
    }

    /****************************************************************************************************
     * Assigns a valid start date to the calendar field. The calendar will not allow dates before this
     * start date.
     * <p>
     * @param date The date to assign.
     ***************************************************************************************************/
    public void setValidStartDate(Date date) {
        startDate = date;
    }

    /****************************************************************************************************
     * Assigns a valid end date to the calendar field. The calendar will not allow dates after this end
     * date.
     * <p>
     * @param date The date to assign.
     ***************************************************************************************************/
    public void setValidEndDate(Date date) {
        endDate = date;
    }

    /****************************************************************************************************
     * Assigns a date in the field assuming the date is a GMT date.
     * <p>
     * @param date The date to assign. This will clear the field if no date is assigned.
     ***************************************************************************************************/
    public void setDate(Date date) {
        if (date == null) {
            clear();
            return;
        }
        if (includeTime) {
            dateField.setText(LocaleManager.getShortDateFormatter().format(date));
            timeField.setText(LocaleManager.getShortTimeFormatter().format(date));
            timeField.setEnabled(true);
        } else {
            dateField.setText(LocaleManager.getShortDateFormatter().format(date));
            timeField.clear();
            timeField.setEnabled(false);
        }
        firePropertyChange(UIPropertyName.CALENDAR_DATE_ASSIGNED, false, true);
    }

    /****************************************************************************************************
     * Retrieves the date as a string.
     * <p>
     * @return The date as a string.
     ***************************************************************************************************/
    public String getText() {
        return dateField.getText() + " " + timeField.getText();
    }

    /****************************************************************************************************
     * Retrieves the selected text within the value field.
     * <p>
     * @return The selected text.
     ***************************************************************************************************/
    public String getSelectedText() {
        return dateField.getSelectedText();
    }

    /****************************************************************************************************
     * Sets the text on the calendar field.
     * <p>
     * @param date The text to place in the date field.
     * @param time The text to place in the time field.
     ***************************************************************************************************/
    public void setText(String date, String time) throws UIException {
        try {
            if (StringUtility.isNullOrEmpty(date)) {
                setDate(null);
            } else {
                setDate(LocaleManager.getDateParser().parse(date));
            }
        } catch (Exception exception) {
            throw new UIException(UIMessageText.DATE_FORMAT_EXCEPTION, RErrorSeverity.ERROR);
        }
        if (includeTime) {
            timeField.setText(time);
        }
    }

    /****************************************************************************************************
     * Retrieves whether or not the calendar field is empty.
     * <p>
     * @return True if the calendar field is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return dateField.isEmpty() && timeField.isEmpty();
    }

    /****************************************************************************************************
     * Clears the calendar field of all information.
     ***************************************************************************************************/
    public void clear() {
        dateField.clear();
        timeField.clear();
        timeField.setEnabled(false);
        firePropertyChange(UIPropertyName.CALENDAR_DATE_CLEARED, false, true);
    }

    /****************************************************************************************************
     * Selects all the text in the calendar field.
     ***************************************************************************************************/
    public void selectAll() {
        dateField.selectAll();
    }

    /****************************************************************************************************
     * Refreshes the displayed text in the date field (this will reformat the text).
     ***************************************************************************************************/
    public void refresh() {
        dateField.refresh();
    }

    /****************************************************************************************************
     * Transfers focus of the cursor to the date field.
     ***************************************************************************************************/
    public void requestFocus() {
        dateField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Transfers focus of the cursor to the calendar field.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return dateField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Transfers focus to the date field.
     ***************************************************************************************************/
    public void requestDateFocus() {
        dateField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Transfers focus to the time field.
     ***************************************************************************************************/
    public void requestTimeFocus() {
        timeField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Overrides the superclass setVisible() to check permissions first. /
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        if (permission.equals(NavigationPermission.NONE)) {
            visible = false;
        }
        super.setVisible(visible);
    }

    /****************************************************************************************************
     * Overrides the superclass setEnabled() to check permissions first. /
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (!permission.equals(NavigationPermission.FULL)) {
            enabled = false;
        }
        dateField.setEnabled(enabled);
        timeField.setEnabled(enabled && includeTime);
        calendarButton.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Sets whether or not the calendar field is editable. This disables/enables the calendar field.
     * <p>
     * @param editable True if the calendar field should be editable, false if not.
     ***************************************************************************************************/
    public void setEditable(boolean editable) {
        dateField.setEditable(editable);
    }

    /****************************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return dateField.isFocusOwner() || timeField.isFocusOwner() || calendarButton.isFocusOwner();
    }

    /****************************************************************************************************
     * Implements the focus listener method. It triggers a property change every time the date field
     * receives focus.
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
        if (dateField.isFocusOwner()) {
            dateField.selectAll();
        } else {
            dateField.select(0, 0);
        }
        if (timeField.isFocusOwner()) {
            timeField.selectAll();
        } else {
            timeField.select(0, 0);
        }
        firePropertyChange(UIPropertyName.CALENDAR_DATE_FOCUS_GAINED, true, false);
    }

    /****************************************************************************************************
     * Implements the focus listener method. It triggers a property change every time the date field
     * looses focus.
     ***************************************************************************************************/
    public void focusLost(FocusEvent event) {
        if (event.isTemporary()) {
            return;
        }
        firePropertyChange(UIPropertyName.CALENDAR_DATE_VALIDATE_DATE, true, false);

        if (dateField.isFocusOwner() || calendarButton.isFocusOwner() || timeField.isFocusOwner()) {
            return;
        }
        firePropertyChange(UIPropertyName.CALENDAR_DATE_FOCUS_LOST, true, false);
    }

    /****************************************************************************************************
     * Creates the calendar window dialog property listener.
     ***************************************************************************************************/
    private PropertyChangeListener createDialogPropertyListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (UIPropertyName.CALENDAR_DATE_SELECTED.equals(event.getPropertyName())) {
                    setDate((Date) event.getNewValue());
                }
            }
        };
    }

    /****************************************************************************************************
     * @see setBackground() in JTextField.
     ***************************************************************************************************/
    public void setBackground(Color color) {
        if (dateField != null) {
            dateField.setBackground(color);
        }
    }

    /****************************************************************************************************
     * @see setForeground() in JTextField.
     ***************************************************************************************************/
    public void setForeground(Color color) {
        if (dateField != null) {
            dateField.setForeground(color);
        }
    }

    /****************************************************************************************************
     * Updates the color state. This method ensures the color displayed by the widget is correct based on
     * the state of the widget. The controlling color flag is to keep the set() methods from changing the
     * default background color.
     ***************************************************************************************************/
    public void updateColorState() {
        dateField.updateColorState();
    }

    /****************************************************************************************************
     * Validates the permission of the object based on its identifier. If no identifier exists, then the
     * permission is true. If an identifier exists and the permission returns as false, the component
     * will not be able to be enabled()
     * <p>
     * @param ownerPrefix The owner class name to attach to the identifier to find permission.
     ***************************************************************************************************/
    public void validatePermission(String ownerPrefix) throws UIException {
        if (permissionManager == null) {
            permissionManager = new UIPermissionManager();
        }
        permission = permissionManager.getComponentPermission(identifier, ownerPrefix);
        if (permission.equals(NavigationPermission.FULL)) {
            return;
        }
        if (permission.equals(NavigationPermission.NONE)) {
            setVisible(false);
        }
        setEnabled(false);
    }

    /****************************************************************************************************
     * Creates the date internally.
     ***************************************************************************************************/
    private Date createDate() {
        GregorianCalendar calendar = createCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis() + offset);
        return calendar.getTime();
    }

    /****************************************************************************************************
     * Helper method to create a calendar.
     ***************************************************************************************************/
    private GregorianCalendar createCalendar() {
        Locale locale = LocaleManager.getLanguageLocale();
        TimeZone timeZone = LocaleManager.getTimeZone();
        if (timeZone != null && locale != null) {
            return new GregorianCalendar(timeZone, locale);
        }
        if (timeZone != null) {
            return new GregorianCalendar(timeZone);
        }
        if (locale != null) {
            return new GregorianCalendar(locale);
        }
        return new GregorianCalendar();
    }
}
