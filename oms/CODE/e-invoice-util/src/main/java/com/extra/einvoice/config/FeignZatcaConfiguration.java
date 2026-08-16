package com.extra.einvoice.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;

/**
 * @author abubakkarSiddique
 *
 */
public class FeignZatcaConfiguration {

	@Bean
	public RequestInterceptor basicAuth(@Value("${zatca.api.binary-token}") String userName, @Value("${zatca.api.secret}") String password) {
		return new BasicAuthRequestInterceptor(userName, password, StandardCharsets.UTF_8) {
		
			@Override
			public void apply(RequestTemplate template) {
				super.apply(template);
				template.header("Accept-Language", "en");
				template.header("Accept-Version", "V2");
				template.header("Content-Type", "application/json");
				template.header("accept", "application/json");
			}
		};
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
