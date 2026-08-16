package oracle.retail.sim.client.screen.printer;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.report.ReportingServices;
import oracle.retail.sim.service.reportformat.ReportFormatServices;

/********************************************************************************************************
 * Reports Format Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailStorePrinterModel extends SimScreenModel {
    private List<RetailStorePrinterWrapper> wrappers = new ArrayList<>();
    private List<String> printerIdsToDelete = new ArrayList<>();

    public List<RetailStorePrinterWrapper> findPrinters() throws Exception {
        if (wrappers.isEmpty()) {
            List<StorePrinter> printers = ClientServiceFactory.getReportingServices().findPrinters(getStoreId());
            wrappers = new ArrayList<>(printers.size());
            for (StorePrinter printer : printers) {
                wrappers.add(ClientWrapperFactory.createRetailStorePrinterWrapper(printer, false));
            }
        }
        return wrappers;
    }

    public RetailStorePrinterWrapper buildNewRetailStorePrinterWrapper() {
        StorePrinter printer = BOFactory.createRetailStorePrinter();
        printer.setStoreId(getStoreId());
        return ClientWrapperFactory.createRetailStorePrinterWrapper(printer);
    }

    /**
     * This is called only when leaving the screen with "done" option. No need to update wrappers.
     */
    public void updatePrinters(List<RetailStorePrinterWrapper> wrappers) throws Exception {
        ReportingServices printerServices = ClientServiceFactory.getReportingServices();
        if (!printerIdsToDelete.isEmpty()) {
            printerServices.deletePrinters(printerIdsToDelete);
        }
        List<StorePrinter> printersToInsert = new ArrayList<>();
        List<StorePrinter> printersToUpdate = new ArrayList<>();
        for (RetailStorePrinterWrapper wrapper : wrappers) {
            if (wrapper.isNew()) {
                printersToInsert.add(wrapper.getRetailStorePrinter());
            } else if (wrapper.isDirty()) {
                printersToUpdate.add(wrapper.getRetailStorePrinter());
            }
        }
        if (!printersToInsert.isEmpty()) {
            printerServices.createPrinters(getStoreId(), printersToInsert);
        }
        if (!printersToUpdate.isEmpty()) {
            printerServices.updatePrinters(getStoreId(), printersToUpdate);
        }
    }

    public boolean deletePrinter(RetailStorePrinterWrapper wrapper) throws Exception {
        if (wrapper.isNew()) {
            return true;
        }
        ReportFormatServices formatServices = ClientServiceFactory.getReportFormatServices();
        if (formatServices.findReportTypeFormatsForPrinter(getStoreId(), wrapper.getRetailStorePrinter().getId()).isEmpty()) {
            printerIdsToDelete.add(wrapper.getRetailStorePrinter().getId().toString());
            return true;
        }
        return false;
    }
}
