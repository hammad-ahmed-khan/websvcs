package com.extra.einvoicing.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.einvoicing.dao.B2BInvoicingDAO;
import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.service.client.B2CSigningService;
import com.extra.einvoicing.util.InvoiceUtil;
import com.gazt.einvoicing.hashing.generation.service.HashingGenerationService;
import com.gazt.einvoicing.signing.service.model.InvoiceSigningResult;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;

/**
 * 
 */
@Service
public class HashingService {

	private static final String PIH = "NWZlY2ViNjZmZmM4NmYzOGQ5NTI3ODZjNmQ2OTZjNzljMmRiYzIzOWRkNGU5MWI0NjcyOWQ3M2EyN2ZiNTdlOQ==";

	private String pihValue = null;

	@Autowired
	private B2BInvoicingDAO b2bInvoicingDAO;

	@Autowired
	private HashingGenerationService hashingGenerationService;

	@Autowired
	private B2CSigningService b2cSigningService;

	public synchronized <T> String getInvoiceHash(InvoiceInfo<T> invoiceInfo) throws Exception {
		updatePIHAndICV(invoiceInfo);
		String hash = hashingGenerationService.getInvoiceHash(InvoiceUtil.serializeToXml(invoiceInfo.getInvoiceType()));
		pihValue = hash;
		invoiceInfo.setHash(pihValue);
		return hash;
	}

	public synchronized <T> InvoiceSigningResult signB2CXml(InvoiceInfo<T> invoiceInfo) throws Exception {
		updatePIHAndICV(invoiceInfo);
		InvoiceSigningResult result = b2cSigningService.signXml(InvoiceUtil.serializeToXml(invoiceInfo.getInvoiceType()));
		pihValue = result.getInvoiceHash();
		invoiceInfo.setHash(pihValue);
		return result;
	}

	private <T> void updatePIHAndICV(InvoiceInfo<T> invoiceInfo) {
		Long icv = b2bInvoicingDAO.getICV();
		if (pihValue == null) {
			pihValue = b2bInvoicingDAO.getPIH();
		}
		if (pihValue == null) {
			pihValue = PIH;
		}
		for (DocumentReferenceType referenceType : invoiceInfo.getInvoiceType().getAdditionalDocumentReference()) {
			if ("PIH".equals(referenceType.getID().getValue())) {
				byte[] bytes = Base64.getDecoder().decode(pihValue);
				referenceType.getAttachment().getEmbeddedDocumentBinaryObject().setValue(bytes);
			} else if ("ICV".equals(referenceType.getID().getValue())) {
				referenceType.getUUID().setValue(icv.toString());
			}
		}
		invoiceInfo.setIcv(icv);
	}
}
