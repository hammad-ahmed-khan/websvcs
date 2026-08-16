package com.extra.oms.einvoice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.controller.BaseController;
import com.extra.oms.einvoice.model.InvoiceInfo;
import com.extra.oms.einvoice.service.EInvoicingService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/b2bpos")
public class B2BPOSController extends BaseController {

	@Autowired
	private EInvoicingService eInvoicingService;

	@GetMapping
	public List<InvoiceInfo> getInvoices(@RequestParam Map<String, String> params) throws BaseException {
		return eInvoicingService.getB2BPOSInvoices(params);
	}
}
