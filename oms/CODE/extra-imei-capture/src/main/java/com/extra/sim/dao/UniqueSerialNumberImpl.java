package com.extra.sim.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.extra.sim.model.EnteredSerialNumbers;
import com.extra.sim.model.ExtraFulfillmentOrderLineItem;
import com.extra.sim.model.ExtraIMEIFulfillmentOrderDelivery;
import com.extra.sim.model.IMEICancelDelivery;
import com.extra.sim.model.LookupUIN;
import com.extra.sim.model.SuccessResponse;
import com.extra.sim.model.UniqueSerialNumber;

/**
 * @author Madhuchandra
 */

@Repository
public class UniqueSerialNumberImpl implements UniqueSerialNumberDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger log = Logger.getLogger(UniqueSerialNumberImpl.class.getName());

	@Override
	public SuccessResponse saveIMEI(List<UniqueSerialNumber> request) throws Exception {
		log.info("inside saveIMEI() ");
		SuccessResponse response = new SuccessResponse();
		Statement stmt = null;
		Statement stmtInsert = null;
		Statement stmtQtyCheck = null;
		Connection conn = null;
		ResultSet rsQtyCheck = null;
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			conn.setAutoCommit(false);

			for (UniqueSerialNumber imeiNumber : request) {
				String deleteQuery = "";
				Long deliveryId = 0l;
				deliveryId = imeiNumber.getFulOrdDlvId();
				log.info("deleting existing IMEI details with Delivery Id " + imeiNumber.getFulOrdDlvId());
				stmt = conn.createStatement();
				if (deliveryId != null) {
					deleteQuery = "DELETE FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
							+ imeiNumber.getFulOrdId() + " AND FUL_ORD_LINE_ITEM_ID="
							+ imeiNumber.getFulOrdDlvLineItemId() + " AND FUL_ORD_DLV_ID=" + deliveryId
							+ " AND STATUS IN ('N', 'S')";
				} else {
					deleteQuery = "DELETE FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
							+ imeiNumber.getFulOrdId() + " AND FUL_ORD_LINE_ITEM_ID="
							+ imeiNumber.getFulOrdDlvLineItemId() + " AND STATUS IN ('N')";
				}
				log.info(deleteQuery.toString());
				stmt.executeUpdate(deleteQuery);
			}

			for (final UniqueSerialNumber serial_number : request) {
				Long fulOrdId = 0l;
				Long fulOrdDlvItemId = null;
				String queryQtyCheck = "";
				int quantity = 0;
				int imeiQuantity = 0;
				log.info("Inside lookupIMEIQty() ");
				fulOrdId = serial_number.getFulOrdId();
				fulOrdDlvItemId = serial_number.getFulOrdDlvLineItemId();

				int pickedQuantity = getPickedQuantity(fulOrdId, fulOrdDlvItemId, conn);
				quantity = serial_number.getQuantity();
				stmtQtyCheck = conn.createStatement();
				queryQtyCheck = "SELECT SUM(QUANTITY) AS IMEI_QTY FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
						+ fulOrdId + " AND FUL_ORD_LINE_ITEM_ID=" + fulOrdDlvItemId + " AND STATUS IN ('D', 'S')";
				log.info(queryQtyCheck.toString());

				rsQtyCheck = stmtQtyCheck.executeQuery(queryQtyCheck);
				while (rsQtyCheck.next()) {
					imeiQuantity = rsQtyCheck.getInt("IMEI_QTY");
					log.info("IMEI quantity from XX_FUL_ORD_DLV_LINE_ITEM_UIN " + imeiQuantity);
				}
				int totalImeiQuantity = quantity + imeiQuantity;
				log.info("IMEI Quantity=" + totalImeiQuantity + " and Picked Quantity " + pickedQuantity);
				if (totalImeiQuantity < pickedQuantity || totalImeiQuantity == pickedQuantity) {
					/*
					 * response.setCode(200); response.setSuccess(true);
					 * response.setMessage("Success");
					 */
				} else {
					log.info("IMEI Qty cannot be more than Picked Quantity");
					response.setCode(404);
					response.setSuccess(false);
					response.setMessage("IMEI Qty cannot be more than Picked Quantity");
					conn.rollback();
					throw new Exception("IMEI Qty cannot be more than Picked Quantity");
				}

				int flag = 0;
				try {
					flag = jdbcTemplate.queryForObject("SELECT MIN(CHECK_FLAG) CHECK_FLAG FROM XX_STORE_IMEI_SN_CONF_INFO WHERE ITEM_ID = ? AND STORE_ID = ?", Integer.class, serial_number.getItemId(), serial_number.getStoreId());
				} catch (Exception e) {
					log.warn("Error while checking the IMEI flag.", e);
				}
				if (flag == 1) {
					List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);
					try {
						log.info("Calling procedure to validate the imei number " + serial_number.getImeiNumber());
						declaredParameters.add(new SqlParameter(Types.VARCHAR));
						declaredParameters.add(new SqlParameter(Types.VARCHAR));
						declaredParameters.add(new SqlParameter(Types.NUMERIC));
						declaredParameters.add(new SqlParameter(Types.VARCHAR));
						declaredParameters.add(new SqlParameter(Types.VARCHAR));
						declaredParameters.add(new SqlOutParameter("valid", Types.VARCHAR));
						Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {
							
							@Override
							public CallableStatement createCallableStatement(Connection con) throws SQLException {
								CallableStatement stmt = con.prepareCall("{ CALL XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE@rmsdb(?, ?, ?, ?, ?, ?) }" );
								stmt.setString(1, serial_number.getItemId().trim());
								stmt.setString(2, serial_number.getImeiNumber().trim());
								stmt.setLong(3, Long.parseLong(serial_number.getStoreId()));
								stmt.setString(4, "SIM");
								stmt.setString(5, "Y");
								stmt.registerOutParameter(6, Types.VARCHAR);
						        return stmt;
							}
						}, declaredParameters);
						Object result = resultMap.get("valid");
						if (StringUtils.isEmpty(result) || !((String) result).equalsIgnoreCase("Y")) {
							flag = 2;
						}
					} catch (Exception e) {
						log.warn("Error while validating the imeinumber", e);
						flag = 2;
					}
				}
				if (flag == 2) {
					log.info("Calling procedure to validate the imei format " + serial_number.getImeiNumber());
					String message = jdbcTemplate.execute(new CallableStatementCreator() {
						
						@Override
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							CallableStatement stmt = con.prepareCall("{? = call XX_IMEI_SN_CONFIG_VALIDATE.XX_IMEI_SN_CONF_VAL(?, ?, ?) }" );
							stmt.registerOutParameter(1, Types.VARCHAR);
							stmt.setString(2, serial_number.getItemId());
							stmt.setLong(3, Long.parseLong(serial_number.getStoreId()));
							stmt.setString(4, serial_number.getImeiNumber());
					        return stmt;
						}
					}, new CallableStatementCallback<String>() {

						@Override
						public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
							cs.execute();
							String message = cs.getString(1);
							return message;
						}
					} );
					log.info("Message received for the imei validation " + serial_number.getItemId() + " is " + message);
					if (!"Y".equalsIgnoreCase(message)) {
						throw new Exception("Invalid IMEI Number");
					}
				}
			}

			for (UniqueSerialNumber imeiNumber : request) {
				String custOrderId = "";
				String item = "";
				String storeId = "";
				Long fulOrdId = 0l;
				Long fulOrdDlvId = 0l;
				Long fulOrdDlvItemId = null;
				Long fulOrdDlvIdUpd = 0l;
				String queryInsert = "";
				String imei_Number = "";

				custOrderId = imeiNumber.getCustOrdId();
				storeId = imeiNumber.getStoreId();
				fulOrdId = imeiNumber.getFulOrdId();
				fulOrdDlvId = imeiNumber.getFulOrdDlvId();
				if (fulOrdDlvId == null) {
					fulOrdDlvIdUpd = 0l;
				} else {
					fulOrdDlvIdUpd = fulOrdDlvId;
				}

				item = imeiNumber.getItemId();
				fulOrdDlvItemId = imeiNumber.getFulOrdDlvLineItemId();
				imei_Number = imeiNumber.getImeiNumber();
				stmtInsert = conn.createStatement();
				queryInsert = "insert into XX_FUL_ORD_DLV_LINE_ITEM_UIN(ID, FUL_ORD_ID, FUL_ORD_LINE_ITEM_ID, CUST_ORDER_ID, STORE_ID,ITEM_ID,QUANTITY,IMEI_NUMBER,CREATE_DATE,STATUS,FUL_ORD_DLV_ID) values (XX_FUL_ORD_DLV_LINE_UIN_SEQ.NEXTVAL,"
						+ fulOrdId + "," + fulOrdDlvItemId + ",'" + custOrderId + "','" + storeId + "','" + item
						+ "',1,'" + imei_Number + "',sysdate,'N'," + fulOrdDlvIdUpd + ")";
				log.info(queryInsert.toString());
				stmtInsert.executeUpdate(queryInsert);

			}
			response.setCode(200);
			response.setSuccess(true);
			response.setMessage("Success");
			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("Exception occured. Error Message is "+e.getMessage());
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (stmtInsert != null) {
				stmtInsert.close();
			}
			if (stmtQtyCheck != null) {
				stmtQtyCheck.close();
			}
			if (rsQtyCheck != null) {
				rsQtyCheck.close();
			}
		}
		return response;
	}

	private boolean validateSpecialCharacters(String imeiNumber) throws Exception {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]+$");
		Matcher matcher = pattern.matcher(imeiNumber);
		boolean specialCharacterFound = false;
		if (!matcher.matches()) {
			log.info("IMEI Number '" + imeiNumber + "' contains special character");
			specialCharacterFound = true;
		}
		return specialCharacterFound;

	}
	private boolean validateSpaces(String imeiNumber) throws Exception {
		Pattern pattern = Pattern.compile("^[^\\s]+(\\s+[^\\s]+)*$");
		Matcher matcher = pattern.matcher(imeiNumber);
		boolean spaceFound = false;
		if (!matcher.matches()) {
			log.info("IMEI Number '" + imeiNumber + "' contains space as first or last character");
			spaceFound = true;
		}
		return spaceFound;

	}

	private void insertIMEIDetails(List<UniqueSerialNumber> request) throws Exception {
		log.info("inside insertIMEIDetails() ");
		SuccessResponse response = new SuccessResponse();
		String custOrderId = "";
		String item = "";
		String storeId = "";
		String imeiNumber = "";
		Long fulOrdId = 0l;
		Long fulOrdDlvId = 0l;
		Long fulOrdDlvItemId = null;
		Long fulOrdDlvIdUpd = 0l;
		Statement stmt = null;
		String query = "";
		Connection conn = null;

		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			conn.setAutoCommit(false);
			for (UniqueSerialNumber serialNumber : request) {
				log.info("Inserting IMEI details..");
				custOrderId = serialNumber.getCustOrdId();
				storeId = serialNumber.getStoreId();
				fulOrdId = serialNumber.getFulOrdId();
				fulOrdDlvId = serialNumber.getFulOrdDlvId();
				if (fulOrdDlvId == null) {
					fulOrdDlvIdUpd = 0l;
				} else {
					fulOrdDlvIdUpd = fulOrdDlvId;
				}

				item = serialNumber.getItemId();
				fulOrdDlvItemId = serialNumber.getFulOrdDlvLineItemId();
				imeiNumber = serialNumber.getImeiNumber();
				stmt = conn.createStatement();
				query = "insert into XX_FUL_ORD_DLV_LINE_ITEM_UIN(ID, FUL_ORD_ID, FUL_ORD_LINE_ITEM_ID, CUST_ORDER_ID, STORE_ID,ITEM_ID,QUANTITY,IMEI_NUMBER,CREATE_DATE,STATUS,FUL_ORD_DLV_ID) values (XX_FUL_ORD_DLV_LINE_UIN_SEQ.NEXTVAL,"
						+ fulOrdId + "," + fulOrdDlvItemId + ",'" + custOrderId + "','" + storeId + "','" + item
						+ "',1,'" + imeiNumber + "',sysdate,'N'," + fulOrdDlvIdUpd + ")";
				log.info(query.toString());

				try {
					stmt.executeUpdate(query);
				} catch (SQLException exp) {
					conn.rollback();
					response.setCode(404);
					response.setSuccess(false);
					response.setMessage("exception occured");
					log.info("Exception occured during inserting IMEI Details for the IMEI Number: "
							+ serialNumber.getImeiNumber() + " with FulfillmentOrderId: " + fulOrdId
							+ " and DeliveryId: " + fulOrdDlvIdUpd);
					exp.printStackTrace();
					throw new Exception(exp.getMessage());
				}

			}
			conn.commit();
			response.setCode(200);
			response.setSuccess(true);
			response.setMessage("Success");
		} catch (Exception e) {
			log.info("Exception occured during inserting IMEI Details. The error is: " + e.getMessage());
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	@Override
	public SuccessResponse cancelIMEI(List<UniqueSerialNumber> request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Statement stmt = null;
		String deleteQuery = "";
		Connection conn = null;
		log.info("inside cancelIMEI()");
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			conn.setAutoCommit(false);
			for (UniqueSerialNumber lineItem : request) {
				stmt = conn.createStatement();
				deleteQuery = "delete from XX_FUL_ORD_DLV_LINE_ITEM_UIN where FUL_ORD_ID=" + lineItem.getFulOrdId()
						+ " and FUL_ORD_LINE_ITEM_ID=" + lineItem.getFulOrdDlvLineItemId() + " and STATUS='N'";
				log.info(deleteQuery.toString());
				try {
					stmt.executeUpdate(deleteQuery);
				} catch (SQLException exp) {
					conn.rollback();
					response.setCode(404);
					response.setSuccess(false);
					response.setMessage("exception occured");
					exp.printStackTrace();
					throw new Exception(exp.getMessage());
				}
			}
			conn.commit();
			response.setCode(200);
			response.setSuccess(true);
			response.setMessage("Success");
		} catch (Exception e) {
			conn.rollback();
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return response;
	}

	private boolean checkBOLExists(Long deliveryId, Long ordId) throws Exception {
		Statement stmt = null;
		Statement stmtBOL = null;
		ResultSet rs = null;
		ResultSet rsBol = null;
		String selectQuery = "";
		String selectBOLQuery = "";
		Connection conn = null;
		boolean captureId = false;
		String carrierRole = "";
		int carrierId = 0;
		log.info("Inside checkBOLExists() : checking if BOL is already assigned for the Delivery Id " + deliveryId);
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			stmtBOL = conn.createStatement();
			selectQuery = "select SHIPMENT_BOL_ID from FUL_ORD_DLV where ID=" + deliveryId + " and FUL_ORD_ID=" + ordId;
			log.info(selectQuery.toString());
			try {
				rs = stmt.executeQuery(selectQuery);
				while (rs.next()) {
					int shipmentBOLID = rs.getInt("SHIPMENT_BOL_ID");
					if (shipmentBOLID != 0) {
						selectBOLQuery = "SELECT CARRIER_ROLE,SHIP_CARRIER_ID from SHIPMENT_BOL where ID="
								+ shipmentBOLID;
						rsBol = stmtBOL.executeQuery(selectBOLQuery);
						while (rsBol.next()) {
							carrierRole = rsBol.getString("CARRIER_ROLE");
							carrierId = rsBol.getInt("SHIP_CARRIER_ID");
							if (carrierRole.equals("Third Party") && carrierId != 0) {
								log.info("BOL is Already Assigned..");
								captureId = true;
							}
						}
					}

				}
			} catch (Exception ex) {
				captureId = false;
				ex.printStackTrace();
				throw new Exception("Error :" + ex.getMessage());
			}
		} catch (Exception e) {
			captureId = false;
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (rsBol != null) {
				rsBol.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (stmtBOL != null) {
				stmtBOL.close();
			}
		}
		return captureId;

	}

	@Override
	public SuccessResponse lookupIMEI(ExtraFulfillmentOrderLineItem request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Long fulOrdDlvId = null;
		log.info("Inside dao lookupIMEI()");
		try {
			fulOrdDlvId = request.getFulOrdDlvId();
			boolean uinEnableCheck = uinEnableCheck(request.getStoreId(), request.getItemId());
			if (uinEnableCheck) {
				if (fulOrdDlvId != null) {
					log.info("Fulfillment Order Delivery Id is not Null");
					int quantity = request.getQuantity();
					int imeiQuantity = lookupIMEIEntered(request.getFulFillmentOrderId(), fulOrdDlvId,
							request.getItemId());
					log.info("lookupIMEI() : IMEI Quantity " + imeiQuantity + " and User Entered Quantity " + quantity);
					if (imeiQuantity < quantity) {
						response.setCode(404);
						response.setSuccess(false);
						response.setMessage("Please Enter the IMEI Number");
						log.info("IMEI is not Inserted, Please Enter IMEI Number before Save or Dispatch");
						throw new Exception("Please Enter the IMEI Number");
					} else if (imeiQuantity > quantity) {
						response.setCode(404);
						response.setSuccess(false);
						response.setMessage("IMEI Quantity Cannot be Greater than the Quantity Entered");
						log.info("IMEI Quantity Cannot be Greater than the Quantity Entered");
						throw new Exception("IMEI Quantity Cannot be Greater than the Quantity Entered");
					} else {
						log.info("IMEI Quantity equal to the Quantity Entered");
						response.setCode(200);
						response.setMessage("Success");
						response.setSuccess(true);
					}
				} else {
					log.info("Fulfillment Order Delivery Id is  Null");
					int enteredQuantity = request.getQuantity();
					int imeiQuantity = lookupIMEIWithStatus(request.getFulFillmentOrderId(), request.getItemId());
					log.info("lookupIMEI() : IMEI Quantity " + imeiQuantity + " and User Entered Quantity "
							+ enteredQuantity);
					if (imeiQuantity < enteredQuantity) {
						response.setCode(404);
						response.setSuccess(false);
						response.setMessage("Please Enter the IMEI Number");
						log.info("IMEI is not Inserted, Please Enter IMEI Number before Save or Dispatch");
						throw new Exception("Please Enter the IMEI Number");
					} else if (imeiQuantity > enteredQuantity) {
						response.setCode(404);
						response.setSuccess(false);
						response.setMessage("IMEI Quantity Cannot be Greater than the Quantity Entered");
						log.info("IMEI Quantity Cannot be Greater than the Quantity Entered");
						throw new Exception("IMEI Quantity Cannot be Greater than the Quantity Entered");
					} else {
						log.info("IMEI Quantity equal to the Quantity Entered");
						response.setCode(200);
						response.setMessage("Success");
						response.setSuccess(true);
					}
				}
			} else {
				log.info("UIN is not Enabled");
				response.setCode(200);
				response.setMessage("UIN is not Enabled");
				response.setSuccess(true);
				return response;
			}

		} catch (Exception e) {
			log.info("Exception occured while checking if IMEI is entered or not. The error is " + e.getMessage());
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		}
		return response;
	}

	private boolean uinEnableCheck(Long StoreId, String ItemId) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		Connection conn = null;
		boolean captureId = false;
		log.info("Inside uinEnableCheck() : checking if UIN is enabled or not for the store " + StoreId + " and Item "
				+ ItemId);
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			selectQuery = "select CAPTURE_TIME_ID from STORE_UIN_ADMIN_ITEM where store_id='" + StoreId
					+ "' and item_id='" + ItemId + "'";
			log.info(selectQuery.toString());
			try {
				rs = stmt.executeQuery(selectQuery);
				while (rs.next()) {
					int captureTimeId = rs.getInt("CAPTURE_TIME_ID");
					log.info("Capture Time Id for the store " + StoreId + " and Item " + ItemId + " is "
							+ captureTimeId);
					if (captureTimeId == 1) {
						captureId = true;
					}
				}
			} catch (Exception ex) {
				captureId = false;
				ex.printStackTrace();
				throw new Exception("Error :" + ex.getMessage());
			}
		} catch (Exception e) {
			captureId = false;
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return captureId;
	}

	private int lookupIMEIEntered(Long fulOrderId, Long fulOrdDlvId, String ItemId) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		Connection conn = null;
		int imeiEntered = 0;
		try {
			log.info("inside lookupIMEIEntered() ");
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			selectQuery = "SELECT COUNT(IMEI_NUMBER) AS COUNT_IMEI FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
					+ fulOrderId + " and ITEM_ID='" + ItemId + "' and FUL_ORD_DLV_ID=" + fulOrdDlvId;
			log.info(selectQuery.toString());
			try {
				rs = stmt.executeQuery(selectQuery);
				while (rs.next()) {
					imeiEntered = rs.getInt("COUNT_IMEI");
					log.info("count of IMEI entered for the Fulfillment Order Id=" + fulOrderId + " and Delivery Id"
							+ fulOrdDlvId + " is " + imeiEntered);
				}
			} catch (Exception ex) {
				imeiEntered = 0;
				log.info("Exception occured while checking the count of IMEI entered for the DeliveryId : "
						+ fulOrdDlvId);
				ex.printStackTrace();
				throw new Exception("Error :" + ex.getMessage());
			}
		} catch (Exception e) {
			imeiEntered = 0;
			log.info("Exception occured while checking the count of IMEI entered for the DeliveryId : " + fulOrdDlvId);
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return imeiEntered;
	}

	private int lookupIMEIWithStatus(Long fulOrdId, String ItemId) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		Connection conn = null;
		int imeiEntered = 0;
		try {
			log.info("inside lookupIMEIWithStatus() ");
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			selectQuery = "SELECT COUNT(IMEI_NUMBER) AS COUNT_IMEI FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
					+ fulOrdId + " and ITEM_ID='" + ItemId + "' and STATUS='N'";
			log.info(selectQuery.toString());
			try {
				rs = stmt.executeQuery(selectQuery);
				while (rs.next()) {
					imeiEntered = rs.getInt("COUNT_IMEI");
					log.info("count of IMEI entered for the Fulfillment Order Id=" + fulOrdId + " with Status'N' is "
							+ imeiEntered);
				}
			} catch (Exception ex) {
				imeiEntered = 0;
				log.info("Exception occured while checking the count of IMEI entered of new delivery");
				ex.printStackTrace();
				throw new Exception("Error :" + ex.getMessage());
			}
		} catch (Exception e) {
			imeiEntered = 0;
			log.info("Exception occured while checking the count of IMEI entered of new delivery");
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return imeiEntered;
	}

	@Override
	public SuccessResponse updateIndicatorIMEI(ExtraIMEIFulfillmentOrderDelivery request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Long fuOrdDlvId = null;
		log.info("Inside updateIndicatorIMEI()");
		try {
			fuOrdDlvId = request.getFulOrdDlvId();
			if (fuOrdDlvId != null) {
				log.info("DeliveryId is not null");
				updateIMEIIndicatorWithDlvId(request);
				log.info("Successfully updated the IMEI indicator");
				response.setCode(200);
				response.setSuccess(true);
				response.setMessage("Success");
			} else {
				log.info("DeliveryId is null");
				updateIMEIIndicatorWithoutDlvId(request);
				log.info("Successfully updated the IMEI indicator");
				response.setCode(200);
				response.setSuccess(true);
				response.setMessage("Success");
			}

		} catch (Exception e) {
			log.info("Exception occured while updating the IMEI Indicator");
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			throw new Exception("Error :" + e.getMessage());
		}
		return response;
	}

	private void updateIMEIIndicatorWithoutDlvId(ExtraIMEIFulfillmentOrderDelivery request) throws Exception {
		Statement stmt = null;
		String updateQuery = "";
		Connection conn = null;
		boolean updateStatus = true;
		log.info("Inside updateIMEIIndicatorWithoutDlvId() ");
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			conn.setAutoCommit(false);
			for (ExtraFulfillmentOrderLineItem lineItem : request.getLineItems()) {
				stmt = conn.createStatement();
				updateQuery = "update XX_FUL_ORD_DLV_LINE_ITEM_UIN set status='" + request.getStatus()
						+ "'where FUL_ORD_ID=" + lineItem.getFulFillmentOrderId() + " and ITEM_ID="
						+ lineItem.getItemId() + " and STATUS='N'";
				log.info(updateQuery.toString());
				try {
					stmt.executeUpdate(updateQuery);
				} catch (Exception ex) {
					conn.rollback();
					updateStatus = false;
					ex.printStackTrace();
					throw new Exception("Failed to update Dispatch Indicator and the Error is " + ex.getMessage());
				}
			}
			if (updateStatus) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (Exception e) {
			updateStatus = false;
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	private void updateIMEIIndicatorWithDlvId(ExtraIMEIFulfillmentOrderDelivery request) throws Exception {
		Statement stmt = null;
		String updateQuery = "";
		Connection conn = null;
		boolean updateStatus = true;
		log.info("Inside updateIMEIIndicatorWithDlvId() ");
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			conn.setAutoCommit(false);
			for (ExtraFulfillmentOrderLineItem lineItem : request.getLineItems()) {
				stmt = conn.createStatement();
				updateQuery = "update XX_FUL_ORD_DLV_LINE_ITEM_UIN set status='" + request.getStatus()
						+ "'where FUL_ORD_ID=" + lineItem.getFulFillmentOrderId() + " and ITEM_ID="
						+ lineItem.getItemId() + "and FUL_ORD_DLV_ID=" + lineItem.getFulOrdDlvId()
						+ " and STATUS IN('N','S')";
				log.info(updateQuery.toString());
				try {
					stmt.executeUpdate(updateQuery);
				} catch (Exception ex) {
					conn.rollback();
					updateStatus = false;
					ex.printStackTrace();
					throw new Exception("Failed to update Dispatch Indicator and the Error is " + ex.getMessage());
				}
			}
			if (updateStatus) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			updateStatus = false;
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	@Override
	public SuccessResponse lookupUINEnabled(LookupUIN request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		Connection conn = null;
		boolean captureId = false;
		log.info("Inside lookupUINEnabled() : checking if UIN is enabled or not for the store " + request.getStoreId()
				+ " and Item " + request.getItemId());
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			selectQuery = "select CAPTURE_TIME_ID from STORE_UIN_ADMIN_ITEM where store_id='" + request.getStoreId()
					+ "' and item_id='" + request.getItemId() + "'";
			log.info(selectQuery.toString());
			try {
				rs = stmt.executeQuery(selectQuery);
				while (rs.next()) {
					int captureTimeId = rs.getInt("CAPTURE_TIME_ID");
					log.info("Capture Time Id for the store " + request.getStoreId() + " and Item "
							+ request.getItemId() + " is " + captureTimeId);
					if (captureTimeId == 1) {
						captureId = true;
					}
				}
			} catch (Exception ex) {
				log.info("Exception occured while checking the UIN is enabeld for the Item or not");
				response.setCode(404);
				response.setSuccess(false);
				response.setMessage("exception occured");
				ex.printStackTrace();
				throw new Exception("Error :" + ex.getMessage());
			}
			if (captureId) {
				log.info("UIN is enabled for the store " + request.getStoreId() + " and Item " + request.getItemId());
				response.setCode(200);
				response.setSuccess(true);
				response.setMessage("UIN_ENABLED");
			}

		} catch (Exception e) {
			log.info("Exception occured while checking the UIN is enabeld for the Item or not");
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return response;
	}

	@Override
	public SuccessResponse lookupIMEIQty(UniqueSerialNumber request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Long fulOrdId = 0l;
		Long fulOrdDlvItemId = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		Connection conn = null;
		int quantity = 0;
		int imeiQuantity = 0;
		try {
			log.info("Inside lookupIMEIQty() ");
			fulOrdId = request.getFulOrdId();
			fulOrdDlvItemId = request.getFulOrdDlvLineItemId();
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			int pickedQuantity = getPickedQuantity(fulOrdId, fulOrdDlvItemId, conn);
			quantity = request.getQuantity();
			stmt = conn.createStatement();
			query = "SELECT SUM(QUANTITY) AS IMEI_QTY FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID=" + fulOrdId
					+ " AND FUL_ORD_LINE_ITEM_ID=" + fulOrdDlvItemId + " AND STATUS IN ('D', 'S')";
			log.info(query.toString());

			try {
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					imeiQuantity = rs.getInt("IMEI_QTY");
					log.info("IMEI quantity from XX_FUL_ORD_DLV_LINE_ITEM_UIN " + imeiQuantity);
				}
				int totalImeiQuantity = quantity + imeiQuantity;
				log.info("IMEI Quantity=" + totalImeiQuantity + " and Picked Quantity " + pickedQuantity);
				if (totalImeiQuantity < pickedQuantity || totalImeiQuantity == pickedQuantity) {
					response.setCode(200);
					response.setSuccess(true);
					response.setMessage("Success");
				} else {
					log.info("IMEI Qty cannot be more than Picked Quantity");
					response.setCode(404);
					response.setSuccess(false);
					response.setMessage("IMEI Qty cannot be more than Picked Quantity");
					throw new Exception("IMEI Qty cannot be more than Picked Quantity");
				}
			} catch (Exception exp) {
				response.setCode(404);
				response.setSuccess(false);
				response.setMessage("exception occured");
				exp.printStackTrace();
				throw new Exception(exp.getMessage());
			}

			response.setCode(200);
			response.setSuccess(true);
			response.setMessage("Success");
		} catch (Exception e) {
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());

		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return response;
	}

	private void deleteExistingFulOrdDlvId(List<UniqueSerialNumber> request) throws Exception {
		Connection con = null;
		Statement stmt = null;
		String deleteQuery = "";

		try {

			log.info("inside deleteExistingFulOrdDlvId() ");
			log.info("Getting the connection..");
			con = jdbcTemplate.getDataSource().getConnection();
			if (con != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			for (UniqueSerialNumber imeiNumber : request) {
				Long deliveryId = 0l;
				deliveryId = imeiNumber.getFulOrdDlvId();
				log.info("deleting existing IMEI details with Delivery Id " + imeiNumber.getFulOrdDlvId());
				stmt = con.createStatement();
				if (deliveryId != null) {
					deleteQuery = "DELETE FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
							+ imeiNumber.getFulOrdId() + " AND FUL_ORD_LINE_ITEM_ID="
							+ imeiNumber.getFulOrdDlvLineItemId() + " AND FUL_ORD_DLV_ID=" + deliveryId
							+ " AND STATUS IN ('N', 'S')";
				} else {
					deleteQuery = "DELETE FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
							+ imeiNumber.getFulOrdId() + " AND FUL_ORD_LINE_ITEM_ID="
							+ imeiNumber.getFulOrdDlvLineItemId() + " AND STATUS IN ('N')";
				}
				log.info(deleteQuery.toString());
				try {
					stmt.executeUpdate(deleteQuery);
				} catch (SQLException exp) {
					log.info("Error while deleting the existing IMEI details of Delivery Id "
							+ imeiNumber.getFulOrdDlvId());
					exp.printStackTrace();
					throw new Exception(exp.getMessage());
				}
			}

		} catch (Exception exception) {
			log.info("Error while deleting the existing IMEI details of In Progress Delivery");
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		} finally {
			if (con != null) {
				con.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	private int getPickedQuantity(Long fulOrdId, Long fulOrdDlvItemId, Connection con) throws Exception {
		Statement createStatement = null;
		String pickedQuantityQuery = "";
		ResultSet rs = null;
		int pickedQuantity = 0;
		try {
			log.info("inside getPickedQuantity() : retrieving the picked quantity from FUL_ORD_PICK_LINE_ITEM table");
			createStatement = con.createStatement();
			pickedQuantityQuery = "SELECT SUM(QUANTITY_PICKED) AS PICKED_QTY FROM FUL_ORD_PICK_LINE_ITEM WHERE FUL_ORD_ID="
					+ fulOrdId + " AND FUL_ORD_LINE_ITEM_ID=" + fulOrdDlvItemId;
			log.info(pickedQuantityQuery.toString());
			rs = createStatement.executeQuery(pickedQuantityQuery);
			while (rs.next()) {
				pickedQuantity = rs.getInt("PICKED_QTY");
				log.info("Picked Quantity from FUL_ORD_PICK_LINE_ITEM " + pickedQuantity);
			}
		} catch (Exception exception) {
			log.info("Exception occured while retrieving the picked quantity from FUL_ORD_PICK_LINE_ITEM table");
			throw new Exception(
					"Exception occured while retrieving the picked quantity from FUL_ORD_PICK_LINE_ITEM table");
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (createStatement != null) {
				createStatement.close();
			}
		}
		return pickedQuantity;

	}

	@Override
	public SuccessResponse cancelDelivery(IMEICancelDelivery request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Statement stmt = null;
		ResultSet rs = null;
		String deleteQuery = "";
		Connection conn = null;
		log.info("Inside cancelDelivery()");
		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			deleteQuery = "DELETE FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID=" + request.getFulfillmentOrderId()
					+ " AND FUL_ORD_DLV_ID=" + request.getDeliveryId() + " AND STATUS NOT IN ('D')";
			log.info(deleteQuery.toString());
			try {
				stmt.executeUpdate(deleteQuery);
				response.setCode(200);
				response.setSuccess(true);
				response.setMessage("Success");
			} catch (Exception ex) {
				log.info("Failed to cancel the delivery. The error is " + ex.getMessage());
				response.setCode(404);
				response.setSuccess(false);
				response.setMessage("exception occured");
				ex.printStackTrace();
				throw new Exception("Failed to Delete the Delivery..");
			}

		} catch (Exception e) {
			log.info("Failed to cancel the delivery. The error is " + e.getMessage());
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return response;
	}

	@Override
	public SuccessResponse deleteQuantityReverted(ExtraFulfillmentOrderLineItem request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Long fulOrdDlvId = null;
		log.info("Inside deleteQuantityReverted()");
		try {
			fulOrdDlvId = request.getFulOrdDlvId();
			boolean uinEnableCheck = uinEnableCheck(request.getStoreId(), request.getItemId());
			if (uinEnableCheck) {
				if (fulOrdDlvId != null) {
					log.info("Fulfillment Order Delivery Id is not Null");
					deleteWithFulOrdDlvId(request);
					response.setCode(200);
					response.setMessage("Success");
					response.setSuccess(true);
				} else {
					log.info("Fulfillment Order Delivery Id is  Null");
					deleteWithoutFulOrdDlvId(request);
					response.setCode(200);
					response.setMessage("Success");
					response.setSuccess(true);
				}
			} else {
				response.setCode(200);
				response.setMessage("UIN is not Enabled");
				response.setSuccess(true);
				return response;
			}

		} catch (Exception e) {
			log.info("Failed to delete the IMEI: " + request.getImeiNumber() + " of which Quantity is reverted to 0");
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			throw new Exception("Error :" + e.getMessage());
		}
		return response;
	}

	private void deleteWithoutFulOrdDlvId(ExtraFulfillmentOrderLineItem request) throws Exception {
		Connection con = null;
		Statement stmt = null;
		String deleteQuery = "";
		try {
			log.info("Getting the connection..");
			con = jdbcTemplate.getDataSource().getConnection();
			if (con != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = con.createStatement();
			deleteQuery = "DELETE FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID=" + request.getFulFillmentOrderId()
					+ " AND ITEM_ID=" + request.getItemId() + " AND STATUS IN ('N')";
			log.info(deleteQuery.toString());
			try {
				stmt.executeUpdate(deleteQuery);
			} catch (SQLException exp) {
				log.info("Exception occured while deleting the IMEI of which Entered Quantity is reverted to 0");
				exp.printStackTrace();
				throw new Exception(exp.getMessage());
			}

		} catch (Exception exception) {
			log.info("Exception occured while deleting the IMEI of which Entered Quantity is reverted to 0");
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		} finally {
			if (con != null) {
				con.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	private void deleteWithFulOrdDlvId(ExtraFulfillmentOrderLineItem request) throws Exception {
		Connection con = null;
		Statement stmt = null;
		String deleteQuery = "";
		try {
			log.info("inside deleteWithFulOrdDlvId() ");
			log.info("Getting the connection..");
			con = jdbcTemplate.getDataSource().getConnection();
			if (con != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = con.createStatement();
			deleteQuery = "DELETE FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID=" + request.getFulFillmentOrderId()
					+ " AND ITEM_ID=" + request.getItemId() + " AND FUL_ORD_DLV_ID=" + request.getFulOrdDlvId()
					+ " AND STATUS IN ('N', 'S')";
			log.info(deleteQuery.toString());
			try {
				stmt.executeUpdate(deleteQuery);
			} catch (SQLException exp) {
				log.info("Exception occured while deleting the IMEI of which Entered Quantity is reverted to 0");
				exp.printStackTrace();
				throw new Exception(exp.getMessage());
			}

		} catch (Exception exception) {
			log.info("Exception occured while deleting the IMEI of which Entered Quantity is reverted to 0");
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		} finally {
			if (con != null) {
				con.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	@Override
	public SuccessResponse ping() {
		Connection con = null;
		SuccessResponse response = new SuccessResponse();
		try {
			log.info("Getting the connection..");
			con = jdbcTemplate.getDataSource().getConnection();
			if (con != null) {
				log.info("Connection established successfully..");
				response.setCode(200);
				response.setSuccess(true);
				response.setMessage("Connection established Successfully");
			} else {
				response.setCode(404);
				response.setSuccess(false);
				response.setMessage("Problem in getting the connection");
				log.info("Connection is not established..");
			}
		} catch (Exception e) {
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("Problem in getting the connection");

			e.printStackTrace();
		} finally {

			if (con != null) {
				try {
					log.info("closing the connection..");
					con.close();
				} catch (SQLException e) {
					log.warn("Error while closing the connection", e);
				}
			}
		}
		return response;

	}

	@Override
	public EnteredSerialNumbers retrieveIMEI(UniqueSerialNumber request) throws Exception {
		EnteredSerialNumbers response = new EnteredSerialNumbers();
		try {
			String status = compareEnteredQuantity(request);
			if (status == "NO_IMEI_ENTERED") {
				log.info("No IMEI entered for this delivery..");
				response.setCode(200);
				response.setDeliveryExists(false);
				response.setMessage("NO_IMEI_ENTERED");
				response.setSuccess(true);
			} else if (status == "IMEI_COUNT_MISMATCH") {
				log.info("Entered Quanity and IMEI quantity is not matching to retrieve IMEIs..");
				deleteEnteredIMEIs(request);
				response.setCode(200);
				response.setDeliveryExists(true);
				response.setMessage("IMEI_COUNT_MISMATCH");
				response.setSuccess(true);
			} else if (status == "EQUAL") {
				log.info("Entered Quanity and IMEI quantity are equal, retrieving Entered IMEIs..");
				List<String> serialNumbers = pickEnteredIMEIs(request);
				response.setCode(200);
				response.setDeliveryExists(true);
				response.setMessage("PICKED_ENTERED_IMEI");
				response.setSuccess(true);
				response.setSerialNumbers(serialNumbers);
			} else {
				response.setCode(500);
				response.setSuccess(false);
				response.setMessage("Something went wrong while retrieving entered IMEIs");
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		}
		return response;
	}

	private List<String> pickEnteredIMEIs(UniqueSerialNumber request) throws Exception {
		Statement stmt = null;
		String selectQuery = "";
		Connection conn = null;
		ResultSet rs = null;
		Long deliveryId = null;
		List<String> imeiList = new ArrayList<String>();
		log.info("inside pickEnteredIMEIs()");
		try {
			deliveryId = request.getFulOrdDlvId();
			conn = jdbcTemplate.getDataSource().getConnection();
			stmt = conn.createStatement();
			if (deliveryId == null) {
				selectQuery = "select IMEI_NUMBER from XX_FUL_ORD_DLV_LINE_ITEM_UIN where FUL_ORD_ID="
						+ request.getFulOrdId() + " and FUL_ORD_LINE_ITEM_ID=" + request.getFulOrdDlvLineItemId()
						+ " and STATUS='N'";
			} else {
				selectQuery = "select IMEI_NUMBER from XX_FUL_ORD_DLV_LINE_ITEM_UIN where FUL_ORD_ID="
						+ request.getFulOrdId() + " and FUL_ORD_LINE_ITEM_ID=" + request.getFulOrdDlvLineItemId()
						+ " and FUL_ORD_DLV_ID=" + deliveryId;
			}
			log.info(selectQuery.toString());
			try {
				rs = stmt.executeQuery(selectQuery);
				while (rs.next()) {
					imeiList.add(rs.getString("IMEI_NUMBER"));
				}
			} catch (SQLException exp) {
				conn.rollback();
				exp.printStackTrace();
				throw new Exception(exp.getMessage());
			}
			conn.commit();

		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return imeiList;

	}

	private boolean deleteEnteredIMEIs(UniqueSerialNumber request) throws Exception {
		Statement stmt = null;
		String deleteQuery = "";
		Connection conn = null;
		boolean success = true;
		log.info("inside deleteEnteredIMEIs()");
		Long deliveryId = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			deliveryId = request.getFulOrdDlvId();
			if (deliveryId == null) {
				deleteQuery = "delete from XX_FUL_ORD_DLV_LINE_ITEM_UIN where FUL_ORD_ID=" + request.getFulOrdId()
						+ " and FUL_ORD_LINE_ITEM_ID=" + request.getFulOrdDlvLineItemId() + " and STATUS='N'";
			} else {
				deleteQuery = "delete from XX_FUL_ORD_DLV_LINE_ITEM_UIN where FUL_ORD_ID=" + request.getFulOrdId()
						+ " and FUL_ORD_LINE_ITEM_ID=" + request.getFulOrdDlvLineItemId()
						+ " and STATUS in ('N','S') and FUL_ORD_DLV_ID=" + deliveryId;
			}
			log.info(deleteQuery.toString());
			try {
				stmt.executeUpdate(deleteQuery);
			} catch (SQLException exp) {
				success = false;
				conn.rollback();
				exp.printStackTrace();
				throw new Exception(exp.getMessage());
			}
			conn.commit();

		} catch (Exception e) {
			success = false;
			if (conn != null) {
				conn.rollback();
			}
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return success;

	}

	public String compareEnteredQuantity(UniqueSerialNumber request) throws Exception {
		Long fulOrdId = 0l;
		Long fulOrdDlvItemId = 0l;
		Long deliveryId = 0l;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		Connection conn = null;
		int quantity = 0;
		int imeiQuantity = 0;
		try {
			log.info("Inside compareEnteredQuantity() ");
			fulOrdId = request.getFulOrdId();
			deliveryId = request.getFulOrdDlvId();
			fulOrdDlvItemId = request.getFulOrdDlvLineItemId();
			conn = jdbcTemplate.getDataSource().getConnection();
			quantity = request.getQuantity();
			stmt = conn.createStatement();
			if (deliveryId == null) {
				query = "SELECT SUM(QUANTITY) AS IMEI_QTY FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
						+ fulOrdId + " AND FUL_ORD_LINE_ITEM_ID=" + fulOrdDlvItemId + " AND STATUS IN ('N')";
			} else {
				query = "SELECT SUM(QUANTITY) AS IMEI_QTY FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN WHERE FUL_ORD_ID="
						+ fulOrdId + " AND FUL_ORD_LINE_ITEM_ID=" + fulOrdDlvItemId
						+ " AND STATUS IN ('N','S') AND FUL_ORD_DLV_ID=" + deliveryId;
			}
			log.info(query.toString());

			try {
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					imeiQuantity = rs.getInt("IMEI_QTY");
					log.info("IMEI quantity from XX_FUL_ORD_DLV_LINE_ITEM_UIN " + imeiQuantity);
				}
				if (quantity == imeiQuantity) {
					return "EQUAL";
				} else if (imeiQuantity == 0) {
					return "NO_IMEI_ENTERED";
				} else {
					return "IMEI_COUNT_MISMATCH";
				}
			} catch (Exception exp) {
				exp.printStackTrace();
				throw new Exception(exp.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());

		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public SuccessResponse checkIfBOLexists(List<UniqueSerialNumber> request) throws Exception {
		SuccessResponse response = new SuccessResponse();
		Statement stmt = null;
		Connection conn = null;
		Long fulOrdDlvId = null;
		Long fulOrdId = null;
		log.info("inside checkIfBOLexists()");
		try {
			if (request.size() == 0) {
				response.setCode(200);
				response.setSuccess(true);
				response.setMessage("No Data");
				return response;
			}
			log.info("Checking if BOL is assigned");
			boolean isBOLAssigned = false;
			fulOrdDlvId = request.get(0).getFulOrdDlvId();
			fulOrdId = request.get(0).getFulOrdId();
			if (fulOrdDlvId != null) {
				isBOLAssigned = checkBOLExists(fulOrdDlvId, fulOrdId);
			}
			if (isBOLAssigned == false) {
				response.setCode(200);
				response.setSuccess(true);
				response.setMessage("Success");
			} else {
				response.setCode(404);
				response.setSuccess(false);
				response.setMessage("BOL Already Assigned");
			}

		} catch (Exception e) {
			response.setCode(404);
			response.setSuccess(false);
			response.setMessage("exception occured");
			e.printStackTrace();
			throw new Exception("Error :" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return response;
	}

}
