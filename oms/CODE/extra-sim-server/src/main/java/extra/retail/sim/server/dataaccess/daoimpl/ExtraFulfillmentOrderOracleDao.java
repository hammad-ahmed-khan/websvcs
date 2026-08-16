package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.retail.sim.common.business.LocationType;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.currency.SimMoneyUtility;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryType;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;
import oracle.retail.sim.server.dataaccess.DAOFactory;
import oracle.retail.sim.server.dataaccess.QueryLockWaitTime;
import oracle.retail.sim.server.dataaccess.ResultSetReader;
import oracle.retail.sim.server.dataaccess.databean.custom.FulfillmentOrderLineItemCountCustomDataBean;
import oracle.retail.sim.server.dataaccess.databean.generated.FulOrdDataBean;

import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;
import extra.retail.sim.server.dataaccess.dao.ExtraFulfillmentOrderDao;
import extra.retail.sim.server.dataaccess.databean.generated.ExtraFulOrdDataBean;
import extra.retail.sim.server.dataaccess.databean.generated.ExtraFulOrdLineItemDataBean;

public class ExtraFulfillmentOrderOracleDao extends BaseOracleDao implements ExtraFulfillmentOrderDao {

	private QueryLockWaitTime lockWait = new QueryLockWaitTime();

	@Override
	public List<ExtraFulfillmentOrderVO> selectFulfillmentOrders(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter) throws SimServerException {
		List<Object> arrayList = new ArrayList<Object>();
		String str = buildFilterSql(paramFulfillmentOrderQueryFilter, arrayList);
		List<ExtraFulOrdDataBean> list = query(new ExtraFulOrdDataBean(), str, arrayList);

		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		List<ExtraFulfillmentOrderVO> fulfilOrderVOs = new ArrayList<ExtraFulfillmentOrderVO>();
		Map<Long, Integer> map = selectLineItemCounts(paramFulfillmentOrderQueryFilter);
		for (ExtraFulOrdDataBean fulOrdDataBean : list) {
			fulfilOrderVOs.add(fromBeanToValueObject(fulOrdDataBean, map));
		}
		return fulfilOrderVOs;
	}

	private String buildFilterSql(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter, List<Object> paramList) {
		String str = buildWhereClause(paramFulfillmentOrderQueryFilter, paramList);
		return StringHelper.isNullOrEmpty(str) ? ExtraFulOrdDataBean.SELECT_SQL.substring(0, ExtraFulOrdDataBean.SELECT_SQL.length() - 6) : (ExtraFulOrdDataBean.SELECT_SQL + str);
	}

	private String buildWhereClause(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter, List<Object> paramList) {
		StringBuilder stringBuilder = new StringBuilder();
		if (paramFulfillmentOrderQueryFilter.getTrackingId() != null) {
			stringBuilder.append("( ").append("FUL_ORD.ID").append(" IN (SELECT ").append("FUL_ORD_DLV.FUL_ORD_ID");
			stringBuilder.append(" FROM ").append("FUL_ORD_DLV").append(" , ").append("SHIPMENT_BOL");
			stringBuilder.append(" WHERE ").append("SHIPMENT_BOL.TRACKING_NUMBER").append(" LIKE ? AND ");
			stringBuilder.append("SHIPMENT_BOL.ID").append(" =  ").append("FUL_ORD_DLV.SHIPMENT_BOL_ID").append(")) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getTrackingId());
		}
		if (paramFulfillmentOrderQueryFilter.getCustomerName() != null) {
			stringBuilder.append("( ").append("FUL_ORD.ID").append(" IN ( SELECT ").append("ADDRESS.ENTITY_ID");
			stringBuilder.append(" FROM ").append("ADDRESS").append(" WHERE ").append("ADDRESS.ENTITY_TYPE").append(" = ? AND ( ");
			stringBuilder.append("ADDRESS.CONTACT_NAME").append(" LIKE ? ) OR ( ");
			stringBuilder.append("ADDRESS.FIRST_NAME").append(" LIKE ? ) OR ( ");
			stringBuilder.append("ADDRESS.LAST_NAME").append(" LIKE ? ))) AND ");
			paramList.add(LocationType.FULFILLMENT_ORDER.getCode());
			paramList.add(paramFulfillmentOrderQueryFilter.getCustomerName());
			paramList.add(paramFulfillmentOrderQueryFilter.getCustomerName());
			paramList.add(paramFulfillmentOrderQueryFilter.getCustomerName());
		}
		if (paramFulfillmentOrderQueryFilter.getFulfillmentOrderExternalId() != null) {
			stringBuilder.append("( ").append("FUL_ORD.EXTERNAL_ID").append(" = ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getFulfillmentOrderExternalId());
		}
		if (paramFulfillmentOrderQueryFilter.getBinId() != null) {
			stringBuilder.append("( ").append("FUL_ORD.ID").append(" IN ( ").append("SELECT ");
			stringBuilder.append("FUL_ORD_BIN.FUL_ORD_ID").append(" FROM ").append("FUL_ORD_BIN");
			stringBuilder.append(" WHERE ").append("FUL_ORD_BIN.BIN_ID").append(" = ? )) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getBinId());
		}
		if (paramFulfillmentOrderQueryFilter.getFromDate() != null) {
			stringBuilder.append("( ").append("FUL_ORD.RELEASE_DATE").append(" >= ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getFromDate());
		}
		if (paramFulfillmentOrderQueryFilter.getToDate() != null) {
			stringBuilder.append("( ").append("FUL_ORD.RELEASE_DATE").append(" <= ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getToDate());
		}
		if (paramFulfillmentOrderQueryFilter.getFulfillmentOrderId() != null) {
			stringBuilder.append("( ").append("FUL_ORD.ID").append(" = ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getFulfillmentOrderId());
		}
		if (paramFulfillmentOrderQueryFilter.getCustomerOrderId() != null) {
			stringBuilder.append("( ").append("FUL_ORD.CUST_ORDER_ID").append(" = ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getCustomerOrderId());
		}
		if (paramFulfillmentOrderQueryFilter.getStoreId() != null) {
			stringBuilder.append("( ").append("FUL_ORD.STORE_ID").append(" = ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getStoreId());
		}
		if (paramFulfillmentOrderQueryFilter.getStatus() != null)
			if (paramFulfillmentOrderQueryFilter.getStatus() == FulfillmentOrderStatus.ACTIVE) {
				stringBuilder.append("( ").append("FUL_ORD.STATUS").append(" = ? OR ").append("FUL_ORD.STATUS").append(" = ? ) AND ");
				paramList.add(FulfillmentOrderStatus.NEW.getCode());
				paramList.add(FulfillmentOrderStatus.IN_PROGRESS.getCode());
			} else {
				stringBuilder.append("( ").append("FUL_ORD.STATUS").append(" = ?) AND ");
				paramList.add(paramFulfillmentOrderQueryFilter.getStatus().getCode());
			}
		if (paramFulfillmentOrderQueryFilter.getOrderType() != null) {
			stringBuilder.append("( ").append("FUL_ORD.ORDER_TYPE").append(" = ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getOrderType().getCode());
		}
		if (paramFulfillmentOrderQueryFilter.getItemId() != null) {
			stringBuilder.append(" ( EXISTS (SELECT 1 FROM ").append("FUL_ORD_LINE_ITEM");
			stringBuilder.append("   WHERE ").append("FUL_ORD_LINE_ITEM.ITEM_ID").append(" = ? ");
			paramList.add(paramFulfillmentOrderQueryFilter.getItemId());
			stringBuilder.append("   AND ").append("FUL_ORD_LINE_ITEM.FUL_ORD_ID");
			stringBuilder.append("   = ").append("FUL_ORD.ID").append(")) AND ");
		}
		if (paramFulfillmentOrderQueryFilter.getDeliveryMode() != null) {
			stringBuilder.append("( ").append("FUL_ORD.DELIVERY_MODE").append(" = ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getDeliveryMode());
		}
		if (paramFulfillmentOrderQueryFilter.getDeliverySlot() != null) {
			stringBuilder.append("( ").append("FUL_ORD.DELIVERY_SLOT").append(" = ?) AND ");
			paramList.add(paramFulfillmentOrderQueryFilter.getDeliverySlot());
		}
		return (stringBuilder.length() > 0) ? (" WHERE " + stringBuilder.toString().substring(0, stringBuilder.length() - 4)) : "";
	}

	private Map<Long, Integer> selectLineItemCounts(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter) throws SimServerException {
		List<Object> arrayList = new ArrayList<Object>();
		StringBuilder stringBuilder = new StringBuilder(FulfillmentOrderLineItemCountCustomDataBean.SQL_SELECT);
		stringBuilder.append(", ").append(ExtraFulOrdDataBean.TABLE_NAME);
		stringBuilder.append(buildWhereClause(paramFulfillmentOrderQueryFilter, arrayList));
		stringBuilder.append(" AND ").append(FulOrdDataBean.COL_ID);
		stringBuilder.append(" = ").append(FulfillmentOrderLineItemCountCustomDataBean.COL_FUL_ORD_ID);
		stringBuilder.append(FulfillmentOrderLineItemCountCustomDataBean.SQL_GROUP);
		List<FulfillmentOrderLineItemCountCustomDataBean> list = query((ResultSetReader<FulfillmentOrderLineItemCountCustomDataBean>) new FulfillmentOrderLineItemCountCustomDataBean(),
				stringBuilder.toString(), arrayList);
		Map<Long, Integer> hashMap = new HashMap<>(list.size());
		for (FulfillmentOrderLineItemCountCustomDataBean fulfillmentOrderLineItemCountCustomDataBean : list)
			hashMap.put(fulfillmentOrderLineItemCountCustomDataBean.getId(), fulfillmentOrderLineItemCountCustomDataBean.getLineItemCount());
		return hashMap;
	}

	private ExtraFulfillmentOrderVO fromBeanToValueObject(ExtraFulOrdDataBean paramFulOrdDataBean, Map<Long, Integer> paramMap) throws SimServerException {
		ExtraFulfillmentOrderVO fulfillmentOrderVO = ExtraBOFactory.createFulfillmentOrderVO();
		fulfillmentOrderVO.doSetId(paramFulOrdDataBean.getId());
		fulfillmentOrderVO.doSetCustomerOrderId(paramFulOrdDataBean.getCustOrderId());
		fulfillmentOrderVO.doSetStoreId(paramFulOrdDataBean.getStoreId());
		fulfillmentOrderVO.doSetExternalId(paramFulOrdDataBean.getExternalId());
		fulfillmentOrderVO.doSetOrderType(FulfillmentOrderType.toValue(Integer.valueOf(paramFulOrdDataBean.getOrderType().intValue())));
		fulfillmentOrderVO.doSetComments(paramFulOrdDataBean.getComments());
		fulfillmentOrderVO.doSetStatus(FulfillmentOrderStatus.toValue(Integer.valueOf(paramFulOrdDataBean.getStatus().intValue())));
		fulfillmentOrderVO.doSetCreateDate(paramFulOrdDataBean.getCreateDate());
		fulfillmentOrderVO.doSetReleaseDate(paramFulOrdDataBean.getReleaseDate());
		if (paramMap != null)
			fulfillmentOrderVO.doSetLineItemsCount(((Integer) paramMap.get(fulfillmentOrderVO.getId())).intValue());
		fulfillmentOrderVO.doSetDeliveryCarrier(DAOFactory.getShipmentDao().selectCarrier(paramFulOrdDataBean.getShipCarrierId()));
		fulfillmentOrderVO.doSetDeliveryService(DAOFactory.getShipmentDao().selectCarrierService(paramFulOrdDataBean.getShipCarrierServiceId()));
		fulfillmentOrderVO.doSetDeliveryType(FulfillmentOrderDeliveryType.toValue(Integer.valueOf(paramFulOrdDataBean.getDeliveryType().intValue())));
		fulfillmentOrderVO.doSetAllowPartialDelivery(ynStringToBoolean(paramFulOrdDataBean.getAllowPartialDelivery()));
		fulfillmentOrderVO.doSetDeliveryMode(paramFulOrdDataBean.getDeliveryMode());
		fulfillmentOrderVO.doSetDeliverySlot(paramFulOrdDataBean.getDeliverySlot());
		return fulfillmentOrderVO;
	}

	@Override
	public ExtraFulfillmentOrder selectFulfillmentOrder(Long paramLong) throws SimServerException {

		return selectFulfillmentOrder(paramLong, false);
	}

	private ExtraFulfillmentOrder selectFulfillmentOrder(Long paramLong, boolean paramBoolean) throws SimServerException {
		StringBuilder stringBuilder = new StringBuilder(ExtraFulOrdDataBean.SELECT_SQL);
		stringBuilder.append(where("FUL_ORD.ID"));
		if (paramBoolean)
			stringBuilder.append(this.lockWait.getLockWaitString());
		ExtraFulOrdDataBean fulOrdDataBean = (ExtraFulOrdDataBean) querySingle((ResultSetReader<ExtraFulOrdDataBean>) new ExtraFulOrdDataBean(), stringBuilder.toString(), Collections.singletonList(paramLong));
		return (fulOrdDataBean != null) ? fromBeanToObject(fulOrdDataBean) : null;
	}

	private ExtraFulfillmentOrder fromBeanToObject(ExtraFulOrdDataBean paramFulOrdDataBean) throws SimServerException {
		ExtraFulfillmentOrder fulfillmentOrder = ExtraBOFactory.createFulfillmentOrder();
		fulfillmentOrder.doSetId(paramFulOrdDataBean.getId());
		fulfillmentOrder.doSetCustomerOrderId(paramFulOrdDataBean.getCustOrderId());
		fulfillmentOrder.doSetStoreId(paramFulOrdDataBean.getStoreId());
		fulfillmentOrder.doSetExternalId(paramFulOrdDataBean.getExternalId());
		fulfillmentOrder.doSetOrderType(FulfillmentOrderType.toValue(Integer.valueOf(paramFulOrdDataBean.getOrderType().intValue())));
		fulfillmentOrder.doSetComments(paramFulOrdDataBean.getComments());
		fulfillmentOrder.doSetTransactionTimestamp(paramFulOrdDataBean.getCustOrderDate());
		fulfillmentOrder.doSetStatus(FulfillmentOrderStatus.toValue(Integer.valueOf(paramFulOrdDataBean.getStatus().intValue())));
		fulfillmentOrder.doSetCreateDate(paramFulOrdDataBean.getCreateDate());
		fulfillmentOrder.doSetUpdateDate(paramFulOrdDataBean.getUpdateDate());
		fulfillmentOrder.doSetReleaseDate(paramFulOrdDataBean.getReleaseDate());
		fulfillmentOrder.doSetDeliveryDate(paramFulOrdDataBean.getDeliveryDate());
		fulfillmentOrder.doSetDeliveryCarrier(DAOFactory.getShipmentDao().selectCarrier(paramFulOrdDataBean.getShipCarrierId()));
		fulfillmentOrder.doSetCustomerId(paramFulOrdDataBean.getCustomerId());
		fulfillmentOrder.doSetDeliveryCharge(SimMoneyUtility.convertToSimMoney(paramFulOrdDataBean.getShipCostCurrency(), paramFulOrdDataBean.getShipCostValue()));
		fulfillmentOrder.doSetDeliveryService(DAOFactory.getShipmentDao().selectCarrierService(paramFulOrdDataBean.getShipCarrierServiceId()));
		fulfillmentOrder.doSetDeliveryType(FulfillmentOrderDeliveryType.toValue(Integer.valueOf(paramFulOrdDataBean.getDeliveryType().intValue())));
		fulfillmentOrder.doSetAllowPartialDelivery(ynStringToBoolean(paramFulOrdDataBean.getAllowPartialDelivery()));
		addFulfillmentOrderLineItems(fulfillmentOrder);
		return fulfillmentOrder;
	}

	private void addFulfillmentOrderLineItems(ExtraFulfillmentOrder paramFulfillmentOrder) throws SimServerException {
		String str = ExtraFulOrdLineItemDataBean.SELECT_SQL + where("FUL_ORD_LINE_ITEM.FUL_ORD_ID");
		List<ExtraFulOrdLineItemDataBean> list = query((ResultSetReader<ExtraFulOrdLineItemDataBean>) new ExtraFulOrdLineItemDataBean(), str, Collections.singletonList(paramFulfillmentOrder.getId()));
		ArrayList<ExtraFulfillmentOrderLineItem> arrayList = new ArrayList<>();
		ArrayList<String> arrayList1 = new ArrayList<>();
		if (!list.isEmpty()) {
			for (ExtraFulOrdLineItemDataBean fulOrdLineItemDataBean : list) {
				arrayList1.add(fulOrdLineItemDataBean.getItemId());
			}
			Map<String, StockItem> map = DAOFactory.getStockItemDao().selectStockItems(arrayList1, paramFulfillmentOrder.getStoreId());
			for (ExtraFulOrdLineItemDataBean fulOrdLineItemDataBean : list) {
				arrayList.add(fromBeanToObject(fulOrdLineItemDataBean, (StockItem) map.get(fulOrdLineItemDataBean.getItemId())));
			}
		}
		paramFulfillmentOrder.doSetLineItems(arrayList);
	}

	private ExtraFulfillmentOrderLineItem fromBeanToObject(ExtraFulOrdLineItemDataBean paramFulOrdLineItemDataBean, StockItem paramStockItem) {
		ExtraFulfillmentOrderLineItem fulfillmentOrderLineItem = ExtraBOFactory.createFulfillmentOrderLineItem(paramStockItem);
		fulfillmentOrderLineItem.doSetId(paramFulOrdLineItemDataBean.getId());
		fulfillmentOrderLineItem.doSetCanceledQuantity(new Quantity(paramFulOrdLineItemDataBean.getQuantityCanceled().doubleValue()));
		fulfillmentOrderLineItem.doSetDeliveredQuantity(new Quantity(paramFulOrdLineItemDataBean.getQuantityDelivered().doubleValue()));
		fulfillmentOrderLineItem.doSetOrderedQuantity(new Quantity(paramFulOrdLineItemDataBean.getQuantityOrdered().doubleValue()));
		fulfillmentOrderLineItem.doSetPickedQuantity(new Quantity(paramFulOrdLineItemDataBean.getQuantityPicked().doubleValue()));
		fulfillmentOrderLineItem.doSetReservedQuantity(new Quantity(paramFulOrdLineItemDataBean.getQuantityReserved().doubleValue()));
		fulfillmentOrderLineItem.doSetPreferredUom(paramFulOrdLineItemDataBean.getPreferredUom());
		fulfillmentOrderLineItem.doSetComments(paramFulOrdLineItemDataBean.getComments());
		fulfillmentOrderLineItem.doSetCreatedDate(paramFulOrdLineItemDataBean.getCreateDate());
		fulfillmentOrderLineItem.doSetUpdatedDate(paramFulOrdLineItemDataBean.getUpdateDate());
		fulfillmentOrderLineItem.doSetSubstituteAllowed(ynStringToBoolean(paramFulOrdLineItemDataBean.getAllowSubstitution()));
		fulfillmentOrderLineItem.doSetSubstituteLineItemId(paramFulOrdLineItemDataBean.getSubstituteLineItemId());
		fulfillmentOrderLineItem.doSetServiceRequired(paramFulOrdLineItemDataBean.getServiceRequired());
		fulfillmentOrderLineItem.doSetPrice(SimMoneyUtility.convertToSimMoney(paramFulOrdLineItemDataBean.getUnitCostCurrency(), paramFulOrdLineItemDataBean.getUnitCostValue()));
		return fulfillmentOrderLineItem;
	}
}
