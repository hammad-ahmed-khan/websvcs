package com.extra.einvoice.bean;

/**
 * @author 
 *
 */
public class ZatcaRequest {

	private String invoiceHash;

	private String uuid;

	private String invoice;

	public String getInvoiceHash() {
		return invoiceHash;
	}

	public void setInvoiceHash(String invoiceHash) {
		this.invoiceHash = invoiceHash;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
}
