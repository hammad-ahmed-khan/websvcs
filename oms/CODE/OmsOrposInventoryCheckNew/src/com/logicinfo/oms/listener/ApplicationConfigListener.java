package com.logicinfo.oms.listener;

import java.util.Arrays;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.logicinfo.oms.util.OmsSysParameterUtil;

/**
 * ApplicationConfigListener.java
 * aibrahim
 * 2024
 */
public class ApplicationConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			OmsSysParameterUtil.initialize(Arrays.asList("WH_SMALL_POS_THRESHOLD", "WH_BIG_POS_THRESHOLD", "OMS_SYSTEM_OPTION", "ST_BIG_POS_THRESHOLD", "ST_SMALL_POS_THRESHOLD"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
