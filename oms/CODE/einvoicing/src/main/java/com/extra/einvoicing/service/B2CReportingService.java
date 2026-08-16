package com.extra.einvoicing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.extra.einvoicing.dao.B2CInvoicingDAO;
import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.util.Partition;

/**
 * @author aibrahim
 *
 */
@Service
public class B2CReportingService {

	private static final Logger _LOG = LoggerFactory.getLogger(B2CReportingService.class);

	@Autowired
	private B2CInvoicingDAO b2cInvoicingDAO;

	@Autowired
	private B2CInvoicingProcess b2cInvoicingProcess;

	@Autowired
	@Qualifier("maxProcessCount")
	private Integer maxProcessCount;
	
	public void reportInvoice() {
		List<InvoiceInfo<String>> signedInvoices = b2cInvoicingDAO.getSignedInvoices();
		Partition<InvoiceInfo<String>> chunkInvoices = new Partition<InvoiceInfo<String>>(signedInvoices, maxProcessCount);
		List<Future<Void>> processStatus = new ArrayList<>();
		chunkInvoices.stream().forEach(partition -> {
			processStatus.add(b2cInvoicingProcess.reportXML(partition));
		});
		processStatus.forEach(s -> {
			try {
				s.get();
			} catch (InterruptedException | ExecutionException e) {
				_LOG.error("Error while reporting the xml to GAZAT", e);
			}
		});
	}
}
