package org.logicinfo.stockfeed.daoImpl;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.logicinfo.stockfeed.dao.StockFeedDao;
import org.logicinfo.stockfeed.dbqueries.StockFeedQueries;
import org.logicinfo.stockfeed.model.Stock;
import org.logicinfo.stockfeed.model.StockFeed;
import org.logicinfo.stockfeed.model.StockFeedRequestModel;
import org.logicinfo.stockfeed.model.StockFeedResponseModel;
import org.springframework.stereotype.Repository;

@Repository
public class StockFeedDaoImpl implements StockFeedDao {

	private static final Logger logger = LogManager.getLogger(StockFeedDaoImpl.class.getName());

	public Connection getDBForDASConnection() {
		Context ctx = null;
		Connection conn = null;
		try {
			ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("jdbc/xtradas");

			conn = ds.getConnection();
		} catch (Exception e) {
			logger.info("Exception while creating a connection for DAS " + e.getMessage());
		}

		return conn;
	}

	public Connection getDBForSIMConnection() {
		Context ctx = null;
		Connection conn = null;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/sim");
			conn = ds.getConnection();
		} catch (Exception e) {
			logger.info("Exception while creating a connection for SIM " + e.getMessage());
		}

		return conn;
	}

	public Connection getDBForOMSConnection() {
		Context ctx = null;
		Connection conn = null;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/oms");
			conn = ds.getConnection();
		} catch (Exception e) {
			logger.error("Exception while creating a connection for OMS ", e);
		}

		return conn;
	}

	@Override
	public StockFeedResponseModel getPhysicalStockDeltaFeedDaoImpl(StockFeedRequestModel request) {
		StockFeedResponseModel response = new StockFeedResponseModel();
		ArrayList<StockFeed> physicalStockFeedList = new ArrayList<StockFeed>();

		Connection conn = null;
		if (request.getLocType().equals("WH")) {
			conn = getDBForOMSConnection();
		} else {
			conn = getDBForSIMConnection();
		}

		if (conn == null) {
			response.setStatus("Failure");
			response.setError("unable to establish the connection");
			return response;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (request.getLocType().equals("ST")) {
				pstmt = conn.prepareCall(StockFeedQueries.physicalStockDeltaQuery_ST);
				logger.info("PhysicalStockDeltaQuery " + StockFeedQueries.physicalStockDeltaQuery_ST + " for request Id is " + request.getId());
			} else {
				pstmt = conn.prepareCall(StockFeedQueries.physicalStockDeltaQuery_WH);
				logger.info("PhysicalStockDeltaQuery " + StockFeedQueries.physicalStockDeltaQuery_WH + " for request Id is " + request.getId());

			}
			rs = pstmt.executeQuery();

			if (!rs.isBeforeFirst()) {
				response.setStatus("Success");
				return response;
			} else {
				Map<String, ArrayList<Stock>> stockMap = new HashMap<String, ArrayList<Stock>>();

				while (rs.next()) {

					Stock stock = new Stock();
					response.setStatus("Success");
					stock.setLocation(rs.getString("location"));
					logger.info("Item " + rs.getString("ITEM"));
					logger.info("location " + rs.getBigDecimal("location"));
					logger.info("AVAIL_QTY " + rs.getBigDecimal("AVAIL_QTY"));
					if (rs.getBigDecimal("AVAIL_QTY") != null && rs.getBigDecimal("AVAIL_QTY").intValue() > 0) {
						stock.setQuantity(rs.getBigDecimal("AVAIL_QTY"));
					}

					else {
						stock.setQuantity(BigDecimal.ZERO);
					}

					if (stockMap.get(rs.getString("ITEM")) == null || (stockMap.get(rs.getString("ITEM")).size() == 0)) {
						ArrayList<Stock> stockList = new ArrayList<Stock>();
						stockList.add(stock);
						stockMap.put(rs.getString("ITEM"), stockList);

					} else {
						ArrayList<Stock> existingStockList = stockMap.get(rs.getString("ITEM"));
						existingStockList.add(stock);
						stockMap.put(rs.getString("ITEM"), existingStockList);
					}

				}
				logger.info("StockMap size for request Id  " + request.getId() + " is " + stockMap.size());
				if (stockMap != null && !stockMap.isEmpty() && stockMap.size() > 0) {
					for (String item : stockMap.keySet()) {
						ArrayList<Stock> ArrayStockList = stockMap.get(item);
						StockFeed feed = new StockFeed();
						feed.setProductCode(item);
						feed.setStock(ArrayStockList);
						physicalStockFeedList.add(feed);

					}

				}
				logger.info("PhysicalStockDeltaFeed size for request Id " + request.getId() + " is " + physicalStockFeedList.size());
				if (physicalStockFeedList != null && !physicalStockFeedList.isEmpty() && physicalStockFeedList.size() > 0) {
					response.setFeed(physicalStockFeedList);
				}

			}

		} catch (Exception e) {
			logger.info("Exception occured while retriving the data for phyiscal delta feed " + e.getMessage() + " for request Id is " + request.getId());
			response.setStatus("Failure");
			response.setError(e.getMessage());
		} finally {
			closeDBConnection(conn, pstmt, rs);
		}
		return response;
	}

	@Override
	public StockFeedResponseModel getFutureInventoryStockFeedDaoImpl(StockFeedRequestModel request) {
		StockFeedResponseModel response = new StockFeedResponseModel();
		ArrayList<StockFeed> futureInvStockFeedList = new ArrayList<StockFeed>();

		Connection conn = getDBForDASConnection();

		if (conn == null) {
			response.setStatus("Failure");
			response.setError("unable to establish the connection");
			return response;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sqlQuery = null;
			logger.info("LocType for the request id  " + request.getId() + " is " + request.getLocType());
			if (null == request.getLocType() || request.getLocType().isEmpty() || ("").contains(request.getLocType())) {
				logger.info("loctype is null/empty ");
				sqlQuery = StockFeedQueries.futureInventoryQuery;
			} else {
				logger.info("locatype contains value " + request.getLocType());
				sqlQuery = StockFeedQueries.futureInventoryQuery + "  and  LOC_TYPE=" + "'" + request.getLocType() + "'";
			}
			logger.info("FutureInventoryQuery " + sqlQuery + " for request Id is " + request.getId());
			pstmt = conn.prepareCall(sqlQuery);
			rs = pstmt.executeQuery();

			if (!rs.isBeforeFirst()) {
				response.setStatus("Success");
				response.setError("No future inventory available");
				return response;
			} else {
				Map<String, ArrayList<Stock>> stockMap = new HashMap<String, ArrayList<Stock>>();

				while (rs.next()) {

					Stock stock = new Stock();
					response.setStatus("Success");
					stock.setLocation(rs.getString("location"));
					if (rs.getBigDecimal("AVAIL_QTY") != null && rs.getBigDecimal("AVAIL_QTY").intValue() > 0) {
						stock.setQuantity(rs.getBigDecimal("AVAIL_QTY"));
					} else {
						stock.setQuantity(BigDecimal.ZERO);
					}

					if (stockMap.get(rs.getString("ITEM")) == null || (stockMap.get(rs.getString("ITEM")).size() == 0)) {
						ArrayList<Stock> stockList = new ArrayList<Stock>();
						stockList.add(stock);
						stockMap.put(rs.getString("ITEM"), stockList);

					} else {
						ArrayList<Stock> existingStockList = stockMap.get(rs.getString("ITEM"));
						existingStockList.add(stock);
						stockMap.put(rs.getString("ITEM"), existingStockList);
					}

				}
				logger.info("Future Inventory StockMap size for request Id " + request.getId() + " is " + stockMap.size());
				if (stockMap != null && !stockMap.isEmpty() && stockMap.size() > 0) {
					for (String item : stockMap.keySet()) {
						ArrayList<Stock> ArrayStockList = stockMap.get(item);
						StockFeed feed = new StockFeed();
						feed.setProductCode(item);
						feed.setStock(ArrayStockList);
						futureInvStockFeedList.add(feed);

					}

				}
				logger.info("Future Inventory futureInvStockFeedList size for request Id " + request.getId() + " is " + futureInvStockFeedList.size());
				if (futureInvStockFeedList != null && !futureInvStockFeedList.isEmpty() && futureInvStockFeedList.size() > 0) {
					response.setFeed(futureInvStockFeedList);
				}
			}

		} catch (Exception e) {
			logger.info("Exception occured while retriving the data for future inventory feed " + e.getMessage() + " for request Id is " + request.getId());
			response.setStatus("Failure");
			response.setError(e.getMessage());
			return response;
		} finally {
			closeDBConnection(conn, pstmt, rs);
		}
		return response;
	}

	private void closeDBConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (Exception e) {
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			logger.info("Exception occured while closing the connection " + e.getMessage());
		}
	}

	@Override
	public int persistReqRespObject(StockFeedRequestModel request, StockFeedResponseModel response, String requestType) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			conn = getDBForOMSConnection();
			if (conn == null) {
				logger.info("Unable to establish the connection to persist the record in stock_feed in OMS for the request Id " + request.getId());
				return 0;
			}
			String insertQuery_ST = " INSERT INTO STOCK_FEED (id,response,CREATE_DATETIME,STATUS,Request_Type) values (?,?,SYSTIMESTAMP- INTERVAL '3' HOUR,?,?) ";

			String insertQuery_WH = " INSERT INTO STOCK_FEED (id,response,CREATE_DATETIME,STATUS,Request_Type) values (?,?,SYSTIMESTAMP,?,?) ";

			if (requestType.equalsIgnoreCase("PhysicalDeltaFeed-ST")) {
				pstmt = conn.prepareStatement(insertQuery_ST);
			} else {
				pstmt = conn.prepareStatement(insertQuery_WH);
			}
			pstmt.setString(1, request.getId());
			ObjectMapper Obj = new ObjectMapper();
			String jsonStr = Obj.writeValueAsString(response);
			Blob reqBlobForCreate = conn.createBlob();
			if (response != null) {
				reqBlobForCreate.setBytes(2, jsonStr.getBytes());
			}

			pstmt.setBlob(2, reqBlobForCreate);
			pstmt.setString(3, response.getStatus());
			pstmt.setString(4, requestType);

			i = pstmt.executeUpdate();
			logger.info("persisted the record into stock_feed for request id :" + request.getId());
		} catch (Exception e) {
			logger.info("Exception while inserting the record into stock_feed for request id :" + request.getId() + " error is " + e.getMessage());
		}

		finally {
			closeDBConnection(conn, pstmt, null);
		}
		return i;
	}

	@Override
	public StockFeedResponseModel getPackItemFeedData(StockFeedRequestModel stockFeedReq) {

		StockFeedResponseModel response = new StockFeedResponseModel();
		Connection conn = getDBForOMSConnection();

		if (conn == null) {
			response.setStatus("Failure");
			response.setError("Unable to establish the connection");
			return response;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.info("ID for the PackItemFeed request is  " + stockFeedReq.getId());
			pstmt = conn.prepareCall("SELECT * FROM GET_PACK_QTY_V");
			pstmt.setFetchSize(1000);
			rs = pstmt.executeQuery();

			if (!rs.isBeforeFirst()) {
				logger.info("No data found for pack item feed for request Id " + stockFeedReq.getId());
				response.setStatus("Success");
				response.setError("No PackItem inventory available");
				return response;
			} else {
				Map<String, StockFeed> stockMap = new HashMap<String, StockFeed>();
				response.setFeed(new ArrayList<StockFeed>());
				while (rs.next()) {
					Stock stock = new Stock();
					stock.setLocation(rs.getString(2));
					stock.setQuantity(rs.getBigDecimal(3));

					String productCode = rs.getString(1);
					StockFeed feed = stockMap.get(productCode);
					if (feed == null) {
						feed = new StockFeed();
						feed.setProductCode(productCode);
						feed.setStock(new ArrayList<Stock>());
						stockMap.put(productCode, feed);
						response.getFeed().add(feed);
					}
					feed.getStock().add(stock);
				}
				response.setStatus("Success");
				logger.info("Inventory pack item feed list size for request Id " + stockFeedReq.getId() + " is " + response.getFeed().size());
			}
		} catch (Exception e) {
			logger.error("Exception occured while retriving the data for pack item feed " + e.getMessage() + " for request Id is " + stockFeedReq.getId(), e);
			response.setStatus("Failure");
			response.setError(e.getMessage());
			return response;
		} finally {
			closeDBConnection(conn, pstmt, rs);
		}
		return response;
	}
}
