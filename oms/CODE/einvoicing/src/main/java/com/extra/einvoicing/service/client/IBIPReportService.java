package com.extra.einvoicing.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.extra.einvoicing.config.FeignSOAPConfiguration;
import com.oracle.xmlns.oxp.service.publicreportservice.DeliveryService;
import com.oracle.xmlns.oxp.service.publicreportservice.DeliveryServiceResponse;
import com.oracle.xmlns.oxp.service.publicreportservice.RunReport;
import com.oracle.xmlns.oxp.service.publicreportservice.RunReportResponse;

/**
 * @author aibrahim
 *
 */
@FeignClient(name = "bipService", url = "${report.bip.url}", configuration = FeignSOAPConfiguration.class)
public interface IBIPReportService {

	@PostMapping()
	RunReportResponse runReport(@RequestBody RunReport runReport);

	@PostMapping()
	DeliveryServiceResponse delivery(@RequestBody DeliveryService deliveryService);
}
