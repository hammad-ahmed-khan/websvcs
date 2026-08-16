package com.logicinfo.oms.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logicinfo.oms.dao.DataAccessDAO;
import com.logicinfo.oms.ejb.ItemMaster;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.Wh;
import com.logicinfo.oms.model.CustOrdFulDesc;
import com.logicinfo.oms.model.CustOrdFulDescResponse;
import com.logicinfo.oms.model.CustOrdItmDesc;
import com.logicinfo.oms.model.CustOrdItmDescResponse;
import com.logicinfo.oms.model.InventoryCheck;
import com.logicinfo.oms.model.InventoryCheckResponse;
import com.logicinfo.oms.model.ItemClassification;
import com.logicinfo.oms.util.OmsSysParameterUtil;

/**
 * InventoryCheckService.java aibrahim 2024
 */
public class InventoryCheckService {

	private final static Logger _LOG = Logger.getLogger(InventoryCheckService.class.getName());

	private DataAccessDAO dataAccessDAO;

	public InventoryCheckService() {
		dataAccessDAO = new DataAccessDAO();
	}

	public InventoryCheckResponse getStockAvailability(InventoryCheck inventoryCheck) {

		InventoryCheckResponse response = new InventoryCheckResponse();
		response.setInitiateLocId(inventoryCheck.getInitiateLocId());
		Set<String> itemIds = new HashSet<String>();
		Set<String> sfsItemIds = new HashSet<String>();
		Set<String> shipCityIds = new HashSet<String>();
		Set<BigDecimal> initLocations = new HashSet<BigDecimal>();
		Set<String> linkedSKUs = null;
		boolean hasCfs = false;
		String countryCode = null;
		if (String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("1") || String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("8")) {
			countryCode = "SA";
		} else if (String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("2")) {
			countryCode = "BH";
		} else if (String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("3")) {
			countryCode = "OM";
		}
		Set<String> delvTypes = new HashSet<String>();
		for (CustOrdFulDesc fulDesc : inventoryCheck.getCustOrdFulDesc()) {
			_LOG.info("Processing delivery type " + fulDesc.getDeliveryType() + " and ship city ->" + fulDesc.getShipCity());
			boolean sfs = false;
			if (fulDesc.getDeliveryType().equals("S")) {
				shipCityIds.add(fulDesc.getShipCity());
				sfs = true;
				initLocations.add(new BigDecimal(inventoryCheck.getInitiateLocId()));
			} else {
				initLocations.add(new BigDecimal(fulDesc.getPickLoc()));
				shipCityIds.add(fulDesc.getShipCity());
				sfs = true;
			}
			for (CustOrdItmDesc item : fulDesc.getCustOrdItmDesc()) {
				_LOG.info("Received Item " + item.getItemId() + " and line no " + item.getLineNo() + " and requested qty " + item.getRequestedQty());
				itemIds.add(item.getItemId());
				if (sfs) {
					sfsItemIds.add(item.getItemId());
				}
			}
			delvTypes.add(fulDesc.getDeliveryType());
		}
		Map<String, ItemMaster> itemMap = dataAccessDAO.getItemDeptInvInds(itemIds);
		linkedSKUs = dataAccessDAO.getLinkedClzSKUs(itemIds);
		Map<String, ItemClassification> itemClassificationMap = null;
		if (!shipCityIds.isEmpty()) {
			itemClassificationMap = dataAccessDAO.getShipClassification(inventoryCheck.getInitiateLocId(), new ArrayList<String>(sfsItemIds));
			_LOG.info("normal ship classification:" + itemClassificationMap);
		}
		itemClassificationMap = WallBracketItemShipClassfication(itemClassificationMap, new ArrayList<String>(sfsItemIds));
		boolean hasExpressDLV = false;
		Set<String> preOrdCities = new HashSet<String>();
		Set<String> preOrdItems = new HashSet<String>();
		Set<String> normalOrdCities = new HashSet<String>();
		Set<String> shipClzs = new HashSet<String>();
		for (CustOrdFulDesc fulDesc : inventoryCheck.getCustOrdFulDesc()) {
			if (fulDesc.getDeliveryType().equals("S") || fulDesc.getDeliveryType().equals("C")) {
				_LOG.info("classification:" + itemClassificationMap);
				for (CustOrdItmDesc item : fulDesc.getCustOrdItmDesc()) {
					_LOG.info("classification:" + itemClassificationMap);
					ItemClassification clz = itemClassificationMap.get(item.getItemId());
					String clazzification = getItemClazzification(clz.getClassification(), item.getItemId(), linkedSKUs, itemClassificationMap.values());
					shipClzs.add(clazzification);
					if ("SMALL".equals(clazzification) || "PRE ORDER SMALL".equals(clazzification)) {
						if ("Y".equals(fulDesc.getExpressDelv())) {
							hasExpressDLV = true;
						} else if (clz.isPreOrder()) {
							preOrdCities.add(fulDesc.getShipCity());
							preOrdItems.add(item.getItemId());
						} else {
							normalOrdCities.add(fulDesc.getShipCity());
						}
						_LOG.info("preOrdItems " + preOrdItems);
					} else if ("BIG".equals(clazzification) && fulDesc.getDeliveryType().equals("C")) {
						normalOrdCities.add(fulDesc.getShipCity());
					}
				}
			}
		}
		String expPromiseDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (hasExpressDLV) {
			Calendar date = Calendar.getInstance();
			Calendar cutOffTime = (Calendar) date.clone();
			cutOffTime.set(Calendar.HOUR_OF_DAY, 14);
			cutOffTime.set(Calendar.MINUTE, 0);
			cutOffTime.set(Calendar.SECOND, 0);
			cutOffTime.set(Calendar.MILLISECOND, 0);
			if (!date.before(cutOffTime)) {
				date.add(Calendar.DATE, 1);
			}
			expPromiseDate = sdf.format(date.getTime());
		}
		Map<String, Date> preOrdDlvDateMap = null;
		Map<String, Date> itemDlvDateMap = null;
		if (!preOrdItems.isEmpty()) {
			preOrdDlvDateMap = dataAccessDAO.getPreOrderItemsDlvDates(preOrdCities, preOrdItems, inventoryCheck.getInitiateLocId(), delvTypes);
		}
		if (!normalOrdCities.isEmpty()) {
			itemDlvDateMap = dataAccessDAO.getItemsDlvDates(normalOrdCities, inventoryCheck.getInitiateLocId(), delvTypes);
		}
		if (delvTypes.contains("C")) {
			itemDlvDateMap = dataAccessDAO.getCfsDlvDates(inventoryCheck.getInitiateLocId());
		}

		Set<BigDecimal> storeLocs = new HashSet<BigDecimal>();
		Set<BigDecimal> whLocs = new HashSet<BigDecimal>();
		Set<BigDecimal> suppLocs = new HashSet<BigDecimal>();
		Map<String, List<OmsFulfillMatrixExtDetail>> matrixMap = dataAccessDAO.getMatrixFulfilmentDetails(initLocations, shipClzs, shipCityIds, hasCfs);
		boolean hasSupplier = false;
		for (List<OmsFulfillMatrixExtDetail> matrixs : matrixMap.values()) {
			for (OmsFulfillMatrixExtDetail matrix : matrixs) {
				if ("ST".equals(matrix.getLocationType())) {
					storeLocs.add(matrix.getLocation());
				} else if ("WH".equals(matrix.getLocationType())) {
					whLocs.add(matrix.getLocation());
				} else if ("SU".equals(matrix.getLocationType())) {
					hasSupplier = true;
					suppLocs.add(matrix.getDeliveryFromLoc());
				}
			}
		}
		Map<String, Integer> stStkMap = null;
		Map<String, Integer> whStkMap = null;
		Map<String, Integer> preOrdStkMap = null;
		if (!storeLocs.isEmpty()) {
			stStkMap = dataAccessDAO.getStoreStocks(storeLocs, itemIds);
		}
		if (!whLocs.isEmpty()) {
			whStkMap = dataAccessDAO.getWarehouseStocks(whLocs, itemIds);
		}
		if (!preOrdItems.isEmpty()) {
			preOrdStkMap = dataAccessDAO.getPreOrdItemStocks(whLocs, storeLocs, preOrdItems);
		}

		List<String> boActiveItems = Collections.emptyList();
		if (hasSupplier) {
			List<BigDecimal> allLocs = new ArrayList<BigDecimal>();
			if (!whLocs.isEmpty()) {
				allLocs.addAll(whLocs);
			}
			if (!storeLocs.isEmpty()) {
				allLocs.addAll(storeLocs);
			}
			if (!allLocs.isEmpty()) {
				boActiveItems = dataAccessDAO.getBOItemStatus(itemIds, allLocs);
			}
		}

		String shipingChargeDept = OmsSysParameterUtil.getValue("OMS_SYSTEM_OPTION", "SHIPPING_CHARGE_DEPT");
		int stSmThrsld = 0, stBgThrsld = 0, whSmThrsld = 0, whBgThrsld = 0;
		String thrsld = OmsSysParameterUtil.getValue("ST_SMALL_POS_THRESHOLD", countryCode + "_ST_POS_SMALL");
		if (thrsld != null && !thrsld.isEmpty()) {
			stSmThrsld = Integer.parseInt(thrsld);
		}
		thrsld = OmsSysParameterUtil.getValue("ST_BIG_POS_THRESHOLD", countryCode + "_ST_POS_BIG");
		if (thrsld != null && !thrsld.isEmpty()) {
			stBgThrsld = Integer.parseInt(thrsld);
		}
		thrsld = OmsSysParameterUtil.getValue("WH_SMALL_POS_THRESHOLD", countryCode + "_WH_POS_SMALL");
		if (thrsld != null && !thrsld.isEmpty()) {
			whSmThrsld = Integer.parseInt(thrsld);
		}
		thrsld = OmsSysParameterUtil.getValue("WH_BIG_POS_THRESHOLD", countryCode + "_WH_POS_BIG");
		if (thrsld != null && !thrsld.isEmpty()) {
			whBgThrsld = Integer.parseInt(thrsld);
		}
		for (CustOrdFulDesc fulDesc : inventoryCheck.getCustOrdFulDesc()) {
			CustOrdFulDescResponse fulDescResp = new CustOrdFulDescResponse();
			fulDescResp.setDeliveryType(fulDesc.getDeliveryType());
			fulDescResp.setShipCity(fulDesc.getShipCity());
			Long initLoc = inventoryCheck.getInitiateLocId();
			if ("C".equals(fulDesc.getDeliveryType())) {
				fulDescResp.setPickLoc(fulDesc.getPickLoc());
				initLoc = fulDesc.getPickLoc();
			}
			for (CustOrdItmDesc item : fulDesc.getCustOrdItmDesc()) {
				CustOrdItmDescResponse itmDescRes = new CustOrdItmDescResponse();
				itmDescRes.setItemId(item.getItemId());
				itmDescRes.setLineNo(item.getLineNo());
				itmDescRes.setRequestedQty(item.getRequestedQty());
				itmDescRes.setLoc(initLoc);

				ItemClassification clz = itemClassificationMap.get(item.getItemId());
				ItemMaster itemMaster = itemMap.get(item.getItemId());
				String itmDept = itemMaster.getDept().toPlainString();
				if ((!shipingChargeDept.equals(itmDept) && "Y".equals(itemMaster.getInventory_Ind())) || (itmDept.equals("8801") /* For Gift Card Voucher */)) {
					String clazzification = getItemClazzification(clz.getClassification(), item.getItemId(), linkedSKUs, itemClassificationMap.values());
					_LOG.info("clz.isPreOrder() " + clz.isPreOrder() + "-" + "clazzification -" + clazzification);
					if ("S".equals(fulDesc.getDeliveryType()) && ("SMALL".equals(clazzification) || "PRE ORDER SMALL".equals(clazzification))) {
						if ("Y".equals(fulDesc.getExpressDelv())) {
							itmDescRes.setPromiseDeliveryDate(expPromiseDate);
						} else if (clz.isPreOrder()) {
							Date pDate = preOrdDlvDateMap.get(fulDesc.getShipCity() + "~" + item.getItemId() + "~" + fulDesc.getDeliveryType());
							_LOG.info("pDate " + pDate + "City_Item" + fulDesc.getShipCity() + "~" + item.getItemId() + "~" + fulDesc.getDeliveryType());
							if (pDate != null)
								itmDescRes.setPromiseDeliveryDate(sdf.format(pDate));
						} else {
							Date dDate = itemDlvDateMap.get(fulDesc.getShipCity() + "~" + fulDesc.getDeliveryType());
							_LOG.info("dDate: " + dDate + "City_DeliveryType" + fulDesc.getShipCity() + "~" + fulDesc.getDeliveryType());
							if (dDate != null)
								itmDescRes.setPromiseDeliveryDate(sdf.format(dDate));
						}
					}
					if ("C".equals(fulDesc.getDeliveryType())) {
						Date dDate = itemDlvDateMap.get(fulDesc.getShipCity() + "~" + fulDesc.getDeliveryType());
						_LOG.info("dDate: " + dDate + "City_DeliveryType" + fulDesc.getShipCity() + "~" + fulDesc.getDeliveryType());
						if (dDate != null)
							itmDescRes.setPromiseDeliveryDate(sdf.format(dDate));
					}
					String matrixKey = initLoc + "~" + clazzification + "~" + fulDesc.getDeliveryType() + ("S".equals(fulDesc.getDeliveryType()) ? ("~" + fulDesc.getShipCity()) : "");
					List<OmsFulfillMatrixExtDetail> matrixs = matrixMap.get(matrixKey);
					if (matrixs == null && "S".equals(fulDesc.getDeliveryType())) {
						matrixKey = initLoc + "~" + clazzification + "~" + fulDesc.getDeliveryType() + "~ALL";
						matrixs = matrixMap.get(matrixKey);
					}
					boolean isSuplierFulfil = false;
					if (matrixs != null) {
						for (OmsFulfillMatrixExtDetail matrixDetail : matrixs) {
							Integer stOnLoc = 0;
							Long loc = matrixDetail.getLocation().longValue();
							String locKey = item.getItemId() + "~" + loc;
							int thrsldQty = 0;
							if ("WH".equals(matrixDetail.getLocationType())) {
								stOnLoc = whStkMap.get(locKey);
								if ("SMALL".equals(clazzification)) {
									thrsldQty = whSmThrsld;
								} else {
									thrsldQty = whBgThrsld;
								}
								if (stOnLoc != null) {
									whStkMap.put(locKey, Math.max(stOnLoc - item.getRequestedQty().intValue(), 0));
									stOnLoc = Math.max(stOnLoc - thrsldQty, 0);
								} else {
									stOnLoc = 0;
								}
							} else if ("ST".equals(matrixDetail.getLocationType())) {
								stOnLoc = stStkMap.get(locKey);
								if ("SMALL".equals(clazzification)) {
									thrsldQty = stSmThrsld;
								} else {
									thrsldQty = stBgThrsld;
								}
								if (stOnLoc != null) {
									stStkMap.put(locKey, Math.max(stOnLoc - item.getRequestedQty().intValue(), 0));
									stOnLoc = Math.max(stOnLoc - thrsldQty, 0);
								} else {
									stOnLoc = 0;
								}
							} else if ("SU".equals(matrixDetail.getLocationType())) {
								isSuplierFulfil = true;
							}
							if (BigDecimal.ONE.equals(matrixDetail.getPriority())) {
								itmDescRes.setStockOnHandQty(new BigDecimal(stOnLoc));
								itmDescRes.setAvailableQty(BigDecimal.ZERO);
							} else {
								itmDescRes.setAvailableQty((itmDescRes.getAvailableQty() == null ? BigDecimal.ZERO : itmDescRes.getAvailableQty()).add(new BigDecimal(stOnLoc)));
							}
							_LOG.info("preOrdStkMap " + preOrdStkMap);
							if (clz.isPreOrder() && preOrdStkMap != null && !preOrdStkMap.isEmpty()) {
								Integer preOrdStk = preOrdStkMap.get(locKey);
								_LOG.info("Inside if preorder item - preOrdStk= " + preOrdStk);
								if (preOrdStk != null && preOrdStk > 0) {
									preOrdStkMap.put(locKey, Math.max(preOrdStk - item.getRequestedQty().intValue(), 0));
									itmDescRes.setFutAvlQuantity(
											(itmDescRes.getFutAvlQuantity() == null ? BigDecimal.ZERO : itmDescRes.getFutAvlQuantity()).add(new BigDecimal(Math.max(preOrdStk - thrsldQty, 0))));
									itmDescRes.setInTransitQty(BigDecimal.ONE);
								}
							}
							_LOG.info("preOrdStk " + preOrdStkMap);

							_LOG.debug("Item " + item.getItemId() + "Priority " + String.format("%02f", matrixDetail.getPriority()) + " Location " + String.format("%05d", loc) + " ThrsldQty "
									+ thrsldQty + " Stock after thrsld " + String.format("%03d", stOnLoc) + " Total Available " + String.format("%04f", itmDescRes.getAvailableQty()));
						}
						if (isSuplierFulfil) {
							itmDescRes.setAvailableQty(null);
							BigDecimal futAvlQty = BigDecimal.ZERO;
							Set<BigDecimal> whs = new HashSet<BigDecimal>();
							Set<BigDecimal> stores = new HashSet<BigDecimal>();
							for (OmsFulfillMatrixExtDetail matrixDetail : matrixs) {
								if (boActiveItems.contains(item.getItemId() + "~" + matrixDetail.getLocation())) {
									if ("WH".equals(matrixDetail.getLocationType())) {
										whs.add(matrixDetail.getLocation());
									} else if ("ST".equals(matrixDetail.getLocationType())) {
										stores.add(matrixDetail.getLocation());
									}
								}
							}
							List<Wh> whChIds = Collections.emptyList();
							if (!whs.isEmpty()) {
								whChIds = dataAccessDAO.getWhsWithChannelIds(whs);
							}
							Map<String, Integer> stPOStkMap = Collections.emptyMap();
							Map<String, Integer> whPOStkMap = Collections.emptyMap();
							Map<String, Integer> boAllocQtyMap = Collections.emptyMap();
							if (!whChIds.isEmpty()) {
								whPOStkMap = dataAccessDAO.getWHPOStocks(item.getItemId(), whChIds);
							}
							if (!stores.isEmpty()) {
								stPOStkMap = dataAccessDAO.getStorePOStocks(item.getItemId(), stores);
							}

							List<BigDecimal> allLocs = new ArrayList<BigDecimal>();
							if (!whs.isEmpty()) {
								allLocs.addAll(whs);
							}
							if (!stores.isEmpty()) {
								allLocs.addAll(stores);
							}
							if (!allLocs.isEmpty()) {
								boAllocQtyMap = dataAccessDAO.getBOAllocatedQty(item.getItemId(), allLocs);
							}

							if (!allLocs.isEmpty()) {
								Integer futLocQty = 0;
								int thrsldQty = 0;
								for (OmsFulfillMatrixExtDetail matrixDetail : matrixs) {
									String locKey = item.getItemId() + "~" + matrixDetail.getLocation();
									Integer boQty = boAllocQtyMap.get(locKey);
									if (boQty == null) {
										boQty = 0;
									}
									if ("WH".equals(matrixDetail.getLocationType())) {
										futLocQty = whPOStkMap.get(locKey);
										if ("SMALL".equals(clazzification)) {
											thrsldQty = whSmThrsld;
										} else {
											thrsldQty = whBgThrsld;
										}
										if (futLocQty != null) {
											whPOStkMap.put(locKey, Math.max(futLocQty - item.getRequestedQty().intValue(), 0));
											futLocQty = Math.max(futLocQty - boQty - thrsldQty, 0);
										} else {
											futLocQty = 0;
										}
									} else if ("ST".equals(matrixDetail.getLocationType())) {
										futLocQty = stPOStkMap.get(locKey);
										if ("SMALL".equals(clazzification)) {
											thrsldQty = stSmThrsld;
										} else {
											thrsldQty = stBgThrsld;
										}
										if (futLocQty != null) {
											stPOStkMap.put(locKey, Math.max(futLocQty - item.getRequestedQty().intValue(), 0));
											futLocQty = Math.max(futLocQty - boQty - thrsldQty, 0);
										} else {
											futLocQty = 0;
										}
									}
									if (futLocQty > 0) {
										itmDescRes.setInTransitQty(BigDecimal.ZERO);
									}
									futAvlQty = futAvlQty.add(new BigDecimal(futLocQty));
								}
								if (futAvlQty.intValue() < item.getRequestedQty().intValue()) {
									futAvlQty = item.getRequestedQty();
								}
							} else {
								futAvlQty = item.getRequestedQty();
							}
							itmDescRes.setFutAvlQuantity(futAvlQty);
						}
					}
				} else {
					itmDescRes.setItemId(item.getItemId());
					itmDescRes.setLineNo(item.getLineNo());
					itmDescRes.setRequestedQty(item.getRequestedQty());
					itmDescRes.setAvailableQty(BigDecimal.ZERO);
					itmDescRes.setStockOnHandQty(item.getRequestedQty());
				}
				fulDescResp.getCustOrdItmDescResponse().add(itmDescRes);
			}
			response.getCustOrdFulDescResponse().add(fulDescResp);
		}
		_LOG.info("ORPOS Inventory Check completed..");
		return response;
	}

	private String getItemClazzification(String classification, String itemId, Set<String> linkedSKUs, Collection<ItemClassification> clazzfications) {
		if (!linkedSKUs.contains(itemId)) {
			return classification;
		}
		for (ItemClassification clz : clazzfications) {
			if (!linkedSKUs.contains(clz.getItemId())) {
				return clz.getClassification();
			}
		}
		return classification;
	}

	public Map<String, ItemClassification> WallBracketItemShipClassfication(Map<String, ItemClassification> classificationMap, List<String> items) {

		String normalItemClass = null;

		// Step 1: Get wall bracket items
		Set<String> wallBracketSet = dataAccessDAO.getWallBracketItems(items);

		// Step 2: Find first normal item classification
		for (String item : items) {
			if (!wallBracketSet.contains(item) && classificationMap.get(item) != null) {
				normalItemClass = classificationMap.get(item).getClassification();
				break;
			}
		}

		// Step 3: Apply to ALL wall bracket items
		if (normalItemClass != null) {

			for (String item : items) {

				if (wallBracketSet.contains(item)) {

					ItemClassification wallBracket = classificationMap.get(item);

					if (wallBracket != null) {
						wallBracket.setClassification(normalItemClass);

						_LOG.info("Wall bracket updated → " + item + " with classification → " + normalItemClass);
					}
				}
			}
		}

		return classificationMap;
	}
}
