package com.extra.einvoicing.ebs;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.extra.einvoicing.ebs.model.ZatcaRequest;
import com.extra.einvoicing.ebs.model.ZatcaResponse;
import com.extra.einvoicing.ebs.service.client.IZatcaInterfaceAPI;
import com.extra.einvoicing.ebs.util.InvoiceUtil;
import com.gazt.einvoicing.hashing.generation.service.HashingGenerationService;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

@SpringBootTest
class EinvoicingApplicationTests {

	@Autowired
	private IZatcaInterfaceAPI interfaceAPI;

	@Autowired
	private HashingGenerationService hashingGenerationService;

	@Autowired
	@Qualifier("jdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() throws Exception {
		
		InvoiceType invoiceType = InvoiceUtil.deserializeToType(Files.readAllBytes(Paths.get("D:\\Projects\\zatca-poc\\Standard\\Invoice\\Standard_Invoice.xml")), InvoiceType.class);
		Long icv = jdbcTemplate.queryForObject("SELECT XX_EINVOICE_COUNTER_SEQ.NEXTVAL FROM DUAL", Collections.emptyMap(), Long.class);
		invoiceType.getUUID().setValue(UUID.randomUUID().toString());
		invoiceType.getAdditionalDocumentReference().get(0).getUUID().setValue(icv.toString());
		
		
		ZatcaRequest request = new ZatcaRequest();
		request.setUuid(invoiceType.getUUID().getValue());
		String xml = InvoiceUtil.serializeToXml(invoiceType);
		request.setInvoiceHash(hashingGenerationService.getInvoiceHash(xml));
		request.setInvoice(Base64.getEncoder().encodeToString(xml.getBytes()));
		
		System.out.println(xml);
		
		ZatcaResponse response = interfaceAPI.clearanceApi(request, 1);
		System.out.println(response.getClearanceStatus());
		System.out.println(response.getClearedInvoice());
	}

}
