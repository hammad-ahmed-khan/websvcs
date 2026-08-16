package oracle.retail.sim.client.report;

import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.store.Store;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public abstract class ReportBrowserLauncher {
    public static final String REPORTS_EXECUTABLE = "REPORTS_EXECUTABLE";
    public static final int BROWSER_LAUNCH_LIMIT = 5;

    protected abstract ReportResponse launchReportRequest(ReportRequest request, Locale locale, StorePrinter printer);

    public List<ReportResponse> launchReportRequests(List<ReportRequest> requests, Locale locale, StorePrinter printer) {
        if (requests.size() > BROWSER_LAUNCH_LIMIT) {
            LogService.error(this, ReportMessageText.PRINTING_BROWSER_LIMIT.getText());
            ReportResponse response = BOFactory.createReportResponse();
            response.setMessage(ReportMessageText.PRINTING_ERROR);
            response.setFailedState();
            response.setPrintResponse(ReportMessageText.PRINTING_BROWSER_LIMIT.getText());
            return Collections.singletonList(response);
        }
        List<ReportResponse> responses = new ArrayList<>(requests.size());
        for (ReportRequest request : requests) {
            responses.add(launchReportRequest(request, locale, printer));
        }
        return responses;
    }

    public void launchShowReports(Store store) throws Exception {
    }

    protected String getReportingToolUrl() {
        return SimConfigManager.getString(SimConfigManager.REPORTING_TOOL_URL);
    }

    protected void launchUrl(String uri, HashMap<String, String> params) throws Exception {
        String postDataString = buildUriParamsFromProperties(params, true);
        LogService.debug(this, postDataString);
        launchUrl(uri + "?" + postDataString);
    }

    protected void launchUrl(String uri) throws Exception {
        try {
            //Lookup the javax.jnlp.BasicService object
            BasicService bs = (BasicService) ServiceManager.lookup(BasicService.class.getName());
            bs.showDocument(new URL(uri));
        } catch (UnavailableServiceException jex) {
            LogService.error(this, "javax.jnlp.BasicService unavailable");
            tryThroughCommandLine(uri);
        }
    }

    protected void tryThroughCommandLine(String reportURL) throws Exception {
        LogService.debug(this, "Launching... " + reportURL);
        String reportExecutable = Application.getConfigManager().getString(REPORTS_EXECUTABLE);
        Runtime.getRuntime().exec(MessageFormat.format(reportExecutable, reportURL.replaceAll("\\&", "\"" + "\\&" + "\"")));
    }

    protected static String buildUriParamsFromProperties(HashMap<String, String> params, boolean encode) throws Exception {
        StringBuilder postData = new StringBuilder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (encode) {
                value = URLEncoder.encode(value, "UTF-8");
            }
            postData.append(key).append("=").append(value).append("&");
        }
        String postDataString = postData.toString();
        return postDataString.substring(0, postDataString.length() - 1);
    }
}
