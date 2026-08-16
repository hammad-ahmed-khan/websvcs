package com.extra.einvoicing.config;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;

/**
 * @author aibrahim
 *
 */
public class FeignMessageAuthConfig {

	@Bean
	public RequestInterceptor basicAuth(@Value("${email.config.username}") String userName, @Value("${email.config.password}") String password, @Value("${email.config.header.req-id} ") String requestId, @Value("${email.config.header.application-id}") String applicationId) {
		return new BasicAuthRequestInterceptor(userName, password, StandardCharsets.UTF_8) {
			@Override
			public void apply(RequestTemplate template) {
				super.apply(template);
				template.header("X-Request-Datetime", new SimpleDateFormat().format(new Date()));
				template.header("X-Request-ID", requestId);
				template.header("X-Application-ID", applicationId);
			}
		};
	}
}
