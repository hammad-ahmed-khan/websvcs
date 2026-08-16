package com.extra.oms.custOrder.service;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.extra.oms.custOrder.config.MvcConfiguration;
import com.extra.oms.custOrder.model.CustomerOrder;
import com.extra.oms.custOrder.model.CustomerOrderResponse;
import com.extra.oms.custOrder.model.CustomerOrderTenders;
import com.extra.oms.custOrder.util.OmsErrorCodeConstants;
import com.extra.oms.custOrder.util.UtillConstantCodes;

public class EComOrderServiceImpl {
	private static final Logger log = LogManager.getLogger(MvcConfiguration.class);
	CustomerOrderResponse res = new CustomerOrderResponse();
	
	public CustomerOrderResponse validateInput(CustomerOrder input) throws Exception {
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "*** Start validation ***");
		if (input.getOrderType().equals("B2B")) {
			if (input.getCustomerSubOrderNo() == null) {
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.SubCustomerReqid);
				return res;
				}
		}
		if (input.getDeliveryType().equals("S") ||input.getDeliveryType().equals("SS") ||input.getDeliveryType().equals("SC")) {
			if (input.getCustomerOrderAddress() == null) {
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.customerAddressReqid);
				return res;
			}
		}
		if (input.getDeliveryType().equals("C") || input.getDeliveryType().equals("CC")) {
			if (input.getPickLoc() == null) {
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.pickUpLocationReqid);
				return res;
			}
		}
		for (CustomerOrderTenders tender : input.getCustomerOrderTenders()) {
			List<String> codeList = Collections.emptyList();
			int found = 0;
			for (String code : codeList) {
				if (code.equals(tender.getTenderType()) == true) {
					found = 1;
					break;
				}
			}
			if (found == 0) {
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.invalidTenderCode);
				return res;
			}
		
		if (tender.getTenderType().equals("CHECK") || tender.getTenderType().equals("GCARD") || tender.getTenderType().equals("VOUCH")) {
			if (tender.getTenderRefId() == null) {
				// If tender type='CHECK/GIFTCARD/VOUCHER' then tender_ref_id is required.
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.tenderRefernceReqid);
				return res;
			}
		} else if (tender.getTenderType().equals("CCARD") || tender.getTenderType().equals("DCARD")) {
			if (tender.getCcNo() == null) {
				// cc_no is required field in case of tender_type ='CCARD' or 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.creditCardNoReqId);
				return res;
			}
			if (tender.getCcAuthNo() == null) {
				// cc_auth_no is required field in case of tender_type ='CCARD' or 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.CreditCardAuthorizartionNo);
				return res;
			}
			if (tender.getCcAuthSrc() == null) {
				// cc_auth_src is required field in case of tender_type ='CCARD' or 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.creditCardAuthorizationSourceNo);
				return res;
			}
			if (tender.getCcCardholderVerf() == null) {
				// cc_cardholder_verf is required field in case of tender_type ='CCARD' or
				// 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.creditCardHolderRequired);
				return res;
			}
			if (tender.getCcEntryMode() == null) {
				// cc_entry_mode is required field in case of tender_type ='CCARD' or 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.CreditCardEntryReqId);
				return res;
			}
			if (tender.getCcExpDate() == null) {
				// cc_exp_date is required field in case of tender_type ='CCARD' or 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.CreditCardExpiryDateReqId);
				return res;
			}
			if (tender.getCcSpecCond() == null) {
				// cc_spec_cond is required field in case of tender_type ='CCARD' or 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.CreditCardSpecReqid);
				return res;
			}
			if (tender.getCcTermId() == null) {
				// cc_term_id is required field in case of tender_type ='CCARD' or 'DCARD'
				res.setMessageStatus(UtillConstantCodes.failedStatus);
				res.setMessageCode(UtillConstantCodes.failedCode);
				res.setMessageDesc(OmsErrorCodeConstants.CreditCardTermsReqId);
				return res;
			}
		}
		
		}
		return res;
	}
	
	
	
	
}
