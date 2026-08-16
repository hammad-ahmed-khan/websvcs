package com.extra.einvoicing.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
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
import com.extra.einvoicing.util.InvoiceUtil;
import com.extra.einvoicing.util.ReportUtil;
import com.oracle.xmlns.oxp.service.publicreportservice.RunReport;
import com.oracle.xmlns.oxp.service.publicreportservice.RunReportResponse;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

public class B2BEmailSender<T> implements Callable<InvoiceInfo<T>> {

	private static final Logger _LOG = LoggerFactory.getLogger(B2BInvoiceProcessor.class);

	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	private DateFormat timeFormat = new SimpleDateFormat("HHmmss");

	private InvoiceInfo<T> invoice;

	private ResourceBean resourceBean;

	private String type;

	public B2BEmailSender(InvoiceInfo<T> invoice, ResourceBean resourceBean, String type) {
		this.invoice = invoice;
		this.resourceBean = resourceBean;
		this.type = type;
	}

	@Override
	public InvoiceInfo<T> call() throws Exception {

		_LOG.info("Sending email to the invoice number - " + invoice.getInvoiceIdentifier());
		RunReportResponse repResponse = null;
		InvoiceType invoiceType = null;
		String emailStatus = "F";
		try {

			byte[] xmlAttachByte = invoice.getClearedXml().getBytes(StandardCharsets.UTF_8);
			invoiceType = InvoiceUtil.deserializeToType(xmlAttachByte, InvoiceType.class);

			RunReport runReport = ReportUtil.createRunReport(invoice);
			ReportUtil.addParameter(runReport, "P_INVOICE_ID", invoice.getInvoiceId().toString());
			String qrCode = Base64.getEncoder().encodeToString(invoiceType.getAdditionalDocumentReference().get(2)
					.getAttachment().getEmbeddedDocumentBinaryObject().getValue());
			ReportUtil.addParameter(runReport, "P_QR_CODE", qrCode);
			repResponse = resourceBean.getBipReportService().runReport(runReport);
			PDDocument document = PDDocument
					.load(new ByteArrayInputStream(repResponse.getRunReportReturn().getReportBytes()));
			
			PDEmbeddedFile embeddedFile = new PDEmbeddedFile(document, new ByteArrayInputStream(xmlAttachByte));
			embeddedFile.setSubtype("application/pdf");
			embeddedFile.setSize(xmlAttachByte.length);

			String attachmentXMLName = invoiceType.getAccountingSupplierParty().getParty().getPartyTaxScheme().get(0)
					.getCompanyID().getValue() + "_"
					+ dateFormat.format(invoiceType.getIssueDate().getValue().toGregorianCalendar().getTime())
					+ timeFormat.format(invoiceType.getIssueTime().getValue().toGregorianCalendar().getTime()) + "_"
					+ invoiceType.getID().getValue() + ".xml";
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
				_LOG.warn("Email ID is missing for the invoice id {} of type {}", invoice.getInvoiceIdentifier(),
						invoice.getType());
			} else {
				MessageInfo emailMessage = ReportUtil.createEmailMessage(invoiceType.getID().getValue(),
						invoice.getCustomerEmail(), content, invoice.getType());
				resourceBean.getEmailService().sendEmail(emailMessage);
			}
			String configToEmail = ReportUtil.getConfigToEmail(invoice.getType());
			if (configToEmail != null && !configToEmail.isBlank()) {
				resourceBean.getEmailService().sendEmail(ReportUtil.createEmailMessage(invoiceType.getID().getValue(),
						configToEmail, content, invoice.getType()));
			}
			emailStatus = "S";

		} catch (Exception e) {
			_LOG.error("Email sent failed for invoice number " + invoice.getCustomerEmail(), e);
			emailStatus = "F";
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
