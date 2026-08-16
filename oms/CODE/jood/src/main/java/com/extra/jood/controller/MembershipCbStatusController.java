package com.extra.jood.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.jood.bean.MemberShipCbInfo;
import com.extra.jood.bean.MembershipCbResponse;
import com.extra.jood.bean.MembershipResponse;
import com.extra.jood.bean.ResponseInfo;
import com.extra.jood.service.JOODService;



@RestController
public class MembershipCbStatusController {

	private static final Logger LOG = Logger.getLogger(MembershipCbStatusController.class);

	@Autowired
	private JOODService joodService;

	@PostMapping(path = "/cbstatus")
	public MembershipCbResponse checkCashBackStatus(@RequestBody MemberShipCbInfo request) {
		MembershipCbResponse membershipCbResponse = null;
		try {
			LOG.info("JOOD cashback status: start getting status");
			membershipCbResponse = joodService.getTransactionCbStatus(request);
			LOG.info("JOOD cashback status: getting status completed");
		} catch (Exception e) {
			LOG.error("JOOD membership eligibility: error while checking cashback status for member id " + request.getActiveMembershipID(), e);
			ResponseInfo response = new ResponseInfo();
	        membershipCbResponse = new MembershipCbResponse();
			membershipCbResponse.setResponseHeader(response); 
			response.setResCode("CashBackStatus_Check_Failed");
			response.setMessage(e.getMessage());
			response.setStatus('F');
		}
		return membershipCbResponse;
	}
}
