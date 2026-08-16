package com.extra.jood.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.jood.bean.CbHistoryRequest;
import com.extra.jood.bean.CbHistoryResponse;
import com.extra.jood.bean.MemberShipCbInfo;
import com.extra.jood.bean.MembershipCbResponse;
import com.extra.jood.bean.MembershipInfo;
import com.extra.jood.bean.MembershipResponse;
import com.extra.jood.bean.TransactionInfo;
import com.extra.jood.dao.JOODDAO;

/**
 * @author aibrahim
 *
 */
@Service
public class JOODService {

	@Autowired
	private JOODDAO joodDao;

	public Map<String, Object> saveTransactionDetail(TransactionInfo transaction) throws Exception {
		return joodDao.saveTransactionDetail(transaction);
	}

	public MembershipResponse validateEligibility(MembershipInfo membershipInfo) throws Exception {
		return joodDao.checkEligibility(membershipInfo);
	}
	
	public MembershipCbResponse getTransactionCbStatus(MemberShipCbInfo request) throws Exception {
		return joodDao.getTransactionCbStatus(request);
	}
	
	public CbHistoryResponse getCbHistoryTransaction(CbHistoryRequest request) throws Exception {
		return joodDao.getCbHistoryTransaction(request);
	}
}
