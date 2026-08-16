package com.extra.einvoicing.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.extra.einvoicing.config.FeignZatcaAuthConfiguration;
import com.extra.einvoicing.model.ZatcaRequest;
import com.extra.einvoicing.model.ZatcaResponse;

/**
 * @author aibrahim
 *
 */
@FeignClient(name = "zatcaAPI", url = "${zatca.api.url}", configuration = { FeignZatcaAuthConfiguration.class })
public interface IZatcaInterfaceAPI {

	@PostMapping("/compliance/invoices")
	ZatcaResponse compilanceInvoice(@RequestBody ZatcaRequest request);

	@PostMapping("/invoices/clearance/single")
	ZatcaResponse clearanceApi(@RequestBody ZatcaRequest request, @RequestHeader("Clearance-Status") int clearanceStatus);

	@PostMapping("/invoices/reporting/single")
	ZatcaResponse reportingApi(@RequestBody ZatcaRequest request, @RequestHeader("Clearance-Status") int clearanceStatus);
}
