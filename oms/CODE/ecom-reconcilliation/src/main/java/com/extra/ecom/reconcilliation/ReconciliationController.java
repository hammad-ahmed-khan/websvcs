package com.extra.ecom.reconcilliation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.ecom.reconcilliation.bean.Response;
import com.extra.ecom.reconcilliation.service.RPMReconciliationService;

/**
 * @author aibrahim
 *
 */
@RestController
public class ReconciliationController {

	private static final Logger LOG = Logger.getLogger(ReconciliationController.class);

	@Autowired
	private RPMReconciliationService reconciliationService;

	@PostMapping(path = "/product")
	public Response reConcileProduct() {
		Response response = new Response();
		try {
			LOG.info("E-Com reconciliation process stated for Items");
			reconciliationService.reConcileItem();
			response.setCode("SUCCESS");
			LOG.info("E-Com reconciliation process completed for Items");
		} catch (Exception e) {
			LOG.error("E-Com reconciliation process failed for Items", e);
			response.setCode("ERROR");
			response.setMessage(e.getMessage());
		} finally {
			reconciliationService.updateResponse(response);
		}
		return response;
	}

	@PostMapping(path = "/price")
	public Response reConcilePrice() {
		Response response = new Response();
		try {
			LOG.info("E-Com reconciliation process stated for Price");
			reconciliationService.reConcilePrice();
			response.setCode("SUCCESS");
			LOG.info("E-Com reconciliation process completed for Price");
		} catch (Exception e) {
			LOG.error("E-Com reconciliation process failed for Price", e);
			response.setCode("ERROR");
			response.setMessage(e.getMessage());
		} finally {
			reconciliationService.updateResponse(response);
		}
		return response;
	}

	@PostMapping(path = "/promotion")
	public Response reConcilePromotion() {
		Response response = new Response();
		try {
			LOG.info("E-Com reconciliation process stated for Promotion");
			reconciliationService.reConcilePromotion();
			response.setCode("SUCCESS");
			LOG.info("E-Com reconciliation process completed for Promotion");
		} catch (Exception e) {
			LOG.error("E-Com reconciliation process failed for Promotion", e);
			response.setCode("ERROR");
			response.setMessage(e.getMessage());
		} finally {
			reconciliationService.updateResponse(response);
		}
		return response;
	}
}
