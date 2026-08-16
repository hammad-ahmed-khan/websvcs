package org.logicinfo.oms.bookingCreation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CustomerValidator {

	static boolean isValid= false;
	
	public  ErrorResponse validation(CreateBookingRequest sub) {
		ErrorResponse  errorResponse=new ErrorResponse();
		CustomerDetails customerDetails = new CustomerDetails();
		List <Deliveries> deliveriesList = new ArrayList<Deliveries>();
		
		if(sub.getOrderNo().isEmpty()||sub.getOrderNo().equals("")||sub.getOrderNo()==null){
			isValid=true;
			errorResponse.setErrorCode("E-0002");
			errorResponse.setErrorMessage("OrderNo doesn't Exists");
		}
		
		customerDetails=sub.getCustomerDetails();
		if(customerDetails==null){
			isValid=true;
			errorResponse.setErrorCode("E-0004");
			errorResponse.setErrorMessage("Customer Details doesn't Exists");
		}
		
		deliveriesList = sub.getDeliveries();
		
		if(null!=deliveriesList){
			if(deliveriesList.isEmpty()||deliveriesList.size()==0){
				isValid=true;
				errorResponse.setErrorCode("E-0005");
				errorResponse.setErrorMessage("Delivery Details doesn't Exists");
			}else{
				for(int i =0; i<deliveriesList.size();i++){
					if(deliveriesList.get(i).getDeliveryId()<0){
						isValid=true;
						errorResponse.setErrorCode("E-0005");
						errorResponse.setErrorMessage("Delivery Id doesn't Exists");
					}
					if(sub.getDeliveries().get(i).getBookings().size()==0){
						isValid=true;
						errorResponse.setErrorCode("E-0005");
						errorResponse.setErrorMessage("Bookings doesn't Exists for this delivery");
					}else{
						for(int j=0; j<sub.getDeliveries().get(i).getBookings().size();j++){
								if(sub.getDeliveries().get(i).getBookings().get(j).getReservationId()<=0){
									isValid=true;
									errorResponse.setErrorCode("E-0001");
									errorResponse.setErrorMessage("Reservation Id doesn't Exists");
								}
								if(sub.getDeliveries().get(i).getBookings().get(j).getGroupId()<=0){
								isValid=true;
								errorResponse.setErrorCode("E-0005");
								errorResponse.setErrorMessage("Group Id doesn't Exists");
								}
								if(sub.getDeliveries().get(i).getBookings().get(j).getWindowCode()==null){
									isValid=true;
									errorResponse.setErrorCode("E-0005");
									errorResponse.setErrorMessage("Window Code doesn't Exists");
								}
								if(sub.getDeliveries().get(i).getBookings().get(j).getDate()==null){
									isValid=true;
									errorResponse.setErrorCode("E-0005");
									errorResponse.setErrorMessage("Date doesn't Exists for this booking");
								}
								if(sub.getDeliveries().get(i).getBookings().get(j).getItems().size()==0){
									isValid=true;
									errorResponse.setErrorCode("E-0005");
									errorResponse.setErrorMessage("Item details doesn't Exists");
								}else{
									for(int k=0; k<sub.getDeliveries().get(i).getBookings().get(j).getItems().size();k++){
										if(sub.getDeliveries().get(i).getBookings().get(j).getItems().get(k).getItemCode()==null || sub.getDeliveries().get(i).getBookings().get(j).getItems().get(k).getItemCode().isEmpty()){
											isValid=true;
											errorResponse.setErrorCode("E-0005");
											errorResponse.setErrorMessage("Item Code doesn't Exists");
										}
									}
								}
							}
						}
					}		
				}
			}		
		return errorResponse; 
	}
}
  