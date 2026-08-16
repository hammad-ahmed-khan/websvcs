package oracle.retail.sim.client.screen.printer;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Reports Format Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SessionPrinterModel extends SimScreenModel {

    private List<StorePrinter> storePrinters;
    private List<ReportTypeFormat> storeReportFormats;
    private List<SessionPrinter> sessionPrinters;

    public void loadSessionPrinters() throws Exception {
        sessionPrinters = (List<SessionPrinter>) SimRepository.getSessionPrinters();
        if (sessionPrinters == null) {
            sessionPrinters = new ArrayList<SessionPrinter>();

            List<ReportTypeFormat> manifestFormats = getSessionReportFormats(ReportFormat.MANIFEST);
            List<ReportTypeFormat> preShipmentFormats = getSessionReportFormats(ReportFormat.PRE_SHIPMENT);

            List<ReportTypeFormat> sessionReportFormats = new ArrayList<ReportTypeFormat>();
            if (!manifestFormats.isEmpty()) {
                sessionReportFormats.addAll(manifestFormats);
            }
            if (!preShipmentFormats.isEmpty()) {
                sessionReportFormats.addAll(preShipmentFormats);
            }
            for (ReportTypeFormat reportTypeFormat : sessionReportFormats) {
                SessionPrinter sessionPrinter = BOFactory.createSessionPrinter();
                sessionPrinter.setStoreId(getStoreId());
                sessionPrinter.setFormatType(reportTypeFormat.getReportType());
                sessionPrinter.setFormatName(reportTypeFormat.getFormatName());
                Long defaultPrinterId = reportTypeFormat.getDefaultPrinterId();
                if (defaultPrinterId != null) {
                    StorePrinter defaultPrinter = getPrinter(defaultPrinterId);
                    if (defaultPrinter != null) {
                        sessionPrinter.setPrinterId(reportTypeFormat.getDefaultPrinterId());
                        sessionPrinter.setDescription(defaultPrinter.getDescription());
                        sessionPrinter.setPrinterUri(defaultPrinter.getUri());
                    }
                }
                sessionPrinters.add(sessionPrinter);
            }
        }
    }

    public List<StorePrinter> getPrinters() throws Exception {
        if (storePrinters == null) {
            storePrinters = ClientServiceFactory.getReportingServices().findPrinters(getStoreId());
        }
        return storePrinters;
    }

    public List<SessionPrinterWrapper> getSessionPrinterWrappers() throws Exception {
        List<SessionPrinterWrapper> wrappers = new ArrayList<>();
        for (SessionPrinter sessionPrinter : sessionPrinters) {
            StorePrinter storePrinter = getPrinter(sessionPrinter.getPrinterId());
            wrappers.add(ClientWrapperFactory.createSessionPrinterWrapper(sessionPrinter, storePrinter));
        }
        return wrappers;
    }

    private List<ReportTypeFormat> getSessionReportFormats(ReportFormat sessionFormat) throws Exception {
        List<ReportTypeFormat> sessionReportFormats = new ArrayList<>();
        if (storeReportFormats == null) {
            storeReportFormats = ClientServiceFactory.getReportFormatServices().findAllReportTypeFormats(SimRepository.getStoreId());
        }
        for (ReportTypeFormat storeReportFormat : storeReportFormats) {
            if (storeReportFormat.getReportType().equals(sessionFormat.getCode())) {
                sessionReportFormats.add(storeReportFormat);
            }

        }
        return sessionReportFormats;
    }

    private StorePrinter getPrinter(Long printerId) throws Exception {
        List<StorePrinter> storePrinters = getPrinters();
        if (storePrinters != null) {
            for (StorePrinter printer : storePrinters) {
                if (printer.getId().equals(printerId)) {
                    return printer;
                }
            }
        }
        return null;
    }

    public void saveInSessionPrinters() {
        SimRepository.setSessionPrinters(sessionPrinters);
    }
}
