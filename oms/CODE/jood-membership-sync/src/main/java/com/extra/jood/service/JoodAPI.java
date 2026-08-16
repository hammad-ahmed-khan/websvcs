package com.extra.jood.service;

import com.extra.jood.model.JoodSyncRequest;
import com.extra.jood.model.JoodSyncResponse;

import feign.Headers;
import feign.RequestLine;

@Headers({ "Content-Type: application/json" })
public interface JoodAPI {

	@RequestLine("POST /vipmembership")
	public JoodSyncResponse joodVipmembership(JoodSyncRequest joodSyncRequest);

}
