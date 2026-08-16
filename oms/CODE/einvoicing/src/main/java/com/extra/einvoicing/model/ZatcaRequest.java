package com.extra.einvoicing.model;

/**
 * @author aibrahim
 *
 */
public class ZatcaRequest {

	private String invoiceHash;

	private String uuid;

	private String invoice;

	public String getInvoiceHash() {
		return invoiceHash;
	}

	public String getUuid() {
		return uuid;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoiceHash(String invoiceHash) {
		this.invoiceHash = invoiceHash;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
}
