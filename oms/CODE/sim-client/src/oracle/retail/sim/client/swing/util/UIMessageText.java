package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.common.business.MessageText;

/**
 * This enum contains the message text related to UI framework.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum UIMessageText implements MessageText {
    ADD_COMPONENT_METHOD_ERROR("Unable to add component using this method."),
    COMPARATOR_FAILURE("Attribute Comparator sort failure."),
    COMPARE_FAILURE("Error in comparing object values."),
    CONFIG_STARTING("Starting system configuration...."),
    CONFIG_ERROR("Exception during configuration."),
    CONFIG_FILE_LOADING("Loading configuration file: {0}"),
    CONFIG_FILE_LOADED("Configuration file loaded."),
    CONFIG_SHUTDOWN_LAUNCHING("CONFIG SHUTDOWN COMMAND LAUNCHING -->{0}"),
    COULD_NOT_CREATE_HELP_URL("Could not create URL for Help Documentation; url string: {0}"),
    DATA_TYPE_ATTRIBUTE_MISMATCH("A data type attribute mismatch occurred on data type [{0}], attribute [{1}], source exception [{2}]."),
    DATE_FORMAT_EXCEPTION("Date was not entered in the correct format [Example: {0}]."),
    DATE_TIME_FORMAT_EXCEPTION("Date and time was not entered in the correct format [Example: {0}]."),
    DEFAULT_EXCEPTION_MESSAGE("Please contact system administrator."),
    DEFAULT_FATAL_MESSAGE("A severe system error has occurred. Please contact system administrator."),
    DEPARTMENTS_NOT_FOUND("Departments were not found.  Editor will not be usable."),
    EDITOR_CONTENT_MISSING("{0} is a required field."),
    EDITOR_ERROR("Editor Error: {0}"),
    EMPTY_SEARCH_MESSAGE(""),
    EXECUTING_ACTION("Executing Action [{0}]"),
    FAILED_ATTRIBUTE_REFLECTION("Failed reflection on attribute."),
    FAILED_TO_START_APP("Failed to start application!"),
    FAILED_TO_LOAD_LANGUAGE("Failed to reload last language."),
    FAILED_TO_LOAD_THEME("Failed to reload last theme."),
    FOCUS_POLICY_ERROR("Failed to install focus policy. Using default policy instead."),
    GET_DATA_TYPE_ERROR("Problem in getDataType() on attribute: {0}"),
    GET_DATA_INTERNAL_ERROR("Exception occurred when trying to invoke: {0} in SimTableData.getDataInternal()"),
    INITIALIZING("Initializing... {0}"),
    INVALID_COLUMN_SORT("Invalid column sort attribute: {0}"),
    LAUNCHING_COMMAND("LAUNCHING -->{0}"),
    LOOK_AND_FEEL_ERROR("Unable to install Look & Feel [{0}]!"),
    LOV_INVALID_SELECTED_INPUT("Not all values were found for the selected input."),
    LOV_NO_SELECTION_MODEL("No selection model has been assigned to the List of Values editor."),
    MENU_ITEM_PRESSED("Menu Item Pressed [{0}]"),
    MESSAGE_ABOUT_COPYRIGHT("ABOUT_COPYRIGHT"),
    MESSAGE_HOURS_MINUTES("{0} Hours {1} minutes"),
    MESSAGE_LOADING_TASK("Attempting to start task..."),
    MESSAGE_LOOK_AND_FEEL_COMPLETE("Look and feel initialization complete!"),
    MESSAGE_LOOK_AND_FEEL_INIT("Initializing Look & Feel [{0}]..."),
    MESSAGE_LOGIN("Enter a valid Username and Password to login."),
    MESSAGE_WELCOME("Welcome To Oracle!"),
    MISSING_REQUIRED_FIELDS("The following column(s) are required: {0}. Please make a selection."),
    MONEY_FORMAT_ERROR("Unable to retrieve money value from editor. Not in proper format."),
    NATIVE_COMMANDS_ERROR("Unable to execute native commands."),
    NAVIGATION_DATA_WRITER_ERROR("Unable to write navigation workflow data."),
    NAVIGATION_INPUT_EMPTY("Unable to create navigation data. Navigation input was empty."),
    NAVIGATION_PARSE_MISMATCH_TAG("Open/Close tag pair [{0}] do not match."),
    NAVIGATION_PARSE_NO_TAG("Unable to parse navigation data. Required tag [{0}] was not found."),
    NAVIGATION_RESOURCE_NOT_FOUND("Navigation resource [{0}] was not found."),
    NAVIGATION_RESOURCE_EMPTY("Navigation resource [{0}] was empty."),
    NAVIGATION_RESOURCE_READ_ERROR("Error occurred during IO read of navigation resource [{0}]."),
    REFLECTION_EXCEPTION("Exception occurred when trying to invoke: {0}"),
    REPORT_FORMAT_MUST_BE_SELECTED("Ticket Type is required. Please make a selection."),
    REQUIRED_CONTENT_MISSING("Please enter all required data before proceeding."),
    RESUMING_SCREEN("Resuming Screen [{0}]"),
    RESUME_SCREEN_FAILURE("Failed to resume screen."),
    SAVE_MODIFIED_DATA("Data has been modified and not saved. Do you want to continue?"),
    START_TASK_FAILURE("Unable to start task [{0}]"),
    STARTING_SCREEN("Starting Screen [{0}]"),
    SYSTEM_VERSION("System: {0}"),
    TABLE_LIMIT_ERROR("Table sort limit has been exceeded."),
    TABLE_RESET_FOCUS(""),
    TASK_NOT_CREATED("Unable to create task: {0}"),
    TASK_NOT_CREATED_SEVERE("Unable to create task due to severe system error!"),
    TIME_FORMAT_INVALID("Time entered is an invalid format."),
    LANGUAGE_NOT_LOADED("Country language bundle NOT loaded for {0}"),
    UNABLE_TO_FIND_COLUMN("Unable to find column for attribute name: {0}"),
    UNABLE_TO_FIND_SCREEN("Unable to find screen: {0}"),
    UNABLE_TO_LOAD_ICON("Unable to load icon: {0}"),
    UNABLE_TO_LOAD_LABELS("Unable to load L&F labels and messages."),
    UNABLE_TO_LOAD_INSET("Unable to load inset resource: {0}"),
    UNABLE_TO_LOAD_DIMENSION("Unable to load dimension resource: {0}"),
    UNABLE_TO_LOAD_HIERARCHY("Unable to load class or subclass hierarchy."),
    UNABLE_TO_FIND_METHOD("Unable to find set method for {0}"),
    UNABLE_TO_START_TASK("Unable to start task {0}"),
    VARIANT_NOT_LOADED("Country variant bundle NOT loaded for {0}"),
    WINDOW_LOCATION_ERROR("Unable to retrieve window location."),
    WINDOW_PROPERTIES_ERROR("Unable to retrieve window properties.");

    private final String message;

    UIMessageText(String message) {
        this.message = message;
    }

    public String getCode() {
        return name();
    }

    public String getText() {
        return message;
    }
}
