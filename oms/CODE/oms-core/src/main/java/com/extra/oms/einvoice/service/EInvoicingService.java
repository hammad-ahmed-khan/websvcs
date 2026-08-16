package com.extra.oms.einvoice.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.einvoice.dao.EInvoicingDAO;
import com.extra.oms.einvoice.model.InvoiceInfo;

@Service
public class EInvoicingService {

	@Autowired
	private EInvoicingDAO eInvoicingDAO;

	public List<InvoiceInfo> getInvoices(Map<String, String> searchParams) {
		return eInvoicingDAO.getInvoices(searchParams);
	}

	public String getInvoiceXML(String invoiceNumber) {
		return eInvoicingDAO.getInvoiceXML(invoiceNumber);
	}

	public String getError(String invoiceNumber) {
		return eInvoicingDAO.getError(invoiceNumber);
	}

	public List<InvoiceInfo> getEBSInvoices(Map<String, String> params) {
		return eInvoicingDAO.getEBSInvoices(params);
	}

	public List<InvoiceInfo> getB2BPOSInvoices(Map<String, String> params) {
		return eInvoicingDAO.getB2BPOSInvoices(params);
	}
}
