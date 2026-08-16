/**
 * 
 */
package com.extra.apple.pricing.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.extra.apple.pricing.model.PriceSheet;

import feign.Param;

/**
 * @author aibrahim
 *
 */
@Service
public class AsyncAPIService {

	@Autowired
	private AppleAPIService appleAPIService;

	@Async
	Future<PriceSheet> updateAndPublishPriceSheet(PriceSheet priceSheet) throws Exception {
		appleAPIService.instantPriceSheet(priceSheet);
		return new AsyncResult<PriceSheet>(priceSheet);
	}

	@Async
	Future<PriceSheet> getPriceSheetTemplate(@Param("priceSheetId") Long priceSheetId) {
		return new AsyncResult<PriceSheet>(appleAPIService.getPriceSheetTemplate(priceSheetId));
	}
}
