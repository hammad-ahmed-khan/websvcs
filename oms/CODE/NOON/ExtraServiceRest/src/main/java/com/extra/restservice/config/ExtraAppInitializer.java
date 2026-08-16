package com.extra.restservice.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ExtraAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {

		return new Class[] { ExtraAppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {

		return null;
	}

	@Override
	protected String[] getServletMappings() {

		return new String[] { "/" };
	}

}
