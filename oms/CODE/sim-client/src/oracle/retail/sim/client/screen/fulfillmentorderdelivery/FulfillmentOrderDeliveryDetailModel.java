package oracle.retail.sim.client.screen.fulfillmentorderdelivery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import extra.retail.sim.webservice.fulfillmentorderdelivery.client.CheckBOLWebserviceClient;
import extra.retail.sim.webservice.fulfillmentorderdelivery.client.DeleteIMEIWebserviceClient;
import extra.retail.sim.webservice.fulfillmentorderdelivery.client.LookupIMEIWebserviceClient;
import extra.retail.sim.webservice.fulfillmentorderdelivery.client.UpdateIndicatorIMEIWebserviceClient;
import extra.retail.sim.webservice.fulfillmentorderdelivery.model.ExtraFulfillmentOrderLineItem;
import extra.retail.sim.webservice.fulfillmentorderdelivery.model.ExtraIMEIFulfillmentOrderDelivery;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.client.CancelIMEIWebserviceClient;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.client.LookupImeiEnabledWebserviceClient;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.LookupUIN;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.UniqueSerialNumber;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryValidateUINCommand;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickStatus;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Delivery Detail Model
 * <p>
 * Provides business logic to the FulfillmentOrderDeliveryDetailScreen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderDeliveryDetailModel extends SimScreenModel {

	private FulfillmentOrderDelivery delivery;
	private FulfillmentOrder fulfillmentOrder;
	private boolean viewOnlyMode;
	private Long storeNumber;

	/**
	 * Loads the current FulfillmentOrderDelivery and respective Fulfillment
	 * Order from the repository and obtains an activity lock.
	 */
	public void loadDelivery() throws Exception {
		delivery = (FulfillmentOrderDelivery) RepositoryManager
				.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_DELIVERY);

		fulfillmentOrder = (FulfillmentOrder) RepositoryManager
				.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER);
		storeNumber = delivery.getStoreId();

		Boolean viewOnly = (Boolean) RepositoryManager
				.getStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_VIEW_ONLY);
		viewOnlyMode = viewOnly != null ? viewOnly : false;
		if (viewOnlyMode) {
			return;
		}
		viewOnlyMode = !isDeliveryEditAllowed();
		if (viewOnlyMode && !isDeliverySubmitted()) {
			return;
		}
		if (obtainLock()) {
			return;
		}
		viewOnlyMode = true;
	}

	/**
	 * Returns the current FulfillmentOrderDelivery.
	 * 
	 * @return The current FulfillmentOrderDelivery.
	 */
	public FulfillmentOrderDelivery getDelivery() {
		return delivery;
	}

	/**
	 * Returns the FulfillmentOrder the delivery was created for.
	 * 
	 * @return The FulfillmentOrder the current delivery was created for.
	 */
	public FulfillmentOrder getFulfillmentOrder() {
		return fulfillmentOrder;
	}

	/**
	 * Returns a List of CustomerOrderDeliveryLineItemWrappers representing the
	 * line items on the current FulfillmentOrderDelivery.
	 * 
	 * @return A List of CustomerOrderDeliveryLineItemWrappers representing the
	 *         line items on the current FulfillmentOrderDelivery.
	 */
	public List<FulfillmentOrderDeliveryLineItemWrapper> getDeliveryItems() throws Exception {
		List<FulfillmentOrderDeliveryLineItemWrapper> wrappers = new ArrayList<>();
		for (FulfillmentOrderDeliveryLineItem deliveryLineItem : delivery.getLineItems()) {
			for (FulfillmentOrderLineItem lineItem : fulfillmentOrder.getLineItems()) {
				if (deliveryLineItem.getFulfillmentOrderLineItemId().equals(lineItem.getId())) {
					wrappers.add(createWrapper(deliveryLineItem, lineItem));
					break;
				}
			}
		}
		return wrappers;
	}

	private FulfillmentOrderDeliveryLineItemWrapper createWrapper(FulfillmentOrderDeliveryLineItem deliveryLineItem,
			FulfillmentOrderLineItem lineItem) throws Exception {
		BigDecimal factor = UomUtility.getStandardUomToTargetUom(lineItem.getStockItem(), lineItem.getPreferredUom());
		return ClientWrapperFactory.createFulfillmentOrderDeliveryLineItemWrapper(delivery, deliveryLineItem,
				fulfillmentOrder, lineItem, factor);
	}

	/**
	 * Returns whether or not the editing the current FulfillmentOrderDelivery
	 * is allowed.
	 * 
	 * @return True if the current FulfillmentOrderDelivery can be edited,
	 *         otherwise false.
	 */
	public boolean isDeliveryEditAllowed() throws Exception {
		if (viewOnlyMode) {
			return false;
		}
		if (fulfillmentOrder.getDeliveryType() == FulfillmentOrderDeliveryType.PICKUP) {
			if (!hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_DELIVERY_FOR_PICKUP) && !delivery.isNew()) {
				return false;
			}
		}
		if (fulfillmentOrder.getDeliveryType() == FulfillmentOrderDeliveryType.SHIPMENT) {
			if (!hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_DELIVERY_FOR_SHIPMENT) && !delivery.isNew()) {
				return false;
			}
		}
		if (!isWebOrder()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns true if the current delivery is in a 'view only' state.
	 * 
	 * @return True if the current delivery is in a 'view only' state.
	 */
	public boolean isViewOnlyMode() {
		return viewOnlyMode;
	}

	/**
	 * Clears the current 'view only' state value.
	 */
	public void clearViewOnly() {
		RepositoryManager.removeStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_VIEW_ONLY);
	}

	/**
	 * Returns whether or not the current delivery is closed.
	 * 
	 * @return True if the current delivery is closed, otherwise false.
	 */
	public boolean isDeliveryClosed() {
		FulfillmentOrderDeliveryStatus status = delivery.getStatus();
		return status == FulfillmentOrderDeliveryStatus.COMPLETED || status == FulfillmentOrderDeliveryStatus.CANCELED;
	}

	/**
	 * Returns true if the current FulfillmentOrderDelivery is in 'Submitted'
	 * status.
	 * 
	 * @return True if the current FulfillmentOrderDelivery is in 'Submitted'
	 *         status, otherwise false.
	 */
	public boolean isDeliverySubmitted() {
		return delivery.getStatus() == FulfillmentOrderDeliveryStatus.SUBMITTED;
	}

	/**
	 * Returns true if the current FulfillmentOrderDelivery's FulfillmentOrder
	 * is a 'Web Order' reservation type.
	 * 
	 * @return True if the current FulfillmentOrderDelivery's FulfillmentOrder
	 *         is a 'Web Order' reservation type, otherwise false.
	 */
	public boolean isWebOrder() {
		return fulfillmentOrder.getOrderType() == FulfillmentOrderType.WEB_ORDER;
	}

	/**
	 * Returns true if the current FulfillmentOrderDelivery's FulfillmentOrder
	 * is a shipment.
	 * 
	 * @return True if the current FulfillmentOrderDelivery's FulfillmentOrder
	 *         is a shipment, otherwise false.
	 */
	public boolean isShipmentType() {
		return fulfillmentOrder.getDeliveryType() == FulfillmentOrderDeliveryType.SHIPMENT;
	}

	/**
	 * Returns if the GS1 Item Scanner is available.
	 * 
	 * @return True if the GS1 Item Scanner is available.
	 */
	public boolean isScannerAvailable() {
		return !(viewOnlyMode || isDeliverySubmitted() || isDeliveryClosed());
	}

	/**
	 * Returns true if the notes are currently editable.
	 */
	public boolean isNotesEditable() {
		FulfillmentOrderStatus status = fulfillmentOrder.getStatus();
		return status == FulfillmentOrderStatus.NEW || status == FulfillmentOrderStatus.IN_PROGRESS;
	}

	/**
	 * Returns true if the current FulfillmentOrderDelivery contains no line
	 * items with delivery quantities.
	 * 
	 * @return True if the current FulfillmentOrderDelivery contains no line
	 *         items with quantities, otherwise false.
	 */
	public boolean isDeliveryEmpty() {
		for (FulfillmentOrderDeliveryLineItem lineItem : delivery.getLineItems()) {
			if (lineItem.getQuantity() != null && !Quantity.ZERO.equals(lineItem.getQuantity())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns whether or not canceling the submission of the current
	 * FulfillmentOrderDelivery is allowed.
	 * 
	 * @return True if the user is allowed to cancel the submission of the
	 *         current FulfillmentOrderDelivery, otherwise false.
	 */
	public boolean isCancelSubmitAllowed() {
		return isDeliverySubmitted();
	}

	/**
	 * Returns whether or not dispatching the current FulfillmentOrderDelivery
	 * is allowed.
	 * 
	 * @return True if the user can dispatch the current
	 *         FulfillmentOrderDelivery.
	 */
	public boolean isDispatchAllowed() {
		if (isDeliveryClosed()) {
			return false;
		}
		if (!isWebOrder()) {
			return false;
		}
		boolean isShipValidate = getStoreBoolean(StoreConfigKeys.FUL_ORDER_DISPATCH_VALIDATE);
		if (!isShipValidate && delivery.getStatus() == FulfillmentOrderDeliveryStatus.IN_PROGRESS) {
			return true;
		}
		if (isShipValidate && delivery.getStatus() == FulfillmentOrderDeliveryStatus.SUBMITTED) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not submitting the current FulfillmentOrderDelivery is
	 * allowed.
	 * 
	 * @return True if the user can submit the current FulfillmentOrderDelivery.
	 */
	public boolean isSubmitAllowed() {
		if (!isWebOrder()) {
			return false;
		}
		boolean isShipValidate = getStoreBoolean(StoreConfigKeys.FUL_ORDER_DISPATCH_VALIDATE);
		if (isShipValidate && delivery.getStatus() == FulfillmentOrderDeliveryStatus.IN_PROGRESS) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the current store requires items to be picked
	 * before they can be delivered.
	 * 
	 * @return True if picking is required for the current store, otherwise
	 *         false.
	 */
	public boolean isPickingRequired() {
		return getStoreBoolean(StoreConfigKeys.PICKING_REQUIRED_FOR_CUSTOMER_ORDERS);
	}

	/**
	 * Returns whether or not the current user has permission to dispatch an
	 * incomplete FulfillmentOrderDelivery.
	 * 
	 * @return True if the user has permission to dispatch an incomplete
	 *         FulfillmentOrderDelivery, otherwise false.
	 */
	public boolean hasDispatchIncompletePermission() {
		return hasPermission(PermissionKey.PC_DISPATCH_INCOMPLETE_CUSTOMER_ORDER_DELIVERY);
	}

	/**
	 * Returns whether or not the FulfillmentOrder of the current delivery
	 * allows for partial delivery.
	 * 
	 * @return True if partial delivery is allowed, otherwise false.
	 */
	public boolean isAllowPartialDelivery() {
		return fulfillmentOrder.isAllowPartialDelivery();
	}

	/**
	 * Returns whether the current FulfillmentOrder has open Reverse Picks tied
	 * to it.
	 * 
	 * @return True if the current FulfillmentOrder has open Reverse Picks for
	 *         it, otherwise false.
	 */
	public boolean hasOpenReversePicks() throws Exception {
		List<FulfillmentOrderReversePickVO> reversePickVOs = ClientServiceFactory
				.getFulfillmentOrderReversePickServices().findFulfillmentOrderReversePickVOs(fulfillmentOrder.getId());
		for (FulfillmentOrderReversePickVO reversePickVO : reversePickVOs) {
			if (FulfillmentOrderReversePickStatus.NEW == reversePickVO.getStatus()
					|| FulfillmentOrderReversePickStatus.IN_PROGRESS == reversePickVO.getStatus()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the default delivery quantity for the input the line item on the
	 * current delivery.
	 * 
	 * @param wrappers
	 *            The line item for which to get default delivery quantity.
	 * @return The default delivery quantity for the input line item.
	 */
	public Quantity getDefaultQuantity(FulfillmentOrderDeliveryLineItemWrapper wrapper) throws Exception {
		Quantity defaultQuantity = Quantity.ZERO;
		if (isPickingRequired()) {
			defaultQuantity = wrapper.getPickedQty().subtract(wrapper.getDeliveredQty());
		} else if (wrapper.getPickedQty().subtract(wrapper.getOrderedQty()).isPositive()) {
			defaultQuantity = wrapper.getPickedQty().subtract(wrapper.getDeliveredQty());
		} else {
			defaultQuantity = wrapper.getRemainingQty();
		}
		if (defaultQuantity.isNegative()) {
			defaultQuantity = Quantity.ZERO;
		}
		return defaultQuantity;
	}

	public void updateExistingLineItem(FulfillmentOrderDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem)
			throws Exception {
		if (barcodeItem.isSerialNumberRequired()) {
			updateExistingLineItemUin(wrapper, barcodeItem);
		} else {
			updateExistingLineItemQty(wrapper, barcodeItem);
		}
	}

	protected boolean lookupIMEI() throws Exception {
		System.out.println("handle IMEI");
		ExtraIMEIFulfillmentOrderDelivery imeiDelivery = new ExtraIMEIFulfillmentOrderDelivery();
		imeiDelivery.setFulOrdId(delivery.getFulfillmentOrderId());
		for (FulfillmentOrderDeliveryLineItem deliveryLineItem : delivery.getLineItems()) {
			for (FulfillmentOrderLineItem lineItem : fulfillmentOrder.getLineItems()) {
				if (deliveryLineItem.getFulfillmentOrderLineItemId().equals(lineItem.getId())) {
					if (deliveryLineItem.getQuantity() != null
							&& !Quantity.ZERO.equals(deliveryLineItem.getQuantity())) {
						Long fulOrdDlvId = null;
						if (!delivery.isNew())
							fulOrdDlvId = delivery.getId();
						ExtraFulfillmentOrderLineItem imeiLineItem = new ExtraFulfillmentOrderLineItem();
						imeiLineItem.setFulOrdLineItemId(deliveryLineItem.getFulfillmentOrderLineItemId());
						imeiLineItem.setItemId(lineItem.getItemId());
						imeiLineItem.setFulFillmentOrderId(delivery.getFulfillmentOrderId());
						imeiLineItem.setStoreId(delivery.getStoreId());
						imeiLineItem.setFulOrdDlvId(fulOrdDlvId);
						imeiLineItem.setQuantity(deliveryLineItem.getQuantity().intValue());
						LookupIMEIWebserviceClient lookupIMEI = new LookupIMEIWebserviceClient();
						String imeiCheck = lookupIMEI.lookupIMEI(imeiLineItem);
						if (!imeiCheck.contains("\"code\":200")) {
							if (imeiCheck.contains("Please Enter the IMEI Number")) {
								throw new Exception("EMPTY_IMEI");
							} else if (imeiCheck
									.contains("IMEI Quantity Cannot be Greater than the Quantity Entered")) {
								throw new Exception("IMEI Quantity Cannot be Greater than the Quantity Entered");
							} else {
								throw new UIException(CommonMessageText.IMEI_GENERAL);
							}
						}

						/*
						 * boolean lookupUINenabled =
						 * lookupUINenabled(lineItem); if (lookupUINenabled) {
						 * imeiDelivery.setStoreId(delivery.getStoreId());
						 * ExtraFulfillmentOrderLineItem imeiLineItem = new
						 * ExtraFulfillmentOrderLineItem();
						 * imeiLineItem.setFulOrdLineItemId(deliveryLineItem.
						 * getFulfillmentOrderLineItemId());
						 * imeiLineItem.setItemId(lineItem.getItemId());
						 * imeiLineItem.setFulFillmentOrderId(delivery.
						 * getFulfillmentOrderId()); LookupIMEIWebserviceClient
						 * lookupIMEI = new LookupIMEIWebserviceClient(); String
						 * imeiCheck = lookupIMEI.lookupIMEI(imeiLineItem); if
						 * (imeiCheck.contains("EMPTY_IMEI")) { throw new
						 * Exception("EMPTY_IMEI"); } }
						 */
					} else {
						Long fulOrdDlvId = null;
						if (!delivery.isNew())
							fulOrdDlvId = delivery.getId();
						ExtraFulfillmentOrderLineItem imeiLineItem = new ExtraFulfillmentOrderLineItem();
						imeiLineItem.setFulOrdLineItemId(deliveryLineItem.getFulfillmentOrderLineItemId());
						imeiLineItem.setItemId(lineItem.getItemId());
						imeiLineItem.setFulFillmentOrderId(delivery.getFulfillmentOrderId());
						imeiLineItem.setStoreId(delivery.getStoreId());
						imeiLineItem.setFulOrdDlvId(fulOrdDlvId);
						imeiLineItem.setQuantity(0);
						DeleteIMEIWebserviceClient deleteIMEI = new DeleteIMEIWebserviceClient();
						String deleteIMEIResult = deleteIMEI.deleteIMEI(imeiLineItem);
						if (!deleteIMEIResult.contains("\"code\":200")) {
							throw new UIException(CommonMessageText.IMEI_GENERAL);

						}
					}
				}
			}
		}
		return true;

	}

	public boolean lookupUINenabled(FulfillmentOrderLineItem lineItem) {
		LookupImeiEnabledWebserviceClient uinEnabledCheck = new LookupImeiEnabledWebserviceClient();
		LookupUIN uin = new LookupUIN();
		uin.setItemId(lineItem.getItemId());
		uin.setStoreId(storeNumber);
		String lookupUINEnabled = uinEnabledCheck.lookupUINEnabled(uin);
		if (lookupUINEnabled.contains("UIN_ENABLED")) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean updateIndicatorIMEI(String status) throws Exception {
		System.out.println("update IMEI Indicator..");
		ExtraIMEIFulfillmentOrderDelivery imeiDelivery = new ExtraIMEIFulfillmentOrderDelivery();
		List<ExtraFulfillmentOrderLineItem> imeiDeliveryLineItem = new ArrayList<>();
		Long fulOrdDlvId = null;
		if (!delivery.isNew())
			fulOrdDlvId = delivery.getId();
		imeiDelivery.setFulOrdDlvId(fulOrdDlvId);
		for (FulfillmentOrderDeliveryLineItem deliveryLineItem : delivery.getLineItems()) {
			for (FulfillmentOrderLineItem lineItem : fulfillmentOrder.getLineItems()) {
				if (deliveryLineItem.getFulfillmentOrderLineItemId().equals(lineItem.getId())) {
					if (deliveryLineItem.getQuantity() != null
							&& !Quantity.ZERO.equals(deliveryLineItem.getQuantity())) {

						ExtraFulfillmentOrderLineItem imeiLineItem = new ExtraFulfillmentOrderLineItem();
						imeiLineItem.setFulOrdLineItemId(deliveryLineItem.getFulfillmentOrderLineItemId());
						imeiLineItem.setFulOrdDlvId(fulOrdDlvId);
						imeiLineItem.setStatus(status);
						imeiLineItem.setItemId(lineItem.getItemId());
						imeiLineItem.setFulFillmentOrderId(delivery.getFulfillmentOrderId());
						imeiDeliveryLineItem.add(imeiLineItem);
					}
				}
			}
		}
		imeiDelivery.setStatus(status);
		imeiDelivery.setLineItems(imeiDeliveryLineItem);
		UpdateIndicatorIMEIWebserviceClient updateIMEI = new UpdateIndicatorIMEIWebserviceClient();
		String isIndicatorUpdated = updateIMEI.updateimeiIndicator(imeiDelivery);
		if (isIndicatorUpdated.contains("\"code\":200")) {
			return true;
		} else {
			throw new UIException(CommonMessageText.IMEI_GENERAL);

		}
	}

	public boolean checkBOLAssigned() throws Exception {
		boolean success = false;
		CheckBOLWebserviceClient checkBOLClient = new CheckBOLWebserviceClient();
		List<UniqueSerialNumber> listIMEI = new ArrayList<>();
		Long fulFilId = null;
		if (!delivery.isNew()) {
			fulFilId = delivery.getId();
			for (FulfillmentOrderDeliveryLineItem deliveryLineItem : delivery.getLineItems()) {
				for (FulfillmentOrderLineItem lineItem : fulfillmentOrder.getLineItems()) {
					if (deliveryLineItem.getFulfillmentOrderLineItemId().equals(lineItem.getId())) {
						if (lineItem.getPickedQuantity() != lineItem.getDeliveredQuantity()) {
							UniqueSerialNumber details = new UniqueSerialNumber();
							details.setFulOrdId(delivery.getFulfillmentOrderId());
							details.setFulOrdDlvId(fulFilId);
							details.setFulOrdDlvLineItemId(deliveryLineItem.getFulfillmentOrderLineItemId());
							listIMEI.add(details);
						}
					}
				}
			}
			String cancelIMEI2 = checkBOLClient.checkBOL(listIMEI);
			if (cancelIMEI2.contains("\"code\":200")) {
				success = true;
			} else {
				success = false;
				if (cancelIMEI2.contains("BOL Already Assigned")) {
					throw new Exception("BOL_ASSIGNED");

				} else {
					throw new Exception(cancelIMEI2);
				}
			}

			return success;
		} else {
			return true;
		}

	}

	public boolean handleIMEICancel() throws Exception {
		boolean success = false;
		CancelIMEIWebserviceClient cancelIMEIClient = new CancelIMEIWebserviceClient();
		List<UniqueSerialNumber> listIMEI = new ArrayList<>();
		Long fulFilId = null;
		if (!delivery.isNew()) {
			fulFilId = delivery.getId();
		}
		for (FulfillmentOrderDeliveryLineItem deliveryLineItem : delivery.getLineItems()) {
			for (FulfillmentOrderLineItem lineItem : fulfillmentOrder.getLineItems()) {
				if (deliveryLineItem.getFulfillmentOrderLineItemId().equals(lineItem.getId())) {
					if (lineItem.getPickedQuantity() != lineItem.getDeliveredQuantity()) {
						UniqueSerialNumber details = new UniqueSerialNumber();
						details.setFulOrdId(delivery.getFulfillmentOrderId());
						details.setFulOrdDlvId(fulFilId);
						details.setFulOrdDlvLineItemId(deliveryLineItem.getFulfillmentOrderLineItemId());
						listIMEI.add(details);
					}
				}
			}
		}
		String cancelIMEI2 = cancelIMEIClient.cancelIMEI(listIMEI);
		if (cancelIMEI2.contains("\"code\":200")) {
			success = true;
		} else {
			success = false;
			throw new Exception(cancelIMEI2);
		}
		return success;
	}

	private void updateExistingLineItemQty(FulfillmentOrderDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem)
			throws Exception {
		if (barcodeItem.getQuantity().isPositive()) {
			if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
				wrapper.setQuantity(
						wrapper.getQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
			} else {
				wrapper.setQuantity(wrapper.getQuantityOrZero().add(barcodeItem.getQuantity()));
			}
			return;
		}
		throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
	}

	private void updateExistingLineItemUin(FulfillmentOrderDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem)
			throws Exception {
		String uinLabel = barcodeItem.getStockItem().getUINLabel();
		if (StringHelper.isNullOrEmpty(barcodeItem.getUin())) {
			throw new UIException(CommonMessageText.NO_UIN_CAPTURED, RErrorSeverity.WARNING);
		}
		SerialNumberValue serialNumber = ClientServiceFactory.getUINServices()
				.findSerialNumberValue(barcodeItem.getId(), barcodeItem.getUin());
		if (serialNumber == null) {
			Object[] values = new Object[3];
			values[0] = uinLabel;
			values[1] = barcodeItem.getUin();
			values[2] = barcodeItem.getId();
			throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
		}
		if (isDuplicateSerialNumber(barcodeItem.getId(), serialNumber)) {
			throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
		}
		FulfillmentOrderDeliveryValidateUINCommand command = ClientCommandFactory
				.createFulfillmentOrderDeliveryValidateUINCommand();
		command.setSerialNumber(serialNumber);
		command.setDelivery(getDelivery());
		command.setUINLabel(uinLabel);
		command.setNewOnTransaction(true);
		command.execute();

		if (!serialNumber.getStoreId().equals(getStoreId())) {
			Object[] values = new Object[3];
			values[0] = uinLabel;
			values[1] = serialNumber.getUin();
			values[2] = serialNumber.getStatus().toString();
			throw new BusinessException(UINMessageText.UIN_AT_ANOTHER_STORE, values);
		}
		wrapper.addSerialNumber(serialNumber);
	}

	public boolean isDuplicateSerialNumber(String itemId, SerialNumberValue newSerialNumber) {
		for (FulfillmentOrderDeliveryLineItem lineItem : delivery.getLineItems()) {
			for (FulfillmentOrderLineItem orderLineItem : fulfillmentOrder.getLineItems()) {
				if (orderLineItem.getId().equals(lineItem.getFulfillmentOrderLineItemId())) {
					if (orderLineItem.getItemId().equals(itemId)) {
						for (SerialNumberValue existingSerialNumber : lineItem.getSerialNumbers()) {
							if (existingSerialNumber.getUinId().equals(newSerialNumber.getUinId())) {
								return true;
							}
						}
					}
					break;
				}
			}
		}
		return false;
	}

	/**
	 * Cancels the current FulfillmentOrderDelivery.
	 */
	public void cancelDelivery() throws Exception {
		ClientServiceFactory.getFulfillmentOrderDeliveryServices().cancelFulfillmentOrderDelivery(delivery.getId());
		RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED, Boolean.TRUE);
	}

	/**
	 * Cancels the submission of the current FulfillmentOrderDelivery.
	 */
	public void cancelSubmitDelivery() throws Exception {
		ClientServiceFactory.getFulfillmentOrderDeliveryServices()
				.cancelSubmitFulfillmentOrderDelivery(delivery.getId());
		RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED, Boolean.TRUE);
	}

	/**
	 * Saves the current FulfillmentOrderDelivery if there are any changes.
	 */
	public void saveDelivery() throws Exception {
		if ((delivery.isNew() || delivery.isDirty()) && delivery.isCoherent()) {
			ClientServiceFactory.getFulfillmentOrderDeliveryServices().updateFulfillmentOrderDelivery(delivery);
			RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED, Boolean.TRUE);
		}
	}

	/**
	 * Dispatches the current FulfillmentOrderDelivery.
	 */
	public void dispatchDelivery() throws Exception {
		Long dispatchFulfillmentOrderDelivery = ClientServiceFactory.getFulfillmentOrderDeliveryServices()
				.dispatchFulfillmentOrderDelivery(delivery, getSessionPrinters());
		RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED, Boolean.TRUE);
		System.out.println(dispatchFulfillmentOrderDelivery);
		RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED, Boolean.TRUE);
	}

	/**
	 * Submits the current FulfillmentOrderDelivery.
	 */
	public void submitDelivery() throws Exception {
		ClientServiceFactory.getFulfillmentOrderDeliveryServices().submitFulfillmentOrderDelivery(delivery,
				getSessionPrinters());
		RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED, Boolean.TRUE);
	}

	private List<SessionPrinter> getSessionPrinters() {
		return (List<SessionPrinter>) SimRepository.getSessionPrinters();
	}

	/**
	 * Refreshes the fulfillment order data in the current delivery with a fresh
	 * copy from the database.
	 */
	public void refreshDeliveryData() throws Exception {
		fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices()
				.readFulfillmentOrder(fulfillmentOrder.getId());
		RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
	}

	/**
	 * Obtains an activity lock for the current FulfillmentOrderDelivery.
	 * 
	 * @return True if an activity lock was obtained, otherwise false.
	 */
	public boolean obtainLock() throws Exception {
		if (delivery.isNew()) {
			return true;
		}
		return obtainLock(ActivityLockType.FULFILLMENT_ORDER_DELIVERY, delivery.getId().toString());
	}

	/**
	 * Checks that the user still holds an activity lock for the current
	 * FulfillmentOrderDelivery.
	 * 
	 * @return True if the user still holds an activity lock, otherwise false.
	 */
	public boolean checkLock() throws Exception {
		if (delivery.isNew()) {
			return true;
		}
		return confirmLock(ActivityLockType.FULFILLMENT_ORDER_DELIVERY, delivery.getId().toString());
	}

	/**
	 * Releases the activity lock for the current FulfillmentOrderDelivery.
	 */
	public void releaseLock() throws Exception {
		releaseLock(ActivityLockType.FULFILLMENT_ORDER_DELIVERY, delivery.getIdAsString());
	}

	/**
	 * Stores the current FulfillmentOrderDelivery and respective
	 * FulfillmentOrder in the repository for navigation to the Bill Of Lading
	 * Detail Screen.
	 */
	public void storeDeliveryForBillOfLading() {
		if (viewOnlyMode) {
			RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_VIEW_ONLY, true);
		}
		RepositoryManager.addStateObject(SimClientStateKey.BILL_OF_LADING_FULFILLMENT_ORDER_DELIVERY, delivery);
		RepositoryManager.addStateObject(SimClientStateKey.BILL_OF_LADING_FULFILLMENT_ORDER, fulfillmentOrder);
	}
}
