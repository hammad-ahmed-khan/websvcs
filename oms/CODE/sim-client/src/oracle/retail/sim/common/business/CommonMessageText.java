package oracle.retail.sim.common.business;

public enum CommonMessageText implements MessageText {
	ACTION_INVALID("Invalid action."), ADMIN_CONFIRM("The changes you have made will affect all stores, are you sure you want to continue?"),
	ADMIN_EOD_MAX_FILL_ERROR("The end of day max fill % must be greater than or equal to {0}"), ADMIN_LOCKOUT_DAYS_ERROR("The Stock Count Lockout Days must be greater than or equal to {0}"),
	ADMIN_OUT_OF_STOCK_PERCENT_ERROR("The item out of stock % must be greater than or equal to {0}"),
	ADMIN_OUT_OF_STOCK_UOM_ERROR("The item out of stock (Standard UOM) must be greater than or equal to {0}"),
	ADMIN_WITHIN_DAY_MAX_FILL_ERROR("The within day max fill % must be greater than or equal to {0}"), AMOUNT_NOT_POSITIVE("The amount must be positive."),
	BLANK_VALUE_INVALID("{0} is not allowed to be blank."), CASE_SIZE_NOT_POSITIVE("The pack size must be a positive number."), CASE_SIZE_NOT_WHOLE("The pack size must be a whole number."),
	CARRIER_TYPE_REQUIRED("Type is required for Third Party."), CLIENT_ALREADY_RUNNING("A client application is already running."),
	COMMENT_TOO_LONG("The text entered is too long for the database. Please shorten the comment."), CONCESSION_ITEM_ERROR("This is a concession item and cannot be used. Please select another item."),
	CONFIG_VALUE_INVALID("Invalid value."), CONSIGNMENT_ITEM_ERROR("This is a consignment item and cannot be used. Please select another item."),
	CURRENCY_INVALID_MIN_SIZE("Currency entered is below the minimum allowed currency amount."), CURRENCY_INVALID_MAX_SIZE("Currency entered exceeded the maximum allowed currency amount."),
	CURRENCY_INVALID_FORMAT("Currency was not entered in a valid format."), CURRENCY_INVALID_SIGN("Failed to handle currency with an invalid currency symbol."),
	DATABASE_PRECISION_ERROR("Value larger than specified precision allowed for this column."), DATE_RANGE_ERROR("From Date may not be later than To Date."),
	DEFAULT_TO_ZERO_CONFIRM("Items without quantities will be set to zero. Would you like to continue?"), EMAIL_DAYS_INVALID("The days must be less than or equal to 999."),
	ERROR_FATAL_CONTACT_MESSAGE("Please contact system administrator."), ERROR_FATAL_DEFAULT_MESSAGE("A severe system error has occurred."),
	ERROR_FATAL_DETAIL_MESSAGE("Severe system error [{0}] occurred at {1}."), FINISHER_OVER_SEARCH_LIMIT("The search limit must be less than 31."),
	FINISHER_UNDER_SEARCH_LIMIT("The search limit must be greater than 0."), FINISHER_NOT_FOUND("{0} is not a valid finisher ID."),
	FUNCTION_NOT_AVAILABLE("The function you have selected is not available."), HELP_URL_ERROR("Help files could not be located."), INVALID_CASE_SIZE("Pack size not valid for current quantity."),
	INVALID_REASON_CODE("The return reason code is invalid."), ITEM_ALREADY_EXISTS("This item already exists as a line item."),
	ITEM_PRICE_CHANGE_NOT_ALLOWED("The item is not ranged at this store and cannot have its price changed."),
	ITEM_RECEIVE_CONFIRM("One or more items are inactive, discontinued or deleted, are you sure you want to receive all?"), ITEM_OVER_SEARCH_LIMIT("The search limit must be less than {0}."),
	ITEM_UNDER_SEARCH_LIMIT("Search limit is a required field."), LINE_ITEM_NOT_FOUND("No matching line item found for input."),
	LINE_ITEM_NO_ITEM("One or more stock items must exist for each line item."), LINE_ITEM_DELETE_CONFIRM("The selected line item(s) will be deleted. Do you want to continue?"),
	LOCK_HELD_ERROR("Lock is held by {0}."), LOCK_NOT_GRANTED("Lock not granted."), LOCK_TAKEN_OVER("Lock has been taken over by another user."),
	LOCK_HELD_CONFIRM("Lock is held by {0}. Would you like to break the lock?"), LOGIN_NOT_ALLOWED("You are not allowed to log in to any stores."),
	LOGIN_USER_LOCALE_FAILED("User {0} does not have a valid locale assigned. Please contact the System Administrator to set the value."),
	LOGIN_STORE_TIMEZONE_FAILED("Store {0} does not have a time zone specified. Please contact the System Administrator to set the value."), LOGIN_PASSWORD_ERROR("Password is required."),
	LOGIN_USERNAME_REQUIRED("Username is required."), LOGIN_FAILED("Login Failed.  Username and/or password is incorrect, or user is unauthorized."), LOGIN_FAILED_WITH_MESSAGE("Login Failed.  {0}"),
	MAX_FIELD_LENGTH_ERROR_12("This field cannot have more than 12 characters."), MAX_FIELD_LENGTH_ERROR_25("This field cannot have more than 25 characters."),
	MAX_FIELD_LENGTH_ERROR_30("This field cannot have more than 30 characters."), MAX_FIELD_LENGTH_ERROR_255("This field cannot have more than 255 characters."),
	MISSING_REASON_CODE("Reason Code is required."), MISSING_DESCRIPTION("Description is required."), MISSING_VARIANCE_VALUE("Must enter either the Variance Units, Variance %, or both."),
	NO_ACCESS_PERMISSION("User does not have permission to access this transaction."), NO_CRITERIA_ENTERED("At least one criterion must be entered."), NO_EMPLOYEE_ASSIGNED("A username must be set."),
	NO_QUANTITY_APPLIED("No quantity was applied."), NO_RECORDS_FOUND("Sorry, no records found. Please try again."), NO_ROWS_SELECTED("Please select one or more rows."),
	NO_ROWS_SELECTED_DELETE("You must have one or more rows selected to delete."), NO_ROWS_REMAINING("There are no items remaining in this transaction. Would you like the transaction canceled?"),
	NO_SINGLE_ROW_SELECTED("Please select a single row."), NO_UIN_CAPTURED("{0} not captured."), NON_ACTIVE_ITEM_CONFIRM("The item is {0}, are you sure you want to use this item?"),
	NON_ORDERABLE_ITEM_ERROR("The item is non-orderable and cannot be added."), NON_INVENTORY_ITEM_ERROR("This is a non-inventory item and cannot be used. Please select another item."),
	NUMBER_NOT_1_TO_99("The number must be between 1 and 99."), NUMBER_NOT_1_TO_999("The number must be between 1 and 999."), NUMBER_NOT_BELOW_101("The quantity must be less than 101."),
	NUMBER_NOT_BELOW_100000("The amount must be less than 100000."), NUMBER_NOT_BELOW_1000000000("The amount must be less than 1000000000."),
	NUMBER_NOT_BELOW_99999("The amount must be less than or equal to 99999."), NUMBER_NOT_BELOW_999999("The amount must be less than or equal to 999999."),
	ONE_ROW_MUST_BE_SELECTED("Please select only one row."), QUANTITY_INVALID_NEGATIVE("The quantity must be a positive number or zero."),
	QUANTITY_NOT_POSITIVE("The quantity must be a positive number."), QUANTITY_NOT_WHOLE("The quantity must be a whole number."),
	REASON_PERMISSION_ERROR("You do not have the necessary privileges to delete one or more of the selected line items."),
	REQUESTED_PICKUP_DATE_IN_PAST_ERROR("Requested pick up date must be greater than or equal to create date"),
	SECURITY_PASSWORD_MISMATCH("The password values do not match, please re-enter the passwords."),
	SECURITY_PASSWORD_EXPIRATION_NOTIFICATION("Password will expire in {0} day(s). Please create a new password."),
	STAGED_MESSAGE_DELETE_CONFIRM("Are you sure you want to delete the selected staged messages now?"), STALE_DATA("STALE DATA - Transaction aborted."),
	START_DATE_IN_PAST_ERROR("Start date must be greater than or equal to today."), SUPPLIER_BLANK("Supplier cannot be blank."), SUPPLIER_NOT_FOUND("No supplier found for {0}."),
	SUPPLIER_OVER_SEARCH_LIMIT("The search limit must be less than 31."), SUPPLIER_UNDER_SEARCH_LIMIT("Search limit is a required field."), SUPPLIER_SITE_INACTIVE("Supplier site is inactive."),
	SUPPLIER_INACTIVE_CONFIRM("The supplier is inactive. Do you want to continue?"), TEMPLATE_MISSING("The template for the selected format does not exist. Please check with system administrator."),
	THEME_COLOR_RESET_CONFIRM("All selected widgets will be set back to the default color settings.  Do you want to continue?"),
	THEME_FONT_RESET_CONFIRM("All selected widgets will be set back to the default font settings.  Do you want to continue?"),
	THEME_FONT_APPLY_CONFIRM("Are you sure you want to apply the font settings to all widgets?"), THEME_FONT_PREVIEW_TEXT("This is a preview of the selected font settings."),
	THEME_ICON_RESET_CONFIRM("All selected widgets will be set back to the default icon settings.  Do you want to continue?"), THEME_NONE_SELECTED("Please select a theme."),
	THEME_NO_NAME("Please select a name and description for the theme."), THEME_NAME_EXISTS("The theme name already exists."), THEME_DESC_EXISTS("The theme description already exists."),
	THEME_NAME_DESC_EXISTS("The theme name and description already exist."), TICKET_NO_TYPE_FORMATS("There are no ticket type formats for this ticket."), TRANSLATION_NO_KEY("Please enter a key."),
	TRANSLATION_NO_LANGUAGE("Please select a language."), TRANSLATION_NOT_SELECTED("Please select a record to edit."),
	UDA_VALUE_ASSOCIATED_TO_ITEM("UDA Value cannot be deleted. It is associated with an Item."), UDA_ASSOCIATED_TO_ITEM("UDA cannot be deleted. It is associated with an Item."),
	UI_EXIT_CONFIRM("Are you sure you want to exit the application?"),
	UIN_RECEIVE_QUANTITY_MISMATCH("The number of {0} captured for the Item {1} does not match the received quantity. Please remove the appropriate UIN."),
	UIN_RECEIVE_ZERO_QTY_CONFIRM("Some items require UINs and do not have UINs entered. The quantity will be set to zero for these items. Do you want to continue?"),
	UIN_REQUIRED("UINs are required for Item {0}."), UIN_REQUIRED_MULTI("UINs are required for the following items:"),
	UIN_REQUIRED_TO_RECEIVE_ALL("Receive All is not available as {0} contains items that require UINs to be captured."), UNIT_COST_BLANK_ERROR("The Unit Cost cannot be blank."),
	USER_NOT_FOUND("No user found for {0}."), VALUE_INVALID_NEGATIVE("The value must be a positive number or zero."), VALUE_INVALID_ZERO("The value must be a positive number."),
	VALUE_NOT_IN_RANGE("{0} is not within the range ({1} - {2})"), VALUE_NOT_VALID("{0} is not a valid number."), VALUE_NOT_VALID_ID("{0} is not a valid {1} ID."),
	VALUE_NOT_WHOLE("The value must be a whole number."), WIDTH_NOT_WHOLE("The width must be a whole number."), WIDTH_INVALID_NEGATIVE("The width must be a positive number or zero."),
	XML_INVALID("Invalid XML: {0}"), XML_SAVE_PROBLEM_ENCOUNTERED("Problem encountered while saving XML."), NO_IMEI_APPLIED("Please Enter The IMEI Number"),
	IMEI_EXISTS("The Entered IMEI number is already exists!"), IMEI_GENERAL("Something Went Wrong, Please Contact Administrator"), NO_QUANTITY_ENTERED("Please Enter The Quantity"),
	IMEI_QUANTITY_GREATER_THAN_PICKED_QUANTITY("IMEI Qty cannot be more than Picked Quantity"),
	IMEI_QUANTITY_GREATER_THAN_ENTERED_QUANTITY("IMEI Quantity Cannot be Greater than the Quantity Entered"), BOL_ASSIGNED("Cannot enter the IMEI since the BOL is already assigned."),
	SPECIAL_CHARACTERS("Cannot enter the IMEI with special characters."), SPACE_EXISTS("Cannot enter the IMEI with first/last character as space."), IMEI_INVALID_NUMBER("Invalid IMEI/Serial Number"), SHIP_TRAILER_FIELD_REQUIRED("{0} field is required"), SHIP_TRAILER_NUMBER_FIELD_REQUIRED("{0} field is required, if not enter Zero \"0\""), SHIP_TRAILER_INVALID_RETURN_TYPE("Return Type should be warehouse to capture ship trailer"), SHIP_TRAILER_REQUIRED("Please save the ship trailer."),
	NUMBER_NOT_NOT_IN_RANGE("The number must be between {0} and {1} for {2}.");

	private final String message;

	CommonMessageText(String paramString1) {
		this.message = paramString1;
	}

	public String getCode() {
		return name();
	}

	public String getText() {
		return this.message;
	}
}
