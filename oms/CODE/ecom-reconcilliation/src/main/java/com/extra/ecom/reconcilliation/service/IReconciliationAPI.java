package com.extra.ecom.reconcilliation.service;

import java.util.Collection;
import java.util.List;

import com.extra.ecom.reconcilliation.bean.Catelogue;
import com.extra.ecom.reconcilliation.bean.CatelogueResponse;

import feign.Headers;
import feign.RequestLine;

/**
 * @author aibrahim
 *
 */
@Headers({ "Content-Type: application/json" })
public interface IReconciliationAPI {

	@RequestLine("POST /product")
	public List<CatelogueResponse> reConcileProduct(Collection<Catelogue> catelogues);

	@RequestLine("POST /price")
	public List<CatelogueResponse> reConcilePrice(Collection<Catelogue> catelogues);

	@RequestLine("POST /promotion")
	public List<CatelogueResponse> reConcilePromotion(Collection<Catelogue> catelogues);
}
