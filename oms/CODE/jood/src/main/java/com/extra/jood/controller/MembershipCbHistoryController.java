package com.extra.jood.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.jood.bean.CbHistoryRequest;
import com.extra.jood.bean.CbHistoryResponse;
import com.extra.jood.bean.ResponseInfo;
import com.extra.jood.service.JOODService;

@RestController
public class MembershipCbHistoryController {

	private static final Logger LOG = Logger.getLogger(MembershipCbHistoryController.class);

	@Autowired
	private JOODService joodService;

	@PostMapping(path = "/cb/history")
	public CbHistoryResponse checkCashBackHistory(@RequestBody CbHistoryRequest request) {
		CbHistoryResponse cbHistoryResponse = new CbHistoryResponse();
		try {
			LOG.info("JOOD cashback history: start getting history");
			cbHistoryResponse = joodService.getCbHistoryTransaction(request);
			LOG.info("JOOD cashback history: getting history completed");
		} catch (Exception e) {
			LOG.error("JOOD membership eligibility: error while checking cashback history for member id " + request.getActiveMembershipID(), e);
			ResponseInfo response = new ResponseInfo();
			response.setResCode("CashBackHistory_Check_Failed");
			response.setMessage(e.getMessage());
			response.setStatus('F');
			cbHistoryResponse.setResponseHeader(response); 
		}
		return cbHistoryResponse;
	}
}
