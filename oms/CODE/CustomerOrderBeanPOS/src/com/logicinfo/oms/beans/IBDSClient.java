package com.logicinfo.oms.beans;

import java.util.List;

import com.extra.bds.bean.Request;
import com.extra.bds.bean.Response;

import feign.Headers;
import feign.RequestLine;

/**
 * @author aibrahim
 *
 */
public interface IBDSClient {

	@RequestLine("PUT /bds/booking")
	@Headers({ "Content-Type: application/json", "Accept: application/json" })
	List<Response> bookSlot(Request request);
}
