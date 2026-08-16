package com.extra.oms.core.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.ItemFulfilment;
import com.extra.oms.core.bean.UserInfo;
import com.extra.oms.core.service.FulfilmentService;

/**
 * @author aibrahim
 *
 */
@RestController
@RequestMapping(path = "/fulfilment")
public class FulfilmentController extends BaseController {

	@Autowired
	private FulfilmentService fulfilmentService;

	@GetMapping
	public List<ItemFulfilment> searchFulfilments(@RequestParam Map<String, Object> params) throws BaseException {
		return fulfilmentService.searchFulfilments(params);
	}

	@PutMapping
	public ItemFulfilment cancelFulfilment(@RequestBody ItemFulfilment fulfilment, @RequestAttribute(name = "user") UserInfo user) throws BaseException {
		return fulfilmentService.cancelFulfilmentReq(fulfilment, user);
	}

	@PostMapping
	public ItemFulfilment createFulfilment(@RequestBody ItemFulfilment fulfilment, @RequestParam(required = false, defaultValue = "false") boolean cancelAndCreate, @RequestAttribute(name = "user") UserInfo user) throws BaseException {
		return fulfilmentService.createWithCancelFulfilment(fulfilment, cancelAndCreate, user);
	}

	@GetMapping("/stock")
	public BigDecimal getStockAvailablity(ItemFulfilment fulfilment) throws BaseException {
		return fulfilmentService.getStockAvailablity(fulfilment);
	}
}
