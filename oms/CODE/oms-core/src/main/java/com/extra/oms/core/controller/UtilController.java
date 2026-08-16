/**
 * 
 */
package com.extra.oms.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.Warehouse;
import com.extra.oms.core.service.UtilService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/util")
public class UtilController extends BaseController {

	@Autowired
	private UtilService utilService;

	@GetMapping(path = "/store")
	Map<Long, String> getStores() throws BaseException {
		return utilService.getStores();
	}

	@GetMapping(path = "/wh")
	List<Warehouse> getWareHouses() throws BaseException {
		return utilService.getWareHouses();
	}

	@GetMapping(path = "/supplier")
	Map<Long, String> getSuppliers() throws BaseException {
		return utilService.getSuppliers();
	}

	@GetMapping(path = "/virtual")
	Map<Long, String> getVirtualStores() throws BaseException {
		return utilService.getVirtualStores();
	}
}
