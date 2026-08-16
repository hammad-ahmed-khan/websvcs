package com.extra.einvoicing.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import com.extra.einvoicing.config.ApplicationProperty;
import com.extra.einvoicing.model.Attachment;
import com.extra.einvoicing.model.Email;
import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.model.MessageInfo;
import com.extra.einvoicing.model.Recipient;
import com.extra.einvoicing.model.To;
import com.oracle.xmlns.oxp.service.publicreportservice.ArrayOfEMailDeliveryOption;
import com.oracle.xmlns.oxp.service.publicreportservice.ArrayOfParamNameValue;
import com.oracle.xmlns.oxp.service.publicreportservice.ArrayOfString;
import com.oracle.xmlns.oxp.service.publicreportservice.DeliveryChannels;
import com.oracle.xmlns.oxp.service.publicreportservice.DeliveryRequest;
import com.oracle.xmlns.oxp.service.publicreportservice.DeliveryService;
import com.oracle.xmlns.oxp.service.publicreportservice.EMailDeliveryOption;
import com.oracle.xmlns.oxp.service.publicreportservice.ParamNameValue;
import com.oracle.xmlns.oxp.service.publicreportservice.ReportRequest;
import com.oracle.xmlns.oxp.service.publicreportservice.RunReport;

/**
 * @author aibrahim
 *
 */
public class ReportUtil {

	private static final ApplicationProperty APPLICATION_PROPERTY;

	static {
		APPLICATION_PROPERTY = ApplicationSpringContext.getBean(ApplicationProperty.class);
	}

	public static <T> RunReport createRunReport(InvoiceInfo<T> invoice) {
		RunReport runReport = new RunReport();
		runReport.setUserID(APPLICATION_PROPERTY.getReportUserName());
		runReport.setPassword(APPLICATION_PROPERTY.getReportPassword());
		
		ReportRequest reportRequest = new ReportRequest();
		reportRequest.setReportAbsolutePath(APPLICATION_PROPERTY.getReportPath().get(invoice.getType()));
		reportRequest.setAttributeFormat("pdf");
		reportRequest.setSizeOfDataChunkDownload(Integer.MAX_VALUE);
		runReport.setReportRequest(reportRequest);
		return runReport;
	}

	public static void addParameter(RunReport runReport, String pName, String pValue) {
		ReportRequest request = runReport.getReportRequest();
		ArrayOfParamNameValue paramNameValues  = request.getParameterNameValues();
		if (paramNameValues == null) {
			paramNameValues = new ArrayOfParamNameValue();
			request.setParameterNameValues(paramNameValues);
		}
		ParamNameValue nameValue = new ParamNameValue();
		nameValue.setName(pName);
		ArrayOfString values = new ArrayOfString();
		nameValue.setValues(values);
		paramNameValues.getItem().add(nameValue);
		values.getItem().add(pValue);
	}

	public static DeliveryService createDeliveryService(String email, byte[] reportData) {
		DeliveryService deliveryService = new DeliveryService();
		deliveryService.setUserID(APPLICATION_PROPERTY.getReportUserName());
		deliveryService.setPassword(APPLICATION_PROPERTY.getReportPassword());

		DeliveryRequest deliveryRequest = new DeliveryRequest();
		deliveryRequest.setContentType("application/pdf");
		deliveryRequest.setDocumentData(reportData);

		DeliveryChannels channels = new DeliveryChannels();
		ArrayOfEMailDeliveryOption deliveryOption = new ArrayOfEMailDeliveryOption();
		channels.setEmailOptions(deliveryOption);
		EMailDeliveryOption emailOption = new EMailDeliveryOption();
		emailOption.setEmailBody(APPLICATION_PROPERTY.getReportEmailBody());
		emailOption.setEmailFrom(APPLICATION_PROPERTY.getReportEmailFrom());
		emailOption.setEmailServerName(APPLICATION_PROPERTY.getReportEmailServerName());
		emailOption.setEmailTo(email);
		emailOption.setEmailSubject(APPLICATION_PROPERTY.getReportEmailSubject());
		deliveryOption.getItem().add(emailOption);
		deliveryRequest.setDeliveryChannels(channels);

		deliveryService.setDeliveryRequest(deliveryRequest);
		return deliveryService;
	}

	public static MessageInfo createEmailMessage(String fileName, String customerEmail, byte[] byteArray, String type) {
		MessageInfo message = new MessageInfo();
		Email email = new Email();
		Recipient recipient = new Recipient();
		
		email.setFrom(APPLICATION_PROPERTY.getReportEmailFrom());
		email.setFromName(APPLICATION_PROPERTY.getReportEmailServerName());
		email.setText(APPLICATION_PROPERTY.getReportEmailBody());
		email.setSubject(String.format(APPLICATION_PROPERTY.getReportEmailSubject(), fileName));

		recipient.setTo(new ArrayList<To>());
		for (String receipnt: customerEmail.split(",")) {
			recipient.getTo().add(new To(receipnt));
		}

		recipient.setCc(new ArrayList<To>());
		String cc = APPLICATION_PROPERTY.getReportEmailCC().get(type);
		if (cc != null && !cc.isBlank()) {
			for (String c: cc.split(",")) {
				recipient.getCc().add(new To(c));
			}
		}
		
		email.setRecipients(recipient);
		
		Attachment attachment = new Attachment();
		attachment.setContentBody(Base64.getEncoder().encodeToString(byteArray));
		attachment.setContentType("application/pdf");
		attachment.setName(fileName);
		email.setAttachments(Collections.singletonList(attachment));
		
		message.setEmail(email);
		return message;
	}

	public static String getConfigToEmail(String type) {
		return APPLICATION_PROPERTY.getReportEmailTo().get(type);
	}
}
