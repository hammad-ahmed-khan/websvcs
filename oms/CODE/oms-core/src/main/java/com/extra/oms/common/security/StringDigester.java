/**
 * 
 */
package com.extra.oms.common.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Base64Utils;

/**
 * @author aibrahim
 *
 */
public class StringDigester {

	private String algorithm = "SHA-256";

	private MessageDigest md = null;

	private Charset defaultCharset = null;

	protected synchronized void initialize() throws NoSuchAlgorithmException {
		this.md = MessageDigest.getInstance(this.algorithm);
		defaultCharset = Charset.forName("UTF-8");
	}

	public String digest(String rawText) {
		return digest(rawText.getBytes(defaultCharset), RandomStringUtils.random(10, true, true).getBytes(defaultCharset));
	}

	private String digest(byte[] rawText, byte[] salt) {
		byte[] digest = null;
		synchronized (this.md) {
			this.md.reset();
			this.md.update(rawText);
			this.md.update(salt);
			digest = this.md.digest();
			for (int i = 0; i < 10; i++) {
				this.md.reset();
				digest = this.md.digest(digest);
			}
			final byte[] result = new byte[digest.length + salt.length];
	        
	        System.arraycopy(salt, 0, result, 0, salt.length);
	        System.arraycopy(digest, 0, result, salt.length, digest.length);
	        return Base64Utils.encodeToString(result);
		}
	}

	public boolean matches(String message, String digest) {
		byte[] encByte = Base64Utils.decodeFromString(digest);
		byte[] salt = Arrays.copyOf(encByte, 10);
		String msgDigest = digest(message.getBytes(defaultCharset), salt);
		return msgDigest.equals(digest);
	}

	public StringDigester cloneDigester() throws NoSuchAlgorithmException {
		StringDigester digester = new StringDigester();
		digester.initialize();
		return digester;
	}
}
