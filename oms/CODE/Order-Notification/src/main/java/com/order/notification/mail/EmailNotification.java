package com.order.notification.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.db.util.PropertiesReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.order.notification.mail.bean.EmailInfo;
import com.order.notification.service.StoreManagerNotification;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class EmailNotification {

	private static final Logger LOGGER = LogManager.getLogger(EmailNotification.class.getName());
	public void sendMail(EmailInfo emailInfo) {
		LOGGER.info("sendMail executing for order number "+emailInfo.getOrderNumber());
		ClientResponse response = null;
		Gson gson;
		String mailInfoString = "", status = "";
		
		try {
			if(null!=emailInfo.getEmail().getRecipients().getTo().get(0).getEmail()) {
				gson = new GsonBuilder().create();
				mailInfoString = gson.toJson(emailInfo);
				// HttpAuthenticationFeature feature =
				// HttpAuthenticationFeature.basic("233712e626774e18a05931beabdfbf3f",
				// "F612a7F22660441187Fc6C6ed0354BAC");
				
				
				System.setProperty("https.protocols", "TLSv1.1");
				String emailNotification_URI = PropertiesReader.getProperty("EmailNotification_URI");
				Client client = Client.create();
		
				WebResource webResource = client.resource(emailNotification_URI);
				Date date = new Date();
				SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				parser.setTimeZone(TimeZone.getTimeZone("UTC"));
				response = webResource.type("application/json")
						.header("X-Request-ID", "644e1dd7-2a7f-18fb-b8ed-ed78c3f92c2b").header("X-Application-ID", "E-COMMERCE")
						.header("X-Request-Datetime", parser.format(date))
						.header("Authorization",
								"Basic " + PropertiesReader.getProperty("client.and.secret.id.base64.encoded"))
						.post(ClientResponse.class, mailInfoString);
			}else {
				LOGGER.info("Inside Failure");
				status ="fail";
			}
			if (status =="fail" || response.getStatus() != 200 ) {
				LOGGER.error("Failed : HTTP error code : " + response.getStatus());
				LOGGER.info("request json body: "+mailInfoString.toString());
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			else{
				LOGGER.info("request json body: "+mailInfoString.toString());
				LOGGER.info("Email Notification sent succesfully to store manager: "+emailInfo.getOrderNumber()+"Response status code"+response.getStatus());
				
			}
		}catch(Exception e) {
			throw e;
		}
		
		/*
		 * String output = response.getEntity(String.class);
		 * System.out.println(output);
		 */

	}

}
