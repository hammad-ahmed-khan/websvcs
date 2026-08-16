package org.logicInfo.oms.bookingDeletion.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class BookingDeletionController {

	@Autowired
	 private BookingDelDAO dao;
	
	@Autowired
	CustomerValidator customerValidator;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@DeleteMapping(value = "/booking/{orderNo}", headers="Accept=application/json", consumes="application/json",produces="application/json")
	public ResponseEntity<List<BookingDeletionResponse>> getOrderItemsResponse(@RequestBody BookingDeletionRequest sub, @PathVariable("orderNo") String orderNo) throws JsonProcessingException, SQLException {
		List<BookingDeletionResponse> bookingDelResp = new ArrayList<BookingDeletionResponse>();
		List <DeliveriesRequest> deliveriesReq = new ArrayList<DeliveriesRequest>();
		List<BookingRequest> bookingReq = new ArrayList<BookingRequest>();
//		int delvId=0;
//		int groupId=0;
		int reservationId = 0;
		System.out.println("OrderNo:"+orderNo);
		
		ErrorResponse valid=customerValidator.validation(sub);
		
		if(valid.getErrorCode()!= null){
			System.out.println("Inside Validation Error");
		}else{
			System.out.println("Inside controller else");
			
//			orderNo = sub.getOrderNo();
			
			deliveriesReq = sub.getDeliveries();
			for(int dsize=0; dsize<deliveriesReq.size();dsize++){
				//delvId = sub.getDeliveries().get(dsize).getDeliveryId();
				bookingReq = deliveriesReq.get(dsize).getBookings();
				for(int bsize=0; bsize<bookingReq.size();bsize++){
				//	groupId=bookingReq.get(bsize).getGroupId();
				//	bookingDelResp = dao.getResponse(reservationId, orderNo);
					reservationId = bookingReq.get(bsize).getReservationId();
					bookingDelResp = dao.getResponse(reservationId, orderNo);
					System.out.println("Inside for - ReservationID:"+reservationId);
				}
			}
		}	
		return new  ResponseEntity<List<BookingDeletionResponse>>(bookingDelResp,HttpStatus.OK);
	}
}
