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
public class B2CXmlGenerationService {

	private static final Logger _LOG = LoggerFactory.getLogger(B2CXmlGenerationService.class);

	@Autowired
	@Qualifier("maxProcessCount")
	private Integer maxProcessCount;

	@Autowired
	private B2CInvoicingDAO b2cInvoicingDAO;

	@Autowired
	private B2CInvoicingProcess b2cInvoicingProcess;

	public void generateB2CXml() {
		List<InvoiceInfo<Long>> invoices = b2cInvoicingDAO.getCancelReturnDetails();
		Partition<InvoiceInfo<Long>> chunkInvoices = new Partition<InvoiceInfo<Long>>(invoices, maxProcessCount);
		List<Future<Void>> processStatus = new ArrayList<>();
		chunkInvoices.stream().forEach(partition -> {
			processStatus.add(b2cInvoicingProcess.generateXML(partition, "OMS"));
		});
		processStatus.forEach(s -> {
			try {
				s.get();
			} catch (InterruptedException | ExecutionException e) {
				_LOG.error("Error while generating the xml for B2C cancellation and return", e);
			}
		});
	}

	public void processSiebelInvoice() {
		List<InvoiceInfo<String>> invoices = b2cInvoicingDAO.getSiebelInvoices();
		Partition<InvoiceInfo<String>> chunkInvoices = new Partition<InvoiceInfo<String>>(invoices, maxProcessCount);
		List<Future<Void>> processStatus = new ArrayList<>();
		chunkInvoices.stream().forEach(partition -> {
			processStatus.add(b2cInvoicingProcess.generateXML(partition, "SEIBEL"));
		});
		processStatus.forEach(s -> {
			try {
				s.get();
			} catch (InterruptedException | ExecutionException e) {
				_LOG.error("Error while processing the xml for B2B POS invoice", e);
			}
		});
	}
}
