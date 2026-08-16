package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.model.CustOrdItmDesc;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

public class InterfacePersistence {
	public InterfacePersistence() {
		super();
	}

	private final static Logger log = Logger.getLogger(InterfacePersistence.class.getName());

	public long callSIMStoreInventory(String item, BigDecimal nextLoc) throws com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException {

		long avlToSell = 0;
		String query = "SELECT AVAIL_TO_SELL FROM XX_OMS_POS_INV_V WHERE ITEM_ID = ? AND STORE_ID = ?";

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			preparedStatement.setBigDecimal(2, nextLoc);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				avlToSell = resultSet.getLong("AVAIL_TO_SELL");
			}
		} catch (Exception e) {
			log.error("Error while fetching the store stock for item " + item + " and location " + nextLoc, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.warn("Error while closing resultset", e);
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					log.warn("Error while closing statement", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.warn("Error while closing connection", e);
				}
			}
		}

		return avlToSell;
	}

	public long avlToSellInv(String item, BigDecimal location) throws Exception {
		long avlToSell = 0;
		String query = "SELECT AVAIL_TO_SELL FROM XX_OMS_INV WHERE ITEM_ID = ? AND STORE_ID = ?";

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			preparedStatement.setBigDecimal(2, location);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				avlToSell = resultSet.getLong("AVAIL_TO_SELL");
			}
		} catch (SQLException e) {
			throw new Exception("Error executing SQL query: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new Exception("An error occurred: " + e.getMessage(), e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.warn("Error while closing resultset", e);
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					log.warn("Error while closing statement", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.warn("Error while closing connection", e);
				}
			}
		}

		return avlToSell;
	}

	public int bo_pending_qty(String item, Long sourceLoc) throws SQLException, Exception {
		int bo_pending_qty = 0;
		String query = "SELECT NVL(SUM(SOURCE_QTY - FULFILL_QTY), 0) FROM OMS_BACK_ORDER_DTL WHERE ITEM = ? AND SOURCE_LOC = ? AND BACKORDER_STATUS <> 'S' AND SOURCE_QTY > FULFILL_QTY";

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			preparedStatement.setInt(2, sourceLoc.intValue());

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				bo_pending_qty = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new SQLException("SQL error: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new Exception("An error occurred: " + e.getMessage(), e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.warn("Error while closing resultset", e);
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					log.warn("Error while closing statement", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.warn("Error while closing connection", e);
				}
			}
		}

		return bo_pending_qty;
	}

	public int stThrsldQty(String parameterName) throws Exception {
		int stThrsldQty = 0;
		String query = "SELECT PARAMETER_NAME, PARAMETER_VALUE FROM OMS_SYSTEM_PARAMETERS WHERE UPPER(PARAMETER_NAME) = ?";

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, parameterName.toUpperCase());

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				stThrsldQty = resultSet.getInt("PARAMETER_VALUE");
			}
		} catch (SQLException e) {
			throw new SQLException("SQL error: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new Exception("An error occurred: " + e.getMessage(), e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.warn("Error while closing resultset", e);
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					log.warn("Error while closing statement", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.warn("Error while closing connection", e);
				}
			}
		}

		return stThrsldQty;
	}

	public long findWHInventory(String item, BigDecimal location) throws SOAPException {
		long SOH = 0;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<Object[]> tempWhObject = session.getWhFindPhysicalWH(location);
		BigDecimal physicalWH = BigDecimal.ZERO;
		BigDecimal channelId = BigDecimal.ZERO;
		for (Object[] result : tempWhObject) {
			physicalWH = new BigDecimal(result[0].toString());
			channelId = new BigDecimal(result[1].toString());
			log.info("WH=" + result[0] + "channel id=" + result[1]);
		}
		List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);
		int i = 0;
		while (i < locList.size()) {
			log.info(locList.get(i));
			i++;
		}
		OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
		String applicationId = "ORPOS";
		SOH = omsUtilCommons.checkSOHForWH(item, locList, applicationId).longValue();
		return SOH;
	}

	public BackOrderResponse backOrder(List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList, CustOrdItmDesc custOrdItmDesc, long pendingQty, String classification, String countryCode)
			throws Exception {
		log.info("inside backOrder");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		BackOrderResponse backOrderResponse = new BackOrderResponse();
		int expectedQty = 0;
		BigDecimal alloctedInventory = BigDecimal.ZERO;
		log.info("looping");
		for (OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail : omsFulfillMatrixExtDetailList) {
			if (omsFulfillMatrixExtDetail.getLocation().intValue() > 0) {
				log.info("omsFulfillMatrixExtDetail.getLocation() is greater than O");
				String boIndicator = "N";
				log.info("omsFulfillMatrixExtDetail.getLocation() " + omsFulfillMatrixExtDetail.getLocation());
				log.info("custOrdItmDesc.getItemId() " + custOrdItmDesc.getItemId());
				/*
				 * BigDecimal alloctedInventory
				 * =session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.
				 * getLocation(), custOrdItmDesc.getItemId()); if (alloctedInventory == null) {
				 * alloctedInventory = BigDecimal.ZERO; } log.info("alloctedInventory" +
				 * alloctedInventory);
				 */
				FindNextFulfillLoc checkItemLocSOH = new FindNextFulfillLoc();
				BigDecimal physicalWH = BigDecimal.ZERO;
				BigDecimal channelId = BigDecimal.ZERO;
				int thsdQty = 0;
				if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
					List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsFulfillMatrixExtDetail.getLocation());
					for (Object[] result : tempWhObject) {
						physicalWH = new BigDecimal(result[0].toString());
						channelId = new BigDecimal(result[1].toString());
						log.info("WH=" + result[0] + "channel id=" + result[1]);
					}
					if ("SMALL".equals(classification)) {
						thsdQty = Integer.parseInt(session.getOmsSystemParametersFindIndValue(countryCode + "_WH_POS_SMALL", countryCode + "_WH_SMALL_POS_THRESHOLD"));
					} else {
						thsdQty = Integer.parseInt(session.getOmsSystemParametersFindIndValue(countryCode + "_WH_POS_BIG", countryCode + "_WH_BIG_POS_THRESHOLD"));
					}
				} else {
					if ("SMALL".equals(classification)) {
						thsdQty = stThrsldQty(countryCode + "_ST_POS_SMALL");
					} else {
						thsdQty = stThrsldQty(countryCode + "_ST_POS_BIG");
					}
				}
//				alloctedInventory = session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(), custOrdItmDesc.getItemId());
//				if (alloctedInventory == null) {
//					alloctedInventory = BigDecimal.ZERO;
//				}
//				log.info("alloctedInventory" + alloctedInventory);
				CustFutureInvPosition custFutureInvPosition = null;
				try {
					log.info("Finding bo indicator for location" + omsFulfillMatrixExtDetail.getLocation());
					boIndicator = oMSUtilCommons.getBOIndicator(omsFulfillMatrixExtDetail.getLocation(), custOrdItmDesc.getItemId());
					log.info("fetched indicator value  from table is +++++++++++===" + boIndicator);
					if (boIndicator == null || boIndicator.isEmpty()) {
						log.info("boIndicator is null ,setting it to N");
						boIndicator = "N";
					}
					if (boIndicator.equals("Y")) {
						log.info("inside indicator Y");
						if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
//							BigDecimal whId = physicalWH + channelId;

//							log.info("physicalWH " + physicalWH + " : whId " + whId + " :channelId " + channelId);
//							custFutureInvPosition = checkItemLocSOH.findFutInvDateAndQty(custOrdItmDesc.getItemId(), whId, new BigDecimal(pendingQty));
						} else {
//							custFutureInvPosition = checkItemLocSOH.findFutInvDateAndQty(custOrdItmDesc.getItemId(), omsFulfillMatrixExtDetail.getLocation().longValue(), new BigDecimal(pendingQty));
						}
						log.info("custFutureInvPosition values fetched" + custFutureInvPosition.getExpectedDate() + "qty=" + custFutureInvPosition.getExpectedQty());
					}
				} catch (Exception e) {
					boIndicator = "N";
				}
				try {
					if (custFutureInvPosition != null) {
						log.info("Expected qty at store  " + omsFulfillMatrixExtDetail.getLocation() + " is " + custFutureInvPosition.getExpectedQty());
						expectedQty = expectedQty + Math.max(custFutureInvPosition.getExpectedQty().intValue() - thsdQty, 0);
						log.info("expectedQty=" + expectedQty);
						// backOrderResponse.setFutInvAvlDate(expectedDate);
						backOrderResponse.setFutureAvlQty(expectedQty);
					} else {
						log.info("inside else");
						backOrderResponse.setFutureAvlQty(expectedQty);
					}
				} catch (Exception e) {
					backOrderResponse.setFutureAvlQty(expectedQty);
					log.error("Error in backorder" + e);
				}
			}
		}
		log.info("returning backorder" + backOrderResponse.getFutureAvlQty());
		return backOrderResponse;
	}
}
