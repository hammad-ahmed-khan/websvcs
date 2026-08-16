package extra.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

/**
 * ShipmentOrderReportRequest.java aibrahim 2024
 */
public class ShipmentOrderReportRequest extends ReportRequest {

	private static final long serialVersionUID = -4326658185675319229L;

	private static final String PARAM_ID = "p_awb";

	public ShipmentOrderReportRequest(String trackingNumber) {
		addParameter(PARAM_ID, trackingNumber);
	}
}
