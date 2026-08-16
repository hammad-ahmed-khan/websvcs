package com.extra.notification.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyHolder {

	private static Logger LOGGER = Logger.getLogger(PropertyHolder.class);

	private static Properties props;
	
	static {
		load();
	}

	private static void load() {
		try {
			props = System.getProperties();
			String configFile = props.getProperty("configFile");
			if (configFile != null) {
				LOGGER.info("Loading the properties from the file: " + configFile);
				props.load(new FileInputStream(configFile));
			} else {
				LOGGER.info("Loading the default properties ");
				props.load(PropertyHolder.class.getClassLoader().getResourceAsStream("configuration.properties"));
			}
			LOGGER.info("Properties loaded successfully...");
		} catch (Exception e) {
			LOGGER.error("Error while loading the property file.", e);
		}
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}
}
