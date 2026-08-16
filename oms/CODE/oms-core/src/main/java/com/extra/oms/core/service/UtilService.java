package com.extra.oms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.core.bean.Warehouse;
import com.extra.oms.core.dao.UtilDAO;

@Service
public class UtilService {

	@Autowired
	private UtilDAO utilDAO;

	public Map<Long, String> getStores() {
		return utilDAO.getStores();
	}

	public List<Warehouse> getWareHouses() {
		return utilDAO.getWareHouses();
	}

	public Map<Long, String> getSuppliers() {
		return utilDAO.getSuppliers();
	}

	public Map<Long, String> getVirtualStores() {
		return utilDAO.getVirtualStores();
	}
}
