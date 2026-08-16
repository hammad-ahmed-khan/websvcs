package org.logicInfo.oms.transferCreation.Controller;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;

@Transactional
@Repository("TsfCreationDAO")
public class TransferCreationDAO implements TsfCreationDAO {

	TransferCreationDAO() {
		log.info(" Default Constructor TransferCreationDAO is Executed");
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final static Logger log = Logger.getLogger(TransferCreationDAO.class.getName());

	public TransferCreationResponse getResponse(int src_id, int dest_id, String refNo, String[] item, int[] qty) throws SQLException {
		log.info("Inside TransferCreationDAO");
		Connection conn = null;
		Array reportsArray = null;
		Object[][] objType = null;
		TransferCreationResponse tcr = new TransferCreationResponse();
		String packageCallStmt = "{? = call XTRA_TSF_CRE_SQL.XTRA_XTSF_CRE(?, ?, ?, ?) }";
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			log.info("Inside TransferCreationDAO - Try");
			if ((item.length == qty.length) && item.length > 0) {
				objType = new Object[item.length][2];
				for (int ii = 0; ii < item.length; ii++) {
					log.info("Inside TransferCreationDAO - For");
					objType[ii][0] = item[ii];
					objType[ii][1] = qty[ii];
					log.info("Inside TransferCreationDAO - For ITEM:" + objType[ii][0] + " :QTY: " + objType[ii][1] + " : " + item[ii] + " : " + qty[ii]);
				}
			} else {
				tcr.setCode(500);
				tcr.setMessage("Item and Qty numbers are not matching");
				tcr.setSuccess(false);
				return tcr;
			}
			reportsArray = ((OracleConnection) conn).createOracleArray("XXTSF_TBL_TYPE", objType);
			CallableStatement oracleCallableStmt = conn.prepareCall(packageCallStmt);
			oracleCallableStmt.registerOutParameter(1, OracleTypes.VARCHAR);
			oracleCallableStmt.setArray(2, reportsArray);
			oracleCallableStmt.setInt(3, src_id);
			oracleCallableStmt.setInt(4, dest_id);
			oracleCallableStmt.setString(5, refNo);
			log.info("Inside TransferCreationDAO - Before function call");
			oracleCallableStmt.execute();
			log.info("Inside TransferCreationDAO - After function call");
			String tsf = ((OracleCallableStatement) oracleCallableStmt).getString(1);
			if (tsf.contains("transfer") || tsf.contains("ORA")) {
				tcr.setTsf_No(null);
				tcr.setCode(500);
				tcr.setSuccess(false);
				tcr.setMessage(((OracleCallableStatement) oracleCallableStmt).getString(1));
			} else {
				tcr.setTsf_No(((OracleCallableStatement) oracleCallableStmt).getString(1));
				tcr.setCode(200);
				tcr.setSuccess(true);
				tcr.setMessage(null);
			}
			log.info("Inside TransferCreationDAO - Tsf Number:" + tcr.getTsf_No());
		} catch (Exception e) {
			log.info("Inside Catch transfer creation package call " + e.getMessage());
		}
		return tcr;
	}

	@Override
	public ArrayList<Items> findIncorrectItem(int srcLoc, int destLoc, ArrayList<TransferRequest> customerItems) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		Connection conn = null;
		ArrayList<Items> items = new ArrayList<>();

		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			for (TransferRequest item : customerItems) {
				// selectQuery = "select item from item_loc where loc=" + loc + " and item='" +
				// item.getItem() + "'";
				// query to check the whether item exist in both src and dest
				selectQuery = "select item from item_loc where loc=" + srcLoc + " and item in(select item from item_loc where item ='" + item.getItem() + "'" + " and loc=" + destLoc + ")";
				log.info("Invalid item Query :" + selectQuery.toString());
				try {
					rs = stmt.executeQuery(selectQuery);
					if (!rs.next()) {
						Items itm = new Items();
						itm.setItem(item.getItem());
						itm.setQuantity(item.getQty());
						items.add(itm);
					}
				} catch (Exception ex) {
					log.info("Exception in findIncorrectItem exceuting the query :" + ex.getMessage());
				}
			}

		} catch (Exception e) {
			log.info("Exception in findIncorrectItem :" + e.getMessage());
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
		return items;
	}

	@Override
	public ArrayList<Items> findInvalidItem(int srcLoc, int destLoc, ArrayList<TransferRequest> customerItems) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		Connection conn = null;
		ArrayList<Items> items = new ArrayList<>();

		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			for (TransferRequest item : customerItems) {
				// selectQuery = "select status from item_loc where loc=" + loc + " and item='"
				// + item.getItem() + "'";
				// query checks the status in both src and dest
				selectQuery = "select status from item_loc where loc=" + srcLoc + " and item in(select item from item_loc where loc=" + destLoc + " and item='" + item.getItem() + "'"
						+ "and status in('A','C'))";
				log.info("inactive_items query " + selectQuery.toString());
				try {
					rs = stmt.executeQuery(selectQuery);
					if (rs.next()) {
						String status = rs.getString("STATUS");
						if (status.equalsIgnoreCase("I") || status.equalsIgnoreCase("D")) {
							Items itm = new Items();
							itm.setItem(item.getItem());
							itm.setQuantity(item.getQty());
							items.add(itm);
						} else {
						}
					} else {
						Items itm = new Items();
						itm.setItem(item.getItem());
						itm.setQuantity(item.getQty());
						items.add(itm);
					}
				} catch (Exception ex) {
					log.info("Exception in findInvalidItem status check  :" + ex.getMessage());
				}
			}

		} catch (Exception e) {
			log.info("Exception in findInvalidItem :" + e.getMessage());
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
		return items;
	}

	@Override
	public ArrayList<Items> findStockPerLoc(int loc, ArrayList<TransferRequest> customerItems) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		Connection conn = null;
		ArrayList<Items> items = new ArrayList<>();

		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			for (TransferRequest item : customerItems) {
				selectQuery = "select avail_qty from xx_oms_invavail_v where loc=" + loc + " and item='" + item.getItem() + "'";
				log.info("selectQuery " + selectQuery.toString());
				try {
					rs = stmt.executeQuery(selectQuery);
					while (rs.next()) {
						int availableQty = rs.getInt("AVAIL_QTY");
						log.info("RequestedQty for an item  " + item.getItem() + " : " + item.getQty());
						log.info("AvailableQty for an item  " + item.getItem() + " : " + availableQty);

						log.info("selectQuery " + selectQuery.toString());
						if (availableQty < item.getQty()) {
							Items itm = new Items();
							itm.setItem(item.getItem());
							itm.setQuantity(availableQty);
							items.add(itm);
						}

					}

				} catch (Exception ex) {
					log.info("Exception in getting the avail qty :" + ex.getMessage());
				}
			}

		} catch (Exception e) {
			log.info("Exception in findStockPerLoc :" + e.getMessage());

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
		return items;
	}

	@Override
	public String transferExists(String refNo) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String selectQuery = "";
		String tsfNo = "";
		Connection conn = null;

		try {
			log.info("Getting the connection..");
			conn = jdbcTemplate.getDataSource().getConnection();
			if (conn != null)
				log.info("Connection established successfully..");
			else
				log.info("Connection is not established..");
			stmt = conn.createStatement();
			selectQuery = "select TSF_NO from TSFHEAD WHERE COMMENT_DESC ='" + refNo + "' and ROWNUM = 1";
			log.info(selectQuery.toString());
			try {
				rs = stmt.executeQuery(selectQuery);
				if (rs.next()) {
					tsfNo = rs.getString("TSF_NO");
					log.info("Transfer already exists");
				} else {
					log.info("Transfer doesnot exists");
				}
			} catch (Exception ex) {
				log.info("Exception in transferExists query check" + ex.getMessage());
			}

		} catch (Exception e) {
			log.info("Exception in transferExists " + e.getMessage());
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
		return tsfNo;
	}

	@Override
	public void saveRequestandResponse(TransferCreationRequest req, TransferCreationResponse resp) {

		log.info("Inside TsfCreationController for saveRequestandResponse method");
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			String requestQuery = "insert into OMS_NOON_TRANSFER_SERVICES (service_id,service_type,request,response,CREATE_DATETIME) values(noon_transfer_service_id.nextval,?,?,?,SYSTIMESTAMP)";
			pstmt = conn.prepareStatement(requestQuery);
			Gson gson = new Gson();
			String gsonRequestString = gson.toJson(req);
			String gsonResponeString = gson.toJson(resp);
			Reader requestReader = new StringReader(gsonRequestString);
			Reader responseReader = new StringReader(gsonResponeString);
			pstmt.setString(1, "TransferCreation");
			pstmt.setClob(2, requestReader);
			pstmt.setClob(3, responseReader);

			int i = pstmt.executeUpdate();

		} catch (SQLException e) {
			log.info("Exception in persisting transfer request record into OMS_NOON_TRANSFER_SERVICES for the saveRequestandResponse : " + req.getRef_no() + " : " + e.getMessage());
		}

	}

}
