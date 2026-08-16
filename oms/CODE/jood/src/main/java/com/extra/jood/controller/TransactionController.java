package com.extra.jood.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.jood.bean.ResponseInfo;
import com.extra.jood.bean.TransactionInfo;
import com.extra.jood.service.JOODService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

	private static final Logger LOG = Logger.getLogger(TransactionController.class);

	@Autowired
	private JOODService joodService;

	@PostMapping()
	public ResponseInfo saveTransactionDetail(@RequestBody TransactionInfo transaction) {
		ResponseInfo response = new ResponseInfo();
		try {
			LOG.info("JOOD Transaction: start saving transction detail process");
			Map<String, Object> resultMap = joodService.saveTransactionDetail(transaction);
			response.setJoodTranId(resultMap.get("joodTranId").toString());
			response.setJoodTranSeqNo(resultMap.get("joodTranSeqNo").toString());
			response.setStatus('S');
			response.setResCode("ENTRY_CREATED");
			response.setMessage("The Transaction has been posted successfully");
			LOG.info("JOOD Transaction: saving transction detail process completed");
		} catch (Exception e) {
			LOG.error("JOOD Transaction: error while saving transction detail process", e);
			response.setResCode("ENTRY_FAILED");
			response.setMessage(e.getMessage());
			response.setStatus('F');
		}
		return response;
	}
}
