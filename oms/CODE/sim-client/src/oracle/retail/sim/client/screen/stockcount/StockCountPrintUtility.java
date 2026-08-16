package oracle.retail.sim.client.screen.stockcount;

import java.util.List;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportrequest.StockCountRejectedItemReportRequest;
import oracle.retail.sim.common.reportrequest.StockCountReportRequest;
import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Utility (for the PC GUI).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountPrintUtility {

    /****************************************************************************************************
     * Private Constructor
     ***************************************************************************************************/
    private StockCountPrintUtility() {
    }

    public static ReportResponse printStockCount(Long stockCountId, Long storeId, List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        List<StockCountChild> locations = ClientServiceFactory.getStockCountChildServices().findStockCountChilds(stockCountId);
        RetailStoreFormatPrinter printer = formatPrinters.get(0);
        ReportResponse reportResponse = null;
        if (printer.getPrinter().getType() == StorePrinter.TYPE_POSTSCRIPT) {
            for (StockCountChild location : locations) {
                reportResponse = SimClientPrintUtility.printReportRequest(buildRequest(stockCountId, location.getId(), location.getPhase(), storeId), formatPrinters);
            }
        } else {
            reportResponse = SimClientPrintUtility.printReportRequest(buildRequest(stockCountId, null, null, storeId), formatPrinters);
        }
        if (reportResponse != null) {
            return reportResponse;
        }
        return null;
    }

    public static ReportResponse printStockCount(Long stockCountId, Long stockCountChildId, Long storeId, StockCountPhase phase, List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        ReportRequest request = buildRequest(stockCountId, stockCountChildId, phase, storeId);
        return SimClientPrintUtility.printReportRequest(request, formatPrinters);
    }

    /****************************************************************************************************
     * Build a report request for a stock count location report.
     ***************************************************************************************************/
    private static ReportRequest buildRequest(Long stockCountId, Long stockCountChildId, StockCountPhase phase, Long storeId) {
        StockCountReportRequest request = BOFactory.createStockCountReportRequest();
        request.setStockCountId(stockCountId);
        request.setStockCountChildId(stockCountChildId);
        request.setStockCountPhaseCode(phase != null ? phase.getCode() : null);
        return request;
    }

    /****************************************************************************************************
     * Print Third Party Rejected Items
     ***************************************************************************************************/
    public static ReportResponse printRejectedLineItems(Long storeId, StorePrinter printer) throws Exception {
        StockCountRejectedItemReportRequest request = BOFactory.createStockCountRejectedItemReportRequest(storeId);
        return SimClientPrintUtility.printReportRequest(request, printer);
    }

    public static ReportResponse printRejectedLineItems(Long storeId, List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        StockCountRejectedItemReportRequest request = BOFactory.createStockCountRejectedItemReportRequest(storeId);
        return SimClientPrintUtility.printReportRequest(request, formatPrinters);
    }
}
