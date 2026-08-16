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

import com.extra.einvoicing.dao.B2BInvoicingDAO;
import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.util.Partition;

/**
 * @author aibrahim
 *
 */
@Service
public class B2BInvoicingService {

	private static final Logger _LOG = LoggerFactory.getLogger(B2BInvoicingService.class);

	@Autowired
	@Qualifier("maxProcessCount")
	private Integer maxProcessCount;

	@Autowired
	private B2BInvoicingDAO b2bInvoicingDAO;

	@Autowired
	private B2BInvoicingProcess b2bInvoicingProcess;

	public void processEBSInvoice() {
		List<InvoiceInfo<String>> invoices = b2bInvoicingDAO.getEBSInvoices();
		Partition<InvoiceInfo<String>> chunkInvoices = new Partition<InvoiceInfo<String>>(invoices, maxProcessCount);
		List<Future<Void>> processStatus = new ArrayList<>();
		chunkInvoices.stream().forEach(partition -> {
			processStatus.add(b2bInvoicingProcess.processInvocies(partition, "EBS"));
		});
		processStatus.forEach(s -> {
			try {
				s.get();
			} catch (InterruptedException | ExecutionException e) {
				_LOG.error("Error while processing the xml for B2B EBS invoice", e);
			}
		});
	}

	public void processPOSInvoice() {
		List<InvoiceInfo<String>> invoices = b2bInvoicingDAO.getB2BInvoices();
		Partition<InvoiceInfo<String>> chunkInvoices = new Partition<InvoiceInfo<String>>(invoices, maxProcessCount);
		List<Future<Void>> processStatus = new ArrayList<>();
		chunkInvoices.stream().forEach(partition -> {
			processStatus.add(b2bInvoicingProcess.processInvocies(partition, "POS"));
		});
		processStatus.forEach(s -> {
			try {
				s.get();
			} catch (InterruptedException | ExecutionException e) {
				_LOG.error("Error while processing the xml for B2B POS invoice", e);
			}
		});
	}

	public void processResendEmail() {
		List<InvoiceInfo<String>> EBSinvoices = b2bInvoicingDAO.getEBSEmailDetails();
		processInvoices(EBSinvoices, "EBS");

		List<InvoiceInfo<String>> POSinvoices = b2bInvoicingDAO.getPOSEmailDetails();
		processInvoices(POSinvoices, "POS");
	}

	private void processInvoices(List<InvoiceInfo<String>> invoices, String type) {
		b2bInvoicingProcess.resendEmails(invoices, type);
	}

}
