package com.extra.oms.custOrder.model.bo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author aibrahim
 *
 */
public class BOStockAvailablityResource {

	private Map<String, BigDecimal> stockAvailablityMap;

	private Map<String, BigDecimal> maxFulfilOrdMap;

	private Map<String, String> sysParams;

	public BOStockAvailablityResource(Map<String, BigDecimal> stockAvailablityMap, Map<String, BigDecimal> maxFulfilOrdMap, Map<String, String> sysParams) {
		this.stockAvailablityMap = stockAvailablityMap;
		this.maxFulfilOrdMap = maxFulfilOrdMap;
		this.sysParams = sysParams;
	}

	public BigDecimal getAvailableSOH(String item, BigDecimal source, String sourceType, BigDecimal requestedQty) {
		String itemLOCKey = (item + "-" + sourceType + "-" + source.toPlainString()).intern();
		BigDecimal availableQty = BigDecimal.ZERO;
		synchronized (itemLOCKey) {
			BigDecimal totalSOH = stockAvailablityMap.get(itemLOCKey);
			if (totalSOH != null) {
				availableQty = totalSOH.min(requestedQty);
				stockAvailablityMap.put(itemLOCKey, totalSOH.subtract(availableQty));
			}
		}
		return availableQty;
	}

	public BigDecimal getMaxFulfilOrdNo(String custOrdNo) {
		BigDecimal maxFulOrdNo = maxFulfilOrdMap.get(custOrdNo);
		return maxFulOrdNo != null ? maxFulOrdNo : BigDecimal.ZERO;
	}

	public String getSystemParam(String optionName) {
		return sysParams.get(optionName);
	}
}
