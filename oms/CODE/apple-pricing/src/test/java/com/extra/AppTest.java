package com.extra;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.extra.apple.common.interceptor.HeaderInterceptor;
import com.extra.apple.pricing.service.AppleAPIService;

import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Logger.JavaLogger;
import feign.Logger.Level;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

public class AppTest {

	public static void main(String[] a) throws UnsupportedEncodingException {
		/*
		 * System.setProperty("https.protocols", "TLSv1.2");
		 * 
		 */

		HeaderInterceptor interceptor = new HeaderInterceptor("myLbfUo0ge9kDIo3BFiLQlPe", "EXT802");

		String time = String.valueOf(System.currentTimeMillis());
		System.out.println(time);
		System.out.println(interceptor.generateToken(time));

		AppleAPIService service = Feign.builder().client(new Client.Default(getSSLSocketFactory(), getHostnameVerifier())).requestInterceptor(interceptor).encoder(new GsonEncoder()).decoder(new GsonDecoder()).logLevel(Level.FULL)
				.logger(new JavaLogger()).logLevel(Logger.Level.HEADERS).target(AppleAPIService.class, "https://dc-pricing-api.apple.com/pricing-api/api/v1");

		System.out.println(service.getActiveMPNs(Long.valueOf("939464")).getPriceSheetDescriptions().get(0).getMpns());
	}

	private static SSLSocketFactory getSSLSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, getTrustManager(), new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//TrustManager
//trust manager that does not validate certificate chains
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

//HostnameVerifier
	private static HostnameVerifier getHostnameVerifier() {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		};
		return hostnameVerifier;
	}

}