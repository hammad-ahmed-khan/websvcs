package com.order.notification.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.db.util.GetDBConnection;
import com.db.util.PropertiesReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.order.notification.sms.bean.SmsInfo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SmsNotification {

	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);

	public void sendsms(SmsInfo smsInfo) {
		LOGGER.info("sendsms executing with order number "+smsInfo.getOrderNumber());
		System.setProperty("https.protocols", "TLSv1.1");
		
		String smsNotification_URI = PropertiesReader.getProperty("SmsNotification_URI");
		Date date = new Date();
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		parser.setTimeZone(TimeZone.getTimeZone("UTC"));
		Gson gson = new GsonBuilder().create();
		
		String smsInfoString = gson.toJson(smsInfo);
		String smsInfoDetails = "";
		Client client = Client.create();
		LOGGER.info("sendsms executing with order number "+smsNotification_URI);
		WebResource webResource = client.resource(smsNotification_URI);
		LOGGER.info("After webresource"+webResource);
		ClientResponse response = webResource.type("application/json")
				.header("X-Request-ID", "644e1dd7-2a7f-18fb-b8ed-ed78c3f92c2b").header("X-Application-ID", "E-COMMERCE")
				.header("X-Request-Datetime", parser.format(date))
				.header("Authorization",
						"Basic " + PropertiesReader.getProperty("client.and.secret.id.base64.encoded"))
				.post(ClientResponse.class, smsInfoString);
		
		if (response.getStatus() != 200) {
			
			LOGGER.error("Failed : HTTP error code : " + response.getStatus());
			LOGGER.info("request json body: "+smsInfoString.toString());
			LOGGER.info("request json body: "+smsInfoString);
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		
		}
		else{
			LOGGER.info("request json body: "+smsInfoString.toString());
			LOGGER.info("SMS Notification sent successfully to StoreManager: "+smsInfo.getOrderNumber()+" Response status code :"+ response.getStatus());
		}
		
		
		/*
		 * String output = response.getEntity(String.class);
		 * System.out.println(output);
		 */
	}

}
