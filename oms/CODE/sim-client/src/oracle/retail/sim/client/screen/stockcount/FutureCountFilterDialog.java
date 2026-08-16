package oracle.retail.sim.client.screen.stockcount;

import java.util.Date;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.stockcount.FutureCountQueryFilter;
import oracle.retail.sim.common.stockcount.StockCountMessageText;

/********************************************************************************************************
 * This dialog handles entering the filter information for stock counts..
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FutureCountFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 2620595763393588714L;

    private RDateFieldEditor startDateEditor = SimEditorFactory.createDateEditor("From Date");
    private RDateFieldEditor finalDateEditor = SimEditorFactory.createDateEditor("To Date");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public FutureCountFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Future Stock Count Filter");
        setSize(325, 165);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        startDateEditor.setSizeType(EditorConstants.LARGE);
        finalDateEditor.setSizeType(EditorConstants.LARGE);

        RDateFieldRangeUtility.setDateRangeEditors(startDateEditor, finalDateEditor);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel dateFilterPanel = new REditorPanel(2);
        dateFilterPanel.setTitleBorder("Date Filters");
        dateFilterPanel.add(startDateEditor);
        dateFilterPanel.add(finalDateEditor);

        setContentPane(dateFilterPanel);
    }

    public void setFilter(FutureCountQueryFilter filter) {
        Date tomorrow = SimDateUtil.addDays(LocaleManager.getTimeZone(), SimDateUtil.getCurrentDate(), 1);
        Date validEndDate = SimDateUtil.addMonths(LocaleManager.getTimeZone(), SimDateUtil.getCurrentDate(), 2);

        startDateEditor.setValidStartDate(tomorrow);
        finalDateEditor.setValidStartDate(tomorrow);

        startDateEditor.setValidEndDate(validEndDate);
        finalDateEditor.setValidEndDate(validEndDate);

        startDateEditor.setDate(filter.getFromDate());
        finalDateEditor.setDate(filter.getToDate());

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Reset Action
     ***************************************************************************************************/
    private void doReset() throws Exception {
        Date tomorrow = SimDateUtil.addDays(LocaleManager.getTimeZone(), SimDateUtil.getCurrentDateAtNoonUTC(), 1);
        startDateEditor.setDate(tomorrow);
        finalDateEditor.setDate(tomorrow);
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        Date tomorrow = SimDateUtil.addDays(LocaleManager.getTimeZone(), SimDateUtil.getCurrentDate(), 1);
        Date checkDate = startDateEditor.getDateAtEndOfDay();
        if (checkDate.compareTo(tomorrow) < 0) {
            throw new BusinessException(StockCountMessageText.FUTURE_DATE_ERROR);
        }
        FutureCountQueryFilter filter = BOFactory.createFutureCountQueryFilter();
        filter.setStoreId(SimRepository.getStoreId());
        filter.setFromDate(startDateEditor.getGMTDateAtNoon());
        filter.setToDate(finalDateEditor.getGMTDateAtNoon());

        RepositoryManager.addStateObject(SimClientStateKey.FUTURE_COUNT_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.FUTURE_COUNT_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        RepositoryManager.removeStateObject(SimClientStateKey.FUTURE_COUNT_FILTER);
        closeWindow();
    }
}
