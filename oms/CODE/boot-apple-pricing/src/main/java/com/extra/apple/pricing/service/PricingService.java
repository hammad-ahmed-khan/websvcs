/**
 * 
 */
package com.extra.apple.pricing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.extra.apple.pricing.dao.PricingDAO;
import com.extra.apple.pricing.model.PriceSheet;
import com.extra.apple.pricing.model.PriceSheetDescription;

/**
 * @author aibrahim
 *
 */
@Service
public class PricingService {

	private static final Logger LOG = LoggerFactory.getLogger(PricingService.class);

	@Autowired
	private PricingDAO pricingDAO;

	@Autowired
	private AsyncAPIService asyncAPIService;

	public void updatePricingDetail() throws Exception {
		LOG.info("Fetching the price sheet ids");
		Map<Long, Long> idStoreMap = pricingDAO.getCurrentPriceIDs();
		Map<Long, Future<PriceSheet>> priceIdMap = new HashMap<Long, Future<PriceSheet>>();
		List<Future<PriceSheet>> asyncResults = new ArrayList<Future<PriceSheet>>();
		for (Entry<Long, Long> idStoreEntry : idStoreMap.entrySet()) {
			Long priceSheetId = idStoreEntry.getKey();
			Future<PriceSheet> newSheet = asyncAPIService.getPriceSheetTemplate(priceSheetId);
			asyncResults.add(newSheet);
			priceIdMap.put(priceSheetId, newSheet);
		}
		
		LOG.info("Updating the active MPNs to database");
		for (Future<PriceSheet> sheet : asyncResults) {
			try {
				PriceSheet mpnSheet = sheet.get();
				List<String> activeMPNs = new ArrayList<String>();
				for (PriceSheetDescription sheetDesc : mpnSheet.getPriceSheetDescriptions()) {
					activeMPNs.addAll(sheetDesc.getMpns());
				}
				pricingDAO.updateMPN(mpnSheet.getPriceSheetDescriptions().get(0).getProductKey().getLob(), activeMPNs);
			} catch (Exception e) {
				LOG.error("Error while updating the active mpns for " + sheet.get().getPriceSheetId(), e);
				throw new Exception("Error while updating the active");
			}
		}
		LOG.info("Active MPNs updated and published successfully to the database");
	}

	public void publishPricingDetail() throws Exception {
		List<Long> priceSheetIds = pricingDAO.getUpdatedPriceSheetIds();
		for (Long priceSheetId : priceSheetIds) {
			asyncAPIService.getPriceSheetTemplate(priceSheetId);
		}

		List<PriceSheet> priceSheets = pricingDAO.getUpdatedPriceSheet();
		Map<Long, Future<PriceSheet>> results = new HashMap<Long, Future<PriceSheet>>();
		for (PriceSheet sheet : priceSheets) {
			results.put(sheet.getPriceSheetId(), asyncAPIService.updateAndPublishPriceSheet(sheet));
		}
		List<String> errorMessages = new ArrayList<String>(priceSheets.size());
		for (Entry<Long, Future<PriceSheet>> result : results.entrySet()) {
			try {
				pricingDAO.publishPriceSheet(result.getValue().get());
				LOG.info("Price Sheet id " + result.getKey() + " is updated successfully");
			} catch (Exception e) {
				String error = "Error while updating the price sheet id " + result.getKey() + " is : " + e.getCause().getMessage();
				errorMessages.add(error);
				LOG.error(error, e);
			}
		}
		if (!errorMessages.isEmpty()) {
			throw new Exception(StringUtils.collectionToCommaDelimitedString(errorMessages));
		}
	}
}
