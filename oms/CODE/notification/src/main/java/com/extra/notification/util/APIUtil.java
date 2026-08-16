package com.extra.notification.util;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class APIUtil {

	private static final Client WEB_CLIENT;

	static {
		System.setProperty("https.protocols", "TLSv1.1");
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, 0);
		clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 0);
		clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		}, getSSLContext()));
		
		WEB_CLIENT = Client.create(clientConfig);
	}

	public static AsyncWebResource getResource(String resource) {
		return WEB_CLIENT.asyncResource(resource);
	}

	private static SSLContext getSSLContext() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, getTrustManager(), new SecureRandom());
			return sslContext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static TrustManager[] getTrustManager() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		} };
		return trustAllCerts;
	}
}
