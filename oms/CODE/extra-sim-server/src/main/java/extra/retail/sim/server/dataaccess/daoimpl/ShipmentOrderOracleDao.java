package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.common.shipment.AWBRequestLineItem;
import extra.retail.sim.server.dataaccess.dao.ShipmentOrderDao;
import extra.retail.sim.server.dataaccess.databean.generated.AWBRequestDataBean;
import extra.retail.sim.server.dataaccess.databean.generated.IMEIDataBean;
import extra.retail.sim.server.dataaccess.databean.generated.ShipmentOrderDataBean;
import extra.retail.sim.server.dataaccess.databean.generated.ShipmentOrderPrintDataBean;
import extra.retail.sim.server.dataaccess.procedure.ShipmentOrderCancelAWBProcedure;
import extra.retail.sim.server.dataaccess.procedure.ShipmentOrderRequestAWBProcedure;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;
import oracle.retail.sim.server.dataaccess.ParametricStatement;

/**
 * aibrahim
 * 2023
 */
public class ShipmentOrderOracleDao extends BaseOracleDao implements ShipmentOrderDao {

	@Override
	public FulfillmentOrderDelivery getPendingDeliveryItems(Long fulfillmentOrderId) throws SimServerException {
		String qry = buildQuery(fulfillmentOrderId);
		List<ShipmentOrderDataBean> list = query(new ShipmentOrderDataBean(), qry, Collections.singletonList(fulfillmentOrderId));
		if (list.isEmpty()) {
			return null;
		}
		FulfillmentOrderDelivery delivery = BOFactory.createFulfillmentOrderDelivery();
		delivery.doSetFulfillmentOrderId(fulfillmentOrderId);
		for (ShipmentOrderDataBean bean : list) {
			delivery.doAddLineItem(fromBeanToValueObject(bean));
		}
		return delivery;
	}

	@Override
	public AWBRequest getAWBDetail(Long pickId) throws SimServerException {
		StringBuilder builder = new StringBuilder();
		List<Object> params = new ArrayList<>(2);
		builder.append(AWBRequestDataBean.SELECT_SQL).append(where("AWB.PICK_ID"));
		params.add(pickId);
		addAnd(builder, "AWB.AWB_STATUS", "<>", "CANCELLED", params);
		List<AWBRequestDataBean> list = query(new AWBRequestDataBean(), builder.toString(), params);
		if (list.isEmpty()) {
			return null;
		}
		AWBRequest request = ExtraBOFactory.createAWBRequest();
		request.setFulOrdId(list.get(0).getFullfilmentOrdId());
		request.setPickId(pickId);
		for (AWBRequestDataBean bean : list) {
			request.getLineItems().add(fromBeanToAWBLineObject(bean));
		}
		return request;
	}

	@Override
	public List<UniqueSerialNumber> getIMEIDetail(Collection<Long> dlvLineItemIds) throws SimServerException {
		StringBuilder builder = new StringBuilder(IMEIDataBean.SELECT_SQL);
		List<Object> params = new ArrayList<Object>();
		addToWhereClauseForIn(builder, "UIN.FUL_ORD_LINE_ITEM_ID", dlvLineItemIds, " AND ", params);
		List<IMEIDataBean> list = query(new IMEIDataBean(), builder.toString(), params);
		if (list.isEmpty()) {
			return null;
		}
		List<UniqueSerialNumber> imeilList = new ArrayList<>(list.size());
		for (IMEIDataBean bean : list) {
			imeilList.add(fromBeanToIMEIObject(bean));
		}
		return imeilList;
	}

	private UniqueSerialNumber fromBeanToIMEIObject(IMEIDataBean bean) {
		UniqueSerialNumber serialNumber = ExtraBOFactory.createUniqueSerialNumber();
		serialNumber.setFulOrdDlvId(bean.getDeliveryId());
		serialNumber.setFulOrdDlvLineItemId(bean.getDeliveryLineItemId());
		serialNumber.setItemId(bean.getItemId());
		serialNumber.setQuantity(bean.getQty());
		serialNumber.setImeiNumber(bean.getImei());
		return serialNumber;
	}

	private AWBRequestLineItem fromBeanToAWBLineObject(AWBRequestDataBean bean) {
		AWBRequestLineItem lineItem = ExtraBOFactory.createAWBRequestLineItem();
		lineItem.setCartonNumber(bean.getCartonNumber());
		lineItem.setDeliveryId(bean.getDeliveryId());
		lineItem.setItem(bean.getItem());
		lineItem.setPickLineItemId(bean.getPickLineItemId());
		lineItem.setQty(bean.getQty());
		lineItem.setAwbNumber(bean.getAwbNumber());
		return lineItem;
	}

	@Override
	public List<String> lookupIMEIEnableItems(Long storeId, Collection<String> items) throws SimServerException {
		
		String baseQry = "SELECT ITEM_ID FROM STORE_UIN_ADMIN_ITEM WHERE STORE_ID = ? AND ITEM_ID ";
		List<Object> params = new ArrayList<>(items.size() + 1);
		params.add(storeId);
		params.addAll(items);
		return queryForStrings(baseQry + buildInClause(items.size()) + " AND CAPTURE_TIME_ID = 1 ", params);
	}

	private String buildQuery(Long fulfillmentOrderId) {
		return ShipmentOrderDataBean.SELECT_SQL + where("FOL.FUL_ORD_ID") + " AND FDL.FUL_ORD_LINE_ITEM_ID (+)= FOL.ID AND FOL.QUANTITY_PICKED > 0";
	}

	private FulfillmentOrderDeliveryLineItem fromBeanToValueObject(ShipmentOrderDataBean bean) throws SimServerException {
		FulfillmentOrderDeliveryLineItem lineItem = BOFactory.createFulfillmentOrderDeliveryLineItem();
		lineItem.doSetFulfillmentOrderLineItemId(bean.getFulfillmentOrderLineItemId());
		try {
			lineItem.setQuantity(bean.getPendingQuantity());
			lineItem.setCaseSize(new Quantity(1));
		} catch (BusinessException e) {
			LogService.error(e, "Error while setting the pending quantity");
			throw new SimServerException(e);
		}
		return lineItem;
	}

	@Override
	public String requestAWB(AWBRequest awbRequest) throws SimServerException {
		ShipmentOrderRequestAWBProcedure awbProcedure = new ShipmentOrderRequestAWBProcedure();
		awbProcedure.setAwbRequest(awbRequest);
		executeReadStoredProcedure(awbProcedure, awbProcedure.buildStatement());
		if (!"SUCCESS".equals(awbProcedure.getStatus())) {
			throw new SimServerException(awbProcedure.getErrorMessage());
		}
		return awbProcedure.getTrackingNumber();
	}

	@Override
	public void cancelAWB(List<String> trackingNumbers) throws SimServerException {
		ShipmentOrderCancelAWBProcedure awbProcedure = new ShipmentOrderCancelAWBProcedure();
		for (String trackingNumber : trackingNumbers) {
			awbProcedure.setTrackingNumber(trackingNumber);
			executeReadStoredProcedure(awbProcedure, awbProcedure.buildStatement());
			if (!"SUCCESS".equals(awbProcedure.getStatus())) {
				throw new SimServerException(awbProcedure.getErrorMessage());
			}
		}
	}

	@Override
	public Entry<String, List<String>> getQueueDetail(Long storeId, String trackingNumber) throws SimServerException {
		List<Object> params = new ArrayList<>();
		params.add(storeId);
		params.add(trackingNumber);
		List<ShipmentOrderPrintDataBean> labels = query(new ShipmentOrderPrintDataBean(), ShipmentOrderPrintDataBean.SELECT_SQL, params);
		if (labels.isEmpty()) {
			return null;
		}
		List<String> paths = new ArrayList<String>(labels.size());
		for (ShipmentOrderPrintDataBean bean : labels) {
			paths.add(bean.getLabelPath());
		}
		return new AbstractMap.SimpleEntry<>(labels.get(0).getQueue(), paths);
	}

	@Override
	public void updateHTC(String awbNumber) throws SimServerException {
		String str = "UPDATE XX_AWB_DETAILS SET HAND_OVER_IND = ? " + where("AWB_NBR");
	    List<String> params = new ArrayList<String>(2);
	    Collections.addAll(params, "Y", awbNumber);
	    execute(new ParametricStatement(str, params));
	}
}
