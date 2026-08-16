package oracle.retail.sim.client.screen.reportformat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.report.ReportBrowserLauncher;
import oracle.retail.sim.client.swing.dialog.RChoiceDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.dialog.RInfoDialog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.util.NoPrinterDefinedException;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.report.RetailStorePrinterComparator;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.reportformat.ReportFormatServices;

/********************************************************************************************************
 * A class to help with printing.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimClientPrintUtility {
    public static final String REPORT_PRINTER_KEY = "REPORT_PRINTER_FORMAT_";
    public static final String REPORTING_SERVICE_BROWSER_LAUNCHER = "REPORTING_SERVICE_BROWSER_LAUNCHER";
    public static final String BROWSER_PRINTER_URL = "browser";

    private SimClientPrintUtility() {
    }

    /****************************************************************************************************
     * Displays the printer selection dialog and returns the option selected.
     * <p>
     * @param frame The parent frame for the dialog.
     * @param printers The list of printers to selection from.
     * @return The selected printer or null if no printer was selected.
     ***************************************************************************************************/
    public static StorePrinter displayPrinterSelectionDialog(JFrame frame, List<StorePrinter> printers) {
        return displayPrinterSelectionDialog(frame, printers, ReportMessageText.PRINT_SELECT_PRINTER);
    }

    /****************************************************************************************************
     * Displays the printer selection dialog and returns the option selected.
     * <p>
     * @param frame The parent frame for the dialog.
     * @param printers The list of printers to selection from.
     * @param message The message to display.
     * @return The selected printer or null if no printer was selected.
     ***************************************************************************************************/
    public static StorePrinter displayPrinterSelectionDialog(JFrame frame, List<StorePrinter> printers, MessageText message) {
        RChoiceDialog dialog = new RChoiceDialog(Application.getFrame());
        dialog.setCommand("Printer Selection");
        dialog.setTitle("Printer Selection");
        dialog.setLabel("Printer");
        dialog.setMessage(message);
        dialog.setOptions(printers, new RetailStorePrinterComparator());
        dialog.setVisible(true);
        return (StorePrinter) dialog.getSelectedOption();
    }

    public static StorePrinter displayPrinterSelectionDialog(JFrame frame, List<StorePrinter> printers, MessageText message, String[] values) {
        RChoiceDialog dialog = new RChoiceDialog(Application.getFrame());
        dialog.setCommand("Printer Selection");
        dialog.setTitle("Printer Selection");
        dialog.setLabel("Printer");
        dialog.setMessage(message, values);
        dialog.setOptions(printers, new RetailStorePrinterComparator());
        dialog.setVisible(true);
        return (StorePrinter) dialog.getSelectedOption();
    }

    /**
     * Displays a printer and format multi selection dialog box
     * <p>
     * @param frame The parent frame for the dialog.
     * @param printers The list of printers to select from.
     * @param message The message to display.
     * @param format The list of formats to select from
     * @return <code>List</code> of printers and formats selected
     */
    public static List<RetailStoreFormatPrinter> displayPrinterSelectionDialog(JFrame frame, List<StorePrinter> printers, MessageText message, String format) throws Exception {
        List<ReportTypeFormat> reportFormats = ClientServiceFactory.getReportFormatServices().findReportTypeFormats(SimRepository.getStoreId(), format);
        RPrinterDialog dialog = new RPrinterDialog(Application.getFrame());
        dialog.setPrinters(printers);
        dialog.setFormatOptions(reportFormats);
        dialog.setVisible(true);
        return dialog.getSelectedOption();
    }

    /**
     * Displays a printer and format multi selection dialog box
     * <p>
     * @param frame The parent frame for the dialog.
     * @param printers The list of printers to select from.
     * @param message The message to display.
     * @param formats The list of formats to select from
     * @return <code>List</code> of printers and formats selected
     */
    public static List<RetailStoreFormatPrinter> displayPrinterSelectionDialog(JFrame frame, List<StorePrinter> printers, MessageText message, List<String> formats) throws Exception {
        List<ReportTypeFormat> reportFormats = new ArrayList<>(formats.size());
        ReportFormatServices reportFormatServices = ClientServiceFactory.getReportFormatServices();
        for (String format : formats) {
            reportFormats.addAll(reportFormatServices.findReportTypeFormats(SimRepository.getStoreId(), format));
        }
        RPrinterDialog dialog = new RPrinterDialog(Application.getFrame());
        dialog.setPrinters(printers);
        dialog.setFormatOptions(reportFormats);
        dialog.setVisible(true);
        return dialog.getSelectedOption();
    }

    /****************************************************************************************************
     * Find item ticket printer with dialog for printer selection.
     ***************************************************************************************************/
    public static StorePrinter selectItemTicketPrinter(ItemTicket itemTicket, Store store) throws Exception {
        List<StorePrinter> ticketPrinters = ClientServiceFactory.getReportingServices().findPrinters(store.getId());
        if (ticketPrinters.isEmpty()) {
            throw new NoPrinterDefinedException(ReportMessageText.NO_STORE_PRINTERS);
        }
        return findTicketPrinter(ticketPrinters, itemTicket.getTicketTypeFormat());
    }

    /****************************************************************************************************
     * Print ticket with dialog for printer selection.
    //     ***************************************************************************************************/
    //    public static ReportResponse printTicket(ItemTicket itemTicket, Store store) throws Exception {
    //        StorePrinter printer;
    //        try {
    //            printer = selectItemTicketPrinter(itemTicket, store);
    //        } catch (NoPrinterDefinedException e) {
    //            ReportResponse response = BOFactory.createReportResponse();
    //            response.setFailedState();
    //            response.setMessage(ReportMessageText.NO_STORE_PRINTERS);
    //            return response;
    //        }
    //        return printTicket(itemTicket, store, printer);
    //    }

    /****************************************************************************************************
     * Retrieves a printer for the store id and printer type and report format URL for a store id and
     * report format. This method handles displaying the appropriate error messages.
     * @return The selected printer and format URL or null if no printer is selected.
     ***************************************************************************************************/
    public static List<RetailStoreFormatPrinter> selectFormatPrinter(Long storeId, ReportFormat format) throws Exception {
        List<StorePrinter> printers = ClientServiceFactory.getReportingServices().findPrinters(storeId);
        if (printers.isEmpty()) {
            displayErrorMessage(ReportMessageText.NO_STORE_PRINTERS);
            return Collections.emptyList();
        }
        return displayPrinterSelectionDialog(Application.getFrame(), printers, ReportMessageText.PRINT_SELECT_PRINTER, format.getCode());
    }

    /****************************************************************************************************
     * Retrieves a printer for the store id and printer type and report format URL for a store id and
     * report format. This method handles displaying the appropriate error messages.
     * @return The selected printer and format URL or null if no printer is selected.
     ***************************************************************************************************/
    public static List<RetailStoreFormatPrinter> selectFormatPrinter(Long storeId, List<ReportFormat> formats) throws Exception {
        List<StorePrinter> printers = ClientServiceFactory.getReportingServices().findPrinters(storeId);
        if (printers.isEmpty()) {
            displayErrorMessage(ReportMessageText.NO_STORE_PRINTERS);
            return Collections.emptyList();
        }
        List<String> formatNames = new ArrayList<>(formats.size());
        for (ReportFormat format : formats) {
            formatNames.add(format.getCode());
        }
        return displayPrinterSelectionDialog(Application.getFrame(), printers, ReportMessageText.PRINT_SELECT_PRINTER, formatNames);
    }

    /****************************************************************************************************
     * Ask the user what printer to print the specified report to.
     ***************************************************************************************************/
    public static ReportResponse printReportRequest(ReportRequest request, StorePrinter printer, MessageText message) throws Exception {
        return printReportRequests(Collections.singletonList(request), printer, message);
    }

    /****************************************************************************************************
     * Ask the user what printer to print the specified report to.
     ***************************************************************************************************/
    public static ReportResponse printReportRequest(ReportRequest request, List<RetailStoreFormatPrinter> formatPrinters, MessageText message) throws Exception {
        return printReportRequests(Collections.singletonList(request), formatPrinters, message);
    }

    /****************************************************************************************************
     * Ask the user what printer to print the specified report to
     ***************************************************************************************************/
    public static ReportResponse printReportRequests(List<ReportRequest> requests, StorePrinter printer, MessageText message) throws Exception {
        List<ReportResponse> responses = callRequestReports(requests, printer);
        ReportResponse response = responses.get(0);
        if (response.isFailedState()) {
            response.setMessageValue(printer.getDescription());
            displayErrorMessage(response);
        } else {
            displayInfoMessage(message);
        }
        return response;
    }

    /****************************************************************************************************
     * Ask the user what printer to print the specified report to
     ***************************************************************************************************/
    public static ReportResponse printReportRequests(List<ReportRequest> requests, List<RetailStoreFormatPrinter> formatPrinters, MessageText message) throws Exception {
        List<ReportResponse> responses = callRequestReports(requests, SimRepository.getUser().getLocale(), formatPrinters);
        if (responses != null && responses.size() > 0) {
            ReportResponse response = responses.get(0);
            if (response.isFailedState()) {
                response.setMessageValue("Error");
                displayErrorMessage(response);
            } else {
                displayInfoMessage(message);
            }
            return response;
        }
        return null;
    }

    /****************************************************************************************************
     * Ask the user what printer to print the specified report to. This version will not automatically
     * display an error or success message.
     ***************************************************************************************************/
    public static ReportResponse printReportRequest(ReportRequest request, StorePrinter printer) throws Exception {
        List<ReportRequest> requests = Collections.singletonList(request);
        List<ReportResponse> responses = callRequestReports(requests, printer);
        ReportResponse response = responses.get(0);
        if (response.isFailedState()) {
            response.setMessageValue(printer.getDescription());
            return response;
        }
        return null;
    }

    /****************************************************************************************************
     * Ask the user what printer to print the specified report to. This version will not automatically
     * display an error or success message.
     ***************************************************************************************************/
    public static ReportResponse printReportRequest(ReportRequest request, List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        List<ReportRequest> requests = Collections.singletonList(request);
        List<ReportResponse> responses = callRequestReports(requests, Locale.getDefault(), formatPrinters);
        ReportResponse response = responses.get(0);
        if (response.isFailedState()) {
            response.setMessageValue(formatPrinters.get(0).getPrinter().getDescription());
            return response;
        }
        return null;
    }

    /****************************************************************************************************
     * Make a call to process the requests, print the reports /
     ***************************************************************************************************/
    public static List<ReportResponse> callRequestReports(List<ReportRequest> requests, StorePrinter printer) throws Exception {
        if (isBrowserLaunch(printer)) {
            return launchBrowserReportRequests(requests, SimRepository.getUser().getLocale(), printer);
        }
        return ClientServiceFactory.getReportingServices().requestReports(requests, SimRepository.getUser().getLocale(), printer);
    }

    /****************************************************************************************************
     * Make a call to process the requests, print the reports /
     ***************************************************************************************************/
    private static List<ReportResponse> callRequestReports(List<ReportRequest> requests, Locale locale, List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        List<RetailStoreFormatPrinter> browserFormats = new ArrayList<>();
        List<RetailStoreFormatPrinter> printerFormats = new ArrayList<>();
        for (RetailStoreFormatPrinter formatPrinter : formatPrinters) {
            if (isBrowserLaunch(formatPrinter.getPrinter())) {
                browserFormats.add(formatPrinter);
            } else {
                printerFormats.add(formatPrinter);
            }
        }
        List<ReportResponse> reportResponses = new ArrayList<>();
        if (browserFormats.size() > 1) {
            displayErrorMessage(ReportMessageText.MULTIPLE_BROWSER_ERROR);
            return reportResponses;
        }
        if (browserFormats.size() == 1) {
            RetailStoreFormatPrinter formatPrinter = browserFormats.iterator().next();
            for (ReportRequest request : requests) {
                request.setUrl(formatPrinter.getReportURL());
            }
            reportResponses.addAll(launchBrowserReportRequests(requests, locale, formatPrinter.getPrinter()));
        }
        if (!printerFormats.isEmpty()) {
            reportResponses.addAll(ClientServiceFactory.getReportingServices().requestReports(requests, locale, printerFormats));
        }
        return reportResponses;
    }

    /****************************************************************************************************
     * Find Specific Ticket Printers
     ***************************************************************************************************/
    private static StorePrinter findTicketPrinter(List<StorePrinter> ticketPrinters, TicketTypeFormat typeFormat) {
        if (ticketPrinters.size() == 1) {
            return ticketPrinters.get(0);
        }
        Long printerId = typeFormat.getDefaultPrinterId();
        if (printerId != null) {
            for (StorePrinter tmpPrinter : ticketPrinters) {
                if (tmpPrinter.getId() == printerId.longValue()) {
                    return tmpPrinter;
                }
            }
        }
        String[] values = new String[] { typeFormat.getFormatName() };
        return displayPrinterSelectionDialog(Application.getFrame(), ticketPrinters, ReportMessageText.PRINT_SELECT_FOR_FORMAT, values);
    }

    /****************************************************************************************************
     * Message Helpers
     ***************************************************************************************************/
    private static void displayInfoMessage(MessageText message) {
        if (message != null) {
            RInfoDialog dialog = new RInfoDialog(Application.getFrame(), "Print Information");
            dialog.displayMessage(message);
        }
    }

    private static void displayErrorMessage(MessageText message) {
        if (message != null) {
            RErrorDialog dialog = new RErrorDialog(Application.getFrame());
            dialog.setTitle("Print Error");
            dialog.setMessage(message);
            dialog.activate();
        }
    }

    private static void displayErrorMessage(ReportResponse response) {
        if (response != null) {
            RErrorDialog dialog = new RErrorDialog(Application.getFrame());
            dialog.setTitle("Print Error");
            dialog.setMessage(response.getMessage(), new Object[] { response.getMessageValue() });
            dialog.activate();
        }
    }

    private static boolean displayConfirmMessage(MessageText message) {
        if (message != null) {
            RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), "Print Confirm");
            dialog.setMessage(message);
            dialog.setYesNoType();
            return dialog.getConfirmation();
        }
        return false;
    }

    private static List<ReportResponse> launchBrowserReportRequests(List<ReportRequest> requests, Locale locale, StorePrinter printer) {
        ReportBrowserLauncher launcher;
        try {
            launcher = getReportBrowserLauncher();
        } catch (Exception exception) {
            ReportResponse response = BOFactory.createReportResponse();
            response.setMessage(ReportMessageText.PRINTING_ERROR);
            response.setFailedState();
            response.setPrintResponse(exception.getMessage());
            return Collections.singletonList(response);
        }
        return launcher.launchReportRequests(requests, locale, printer);
    }

    private static boolean isBrowserLaunch(StorePrinter printer) {
        String uri = printer.getUri();
        return uri != null && uri.toLowerCase().startsWith(BROWSER_PRINTER_URL);
    }

    public static void launchBrowserShowReports(Store store) {
        try {
            getReportBrowserLauncher().launchShowReports(store);
        } catch (Throwable t) {
            UIStatusUtility.displayException(SimClientPrintUtility.class, t);
        }
    }

    private static ReportBrowserLauncher getReportBrowserLauncher() throws Exception {
        ReportBrowserLauncher launcher = Application.getConfigManager().getObject(REPORTING_SERVICE_BROWSER_LAUNCHER, ReportBrowserLauncher.class);
        if (launcher == null) {
            throw new BusinessException(ReportMessageText.REPORT_TOOL_ERROR);
        }
        return launcher;
    }

    public static ReportResponse checkResponseFailure(List<ReportResponse> responses) {
        if (responses != null && responses.size() > 0) {
            for (ReportResponse response : responses) {
                if (response.isFailedState()) {
                    return response;
                }
            }
        }
        return null;

    }

}
