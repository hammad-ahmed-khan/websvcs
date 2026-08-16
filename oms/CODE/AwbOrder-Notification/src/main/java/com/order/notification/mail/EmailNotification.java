package com.order.notification.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.PropertiesReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.order.notification.mail.bean.EmailInfo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class EmailNotification {
	private static Logger LOGGER = Logger.getLogger(EmailNotification.class);
	public void sendMail(EmailInfo emailInfo) {
		Gson gson = new GsonBuilder().create();
		String mailInfoString = gson.toJson(emailInfo);
		Client client = Client.create();
		System.setProperty("https.protocols", "TLSv1.1");
		WebResource webResource = client.resource(PropertiesReader.getProperty(Constant.EMAILAPI));
		Date date = new Date();
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		parser.setTimeZone(TimeZone.getTimeZone("UTC"));
		ClientResponse response = webResource.type("application/json")
				.header("X-Request-ID", "644e1dd7-2a7f-18fb-b8ed-ed78c3f92c2b").header("X-Application-ID", "E-COMMERCE")
				.header("X-Request-Datetime", parser.format(date))
/*Prod*/		.header("Authorization", "Basic "+"MDc5NmMyOTc0M2Q1NDZhNGFlODcwYTRkMWQ3YTc3OTk6OEQ4OWM0Q0I0QzU5NDk4MTk5RjhkYjIwOUY3MWExM2Y=")
/*QA*/	      //.header("Authorization", "Basic "+"MjMzNzEyZTYyNjc3NGUxOGEwNTkzMWJlYWJkZmJmM2Y6RjYxMmE3RjIyNjYwNDQxMTg3RmM2QzZlZDAzNTRCQUM==DAzNTRCQUM=")
				.post(ClientResponse.class, mailInfoString);
		
		LOGGER.info("Email Status code is"+response.getStatus());
		if (response.getStatus() != 200) {
			LOGGER.error("failed to send email");
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

	 }

}
