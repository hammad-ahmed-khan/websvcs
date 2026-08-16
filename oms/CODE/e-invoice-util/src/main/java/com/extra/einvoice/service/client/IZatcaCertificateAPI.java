/**
 * 
 */
package com.extra.einvoice.service.client;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.extra.einvoice.bean.CSRRequest;
import com.extra.einvoice.bean.CSRResponse;
import com.extra.einvoice.bean.ZatcaRequest;
import com.extra.einvoice.bean.ZatcaResponse;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * 
 */
@Headers({"Accept-Version:V2", "Accept:application/json", "Content-Type:application/json"})
public interface IZatcaCertificateAPI {

	@RequestLine("POST /compliance")
	@Headers("OTP: {otp}")
	CSRResponse getCompilanceCSID(@RequestBody CSRRequest request, @Param("otp") String otp);

	@RequestLine("POST /compliance/invoices")
	@Headers("Accept-Language:en")
	ZatcaResponse compilanceInvoice(ZatcaRequest request);

	@RequestLine("POST /production/csids")
	CSRResponse getProductionCSID(Map<String, String> request);

	@RequestLine("POST /invoices/reporting/single")
	@Headers({"Clearance-Status: {Clearance-Status}", "Accept-Language:en"})
	ZatcaResponse reportingApi(ZatcaRequest request, @Param("Clearance-Status") int clearanceStatus);
}
