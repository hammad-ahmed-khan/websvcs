package com.extra.proxy.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.proxy.service.ProxyService;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * aibrahim
 * 2024
 */

@RestController
@RequestMapping(path = "/")
public class ProxyController {

	private static final Logger LOG = Logger.getLogger(ProxyController.class);

	@Autowired
	private ProxyService proxyService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JsonNode> postJSON(@RequestBody String request, @RequestHeader Map<String, String> headerMap) throws Exception {
		return proxyService.postRequest(request, headerMap, okhttp3.MediaType.get(MediaType.APPLICATION_JSON_VALUE));
	}

	@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<JsonNode> postXML(@RequestBody String request, @RequestHeader Map<String, String> headerMap) throws Exception {
		return proxyService.postRequest(request, headerMap, okhttp3.MediaType.get(MediaType.APPLICATION_XML_VALUE));
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JsonNode> putJSON(@RequestBody String request, @RequestHeader Map<String, String> headerMap) throws Exception {
		return proxyService.putRequest(request, headerMap, okhttp3.MediaType.get(MediaType.APPLICATION_JSON_VALUE));
	}

	@PutMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<JsonNode> putXML(@RequestBody String request, @RequestHeader Map<String, String> headerMap) throws Exception {
		return proxyService.putRequest(request, headerMap, okhttp3.MediaType.get(MediaType.APPLICATION_XML_VALUE));
	}

	@GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JsonNode> getJSON(@RequestParam Map<String, String> requestParams, @RequestHeader Map<String, String> headerMap) throws Exception {
		return proxyService.getRequest(requestParams, headerMap, okhttp3.MediaType.get(MediaType.APPLICATION_XML_VALUE));
	}

	@GetMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<JsonNode> getXML(@RequestParam Map<String, String> requestParams, @RequestHeader Map<String, String> headerMap) throws Exception {
		return proxyService.getRequest(requestParams, headerMap, okhttp3.MediaType.get(MediaType.APPLICATION_XML_VALUE));
	}

	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleException(HttpServletRequest httpRequest, Exception e) {
		LOG.error("Error while processing the request", e);
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonMap("error", e.getMessage()));
	}
}
