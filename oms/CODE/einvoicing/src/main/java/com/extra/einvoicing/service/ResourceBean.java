package com.extra.einvoicing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.extra.einvoicing.config.ApplicationProperty;
import com.extra.einvoicing.dao.B2BInvoicingDAO;
import com.extra.einvoicing.service.client.IBIPReportService;
import com.extra.einvoicing.service.client.IEmailService;
import com.extra.einvoicing.service.client.IZatcaInterfaceAPI;
import com.zatca.sdk.service.flow.ValidationProcessor;

/**
 * 
 */
@Component
public class ResourceBean {

	@Autowired
	@Value("${zatca.xml.validate}")
	private Boolean validateXML;

	@Autowired
	private ValidationProcessor validationProcessor;

	@Autowired
	private ApplicationProperty applicationProperty;

	@Autowired
	private IZatcaInterfaceAPI zatcaInterfaceAPI;

	@Autowired
	private IBIPReportService bipReportService;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private HashingService hashingService;

	@Autowired
	private B2BInvoicingDAO b2bInvoicingDAO;

	public ValidationProcessor getValidationProcessor() {
		return validationProcessor;
	}

	public ApplicationProperty getApplicationProperty() {
		return applicationProperty;
	}

	public Boolean getValidateXML() {
		return validateXML;
	}

	public IZatcaInterfaceAPI getZatcaInterfaceAPI() {
		return zatcaInterfaceAPI;
	}

	public IBIPReportService getBipReportService() {
		return bipReportService;
	}

	public IEmailService getEmailService() {
		return emailService;
	}

	public HashingService getHashingService() {
		return hashingService;
	}

	public B2BInvoicingDAO getB2bInvoicingDAO() {
		return b2bInvoicingDAO;
	}
}
