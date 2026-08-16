package com.extra.oms.custOrder;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.extra.oms.custOrder.config.MvcConfiguration;
import com.extra.oms.custOrder.service.BackOrderService;

/**
 * @author aibrahim
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfiguration.class })
public class BackOrderApp {

	@Autowired
	private BackOrderService backOrderService;

	@BeforeClass
	public static void init() throws Exception {
		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		builder.bind("java:comp/env/jdbc/oms", new DriverManagerDataSource("jdbc:oracle:thin:@192.168.41.188:1521/RMSQA.extrastores.com", "OMSDEV", "Logic123"));
		builder.activate();
	}

	@Test
	public void testBO() throws Exception {
		backOrderService.processBackOrders();
	}
}
