package com.extra.oms.discount.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.discount.dao.DiscountDAO;
import com.extra.oms.discount.model.DiscountInfo;

/**
 * @author aibrahim
 *
 */
@Service
public class DiscountService {

	@Autowired
	private DiscountDAO discountDAO;

	public void saveDiscountInfo(DiscountInfo discount) {
		discountDAO.saveDiscountInfo(discount);
	}
}
