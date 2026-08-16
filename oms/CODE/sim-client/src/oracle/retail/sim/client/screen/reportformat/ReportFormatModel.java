package oracle.retail.sim.client.screen.reportformat;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.reportformat.ReportFormatServices;

/********************************************************************************************************
 * Reports Format Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReportFormatModel extends SimScreenModel {
    private List<StorePrinter> storePrinters = new ArrayList<>();
    private List<String> formatIdsToDelete = new ArrayList<>();
    private List<ReportFormatWrapper> reportFormatWrappers = new ArrayList<>();

    public List<StorePrinter> findPrinters() throws Exception {
        if (storePrinters.isEmpty()) {
            storePrinters = ClientServiceFactory.getReportingServices().findPrinters(getStoreId());
        }
        return storePrinters;
    }

    public List<ReportFormatWrapper> getAllWrappers() {
        return reportFormatWrappers;
    }

    public ReportFormatWrapper buildNewFormatWrapper() {
        ReportFormatWrapper wrapper = ClientWrapperFactory.createReportFormatWrapper(storePrinters);
        reportFormatWrappers.add(wrapper);
        return wrapper;
    }

    public List<ReportFormatWrapper> findAllReportFormats() throws Exception {
        return filterReportFormats(ClientServiceFactory.getReportFormatServices().findAllReportTypeFormats(getStoreId()));
    }
    
    public List<ReportFormatWrapper> filterReportFormats(List<ReportTypeFormat> reportFormats) throws Exception {
        for (ReportTypeFormat reportFormat : reportFormats) {
        	if(reportFormat.getReportType().equalsIgnoreCase(ReportFormat.DIRECT_DELIVERY_DISCREPANT_ITEMS.getCode())) {
                boolean isRemoveDamages = getStoreBoolean(StoreConfigKeys.DIRECT_DELIVERY_REMOVE_DAMAGES);
                boolean isRemoveOverReceive = getStoreBoolean(StoreConfigKeys.DIRECT_DELIVERY_REMOVE_OVER_RECEIVE);
        		if(isRemoveDamages || isRemoveOverReceive) {
        			reportFormatWrappers.add(ClientWrapperFactory.createReportFormatWrapper(reportFormat, storePrinters, false));
        		}
        	} else {
                reportFormatWrappers.add(ClientWrapperFactory.createReportFormatWrapper(reportFormat, storePrinters, false));
        	}
        }
        return reportFormatWrappers;
    }

    public List<ReportFormatWrapper> filterReportFormats(ReportFormat reportFormat) {
        if (reportFormat != null) {
            List<ReportFormatWrapper> filteredWrappers = new ArrayList<>();
            for (ReportFormatWrapper wrapper : reportFormatWrappers) {
                if (wrapper.getReportType().equals(reportFormat.getCode())) {
                    filteredWrappers.add(wrapper);
                }
            }
            return filteredWrappers;
        }
        return reportFormatWrappers;
    }

    /**
     * This is called only when leaving the screen with "done" option. No need to update wrappers.
     */
    public void updateReportTypeFormats() throws Exception {
        ReportFormatServices reportFormatServices = ClientServiceFactory.getReportFormatServices();
        if (!formatIdsToDelete.isEmpty()) {
            reportFormatServices.deleteReportTypeFormats(formatIdsToDelete);
        }
        List<ReportTypeFormat> reportTypeFormatsToInsert = new ArrayList<>();
        List<ReportTypeFormat> reportTypeFormatsToUpdate = new ArrayList<>();
        for (ReportFormatWrapper wrapper : reportFormatWrappers) {
            if (wrapper.isNew()) {
                reportTypeFormatsToInsert.add(wrapper.getReportTypeFormat());
            } else if (wrapper.isDirty()) {
                reportTypeFormatsToUpdate.add(wrapper.getReportTypeFormat());
            }
        }
        if (!reportTypeFormatsToInsert.isEmpty()) {
            reportFormatServices.createReportTypeFormats(getStoreId(), reportTypeFormatsToInsert);
        }
        if (!reportTypeFormatsToUpdate.isEmpty()) {
            reportFormatServices.updateReportTypeFormats(getStoreId(), reportTypeFormatsToUpdate);
        }
    }

    public void deleteReportType(ReportFormatWrapper wrapper) {
        if (wrapper.isNew()) {
            return;
        }
        formatIdsToDelete.add(wrapper.getReportTypeFormat().getId());
    }
}
