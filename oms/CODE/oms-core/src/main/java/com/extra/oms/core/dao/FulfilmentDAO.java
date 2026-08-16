/**
 * 
 */
package com.extra.oms.core.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.CustomerAddressInfo;
import com.extra.oms.core.bean.ItemFulfilment;
import com.extra.oms.core.bean.OrderInfo;
import com.extra.oms.core.bean.RequestType;
import com.extra.oms.core.bean.UserInfo;

/**
 * @author aibrahim
 *
 */
@Repository
public class FulfilmentDAO extends BaseDAO {

	private static final Logger _LOG = Logger.getLogger(FulfilmentDAO.class);

	private static final String FULFILMENT_SEARCH_QUERY = "SELECT H.CUST_ORDER_NO, F.ITEM, (SELECT  M.SHORT_DESC FROM ITEM_MASTER M WHERE F.ITEM = M.ITEM), F.SOURCE_LOC_TYPE, F.FULFILL_LOC_TYPE, F.SOURCE_LOC, F.FULFILL_LOC, F.FULFILL_REQ_QTY, F.FULFILL_CANCEL_QTY, F.FULFILL_ORDER_NO, F.LINE_NO, F.OMS_CUST_ORD_NO, F.FULFILL_CONF_QTY, F.FULFILL_DELIVER_QTY, H.DELIVERY_TYPE, I.QTY_CANCELLED, I.QTY_ORDERED_SUOM FROM OMS_CUST_ORD_HEAD H, OMS_CO_FULFILL_DETAIL F, OMS_CUST_ORD_ITEM I WHERE H.OMS_CUST_ORD_NO = F.OMS_CUST_ORD_NO AND H.STATUS = 'S' AND H.ORD_PAYMENT_STATUS = 'S' AND F.FULFILL_DELIVER_QTY = 0 AND I.OMS_CUST_ORD_NO = H.OMS_CUST_ORD_NO AND I.ITEM = F.ITEM AND I.LINE_NO = F.LINE_NO ";

	private static final String GET_FULFILMENT_QUERY = "SELECT OMS_CUST_ORD_NO, FULFILL_ORDER_NO, ITEM, LINE_NO, SOURCE_LOC_TYPE, SOURCE_LOC, FULFILL_LOC_TYPE, FULFILL_LOC, FULFILL_REQ_QTY, FULFILL_CONF_QTY, FULFILL_DELIVER_QTY, FULFILL_CANCEL_QTY, FULFILL_STATUS, COMBINATION_ID, TSF_NO FROM OMS_CO_FULFILL_DETAIL WHERE OMS_CUST_ORD_NO = :omsOrderNo AND FULFILL_ORDER_NO = :fulfilOrderNo AND ITEM = :item AND LINE_NO = :lineNo FOR UPDATE ";

	private static final String GET_CUSTOMER_ADDRESS_QUERY = "SELECT DISTINCT (SELECT MAX(FULFILL_ORDER_NO) + 1 FROM OMS_CO_FULFILL_DETAIL WHERE OMS_CUST_ORD_NO = :omsOrderNo), DELIVER_FIRST_NAME, DELIVER_PHONETIC_FIRST, DELIVER_LAST_NAME, DELIVER_PHONETIC_LAST, DELIVER_PREFERRED_NAME, DELIVER_ADD1, DELIVER_CITY, DELIVER_STATE, DELIVER_COUNTRY_ID, DELIVER_POST, DELIVER_PHONE, BILL_FIRST_NAME, BILL_PHONETIC_FIRST, BILL_LAST_NAME, BILL_PHONETIC_LAST, BILL_PREFERRED_NAME, BILL_COMPANY_NAME, BILL_ADD1, BILL_ADD2, BILL_ADD3, BILL_COUNTY, BILL_CITY, BILL_STATE, BILL_COUNTRY_ID, BILL_POST, BILL_JURISDICTION, BILL_PHONE FROM ORDCUST WHERE CUSTOMER_ORDER_NO = :orderNo AND ROWNUM = 1";

	private static final String INSERT_FULFILMENT_QUERY = "INSERT INTO OMS_CO_FULFILL_DETAIL(FULFILL_ORDER_NO, ITEM, LINE_NO, OMS_CUST_ORD_NO, SOURCE_LOC_TYPE, SOURCE_LOC, FULFILL_LOC_TYPE, FULFILL_LOC, FULFILL_REQ_QTY, FULFILL_CONF_QTY, FULFILL_DELIVER_QTY, FULFILL_CANCEL_QTY, FULFILL_STATUS, TSF_NO, TSF_APPROVAL_STATUS, CREATE_DATETIME) VALUES(:fulfilOrdNo, :item, :lineNo, :omsOrderNumber, :srcLocationType, :srcLocation, :fulfilLocationType, :fulfilLocation, :requestQty, :confirmQty, :deliveryQty, :cancelledQty, 'C', :tsfNo, 'A', SYSDATE)";

	private static final String GET_TSF_NO_QUERY = "SELECT TSF_NO FROM ORDCUST WHERE SOURCE_LOC_ID = :srcLocation AND FULFILL_LOC_ID = :fulfilLocation AND CUSTOMER_ORDER_NO = :orderNo AND FULFILL_ORDER_NO = :fulfilOrdNo";

	private static final String GET_STORE_STOCK_AVAILABLITY_QUERY = "SELECT AVAIL_TO_SELL FROM XX_OMS_INV@SIMDB WHERE ITEM_ID = :item AND STORE_ID = :location AND SOURCE = 'E-COMMERCE'";

	private static final String GET_WH_STOCK_AVAILABLITY_QUERY = "SELECT AVAIL_QTY FROM XX_OMS_INVAVAIL_V WHERE ITEM = :item AND LOC = :location and LOC_TYPE = 'W'";

	private static final String INSERT_FULFILMENT_REQ_QUERY = "INSERT INTO XX_FULFILL_CAN_CRE_TMP_WS(CUSTOMER_ORDER_NO, OMS_CUST_ORD_NO, FULFILL_ORDER_NO_OLD, SOURCE_LOC_TYPE_OLD, SOURCE_LOC_ID_OLD, FULFILL_LOC_TYPE_OLD, FULFILL_LOC_ID_OLD, ITEM, QTY, FULFILL_ORDER_NO_NEW, SOURCE_LOC_TYPE_NEW, SOURCE_LOC_ID_NEW, FULFILL_LOC_TYPE_NEW, FULFILL_LOC_ID_NEW, DELIVERY_TYPE, FULFIL_ITEM, FULFIL_QTY, STATUS, UPDATE_DATE, FULFILLMENT_SEQ_ID, LINE_NO, USER_NAME) VALUES(:customerNo, :omsOrderNo, :fulfilOrdNoOld, :srcLocTypeOld, :srcLocOld, :fulfillLocTypeOld, :fulfillLocIdOld, :item, :qty, :fulfillOrderNoNew, :sourceLocTypeNew, :sourceLocIdNew, :fulfillLocTypeNew, :fulfillLocIdNew, :deliveryType, :fulfilItem, :fulfilQty, :status, SYSDATE, :fulfillmentSeqId, :lineNo, :userName)";

	private static final String VALIDATE_FULFILMENT_QUERY = "SELECT F.OMS_CUST_ORD_NO, F.FULFILL_ORDER_NO, F.ITEM, F.LINE_NO, F.SOURCE_LOC_TYPE, F.SOURCE_LOC, F.FULFILL_LOC_TYPE, F.FULFILL_LOC, F.FULFILL_REQ_QTY, F.FULFILL_DELIVER_QTY, F.FULFILL_CANCEL_QTY, C.CUST_ORDER_NO, C.DELIVERY_TYPE FROM OMS_CO_FULFILL_DETAIL F, OMS_CUST_ORD_HEAD C WHERE C.OMS_CUST_ORD_NO = F.OMS_CUST_ORD_NO AND F.OMS_CUST_ORD_NO IN (:omsOrds) AND F.ITEM IN (:items) AND F.FULFILL_ORDER_NO IN (:fulfilOrds)";

	public List<ItemFulfilment> searchFulfilments(Map<String, Object> params) {
		StringBuilder queryBuilder = new StringBuilder(FULFILMENT_SEARCH_QUERY);
		if (params.get("orderNumber") != null && !StringUtils.isEmpty(params.get("orderNumber").toString().trim())) {
			appendWhere(queryBuilder);
			queryBuilder.append(" H.CUST_ORDER_NO = :orderNumber ");
		}
		if (params.get("item") != null && !StringUtils.isEmpty(params.get("item").toString().trim())) {
			appendWhere(queryBuilder);
			queryBuilder.append(" F.ITEM = :item ");
		}
		return jdbcTemplate.query(queryBuilder.toString(), params, new RowMapper<ItemFulfilment>() {

			@Override
			public ItemFulfilment mapRow(ResultSet rs, int rowNum) throws SQLException {
				ItemFulfilment fulfilment = new ItemFulfilment();
				OrderInfo order = new OrderInfo();
				
				order.setOrderNumber(rs.getString(1));
				fulfilment.setItem(rs.getString(2));
				fulfilment.setItemDesc(rs.getString(3));
				fulfilment.setSrcLocationType(rs.getString(4));
				fulfilment.setFulfilLocationType(rs.getString(5));
				fulfilment.setSrcLocation(rs.getLong(6));
				fulfilment.setFulfilLocation(rs.getLong(7));
				fulfilment.setRequestQty(rs.getBigDecimal(8));
				fulfilment.setCancelledQty(rs.getBigDecimal(9));
				fulfilment.setFulfilOrdNo(rs.getLong(10));
				fulfilment.setLineNo(rs.getString(11));
				order.setOmsOrderNumber(rs.getString(12));
				fulfilment.setConfirmQty(rs.getBigDecimal(13));
				fulfilment.setDeliveryQty(rs.getBigDecimal(14));
				order.setDeliveryType(rs.getString(15));
				fulfilment.setItemCancelledQty(rs.getBigDecimal(16));
				fulfilment.setItemOrderedQty(rs.getBigDecimal(17));
				fulfilment.setOrderInfo(order);
				return fulfilment;
			}
		});
	}

	public ItemFulfilment validateAndLockFulfilment(ItemFulfilment fulfilment) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omsOrderNo", fulfilment.getOrderInfo().getOmsOrderNumber());
		params.put("fulfilOrderNo", fulfilment.getFulfilOrdNo());
		params.put("item", fulfilment.getItem());
		params.put("lineNo", fulfilment.getLineNo());
		return jdbcTemplate.queryForObject(GET_FULFILMENT_QUERY, params, new RowMapper<ItemFulfilment>() {

			@Override
			public ItemFulfilment mapRow(ResultSet rs, int rowNum) throws SQLException {
				ItemFulfilment fulfilment = new ItemFulfilment();
				OrderInfo order = new OrderInfo();
				
				order.setOrderNumber(rs.getString(1));
				fulfilment.setOrderInfo(order);
				fulfilment.setFulfilOrdNo(rs.getLong(2));
				fulfilment.setItem(rs.getString(3));
				fulfilment.setLineNo(rs.getString(4));
				fulfilment.setSrcLocationType(rs.getString(5));
				fulfilment.setSrcLocation(rs.getLong(6));
				fulfilment.setFulfilLocationType(rs.getString(7));
				fulfilment.setFulfilLocation(rs.getLong(8));
				fulfilment.setRequestQty(rs.getBigDecimal(9));
				fulfilment.setConfirmQty(rs.getBigDecimal(10));
				fulfilment.setDeliveryQty(rs.getBigDecimal(11));
				fulfilment.setCancelledQty(rs.getBigDecimal(12));
				fulfilment.setTsfNo(rs.getString(15));
				return fulfilment;
			}
		});
	}

	public void updateOMSFulfiment(ItemFulfilment fulfilment) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omsOrderNo", fulfilment.getOrderInfo().getOmsOrderNumber());
		params.put("fulfilOrderNo", fulfilment.getFulfilOrdNo());
		params.put("item", fulfilment.getItem());
		params.put("lineNo", fulfilment.getLineNo());
		params.put("cancelledQty", fulfilment.getCancelledQty());
		StringBuilder queryBuilder = new StringBuilder("UPDATE OMS_CO_FULFILL_DETAIL SET FULFILL_CANCEL_QTY = :cancelledQty, LAST_UPDATE_DATETIME = SYSDATE ");
		if (fulfilment.getRequestQty().equals(fulfilment.getConfirmQty())) {
			queryBuilder.append(", FULFILL_STATUS = 'C' ");
		}
		queryBuilder.append(" WHERE OMS_CUST_ORD_NO = :omsOrderNo AND FULFILL_ORDER_NO = :fulfilOrderNo AND ITEM = :item AND LINE_NO = :lineNo");
		jdbcTemplate.update(queryBuilder.toString(), params);
	}

	public void createOmsFulfilment(ItemFulfilment fulfilment) {
		Map<String, Object> params = new HashMap<String, Object>();
		fulfilment.setCancelledQty(BigDecimal.ZERO);
		fulfilment.setDeliveryQty(BigDecimal.ZERO);
		fulfilment.setConfirmQty(fulfilment.getRequestQty());
		params.put("fulfilOrdNo", fulfilment.getFulfilOrdNo());
		params.put("item", fulfilment.getItem());
		params.put("lineNo", fulfilment.getLineNo());
		params.put("omsOrderNumber", fulfilment.getOrderInfo().getOmsOrderNumber());
		params.put("srcLocationType", fulfilment.getSrcLocationType());
		params.put("srcLocation", fulfilment.getSrcLocation());
		params.put("fulfilLocationType", fulfilment.getFulfilLocationType());
		params.put("fulfilLocation", fulfilment.getFulfilLocation());
		params.put("requestQty", fulfilment.getRequestQty());
		params.put("confirmQty", fulfilment.getConfirmQty());
		params.put("deliveryQty", BigDecimal.ZERO);
		params.put("cancelledQty", BigDecimal.ZERO);
		params.put("tsfNo", fulfilment.getTsfNo());
		jdbcTemplate.update(INSERT_FULFILMENT_QUERY, params);
	}

	public ItemFulfilment getAddresDetail(ItemFulfilment fulfilment) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omsOrderNo", fulfilment.getOrderInfo().getOmsOrderNumber());
		params.put("orderNo", fulfilment.getOrderInfo().getOrderNumber());
		return jdbcTemplate.queryForObject(GET_CUSTOMER_ADDRESS_QUERY, params, new RowMapper<ItemFulfilment>() {

			@Override
			public ItemFulfilment mapRow(ResultSet rs, int rowNum) throws SQLException {
				ItemFulfilment fulfilment = new ItemFulfilment();
				OrderInfo orderInfo = new OrderInfo();
				CustomerAddressInfo addressInfo = new CustomerAddressInfo();
				
				fulfilment.setFulfilOrdNo(rs.getLong(1));
				addressInfo.setDeliverFirstName(rs.getString(2));
				addressInfo.setDeliverPhoneticFirst(rs.getString(3));
				addressInfo.setDeliverLastName(rs.getString(4));
				addressInfo.setDeliverPhoneticLast(rs.getString(5));
				addressInfo.setDeliverPreferredName(rs.getString(6));
				addressInfo.setDeliverAdd1(rs.getString(7));
				addressInfo.setDeliverCity(rs.getString(8));
				addressInfo.setDeliverState(rs.getString(9));
				addressInfo.setDeliverCountryId(rs.getString(10));
				addressInfo.setDeliverPost(rs.getString(11));
				addressInfo.setDeliverPhone(rs.getString(12));
				
				addressInfo.setBillFirstName(rs.getString(13));
				addressInfo.setBillPhoneticFirst(rs.getString(14));
				addressInfo.setBillLastName(rs.getString(15));
				addressInfo.setBillPhoneticLast(rs.getString(16));
				addressInfo.setBillPreferredName(rs.getString(17));
				addressInfo.setBillCompanyName(rs.getString(18));
				addressInfo.setBillAdd1(rs.getString(19));
				addressInfo.setBillAdd2(rs.getString(20));
				addressInfo.setBillAdd3(rs.getString(21));
				addressInfo.setBillCity(rs.getString(23));
				addressInfo.setBillState(rs.getString(24));
				addressInfo.setBillCountryId(rs.getString(25));
				addressInfo.setBillCounty(rs.getString(22));
				addressInfo.setBillPost(rs.getString(26));
				addressInfo.setBillJurisdiction(rs.getString(27));
				addressInfo.setBillPhone(rs.getString(28));
				
				orderInfo.setAddressInfo(addressInfo);
				fulfilment.setOrderInfo(orderInfo);
				return fulfilment;
			}
		});
	}

	public String getTSFNo(ItemFulfilment fulfilment) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", fulfilment.getOrderInfo().getOrderNumber());
		params.put("fulfilOrdNo", fulfilment.getFulfilOrdNo());
		params.put("fulfilLocation", fulfilment.getFulfilLocation());
		params.put("srcLocation", fulfilment.getSrcLocation());
		try {			
			return jdbcTemplate.queryForObject(GET_TSF_NO_QUERY, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// EAT Exception
		}
		return null;
	}

	public BigDecimal getStockAvailablity(ItemFulfilment fulfilment) throws BaseException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("item", fulfilment.getItem());
		ResultSetExtractor<BigDecimal> mapper = new ResultSetExtractor<BigDecimal>() {
			
			@Override
			public BigDecimal extractData(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
			}
		};
		if (fulfilment.getSrcLocationType().equals("ST")) {
			params.put("location", fulfilment.getSrcLocation());
			return jdbcTemplate.query(GET_STORE_STOCK_AVAILABLITY_QUERY, params, mapper);
		} else {
			params.put("location", fulfilment.getSrcLocCHId());
			return jdbcTemplate.query(GET_WH_STOCK_AVAILABLITY_QUERY, params, mapper);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertFulfilmentRequest(ItemFulfilment fulfilment, UserInfo user, RequestType type) throws BaseException {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("item", fulfilment.getItem());
			params.put("customerNo", fulfilment.getOrderInfo().getOrderNumber());
			params.put("omsOrderNo", fulfilment.getOrderInfo().getOmsOrderNumber());
			params.put("fulfilOrdNoOld", fulfilment.getFulfilOrdNo());
			params.put("srcLocTypeOld", fulfilment.getExFulfilment().getSrcLocationType());
			params.put("srcLocOld", fulfilment.getExFulfilment().getSrcLocation());
			params.put("fulfillLocTypeOld", fulfilment.getExFulfilment().getFulfilLocationType());
			params.put("fulfillLocIdOld", fulfilment.getExFulfilment().getFulfilLocation());
			params.put("qty", type.equals(RequestType.CR) ? fulfilment.getExFulfilment().getRequestQty() : fulfilment.getExFulfilment().getCancelledQty());
			if (RequestType.CN.equals(type)) {
				params.put("fulfillOrderNoNew", null);
				params.put("sourceLocTypeNew", null);
				params.put("sourceLocIdNew", null);
				params.put("fulfillLocTypeNew", null);
				params.put("fulfillLocIdNew", null);
				params.put("fulfilQty", null);
			} else {
				params.put("fulfillOrderNoNew", fulfilment.getFulfilOrdNo());
				params.put("sourceLocTypeNew", fulfilment.getSrcLocationType());
				params.put("sourceLocIdNew", fulfilment.getSrcLocation());
				params.put("fulfillLocTypeNew", fulfilment.getFulfilLocationType());
				params.put("fulfillLocIdNew", fulfilment.getFulfilLocation());
				params.put("fulfilQty", fulfilment.getRequestQty());
			}
			params.put("deliveryType", fulfilment.getOrderInfo().getDeliveryType());
			params.put("fulfilItem", fulfilment.getItem());
			params.put("status", "P");
			params.put("fulfillmentSeqId", "");
			params.put("lineNo", fulfilment.getLineNo());
			params.put("userName", user.getUserName());
			jdbcTemplate.update(INSERT_FULFILMENT_REQ_QUERY, params);
		} catch (Exception e) {
			_LOG.error("Error while inserting the request entry ", e);
			throw new BaseException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateFulfilmentRequest(ItemFulfilment fulfilment, String status, String message) {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("item", fulfilment.getItem());
			params.put("customerNo", fulfilment.getOrderInfo().getOrderNumber());
			params.put("omsOrderNo", fulfilment.getOrderInfo().getOmsOrderNumber());
			params.put("fulfilOrdNoOld", fulfilment.getFulfilOrdNo());
			params.put("status", status);
			params.put("stage", message);
			jdbcTemplate.update("UPDATE XX_FULFILL_CAN_CRE_TMP_WS SET STATUS = :status, STAGE = :stage WHERE CUSTOMER_ORDER_NO = :customerNo AND OMS_CUST_ORD_NO = :omsOrderNo AND FULFILL_ORDER_NO_OLD = :fulfilOrdNoOld AND ITEM = :item ", params);
		} catch (Exception e) {
			_LOG.error("Error while updating the status", e);
		}
	}

	public List<ItemFulfilment> validateFulfilments(final Map<String, ItemFulfilment> fulfilmentMap, Set<String> omsOrderNumbers, Set<String> items, Set<Integer> fulfulOrdNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omsOrds", omsOrderNumbers);
		params.put("items", items);
		params.put("fulfilOrds", fulfulOrdNos);
		return jdbcTemplate.query(VALIDATE_FULFILMENT_QUERY, params, new ResultSetExtractor<List<ItemFulfilment>>() {

			@Override
			public List<ItemFulfilment> extractData(ResultSet rs) throws SQLException, DataAccessException {
				StringBuilder remarks = new StringBuilder();
				while(rs.next()) {
					String key = rs.getString(1) + "~" + rs.getString(3) + "~" + rs.getLong(2);
					ItemFulfilment fulfilment = fulfilmentMap.get(key);
					if (fulfilment != null) {
						if (!fulfilment.getOrderInfo().getOrderNumber().equals(rs.getString(12))) {
							remarks.append("Mismatch in customer order number.");
						}
						if (!rs.getBigDecimal(9).equals(fulfilment.getRequestQty())) {
							remarks.append("Mismatch in quantity");
						}

						if (!rs.getString(5).equals(fulfilment.getExFulfilment().getSrcLocationType())) {
							if (remarks.length() > 0) {
								remarks.append(",");
							}
							remarks.append("Mismatch in source location type");
						}
						if (!fulfilment.getExFulfilment().getSrcLocation().equals(rs.getLong(6))) {
							if (remarks.length() > 0) {
								remarks.append(",");
							}
							remarks.append("Mismatch in source location");
						}
						if (!rs.getString(7).equals(fulfilment.getExFulfilment().getFulfilLocationType())) {
							if (remarks.length() > 0) {
								remarks.append(",");
							}
							remarks.append("Mismatch in fulfilment location type");
						}
						Long fulLoc = rs.getLong(8);
						if (!(fulfilment.getExFulfilment().getFulfilLocation().equals(fulLoc) || (fulfilment.getExFulfilment().getFulfilLocCHId() != null && fulfilment.getExFulfilment().getFulfilLocCHId().equals(fulLoc.intValue())) )) {
							if (remarks.length() > 0) {
								remarks.append(",");
							}
							remarks.append("Mismatch in fulfilment location");
						}
						fulfilment.setDeliveryQty(rs.getBigDecimal(10));
						fulfilment.setCancelledQty(rs.getBigDecimal(11));
						fulfilment.getOrderInfo().setDeliveryType(rs.getString(13));
						if (remarks.length() > 0) {
							fulfilment.setRemarks(remarks.toString());
						} else {
							fulfilment.setValid(true);
							fulfilment.setRemarks(null);
						}
					}
					remarks.setLength(0);
				}
				return new ArrayList<ItemFulfilment>(fulfilmentMap.values());
			}
		});
	}

	public void cancelCareraFulfilment(final ItemFulfilment exFulfilment) throws BaseException {

		_LOG.info("Calling procedure to cancel the carera fulfilment " + exFulfilment.getFulfilOrdNo());
		String message = jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {
			
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?) }" );
				stmt.registerOutParameter(1, Types.VARCHAR);
				stmt.registerOutParameter(2, Types.VARCHAR);
				stmt.setBigDecimal(3, new BigDecimal(exFulfilment.getTsfNo()));
				stmt.setString(4, exFulfilment.getItem());
				stmt.setLong(5, exFulfilment.getCancelledQty().longValue());
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
		if (!"Success".equalsIgnoreCase(message)) {
			throw new BaseException(message);
		}
	}
}
