package com.extra.ecom.reconcilliation;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.extra.ecom.reconcilliation.config.MvcConfiguration;

/**
 * @author aibrahim
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfiguration.class })
public class ReconciliationApp {

	@Autowired
	private ReconciliationController reconciliationController;

	@BeforeClass
	public static void init() throws Exception {
		/*
		 * SimpleNamingContextBuilder builder =
		 * SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		 * builder.bind("java:comp/env/jdbc/extradev", new DriverManagerDataSource(
		 * "jdbc:oracle:thin:@192.168.41.188:1521/RMSQA.extrastores.com", "EXTRADEV",
		 * "wFeJdt6c")); builder.activate();
		 */
	}

	@Test
	public void testBO() throws Exception {
		reconciliationController.reConcileProduct();
	}
}
