package com.extra.apple.pricing.service;

import com.extra.apple.pricing.model.PriceSheet;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author aibrahim
 *
 */
public interface AppleAPIService {

	@RequestLine("GET /price-sheets/{priceSheetId}/template")
	PriceSheet getPriceSheetTemplate(@Param("priceSheetId") Long priceSheetId);

	@RequestLine("PUT /instant-pricing/mpns")
	@Headers({"Content-Type: application/json"})
	PriceSheet instantPriceSheet(PriceSheet priceSheet);

	@RequestLine("DELETE /instant-pricing/lob/{product}/store/{priceSheetId}")
	void deletePriceSheet(@Param("product") String product, @Param("priceSheetId") Long priceSheetId);
}
