package com.extra.oms.custOrder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.common.exception.BaseException;
import com.extra.common.model.OmsCustOrdHead;
import com.extra.oms.custOrder.bean.ResourceBean;
import com.extra.oms.custOrder.dao.BackOrderDAO;
import com.extra.oms.custOrder.model.bo.BOStockAvailablityResource;

/**
 * @author aibrahim
 *
 */
@Service
public class BackOrderService {

	private static final Logger LOG = Logger.getLogger(BackOrderService.class);

	@Autowired
	private BackOrderDAO backOrderDAO;

	@Autowired
	private ThreadPoolExecutor backOrderExecutor;

	@Autowired
	private ResourceBean resourceBean;

	public void processBackOrders() throws BaseException {
		Long seqId = null;
		String status = "FAILED";
		try {
			seqId = backOrderDAO.aquireLock();
			Map<String, BigDecimal> stockAvailablityMap = backOrderDAO.getStockAvailablity();
			if (stockAvailablityMap != null && !stockAvailablityMap.isEmpty()) {
				Map<String, BigDecimal> maxFulfilOrdMap = backOrderDAO.getMaxFulfilOrderNo();
				Map<String, String> sysParams = backOrderDAO.getSystemParams();
				BOStockAvailablityResource availablityResource = new BOStockAvailablityResource(stockAvailablityMap, maxFulfilOrdMap, sysParams);
				List<OmsCustOrdHead> boOrders = backOrderDAO.getBOOrders(new HashMap<>(stockAvailablityMap));
				LOG.info("No. of back order available to process is " + boOrders.size());
				resourceBean.getInventoryBackOrderService().clearInvBackOrd();
				List<BackOrderProcess> boProcesses = new ArrayList<>(boOrders.size());
				backOrderExecutor.setCorePoolSize(Math.min(boOrders.size(), 20));
				for (OmsCustOrdHead boOrder : boOrders) {
					boProcesses.add(new BackOrderProcess(boOrder, availablityResource, resourceBean));
				}
				backOrderExecutor.invokeAll(boProcesses);
				resourceBean.getInventoryBackOrderService().processInvBackOrds();
				backOrderExecutor.setCorePoolSize(0);
			} else {
				LOG.warn("Stocts are not avaiable to process the back orders");
			}
			status = "COMPLETED";
		} catch (BaseException e) {
			LOG.error("Error while processing the back orders", e);
			throw e;
		} catch (Exception e) {
			LOG.error("Error while processing the back orders", e);
			throw new BaseException(e.getMessage());
		} finally {
			backOrderDAO.releaseLock(seqId, status);
		}
	}
}
