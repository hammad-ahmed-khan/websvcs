package com.extra.oms.sim;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.extra.oms.sim.bean.Request;
import com.extra.oms.sim.config.MvcConfiguration;

/**
 * @author aibrahim
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfiguration.class })
public class SimServiceApp {

	@Autowired
	private DispatchController controller;

	@BeforeClass
	public static void init() throws Exception {
		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		builder.bind("java:comp/env/jdbc/sim", new DriverManagerDataSource("jdbc:oracle:thin:@192.168.41.184:1521/SIMQA.extrastores.com", "sim14", "retQAtrex123"));
		builder.activate();
	}

	@Test
	public void testBO() throws Exception {
		Request request = new Request();
		request.setOrderNo("WEBQATSTS808");
		request.setItem("100021534");
		request.setFulfilNo("1");
		request.setQty(1);
		controller.dispatchItem(request);
	}
}
