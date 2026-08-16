package com.extra.apple.common.interceptor;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author aibrahim
 *
 */
public class HeaderInterceptor implements RequestInterceptor {

	private static final Logger LOG = Logger.getLogger(HeaderInterceptor.class);

	private static final String CHARSET_UTF_8 = "UTF-8";
	private static final String ENCRYPTION_SCHEME_AES = "AES";
	private static final String HASH_SHA_256 = "SHA-256";

	private String apiClientId = "EXT226";

	private SecretKeySpec secretKeySpec;

	public HeaderInterceptor(String apiSecretKey, String apiClientId) throws UnsupportedEncodingException {
		this.apiClientId = apiClientId;
		secretKeySpec = new SecretKeySpec(apiSecretKey.getBytes(CHARSET_UTF_8), ENCRYPTION_SCHEME_AES);
	}

	@Override
	public void apply(RequestTemplate template) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		template.header("API_CLIENT_ID", apiClientId);
		template.header("TIMESTAMP", timestamp);
		template.header("AUTHORIZATION", generateToken(timestamp));
	}

	public String generateToken(String timestamp) {
		String token = null;
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_SCHEME_AES);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

			byte[] encryptedBytes = cipher.doFinal(timestamp.getBytes(CHARSET_UTF_8));

			MessageDigest messageDigest = MessageDigest.getInstance(HASH_SHA_256);
			byte[] macEncryptedBytes = messageDigest.digest(encryptedBytes);

			byte[] encodedBytes = Base64Utils.encode(macEncryptedBytes);
			token = new String(encodedBytes, CHARSET_UTF_8);

		} catch (final Exception exception) {
			LOG.error("Error while generating the token", exception);
			throw new RuntimeException(exception);
		}
		return token;
	}
}
