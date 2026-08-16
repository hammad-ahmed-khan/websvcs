package com.extra.proxy.service;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author aibrahim
 *
 */
@Service
public class ProxyService {

	private static final Logger LOG = Logger.getLogger(ProxyService.class);

	private static final String HEADER_URL = "apiURL";

	private static final String AUTHENTICATION = "auth";

	private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
	
	static {
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
	}

	final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging)
			.callTimeout(0L, TimeUnit.MICROSECONDS)
			.connectTimeout(0L, TimeUnit.MICROSECONDS)
			.readTimeout(0L, TimeUnit.MICROSECONDS)
			.writeTimeout(0L, TimeUnit.MICROSECONDS)
			.sslSocketFactory(getSSLSocketFactory(), getTrustManager()).build();

	public ResponseEntity<JsonNode> postRequest(String request, Map<String, String> headerMap, MediaType mediaType) throws Exception {
		String url = headerMap.remove(HEADER_URL);
		if (url == null || url.isEmpty()) {
			LOG.error("Missing URL");
			throw new IllegalArgumentException("Missing URL in the header");
		}
		
		headerMap.remove("Host");
		headerMap.remove("host");
		RequestBody body = RequestBody.create(mediaType, request);
		Builder reqBuilder = new Request.Builder().url(url).post(body);
		String authorization = headerMap.remove(AUTHENTICATION);
		reqBuilder.headers(Headers.of(headerMap));
		if (authorization != null && !authorization.isEmpty()) {
			reqBuilder.header("Authorization", authorization);
		}
		Response response = client.newCall(reqBuilder.build()).execute();
		BodyBuilder resBuilder = ResponseEntity.ok();
		for (String headerKey : response.headers().names()) {
			if (!"Content-Length".equalsIgnoreCase(headerKey)) {				
				resBuilder.header(headerKey, response.header(headerKey));
			}
		}
		return resBuilder.body(new ObjectMapper().readValue(response.body().string(), JsonNode.class));
	}

	public ResponseEntity<JsonNode> putRequest(String request, Map<String, String> headerMap, MediaType mediaType) throws Exception {
		String url = headerMap.remove(HEADER_URL);
		if (url == null || url.isEmpty()) {
			LOG.error("Missing URL");
			throw new IllegalArgumentException("Missing URL in the header");
		}
		
		headerMap.remove("Host");
		headerMap.remove("host");
		RequestBody body = RequestBody.create(mediaType, request);
		Builder reqBuilder = new Request.Builder().url(url).put(body);
		String authorization = headerMap.remove(AUTHENTICATION);
		reqBuilder.headers(Headers.of(headerMap));
		if (authorization != null && !authorization.isEmpty()) {
			reqBuilder.header("Authorization", authorization);
		}
		Response response = client.newCall(reqBuilder.build()).execute();
		BodyBuilder resBuilder = ResponseEntity.ok();
		for (String headerKey : response.headers().names()) {
			if (!"Content-Length".equalsIgnoreCase(headerKey)) {				
				resBuilder.header(headerKey, response.header(headerKey));
			}
		}
		return resBuilder.body(new ObjectMapper().readValue(response.body().string(), JsonNode.class));
	}

	public ResponseEntity<JsonNode> getRequest(Map<String, String> requestParams, Map<String, String> headerMap, MediaType mediaType) throws Exception {
		String url = headerMap.remove(HEADER_URL);
		if (url == null || url.isEmpty()) {
			LOG.error("Missing URL");
			throw new IllegalArgumentException("Missing URL in the header");
		}
		
		headerMap.remove("Host");
		headerMap.remove("host");
		HttpUrl.Builder httpUrl = HttpUrl.parse(url).newBuilder();
		if (requestParams != null) {
			for (Entry<String, String> entry : requestParams.entrySet()) {			
				httpUrl.addQueryParameter(entry.getKey(), entry.getValue());
			}
		}
		Builder reqBuilder = new Request.Builder().url(httpUrl.build()).get();
		String authorization = headerMap.remove(AUTHENTICATION);
		reqBuilder.headers(Headers.of(headerMap));
		if (authorization != null && !authorization.isEmpty()) {
			reqBuilder.header("Authorization", authorization);
		}
		Response response = client.newCall(reqBuilder.build()).execute();
		BodyBuilder resBuilder = ResponseEntity.ok();
		for (String headerKey : response.headers().names()) {
			if (!"Content-Length".equalsIgnoreCase(headerKey)) {				
				resBuilder.header(headerKey, response.header(headerKey));
			}
		}
		return resBuilder.body(new ObjectMapper().readValue(response.body().string(), JsonNode.class));
	}

	private static SSLSocketFactory getSSLSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, getTrustManagers(), new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static TrustManager[] getTrustManagers() {
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

	private static X509TrustManager getTrustManager() {
		X509TrustManager trustAllCert = new X509TrustManager() {
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
		};
		return trustAllCert;
	}
}
