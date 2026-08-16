package com.extra.einvoicing.service;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.extra.einvoicing.model.InvoiceInfo;

/**
 * @author aibrahim
 *
 */
@Service
public class B2BInvoicingProcess {

	private static final Logger _LOG = LoggerFactory.getLogger(B2BInvoicingProcess.class);

	@Autowired
	private ResourceBean resourceBean;

	@Autowired
	private ThreadPoolExecutor invoiceExecutor;

	@Async("asyncTaskExecutor")
	public Future<Void> processInvocies(List<InvoiceInfo<String>> invoices, String type) {
		
		invoices.stream().map(i -> invoiceExecutor.submit(new B2BInvoiceProcessor<>(i, resourceBean, type))).forEach(c -> {
			try {
				c.get();
			} catch (Exception e) {
				_LOG.error("", e);
			}
		});;
		return new AsyncResult<>(null);
	}

	public Future<Void> resendEmails(List<InvoiceInfo<String>> invoices, String type) {

		invoices.stream().map(i -> invoiceExecutor.submit(new B2BEmailSender<>(i, resourceBean, type)))
			.forEach(c -> {
				try {
					c.get();
				} catch (Exception e) {
					_LOG.error("", e);
				}
			});
		return new AsyncResult<>(null);
	}
}
