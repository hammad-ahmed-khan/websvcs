package com.extra.jood.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.jood.bean.MembershipInfo;
import com.extra.jood.bean.MembershipResponse;
import com.extra.jood.bean.ResponseInfo;
import com.extra.jood.service.JOODService;

/**
 * aibrahim
 * 2024
 */

@RestController
@RequestMapping(path = "/membership")
public class MembershipController {

	private static final Logger LOG = Logger.getLogger(MembershipController.class);

	@Autowired
	private JOODService joodService;

	@PostMapping(path = "/eligibility")
	public MembershipResponse validateEligibility(@RequestBody MembershipInfo membershipInfo) {
		MembershipResponse membershipResponse = null;
		try {
			LOG.info("JOOD membership eligibility: start validating eligibility");
			membershipResponse = joodService.validateEligibility(membershipInfo);
			LOG.info("JOOD membership eligibility: validating eligibility completed");
		} catch (Exception e) {
			LOG.error("JOOD membership eligibility: error while validating eligibility for member id " + membershipInfo.getActiveMembershipID(), e);
			membershipResponse = new MembershipResponse();
			ResponseInfo response = new ResponseInfo();
			membershipResponse.setResponseHeader(response); 
			response.setResCode("Elgiibility_Check_Failed");
			response.setMessage(e.getMessage());
			response.setStatus('F');
		}
		return membershipResponse; 
	}
}
