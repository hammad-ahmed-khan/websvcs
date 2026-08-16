package oracle.retail.sim.client.displayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;

/********************************************************************************************************
 * Ticket Type Format Displayer
 * <p>
 * This displayer can work differently than other renderers throughout the application - this is done
 * since because of the way the displayer is used in some situations. The displayer works exactly like
 * all other displayers if an actual CMSReportTypeFormat object is passed in to the getDisplayText()
 * method. The displayer works differently if it is passed anything other than a CMSReportTypeFormat
 * object. In this case, it will try to look up the value from a previously loaded Map. The map contains
 * all of the ticket type formats. This should be used in the case that a business object expects the
 * info code rather than the actual ticket type format object itself.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReportTypeFormatDisplayer extends AbstractDisplayer {

    private Map<String, ReportTypeFormat> reportTypeFormatMap = new HashMap<>();

    /****************************************************************************************************
     * Returns an unmodifiable view of the Map containing the loaded ticket type formats.
     ***************************************************************************************************/
    public Map getReportTypeFormats() {
        return Collections.unmodifiableMap(reportTypeFormatMap);
    }

    /****************************************************************************************************
     * Builds a map of ticket type formats.
     ***************************************************************************************************/
    public void setReportTypeFormats(List<ReportTypeFormat> reportTypeFormats) {
        reportTypeFormatMap.clear();

        if (reportTypeFormats != null) {
            for (ReportTypeFormat format : reportTypeFormats) {
                reportTypeFormatMap.put(format.getId(), format);
            }
        }
    }

    /****************************************************************************************************
     * Determines whether the renderer has a Map loaded with all the ticket type data.
     ***************************************************************************************************/
    public boolean isReportTypesLoaded() {
        return !reportTypeFormatMap.isEmpty();
    }

    /****************************************************************************************************
     * Retrieves the display text. If not TickTypeFormat object, assume it is an ID and attempt to pull
     * from loaded ticket types.
     ***************************************************************************************************/
    public String getDisplayText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        }
        ReportTypeFormat reportTypeFormat = null;
        if (value instanceof ReportTypeFormat) {
            reportTypeFormat = (ReportTypeFormat) value;
        } else {
            reportTypeFormat = reportTypeFormatMap.get(value);
        }
        if (reportTypeFormat != null) {
            return Translator.getText(reportTypeFormat.getReportType());
        }
        return StringConstants.EMPTY;
    }
}
