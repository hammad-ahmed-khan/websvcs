package com.extra.einvoicing.config;

import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;

/**
 * @author aibrahim
 *
 */
public class FeignSOAPConfiguration {

	private static final JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();

	@Bean
	public Encoder feignEncoder() {
		return new SOAPEncoder(jaxbFactory);
	}

	@Bean
	public Decoder feignDecoder() {
		return new SOAPDecoder(jaxbFactory);
	}

	@Bean
	public RequestInterceptor getHeaderInterceptor() {
		return (template) -> template.header("SOAPAction", " ");
	}
}
