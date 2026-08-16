package com.extra.einvoice.bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

import com.extra.einvoice.util.InvoiceUtil;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

/**
 * @author abubakkarSiddique
 *
 */
public class ZatcaResponse {

	private ValidationResult validationResults;

	private String clearedInvoice;

	private String clearanceStatus;

	private String reportingStatus;

	private String clearedXML;

	private InvoiceType invoiceType;

	public ValidationResult getValidationResults() {
		return validationResults;
	}

	public void setValidationResults(ValidationResult validationResults) {
		this.validationResults = validationResults;
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

	public String getClearanceStatus() {
		return clearanceStatus;
	}

	public void setClearanceStatus(String clearanceStatus) {
		this.clearanceStatus = clearanceStatus;
	}

	public String getReportingStatus() {
		return reportingStatus;
	}

	public void setReportingStatus(String reportingStatus) {
		this.reportingStatus = reportingStatus;
	}

	public String getClearedXML() {
		return clearedXML;
	}

	public void setClearedXML(String clearedXML) {
		this.clearedXML = clearedXML;
	}

	public InvoiceType getInvoiceType() {
		return invoiceType;
	}
}
