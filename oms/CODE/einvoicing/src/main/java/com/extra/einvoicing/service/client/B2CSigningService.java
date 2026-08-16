package com.extra.einvoicing.service.client;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gazt.einvoicing.signing.service.SigningService;
import com.gazt.einvoicing.signing.service.impl.SigningServiceImpl;
import com.gazt.einvoicing.signing.service.model.InvoiceSigningResult;

/**
 * @author aibrahim
 *
 */
@Service
public class B2CSigningService {

	private SigningService signingService = new SigningServiceImpl();

	private PrivateKey privateKey;

	private String certificate;

	public B2CSigningService(@Autowired @Value("${zatca.api.private-key}") String pKey, @Autowired @Value("${zatca.api.binary-token}") String encCert) throws Exception {
		init(pKey, encCert);
	}

	public InvoiceSigningResult signXml(String xml) throws Exception {
		return signingService.signDocument(xml, privateKey, certificate, "");
	}

	private void init(String pKey, String encCert) throws Exception {

		EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pKey));
		KeyFactory kf = KeyFactory.getInstance("EC");
		privateKey = kf.generatePrivate(privKeySpec);
		certificate = new String(Base64.getDecoder().decode(encCert));
	}
}
