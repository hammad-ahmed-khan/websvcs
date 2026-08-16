package com.extra.oms.einvoice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.controller.BaseController;
import com.extra.oms.einvoice.model.InvoiceInfo;
import com.extra.oms.einvoice.service.EInvoicingService;

@RestController
@RequestMapping(path = "/einvoice")
public class ReportController extends BaseController {

	@Autowired
	private EInvoicingService eInvoicingService;

	@GetMapping
	public List<InvoiceInfo> getInvoices(@RequestParam Map<String, String> params) throws BaseException {
		return eInvoicingService.getInvoices(params);
	}

	@GetMapping(path = "/xml/{invoiceNumber}")
	public String getInvoiceXML(@PathVariable("invoiceNumber") String invoiceNumber) throws BaseException {
		return eInvoicingService.getInvoiceXML(invoiceNumber);
	}

	@GetMapping(path = "/error/{invoiceNumber}")
	public String getError(@PathVariable("invoiceNumber") String invoiceNumber) throws BaseException {
		return eInvoicingService.getError(invoiceNumber);
	}
}
