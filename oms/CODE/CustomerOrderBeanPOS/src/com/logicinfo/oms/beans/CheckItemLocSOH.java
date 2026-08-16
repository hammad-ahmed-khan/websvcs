package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.sql.DataSource;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

public class CheckItemLocSOH {
	public CheckItemLocSOH() {
		super();
	}

	private final static Logger log = Logger.getLogger(CheckItemLocSOH.class.getName());

	public long checkSOH(String item, BigDecimal loc) throws SOAPException {
		log.info("***Start checkSOH***");
		int SOH = 0;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Check SOH in DAS schema
		log.info("item " + item);
		log.info("loc " + loc);
		String query = "select avail_qty from V_AVAIL_INV_POSITION where item= ? and location= ?";
		try {
			conn = createConnection();
			preparedStatement = conn.prepareStatement(query);
			log.info("before parsing");
			// log.info(Integer.parseInt(item));
			preparedStatement.setString(1, item);
			// preparedStatement.setLong(1,Long.parseLong(item));
			log.info(loc.intValue());
			preparedStatement.setInt(2, loc.intValue());
			rs = preparedStatement.executeQuery();
			log.info("before rs");
			while (rs.next()) {
				log.info("before avail");
				SOH = rs.getInt("avail_qty");
				log.info("SOH : " + SOH);
			}
		} catch (SQLException e) {
			log.info(e.getMessage());
		} finally {
			try {
				rs.close();
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return SOH;
	}

	public long findSupplier(String item, String directSupplierInd) throws SOAPException {
		log.info("Inside the findSupplier method");
		log.info("***Start checkSOH***");
		int supplier = 0;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		log.info("item " + item);
		log.info("directSupplierInd " + directSupplierInd);
		// Check SOH in DAS schema
		String query = "select supplier from item_supplier where item=? and direct_ship_ind=? and primary_supp_ind='Y'";
		try {
			conn = createConnection();
			preparedStatement = conn.prepareStatement(query);
			log.info("before parsing");
			// log.info(Integer.parseInt(item));
			// preparedStatement.setLong(1,Long.parseLong(item));
			// changed the code to check for Invalid number error
			log.info("Item---" + item);
			preparedStatement.setString(1, item);
			preparedStatement.setString(2, directSupplierInd);
			rs = preparedStatement.executeQuery();
			log.info("before rs");
			while (rs.next()) {
				log.info("before avail");
				supplier = rs.getInt("supplier");
				log.info("supplier : " + supplier);
			}
		} catch (SQLException e) {
			log.info(e.getMessage());
		} finally {
			try {
				rs.close();
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return supplier;
	}

	public String findItemStatus(String item, BigDecimal loc) throws SOAPException {
		log.info("inside find item status");
		log.info("***Start checkSOH***");
		String status = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Check SOH in DAS schema
		log.info("item " + item);
		log.info("loc " + loc);
		String query = "select status from item_loc where item=? and loc=?";
		try {
			conn = createConnection();
			preparedStatement = conn.prepareStatement(query);
			log.info("before parsing");
			// log.info(Integer.parseInt(item));
			preparedStatement.setString(1, item);
			// preparedStatement.setLong(1,Long.parseLong(item));
			preparedStatement.setInt(2, loc.intValue());
			rs = preparedStatement.executeQuery();
			log.info("before rs");
			while (rs.next()) {
				log.info("before avail");
				status = rs.getString("status");
				log.info("status : " + status);
			}
		} catch (SQLException e) {
			log.info(e.getMessage());
		} finally {
			try {
				rs.close();
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return status;
	}

	public long checkSOHForWH(String item, List<BigDecimal> locs) throws SOAPException {
		log.info("Inside the checkSOHForWH method");
		log.info("***Start checkSOH***");
		long SOH = 0;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Check SOH in DAS schema
		log.info("item " + item);
		log.info("locs " + locs.toString());
		StringBuffer locations = new StringBuffer("");
		for (int j = 0; j < locs.size();) {
			locations.append(locs.get(j));
			if (++j < locs.size()) {
				locations.append(",");
			}
		}
		log.info(locations.toString());
		String query = "select avail_qty from V_AVAIL_INV_POSITION where item='" + item + "' and location IN(" + locations.toString() + ")";
		log.info(query);
		try {
			conn = createConnection();
			preparedStatement = conn.prepareStatement(query);
			log.info("before parsing");
			log.info("Item " + item);
			// log.info(Integer.parseInt(item));
			preparedStatement.setString(1, item);
			// preparedStatement.setLong(1,Long.parseLong(item));
			// preparedStatement.setString(1, item);
			rs = preparedStatement.executeQuery();
			log.info("before rs");
			while (rs.next()) {
				log.info("before avail");
				SOH = rs.getInt("avail_qty") + SOH;
				log.info("SOH : " + SOH);
			}
		} catch (SQLException e) {
			log.info(e.getMessage());
		} finally {
			try {
				rs.close();
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return SOH;
	}

	private Connection createConnection() {
		log.info("***Start createConnection***");
		Connection connection = null;
		try {
			OMSUtil.getInstance();
			Context initContext = OMSUtil.getInitialContext();
			DataSource ds = (DataSource) initContext.lookup("jdbc/das");
			connection = ds.getConnection();
			log.info("connected to db");
		} catch (Exception e) {
			log.error("unable to connect to database" + e);
		}
		return connection;
	}

	public CustFutureInvPosition findFutInvDateAndQty(String item, Long location, BigDecimal pendingQty) throws SQLException {
		log.info("inside findFutInvDateAndQty");
		CustFutureInvPosition custFutureInvPosition = new CustFutureInvPosition();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "";
	
		query = "select * from V_CUST_FUTURE_INV_ECOM_FEED_P where item=? and location=? ";
		log.info(query);
		try {
			int targetQty = pendingQty.intValue();
			log.info("targetQty=" + targetQty);
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			// preparedStatement.setLong(1,Long.parseLong(item));
			preparedStatement.setString(1, item);
			preparedStatement.setLong(2, location);

			rs = preparedStatement.executeQuery();
			custFutureInvPosition.setExpectedQty(new BigDecimal(0));
			int expectedQty = 0;
			while (rs.next() && expectedQty < targetQty) {
				expectedQty = expectedQty + rs.getBigDecimal("AVAIL_QTY").intValue();
				// custFutureInvPosition.setExpectedQty(rs.getBigDecimal("EXPECTED_QTY"));
				custFutureInvPosition.setExpectedQty(new BigDecimal(expectedQty));
				log.info("custFutureInvPosition.setExpectedQty=" + custFutureInvPosition.getExpectedQty());
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				e.getMessage();
			}
		}
		return custFutureInvPosition;
	}

	public BigDecimal getPrimarySupplierFromItemLocation(String item, long location) {
		log.info("*** Begin of  getPrimarySupplierFromItemLocation Method***");
		int supplier = 0;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "Select PRIMARY_SUPP from ITEM_LOC where ITEM=? and LOC=?";
		log.info("Query is Select PRIMARY_SUPP from ITEM_LOC where ITEM=" + item + "and LOC=" + location);
		try {
			log.info("Creating DB connection");
			conn = OMSUtil.createDBConnection(OMSConstants.DS_DAS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			preparedStatement.setLong(2, location);
			log.info("Executing the Sql query");
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				supplier = rs.getInt("PRIMARY_SUPP");
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				e.getMessage();
			}
		}
		log.info("*** End of getPrimarySupplierFromItemLocation method***");
		return (new BigDecimal(supplier));
	}

	Boolean checkDirectShipIndicatoryofaGivenSupplier(String item, long supplier, Long requestorId) {
		log.info("**** Begin of checkDirectShipIndicatoryofaGivenSupplier ****");
		Boolean result = false;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "Select DIRECT_SHIP_IND from item_supplier where ITEM=?  and SUPPLIER=?";
		String ksaReqQuery = "Select DIRECT_SHIP_IND from V_ITEM_LOC_SUPS_PO where ITEM=?  and PRIMARY_SUPP=?";
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_DAS_STRING);
			if (requestorId != null && requestorId.toString().startsWith("1" )) {
				preparedStatement = conn.prepareStatement(ksaReqQuery);
			} else {
				preparedStatement = conn.prepareStatement(query);
			}
			preparedStatement.setString(1, item);
			preparedStatement.setLong(2, supplier);
			rs = preparedStatement.executeQuery();
			log.info(" Query is :- Select DIRECT_SHIP_IND from item_supplier where ITEM=" + item + "  and SUPPLIER=" + supplier);
			if (rs.next()) {
				if (requestorId != null && requestorId.toString().startsWith("1" )) {
					result = true;
				} else {
					result = "Y".equals(rs.getString(1));
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				e.getMessage();
			}
		}
		log.info("**** End of checkDirectShipIndicatoryofaGivenSupplier ******");
		return result;
	}
}
