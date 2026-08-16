package oracle.retail.sim.common.report;

import oracle.retail.sim.common.business.MessageText;

public enum ReportMessageText implements MessageText {
  AGSN_LABEL_PRINTED("AGSN label printed."),
  BIP_AUTHENTICATION_FAILURE_MESSAGE("BI Publisher Authentication unsuccessful."),
  BIP_REPORT_DEFINITION_NOT_FOUND("Report definition not found."),
  BIP_REPORT_INVALID_PARAMETERS("Invalid parameters."),
  DEFAULT_FORMAT_NOT_FOUND("The Default Format for this report could not be found. Please ensure the Default Format is specified."),
  INVALID_REPORTING_TOOL_REQUEST_URL("Invalid Reporting tool request URL."),
  ITEM_TICKET_DIFFERENT_TYPES("Items have been selected with different ticket types, would you still like to print the tickets?"),
  ITEM_TICKET_PRINTED("Item Ticket Printed"),
  ITEM_TICKETS_PRINTED("Item Tickets Printed"),
  ITEM_TICKETS_NOT_PRINTED("Item Ticket Not Printed"),
  MULTIPLE_BROWSER_ERROR("Multiple browsers not supported."),
  NO_ROW_SELECTED_FOR_PRINTING("At least one record must be selected to print."),
  NO_ROWS_SELECTED_PRINT("You must have one or more rows selected to print."),
  NO_STORE_PRINTERS("There are no printers defined for your store."),
  NO_DEFAULT_FORMAT_PRINTERS("No default printer assigned to report format."),
  PRINTING_ERROR("There was an error, the report was unable to print to {0}."),
  PRINT_SELECT_PRINTER("Please select a printer."),
  PRINT_SELECT_FOR_FORMAT("Please select a printer for format {0}."),
  PRINTER_DUPLICATE_ERROR("Duplicate Printer Description"),
  PRINTER_DESCRIPTION_REQUIRED("Printer description required"),
  PRINTER_DELETE_CONFIRM("Do you want to delete the printer(s)?"),
  PRINTER_DEFAULT_CANNOT_DELETE("Printer {0} assigned as a default printer for a format cannot be deleted"),
  PRINTER_NETWORK_ADDRESS_REQUIRED("Printer network address required"),
  PRINTER_TYPE_REQUIRED("Printer Type required"),
  PRINTING_BROWSER_LIMIT("Cannot launch more than 5 browsers"),
  REPORT_DELETE_FORMAT_CONFIRM("Are you sure you want to delete the selected report type formats?"),
  REPORT_DUPLICATE_FORMAT_ERROR("Duplicate report type formats exist."),
  REPORT_FORMAT_NO_DEFAULT("No default report setup for {0}."),
  REPORT_FORMAT_REQUIRED("Report type format name is required."),
  REPORT_FORMAT_URL_REQUIRED("Report template URL Location is required."),
  REPORT_FORMAT_SINGLE_SELECT("Only one default format can be selected for {0}."),
  REPORT_FORMAT_NOT_SELECTED("No report selected. Please select a report."),
  REPORT_TOOL_ERROR("Reporting tool could not be located."),
  TEMPLATE_MISSING("The template for the selected format does not exist. Please check with system administrator.");
  
  private final String message;
  
  ReportMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\report\ReportMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */