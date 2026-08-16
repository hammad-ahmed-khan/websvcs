package com.extra.jood;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.extra.jood.bean.CustomerInfo;
import com.extra.jood.bean.MembershipInfo;
import com.extra.jood.bean.MembershipResponse;
import com.extra.jood.bean.TransactionDetail;
import com.extra.jood.bean.TransactionInfo;
import com.extra.jood.config.MvcConfiguration;
import com.extra.jood.service.JOODService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aibrahim
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfiguration.class })
public class JoodTestApp {

	@Autowired
	private JOODService joodService;

	@BeforeClass
	public static void init() throws Exception {
		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		builder.bind("java:comp/env/jdbc/oms", new DriverManagerDataSource("jdbc:oracle:thin:@exvm-qarmsdb01.extrastores.com:1521/RMSQA.extrastores.com", "OMSDEV", "Logic123"));
		builder.activate();
	}

	@Test
	public void testBO() {
		MembershipInfo membershipInfo = new MembershipInfo();
		membershipInfo.setActiveMembershipID(1000515L);
		membershipInfo.setMembershipTypeID(1L);
		membershipInfo.setRenewUpgradeDetails(false);
		
		TransactionInfo info = new TransactionInfo();
		info.setCountry("KSA");
		info.setLocation(13001L);
		CustomerInfo customerInfo = new CustomerInfo();
		customerInfo.setId("321");
		customerInfo.setEmail("rami.jamal@extra.com");
		customerInfo.setMobile("00201099022300");
		customerInfo.setName("RamiJamal");
		info.setCustomer(customerInfo);
		
		membershipInfo.setTransaction(info);
		info.setChannel("ORPOS");
		info.setDate(new Date());
		info.setOrderNumber("221545");
		info.setTotalJoodDiscount(BigDecimal.valueOf(50));
		info.setTotalRetailDiscount(BigDecimal.valueOf(100));
		info.setTotalRetailPrice(BigDecimal.valueOf(500));
		info.setTotalSellingPrice(BigDecimal.valueOf(350));
		info.setTransactionLineItems(new ArrayList<TransactionDetail>(1));
		info.setTransactionNumber("KS132434454");
		
		TransactionDetail detail = new TransactionDetail();
		info.getTransactionLineItems().add(detail);
		detail.setItem("100315729");
		detail.setLineNumber(1L);
		detail.setQty(1);
		detail.setUnitRetailPrice("500");
		detail.setUnitSellingPrice("350");
		detail.setUnitTotalJoodDiscount("50");
		detail.setUnitTotalRetailDiscount("100");
		
		try {
			System.out.println(new ObjectMapper().writeValueAsString(membershipInfo));
			MembershipResponse response = joodService.validateEligibility(membershipInfo);
			System.out.println(new ObjectMapper().writeValueAsString(response));
			System.out.println("Completed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
