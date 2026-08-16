/**
 * 
 */
package com.extra.notification.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

import com.extra.notification.common.Constant;
import com.extra.notification.util.APIUtil;
import com.extra.notification.util.PropertyHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.ClientResponse;

/**
 * @author aibrahim
 *
 */
public class APIService {

	public <T> Future<ClientResponse> invokeAPI(String resource, T data) {

		AsyncWebResource webResource = APIUtil.getResource(resource);
		Gson gson = new GsonBuilder().create();
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return webResource.type("application/json")
				.header("X-Request-ID", PropertyHolder.getValue(Constant.NOTIFICATION_GATEWAY_REQUEST_ID))
				.header("X-Application-ID", PropertyHolder.getValue(Constant.NOTIFICATION_GATEWAY_APPLICATION_ID))
				.header("Authorization", "Basic " + PropertyHolder.getValue(Constant.NOTIFICATION_GATEWAY_AUTHORIZATION_TOKEN))
				.header("X-Request-Datetime", parser.format(new Date())).post(ClientResponse.class, gson.toJson(data));
	}
}
