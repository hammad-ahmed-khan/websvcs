package com.extra.einvoicing.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

import com.extra.einvoicing.util.InvoiceUtil;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

/**
 * @author aibrahim
 *
 */
public class ZatcaResponse {

	private ValidationResult validationResults;

	private String reportingStatus;

	private String clearanceStatus;

	private String clearedInvoice;

	private String clearedXML;

	private InvoiceType invoiceType;
	
	public ValidationResult getValidationResults() {
		return validationResults;
	}

	public String getReportingStatus() {
		return reportingStatus;
	}

	public String getClearanceStatus() {
		return clearanceStatus;
	}

	public void setValidationResults(ValidationResult validationResults) {
		this.validationResults = validationResults;
	}

	public void setReportingStatus(String reportingStatus) {
		this.reportingStatus = reportingStatus;
	}

	public void setClearanceStatus(String clearanceStatus) {
		this.clearanceStatus = clearanceStatus;
	}

	public String getClearedInvoice() {
		return clearedInvoice;
	}

	public void setClearedInvoice(String clearedInvoice) throws Exception {
		this.clearedInvoice = clearedInvoice;
		if (StringUtils.isNotBlank(clearedInvoice)) {
			byte[] arr = Base64.getDecoder().decode(clearedInvoice);
			clearedXML = new String(arr, StandardCharsets.UTF_8);
			invoiceType = InvoiceUtil.deserializeToType(arr, InvoiceType.class);
		}
	}

	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	public String getClearedXML() {
		return clearedXML;
	}
}
