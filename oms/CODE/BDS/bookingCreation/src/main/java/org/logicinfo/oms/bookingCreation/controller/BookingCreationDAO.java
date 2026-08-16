package org.logicinfo.oms.bookingCreation.controller;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;

@Transactional
@Repository("bookingDAO")
public class BookingCreationDAO implements BookingDAO {
	private static final Logger log = Logger.getLogger(BookingCreationDAO.class);

	BookingCreationDAO() {
		log.info(" Default Constructor bookingCreationDAO is Executed");
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Struct[] structs = null;
	java.sql.Date sqlDate;
	Map<String, List<BookingsResponse>> bookingRespMap = new ConcurrentHashMap<String, List<BookingsResponse>>();

	// createBookingRespList=new ArrayList<CreateBookingResponse>();

	public DeliveriesResponse getResponse(int resvId, String orderNo, List<Deliveries> deliveries, int index, int grpId, int bsize, int lineNo, String itemCode, int orderQty, Date reqDate,
			String window, int slots, int region, String custCity, String custArea, String custName, String custAddr, String custMob, String custLat, String custLng) throws SQLException {

		Connection conn = null;
		int resultsetBookingId = 0;
		String resultsetStatus = null;
		String resultsetMsg = null;
		ResultSet rSet = null;
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmtInsert = null;
		String selectQuery = "";
		int itemSize = 0;

		String packageCallStmt = "{call XXHDB_CORE_PKG.reserve_booking(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		List<CreateBookingResponse> createBookingRespList = new ArrayList<CreateBookingResponse>();
		CreateBookingResponse createBookingResp = new CreateBookingResponse();
		// ArrayList<BookingsResponse> bookingsResponse = new
		// ArrayList<BookingsResponse>();
		DeliveriesResponse delvresponse = new DeliveriesResponse();
		ArrayList<DeliveriesResponse> delvList = new ArrayList<DeliveriesResponse>();

		Status status = new Status();

		try {

			structs = new Struct[deliveries.size()];

			int cusCity = 0;
			int cusArea = 0;
			int delvId = 0;
			int groupId = 0;
			int bookingId = 0;
			log.info("Inside DAO: booking Reservation: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
			delvId = deliveries.get(index).getDeliveryId();
			delvresponse.setDeliveryId(delvId);
			log.info("Delivery Id:" + delvresponse.getDeliveryId());
			sqlDate = null;
			if (reqDate != null) {
				sqlDate = new java.sql.Date(reqDate.getTime());
			}
			log.info("Date:" + sqlDate + ":" + reqDate);
			conn = jdbcTemplate.getDataSource().getConnection();
			stmt = conn.createStatement();
			stmtInsert = conn.createStatement();
			OracleCallableStatement oracleCallableStmt = (OracleCallableStatement) conn.prepareCall(packageCallStmt);
			Array reportsArray = null;
			Object[][] objType = null;

			int d = index;
			int b = bsize;
			// for(int d=index; d<deliveries.size();d++){
			// for(int b=bsize; b<deliveries.get(d).getBookings().size();b++){
			itemSize = deliveries.get(d).getBookings().get(b).getItems().size();

			objType = new Object[itemSize][4];
			
			String insQuery = "";
			String dept = "";
			
			stmt1 = conn.createStatement();
			for (int ii = 0; ii < itemSize; ii++) {

				itemCode = deliveries.get(d).getBookings().get(b).getItems().get(ii).getItemCode();
				log.info("Inside FOR - Order Qty " + deliveries.get(d).getBookings().get(b).getItems().get(ii).getOrderQty() + ":" + itemCode + ":"
						+ deliveries.get(d).getBookings().get(b).getItems().get(ii).getRmsGroupNumber());
				// Object[][] obj = new Object[itemSize][4];
				objType[ii][0] = new Integer(deliveries.get(d).getBookings().get(b).getItems().get(ii).getLineNo());
				objType[ii][1] = new String(itemCode);
				objType[ii][2] = new Integer(deliveries.get(d).getBookings().get(b).getItems().get(ii).getOrderQty());

				insQuery = "select DEPT from ITEM_MASTER WHERE ITEM ='" + itemCode + "'";
				log.info("***DEPT***" + itemCode);
				log.info("InsQry::: " + ii + " ::: " + insQuery);
				rSet = stmt1.executeQuery(insQuery);
				if (rSet.isBeforeFirst()) {
					while (rSet.next()) {
						log.info("***Inside rset***");
						dept = rSet.getString("DEPT");
						if (dept.equals("402") || dept.equals("411")) {
							objType[ii][3] = "Y";
						} else {
							objType[ii][3] = "N";
						}
						log.info("InstallFlag:" + objType[ii][3] + ":" + dept);
					}
				} else {
					log.info("***INSIDE ELSE rSet***" + insQuery);
				}
//					}				
				log.info("After obj: Line No :" + objType[ii][0] + ":Item Code:" + objType[ii][1] + ":Order Qty:" + objType[ii][2] + ":Install Flag:" + objType[ii][3]);

			}

			// }
			reportsArray = ((OracleConnection) conn).createOracleArray("XXHDB_TBL_TYPE", objType);
			// System.out.println("Inside DAO -
			// reportsArray:"+reportsArray.getArray()+":"+reportsArray.toString());
			log.info("Inside DAO - reportsArray");
			// }

			custCity = custCity.toUpperCase().replace("'", "");
			custArea = custArea.toUpperCase().replace("'", "");

			/*
			 * selectQuery =
			 * "select b.city_id, c.area_id from xx_dlvry_cities b, xx_dlvry_areas c where c.city_id = b.city_id and upper(b.city_name) ='"
			 * + custCity + "'and upper(c.area_name) = '" + custArea + "'";
			 */
			selectQuery = "select CITY_ID, AREA_ID from XX_DLVRY_REGIONS_V where upper(city_name) ='" + custCity + "'and upper(area_name) = '" + custArea + "'";
			ResultSet res = stmt.executeQuery(selectQuery);
			log.info("Before while");
			while (res.next()) {
				oracleCallableStmt.setInt(9, res.getInt("CITY_ID"));
				oracleCallableStmt.setInt(10, res.getInt("AREA_ID"));
			}
			float f = resvId;

			oracleCallableStmt.setInt(1, resvId);
			oracleCallableStmt.setString(2, orderNo);
			oracleCallableStmt.setInt(3, grpId);
			oracleCallableStmt.setArray(4, reportsArray);
			oracleCallableStmt.setDate(5, sqlDate);
			oracleCallableStmt.setString(6, window);
			oracleCallableStmt.setInt(7, slots);
			oracleCallableStmt.setInt(8, region);
			oracleCallableStmt.setString(11, custName);
			oracleCallableStmt.setString(12, custAddr);
			oracleCallableStmt.setString(13, custMob);
			oracleCallableStmt.setString(14, custLat);
			oracleCallableStmt.setString(15, custLng);
			oracleCallableStmt.registerOutParameter(16, OracleTypes.VARCHAR);
			oracleCallableStmt.registerOutParameter(17, OracleTypes.VARCHAR);
			log.info("Before execute: Booking Reservation:" + orderNo + ":" + objType[0][0] + ":" + objType[0][1] + ":" + objType[0][2] + " - TimeStamp :"
					+ new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

			oracleCallableStmt.executeUpdate();
			log.info("After execute: Booking Reservation:" + " - TimeStamp :" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
			if (null != ((OracleCallableStatement) oracleCallableStmt).getString(16)) {
				resultsetStatus = ((OracleCallableStatement) oracleCallableStmt).getString(16);
			}
			if (null != ((OracleCallableStatement) oracleCallableStmt).getString(17)) {
				resultsetMsg = ((OracleCallableStatement) oracleCallableStmt).getString(17);
			}

			if (resultsetStatus == "F" || resultsetStatus.equals("F")) {
				status.setSuccess(false);
				if (resultsetMsg.contains("no available slot at this time")) {
					status.setCode("E-906");
				} else {
					status.setCode("");
				}
				status.setMessage(resultsetMsg);
			} else {
				status.setSuccess(true);
				status.setCode("");
				status.setMessage(resultsetMsg);
			}

			delvresponse.setDeliveryId(delvId);

			BookingsResponse bkresponse = new BookingsResponse();

			bkresponse.setGroupId(grpId);
			bkresponse.setReservationId(resvId);
			delvresponse.setBookings(bkresponse);

			if (null != status.getMessage())
				delvresponse.setStatuss(status);
			delvList.add(delvresponse);

			createBookingResp.setDeliveries(delvList);

		} catch (Exception e) {
			log.info("***>>>>>>>>>>>>>>>>>>>>>>>>>>>>Exception:" + e);
			status.setSuccess(false);
			status.setCode("");
			status.setMessage(e.getMessage());
			// createBookingResp.setStatus(status);
			createBookingRespList.add(createBookingResp);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmtInsert != null) {
				stmtInsert.close();
			}
			if (rSet != null) {
				rSet.close();
			}
			if (conn != null) {
				conn.close();
			}
		}


		return delvresponse;
	}

	public Long saveRequest(CreateBookingRequest sub) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Long seq = null;
		try {
			log.info("inside dao, Inserting the Request body for the Order Number: Booking Reservation" + sub.getOrderNo() + " Date:"
					+ new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
			con = jdbcTemplate.getDataSource().getConnection();
			if (con != null)
				log.info("Connection Established Successfully..");
			String keys[] = { "SEQUENCE_NO" };
			pstmt = con.prepareStatement(
					"insert into OMS_ORDER_BOOKING_HEAD_INFO (SEQUENCE_NO, ORDER_NO,REQUEST,STATUS,ORD_IND,SOURCE,CREATE_DATETIME) values(XX_BOOK_SEQ.nextval, ?,?,?,?,?,SYSTIMESTAMP)", keys);
			pstmt.setString(1, sub.getOrderNo());
			Gson gson = new Gson();
			String gsonString = gson.toJson(sub);
			Reader reader = new StringReader(gsonString);
			pstmt.setClob(2, reader);
			pstmt.setString(3, "N");
			pstmt.setInt(4, 0);
			pstmt.setString(5, "RES");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				seq = rs.getLong(1);
			}
		} catch (Exception exception) {
			log.info("Failed to insert Request Body while Inserting the Request", exception);
			exception.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return seq;
	}

	public void saveResponse(String orderNumber, List<CreateBookingResponse> sAResposne, Long seqId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			if (con != null)
				log.info("Connection Established Successfully..");
			pstmt = con.prepareStatement("update OMS_ORDER_BOOKING_HEAD_INFO set response=?,status='S',ord_ind=1, UPDATE_DATETIME = SYSTIMESTAMP where SEQUENCE_NO = ? ");
			Gson gson = new Gson();
			String gsonString = gson.toJson(sAResposne);
			Reader reader = new StringReader(gsonString);
			pstmt.setClob(1, reader);
			pstmt.setLong(2, seqId);
			pstmt.executeUpdate();
		} catch (Exception exception) {
			log.info("Failed to insert Response.. ", exception);
			exception.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
