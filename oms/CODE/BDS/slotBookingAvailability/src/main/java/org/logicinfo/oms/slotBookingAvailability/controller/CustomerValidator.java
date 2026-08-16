package org.logicinfo.oms.slotBookingAvailability.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidator {

	static boolean isValid = false;
	private static final Logger log = Logger.getLogger(SlotsAvailCheckController.class);

	
	public ErrorResponse validation(SlotsAvailCheckRequest sub) {
		ErrorResponse errorResponse = new ErrorResponse();
		CustomerDetails customerDetails = new CustomerDetails();
		List<Deliveries> deliveriesList = new ArrayList<Deliveries>();
		try {
			log.info("Inside validation::: SAC: order no:" + sub.getOrderNo());
			if (sub.getOrderNo().isEmpty() || sub.getOrderNo().equals("") || sub.getOrderNo() == null) {
				isValid = true;
				errorResponse.setErrorCode("E-0001");
				errorResponse.setErrorMessage("OrderNo doesn't Exists");
			}

			if (sub.getCreateDate().isEmpty() || sub.getCreateDate().equals("") || sub.getCreateDate() == null) {
				isValid = true;
				errorResponse.setErrorCode("E-0003");
				errorResponse.setErrorMessage("CreateDate doesn't Exists");
			}

			customerDetails = sub.getCustomerDetails();
			if (customerDetails == null) {
				isValid = true;
				errorResponse.setErrorCode("E-0004");
				errorResponse.setErrorMessage("Customer Details doesn't Exists");
			}

			deliveriesList = sub.getDeliveries();

			if (null != deliveriesList) {
				if (deliveriesList.isEmpty() || deliveriesList.size() == 0) {
					isValid = true;
					errorResponse.setErrorCode("E-0005");
					errorResponse.setErrorMessage("Delivery Details doesn't Exists");
				} else {
					for (int i = 0; i < deliveriesList.size(); i++) {
						for (int j = 0; j < deliveriesList.get(i).getItems().size(); j++) {

							if (deliveriesList.get(i).getDeliveryId() <= 0) {
								isValid = true;
								errorResponse.setErrorCode("E-0006");
								errorResponse.setErrorMessage("Delivery Id doesn't Exists");
							} else if ((deliveriesList.get(i).getItems().get(j).getItemCode() == null) || deliveriesList.get(i).getItems().get(j).getItemCode() == "") {
								isValid = true;
								errorResponse.setErrorCode("E-0007");
								errorResponse.setErrorMessage("Item Code doesn't Exists");
							} else {
								int lineNo = deliveriesList.get(i).getItems().get(j).getLineNo();
								if (lineNo < 0) {
									isValid = true;
									errorResponse.setErrorCode("E-0008");
									errorResponse.setErrorMessage("Line no doesn't Exists for Item:" + deliveriesList.get(i).getItems().get(i).getItemCode());
								}
							}
						}
						if (deliveriesList.get(i).getItems().size() == 0) {
							isValid = true;
							errorResponse.setErrorCode("E-0009");
							errorResponse.setErrorMessage("Item Details doesn't Exists");
						}
					}
				}
			}
		} catch (Exception e) {
			errorResponse.setErrorCode("500");
			errorResponse.setErrorMessage(e.toString());
			log.error("Error in validation method-" , e);
			return errorResponse;
		}
		return errorResponse;
	}

}
