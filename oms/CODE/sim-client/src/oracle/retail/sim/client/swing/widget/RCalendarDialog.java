package oracle.retail.sim.client.swing.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.frame.RTitleButton;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.common.core.locale.NumberParser;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.date.SimDateUtil;

/********************************************************************************************************
 * This class represents a calendar widget that assists with selecting a date.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RCalendarDialog extends JDialog implements WindowFocusListener {
    private static final long serialVersionUID = 3745143425729796107L;

    private static final int NO_DEACTIVATE = 0;
    private static final int FULL_DEACTIVATE = 1;
    private static final int DAY_DEACTIVATE = 2;

    private RPanel labPanel = new RPanel();
    private RPanel topPanel = new RPanel();
    private RPanel dayPanel = new RPanel();

    private RLabel titleLabel = new RLabel();
    private RLabel timeZoneLabel = new RLabel();
    private RComboBox monthCombo = new RComboBox();
    private RTextField yearField = new RTextField();

    private RButton[] dayButtonArray = new RButton[42];
    private int[] dayOfWeekArray = new int[42];
    private RLabel[] dayTitleArray = new RLabel[7];
    private RMonth[] monthArray = new RMonth[12];

    private RArrowButton monthDownButton = new RArrowButton(RArrowButton.WEST);
    private RArrowButton monthUpButton = new RArrowButton(RArrowButton.EAST);
    private RArrowButton yearDownButton = new RArrowButton(RArrowButton.WEST);
    private RArrowButton yearUpButton = new RArrowButton(RArrowButton.EAST);
    private RTitleButton closeButton = new RTitleButton(RTitleButton.CLOSE);

    private Color borderColor = Color.BLACK;
    private Color dayTitleBackground = Color.BLACK;
    private Color dayForeground = Color.BLACK;
    private Color dayBackground = Color.WHITE;
    private Color dayFocusForeground = Color.WHITE;
    private Color dayFocusBackground = Color.BLACK;
    private Color dayCurrentForeground = Color.BLACK;
    private Color dayCurrentBackground = Color.LIGHT_GRAY;

    private GregorianCalendar storeToday;
    private String storeDay = StringConstants.EMPTY;
    private int storeMonth = -1;

    private GregorianCalendar calendar;
    private GregorianCalendar startDateCalendar;
    private GregorianCalendar endDateCalendar;
    
    private NumberParser numberParser;

    private boolean includeTimeZone;

    /****************************************************************************************************
     * Constructs and returns a new RCalendarDialog widget.
     * <p>
     * @param The date to initialize the calendar to.
     ***************************************************************************************************/
    public RCalendarDialog(Date date) {
        initializeCalendar(date);
    }

    /****************************************************************************************************
     * Constructs and returns a new RCalendarDialog widget.
     * <p>
     * @param The parent dialog of the calendar.
     * @param The date to initialize the calendar to.
     ***************************************************************************************************/
    public RCalendarDialog(JDialog dialog, Date date) {
        super(dialog, true);
        initializeCalendar(date);
    }

    /****************************************************************************************************
     * Constructs and returns a new RCalendarDialog widget.
     * <p>
     * @param The parent frame of the calendar.
     * @param The date to initialize the calendar to.
     ***************************************************************************************************/
    public RCalendarDialog(JFrame frame, Date date) {
        super(frame, true);
        initializeCalendar(date);
    }

    /****************************************************************************************************
     * Initializes the calendar widget.
     ***************************************************************************************************/
    private void initializeCalendar(Date date) {
        setUndecorated(true);
        setTitle("Calendar");
        setSize(260, 200);

        initializeCalendars();
        initializeColors();
        initializeWidgets();
        initializeTimeZone();
        initializeParser();

        layoutCalendar();

        setDate(date);

        WindowPlacer.centerWindow(this);
    }

    /****************************************************************************************************
     * Builds the appropriate calendars.
     ***************************************************************************************************/
    private void initializeCalendars() {
        storeToday = createCalendar();
        calendar = createCalendar();
    }

    /****************************************************************************************************
     * Builds the appropriate font and color information for the calendar.
     ***************************************************************************************************/
    private void initializeColors() {
        borderColor = UIManager.getColor(UIThemeName.CALENDAR_BORDER_COLOR);
        dayTitleBackground = UIManager.getColor(UIThemeName.PANEL_BACKGROUND);
        dayForeground = UIManager.getColor(UIThemeName.CALENDAR_DAY_FOREGROUND);
        dayBackground = UIManager.getColor(UIThemeName.CALENDAR_DAY_BACKGROUND);
        dayFocusForeground = UIManager.getColor(UIThemeName.CALENDAR_DAY_FOCUS_FOREGROUND);
        dayFocusBackground = UIManager.getColor(UIThemeName.CALENDAR_DAY_FOCUS_BACKGROUND);
        dayCurrentForeground = UIManager.getColor(UIThemeName.CALENDAR_DAY_CURRENT_FOREGROUND);
        dayCurrentBackground = UIManager.getColor(UIThemeName.CALENDAR_DAY_CURRENT_BACKGROUND);

        titleLabel.setBackground(UIManager.getColor(UIThemeName.CALENDAR_TITLE_BACKGROUND));
        titleLabel.setForeground(UIManager.getColor(UIThemeName.CALENDAR_TITLE_FOREGROUND));
        titleLabel.setFont(UIManager.getFont(UIThemeName.CALENDAR_TITLE_FONT));

        timeZoneLabel.setBackground(UIManager.getColor(UIThemeName.CALENDAR_TITLE_BACKGROUND));
        timeZoneLabel.setForeground(UIManager.getColor(UIThemeName.CALENDAR_TITLE_FOREGROUND));
        timeZoneLabel.setFont(UIManager.getFont(UIThemeName.CALENDAR_TITLE_FONT));

        topPanel.setBackground(UIManager.getColor(UIThemeName.CALENDAR_BACKGROUND));
        dayPanel.setBackground(dayCurrentBackground);
    }

    /****************************************************************************************************
     * Builds the default settings for widgets within the calendar.
     ***************************************************************************************************/
    private void initializeWidgets() {
        labPanel.setBorder(new MatteBorder(1, 1, 0, 1, borderColor));
        topPanel.setBorder(new MatteBorder(1, 1, 0, 1, borderColor));

        MatteBorder outerBorder = new MatteBorder(1, 1, 1, 1, borderColor);
        EmptyBorder innerBorder = new EmptyBorder(0, 2, 0, 0);

        dayPanel.setBorder(new CompoundBorder(outerBorder, innerBorder));

        titleLabel.setOpaque(true);

        timeZoneLabel.setOpaque(true);
        timeZoneLabel.setHorizontalAlignment(RLabel.CENTER);

        yearField.setLength(4);
        yearField.setMinimumWidth(48);
        yearField.addFocusListener(createYearResetAction());

        initializeDayButtons();
        buildArrowButtons();
        buildDayTitleArray();

        monthCombo.setMinimumWidth(95);
        monthCombo.setSortEnabled(false);
        monthCombo.setItems(getMonthArray());
        monthCombo.setSelectionRequired(true);
        monthCombo.setSelectedIndex(1);
        monthCombo.addActionListener(createMonthListener());

        closeButton.addActionListener(createCloseAction());
    }

    /****************************************************************************************************
     * Initialize the day buttons and day of week at the same time.
     ***************************************************************************************************/
    private void initializeDayButtons() {
        int firstDayOfWeek = LocaleManager.getFirstDayOfWeek();
        for (int i = 0; i < 42; i++) {
            dayButtonArray[i] = buildDayButton();
            dayOfWeekArray[i] = firstDayOfWeek;
            firstDayOfWeek++;
            if (firstDayOfWeek > 7) {
                firstDayOfWeek = 1;
            }
        }
    }

    /****************************************************************************************************
     * Initialize time zone
     ***************************************************************************************************/
    private void initializeTimeZone() {
        includeTimeZone = StringUtility.booleanValue(UIManager.getString(UIThemeName.CALENDAR_TIME_ZONE));

        if (includeTimeZone) {
            TimeZone timeZone = LocaleManager.getTimeZone();
            if (timeZone != null) {
                timeZoneLabel.setText("(" + timeZone.getDisplayName(LocaleManager.getLanguageLocale()) + ")");
            }
        }
    }
    
    /****************************************************************************************************
     * Initializes the number parser.
     ***************************************************************************************************/
    private void initializeParser() {
        numberParser = NumberParser.getInstance(LocaleManager.getNumericLocale());
    }

    /****************************************************************************************************
     * Creates a listener for the month combo box change.
     ***************************************************************************************************/
    private ActionListener createMonthListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doMonthChanged();
            }
        };
    }

    /****************************************************************************************************
     * Initializes the arrow buttons.
     ***************************************************************************************************/
    private void buildArrowButtons() {
        int size = monthCombo.getPreferredSize().height;

        monthDownButton.setMinimumSize(size, size);
        monthUpButton.setMinimumSize(size, size);
        yearDownButton.setMinimumSize(size, size);
        yearUpButton.setMinimumSize(size, size);

        monthDownButton.setOpaque(true);
        monthUpButton.setOpaque(true);
        yearDownButton.setOpaque(true);
        yearUpButton.setOpaque(true);

        monthDownButton.addActionListener(createMonthDecrementAction());
        monthUpButton.addActionListener(createMonthIncrementAction());
        yearDownButton.addActionListener(createYearDecrementAction());
        yearUpButton.addActionListener(createYearIncrementAction());
    }

    /****************************************************************************************************
     * Creates the month increment action.
     ***************************************************************************************************/
    private ActionListener createMonthIncrementAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int month = monthCombo.getSelectedIndex() + 1;
                if (month == 12) {
                    doYearChanged(+1);
                    month = 0;
                }
                monthCombo.setSelectedItem(monthArray[month]);
            }
        };
    }

    /****************************************************************************************************
     * Creates the month decrement action.
     ***************************************************************************************************/
    private ActionListener createMonthDecrementAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int month = monthCombo.getSelectedIndex() - 1;
                if (month == -1) {
                    doYearChanged(-1);
                    month = 11;
                }
                monthCombo.setSelectedItem(monthArray[month]);
            }
        };
    }

    /****************************************************************************************************
     * Creates the year increment action.
     ***************************************************************************************************/
    private ActionListener createYearIncrementAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doYearChanged(+1);
            }
        };
    }

    /****************************************************************************************************
     * Creates the year decrement action.
     ***************************************************************************************************/
    private ActionListener createYearDecrementAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doYearChanged(-1);
            }
        };
    }

    /****************************************************************************************************
     * Creates the year focus listener that repopulates the widget if the year changes.
     ***************************************************************************************************/
    private FocusListener createYearResetAction() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
            }

            public void focusLost(FocusEvent event) {
                int year = calendar.get(GregorianCalendar.YEAR);
                try {
                    int value = numberParser.getIntValue(yearField.getText());
                    if (year != value) {
                        calendar.set(GregorianCalendar.YEAR, value);
                        doYearChanged(0);
                    }
                } catch (Exception exception) {
                    calendar.set(GregorianCalendar.YEAR, year);
                    yearField.setText(String.valueOf(year));
                }
            }
        };
    }

    /****************************************************************************************************
     * Creates the action that closes the dialog.
     ***************************************************************************************************/
    private ActionListener createCloseAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        };
    }

    /****************************************************************************************************
     * Lays out the widgets with the RCalendar.
     ***************************************************************************************************/
    private void layoutCalendar() {
        labPanel.setLayout(new BorderLayout());
        labPanel.add(titleLabel, BorderLayout.WEST);
        labPanel.add(timeZoneLabel, BorderLayout.CENTER);
        labPanel.add(closeButton, BorderLayout.EAST);

        topPanel.setLayout(new GridBagLayout());
        topPanel.add(monthDownButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 3, 1, 3, 0));
        topPanel.add(monthCombo, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 3, 0, 3, 0));
        topPanel.add(monthUpButton, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 3, 0, 3, 5));
        topPanel.add(yearDownButton, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 3, 0, 3, 0));
        topPanel.add(yearField, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 1, 3, 0, 3, 0));
        topPanel.add(yearUpButton, GridTool.constraints(5, 0, 1, 1, 0, 0, 0, 0, 3, 0, 3, 5));

        dayPanel.setLayout(new GridLayout(7, 7, 1, 1));

        for (int i = 0; i < 7; i++) {
            dayPanel.add(dayTitleArray[i]);
        }
        for (int i = 0; i < 42; i++) {
            dayPanel.add(dayButtonArray[i]);
        }

        Container container = getContentPane();

        container.setLayout(new GridBagLayout());
        container.add(labPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        container.add(topPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        container.add(dayPanel, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Builds a new calendar day button.
     * <p>
     * @return A new calendar day button.
     ***************************************************************************************************/
    private RButton buildDayButton() {
        RButton button = new RButton();

        button.setBorder(null);
        button.setChromeActivated(false);
        button.setForeground(dayForeground);
        button.setBackground(dayBackground);
        button.addActionListener(createSelectedCommand());
        button.addFocusListener(createDayFocusListener());

        return button;
    }

    /****************************************************************************************************
     * Creates the selected date listener for the day buttons.
     ***************************************************************************************************/
    private ActionListener createSelectedCommand() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doDateSelected(event.getActionCommand());
            }
        };
    }

    /****************************************************************************************************
     * Creates the button focus listener that alternates colors.
     ***************************************************************************************************/
    private FocusListener createDayFocusListener() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                RButton button = (RButton) event.getSource();
                button.setForeground(dayFocusForeground);
                button.setBackground(dayFocusBackground);
                storeDay = button.getText();
            }

            public void focusLost(FocusEvent event) {
                RButton button = (RButton) event.getSource();
                String checkDay = String.valueOf(storeToday.get(GregorianCalendar.DATE));
                if (checkDay.equals(button.getText())) {
                    button.setForeground(dayCurrentForeground);
                    button.setBackground(dayCurrentBackground);
                } else {
                    button.setForeground(dayForeground);
                    button.setBackground(dayBackground);
                }
            }
        };
    }

    /****************************************************************************************************
     * Assigns a title to the RCalendarDialog title bar. This title is automatically language translated.
     * <p>
     * @param text The title to assign to the RCalendarDialog widget title bar.
     ***************************************************************************************************/
    public void setTitle(String text) {
        super.setTitle(Translator.getText(text));
        titleLabel.setText(text);
    }

    /****************************************************************************************************
     * Assigns a valid date range to the calendar. The calendar will not let an invalid day be selected.
     * <p>
     * @param startDate The start date (this value may be null).
     * @param endDate The end date (this value may be null).
     ***************************************************************************************************/
    public void setValidDateRange(Date startDate, Date endDate) {
        startDateCalendar = null;
        endDateCalendar = null;

        if (startDate != null) {
            startDateCalendar = createCalendar();
            startDateCalendar.setTime(startDate);
        }
        if (endDate != null) {
            endDateCalendar = createCalendar();
            endDateCalendar.setTime(endDate);
        }
        if (startDateCalendar != null || endDateCalendar != null) {
            resetDays();
        }
    }

    /****************************************************************************************************
     * Sets the date.
     * <p>
     * @param date The date.
     ***************************************************************************************************/
    public void setDate(Date date) {
        if (date == null) {
            date = SimDateUtil.getCurrentDate();
        }
        calendar.setTime(date);
        displayDate();
    }

    /****************************************************************************************************
     * Resets the screen when the year has been modified.
     * <p>
     * @param change The number of years the year has changed.
     ***************************************************************************************************/
    private void doYearChanged(int change) {
        calendar.add(GregorianCalendar.YEAR, change);
        yearField.setText(String.valueOf(calendar.get(GregorianCalendar.YEAR)));
        resetDays();
    }

    /****************************************************************************************************
     * Resets the screen when the month has been modified. Stores the day of the month and then validates
     * that it can fit in the new month, otherwise it sets it to the last day of the month.
     ***************************************************************************************************/
    private void doMonthChanged() {
        RMonth month = (RMonth) monthCombo.getSelectedItem();
        if (storeMonth == month.monthNumber) {
            return;
        }
        int today = calendar.get(GregorianCalendar.DATE);

        calendar.set(GregorianCalendar.DATE, 1);
        calendar.set(GregorianCalendar.MONTH, month.monthNumber);

        int maxDay = calendar.getActualMaximum(GregorianCalendar.DATE);
        if (today > maxDay) {
            today = maxDay;
        }
        calendar.set(GregorianCalendar.DATE, today);
        storeMonth = month.monthNumber;

        resetDays();
    }

    /****************************************************************************************************
     * Displays the date and highlights the current date.
     ***************************************************************************************************/
    private void displayDate() {
        yearField.setText(String.valueOf(calendar.get(GregorianCalendar.YEAR)));
        monthCombo.setSelectedIndex(calendar.get(GregorianCalendar.MONTH));
        resetDays();
    }

    /****************************************************************************************************
     * Resets all the numeric values on the days of the calendar.
     ***************************************************************************************************/
    private void resetDays() {
        int today = calendar.get(GregorianCalendar.DATE);

        calendar.set(GregorianCalendar.DATE, 1);

        int dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);

        calendar.set(GregorianCalendar.DATE, today);

        int index = -1;
        for (int i = 0; i < 42; i++) {
            if (dayOfWeekArray[i] == dayOfWeek) {
                index = i;
                break;
            }
            dayButtonArray[i].setText(StringConstants.EMPTY);
            dayButtonArray[i].setEnabled(false);
        }

        int maxDays = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        for (int i = 0; i < maxDays; i++) {
            dayButtonArray[index].setText(String.valueOf(i + 1));
            dayButtonArray[index].setForeground(dayForeground);
            dayButtonArray[index].setBackground(dayBackground);
            dayButtonArray[index].setActionCommand(String.valueOf(i + 1));
            dayButtonArray[index].setEnabled(true);
            index++;
        }
        for (int i = index; i < 42; i++) {
            dayButtonArray[i].setText(StringConstants.EMPTY);
            dayButtonArray[i].setForeground(dayForeground);
            dayButtonArray[i].setBackground(dayBackground);
            dayButtonArray[i].setEnabled(false);
        }
        if (startDateCalendar != null) {
            deactivateBeforeStartDate();
        }
        if (endDateCalendar != null) {
            deactivateAfterEndDate();
        }
        resetGrayDay();
        highlightDay();
    }

    /****************************************************************************************************
     * Reset the grayed out day.
     ***************************************************************************************************/
    private void resetGrayDay() {
        if (calendar.get(GregorianCalendar.YEAR) == storeToday.get(GregorianCalendar.YEAR)) {
            if (calendar.get(GregorianCalendar.MONTH) == storeToday.get(GregorianCalendar.MONTH)) {
                String day = String.valueOf(storeToday.get(GregorianCalendar.DATE));
                for (int i = 0; i < 42; i++) {
                    if (day.equals(dayButtonArray[i].getText())) {
                        dayButtonArray[i].setForeground(dayCurrentForeground);
                        dayButtonArray[i].setBackground(dayCurrentBackground);
                        break;
                    }
                }
            }
        }
    }

    /****************************************************************************************************
     * Deactive all displayed days before the start date.
     ***************************************************************************************************/
    private void deactivateBeforeStartDate() {
        int currentYear = calendar.get(GregorianCalendar.YEAR);
        int testDateYear = startDateCalendar.get(GregorianCalendar.YEAR);
        int currentMonth = calendar.get(GregorianCalendar.MONTH);
        int testDateMonth = startDateCalendar.get(GregorianCalendar.MONTH);
        int deactivate = NO_DEACTIVATE;

        if (currentYear < testDateYear) {
            deactivate = FULL_DEACTIVATE;
        }
        if (deactivate == NO_DEACTIVATE && currentYear == testDateYear) {
            if (currentMonth < testDateMonth) {
                deactivate = FULL_DEACTIVATE;
            } else if (currentMonth == testDateMonth) {
                deactivate = DAY_DEACTIVATE;
            }
        }

        switch (deactivate) {
            case NO_DEACTIVATE:
                return;
            case FULL_DEACTIVATE:
                for (int i = 0; i < 42; i++) {
                    dayButtonArray[i].setForeground(ColorUtility.disabledTint(dayForeground));
                    dayButtonArray[i].setEnabled(false);
                }
                break;
            case DAY_DEACTIVATE:
                int startDateDay = startDateCalendar.get(GregorianCalendar.DATE);
                int checkDay = -1;
                for (int i = 0; i < 42; i++) {
                    try {
                        checkDay = numberParser.getIntValue(dayButtonArray[i].getText());
                    } catch (Throwable exception) {
                        checkDay = Integer.MAX_VALUE;
                    }
                    if (checkDay < startDateDay) {
                        dayButtonArray[i].setForeground(ColorUtility.disabledTint(dayForeground));
                        dayButtonArray[i].setEnabled(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    /****************************************************************************************************
     * Deactive all displayed days after the end date.
     ***************************************************************************************************/
    private void deactivateAfterEndDate() {
        int currentYear = calendar.get(GregorianCalendar.YEAR);
        int testDateYear = endDateCalendar.get(GregorianCalendar.YEAR);
        int currentMonth = calendar.get(GregorianCalendar.MONTH);
        int testDateMonth = endDateCalendar.get(GregorianCalendar.MONTH);
        int deactivate = NO_DEACTIVATE;

        if (currentYear > testDateYear) {
            deactivate = FULL_DEACTIVATE;
        }
        if (deactivate == NO_DEACTIVATE && currentYear == testDateYear) {
            if (currentMonth > testDateMonth) {
                deactivate = FULL_DEACTIVATE;
            } else if (currentMonth == testDateMonth) {
                deactivate = DAY_DEACTIVATE;
            }
        }

        switch (deactivate) {
            case NO_DEACTIVATE:
                return;
            case FULL_DEACTIVATE:
                for (int i = 0; i < 42; i++) {
                    dayButtonArray[i].setForeground(ColorUtility.disabledTint(dayForeground));
                    dayButtonArray[i].setEnabled(false);
                }
                break;
            case DAY_DEACTIVATE:
                int endDateDay = endDateCalendar.get(GregorianCalendar.DATE);
                int checkDay = -1;
                for (int i = 0; i < 42; i++) {
                    try {
                        checkDay = numberParser.getIntValue(dayButtonArray[i].getText());
                    } catch (Throwable exception) {
                        checkDay = -1;
                    }
                    if (checkDay > endDateDay) {
                        dayButtonArray[i].setForeground(ColorUtility.disabledTint(dayForeground));
                        dayButtonArray[i].setEnabled(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    /****************************************************************************************************
     * Triggered when a date is selected.
     * <p>
     * @param day The day label in the button that was pressed.
     ***************************************************************************************************/
    private void doDateSelected(String day) {
        if (!StringUtility.isNullOrEmpty(day)) {
            try {
                calendar.set(GregorianCalendar.DATE, numberParser.getIntValue(day));
            } catch (ParseException exception) {
                return;
            }
            firePropertyChange(UIPropertyName.CALENDAR_DATE_SELECTED, null, calendar.getTime());
            dispose();
        }
    }

    /****************************************************************************************************
     * Implements window focus listener methods to dispose of dialog if it looses focus.
     ***************************************************************************************************/
    public void windowGainedFocus(WindowEvent event) {
    }

    public void windowLostFocus(WindowEvent event) {
        dispose();
    }

    /****************************************************************************************************
     * Overrides the superclass method to select the day when the window is displayed.
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        if (visible) {
            SwingUtilities.invokeLater(new HighlightAction());
        }
        super.setVisible(visible);
    }

    /****************************************************************************************************
     * Highlights the currently selected day.
     ***************************************************************************************************/
    private void highlightDay() {
        for (int i = 0; i < 42; i++) {
            if (storeDay.equals(dayButtonArray[i].getText())) {
                dayButtonArray[i].requestFocusInWindow();
                break;
            }
        }
    }

    /****************************************************************************************************
     * Builds the array of day titles for the week. Note: This method needs to be localized.
     ***************************************************************************************************/
    private void buildDayTitleArray() {
        int firstDayOfWeek = LocaleManager.getFirstDayOfWeek();
        String[] weekdays = getWeekdayNames();
        for (int i = 0; i < 7; i++) {
            dayTitleArray[i] = buildDayTitleLabel(weekdays[firstDayOfWeek]);
            firstDayOfWeek = firstDayOfWeek + 1;
            if (firstDayOfWeek > 7) {
                firstDayOfWeek = 1;
            }
        }
    }

    /****************************************************************************************************
     * Retrieves the weekday names to display. Per internationalization team, Chinese should only display
     * the last two characters. Japanese should only display the first character.
     ***************************************************************************************************/
    private String[] getWeekdayNames() {
        DateFormatSymbols symbols = LocaleManager.getDateFormatSymbols();
        String[] weekdays = symbols.getShortWeekdays();
        if (LocaleManager.getLanguageLocale().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            for (int i = 1; i < weekdays.length; i++) {
                weekdays[i] = weekdays[i].substring(1);
            }
        }
        if (LocaleManager.getLanguageLocale().getLanguage().equals(Locale.JAPANESE.getLanguage())) {
            for (int i = 1; i < weekdays.length; i++) {
                weekdays[i] = weekdays[i].substring(0, 1);
            }
        }
        return weekdays;
    }

    /****************************************************************************************************
     * Builds a new calendar day title label.
     * <p>
     * @return A new calendar day title label.
     ***************************************************************************************************/
    private RLabel buildDayTitleLabel(String dayTitle) {
        RLabel label = new RLabel(dayTitle);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBackground(dayTitleBackground);
        label.setOpaque(true);
        return label;
    }

    /****************************************************************************************************
     * Builds the selection array of months. Note: This method needs to be localized.
     ***************************************************************************************************/
    private RMonth[] getMonthArray() {
        String[] months = LocaleManager.getDateFormatSymbols().getMonths();
        monthArray[0] = new RMonth(months[0], 0);
        monthArray[1] = new RMonth(months[1], 1);
        monthArray[2] = new RMonth(months[2], 2);
        monthArray[3] = new RMonth(months[3], 3);
        monthArray[4] = new RMonth(months[4], 4);
        monthArray[5] = new RMonth(months[5], 5);
        monthArray[6] = new RMonth(months[6], 6);
        monthArray[7] = new RMonth(months[7], 7);
        monthArray[8] = new RMonth(months[8], 8);
        monthArray[9] = new RMonth(months[9], 9);
        monthArray[10] = new RMonth(months[10], 10);
        monthArray[11] = new RMonth(months[11], 11);
        return monthArray;
    }

    /****************************************************************************************************
     *
     * Private inner class that defines a month.
     *
     ***************************************************************************************************/
    private class RMonth {

        public String monthName = "";
        public int monthNumber;

        public RMonth(String name, int number) {
            monthName = name;
            monthNumber = number;
        }

        public String toString() {
            return monthName;
        }
    }

    /****************************************************************************************************
     *
     * Inner Class to represent highlighting the initial button.
     *
     ***************************************************************************************************/
    private class HighlightAction implements Runnable {

        public void run() {
            storeToday = createCalendar();
            if (includeTimeZone) {
                if (LocaleManager.getTimeZone() != null) {
                    storeToday.setTimeZone(LocaleManager.getTimeZone());
                }
            }
            storeToday.setTime(calendar.getTime());
            storeDay = String.valueOf(calendar.get(GregorianCalendar.DATE));
            highlightDay();
        }
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
