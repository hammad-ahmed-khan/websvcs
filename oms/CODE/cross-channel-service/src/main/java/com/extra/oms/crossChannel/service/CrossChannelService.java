package com.extra.oms.crossChannel.service;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.crossChannel.dao.CrossChannelDAO;
import com.extra.oms.crossChannel.model.CrossChannelRequest;
import com.extra.oms.crossChannel.model.CrossChannelResponse;
import com.extra.oms.crossChannel.model.ServiceOfferedRequest;
import com.extra.oms.crossChannel.model.ServiceOfferedResponse;

@Service
public class CrossChannelService {

	private static final Logger LOG = LogManager.getLogger(CrossChannelService.class);

	@Autowired
	private CrossChannelDAO crossChannelDAO;

	public List<CrossChannelResponse> processCrossChannel(CrossChannelRequest request) {
		return crossChannelDAO.getOfflineOrders(request);
	}

	public List<ServiceOfferedResponse> processCrossChannelServicesoffered(ServiceOfferedRequest request) {
		return crossChannelDAO.getServicesOffered(request);
	}
}