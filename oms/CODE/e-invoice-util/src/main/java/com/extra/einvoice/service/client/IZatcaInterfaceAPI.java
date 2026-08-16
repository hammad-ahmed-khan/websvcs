package com.extra.einvoice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.extra.einvoice.bean.ZatcaRequest;
import com.extra.einvoice.bean.ZatcaResponse;
import com.extra.einvoice.config.FeignZatcaConfiguration;

@FeignClient(name = "zatcaInterfaceAPI", url = "${zatca.api.url}", configuration = FeignZatcaConfiguration.class)
public interface IZatcaInterfaceAPI {

	@PostMapping("/invoices/clearance/single")
	ZatcaResponse clearanceApi(@RequestBody ZatcaRequest request, @RequestHeader("Clearance-Status") int clearanceStatus);

	@PostMapping("/invoices/reporting/single")
	ZatcaResponse reportingApi(@RequestBody ZatcaRequest request, @RequestHeader("Clearance-Status") int clearanceStatus);
}
