package oracle.retail.sim.client.screen.productgroup;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.panel.RCardPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RRadioButton;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.schedule.DailyByWeekdaySchedule;
import oracle.retail.sim.common.schedule.DailySchedule;
import oracle.retail.sim.common.schedule.MonthlyByDaySchedule;
import oracle.retail.sim.common.schedule.MonthlyByWeekSchedule;
import oracle.retail.sim.common.schedule.Schedule;
import oracle.retail.sim.common.schedule.ScheduleType;
import oracle.retail.sim.common.schedule.WeeklySchedule;
import oracle.retail.sim.common.schedule.YearlyByDaySchedule;
import oracle.retail.sim.common.schedule.YearlyByWeekSchedule;

/********************************************************************************************************
 * Product Group Schedule Option Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleOptionPanel extends RCardPanel {
    private static final long serialVersionUID = -256525628336619099L;

    private DayPanel dayPanel = new DayPanel();
    private WeekPanel weekPanel = new WeekPanel();
    private MonthPanel monthPanel = new MonthPanel();
    private YearPanel yearPanel = new YearPanel();

    public ProductGroupScheduleOptionPanel() {
        addCard("DayPanel", dayPanel);
        addCard("WeekPanel", weekPanel);
        addCard("MonthPanel", monthPanel);
        addCard("YearPanel", yearPanel);
    }

    public void showDayPanel() {
        showCard(dayPanel);
    }

    public void showWeekPanel() {
        showCard(weekPanel);
    }

    public void showMonthPanel() {
        showCard(monthPanel);
    }

    public void showYearPanel() {
        showCard(yearPanel);
    }

    public void setSchedule(Schedule schedule) {
        clearSchedule();

        ScheduleType type = schedule.getType();

        if (type == ScheduleType.DAILY) {
            dayPanel.setSchedule(schedule);
        } else if (type == ScheduleType.DAILY_BY_WEEKDAY) {
            dayPanel.setSchedule(schedule);
        } else if (type == ScheduleType.WEEKLY) {
            weekPanel.setSchedule(schedule);
        } else if (type == ScheduleType.MONTHLY_BY_DAY) {
            monthPanel.setSchedule(schedule);
        } else if (type == ScheduleType.MONTHLY_BY_WEEK) {
            monthPanel.setSchedule(schedule);
        } else {
            yearPanel.setSchedule(schedule);
        }
    }

    public Schedule getSchedule() throws Exception {
        Component card = getCard();

        if (card == dayPanel) {
            return dayPanel.getSchedule();
        }
        if (card == weekPanel) {
            return weekPanel.getSchedule();
        }
        if (card == monthPanel) {
            return monthPanel.getSchedule();
        }
        return yearPanel.getSchedule();
    }

    public void clearSchedule() {
        dayPanel.clear();
        weekPanel.clear();
        monthPanel.clear();
        yearPanel.clear();
    }

    public void setScheduleEnabled(boolean enabled) {
        dayPanel.setScheduleEnabled(enabled);
        weekPanel.setScheduleEnabled(enabled);
        monthPanel.setScheduleEnabled(enabled);
        yearPanel.setScheduleEnabled(enabled);
    }

    /****************************************************************************************************
     * DAY PANEL
     ***************************************************************************************************/

    private class DayPanel extends RPanel {
        private static final long serialVersionUID = 7621716878335299820L;

        private DailyByWeekdaySchedule dailyByWeekSchedule;
        private DailySchedule dailySchedule;

        private RRadioButton everyDayButton = new RRadioButton("Every");
        private RIntegerFieldEditor everyDayField = new RIntegerFieldEditor();
        private RLabel everyDayLabel = new RLabel("day(s)");
        private RRadioButton everyWeekdayButton = new RRadioButton("Every Weekday");

        private DayPanel() {
            super(new GridBagLayout());

            everyDayField.setIdentifier(SimName.SCHEDULE_DAY);
            everyDayButton.setHorizontalTextPosition(SwingConstants.RIGHT);
            everyWeekdayButton.setHorizontalTextPosition(SwingConstants.RIGHT);
            everyDayButton.setSelected(true);
            everyDayField.setMinimumWidth(50);
            everyDayField.setMinimumValue(1);

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(everyDayButton);
            buttonGroup.add(everyWeekdayButton);

            add(everyDayButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 5));
            add(everyDayField, GridTool.constraints(1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 5));
            add(everyDayLabel, GridTool.constraints(2, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0));
            add(new RLabel(), GridTool.constraints(3, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
            add(everyWeekdayButton, GridTool.constraints(0, 1, 3, 1, 0, 0, 1, 0, 0, 0, 0, 0));
            add(new RLabel(), GridTool.constraints(0, 2, 1, 1, 0, 1, 0, 2, 0, 0, 0, 0));
        }

        public void setSchedule(Schedule schedule) {
            if (schedule instanceof DailyByWeekdaySchedule) {
                dailyByWeekSchedule = (DailyByWeekdaySchedule) schedule;
                dailySchedule = BOFactory.createDailySchedule();
                everyWeekdayButton.setSelected(true);
            } else {
                dailySchedule = (DailySchedule) schedule;
                dailyByWeekSchedule = BOFactory.createDailyByWeekdaySchedule();
                everyDayButton.setSelected(true);
                everyDayField.setInteger(dailySchedule.getDailyFrequency());
            }
        }

        public Schedule getSchedule() throws Exception {
            if (everyWeekdayButton.isSelected()) {
                if (dailyByWeekSchedule == null) {
                    dailyByWeekSchedule = BOFactory.createDailyByWeekdaySchedule();
                }
                return dailyByWeekSchedule;
            }
            if (everyDayButton.isSelected()) {
                if (dailySchedule == null) {
                    dailySchedule = BOFactory.createDailySchedule();
                }
                if (everyDayField.getIntegerValue() < 1) {
                    throw new BusinessException(CommonMessageText.QUANTITY_NOT_POSITIVE);
                }
                dailySchedule.setDailyFrequency(everyDayField.getInteger());
                return dailySchedule;
            }
            return BOFactory.createDailyByWeekdaySchedule();
        }

        public void setScheduleEnabled(boolean enabled) {
            everyDayButton.setEnabled(enabled);
            everyWeekdayButton.setEnabled(enabled);
            everyDayField.setEnabled(enabled);
        }

        public void clear() {
            everyDayButton.setSelected(false);
            everyWeekdayButton.setSelected(false);
            everyDayField.setInteger(1L);
        }
    }

    /****************************************************************************************************
     * WEEK PANEL
     ***************************************************************************************************/

    private class WeekPanel extends RPanel {
        private static final long serialVersionUID = -163841245044119944L;

        private WeeklySchedule weekSchedule;

        private RLabel recursLabel = new RLabel("Recurs every");
        private RIntegerFieldEditor weekEditor = new RIntegerFieldEditor();
        private RLabel weekLabel = new RLabel("week(s) on:");

        private RCheckBoxEditor sundayEditor = new RCheckBoxEditor("Sunday");
        private RCheckBoxEditor mondayEditor = new RCheckBoxEditor("Monday");
        private RCheckBoxEditor tuesdayEditor = new RCheckBoxEditor("Tuesday");
        private RCheckBoxEditor wednesdayEditor = new RCheckBoxEditor("Wednesday");
        private RCheckBoxEditor thursdayEditor = new RCheckBoxEditor("Thursday");
        private RCheckBoxEditor fridayEditor = new RCheckBoxEditor("Friday");
        private RCheckBoxEditor saturdayEditor = new RCheckBoxEditor("Saturday");

        private WeekPanel() {
            super(new GridBagLayout());

            weekEditor.setSizeType(EditorConstants.TINY);
            weekEditor.setIdentifier(SimName.SCHEDULE_WEEK);
            weekEditor.setMinimumValue(1);

            initCheckBox(sundayEditor);
            initCheckBox(mondayEditor);
            initCheckBox(tuesdayEditor);
            initCheckBox(wednesdayEditor);
            initCheckBox(thursdayEditor);
            initCheckBox(fridayEditor);
            initCheckBox(saturdayEditor);

            RPanel topPanel = new RPanel(new GridBagLayout());
            topPanel.add(recursLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            topPanel.add(weekEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 5, 0, 0));
            topPanel.add(weekLabel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            topPanel.add(new RLabel(), GridTool.constraints(3, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

            RPanel bottomPanel = new RPanel(new GridBagLayout());
            bottomPanel.add(sundayEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            bottomPanel.add(mondayEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            bottomPanel.add(tuesdayEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            bottomPanel.add(wednesdayEditor, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            bottomPanel.add(thursdayEditor, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            bottomPanel.add(fridayEditor, GridTool.constraints(1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            bottomPanel.add(saturdayEditor, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 5));
            bottomPanel.add(new RLabel(), GridTool.constraints(3, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 5));

            add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
            add(bottomPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 5, 0, 0, 0));
        }

        private void initCheckBox(RCheckBoxEditor editor) {
            editor.setTitleAlignment(EditorConstants.RIGHT);
        }

        public void setSchedule(Schedule schedule) {
            weekSchedule = (WeeklySchedule) schedule;

            weekEditor.setInteger(weekSchedule.getWeeklyFrequency());

            setDaySelected(Calendar.SUNDAY, sundayEditor);
            setDaySelected(Calendar.MONDAY, mondayEditor);
            setDaySelected(Calendar.TUESDAY, tuesdayEditor);
            setDaySelected(Calendar.WEDNESDAY, wednesdayEditor);
            setDaySelected(Calendar.THURSDAY, thursdayEditor);
            setDaySelected(Calendar.FRIDAY, fridayEditor);
            setDaySelected(Calendar.SATURDAY, saturdayEditor);
        }

        private void setDaySelected(int day, RCheckBoxEditor editor) {
            editor.setSelected(weekSchedule.isDayOfTheWeekValid(day));
        }

        public Schedule getSchedule() throws Exception {
            if (weekSchedule == null) {
                weekSchedule = BOFactory.createWeeklySchedule();
            }
            Set<Integer> daysOfWeek = new HashSet<>();
            if (sundayEditor.isSelected()) {
                daysOfWeek.add(Calendar.SUNDAY);
            }
            if (mondayEditor.isSelected()) {
                daysOfWeek.add(Calendar.MONDAY);
            }
            if (tuesdayEditor.isSelected()) {
                daysOfWeek.add(Calendar.TUESDAY);
            }
            if (wednesdayEditor.isSelected()) {
                daysOfWeek.add(Calendar.WEDNESDAY);
            }
            if (thursdayEditor.isSelected()) {
                daysOfWeek.add(Calendar.THURSDAY);
            }
            if (fridayEditor.isSelected()) {
                daysOfWeek.add(Calendar.FRIDAY);
            }
            if (saturdayEditor.isSelected()) {
                daysOfWeek.add(Calendar.SATURDAY);
            }
            weekSchedule.setDaysOfTheWeek(daysOfWeek);

            if (weekEditor.getIntegerValue() < 1) {
                throw new BusinessException(CommonMessageText.QUANTITY_NOT_POSITIVE);
            }
            weekSchedule.setWeeklyFrequency(weekEditor.getInteger());

            return weekSchedule;
        }

        public void setScheduleEnabled(boolean enabled) {
            weekEditor.setEnabled(enabled);
            sundayEditor.setEnabled(enabled);
            mondayEditor.setEnabled(enabled);
            tuesdayEditor.setEnabled(enabled);
            wednesdayEditor.setEnabled(enabled);
            thursdayEditor.setEnabled(enabled);
            fridayEditor.setEnabled(enabled);
            saturdayEditor.setEnabled(enabled);
        }

        public void clear() {
            weekEditor.setInteger(1L);
            sundayEditor.setSelected(true);
            mondayEditor.setSelected(false);
            tuesdayEditor.setSelected(false);
            wednesdayEditor.setSelected(false);
            thursdayEditor.setSelected(false);
            fridayEditor.setSelected(false);
            saturdayEditor.setSelected(false);
        }
    }

    /****************************************************************************************************
     * MONTH PANEL
     ***************************************************************************************************/

    private class MonthPanel extends RPanel {
        private static final long serialVersionUID = 5450062996929659402L;

        private MonthlyByDaySchedule monthByDaySchedule;
        private MonthlyByWeekSchedule monthByWeekSchedule;

        private RRadioButton dayRadioButton = new RRadioButton("Day");
        private RIntegerFieldEditor topDateEditor = new RIntegerFieldEditor();
        private RLabel topOfLabel = new RLabel("of every");
        private RIntegerFieldEditor topMonthEditor = new RIntegerFieldEditor();
        private RLabel topMonthLabel = new RLabel("months(s)");

        private RRadioButton theRadioButton = new RRadioButton("The");
        private RComboBoxEditor dayCountEditor = SimEditorFactory.createOrdinalEditor("", 5);
        private RComboBoxEditor dayEditor = SimEditorFactory.createDayOfWeekEditor("");
        private RLabel ofLabel = new RLabel("of every");
        private RIntegerFieldEditor monthEditor = new RIntegerFieldEditor();
        private RLabel monthLabel = new RLabel("months(s)");

        private MonthPanel() {
            super(new GridBagLayout());

            dayRadioButton.setHorizontalTextPosition(EditorConstants.RIGHT);
            theRadioButton.setHorizontalTextPosition(EditorConstants.RIGHT);
            dayRadioButton.setHorizontalAlignment(EditorConstants.LEFT);
            theRadioButton.setHorizontalAlignment(EditorConstants.LEFT);

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(dayRadioButton);
            buttonGroup.add(theRadioButton);

            topDateEditor.setSizeType(EditorConstants.TINY);
            topDateEditor.setIdentifier(SimName.SCHEDULE_DAY_OF_MONTH);
            topDateEditor.setMinimumValue(1);
            topMonthEditor.setSizeType(EditorConstants.SMALL);
            topMonthEditor.setIdentifier(SimName.SCHEDULE_MONTH);
            topMonthEditor.setMinimumValue(1);
            dayCountEditor.setSizeType(EditorConstants.SMALL);
            dayEditor.setSizeType(EditorConstants.SMALL);
            monthEditor.setSizeType(EditorConstants.TINY);
            monthEditor.setIdentifier(SimName.SCHEDULE_MONTH);
            monthEditor.setMinimumValue(1);

            RPanel topPanel = new RPanel(new GridBagLayout());
            topPanel.add(dayRadioButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            topPanel.add(topDateEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            topPanel.add(topOfLabel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 8));
            topPanel.add(topMonthEditor, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            topPanel.add(topMonthLabel, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            topPanel.add(new RLabel(), GridTool.constraints(5, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

            RPanel bottomPanel = new RPanel(new GridBagLayout());
            bottomPanel.add(theRadioButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            bottomPanel.add(dayCountEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            bottomPanel.add(dayEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            bottomPanel.add(ofLabel, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 8));
            bottomPanel.add(monthEditor, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            bottomPanel.add(monthLabel, GridTool.constraints(5, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            bottomPanel.add(new RLabel(), GridTool.constraints(6, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

            add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
            add(bottomPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 5, 0, 0, 0));
        }

        public void setSchedule(Schedule schedule) {
            if (schedule instanceof MonthlyByDaySchedule) {
                monthByDaySchedule = (MonthlyByDaySchedule) schedule;
                monthByWeekSchedule = BOFactory.createMonthlyByWeekSchedule();
                dayRadioButton.setSelected(true);
                topDateEditor.setInteger(monthByDaySchedule.getDayOfTheMonth());
                topMonthEditor.setInteger(monthByDaySchedule.getMonthlyFrequency());
            } else {
                monthByWeekSchedule = (MonthlyByWeekSchedule) schedule;
                monthByDaySchedule = BOFactory.createMonthlyByDaySchedule();
                theRadioButton.setSelected(true);
                dayCountEditor.setSelectedItem(monthByWeekSchedule.getWeekOfTheMonth());
                dayEditor.setSelectedItem(monthByWeekSchedule.getDayOfTheWeek());
                monthEditor.setInteger(monthByWeekSchedule.getMonthlyFrequency());
            }
        }

        public Schedule getSchedule() throws Exception {
            if (dayRadioButton.isSelected()) {
                if (monthByDaySchedule == null) {
                    monthByDaySchedule = BOFactory.createMonthlyByDaySchedule();
                }
                if (topDateEditor.getIntegerValue() < 1) {
                    throw new BusinessException(CommonMessageText.QUANTITY_NOT_POSITIVE);
                }
                if (topMonthEditor.getIntegerValue() < 1) {
                    throw new BusinessException(CommonMessageText.QUANTITY_NOT_POSITIVE);
                }
                monthByDaySchedule.setDayOfTheMonth(topDateEditor.getInteger());
                monthByDaySchedule.setMonthlyFrequency(topMonthEditor.getInteger());
                return monthByDaySchedule;
            }
            if (theRadioButton.isSelected()) {
                if (monthByWeekSchedule == null) {
                    monthByWeekSchedule = BOFactory.createMonthlyByWeekSchedule();
                }
                if (monthEditor.getIntegerValue() < 1) {
                    throw new BusinessException(CommonMessageText.QUANTITY_NOT_POSITIVE);
                }
                monthByWeekSchedule.setWeekOfTheMonth((Integer) dayCountEditor.getSelectedItem());
                monthByWeekSchedule.setDayOfTheWeek((Integer) dayEditor.getSelectedItem());
                monthByWeekSchedule.setMonthlyFrequency(monthEditor.getInteger());
                return monthByWeekSchedule;
            }
            return null;
        }

        public void setScheduleEnabled(boolean enabled) {
            dayRadioButton.setEnabled(enabled);
            topDateEditor.setEnabled(enabled);
            topMonthEditor.setEnabled(enabled);
            theRadioButton.setEnabled(enabled);
            dayCountEditor.setEnabled(enabled);
            dayEditor.setEnabled(enabled);
            monthEditor.setEnabled(enabled);
        }

        public void clear() {
            dayRadioButton.setSelected(true);
            topDateEditor.setInteger(1L);
            topMonthEditor.setInteger(1L);
            theRadioButton.setSelected(false);
            dayCountEditor.setEmptySelection();
            dayEditor.setEmptySelection();
            monthEditor.clear();
        }
    }

    /****************************************************************************************************
     * YEAR PANEL
     ***************************************************************************************************/

    private class YearPanel extends RPanel {
        private static final long serialVersionUID = -5852426279228042368L;

        private YearlyByDaySchedule yearByDaySchedule;
        private YearlyByWeekSchedule yearByWeekSchedule;

        private RRadioButton everyRadioButton = new RRadioButton("Every");
        private RIntegerFieldEditor topDateEditor = new RIntegerFieldEditor();
        private RComboBoxEditor topMonthEditor = SimEditorFactory.createMonthEditor("");
        private RRadioButton theRadioButton = new RRadioButton("The");
        private RComboBoxEditor dayCountEditor = SimEditorFactory.createOrdinalEditor("", 5);
        private RComboBoxEditor dayEditor = SimEditorFactory.createDayOfWeekEditor("");
        private RLabel ofLabel = new RLabel("of");
        private RComboBoxEditor monthEditor = SimEditorFactory.createMonthEditor("");

        private YearPanel() {
            super(new GridBagLayout());

            everyRadioButton.setHorizontalTextPosition(EditorConstants.RIGHT);
            theRadioButton.setHorizontalTextPosition(EditorConstants.RIGHT);
            everyRadioButton.setHorizontalAlignment(EditorConstants.LEFT);
            theRadioButton.setHorizontalAlignment(EditorConstants.LEFT);

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(everyRadioButton);
            buttonGroup.add(theRadioButton);

            topDateEditor.setSizeType(EditorConstants.SMALL);
            topDateEditor.setIdentifier(SimName.SCHEDULE_DAY_OF_MONTH);
            topDateEditor.setMinimumValue(1);
            topMonthEditor.setSizeType(EditorConstants.SMALL);
            dayCountEditor.setSizeType(EditorConstants.SMALL);
            dayCountEditor.setIdentifier(SimName.SCHEDULE_DAY_OF_YEAR);
            dayEditor.setSizeType(EditorConstants.SMALL);
            monthEditor.setSizeType(EditorConstants.SMALL);

            add(everyRadioButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            add(topMonthEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            add(topDateEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 5, 0));
            add(theRadioButton, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 5, 0, 0, 0));
            add(dayCountEditor, GridTool.constraints(1, 1, 1, 1, 0, 0, 0, 1, 5, 0, 0, 0));
            add(dayEditor, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 1, 5, 0, 0, 0));
            add(ofLabel, GridTool.constraints(3, 1, 1, 1, 0, 0, 0, 1, 5, 0, 0, 5));
            add(monthEditor, GridTool.constraints(4, 1, 1, 1, 0, 0, 0, 1, 5, 5, 0, 0));
            add(new RLabel(), GridTool.constraints(5, 1, 1, 1, 1, 0, 0, 1, 5, 0, 0, 0));
        }

        public void setSchedule(Schedule schedule) {
            if (schedule instanceof YearlyByDaySchedule) {
                yearByDaySchedule = (YearlyByDaySchedule) schedule;
                yearByWeekSchedule = BOFactory.createYearlyByWeekSchedule();
                everyRadioButton.setSelected(true);
                topMonthEditor.setSelectedItem(yearByDaySchedule.getMonthOfTheYear());
                topDateEditor.setInteger(yearByDaySchedule.getDayOfTheMonth());
            } else {
                yearByWeekSchedule = (YearlyByWeekSchedule) schedule;
                yearByDaySchedule = BOFactory.createYearlyByDaySchedule();
                theRadioButton.setSelected(true);
                dayCountEditor.setSelectedItem(yearByWeekSchedule.getWeekOfTheMonth());
                dayEditor.setSelectedItem(yearByWeekSchedule.getDayOfTheWeek());
                monthEditor.setSelectedItem(yearByWeekSchedule.getMonthOfTheYear());
            }
        }

        public Schedule getSchedule() throws Exception {
            if (everyRadioButton.isSelected()) {
                if (yearByDaySchedule == null) {
                    yearByDaySchedule = BOFactory.createYearlyByDaySchedule();
                }
                if (topDateEditor.getIntegerValue() < 1) {
                    throw new BusinessException(CommonMessageText.QUANTITY_NOT_POSITIVE);
                }
                yearByDaySchedule.setMonthOfTheYear((Integer) topMonthEditor.getSelectedItem());
                yearByDaySchedule.setDayOfTheMonth(topDateEditor.getInteger());
                return yearByDaySchedule;
            }
            if (theRadioButton.isSelected()) {
                if (yearByWeekSchedule == null) {
                    yearByWeekSchedule = BOFactory.createYearlyByWeekSchedule();
                }
                yearByWeekSchedule.setWeekOfTheMonth((Integer) dayCountEditor.getSelectedItem());
                yearByWeekSchedule.setDayOfTheWeek((Integer) dayEditor.getSelectedItem());
                yearByWeekSchedule.setMonthOfTheYear((Integer) monthEditor.getSelectedItem());
                return yearByWeekSchedule;
            }
            return null;
        }

        public void setScheduleEnabled(boolean enabled) {
            everyRadioButton.setEnabled(enabled);
            topDateEditor.setEnabled(enabled);
            topMonthEditor.setEnabled(enabled);
            theRadioButton.setEnabled(enabled);
            dayCountEditor.setEnabled(enabled);
            dayEditor.setEnabled(enabled);
            monthEditor.setEnabled(enabled);
        }

        public void clear() {
            everyRadioButton.setSelected(true);
            topDateEditor.setInteger(1L);
            topMonthEditor.setEmptySelection();
            theRadioButton.setSelected(false);
            dayCountEditor.setEmptySelection();
            dayEditor.setEmptySelection();
            monthEditor.setEmptySelection();
        }
    }
}
