package extra.retail.sim.client.screen.imeifulfillmentorderdelivery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import extra.retail.sim.webservice.imeifulfillmentorderdelivery.client.CancelIMEIWebserviceClient;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.client.LookupImeiEnabledWebserviceClient;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.client.RetrieveEnteredIMEIWSClient;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.client.SaveIMEIWebserviceClient;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.EnteredSerialNumbers;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.IMEIFulfillmentOrderDelivery;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.LookupUIN;
import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.UniqueSerialNumber;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryType;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickStatus;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.service.core.ClientServiceFactory;

public class IMEIFulfillmentOrderDeliveryDetailModel extends SimScreenModel {

	private FulfillmentOrderDelivery delivery;
	private FulfillmentOrder fulfillmentOrder;
	private boolean viewOnlyMode;
	List<IMEIFulfillmentOrderDelivery> imeifulfillmentorder = new ArrayList<>();
	private Long storeNumber = null;

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
		for (FulfillmentOrderDeliveryLineItem deliveryLineItem : delivery.getLineItems()) {
			for (FulfillmentOrderLineItem lineItem : fulfillmentOrder.getLineItems()) {
				if (deliveryLineItem.getFulfillmentOrderLineItemId().equals(lineItem.getId())) {
					if (deliveryLineItem.getQuantity() != null
							&& !Quantity.ZERO.equals(deliveryLineItem.getQuantity())) {
						boolean lookupUINenabled = lookupUINenabled(lineItem);
						if (lookupUINenabled) {
							Long deliveryId = null;
							int quantity = deliveryLineItem.getQuantity().intValue();
							if (!delivery.isNew()) {
								deliveryId = delivery.getId();
							}
							EnteredSerialNumbers imeiNumbers = retrieveUINs(delivery.getFulfillmentOrderId(),
									deliveryId, deliveryLineItem.getFulfillmentOrderLineItemId(), quantity);

							for (int i = 0; i < quantity; i++) {
								IMEIFulfillmentOrderDelivery bean = new IMEIFulfillmentOrderDelivery();
								bean.setCustOrdId(fulfillmentOrder.getCustomerOrderId());
								bean.setFulOrdId(delivery.getFulfillmentOrderId());
								bean.setItemId(lineItem.getItemId());
								bean.setItemDescription(lineItem.getItemDescription());
								bean.setStoreId(fulfillmentOrder.getStoreId());
								bean.setQuantity(1d);
								bean.setImeiNumber("");
								bean.setFulOrdDlvLineItemId(deliveryLineItem.getFulfillmentOrderLineItemId());
								bean.setOrderStatus(fulfillmentOrder.getStatus());
								bean.setPickQuantity(deliveryLineItem.getQuantity().intValue());
								if (imeiNumbers.getSerialNumbers() != null) {
									bean.setImeiNumber(imeiNumbers.getSerialNumbers().get(i));
								}
								if (!delivery.isNew()) {
									bean.setFulOrdDlvId(delivery.getId());
								}
								imeifulfillmentorder.add(bean);
							}
						}
					}
				}
			}
		}

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

	private EnteredSerialNumbers retrieveUINs(Long fulfillmentOrderId, Long deliveryId, Long fulfilmentItemId,
			int quantity) {
		RetrieveEnteredIMEIWSClient retrieveIMEIClient = new RetrieveEnteredIMEIWSClient();
		UniqueSerialNumber request = new UniqueSerialNumber();
		request.setFulOrdId(fulfillmentOrderId);
		request.setFulOrdDlvId(deliveryId);
		request.setQuantity(quantity);
		request.setFulOrdDlvLineItemId(fulfilmentItemId);
		String data = retrieveIMEIClient.retrieveIMEIDetails(request);
		Gson gson = new Gson();
		EnteredSerialNumbers serialNumbers = gson.fromJson(data, EnteredSerialNumbers.class);
		return serialNumbers;
	}

	private boolean lookupUINenabled(FulfillmentOrderLineItem lineItem) {
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
	public List<IMEIFulfillmentOrderDeliveryLineItemWrapper> getDeliveryItems() throws Exception {
		List<IMEIFulfillmentOrderDeliveryLineItemWrapper> wrappers = new ArrayList<>();
		for (IMEIFulfillmentOrderDelivery bean : imeifulfillmentorder) {
			wrappers.add(createWrapper(bean));
		}
		return wrappers;
	}

	private IMEIFulfillmentOrderDeliveryLineItemWrapper createWrapper(IMEIFulfillmentOrderDelivery bean)
			throws Exception {

		return ClientWrapperFactory.createIMEIFulfillmentOrderDeliveryLineItemWrapper(bean);
	}

	/**
	 * Returns whether or not the editing the current FulfillmentOrderDelivery
	 * is allowed.
	 * 
	 * @return True if the current FulfillmentOrderDelivery can be edited,
	 *         otherwise false.
	 */
	public boolean isDeliveryEditAllowed() throws Exception {
		/*
		 * if (viewOnlyMode) { return false; } if
		 * (fulfillmentOrder.getDeliveryType() ==
		 * FulfillmentOrderDeliveryType.PICKUP) { if
		 * (!hasPermission(PermissionKey.
		 * PC_EDIT_CUSTOMER_ORDER_DELIVERY_FOR_PICKUP) && !delivery.isNew()) {
		 * return false; } } if (fulfillmentOrder.getDeliveryType() ==
		 * FulfillmentOrderDeliveryType.SHIPMENT) { if
		 * (!hasPermission(PermissionKey.
		 * PC_EDIT_CUSTOMER_ORDER_DELIVERY_FOR_SHIPMENT) && !delivery.isNew()) {
		 * return false; } } if (!isWebOrder()) { return false; }
		 */
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
		// return
		// hasPermission(PermissionKey.PC_DISPATCH_INCOMPLETE_CUSTOMER_ORDER_DELIVERY);
		return true;
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
	public boolean saveDelivery() throws Exception {
		System.out.println("saving IMEI details");
		SaveIMEIWebserviceClient saveIMEI = new SaveIMEIWebserviceClient();

		List<UniqueSerialNumber> imeiDetailsList = new ArrayList<>();
		for (IMEIFulfillmentOrderDelivery orderLineItem : imeifulfillmentorder) {
			UniqueSerialNumber details = new UniqueSerialNumber();
			System.out.println("IMEI Number " + orderLineItem.getImeiNumber());
			String imeiNumber="";
			imeiNumber=orderLineItem.getImeiNumber();
			if (imeiNumber== null || imeiNumber == ""||imeiNumber.equals("")) {
				throw new UIException(CommonMessageText.NO_IMEI_APPLIED);
			}
			else{
				System.out.println("length of IMEI Number "+imeiNumber.length());
			}
			boolean validateSpecialCharacters = validateSpecialCharacters(imeiNumber);
			if (validateSpecialCharacters == true) {
				throw new UIException(CommonMessageText.SPECIAL_CHARACTERS);
				//SPECIAL_CHARACTER_PRESENT
			}
			boolean validateSpaces = validateSpaces(imeiNumber);
			if (validateSpaces == true) {
				throw new UIException(CommonMessageText.SPACE_EXISTS);
				//throw new Exception("SPACE_AT_FIRST_OR_LAST_INDEX");
			}
			Long FulOrdDlvId = null;
			FulOrdDlvId = orderLineItem.getFulOrdDlvId();
			if (FulOrdDlvId != null) {
				details.setFulOrdDlvId(orderLineItem.getFulOrdDlvId());
			}
			details.setCustOrdId(orderLineItem.getCustOrdId());
			details.setItemId(orderLineItem.getItemId());
			details.setFulOrdId(orderLineItem.getFulOrdId());
			details.setStoreId(orderLineItem.getStoreId());
			details.setFulOrdDlvLineItemId((orderLineItem.getFulOrdDlvLineItemId()));
			details.setImeiNumber(orderLineItem.getImeiNumber());
			details.setQuantity(orderLineItem.getPickQuantity());
			imeiDetailsList.add(details);
		}
		String insertIMEIDetails = saveIMEI.insertIMEIDetails(imeiDetailsList);
		if (!insertIMEIDetails.contains("\"code\":200")) {
			throw new Exception(insertIMEIDetails);
		}
		return true;

	}

	private boolean validateSpecialCharacters(String imeiNumber) throws Exception {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]+$");
		Matcher matcher = pattern.matcher(imeiNumber);
		boolean specialCharacterFound = false;
		if (!matcher.matches()) {
			System.out.println("IMEI Number '" + imeiNumber + "' contains special character");
			specialCharacterFound = true;
		}
		return specialCharacterFound;

	}

	private boolean validateSpaces(String imeiNumber) throws Exception {
		Pattern pattern = Pattern.compile("^[^\\s]+(\\s+[^\\s]+)*$");
		Matcher matcher = pattern.matcher(imeiNumber);
		boolean spaceFound = false;
		if (!matcher.matches()) {
			System.out.println("IMEI Number '" + imeiNumber + "' contains space as first or last character");
			spaceFound = true;
		}
		return spaceFound;

	}

	public String handleCancelIMEI() throws Exception {
		System.out.println("Cancel IMEI..");
		CancelIMEIWebserviceClient cancelIMEI = new CancelIMEIWebserviceClient();
		List<UniqueSerialNumber> listIMEI = new ArrayList<>();
		for (IMEIFulfillmentOrderDelivery orderLineItem : imeifulfillmentorder) {
			UniqueSerialNumber details = new UniqueSerialNumber();
			details.setFulOrdId(orderLineItem.getFulOrdId());
			details.setFulOrdDlvLineItemId(orderLineItem.getFulOrdDlvLineItemId());
			listIMEI.add(details);
		}
		String cancelIMEI2 = cancelIMEI.cancelIMEI(listIMEI);
		if (!cancelIMEI2.contains("\"code\":200")) {
			throw new Exception(cancelIMEI2);
		}
		return cancelIMEI2;

	}

	/**
	 * Dispatches the current FulfillmentOrderDelivery.
	 */
	public void dispatchDelivery() throws Exception {
		ClientServiceFactory.getFulfillmentOrderDeliveryServices().dispatchFulfillmentOrderDelivery(delivery,
				getSessionPrinters());
		RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED, Boolean.TRUE);
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
