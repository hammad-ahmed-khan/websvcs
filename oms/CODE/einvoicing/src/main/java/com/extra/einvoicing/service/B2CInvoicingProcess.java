package com.extra.einvoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.extra.einvoicing.dao.B2CInvoicingDAO;
import com.extra.einvoicing.model.EReconDetail;
import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.model.ReconHead;
import com.extra.einvoicing.model.ZatcaRequest;
import com.extra.einvoicing.model.ZatcaResponse;
import com.extra.einvoicing.service.client.IZatcaInterfaceAPI;
import com.extra.einvoicing.util.InvoiceUtil;
import com.extra.einvoicing.util.SoftException;
import com.gazt.einvoicing.signing.service.model.InvoiceSigningResult;

import feign.FeignException;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

/**
 * @author aibrahim
 *
 */
@Service
public class B2CInvoicingProcess {

	private static final Logger _LOG = LoggerFactory.getLogger(B2BInvoicingProcess.class);

	@Autowired
	private HashingService hashingService;

	@Autowired
	private B2CInvoicingDAO b2cInvoicingDAO;

	@Autowired
	private IZatcaInterfaceAPI zatcaInterfaceAPI;

	@Async("asyncTaskExecutor")
	public <T> Future<Void> generateXML(List<InvoiceInfo<T>> invoices, String app) {
		List<InvoiceInfo<T>> processedInvs = new ArrayList<>();
		List<InvoiceInfo<T>> xmlInvoices = new ArrayList<>();
		invoices.stream().forEach(inv -> {
			try {
				InvoiceSigningResult result = hashingService.signB2CXml(inv);
				if (_LOG.isDebugEnabled()) {
					_LOG.debug("Generated XML for " + inv.getInvoiceIdentifier() + " is  " + result.getSingedXML());
				}
				inv.setClearedXml(result.getSingedXML());
				xmlInvoices.add(inv);
				inv.setClearanceStatus('Y');
			} catch (Exception e) {
				_LOG.error("Error while generating the xml for cancellation / return " + inv.getInvoiceIdentifier(), e);
				inv.setErrorMessage(e.getMessage());
				inv.setClearanceStatus('F');
			} finally {
				processedInvs.add(inv);
			}
		});
		if ("OMS".equals(app)) {
			b2cInvoicingDAO.updateOMSXMLGeneration(processedInvs, xmlInvoices);
		} else if ("SEIBEL".equals(app)) {
			b2cInvoicingDAO.updateSiebelXML(processedInvs, xmlInvoices);
		}
		return new AsyncResult<>(null);
	}

	@Async("asyncTaskExecutor")
	public Future<Void> reportXML(List<InvoiceInfo<String>> invoices) {
		Map<String, FutureTask<ZatcaResponse>> reportingMap = new HashMap<>();
		List<InvoiceInfo<String>> processedInvs = new ArrayList<>();
		ThreadPoolExecutor b2cReportProcessor = new ThreadPoolExecutor(10, 10, 100L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(5000));
		invoices.stream().forEach(inv -> {
			try {
				if (StringUtils.isBlank(inv.getClearedXml())) {
					_LOG.error("Error: XML is blank for " + inv.getInvoiceIdentifier());
					inv.setErrorMessage("XML is blank");
					inv.setClearanceStatus('F');
					processedInvs.add(inv);
					return;
				}
				try {
					inv.setInvoiceType(InvoiceUtil.deserializeToType(inv.getClearedXml().getBytes(StandardCharsets.UTF_8), InvoiceType.class));
				} catch (Exception e) {
					_LOG.error("Error while parsing the xml for validation of invoice + " + inv.getInvoiceIdentifier(), e);
					inv.setErrorMessage("Error while parsing XML for validation");
					inv.setClearanceStatus('F');
					processedInvs.add(inv);
					return;
				}
				try {
					if (!"N".equals(inv.getValidateXML())) {
						validateInvoice(inv);
					}
				} catch (Exception e) {
					_LOG.error("Error while validating the xml for validation of invoice + " + inv.getInvoiceIdentifier(), e);
					inv.setErrorMessage("Error while validating XML -> " + e.getMessage());
					inv.setClearanceStatus('F');
					processedInvs.add(inv);
					return;
				}
				ZatcaRequest request = new ZatcaRequest();
				byte[] xmlByte = inv.getClearedXml().getBytes(StandardCharsets.UTF_8);
				InvoiceType invoiceType = InvoiceUtil.deserializeToType(xmlByte, InvoiceType.class);
				request.setUuid(invoiceType.getUUID().getValue());
				request.setInvoiceHash(InvoiceUtil.getHashFromXml(invoiceType));
				request.setInvoice(Base64.getEncoder().encodeToString(xmlByte));

				FutureTask<ZatcaResponse> fTask = new FutureTask<>(() -> zatcaInterfaceAPI.reportingApi(request, 0));
				reportingMap.put(inv.getInvoiceIdentifier(), fTask);
				b2cReportProcessor.submit(fTask);
			} catch (Exception e) {
				_LOG.error("Error while reprting xml to Gazat " + inv.getInvoiceIdentifier(), e);
				inv.setErrorMessage(e.getMessage());
				inv.setClearanceStatus('F');
				processedInvs.add(inv);
			}
		});
		b2cReportProcessor.setCorePoolSize(0);
		invoices.stream().filter(i -> !((Character)'F').equals(i.getClearanceStatus())).forEach(inv -> {
			ZatcaResponse response = null;
			try {
				response = reportingMap.get(inv.getInvoiceIdentifier()).get();
			} catch (InterruptedException | ExecutionException e) {
				_LOG.error("Error while calling the zatca api for reprting the invoice " + inv.getInvoiceIdentifier(), e);
				if(e.getCause() instanceof FeignException) {
					_LOG.error(((FeignException)e.getCause()).request().toString());
					inv.setErrorMessage(((FeignException)e.getCause()).contentUTF8());
				} else {
					inv.setErrorMessage(e.getMessage());
				}
				inv.setClearanceStatus('F');
				processedInvs.add(inv);
				return;
			} catch (Exception e) {
				_LOG.error("Error while calling the zatca api for reprting the invoice " + inv.getInvoiceIdentifier(), e);
				inv.setErrorMessage(e.getMessage());
				inv.setClearanceStatus('F');
				processedInvs.add(inv);
				return;
			}
			if ("REPORTED".equals(response.getReportingStatus())) {
				inv.setClearanceStatus('Y');
				processedInvs.add(inv);
			} else {
				inv.setClearanceStatus('F');
				processedInvs.add(inv);
			}
			inv.setErrorMessage(response.getValidationResults().toString());
		});
		b2cInvoicingDAO.updateXMLReporting(processedInvs);
		persistInvForRecon(processedInvs);
		return new AsyncResult<>(null);
	}

	private void validateInvoice(InvoiceInfo<String> inv) throws Exception {
		Set<String> items = new HashSet<>();
		for (InvoiceLineType lineItem : inv.getInvoiceType().getInvoiceLine()) {
			items.add(lineItem.getItem().getSellersItemIdentification().getID().getValue());
		}
		Map<String, BigDecimal> itemVatMap = b2cInvoicingDAO.getItemVatRates(items);
		BigDecimal totalVatAmt = BigDecimal.ZERO;
		BigDecimal taxableAmt = BigDecimal.ZERO;
		BigDecimal totalAmt = BigDecimal.ZERO;
		for (InvoiceLineType lineItem : inv.getInvoiceType().getInvoiceLine()) {
			TaxTotalType totalType = lineItem.getTaxTotal().get(0);
			
			ItemType itemType = lineItem.getItem();
			BigDecimal vatRate = itemVatMap.get(itemType.getSellersItemIdentification().getID().getValue());
			if (itemType.getClassifiedTaxCategory().get(0).getPercent().getValue().compareTo(vatRate) != 0 && totalType.getTaxAmount().getValue().intValue() > 0) {
				throw new SoftException("Vat rate is not matching for the item: " + itemType.getSellersItemIdentification().getID().getValue() + ":" + vatRate);
			}
			BigDecimal lineAmount = lineItem.getPrice().getPriceAmount().getValue();
			lineAmount = lineItem.getInvoicedQuantity().getValue().multiply(lineAmount);
			if (lineItem.getPrice().getBaseQuantity() != null) {
				lineAmount = lineAmount.divide(lineItem.getPrice().getBaseQuantity().getValue(), 2 , RoundingMode.HALF_UP);
			}

			BigDecimal xmlLineAmt = lineItem.getLineExtensionAmount().getValue();
			float diff = lineAmount.subtract(xmlLineAmt).floatValue();
			if (-0.25 > diff || diff > 0.25) {
				throw new SoftException("Net amount without vat is not matching for the item: " + itemType.getSellersItemIdentification().getID().getValue() + ":" + lineAmount);
			}
			taxableAmt = taxableAmt.add(lineAmount.setScale(2, RoundingMode.HALF_UP));

			BigDecimal vatAmt = lineAmount.multiply(vatRate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
			
			diff = vatAmt.subtract(totalType.getTaxAmount().getValue()).floatValue();
			if (-0.25 > diff || diff > 0.25) {
				throw new SoftException("Vat amount is not matching for the item: " + itemType.getSellersItemIdentification().getID().getValue() + ":" + vatAmt);
			}
			totalVatAmt = totalVatAmt.add(vatAmt.setScale(2, RoundingMode.HALF_UP));

			BigDecimal totalItemAmt = lineAmount.add(vatAmt);
			diff = totalItemAmt.subtract(totalType.getRoundingAmount().getValue()).floatValue();
			if (-0.25 > diff || diff > 0.25) {
				throw new SoftException("Total amount is not matching for the item: " + itemType.getSellersItemIdentification().getID().getValue() + ":" + totalItemAmt);
			}
			totalAmt = totalAmt.add(totalItemAmt.setScale(2, RoundingMode.HALF_UP));
		}
		BigDecimal xmlTotalTaxableAmt = inv.getInvoiceType().getLegalMonetaryTotal().getLineExtensionAmount().getValue();
		float diff = xmlTotalTaxableAmt.subtract(taxableAmt).floatValue();
		if (-0.25 > diff || diff > 0.25) {
			throw new SoftException("Total taxable amount is not matching for the invoice: " + inv.getInvoiceType().getID().getValue() + ":" + taxableAmt);
		}

		diff = totalVatAmt.subtract(inv.getInvoiceType().getTaxTotal().get(0).getTaxAmount().getValue()).floatValue();
		if (-0.25 > diff || diff > 0.25) {
			throw new SoftException("Total tax amount is not matching for the invoice: " + inv.getInvoiceType().getID().getValue() + ":" + totalVatAmt);
		}

		diff = totalAmt.subtract(inv.getInvoiceType().getLegalMonetaryTotal().getPayableAmount().getValue()).floatValue();
		if (-0.25 > diff || diff > 0.25) {
			throw new SoftException("Total amount is not matching for the invoice: " + inv.getInvoiceType().getID().getValue() + ":" + totalAmt);
		}
	}

	private void persistInvForRecon(List<InvoiceInfo<String>> processedInvs) {
		List<ReconHead> reconDatas = new ArrayList<>(processedInvs.size());
		processedInvs.stream().filter(i -> i.getInvoiceType() != null).forEach(invoice -> {
			ReconHead head = new ReconHead();
			head.setBusinessDate(invoice.getBusinessDate());
			head.setInvoiceNumber(invoice.getInvoiceIdentifier());
			head.setOrderNo(invoice.getOrderNo());
			head.setOriginSys(invoice.getOriginSystem());
			head.setStore(invoice.getStore());
			head.setTaxAmt(invoice.getInvoiceType().getTaxTotal().get(0).getTaxAmount().getValue());
			head.setTaxExAmt(invoice.getInvoiceType().getLegalMonetaryTotal().getLineExtensionAmount().getValue());
			head.setTaxInclvAmt(invoice.getInvoiceType().getLegalMonetaryTotal().getTaxInclusiveAmount().getValue());
			head.setType(invoice.getType());
			head.setTypeCode(invoice.getInvoiceType().getInvoiceTypeCode().getValue());
			head.setXmlIssueDate(invoice.getInvoiceType().getIssueDate().getValue().toGregorianCalendar().getTime());
			List<InvoiceLineType> lines = invoice.getInvoiceType().getInvoiceLine();
			head.setDetails(new ArrayList<>(lines.size()));
			for (InvoiceLineType invoiceLine : lines) {
				EReconDetail detail = new EReconDetail();
				detail.setDiscAmt(BigDecimal.ZERO);
				for (AllowanceChargeType charge : invoiceLine.getAllowanceCharge()) {
					detail.setDiscAmt(detail.getDiscAmt().add(charge.getAmount().getValue()));
				}
				detail.setInvoiceLineId(Long.parseLong(invoiceLine.getID().getValue()));
				detail.setInvoiceNumber(invoice.getInvoiceIdentifier());
				detail.setQty(invoiceLine.getInvoicedQuantity().getValue());
				detail.setTotalVat(invoiceLine.getTaxTotal().get(0).getTaxAmount().getValue());
				detail.setTotalAmt(invoiceLine.getTaxTotal().get(0).getRoundingAmount().getValue());
				detail.setTotalAmtExTax(invoiceLine.getLineExtensionAmount().getValue());
				detail.setUnitAmtExTax(invoiceLine.getPrice().getPriceAmount().getValue());
				detail.setVatRate(invoiceLine.getItem().getClassifiedTaxCategory().get(0).getPercent().getValue());
				if (invoiceLine.getPrice().getBaseQuantity() != null) {					
					detail.setBaseQty(invoiceLine.getPrice().getBaseQuantity().getValue());
				}
				if (invoiceLine.getItem().getSellersItemIdentification() != null) {
					detail.setItem(invoiceLine.getItem().getSellersItemIdentification().getID().getValue());
				}
				head.getDetails().add(detail);
			}
			reconDatas.add(head);
		});
		b2cInvoicingDAO.persistInvForRecon(reconDatas);
	}
}
