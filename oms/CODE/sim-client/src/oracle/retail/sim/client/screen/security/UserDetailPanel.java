package oracle.retail.sim.client.screen.security;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.type.LocaleDisplayer;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.format.PhoneMaskFactory;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.util.ArrayUtility;

/********************************************************************************************************
 * User Detail (Create/Edit) Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -1759522492188899434L;

    private static final String ASSIGN_USERNAME = "Assign.username";
    private static final String STATUS_SELECTED = "Status.selected";

    private UserDetailModel model = new UserDetailModel();

    private RTextFieldEditor firstNameEditor = new RTextFieldEditor("First Name");
    private RTextFieldEditor middleInitEditor = new RTextFieldEditor("Middle Initial");
    private RTextFieldEditor lastNameEditor = new RTextFieldEditor("Last Name");
    private RTextFieldEditor employeeIdEditor = new RTextFieldEditor("Employee Number");
    private RTextFieldEditor userNameEditor = new RTextFieldEditor("Username");
    private RCheckBoxEditor passwordEditor = new RCheckBoxEditor("Password Assigned");
    private RTextFieldEditor emailEditor = new RTextFieldEditor("Email");
    private RTextFieldEditor phoneNumberEditor = new RTextFieldEditor("Phone Number");
    private RTextFieldEditor supervisorEditor = new RTextFieldEditor("Supervisor");
    private RComboBoxEditor localeEditor = new RComboBoxEditor("Locale");
    private RDateFieldEditor createDateEditor = new RDateFieldEditor("Create Date");
    private RDateFieldEditor startDateEditor = new RDateFieldEditor("Start Date");
    private RDateFieldEditor endDateEditor = new RDateFieldEditor("End Date");
    private RComboBoxEditor userTypeEditor = new RComboBoxEditor("User Type");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public UserDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        firstNameEditor.setIdentifier(SimName.USER_FIRST_NAME);
        middleInitEditor.setIdentifier(SimName.USER_MIDDLE_INIT);
        lastNameEditor.setIdentifier(SimName.USER_LAST_NAME);
        employeeIdEditor.setIdentifier(SimName.USER_EXTERNAL_ID);
        userNameEditor.setIdentifier(SimName.USER_USERNAME);
        passwordEditor.setIdentifier(SimName.USER_PASSWORD);
        emailEditor.setIdentifier(SimName.USER_EMAIL);
        phoneNumberEditor.setIdentifier(SimName.USER_PHONE_NUMBER);
        supervisorEditor.setIdentifier(SimName.USER_SUPERVISOR);
        commentsEditor.setIdentifier(SimName.USER_COMMENT);

        localeEditor.setDisplayer(new LocaleDisplayer(LocaleManager.getLanguageLocale()));
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        userTypeEditor.setDisplayer(new TranslatedObjectDisplayer());

        firstNameEditor.setRequired(true);
        lastNameEditor.setRequired(true);
        userNameEditor.setRequired(true);
        localeEditor.setRequired(true);
        statusEditor.setRequired(true);
        userTypeEditor.setRequired(true);
        localeEditor.setSelectionRequired(true);
        statusEditor.setSelectionRequired(true);
        userTypeEditor.setSelectionRequired(true);

        phoneNumberEditor.setMask(PhoneMaskFactory.createPhoneMask(LocaleManager.getLanguageLocale()));

        middleInitEditor.setSizeType(EditorConstants.TINY);
        createDateEditor.setSizeType(EditorConstants.MEDIUM);
        startDateEditor.setSizeType(EditorConstants.MEDIUM);
        endDateEditor.setSizeType(EditorConstants.MEDIUM);
        localeEditor.setSizeType(EditorConstants.MEDIUM);
        userTypeEditor.setSizeType(EditorConstants.MEDIUM);
        statusEditor.setSizeType(EditorConstants.MEDIUM);

        passwordEditor.setEnabled(true, false);
        createDateEditor.setEnabled(true, false);

        firstNameEditor.registerAction(this, ASSIGN_USERNAME);
        lastNameEditor.registerAction(this, ASSIGN_USERNAME);
        statusEditor.registerAction(this, STATUS_SELECTED);
    }

    private void layoutPanel() {
        REditorPanel upperPanel = new REditorPanel(8, 2);
        upperPanel.add(firstNameEditor);
        upperPanel.add(middleInitEditor);
        upperPanel.add(lastNameEditor);
        upperPanel.add(employeeIdEditor);
        upperPanel.add(userNameEditor);
        upperPanel.add(passwordEditor);
        upperPanel.add(emailEditor);
        upperPanel.add(phoneNumberEditor);
        upperPanel.add(supervisorEditor);
        upperPanel.add(localeEditor);
        upperPanel.add(createDateEditor);
        upperPanel.add(startDateEditor);
        upperPanel.add(endDateEditor);
        upperPanel.add(userTypeEditor);
        upperPanel.add(statusEditor);

        REditorPanel lowerPanel = new REditorPanel(1);
        lowerPanel.add(commentsEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(upperPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 3, 1, 0, 0, 0, 0));
        mainPanel.add(lowerPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0));

        LayoutUtility.alignPanels(upperPanel, lowerPanel);

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
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        User user = wrapper.getUser();

        setActionsEnabled(false);

        localeEditor.setItems(model.findAvailableLocales());
        statusEditor.setItems(model.findAvailableUserStatuses(user.getStatus()));
        userTypeEditor.setItems(model.findAvailableUserTypes());

        firstNameEditor.setText(user.getFirstName());
        middleInitEditor.setText(user.getMiddleName());
        lastNameEditor.setText(user.getLastName());
        employeeIdEditor.setText(user.getExternalId());
        userNameEditor.setText(user.getUserName());
        emailEditor.setText(user.getEmail());
        phoneNumberEditor.setText(user.getPhoneNumber());
        supervisorEditor.setText(user.getSupervisor());
        commentsEditor.setText(user.getComments());
        if (user.getLocale() != null) {
            localeEditor.setSelectedItem(user.getLocale());
        }
        if (user.getStatus() != null) {
            statusEditor.setSelectedItem(user.getStatus());
        }
        createDateEditor.setDate(user.getCreateDate() != null ? user.getCreateDate() : SimDateUtil.getCurrentDate());
        startDateEditor.setDate(user.getStartDate() != null ? user.getStartDate() : SimDateUtil.getCurrentDate());
        if (user.getEndDate() != null) {
            endDateEditor.setDate(user.getEndDate());
        }
        if (user.getType() != null) {
            userTypeEditor.setSelectedItem(user.getType());
        }

        if (!wrapper.isNew()) {
            passwordEditor.setSelected(true);
            userNameEditor.setRequired(false);
            userNameEditor.setEnabled(true, false);
        }
        if (wrapper.isUserReadOnly() || wrapper.isDeleted()) {
            firstNameEditor.setRequired(false);
            lastNameEditor.setRequired(false);
            firstNameEditor.setEnabled(true, false);
            middleInitEditor.setEnabled(true, false);
            lastNameEditor.setEnabled(true, false);
            employeeIdEditor.setEnabled(true, false);
            emailEditor.setEnabled(true, false);
            phoneNumberEditor.setEnabled(true, false);
            supervisorEditor.setEnabled(true, false);
            localeEditor.setEnabled(true, false);
            startDateEditor.setEnabled(true, false);
            endDateEditor.setEnabled(true, false);
            userTypeEditor.setEnabled(true, false);
            commentsEditor.setEnabled(true, false);
            if (wrapper.isUserReadOnly()) {
                statusEditor.setEnabled(true, false);
            }
        }

        setActionsEnabled(true);
    }

    public void stop() {
        model.clearState();
    }

    public boolean isUserNew() throws Exception {
        return model.getUserDetailWrapper().isNew();
    }

    public boolean isUserReadOnly() throws Exception {
        return model.getUserDetailWrapper().isUserReadOnly();
    }

    /****************************************************************************************************
     * Handle Assign Password
     ***************************************************************************************************/

    public void handleAssignPassword() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isDeleted()) {
            throw new BusinessException(SecurityMessageText.DELETED_USER_ACTION_DENIED);
        }

        AssignPasswordDialog dialog = new AssignPasswordDialog();
        dialog.setUserDetailWrapper(wrapper);
        dialog.setVisible(true);

        passwordEditor.setSelected(!ArrayUtility.isNullOrEmpty(wrapper.getPassword()));
    }

    /****************************************************************************************************
     * Handle Assign Stores
     ***************************************************************************************************/

    public void handleAssignStores() throws Exception {
        if (model.getUserDetailWrapper().isDeleted()) {
            return;
        }
        validateRequiredContent();
        populateUserObject();
    }

    /****************************************************************************************************
     * Handle Assign Roles
     ***************************************************************************************************/

    public void handleAssignRoles() throws Exception {
        if (model.getUserDetailWrapper().isDeleted()) {
            return;
        }
        validateRequiredContent();
        populateUserObject();
    }

    /****************************************************************************************************
     * Handle Copy Assignments
     ***************************************************************************************************/

    public void handleCopyAssignments() throws Exception {
        validateRequiredContent();
        populateUserObject();

        UserLookupDialog dialog = new UserLookupDialog();
        dialog.setSearchListener(buildUserSearchListener());
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        dialog.setAvailableRoles(new ArrayList<Role>(wrapper.getAvailableRoles().values()));
        List<UserType> availableUserTypes = model.findAvailableUserTypes();
        if (!wrapper.isSuperUser() && availableUserTypes.contains(UserType.SUPER_USER)) {
            availableUserTypes = new ArrayList<UserType>(availableUserTypes);
            availableUserTypes.remove(UserType.SUPER_USER);
        }
        dialog.setAvailableUserTypes(availableUserTypes, availableUserTypes.size() != SimEnumUtility.findUserTypes().size());
        dialog.loadDialog();
        dialog.setVisible(true);
    }

    private SearchListener buildUserSearchListener() {
        return new SearchListener() {
            public void search() {
            }

            public void assign(Object value) {
                User user = (User) value;
                if (user == null) {
                    return;
                }
                doCopyAssignments(user);
            }
        };
    }

    private void doCopyAssignments(User user) {
        try {
            if (!model.copyAssignments(user)) {
                displayMessage(SecurityMessageText.COPY_ASSIGNMENTS_LIMITED);
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isDeleted()) {
            return;
        }
        validateRequiredContent();
        populateUserObject();
        validateUserDates();
        if (wrapper.isNew()) {
            if (!passwordEditor.isSelected()) {
                throw new BusinessException(SecurityMessageText.ASSIGN_PASSWORD);
            }
            if (!model.validateUserName()) {
                throw new BusinessException(SecurityMessageText.ERROR_USERNAME_INVALID);
            }
            if (model.userNameExists()) {
                throw new BusinessException(SecurityMessageText.USERNAME_EXISTS);
            }
        }
        if (wrapper.isDefaultStoreRequired()) {
            throw new BusinessException(SecurityMessageText.DEFAULT_STORE_NEEDED);
        }
        model.saveUser();
    }

    private void validateUserDates() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isUserReadOnly()) {
            return;
        }
        if (wrapper.getStatus() != UserStatus.ACTIVE && !wrapper.isTemporaryUser()) {
            return;
        }
        Date startDate = wrapper.getStartDate();
        Date endDate = wrapper.getEndDate();
        if (!SimDateUtil.isValidDateRange(startDate, endDate)) {
            throw new BusinessException(SecurityMessageText.USER_DATE_RANGE_INVALID);
        }
        Date currentDate = SimDateUtil.getCurrentDateAtStartOfDay(model.getTimeZone());
        if (wrapper.isNew() && !SimDateUtil.isValidDateRange(currentDate, startDate)) {
            throw new BusinessException(SecurityMessageText.USER_START_DATE_INVALID);
        }
        if (!SimDateUtil.isValidDateRange(currentDate, endDate)) {
            throw new BusinessException(SecurityMessageText.USER_END_DATE_INVALID);
        }
        if (wrapper.isTemporaryUser()) {
            if (endDate == null) {
                throw new BusinessException(SecurityMessageText.TEMP_USER_END_DATE_REQUIRED);
            }
            Integer tempUserEndDateMaxDays = SimConfigManager.getInteger(SimConfigManager.SECURITY_MAX_DAYS_FOR_TEMP_USER_END_DATE);
            if (tempUserEndDateMaxDays != null && tempUserEndDateMaxDays > 0) {
                Date finalStartDate = SimDateUtil.addDays(model.getTimeZone(), startDate != null ? startDate : currentDate, tempUserEndDateMaxDays);
                if (endDate.compareTo(finalStartDate) > 0) {
                    throw new BusinessException(SecurityMessageText.TEMP_USER_END_DATE_INVALID, tempUserEndDateMaxDays);
                }
            }
        }
    }

    protected void populateUserObject() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isUserReadOnly()) {
            return;
        }
        if (wrapper.isDeleted()) {
            return;
        }
        User user = wrapper.getUser();
        if (wrapper.isNew()) {
            wrapper.setUserName(userNameEditor.getTextOrNull());
        }
        user.setFirstName(firstNameEditor.getTextOrNull());
        user.setMiddleName(middleInitEditor.getTextOrNull());
        user.setLastName(lastNameEditor.getTextOrNull());
        user.setExternalId(employeeIdEditor.getTextOrNull());
        user.setEmail(emailEditor.getTextOrNull());
        user.setPhoneNumber(phoneNumberEditor.getTextOrNull());
        user.setSupervisor(supervisorEditor.getTextOrNull());
        user.setLocale((Locale) localeEditor.getSelectedItem());
        user.setStatus((UserStatus) statusEditor.getSelectedItem());
        user.setComments(commentsEditor.getTextOrNull());
        user.setCreateDate(createDateEditor.getDate());
        user.setStartDate(startDateEditor.getDateAtStartOfDay());
        user.setEndDate(endDateEditor.getDateAtEndOfDay());
        wrapper.setType((UserType) userTypeEditor.getSelectedItem());
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ASSIGN_USERNAME)) {
                doAssignDefaultUsername();
            } else if (command.equals(STATUS_SELECTED)) {
                doStatusSelected();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doAssignDefaultUsername() {
        if (userNameEditor.isEmpty() && !firstNameEditor.isEmpty() && !lastNameEditor.isEmpty()) {
            userNameEditor.setText(model.buildDefaultUsername(firstNameEditor.getText(), lastNameEditor.getText()));
        }
    }

    private void doStatusSelected() throws Exception {
        UserDetailWrapper wrapper = model.getUserDetailWrapper();
        if (wrapper.isUserReadOnly()) {
            return;
        }
        UserStatus userStatus = wrapper.getStatus();
        if (userStatus != UserStatus.DELETE && userStatus != UserStatus.LOCKED) {
            return;
        }
        UserStatus selectedUserStatus = (UserStatus) statusEditor.getSelectedItem();
        if (userStatus == selectedUserStatus) {
            return;
        }
        wrapper.getUser().setStatus(selectedUserStatus);
        RepositoryManager.addStateObject(SimClientStateKey.USER_DETAIL_MODIFIED, Boolean.TRUE);
        setActionsEnabled(false);
        if (userStatus == UserStatus.DELETE) {
            firstNameEditor.setRequired(true);
            lastNameEditor.setRequired(true);
            firstNameEditor.setEnabled(true, true);
            middleInitEditor.setEnabled(true, true);
            lastNameEditor.setEnabled(true, true);
            employeeIdEditor.setEnabled(true, true);
            emailEditor.setEnabled(true, true);
            phoneNumberEditor.setEnabled(true, true);
            supervisorEditor.setEnabled(true, true);
            localeEditor.setEnabled(true, true);
            startDateEditor.setEnabled(true, true);
            endDateEditor.setEnabled(true, true);
            userTypeEditor.setEnabled(true, true);
            commentsEditor.setEnabled(true, true);
        }
        statusEditor.setItems(model.findAvailableUserStatuses(selectedUserStatus));
        statusEditor.setSelectedItem(selectedUserStatus);
        setActionsEnabled(true);
    }
}
