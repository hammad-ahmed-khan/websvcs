package org.logicInfo.oms.bookingDeletion.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CustomerValidator {

	static boolean isValid= false;
	
	public  ErrorResponse validation(BookingDeletionRequest sub) {
		ErrorResponse  errorResponse=new ErrorResponse();
		
		if(sub.getOrderNo().isEmpty()||sub.getOrderNo().equals("")||sub.getOrderNo()==null){
			isValid=true;
			errorResponse.setErrorCode("E-0001");
			errorResponse.setErrorMessage("OrderNo doesn't Exists");
		}
		
		
		if(sub.getDeliveries()==null){
			isValid=true;
			errorResponse.setErrorCode("E-0003");
			errorResponse.setErrorMessage("Delivery details doesn't Exists");
		}
		
		for(int i=0; i<sub.getDeliveries().size();i++){
			if(sub.getDeliveries().get(i).getBookings()==null){
				isValid=true;
				errorResponse.setErrorCode("E-0003");
				errorResponse.setErrorMessage("Booking Details doesn't Exists");
			}else{
				for(int j=0; j<sub.getDeliveries().get(i).getBookings().size();j++){
					if(sub.getDeliveries().get(i).getBookings().get(j).getReservationId()<0 || sub.getDeliveries().get(i).getBookings().get(j).getReservationId() == 0 ){
						isValid=true;
						errorResponse.setErrorCode("E-0004");
						errorResponse.setErrorMessage("Booking Id doesn't Exists");
					}
					if(sub.getDeliveries().get(i).getBookings().get(j).getGroupId()<0 || sub.getDeliveries().get(i).getBookings().get(j).getGroupId() == 0 ){
						isValid=true;
						errorResponse.setErrorCode("E-0005");
						errorResponse.setErrorMessage("Group Id doesn't Exists");
					}
			}
			}
		}		
		return errorResponse; 
	}
}
  