package oracle.retail.sim.client.core;

/********************************************************************************************************
 * This class contains some common SIM navigation commands. Each of the menu items has a static variable
 * declaration here for readability and consistency. The value is the button identifier, which is also
 * its label and its identifier when declared in that navigation.xml configuration.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimNavigation {

    // Navigation Buttons
    public static final String ACCEPT = "Accept";
    public static final String ADD = "Add";
    public static final String ADD_ITEM = "Add Item";
    public static final String ADD_SEQUENCE = "Add Location";
    public static final String ADDITIONAL_SUPPLIERS = "Additional Suppliers";
    public static final String ADJUST = "Adjust";
    public static final String APPLY = "Apply";
    public static final String APPLY_CLASS_LIST = "Apply Class List";
    public static final String APPLY_ITEM_LIST = "Apply Item List";
    public static final String APPLY_TO_ALL = "Apply To All";
    public static final String APPROVE = "Approve";
    public static final String ASSIGN_PASSWORD = "Assign Password";
    public static final String ASSIGN_ROLES = "Assign Roles";
    public static final String ASSIGN_STORES = "Assign Stores";
    public static final String AUTHORIZE = "Authorize";
    public static final String BACK = "Back";
    public static final String BILL_OF_LADING = "BOL";
    public static final String BINS = "Bins";
    public static final String CANCEL = "Cancel";
    public static final String CANCEL_SUBMIT = "Cancel Submit";
    public static final String CHANGE_PASSWORD = "Change Password";
    public static final String COLORS = "Colors";
    public static final String COMPLETE_COUNT = "Complete Count";
    public static final String COMPONENT_INFO = "Component Info";
    public static final String CONFIRM = "Confirm";
    public static final String CONFIRM_AUTHORIZATION = "Confirm Authorization";
    public static final String CONFIRM_CHILD = "Confirm Child";
    public static final String COPY = "Copy";
    public static final String COPY_ASSIGNMENTS = "Copy Assignments";
    public static final String COUNT_DETAIL = "Count Detail";
    public static final String CREATE = "Create";
    public static final String CREATE_DELIVERY = "Create Delivery";
    public static final String CUSTOMER = "Customer";
    public static final String CUSTOMER_ORDER = "Customer Order";
    public static final String CUSTOMER_ORDERS = "Customer Orders";
    public static final String DATA_PERMISSION = "Data Permissions";
    public static final String DEALS_QUERY = "Deals Query";
    public static final String DEFAULT_QUANTITIES = "Default Quantities";
    public static final String DELETE = "Delete";
    public static final String DELIVERY = "Delivery";
    public static final String DISPATCH = "Dispatch";
    public static final String EDIT = "Edit";
    public static final String EDIT_ITEMS = "Edit Items";
    public static final String EDIT_SEQUENCES = "Edit Locations";
    public static final String EXIT = "Exit";
    public static final String FINISHER_LOOKUP = "Finisher Lookup";
    public static final String FONTS = "Fonts";
    public static final String HELP = "Help";
    public static final String ICONS = "Icons";
    public static final String ITEM_ORDERS = "Item's Orders";
    public static final String ITEM_SALES = "Item's Sales";
    public static final String ITEM_SUBSTITUTION = "Item Substitution";
    public static final String ITEM_TICKET = "Item Tickets";
    public static final String LOGIN = "Login";
    public static final String LOGOUT = "Logout";
    public static final String MASS_ASSIGN_ROLES = "Mass Assign Roles";
    public static final String MASS_ASSIGN_STORES = "Mass Assign Stores";
    public static final String MOVE_DOWN = "Move Down";
    public static final String MOVE_UP = "Move Up";
    public static final String NON_SELLABLE = "Non-Sellable";
    public static final String NOTES = "Notes";
    public static final String PACK_INFO = "Pack Info";
    public static final String PASSWORD_CONFIGURATION = "Password Configuration";
    public static final String PRICE_CHANGE = "Price Change";
    public static final String PRICE_INFORMATION = "Price Information";
    public static final String PRINT = "Print";
    public static final String PRINT_TICKETS = "Print Tickets";
    public static final String RECEIVE = "Receive";
    public static final String RECEIVE_ALL = "Receive All";
    public static final String REFRESH = "Refresh";
    public static final String REJECT = "Reject";
    public static final String REJECTED_ITEMS = "Rejected Items";
    public static final String RELATED_ITEMS = "Related Items";
    public static final String REMOVE = "Remove";
    public static final String REMOVE_ITEM = "Remove Item";
    public static final String REMOVE_SEQUENCE = "Remove Location";
    public static final String REQUEST = "Request";
    public static final String REPORTS = "Reports";
    public static final String RESET = "Reset";
    public static final String RESOLVE = "Resolve";
    public static final String REVERSE_PICK = "Reverse Pick";
    public static final String SAVE = "Save";
    public static final String SAVE_CHILD = "Save Child";
    public static final String SCANNER = "Scanner";
    public static final String SEARCH = "Search";
    public static final String SHELF_LABELS = "Shelf Labels";
    public static final String START = "Start";
    public static final String STOCK_LOCATOR = "Stock Locator";
    public static final String SUBMIT = "Submit";
    public static final String SUPPLIER_DETAIL = "Supplier Detail";
    public static final String STOP = "Stop";
    public static final String STORE_ORDERS = "Store Orders";
    public static final String TAKE_SNAPSHOT = "Take Snapshot";
    public static final String TRANSFER_INFO = "Transfer Info";
    public static final String UDA_DETAIL = "UDA Detail";
    public static final String UIN_ATTRIBUTES = "UIN Attributes";
    public static final String UIN_DETAIL = "UIN Detail";
    public static final String UIN_PRINT_TICKET = "Print Ticket";
    public static final String UIN_REMOVE = "Remove UIN";
    public static final String UIN_RESOLUTION = "UIN Resolution";
    public static final String UIN_SELECT = "Select UIN";
    public static final String UNRECEIVE = "Un-Receive";
    public static final String UPDATE_AUTH_QTY = "Update Auth Qty";
    public static final String UPDATE_SNAPSHOT = "Update Snapshot";
    public static final String UPDATE_SOH = "Update SOH";
    public static final String USE_ASN = "Use ASN";
    public static final String USER_MAINTENANCE = "User Maintenance";
    public static final String VIEW_HISTORY = "View History";
    
    public static final String IMEI="IMEI";
    
    // Dialog Button Labels
    public static final String DIALOG_AUTO_GENERATE = "Auto Generate";
    public static final String DIALOG_ADD = "Add";
    public static final String DIALOG_ADD_ITEM = "Add Item";
    public static final String DIALOG_APPLY = "Apply";
    public static final String DIALOG_CANCEL = "Cancel";
    public static final String DIALOG_CLOSE = "Close";
    public static final String DIALOG_JUMP = "Jump";
    public static final String DIALOG_PRINT = "Print";
    public static final String DIALOG_REMOVE = "Remove";
    public static final String DIALOG_RESET = "Reset";
    public static final String DIALOG_RESTORE = "Restore";
    public static final String DIALOG_SEARCH = "Search";

    // This one is only used as label text in widgets.
    public static final String LOOKUP = "...";
    
    // Text that causes navigate to go back to previous screen.
    public static final String PREVIOUS_SCREEN = "BACK";
    
    /*
     * For Ship Trailer Screen.
     */
	public static final String SHIP_TRAILER = "Ship Trailer";
	public static final String SHIP_TRAILER_DRAFT = "Draft";
	public static final String SHIP_TRAILER_UPDATE = "Update";
	public static final String SHIP_TRAILER_SAVE = "Save";

    private SimNavigation() {
    }
}
