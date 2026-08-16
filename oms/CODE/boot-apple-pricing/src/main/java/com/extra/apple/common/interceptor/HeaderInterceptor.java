package com.extra.apple.common.interceptor;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author aibrahim
 *
 */
public class HeaderInterceptor implements RequestInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(HeaderInterceptor.class);

	private static final String CHARSET_UTF_8 = "UTF-8";
	private static final String ENCRYPTION_SCHEME_AES = "AES";
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;

	private String apiClientId = "EXT226";

	private SecretKeySpec secretKeySpec;

	public HeaderInterceptor(@Value("${apple.pricing.secret}") String apiSecretKey, @Value("${apple.pricing.clientid}")String apiClientId) throws UnsupportedEncodingException {
		this.apiClientId = apiClientId;
		secretKeySpec = new SecretKeySpec(apiSecretKey.getBytes(CHARSET_UTF_8), ENCRYPTION_SCHEME_AES);
	}

	@Override
	public void apply(RequestTemplate template) {
		String timestamp = String.valueOf(System.currentTimeMillis() + (3 * 60 * 1000) + 18000);
		template.header("API_CLIENT_ID", apiClientId);
		template.header("TIMESTAMP", timestamp);
		template.header("AUTHORIZATION", generateToken(timestamp));
	}

	public String generateToken(String timestamp) {
		String token = null;
		try {
			byte[] iv = new byte[GCM_IV_LENGTH];
			new SecureRandom().nextBytes(iv);
			GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);
            
            byte[] encryptedBytes = cipher.doFinal(timestamp.getBytes(CHARSET_UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedBytes.length);
			byteBuffer.put(iv);
			byteBuffer.put(encryptedBytes);

            byte[] encodedBytes = Base64.getEncoder().encode(byteBuffer.array());
            token = new String(encodedBytes, CHARSET_UTF_8);

		} catch (final Exception exception) {
			LOG.error("Error while generating the token", exception);
			throw new RuntimeException(exception);
		}
		return token;
	}
}
