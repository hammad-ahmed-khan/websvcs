package org.logicinfo.oms.slotBookingAvailability.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

/*import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import oracle.jdbc.OracleCallableStatement;
*/
@RestController
public class SlotsAvailCheckController {

	@Autowired
	private SlotsAvailDAO dao;

	@Autowired
	CustomerValidator customerValidator;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final Logger log = Logger.getLogger(SlotsAvailCheckController.class);

	@PostMapping(value = "/schedule", headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<SlotsAvailabilityResponse>> getOrderItemsResponse(@RequestBody SlotsAvailCheckRequest sub) throws JsonProcessingException, SQLException {

		ArrayList<Deliveries> deliveries = sub.getDeliveries();

		ErrorResponse valid = customerValidator.validation(sub);
		log.info("Inside controller:::" + valid + " SAC: order no:" + sub.getOrderNo() + " timestamp:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String inputQuery = "";
		String flag = "F";

		List<SlotsAvailabilityResponse> sAvailable = new ArrayList<SlotsAvailabilityResponse>();
		Long seqId = null;
		if (valid.getErrorCode() != null) {
			log.error("Validation Error");
		} else {

			try {
				/*
				 * Save the Incoming Request
				 */
				try {
					log.info("Saving the Request Body into the table. SAC: order no:" + sub.getOrderNo() + " timestamp:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					seqId = saveRequest(sub);
				} catch (Exception exception) {
					throw exception;
				}
				int siz = deliveries.size();
				String expressItemSKU = dao.getExpressItemSKU();
				for (int dsize = 0; dsize < deliveries.size(); dsize++) {
					inputQuery = itemQuery(sub, dsize);
					if (inputQuery.equalsIgnoreCase("INVALID ITEM NUMBER")) {
						log.info("Input Query value1:" + inputQuery + " SAC: order no:" + sub.getOrderNo() + " DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						flag = "T";
					} else if (inputQuery.equalsIgnoreCase("ITEMS MISMATCH")) {
						log.info("Input Query value2:" + inputQuery + " SAC: order no:" + sub.getOrderNo() + " DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						flag = "M";
					} else if (inputQuery.equalsIgnoreCase("SOURCE_OR_FULFILLMENT_LOCATION_EMPTY")) {
						log.info("Input Query value3:" + inputQuery + " SAC: order no:" + sub.getOrderNo() + " DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						flag = "SF";
					} else {
						log.info("Input Query value3:" + inputQuery + " SAC: order no:" + sub.getOrderNo() + " DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						sAvailable = dao.getResponse(inputQuery, deliveries, dsize, siz, expressItemSKU);
						log.info("After Package Call :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					}
				}
			} catch (Exception e) {
				log.info("Inside catch block SAC :", e);
				sAvailable = new ArrayList<SlotsAvailabilityResponse>();
				SlotsAvailabilityResponse slotsResp = new SlotsAvailabilityResponse();
				Status status = new Status();
				status.setSuccess(false);
				status.setCode("500");
				status.setMessage("Error -: " + e);
				slotsResp.setStatus(status);
				slotsResp.setDeliveries(null);
				sAvailable.add(slotsResp);
				return new ResponseEntity<List<SlotsAvailabilityResponse>>(sAvailable, HttpStatus.OK);
			}
		}
		if (valid.getErrorCode() != null || flag.equalsIgnoreCase("T") || flag.equalsIgnoreCase("M") || flag.equalsIgnoreCase("SF") || sAvailable == null) {
			SlotsAvailabilityResponse sAResposne = new SlotsAvailabilityResponse();
			Status st = new Status();
			if (flag.equalsIgnoreCase("T")) {
				flag = "F";
				st.setCode("E-505");
				st.setMessage("Invalid Item Code");
			} else if (flag.equalsIgnoreCase("M")) {
				flag = "F";
				st.setCode("E-705");
				st.setMessage("Items in the cart are not matching");
			} else if (flag.equalsIgnoreCase("SF")) {
				flag = "F";
				st.setCode("E-615");
				st.setMessage("Source or Fulfillment Location is null");
			} else if (sAvailable == null) {
				log.info("Input Query value7:" + sAvailable + " SAC: order no:" + sub.getOrderNo() + " DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				sAvailable = new ArrayList<SlotsAvailabilityResponse>();
				flag = "F";
				st.setCode("E-905");
				st.setMessage("Delivery slots not available for the requested item");
			} else {
				st.setCode(valid.getErrorCode());
				st.setMessage(valid.getErrorMessage());
			}
			sAResposne.setStatus(st);
			sAvailable.add(sAResposne);
			/*
			 * Save the Response
			 */
			try {
				saveResponse(sub.getOrderNo(), sAvailable, seqId);
			} catch (Exception exception) {
				log.info("Inside catch block SAC :", exception);
			}
			return new ResponseEntity<List<SlotsAvailabilityResponse>>(sAvailable, HttpStatus.OK);
		} else {
			try {
				saveResponse(sub.getOrderNo(), sAvailable, seqId);
			} catch (Exception exception) {
				log.info("Inside catch block SAC :", exception);
			}
			return new ResponseEntity<List<SlotsAvailabilityResponse>>(sAvailable, HttpStatus.OK);
		}
	}

	// Building input query that has to be sent as an input to the Get_Schedule
	// sql package
	public String itemQuery(SlotsAvailCheckRequest inputReq, int i) throws SQLException {
		String qry = "";
		String sourceLocation = "";
		String fulfillmentLocation = "";
		// String qry = "select " +
		// inputReq.getDeliveries().get(0).getItems().get(0).getLineNo() + " as
		// line_no, '" +
		// inputReq.getDeliveries().get(0).getItems().get(0).getItemCode() + "'
		// as item, "+
		// inputReq.getDeliveries().get(0).getItems().get(0).getOrderQty() + "
		// as QTY, 'N' as install_srv_flag from dual";
		log.info("Started ItemQuery method" + " OrderNo:" + inputReq.getOrderNo() + " timestamp:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		try {
			List<Items> inputItemsList;
			for (int j = 0; j < inputReq.getDeliveries().get(i).getItems().size(); j++) {
//				System.out.println("Inside item query build:" + i + ":" + j + "::" + inputReq.getDeliveries().get(i).getItems().size());
				inputItemsList = new ArrayList<Items>();
				String itemCode = "";
				long lineNo = inputReq.getDeliveries().get(i).getItems().get(j).getLineNo();
				itemCode = inputReq.getDeliveries().get(i).getItems().get(j).getItemCode();
				sourceLocation = inputReq.getDeliveries().get(i).getItems().get(j).getSourceLocation();
				fulfillmentLocation = inputReq.getDeliveries().get(i).getItems().get(j).getFulfillmentLocation();
				log.info("LineNo:" + lineNo);
				inputItemsList = inputReq.getDeliveries().get(i).getItems();
				// Charan - Commenting Mismatch code
				String misMatch = installMismatch(inputItemsList);
				log.info("***Install Mismatch return:" + misMatch);
				if (misMatch.equals("Mismatch")) {
					log.info("Inside if Mismatch");
					return "ITEMS MISMATCH";
				}
				if (isNullOrEmpty(sourceLocation) || isNullOrEmpty(fulfillmentLocation)) {
					return "SOURCE_OR_FULFILLMENT_LOCATION_EMPTY";
				}
				String query = "SELECT INVENTORY_IND from ITEM_MASTER WHERE ITEM ='" + inputItemsList.get(j).getItemCode() + "'";

				String invInd = null;
				try {
					invInd = jdbcTemplate.queryForObject(query, Collections.<String, Object>emptyMap(), String.class);
				} catch (DataAccessException e) {
					return "INVALID ITEM NUMBER";
				}

//				System.out.println("Inv_Ind from oracle for Item:" + itemCode + ":" + invInd);
				String ch = installFlag(itemCode);
//				System.out.println("install Flag value:" + ch + ":" + qry);
				// Iterate each item if Inventory check for line_no link
				// exists or not
				if (qry.equals("")) {
//					System.out.println("Inside if:" + inputItemsList.get(j).getItemCode());
					qry = "select " + inputItemsList.get(j).getLineNo() + " as line_no, '" + inputItemsList.get(j).getItemCode() + "' as item, " + inputItemsList.get(j).getOrderQty() + " as QTY,"
							+ inputItemsList.get(j).getSourceLocation() + " as source_loc," + inputItemsList.get(j).getFulfillmentLocation() + " as fulfill_loc, '" + ch
							+ "' as install_srv_flag from dual";
				} else {
//					System.out.println("Inside else:" + inputItemsList.get(j).getItemCode());
					qry += " union all select " + inputItemsList.get(j).getLineNo() + " as line_no, '" + inputItemsList.get(j).getItemCode() + "' as item, " + inputItemsList.get(j).getOrderQty()
							+ " as QTY," + inputItemsList.get(j).getSourceLocation() + " as source_loc," + inputItemsList.get(j).getFulfillmentLocation() + " as fulfill_loc, '" + ch
							+ "' as install_srv_flag from dual";
				}
			}
//			System.out.println("******************* Input Query********************");
			log.info("Input Query:" + qry);
			return qry;
		} catch (SQLException e) {
			log.info("Inside catch block SAC :", e);
			e.printStackTrace();
		}
		// }
		// return "select 1 as line_no, '00165769' as item, 2 as QTY, 'N' as
		// install_srv_flag from dual";
		// return qry;
		return "select " + inputReq.getDeliveries().get(0).getItems().get(0).getLineNo() + " as line_no, '" + inputReq.getDeliveries().get(0).getItems().get(0).getItemCode() + "' as item, "
				+ inputReq.getDeliveries().get(0).getItems().get(0).getOrderQty() + " as QTY,'" + inputReq.getDeliveries().get(0).getItems().get(0).getSourceLocation() + "' as source_loc,'"
				+ inputReq.getDeliveries().get(0).getItems().get(0).getFulfillmentLocation() + "' as fulfill_loc, 'N' as install_srv_flag from dual";
	}

	public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty())
			return false;
		return true;
	}

	// public String installFlag(List<Items> inputItemsList, Long lineNo)
	public String installFlag(String itemCode) throws SQLException {
		log.info("Inside installFlag: SAC: itemCode:" + itemCode + " DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String insQuery = "";

		String dept = null;

		insQuery = "SELECT DEPT FROM ITEM_MASTER WHERE ITEM ='" + itemCode + "'";
		List<String> depts = jdbcTemplate.queryForList(insQuery, Collections.<String, Object>emptyMap(), String.class);
		if (depts != null && !depts.isEmpty()) {
			dept = depts.get(0);
			if (dept.equals("2005")) { // ||dept.equals("411")
				return "Y";
			} else {
				return "N";
			}
		}
		return "N";
	}

	// Charan - Commenting Mismatch code
	public String installMismatch(List<Items> inputItemsList) throws SQLException {
		log.info("Inside installMismatch: SAC:  " + "DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		String insQuery = "";
		String dept = null;
		String delvQuery = "";
		String installQuery = "";
		int count, acCount, installCount, iSize;
		count = acCount = installCount = iSize = 0;
		int itemClass = 0;

//		System.out.println("Inside Install Items Mismatch::Item List Size:" + inputItemsList.size());
		for (Items itm : inputItemsList) {
			insQuery = "SELECT DEPT, CLASS FROM ITEM_MASTER WHERE ITEM = :item";

			Map<String, Object> params = Collections.<String, Object>singletonMap("item", itm.getItemCode());

			Map<String, Object> row = jdbcTemplate.queryForMap(insQuery, params);

			dept = String.valueOf(row.get("DEPT"));
			itemClass = ((Number) row.get("CLASS")).intValue();

			delvQuery = "SELECT COUNT(ITEM_CODE) FROM XX_DLVRY_AC_ITEMS WHERE ITEM_CODE ='" + itm.getItemCode() + "'";
			installQuery = "SELECT COUNT(ITEM) FROM XX_DLVRY_AC_INS_ITEMS_V WHERE ITEM ='" + itm.getItemCode() + "'";
			int delvCnt = jdbcTemplate.queryForObject(delvQuery, Collections.<String, Object>emptyMap(), Integer.class);
			int insSkuCnt = jdbcTemplate.queryForObject(installQuery, Collections.<String, Object>emptyMap(), Integer.class);
			if (dept != null) {
				if (delvCnt > 0) {
					if (insSkuCnt > 0) { // ||dept.equals("411")
						iSize = Integer.parseInt(itm.getOrderQty());
						installCount = iSize + installCount;
//						System.out.println("Install count :" + installCount);
					} else {
						count++;
					}

				} else {
//					System.out.println("Inside Install Items Mismatch::Item Code:" + itm.getItemCode() + ":" + dept);
					if (dept.equals("402") && (itemClass == 2 || itemClass == 3)) {
						iSize = Integer.parseInt(itm.getOrderQty());
						acCount = iSize + acCount;
//						System.out.println("AC Count :" + acCount);
					} else {
						count++;
					}

				}
			}
		}
		log.info("acCount:::" + acCount + ":installCount:" + installCount + ":count:" + count + "SAC:  " + "DateTime -:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if (acCount > 0 && installCount > 0) {
			if (acCount == installCount) {
				return "Equal";
			} else if (acCount != installCount) {
				return "Mismatch";
			}
		} else {
			return "Equal";
		}
		return "Equal";
	}

	private Long saveRequest(SlotsAvailCheckRequest sub) {
		try {
//			System.out.println("inside saveRequest()");
			return dao.saveRequest(sub);
		} catch (Exception exception) {
			log.info("Inside catch block SAC :", exception);
			exception.printStackTrace();
		}
		return null;

	}

	private void saveResponse(String OrderNumber, List<SlotsAvailabilityResponse> sAResposne, Long seqId) {
		try {
			dao.saveResponse(OrderNumber, sAResposne, seqId);
		} catch (Exception exception) {
			log.info("Inside catch block SAC :", exception);
			exception.printStackTrace();
		}
	}
}
