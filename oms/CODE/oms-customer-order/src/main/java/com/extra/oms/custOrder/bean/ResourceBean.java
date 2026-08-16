package com.extra.oms.custOrder.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.extra.oms.custOrder.dao.BackOrderDAO;
import com.extra.oms.custOrder.model.bo.InventoryBackOrderService;
import com.extra.oms.service.client.ICarreraClient;
import com.extra.oms.service.client.IOracleRMSClient;
import com.extra.oms.service.client.IOracleSIMClient;

/**
 * @author aibrahim
 *
 */
@Component
public class ResourceBean {

	@Autowired
	private BackOrderDAO backOrderDAO;

	@Autowired
	private ICarreraClient carreraClient;

	@Autowired
	private IOracleRMSClient oracleRMSClient;

	@Autowired
	private IOracleSIMClient oracleSIMClient;

	@Autowired
	private InventoryBackOrderService inventoryBackOrderService; 

	public BackOrderDAO getBackOrderDAO() {
		return backOrderDAO;
	}

	public ICarreraClient getCarreraClient() {
		return carreraClient;
	}

	public IOracleRMSClient getOracleRMSClient() {
		return oracleRMSClient;
	}

	public IOracleSIMClient getOracleSIMClient() {
		return oracleSIMClient;
	}

	public InventoryBackOrderService getInventoryBackOrderService() {
		return inventoryBackOrderService;
	}
}
