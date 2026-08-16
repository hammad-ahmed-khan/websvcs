package com.logicinfo.oms.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.logicinfo.oms.util.OracleBaseAPIUtil;

/**
 * OracleBaseAPIConfigListener.java
 * aibrahim
 * 2024
 */
public class OracleBaseAPIConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			OracleBaseAPIUtil.initialize();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
