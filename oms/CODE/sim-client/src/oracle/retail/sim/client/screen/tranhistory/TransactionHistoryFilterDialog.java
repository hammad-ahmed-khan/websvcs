package oracle.retail.sim.client.screen.tranhistory;

import java.util.Date;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.tranhistory.TransactionHistoryMessageText;
import oracle.retail.sim.common.tranhistory.TransactionHistoryQueryFilter;
import oracle.retail.sim.common.tranhistory.TransactionType;

/********************************************************************************************************
 * This dialog handles entering the filter information for transaction history .
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransactionHistoryFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -9038501659961047620L;

    private TransactionHistoryFilterDialogModel model = new TransactionHistoryFilterDialogModel();

    private RDateFieldEditor startDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor finalDateEditor = new RDateFieldEditor("To Date");
    private RComboBoxEditor typeEditor = new RComboBoxEditor("Tran Type");
    private RComboBoxEditor reasonEditor = new RComboBoxEditor("Reason");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RTextFieldEditor userEditor = new RTextFieldEditor("User");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public TransactionHistoryFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Transaction History Filter");
        setSize(450, 325);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        startDateEditor.setSizeType(EditorConstants.MEDIUM);
        finalDateEditor.setSizeType(EditorConstants.MEDIUM);

        typeEditor.setDisplayer(new TranslatedObjectDisplayer());
        typeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        reasonEditor.setDisplayer(new TranslatedObjectDisplayer());
        reasonEditor.setEmptyType(RComboBoxEmptyType.ALL);
        itemEditor.setSearchListener(buildItemSearchListener());
        userEditor.setIdentifier(SimName.USER_USERNAME);

        searchLimitEditor.setIdentifier(SimName.CARTON_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setRequired(true);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);

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

        REditorPanel miscFilterPanel = new REditorPanel(5);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(typeEditor);
        miscFilterPanel.add(reasonEditor);
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(userEditor);
        miscFilterPanel.add(searchLimitEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(miscFilterPanel);

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(TransactionHistoryQueryFilter filter) throws Exception {
        model.setFilter(filter);

        typeEditor.setActionsEnabled(false);
        typeEditor.setItems(model.getTransactionTypes());
        typeEditor.setActionsEnabled(true);

        reasonEditor.setActionsEnabled(false);
        reasonEditor.setItems(model.findDescriptions());
        reasonEditor.setActionsEnabled(true);

        startDateEditor.setDate(filter.getFromDate());
        finalDateEditor.setDate(filter.getToDate());
        typeEditor.setSelectedItem(filter.getType());
        reasonEditor.setSelectedItem(filter.getReasonDescription());
        itemEditor.setText(filter.getItemId());
        userEditor.setText(filter.getUsername());
        searchLimitEditor.setInteger(filter.getSearchLimit());

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                }
            }
        };
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
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        TransactionHistoryQueryFilter filter = model.getFilter();

        Date startDate = startDateEditor.getDateAtStartOfDay();
        Date finalDate = finalDateEditor.getDateAtEndOfDay();

        if (startDate == null) {
            displayException(TransactionHistoryMessageText.MISSING_FILTER_DATES);
            startDateEditor.requestFocus();
            return;
        }
        if (finalDate == null) {
            displayException(TransactionHistoryMessageText.MISSING_FILTER_DATES);
            finalDateEditor.requestFocus();
            return;
        }

        String username = userEditor.getTextOrNull();
        if (username != null) {
            User user = model.readUser(username);
            if (user == null) {
                displayException(SecurityMessageText.ERROR_USER_INVALID);
                userEditor.requestFocus();
                return;
            }
            filter.setUsername(user.getUserName());
        }

        filter.setDateRange(startDate, finalDate);
        filter.setType((TransactionType) typeEditor.getSelectedItem());
        filter.setReasonDescription((String) reasonEditor.getSelectedItem());

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());

        RepositoryManager.addStateObject(SimClientStateKey.TRANSACTION_HISTORY_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.TRANSACTION_HISTORY_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
