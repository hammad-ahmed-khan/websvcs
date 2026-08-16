package extra.retail.sim.client.screen.reportformat;

import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.screen.reportformat.RPrinterDialog;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.service.core.ClientServiceFactory;

/**
 * ExtraSimClientPrintUtility.java
 * aibrahim
 * 2024
 */
public class ExtraSimClientPrintUtility {

	public static List<RetailStoreFormatPrinter> selectFormatPrinter(Long storeId, SimEnum<String> format) throws Exception {
		List<StorePrinter> printers = ClientServiceFactory.getReportingServices().findPrinters(storeId);
		if (printers.isEmpty()) {
			displayErrorMessage(ReportMessageText.NO_STORE_PRINTERS);
			return Collections.emptyList();
		}
		return displayPrinterSelectionDialog(Application.getFrame(), printers, ReportMessageText.PRINT_SELECT_PRINTER, format.getCode());
	}

	public static List<RetailStoreFormatPrinter> displayPrinterSelectionDialog(JFrame frame, List<StorePrinter> printers, MessageText message, String format) throws Exception {
        List<ReportTypeFormat> reportFormats = ClientServiceFactory.getReportFormatServices().findReportTypeFormats(SimRepository.getStoreId(), format);
        RPrinterDialog dialog = new RPrinterDialog(Application.getFrame());
        dialog.setPrinters(printers);
        dialog.setFormatOptions(reportFormats);
        dialog.setVisible(true);
        return dialog.getSelectedOption();
    }

	private static void displayErrorMessage(MessageText message) {
		if (message != null) {
			RErrorDialog dialog = new RErrorDialog(Application.getFrame());
			dialog.setTitle("Print Error");
			dialog.setMessage(message);
			dialog.activate();
		}
	}
}
