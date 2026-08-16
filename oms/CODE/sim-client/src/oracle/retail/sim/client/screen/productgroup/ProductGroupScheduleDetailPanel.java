package oracle.retail.sim.client.screen.productgroup;

import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RListTransferPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupMessageText;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.schedule.ProductGroupSchedule;
import oracle.retail.sim.common.schedule.ProductGroupScheduleMessageText;
import oracle.retail.sim.common.schedule.Schedule;
import oracle.retail.sim.common.schedule.ScheduleType;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Product Group Schedule Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 2518623264851754795L;

    private ProductGroupScheduleDetailModel model = new ProductGroupScheduleDetailModel();

    private RDisplayLabelEditor scheduleIdEditor = new RDisplayLabelEditor("Schedule ID");
    private RComboBoxEditor productTypeEditor = new RComboBoxEditor("Product Type");
    private RComboBoxEditor productGroupEditor = new RComboBoxEditor("Product Group");
    private RLongTextFieldEditor descriptionEditor = new RLongTextFieldEditor("Description");
    private RDateFieldEditor startDateEditor = new RDateFieldEditor("Start Date");
    private RDateFieldEditor endDateEditor = new RDateFieldEditor("End Date");
    private RListTransferPanel storeTransferPanel = new RListTransferPanel("Available Locations", "Selected Locations");

    private static final String DAILY = "Daily";
    private static final String WEEKLY = "Weekly";
    private static final String MONTHLY = "Monthly";
    private static final String YEARLY = "Yearly";
    private static final String[] RADIO_HEADERS = { DAILY, WEEKLY, MONTHLY, YEARLY };
    private RRadioButtonEditor scheduleTypeEditor = new RRadioButtonEditor();

    private ProductGroupScheduleOptionPanel optionPanel = new ProductGroupScheduleOptionPanel();

    private static final String DATE_MODIFIED = "Date.modified";
    private static final String TYPE_MODIFIED = "Type.modified";
    private static final String GROUP_MODIFIED = "Group.modified";
    private static final String SCHEDULE_TYPE_MODIFIED = "ScheduleType.modified";

    private String requestConfirmTitle = "Item Request Update Confirmation";
    private String countConfirmTitle = "Stock Count Update Confirmation";

    /****************************************************************************************************
     * INITIALIZATION
     ***************************************************************************************************/
    public ProductGroupScheduleDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        productTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        productTypeEditor.registerAction(this, TYPE_MODIFIED);
        productGroupEditor.setDisplayer(new DualAttributeDisplayer("idAsString", "description"));
        productGroupEditor.registerAction(this, GROUP_MODIFIED);
        scheduleTypeEditor.setRadioButtons(RADIO_HEADERS, 4, 1);
        scheduleTypeEditor.setRadioTextPosition(EditorConstants.RIGHT);
        scheduleTypeEditor.registerAction(this, SCHEDULE_TYPE_MODIFIED);
        startDateEditor.setSizeType(EditorConstants.MEDIUM);
        endDateEditor.setSizeType(EditorConstants.MEDIUM);
        startDateEditor.registerAction(this, DATE_MODIFIED);
        endDateEditor.registerAction(this, DATE_MODIFIED);
        storeTransferPanel.setRowDisplayer(new StoreDisplayer());
        storeTransferPanel.setIncludeAllOptions(true);
        descriptionEditor.setIdentifier(SimName.PRODUCT_GROUP_SCHEDULE_DESCRIPTION);
    }

    private void layoutScreen() {
        REditorPanel headerPanel = new REditorPanel(3, 2);
        headerPanel.setTitleBorder("Product Group Schedule Detail");
        headerPanel.add(scheduleIdEditor);
        headerPanel.add(productTypeEditor);
        headerPanel.add(productGroupEditor);
        headerPanel.add(descriptionEditor);
        headerPanel.add(startDateEditor);
        headerPanel.add(endDateEditor);

        RPanel locationPanel = new RPanel(new GridBagLayout());
        locationPanel.setTitleBorder("Locations", 5);
        locationPanel.add(storeTransferPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 1, 3, 0, 0, 5, 0));
        locationPanel.add(new RLabel(), GridTool.constraints(1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 5, 0));

        RPanel schedulePanel = new RPanel(new GridBagLayout());
        schedulePanel.setTitleBorder("Schedule", 5);
        schedulePanel.add(scheduleTypeEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 5, 30));
        schedulePanel.add(optionPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(locationPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));
        mainPanel.add(schedulePanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadProductGroupSchedule();
        model.obtainProductGroupScheduleLock();

        ProductGroup productGroup = model.getProductGroupSchedule().getProductGroup();
        setActionsEnabled(false);
        if (isNewProductGroupSchedule()) {
            productTypeEditor.setItems(model.getFilteredProductGroupTypes());
        } else {
            productTypeEditor.setItems(model.getProductGroupTypes());
        }
        if (productGroup != null) {
            ProductGroupType groupType = productGroup.getType();
            if (groupType != null) {
                productTypeEditor.setSelectedItem(groupType);
                productGroupEditor.setItems(model.findProductGroups(groupType));
                doProductGroupModified();
            }
        } else {
            productTypeEditor.setEmptySelection();
        }
        productGroupEditor.setSelectedItem(model.convertToVO(productGroup));
        setActionsEnabled(true);
        refreshEditors();
    }

    public boolean isNewProductGroupSchedule() throws Exception {
        return model.isNewProductGroupSchedule();
    }

    public boolean isProductGroupScheduleEditable() {
        return model.isProductGroupScheduleEditable();
    }

    /****************************************************************************************************
     * Refresh Editors Helper Method
     ***************************************************************************************************/

    private void refreshEditors() throws Exception {
        boolean isGroupSelected = !productGroupEditor.isEmptySelection();
        boolean isPersisted = model.isProductGroupSchedulePersisted();
        boolean isEditable = model.isProductGroupScheduleEditable();
        boolean isUnitAndAmount = true;

        if (isGroupSelected) {
            ProductGroupVO group = (ProductGroupVO) productGroupEditor.getSelectedItem();
            isUnitAndAmount = group.getType() == ProductGroupType.STOCK_COUNT_UNIT_AMOUNT;

            storeTransferPanel.setSelectableItems(model.findAvailableStores(group));
            storeTransferPanel.setSelectedItems(Collections.emptyList());
        } else {
            storeTransferPanel.clearSelectableItems();
            storeTransferPanel.clearSelectedItems();
        }

        productGroupEditor.setEnabled(isEditable && !isPersisted);
        productTypeEditor.setEnabled(isEditable && !isPersisted);
        descriptionEditor.setEnabled(isEditable);
        startDateEditor.setEnabled(isGroupSelected && isEditable);
        endDateEditor.setEnabled(isGroupSelected && isEditable);

        storeTransferPanel.setEnabled(isGroupSelected && isEditable);
        scheduleTypeEditor.setEnabled(isGroupSelected && isEditable && !isUnitAndAmount);

        optionPanel.clearSchedule();

        if (isUnitAndAmount) {
            endDateEditor.setEnabled(false);
            optionPanel.setScheduleEnabled(false);
        }
        scheduleTypeEditor.setSelected(DAILY, true);

        ProductGroupSchedule productGroupSchedule = model.getProductGroupSchedule();

        scheduleIdEditor.setData(productGroupSchedule.getIdAsString());
        if (productGroupSchedule.getId() != null) {
            descriptionEditor.setText(productGroupSchedule.getDescription());
        }
        startDateEditor.clear();

        refreshSchedule(productGroupSchedule.getSchedule());

        List<Store> stores = model.convertToStores(productGroupSchedule.getStoreIds());
        storeTransferPanel.setSelectedItems(stores);
    }

    /****************************************************************************************************
     * Refresh Schedule Helper Method
     ***************************************************************************************************/

    private void refreshSchedule(Schedule schedule) {
        if (schedule != null) {
            if (schedule.getStartDate() != null) {
                Date startDate = SimDateUtil.convertDateFromUTC(model.getTimeZone(), schedule.getStartDate());
                startDateEditor.setDate(startDate);
            }
            if (schedule.getEndDate() != null) {
                Date endDate = SimDateUtil.convertDateFromUTC(model.getTimeZone(), schedule.getEndDate());
                endDateEditor.setDate(endDate);
            }
            ScheduleType type = schedule.getType();
            if (type == ScheduleType.DAILY) {
                scheduleTypeEditor.setSelected(DAILY, true);
            } else if (type == ScheduleType.DAILY_BY_WEEKDAY) {
                scheduleTypeEditor.setSelected(DAILY, true);
            } else if (type == ScheduleType.WEEKLY) {
                scheduleTypeEditor.setSelected(WEEKLY, true);
            } else if (type == ScheduleType.MONTHLY_BY_DAY) {
                scheduleTypeEditor.setSelected(MONTHLY, true);
            } else if (type == ScheduleType.MONTHLY_BY_WEEK) {
                scheduleTypeEditor.setSelected(MONTHLY, true);
            } else {
                scheduleTypeEditor.setSelected(YEARLY, true);
            }
            optionPanel.setSchedule(schedule);
        }
    }

    /****************************************************************************************************
     * Stop
     ***************************************************************************************************/

    public void stop() {
        try {
            model.releaseProductGroupScheduleLock();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(TYPE_MODIFIED)) {
                doProductGroupTypeModified();
            } else if (command.equals(GROUP_MODIFIED)) {
                doProductGroupModified();
            } else if (command.equals(SCHEDULE_TYPE_MODIFIED)) {
                doScheduleTypeModified();
            } else if (command.equals(DATE_MODIFIED)) {
                doDateModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Product Group Type Modified
     ***************************************************************************************************/

    private void doProductGroupTypeModified() throws Exception {
        ProductGroupType groupType = (ProductGroupType) productTypeEditor.getSelectedItem();
        productGroupEditor.setItems(model.findProductGroups(groupType));
    }

    /****************************************************************************************************
     * Product Group Modified
     ***************************************************************************************************/

    private void doProductGroupModified() throws Exception {
        Date tomorrow = SimDateUtil.getTomorrowAtStartOfDay(model.getTimeZone());
        Date today = SimDateUtil.getCurrentDateAtStartOfDay(model.getTimeZone());
        Integer lockoutDays = SimConfigManager.getInteger(SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS);
        if (lockoutDays == null) {
            lockoutDays = SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS_DEFAULT;
        }

        refreshEditors();

        ProductGroupType groupType = (ProductGroupType) productTypeEditor.getSelectedItem();
        if (groupType == ProductGroupType.STOCK_COUNT_WASTAGE) {
            startDateEditor.setValidStartDate(today);
            startDateEditor.setDate(today);
            endDateEditor.setValidStartDate(today);
            endDateEditor.clear();
        } else if (groupType == ProductGroupType.STOCK_COUNT_UNIT_AMOUNT) {
            tomorrow = SimDateUtil.addDays(model.getTimeZone(), today, lockoutDays);
            startDateEditor.setValidStartDate(tomorrow);
            startDateEditor.setDate(tomorrow);
            endDateEditor.setValidStartDate(tomorrow);
            endDateEditor.clear();
            scheduleTypeEditor.setEnabled(false);
            optionPanel.setScheduleEnabled(false);
        } else if (groupType == ProductGroupType.STOCK_COUNT_PROBLEM_LINE) {
            startDateEditor.setValidStartDate(today);
            startDateEditor.setDate(today);
            endDateEditor.setValidStartDate(today);
            endDateEditor.clear();
            scheduleTypeEditor.setEnabled(false);
            optionPanel.setScheduleEnabled(false);
        } else if (groupType == ProductGroupType.STOCK_COUNT_RMS_SYNC) {
            startDateEditor.setValidStartDate(today);
            startDateEditor.setDate(today);
            endDateEditor.setValidStartDate(today);
            endDateEditor.clear();
            endDateEditor.setEnabled(false);
            scheduleTypeEditor.setEnabled(false);
            optionPanel.setScheduleEnabled(false);
        } else {
            startDateEditor.setValidStartDate(today);
            startDateEditor.setDate(today);
            endDateEditor.setValidStartDate(today);
            endDateEditor.clear();
        }
    }

    /****************************************************************************************************
     * Schedule Type Modified
     ***************************************************************************************************/

    private void doScheduleTypeModified() {
        if (scheduleTypeEditor.isSelected(DAILY)) {
            optionPanel.showDayPanel();
        } else if (scheduleTypeEditor.isSelected(WEEKLY)) {
            optionPanel.showWeekPanel();
        } else if (scheduleTypeEditor.isSelected(MONTHLY)) {
            optionPanel.showMonthPanel();
        } else if (scheduleTypeEditor.isSelected(YEARLY)) {
            optionPanel.showYearPanel();
        }
    }

    /****************************************************************************************************
     * Date Modified
     ***************************************************************************************************/

    private void doDateModified() {
        try {
            Date startDate = startDateEditor.getDate();
            Date endDate = endDateEditor.getDate();
            ProductGroupType groupType = (ProductGroupType) productTypeEditor.getSelectedItem();
            boolean isModifiable = model.isScheduleDetailModifiable(groupType, startDate, endDate);
            scheduleTypeEditor.setEnabled(isModifiable);
            optionPanel.setScheduleEnabled(isModifiable);
        } catch (Throwable exception) {
            scheduleTypeEditor.setEnabled(true);
            optionPanel.setScheduleEnabled(true);
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() {
        showScreenBusy(true);
        try {
            if (productTypeEditor.getSelectedItem() == null) {
                displayError(ProductGroupMessageText.MISSING_TYPE);
                showScreenBusy(false);
                return false;
            }
            if (productGroupEditor.getSelectedItem() == null) {
                displayError(ProductGroupMessageText.GROUP_NOT_SELECTED);
                showScreenBusy(false);
                return false;
            }
            if (!model.checkProductGroupScheduleLock()) {
                displayError(CommonMessageText.LOCK_TAKEN_OVER);
                showScreenBusy(false);
                return false;
            }
            if (model.isProductGroupScheduleEditable()) {
                saveProductGroupSchedule();
                showScreenBusy(false);
                return true;
            }
        } catch (Throwable exception) {
            displayException(exception);
            showScreenBusy(false);
            return false;
        }
        return true;
    }

    /****************************************************************************************************
     * Save Schedule
     ***************************************************************************************************/

    private void saveProductGroupSchedule() throws Exception {
        ProductGroupSchedule productGroupSchedule = model.getProductGroupSchedule();
        productGroupSchedule.setDescription(descriptionEditor.getTextOrNull());

        ProductGroupVO productGroupVO = (ProductGroupVO) productGroupEditor.getSelectedItem();
        if (productGroupVO != null) {
            productGroupSchedule.setProductGroup(model.readProductGroup(productGroupVO));
        }

        Schedule schedule = optionPanel.getSchedule();
        Date startDate = startDateEditor.getDate();
        Date endDate = endDateEditor.getDate();

        if (model.useStartDateAsEndDate() && startDate != null) {
            schedule.setStartDate(model.getTimeZone(), startDate);
            schedule.setEndDate(model.getTimeZone(), startDate);
        } else {
            if (startDate != null) {
                schedule.setStartDate(model.getTimeZone(), startDate);
            }
            if (endDate != null) {
                schedule.setEndDate(model.getTimeZone(), endDate);
            }
        }
        model.validateSchedule(schedule);

        productGroupSchedule.setSchedule(schedule);

        List<Long> storeIds = model.convertToStoreIds(storeTransferPanel.getSelectedItems());
        productGroupSchedule.setStoreIds(storeIds);

        if (productGroupSchedule.isCoherent()) {
            if (isTransactionGenerationNeeded()) {
                showScreenBusy(true);
                try {
                    model.generateTransactionsFromSchedule();
                } finally {
                    showScreenBusy(false);
                }
            }
        }
    }

    /****************************************************************************************************
     * Is Transaction Generation Needed
     ***************************************************************************************************/

    private boolean isTransactionGenerationNeeded() throws Exception {
        boolean isPreviouslyPersisted = model.isProductGroupSchedulePersisted();

        model.persistProductGroupSchedule();

        if (model.isGenerateTransactionNeeded()) {
            if (model.getProductGroupSchedule().getProductGroup().getType().isItemRequest()) {
                return RConfirmUtility.confirm(requestConfirmTitle, ProductGroupScheduleMessageText.CONFIRM);
            }
            if (isPreviouslyPersisted) {
                return RConfirmUtility.confirm(countConfirmTitle, ProductGroupScheduleMessageText.CONFIRM_EDIT);
            }
            return RConfirmUtility.confirm(countConfirmTitle, ProductGroupScheduleMessageText.CONFIRM_NEW);
        }
        return false;
    }
}
