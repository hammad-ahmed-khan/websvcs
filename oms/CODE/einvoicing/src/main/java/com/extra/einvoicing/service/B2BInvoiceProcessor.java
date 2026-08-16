package com.extra.einvoicing.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.model.MessageInfo;
import com.extra.einvoicing.model.ZatcaRequest;
import com.extra.einvoicing.model.ZatcaResponse;
import com.extra.einvoicing.util.InvoiceUtil;
import com.extra.einvoicing.util.ReportUtil;
import com.oracle.xmlns.oxp.service.publicreportservice.RunReport;
import com.oracle.xmlns.oxp.service.publicreportservice.RunReportResponse;
import com.zatca.sdk.service.validation.Result;

import feign.FeignException;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

/**
 * 
 */
public class B2BInvoiceProcessor<T> implements Callable<InvoiceInfo<T>> {

	private static final Logger _LOG = LoggerFactory.getLogger(B2BInvoiceProcessor.class);

	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	private DateFormat timeFormat = new SimpleDateFormat("HHmmss");

	private InvoiceInfo<T> invoice;

	private ResourceBean resourceBean;

	private String type;

	public B2BInvoiceProcessor(InvoiceInfo<T> invoice, ResourceBean resourceBean, String type) {
		this.invoice = invoice;
		this.resourceBean = resourceBean;
		this.type = type;
	}

	@Override
	public InvoiceInfo<T> call() throws Exception {
		Result validateRes = null;
		File xmlFile = null;
		String emailStatus = "F";
		ZatcaResponse response = null;
		RunReportResponse repResponse = null;
		try {
			ZatcaRequest request = new ZatcaRequest();
			request.setInvoiceHash(resourceBean.getHashingService().getInvoiceHash(invoice));
			String xml = InvoiceUtil.serializeToXml(invoice.getInvoiceType());
			if (resourceBean.getValidateXML()) {
				xmlFile = Files.write(Files.createTempFile(invoice.getInvoiceIdentifier().toString(), ".xml"), xml.getBytes(StandardCharsets.UTF_8)).toFile();
				validateRes = resourceBean.getValidationProcessor().run(xml);
			}
			if (_LOG.isDebugEnabled()) {
				_LOG.debug("XML for " + invoice.getInvoiceIdentifier() + " is " + xml);
			}
			if (!resourceBean.getValidateXML() || validateRes.isValid()) {
				request.setUuid(invoice.getInvoiceType().getUUID().getValue());
				request.setInvoice(Base64.getEncoder().encodeToString(xml.getBytes()));

				response = resourceBean.getZatcaInterfaceAPI().clearanceApi(request, 1);
			} else {
				invoice.setErrorMessage(validateRes.toString());
				invoice.setClearanceStatus('F');
			}
			try {
				if (xmlFile != null) {
					Files.deleteIfExists(xmlFile.toPath());
				}
			} catch (Exception e) {
				// EAT Exception
			}
			if (response != null && response.getClearanceStatus().equals("CLEARED")) {
				invoice.setClearanceStatus('Y');
				invoice.setClearedXml(response.getClearedXML());
			} else {
				invoice.setClearanceStatus('F');
			}
			invoice.setErrorMessage(response.getValidationResults().toString());
		} catch (FeignException fe) {
			_LOG.error("Error while calling the zatca interface for clearing the invoice " + invoice.getInvoiceIdentifier(), fe);
			invoice.setErrorMessage(fe.contentUTF8());
			invoice.setClearedXml(new String(fe.request().body()));
			invoice.setClearanceStatus('F');
		} catch (Exception e) {
			_LOG.error("Error while generating the xml for clearing the invoice " + invoice.getInvoiceIdentifier(), e);
			invoice.setErrorMessage(e.getMessage());
			invoice.setClearanceStatus('F');
		}
		_LOG.debug("Updaing status to database");
		resourceBean.getB2bInvoicingDAO().saveClearedInvoice(invoice, type);
		if (type.equals("EBS")) {			
			resourceBean.getB2bInvoicingDAO().updateInvoiceStatus(invoice);
		} else {
			resourceBean.getB2bInvoicingDAO().updatePOSInvoice(invoice);
		}
		try {
			if (invoice.getClearanceStatus() != null && invoice.getClearanceStatus().equals('Y')) {
				RunReport runReport = ReportUtil.createRunReport(invoice);
				ReportUtil.addParameter(runReport, "P_INVOICE_ID", invoice.getInvoiceId().toString());
				String qrCode = Base64.getEncoder().encodeToString(response.getInvoiceType().getAdditionalDocumentReference().get(2).getAttachment().getEmbeddedDocumentBinaryObject().getValue());
				ReportUtil.addParameter(runReport, "P_QR_CODE", qrCode);
				repResponse = resourceBean.getBipReportService().runReport(runReport);
				PDDocument document = PDDocument.load(new ByteArrayInputStream(repResponse.getRunReportReturn().getReportBytes()));
				byte[] xmlAttachByte = Base64.getDecoder().decode(response.getClearedInvoice());
				PDEmbeddedFile embeddedFile = new PDEmbeddedFile(document, new ByteArrayInputStream(xmlAttachByte));
				embeddedFile.setSubtype("application/pdf");
				embeddedFile.setSize(xmlAttachByte.length);
	
				InvoiceType invoiceType = invoice.getInvoiceType();
				String attachmentXMLName = invoiceType.getAccountingSupplierParty().getParty().getPartyTaxScheme().get(0).getCompanyID().getValue() + "_"
						+ dateFormat.format(invoiceType.getIssueDate().getValue().toGregorianCalendar().getTime())
						+ timeFormat.format(invoiceType.getIssueTime().getValue().toGregorianCalendar().getTime()) + "_" + invoiceType.getID().getValue() + ".xml";
				PDComplexFileSpecification fileSpecification = new PDComplexFileSpecification();
				fileSpecification.setFile(attachmentXMLName);
				fileSpecification.setEmbeddedFile(embeddedFile);
	
				Map<String, PDComplexFileSpecification> embeddedFileMap = new HashMap<String, PDComplexFileSpecification>();
				embeddedFileMap.put(attachmentXMLName, fileSpecification);
	
				PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();
				efTree.setNames(embeddedFileMap);
	
				final PDDocumentNameDictionary names = new PDDocumentNameDictionary(document.getDocumentCatalog());
				names.setEmbeddedFiles(efTree);
				document.getDocumentCatalog().setNames(names);
	
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				document.save(baos);
				document.close();
	
				byte[] content = baos.toByteArray();
				if (invoice.getCustomerEmail() == null || invoice.getCustomerEmail().isBlank()) {
					_LOG.warn("Email ID is missing for the invoice id {} of type {}", invoice.getInvoiceIdentifier(), invoice.getType());
				} else {
					MessageInfo emailMessage = ReportUtil.createEmailMessage(invoiceType.getID().getValue(), invoice.getCustomerEmail(), content, invoice.getType());
					resourceBean.getEmailService().sendEmail(emailMessage);
				}
				String configToEmail = ReportUtil.getConfigToEmail(invoice.getType());
				if (configToEmail != null && !configToEmail.isBlank()) {
					resourceBean.getEmailService().sendEmail(ReportUtil.createEmailMessage(invoiceType.getID().getValue(), configToEmail, content, invoice.getType()));
				}
				emailStatus = "S";
			}
		} catch (Exception e) {
			_LOG.error("Error while sending mail of the invoice number " + invoice.getInvoiceIdentifier(), e);
		} finally {
			if (type.equals("EBS")) {
				resourceBean.getB2bInvoicingDAO().updateEBSEmailStatus(invoice, emailStatus);
			} else {
				resourceBean.getB2bInvoicingDAO().updatePOSEmailStatus(invoice, emailStatus);
			}
		}
		return invoice;
	}
}
