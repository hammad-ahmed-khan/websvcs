package org.logicInfo.oms.transferCreation.Controller;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class TransferCreationController {

	@Autowired
	private TsfCreationDAO dao;

	private final static Logger log = Logger.getLogger(TransferCreationController.class.getName());

	@PostMapping(value = "/NewTransferCreation", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<TransferCreationResponse> getOrderItemsResponse(@RequestBody TransferCreationRequest sub) throws JsonProcessingException, SQLException {
		log.info("Inside Carrera TsfCreationController " + sub.getCust_ord_no());
		TransferCreationResponse tcResp = new TransferCreationResponse();
		String item = "", ref_no = "", cust_ord_no = "";
		int qty = 0, src_id = 0, dest_id = 0, src_loc = 0, ful_loc = 0, ful_ord_no = 0;
		String itemArr[] = new String[sub.getCustomerItems().size()];
		int qtyArr[] = new int[sub.getCustomerItems().size()];
		log.info("Inside Carrera TsfCreationController " + sub.getCust_ord_no());
		src_id = sub.getSrc_id();
		dest_id = sub.getDest_id();
		ref_no = sub.getRef_no();
		src_loc = sub.getSrc_loc();
		ful_loc = sub.getFul_loc();
		ful_ord_no = sub.getFul_ord_no();
		cust_ord_no = sub.getCust_ord_no();
		tcResp.setSuccess(false);
		log.info("Inside Carrera TsfCreationController_src, Dest, ref,cust_order_no : " + src_id + ":" + dest_id + ":" + ref_no + ":" + sub.getCust_ord_no());
		try {
			for (int i = 0; i < sub.getCustomerItems().size(); i++) {
				item = sub.getCustomerItems().get(i).getItem();
				qty = sub.getCustomerItems().get(i).getQty();
				if (item.isEmpty() || item.equalsIgnoreCase("") || item == null || qty <= 0) {
					tcResp.setCode(500);
					tcResp.setSuccess(false);
					tcResp.setTsf_No(null);
					tcResp.setMessage("Invalid Item/Qty Value");
					return new ResponseEntity<TransferCreationResponse>(tcResp, HttpStatus.OK);
				} else {
					itemArr[i] = item;
					qtyArr[i] = qty;
				}
				log.info("Inside Carrera TsfCreationController for - ItemId and Qty,Cust Order No:" + item + ":" + qty + ":" + sub.getCust_ord_no());
			}
			tcResp = dao.getResponse(src_id, dest_id, ref_no, itemArr, qtyArr, item, qty, src_loc, ful_loc, ful_ord_no, cust_ord_no);

		} catch (Exception e) {
			log.info("Inside Carrera Transfer creation controller catch block. OrderNo:Error " + sub.getCust_ord_no() + ":" + e.getMessage());
			e.printStackTrace();
		} finally {
		}
		return new ResponseEntity<TransferCreationResponse>(tcResp, HttpStatus.OK);
	}
}
