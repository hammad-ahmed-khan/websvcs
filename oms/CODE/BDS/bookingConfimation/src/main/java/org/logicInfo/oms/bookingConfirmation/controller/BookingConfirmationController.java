package org.logicInfo.oms.bookingConfirmation.controller;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class BookingConfirmationController {
	@Autowired
	 private BookingConfDAO dao;
	
	@Autowired
	CustomerValidator customerValidator;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
//	@PutMapping(value = "/booking/{orderNo}", headers="Accept=application/json", consumes="application/json",produces="application/json")
	@RequestMapping(value = "/booking/{orderNo}", method = RequestMethod.PUT, headers="Accept=application/json", consumes="application/json",produces="application/json")
	public ResponseEntity<List<BookingConfirmationResponse>> getOrderItemsResponse(@RequestBody BookingConfirmationRequest sub, @PathVariable("orderNo") String orderNo) throws JsonProcessingException, SQLException {
		List<BookingConfirmationResponse> bookingConfResp = new ArrayList<BookingConfirmationResponse>();
		List <DeliveriesRequest> deliveriesReq = new ArrayList<DeliveriesRequest>();
		List<BookingRequest> bookingReq = new ArrayList<BookingRequest>();
		int resvId = 0;
		String omsOrderNo = sub.getOmsOrderNo();
		orderNo = sub.getOrderNo();
		
		ErrorResponse valid=customerValidator.validation(sub);
		
		if(valid.getErrorCode()!= null){
			System.out.println("Validation Error");
		}else{
			System.out.println("Inside controller else");
			deliveriesReq = sub.getDeliveries();
			for(int dsize=0; dsize<deliveriesReq.size();dsize++){
				//delvId = sub.getDeliveries().get(dsize).getDeliveryId();
				bookingReq = deliveriesReq.get(dsize).getBookings();
				for(int bsize=0; bsize<bookingReq.size();bsize++){
					resvId=bookingReq.get(bsize).getReservationId();
					System.out.println("orderNo:::"+orderNo+":"+resvId+":"+omsOrderNo);
					bookingConfResp = dao.getResponse(resvId, orderNo, omsOrderNo);
				}
			}
		}	
		
		return new  ResponseEntity<List<BookingConfirmationResponse>>(bookingConfResp,HttpStatus.OK);
	}
}
