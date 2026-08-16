package com.extra.jood;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.extra.proxy.config.MvcConfiguration;
import com.extra.proxy.controller.ProxyController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author aibrahim
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfiguration.class })
public class ProxyTestApp {

	@Autowired
	private ProxyController controller;

	private MockMvc mockMvc;

	public static void main(String[] a) throws Exception {
		
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); 
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging)
				.callTimeout(0L, TimeUnit.MICROSECONDS)
				.connectTimeout(0L, TimeUnit.MICROSECONDS)
				.readTimeout(0L, TimeUnit.MICROSECONDS)
				.writeTimeout(0L, TimeUnit.MICROSECONDS).build();
		
		
		JsonNode node = new ObjectMapper().readValue("{\"membershipId\":1011709,\"Customer_mobile\":\"00966568803591\",\"Customer_Email\":\"test53123@gmail.com\",\"Customer_first_name\":\"testtest\",\"Customer_last_name\":\"testtest\",\"Contract_number\":\"20240321130011070357\",\"Invoice_number\":\"\",\"Invoice_line_number\":\"\",\"Service_product_line\":\"VIP\",\"Package_name\":\"B500-JOODGOLDMEMBERSHIP\",\"Service_SKU\":\"100052044\",\"Contract_period\":\"364\",\"Contract_starting_date\":\"2024-03-21\",\"Contract_ending_date\":\"2025-03-20\",\"Source_system\":\"POS\",\"Contract_status\":\"Active\",\"Paid_Amount\":\"599\"}", JsonNode.class);
		System.out.println(new ObjectMapper().writeValueAsString(node));
		
		
		RequestBody body = RequestBody.create(okhttp3.MediaType.get(MediaType.APPLICATION_JSON_VALUE), new ObjectMapper().writeValueAsString(node));
		Builder reqBuilder = new Request.Builder().url("https://qa-customer-profile-v3.uk-e1.cloudhub.io/customer/vipmembership").post(body);
		
		reqBuilder.addHeader("X-Application-ID", "E-COMMERCE");
		reqBuilder.addHeader("X-Request-ID", "644e1dd7-2a7f-18fb-b8ed-ed78c3f92c2b");
		reqBuilder.addHeader("X-Request-Datetime", "24/03/24, 1:29 PM");
		reqBuilder.addHeader("Content-Type", "application/json");
		reqBuilder.addHeader("Accept", "*/*");
		reqBuilder.addHeader("Authorization", "Basic MjMzNzEyZTYyNjc3NGUxOGEwNTkzMWJlYWJkZmJmM2Y6RjYxMmE3RjIyNjYwNDQxMTg3RmM2QzZlZDAzNTRCQUM=");
		Response response = client.newCall(reqBuilder.build()).execute();
		System.out.println(response.body().string());
	}

	@Test
	public void testBO() throws Exception, IOException {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		// mockMvc.perform(post("/api").content("{\"test\":\"value\"}").contentType(MediaType.APPLICATION_JSON_UTF8));
		mockMvc.perform(post("/").content("{\"membershipId\":1011709,\"Customer_mobile\":\"00966568803591\",\"Customer_Email\":\"test53123@gmail.com\",\"Customer_first_name\":\"test test\",\"Customer_last_name\":\"test test\",\"Contract_number\":\"20240321130011070357\",\"Invoice_number\":\"\",\"Invoice_line_number\":\"\",\"Service_product_line\":\"VIP\",\"Package_name\":\"B500-JOOD GOLD MEMBERSHIP\",\"Service_SKU\":\"100052044\",\"Contract_period\":\"364\",\"Contract_starting_date\":\"2024-03-21\",\"Contract_ending_date\":\"2025-03-20\",\"Source_system\":\"POS\",\"Contract_status\":\"Active\",\"Paid_Amount\":\"599\"}")
				.header("Authorization", "Basic MjMzNzEyZTYyNjc3NGUxOGEwNTkzMWJlYWJkZmJmM2Y6RjYxMmE3RjIyNjYwNDQxMTg3RmM2QzZlZDAzNTRCQUM=")
				.header("apiURL", "https://qa-customer-profile-v3.uk-e1.cloudhub.io/customer/vipmembership")
				.contentType(MediaType.APPLICATION_JSON));
	}
}
