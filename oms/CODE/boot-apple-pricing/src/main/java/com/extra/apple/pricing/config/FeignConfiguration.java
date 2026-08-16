package com.extra.apple.pricing.config;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.extra.apple.common.interceptor.HeaderInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

/**
 * FeignConfiduration.java
 * aibrahim
 * 2025
 */
public class FeignConfiguration {

	@Bean
	public HeaderInterceptor getInterceptor(@Value("${apple.pricing.secret}") String apiSecretKey, @Value("${apple.pricing.clientid}") String apiClientId) throws UnsupportedEncodingException {
		return new HeaderInterceptor(apiSecretKey, apiClientId);
	}

	@Bean
	public GsonEncoder encoder() {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM/dd/yyyy HH:mm").create();
		return new GsonEncoder(gson);
	}

	@Bean
	public GsonDecoder decoder() {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM/dd/yyyy HH:mm").create();
		return new GsonDecoder(gson);
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
