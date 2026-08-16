package oracle.retail.sim.client.report.bipublisher;

import java.util.HashMap;
import java.util.Locale;
import oracle.retail.sim.client.report.ReportBrowserLauncher;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.store.Store;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class BIPublisherBrowserReportLauncher extends ReportBrowserLauncher {
    private static final String REPORT_XSCH_XPF_KEY = "_xpf";
    private static final String REPORT_XSCH_XPT_KEY = "_xpt";
    private static final String REPORT_FORMAT_KEY = "_xf";
    private static final String REPORT_LOCALE_KEY = "_xl";
    private static final String REPORT_FORMAT_HTML_VALUE = "html";
    private static final String REPORT_FORMAT_XML_VALUE = "xml";
    private static final String REPORTS_URL_FRAGMENT = "/servlet/report";

    protected ReportResponse launchReportRequest(ReportRequest request, Locale locale, StorePrinter printer) {
        try {
            request.setStoreTimezoneId(ClientDataCacheUtility.getStore(printer.getStoreId()).getTimeZone().getID());
        } catch (Exception e) {
            return buildFailureResponse(e.getMessage());
        }
        String urlString = getReportingToolUrl();
        if (!urlString.toLowerCase().startsWith("http")) {
            return buildFailureResponse(ReportMessageText.INVALID_REPORTING_TOOL_REQUEST_URL.getText());
        }
        return launch(urlString, locale, request, printer);
    }

    private ReportResponse launch(String uri, Locale locale, ReportRequest request, StorePrinter printer) {
        HashMap<String, String> params = new HashMap<>();
        params.put(REPORT_XSCH_XPF_KEY, "");
        params.put(REPORT_XSCH_XPT_KEY, "");
        if (printer.getType() == StorePrinter.TYPE_POSTSCRIPT) {
            params.put(REPORT_FORMAT_KEY, REPORT_FORMAT_HTML_VALUE);
        } else {
            params.put(REPORT_FORMAT_KEY, REPORT_FORMAT_XML_VALUE);
        }
        params.put(REPORT_LOCALE_KEY, locale.toString());
        params.putAll(request.getParameters());
        try {
            launchUrl(uri + request.getUrl(), params);
        } catch (Exception exception) {
            LogService.error(this, exception.getMessage(), exception);
            return buildFailureResponse(exception.getMessage());
        }
        return buildSuccessResponse();
    }

    public void launchShowReports(Store store) throws Exception {
        String url = getReportingToolUrl();
        if (StringHelper.isNullOrEmpty(url)) {
            throw new BusinessException(ReportMessageText.REPORT_TOOL_ERROR);
        }
        launchUrl(url + REPORTS_URL_FRAGMENT);
    }

    /**
     * Parses the response string and returns a reportResponse object
     * @param responseString the response string from http post
     * @return the reportResponse object
     *
     */
    private ReportResponse buildSuccessResponse() {
        ReportResponse response = BOFactory.createReportResponse();
        return response;
    }

    /**
     * Parses the response string and returns a reportResponse object
     * @param responseString the response string from http post
     * @return the reportResponse object
     *
     */
    private ReportResponse buildFailureResponse(String responseText) {
        ReportResponse response = BOFactory.createReportResponse();
        response.setFailedState();
        response.setMessage(ReportMessageText.PRINTING_ERROR);
        response.setPrintResponse(responseText);
        return response;
    }

}
