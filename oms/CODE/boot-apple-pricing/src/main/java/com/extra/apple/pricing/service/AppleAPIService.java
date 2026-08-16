package com.extra.apple.pricing.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.extra.apple.pricing.config.FeignConfiguration;
import com.extra.apple.pricing.model.PriceSheet;

/**
 * @author aibrahim
 *
 */
@FeignClient(name = "appleAPIService", configuration = FeignConfiguration.class, url = "${apple.pricing.url.v2}")
public interface AppleAPIService {

	@GetMapping("/price-sheets/{priceSheetId}/template")
	PriceSheet getPriceSheetTemplate(@PathVariable("priceSheetId") Long priceSheetId);

	@PutMapping(path = "/instant-pricing/mpns", consumes = MediaType.APPLICATION_JSON_VALUE)
	PriceSheet instantPriceSheet(PriceSheet priceSheet);

	@DeleteMapping("/instant-pricing/lob/{product}/store/{priceSheetId}")
	void deletePriceSheet(@PathVariable("product") String product, @PathVariable("priceSheetId") Long priceSheetId);
}
