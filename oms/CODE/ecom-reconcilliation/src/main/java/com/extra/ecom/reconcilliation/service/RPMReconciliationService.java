package com.extra.ecom.reconcilliation.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.ecom.reconcilliation.bean.Catelogue;
import com.extra.ecom.reconcilliation.bean.CatelogueResponse;
import com.extra.ecom.reconcilliation.bean.Response;
import com.extra.ecom.reconcilliation.dao.RPMReconiliationDAO;

/**
 * @author aibrahim
 *
 */
@Service
public class RPMReconciliationService {

	@Autowired
	private RPMReconiliationDAO reconiliationDAO;

	@Autowired
	private IReconciliationAPI reconciliationAPI;

	public void reConcileItem() {
		List<Collection<Catelogue>> catelogues = reconiliationDAO.getItemDetails();
		if (catelogues != null && !catelogues.isEmpty()) {
			for(Collection<Catelogue> subList : catelogues) {
				List<CatelogueResponse> catelogueProducts = reconciliationAPI.reConcileProduct(subList);
				reconiliationDAO.updateProductDetail(subList, catelogueProducts);
			}
		}
	}

	public void reConcilePrice() {
		List<Collection<Catelogue>> catelogues = reconiliationDAO.getPriceDetails();
		if (catelogues != null && !catelogues.isEmpty()) {
			for(Collection<Catelogue> subList : catelogues) {
				List<CatelogueResponse> catelogueProducts = reconciliationAPI.reConcilePrice(subList);
				reconiliationDAO.updatePriceDetail(subList, catelogueProducts);
			}
		}
	}

	public void reConcilePromotion() {
		List<Collection<Catelogue>> catelogues = reconiliationDAO.getPromotionDetails();
		if (catelogues != null && !catelogues.isEmpty()) {
			for(Collection<Catelogue> subList : catelogues) {
				List<CatelogueResponse> catelogueProducts = reconciliationAPI.reConcilePromotion(subList);
				reconiliationDAO.updatePromotionDetail(subList, catelogueProducts);
			}
		}
	}

	public void updateResponse(Response resp) {
		reconiliationDAO.updateResponse(resp);
	}
}
