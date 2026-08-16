package com.extra.finance;

import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.extra.finance.config.AppConfig;

/**
 * @author aibrahim
 *
 */
public class Application {

	private static final Logger LOG = Logger.getLogger(Application.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = null;
		LOG.info("Starting File reading process");
		try {
			context = new AnnotationConfigApplicationContext(AppConfig.class);
		} catch(Exception e) {
			LOG.error("Error while reading the ftp files ", e);
			context.close();
		}
 		LOG.info("Completed file reading process");
	}
}
