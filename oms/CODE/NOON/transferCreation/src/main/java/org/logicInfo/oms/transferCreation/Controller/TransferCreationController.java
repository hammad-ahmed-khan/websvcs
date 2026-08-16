package org.logicInfo.oms.transferCreation.Controller;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		log.info("Inside TsfCreationController");
		TransferCreationResponse tcResp = new TransferCreationResponse();
		String item = "", ref_no = "";

		int qty = 0, src_id = 0, dest_id = 0;
		src_id = sub.getSrc_id();
		dest_id = sub.getDest_id();
		ref_no = sub.getRef_no();
		log.info("Inside TsfCreationController_src, Dest, ref : " + src_id + ":" + dest_id + ":" + ref_no);

		try {
			String itemArr[] = new String[sub.getCustomerItems().size()];
			int qtyArr[] = new int[sub.getCustomerItems().size()];
			String tsfNumber = dao.transferExists(ref_no);
			log.info("transfer number : " + tsfNumber);
			if (tsfNumber == null || tsfNumber == "" || tsfNumber.isEmpty()) {
				ArrayList<Items> inCorrectItems = dao.findIncorrectItem(src_id, dest_id, sub.getCustomerItems());
				if (inCorrectItems.size() > 0) {
					tcResp.setCode(500);
					tcResp.setSuccess(false);
					tcResp.setTsf_No(null);
					tcResp.setError("invalid_items");
					tcResp.setItems(inCorrectItems);
					dao.saveRequestandResponse(sub, tcResp);
					return new ResponseEntity<TransferCreationResponse>(tcResp, HttpStatus.OK);
				}

				ArrayList<Items> invalidItems = dao.findInvalidItem(src_id, dest_id, sub.getCustomerItems());
				if (invalidItems.size() > 0) {
					tcResp.setCode(500);
					tcResp.setSuccess(false);
					tcResp.setTsf_No(null);
					tcResp.setError("inactive_items");
					tcResp.setItems(invalidItems);
					dao.saveRequestandResponse(sub, tcResp);
					return new ResponseEntity<TransferCreationResponse>(tcResp, HttpStatus.OK);
				}
				ArrayList<Items> findStockPerLoc = dao.findStockPerLoc(src_id, sub.getCustomerItems());
				if (findStockPerLoc.size() > 0) {
					tcResp.setCode(500);
					tcResp.setSuccess(false);
					tcResp.setTsf_No(null);
					tcResp.setError("insufficient_qty");
					tcResp.setItems(findStockPerLoc);
					dao.saveRequestandResponse(sub, tcResp);
					return new ResponseEntity<TransferCreationResponse>(tcResp, HttpStatus.OK);
				}

				for (int i = 0; i < sub.getCustomerItems().size(); i++) {
					item = sub.getCustomerItems().get(i).getItem();
					qty = sub.getCustomerItems().get(i).getQty();
					if (item.isEmpty() || item.equalsIgnoreCase("") || item == null || qty <= 0) {
						tcResp.setCode(500);
						tcResp.setSuccess(false);
						tcResp.setTsf_No(null);
						tcResp.setMessage("Invalid Item/Qty Value");
						dao.saveRequestandResponse(sub, tcResp);
						return new ResponseEntity<TransferCreationResponse>(tcResp, HttpStatus.OK);
					} else {
						itemArr[i] = item;
						qtyArr[i] = qty;
					}

					log.info("Inside TsfCreationController for - ItemId and Qty:" + item + ":" + qty);
				}

				tcResp = dao.getResponse(src_id, dest_id, ref_no, itemArr, qtyArr);
			} else {
				tcResp.setCode(200);
				tcResp.setSuccess(true);
				tcResp.setTsf_No(tsfNumber);
			}
		} catch (Exception e) {
			tcResp.setCode(500);
			tcResp.setSuccess(false);
			tcResp.setMessage(e.getMessage());
			dao.saveRequestandResponse(sub, tcResp);
			log.info("Exception in getOrderItemsResponse transfer creation " + e.getMessage());
		}
		dao.saveRequestandResponse(sub, tcResp);
		return new ResponseEntity<TransferCreationResponse>(tcResp, HttpStatus.OK);
	}

}
