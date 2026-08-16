package com.extra.einvoicing.ebs;


import com.oracle.xmlns.oxp.service.publicreportservice.ReportRequest;
import com.oracle.xmlns.oxp.service.publicreportservice.ReportResponse;

import feign.RequestLine;

/**
 * @author aibrahim
 *
 */
public interface BIPReportService {

	@RequestLine("POST")
	ReportResponse runReport(ReportRequest reportRequest);
}
