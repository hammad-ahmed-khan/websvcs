/**
 * 
 */
package com.extra.oms.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extra.common.model.OmsBackOrderDtl;
import com.extra.common.model.OmsCoFulfillDetail;
import com.extra.common.model.OmsCustOrdHead;
import com.extra.common.model.OmsCustOrdItem;
import com.extra.common.model.OmsCustOrdReserve;
import com.extra.oms.common.BaseException;
import com.extra.oms.model.OmsCoCancelItem;
import com.extra.oms.model.OmsCoFoCancel;
import com.extra.oms.model.OrderCancelDetailRequest;
import com.extra.oms.model.POSOrderCancelRequest;
import com.extra.oms.model.TransactionDetail;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.SourceLocType;

/**
 * @author aibrahim
 *
 */
@Repository
public class CancelOrderDAO extends BaseDAO {

	private static final Logger LOG = Logger.getLogger(CancelOrderDAO.class);

	public OmsCustOrdHead validateAndGetDetail(POSOrderCancelRequest cancelRequest) throws BaseException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("custOrdNo", cancelRequest.getCustOrderNo());
		params.put("canRequestId", cancelRequest.getCancellationId());

		boolean reqExist = jdbcTemplate.queryForObject("SELECT COUNT(CUST_ORD_NO) FROM OMS_CO_CANCEL_HEAD WHERE CUST_ORD_NO = :custOrdNo AND CANCEL_REQ_ID = :canRequestId", params, Integer.class) > 0;
		if (reqExist) {
			throw new BaseException("CANCEL_ID_EXIST");
		}
		OmsCustOrdHead custOrderHead = jdbcTemplate.query(
				"SELECT H.*, IT.DEPT, IT.INVENTORY_IND, I.*,I.STATUS AS STATUS_ITEM FROM OMS_CUST_ORD_HEAD H INNER JOIN OMS_CUST_ORD_ITEM I ON H.OMS_CUST_ORD_NO = I.OMS_CUST_ORD_NO, ITEM_MASTER IT WHERE CUST_ORDER_NO = :custOrdNo AND IT.ITEM = I.ITEM AND H.STATUS = 'S' ORDER BY I.LINE_LINK_NO NULLS FIRST",
				params, new ResultSetExtractor<OmsCustOrdHead>() {

					@Override
					public OmsCustOrdHead extractData(ResultSet rs) throws SQLException, DataAccessException {
						OmsCustOrdHead custOrderHead = null;
						while (rs.next()) {
							if (rs.isFirst()) {
								custOrderHead = new OmsCustOrdHead();
								custOrderHead.setCustOrderNo(rs.getString("CUST_ORDER_NO"));
								custOrderHead.setSubCustOrderNo(rs.getString("SUB_CUST_ORDER_NO"));
								custOrderHead.setOmsCustOrdNo(rs.getBigDecimal("OMS_CUST_ORD_NO"));
								custOrderHead.setCustOrderType(rs.getString("CUST_ORDER_TYPE"));
								custOrderHead.setOrderRequestorId(rs.getBigDecimal("ORDER_REQUESTOR_ID"));
								custOrderHead.setDeliveryType(rs.getString("DELIVERY_TYPE"));
								custOrderHead.setDeliveryMode(rs.getString("DELIVERY_MODE"));
								custOrderHead.setOrdPaymentStatus(rs.getString("ORD_PAYMENT_STATUS"));
								custOrderHead.setStatus(rs.getString("STATUS"));
								custOrderHead.setOrderCreateReserveInd(rs.getString("ORDER_CREATE_RESERVE_IND"));
								custOrderHead.setPickLoc(rs.getBigDecimal("PICK_LOC"));
								custOrderHead.setPayInStore(rs.getString("PAY_IN_STORE"));
								custOrderHead.setConsumerDlyTime(rs.getTimestamp("CONSUMER_DLY_TIME"));
								custOrderHead.setLastUpdateDatetime(rs.getTimestamp("LAST_UPDATE_DATETIME"));
								custOrderHead.setItemMap(new HashMap<BigDecimal, OmsCustOrdItem>());
							}
							OmsCustOrdItem item = new OmsCustOrdItem();
							item.setOmsCustOrdNo(custOrderHead.getOmsCustOrdNo());
							item.setItem(rs.getString("ITEM"));
							item.setLineNo(rs.getBigDecimal("LINE_NO"));
							item.setShipClassification(rs.getString("SHIP_CLASSIFICATION"));
							item.setSubstituteAllowInd(rs.getString("SUBSTITUTE_ALLOW_IND"));
							item.setBackorderInd(rs.getString("BACKORDER_IND"));
							item.setQtyOrderedSuom(rs.getBigDecimal("QTY_ORDERED_SUOM"));
							item.setCumQtyDelivered(rs.getBigDecimal("CUM_QTY_DELIVERED"));
							item.setQtyCancelled(rs.getBigDecimal("QTY_CANCELLED"));
							item.setQtyReturned(rs.getBigDecimal("QTY_RETURNED"));
							item.setStandardUom(rs.getString("STANDARD_UOM"));
							item.setTransactionUom(rs.getString("TRANSACTION_UOM"));
							item.setUnitRetail(rs.getBigDecimal("UNIT_RETAIL"));
							item.setOrigUnitRetail(rs.getBigDecimal("ORIG_UNIT_RETAIL"));
							item.setStatus(rs.getString("STATUS_ITEM"));
							item.setLineLinkNo(rs.getBigDecimal("LINE_LINK_NO"));
							item.setUnitVatAmount(rs.getBigDecimal("UNIT_VAT_AMOUNT"));
							item.setRsaItem(rs.getString("RSAITEM"));
							item.setItemDept(rs.getLong("DEPT"));
							item.setInvInd(rs.getString("INVENTORY_IND"));
							custOrderHead.getItemMap().put(item.getLineNo(), item);
						}
						return custOrderHead;
					}
				});
		return custOrderHead;
	}

	@Transactional()
	public Long saveCancellationRequest(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead) {

		MapSqlParameterSource paramSource[] = new MapSqlParameterSource[cancelRequest.getCancellationItems().size()];
		int i = 0;

		Long seqId = jdbcTemplate.queryForObject("SELECT XX_ORD_CANCEL_VALID_SEQ.NEXTVAL FROM DUAL", Collections.<String, Object>emptyMap(), Long.class);
		for (OrderCancelDetailRequest iteReq : cancelRequest.getCancellationItems()) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("seqId", seqId);
			param.addValue("custOrdNo", cancelRequest.getCustOrderNo());
			param.addValue("omsCustOrdNo", custOrdHead.getOmsCustOrdNo());
			param.addValue("cancelRequestorId", cancelRequest.getCancellationRequestorId());
			param.addValue("cancellationId", cancelRequest.getCancellationId());
			param.addValue("item", iteReq.getItem());
			param.addValue("lineNo", iteReq.getLineNo());
			param.addValue("cancelReqQty", iteReq.getCancelQtySuom());
			param.addValue("refundOption", cancelRequest.getRefundPreference());
			param.addValue("tenderAmount", cancelRequest.getRefundAmount());
			param.addValue("comment", iteReq.getItemComments());
			paramSource[i++] = param;
		}
		jdbcTemplate.batchUpdate(
				"INSERT INTO XX_ORD_CANCELLATION_REQ VALUES(:seqId, :custOrdNo, :omsCustOrdNo, :cancelRequestorId, :cancellationId, :item, :lineNo, :cancelReqQty, :refundOption, :tenderAmount, SYSDATE, :comment)",
				paramSource);
		return seqId;
	}

	public void callCancelPackage(POSOrderCancelRequest cancelRequest, final OmsCustOrdHead custOrdHead, final Long seqId) throws BaseException {
		// _LOG.info("Calling procedure to validate the cancellation reqest " +
		// custOrdHead.getCustOrderNo());
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("status", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("refundAmount", Types.NUMERIC));
		Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ call XX_IS_CANCELLABLE(?, ?, ?, ?, ?) }");
				stmt.setLong(1, seqId);
				stmt.setLong(2, custOrdHead.getOmsCustOrdNo().longValue());
				stmt.registerOutParameter(3, Types.NUMERIC);
				stmt.registerOutParameter(4, Types.VARCHAR);
				stmt.registerOutParameter(5, Types.NUMERIC);
				return stmt;
			}
		}, declaredParameters);
		BigDecimal status = (BigDecimal) result.get("status");
		if (status.intValue() == 0) {
			String message = (String) result.get("message");
			throw new BaseException(message, message);
		} else if (cancelRequest.getRefundAmount().compareTo(new BigDecimal(result.get("refundAmount").toString())) != 0) {
			LOG.error("Mismatch in refund amount for the order " + cancelRequest.getCustOrderNo() + "Refund amount -" + result.get("refundAmount").toString());
//			throw new BaseException("INVALID_REFUND_AMOUNT");
		}
		saveOmsCancelHead(cancelRequest, custOrdHead);
	}

	public Map<BigDecimal, OmsCustOrdReserve> getReserveItems(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omsCustOrdNo", custOrdHead.getOmsCustOrdNo());
		Set<Integer> lineNos = new HashSet<Integer>();
		for (OrderCancelDetailRequest itemCanReq : cancelRequest.getCancellationItems()) {
			lineNos.add(itemCanReq.getLineNo().intValue());
		}
		params.put("lineNos", lineNos);
		return jdbcTemplate.query(
				"SELECT OMS_CUST_ORD_NO, FULFILL_ORDER_NO, ITEM, LINE_NO, LOC_TYPE, LOC, QTY, RMS_RESV_LOC_TYPE, RMS_RESV_LOC, RMS_RESV_QTY, PAYMENT_STATUS, RESV_STATUS FROM OMS_CUST_ORD_RESERVE WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND LINE_NO IN (:lineNos)",
				params, new ResultSetExtractor<Map<BigDecimal, OmsCustOrdReserve>>() {

					@Override
					public Map<BigDecimal, OmsCustOrdReserve> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<BigDecimal, OmsCustOrdReserve> reseveOrdMap = new HashMap<BigDecimal, OmsCustOrdReserve>();
						while (rs.next()) {
							BigDecimal lineNo = rs.getBigDecimal(4);
							OmsCustOrdReserve reserve = new OmsCustOrdReserve();
							reserve.setOmsCustOrdNo(rs.getBigDecimal(1));
							reserve.setFulfillOrderNo(rs.getBigDecimal(2));
							reserve.setItem(rs.getString(3));
							reserve.setLocType(rs.getString(5));
							reserve.setLoc(rs.getBigDecimal(6));
							reserve.setQty(rs.getBigDecimal(7));
							reserve.setRmsResvLocType(rs.getString(8));
							reserve.setRmsResvLoc(rs.getBigDecimal(9));
							reserve.setRmsResvQty(rs.getBigDecimal(10));
							reserve.setPaymentStatus(rs.getString(11));
							reserve.setResvStatus(rs.getString(12));
							reseveOrdMap.put(lineNo, reserve);
						}
						return reseveOrdMap;
					}
				});
	}

	public void cancelWHAdjusment(List<Entry<OrderCancelDetailRequest, OmsCustOrdReserve>> whRsrvs, final Map<String, String> sysParam, String cusOrdNo) throws BaseException {
		LOG.info("Calling procedure to canecl the adjustment for warehouse ");
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);
		Map<String, Object> resultMap = null;
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("status", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		final String userId = cusOrdNo + "_CL";
		for (final Entry<OrderCancelDetailRequest, OmsCustOrdReserve> reserve : whRsrvs) {
			resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{ CALL OMS_INVADJ_STATUS_UNAVIALINV(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
					stmt.setString(1, reserve.getValue().getItem());
					stmt.setInt(2, Integer.parseInt(sysParam.get("RESV_INV_STATUS")));
					stmt.setString(3, "W");
					stmt.setBigDecimal(4, reserve.getValue().getRmsResvLoc());
					stmt.setBigDecimal(5, reserve.getKey().getCancelQtySuom().negate());
					stmt.setInt(6, Integer.parseInt(sysParam.get("REASON_CODE")));
					stmt.setString(7, userId);
					stmt.registerOutParameter(8, Types.NUMERIC);
					stmt.registerOutParameter(9, Types.VARCHAR);
					return stmt;
				}
			}, declaredParameters);
			Integer status = ((BigDecimal) resultMap.get("status")).intValue();
			LOG.info("Response received from the adjustment procedure call for item " + reserve.getValue().getItem() + " is " + status);
			if (status != 1) {
				String message = resultMap.get("message").toString();
				LOG.error("Business Error: " + message);
				throw new BaseException(message);
			}
		}
	}

	public Map<String, String> getSystemParams(List<String> paramIds, String... param) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paramIds", paramIds);
		params.put("params", Arrays.asList(param));
		return jdbcTemplate.query("SELECT PARAMETER_VALUE, PARAMETER_NAME FROM OMS_SYSTEM_PARAMETERS WHERE PARAMETER_ID IN (:paramIds) AND PARAMETER_NAME IN (:params)", params,
				new ResultSetExtractor<Map<String, String>>() {

					@Override
					public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<String, String> paramValues = new HashMap<String, String>();
						while (rs.next()) {
							paramValues.put(rs.getString(2), rs.getString(1));
						}
						return paramValues;
					}
				});
	}

	public Map<BigDecimal, OmsBackOrderDtl> getBackOrderDetail(OmsCustOrdHead custOrdHead, POSOrderCancelRequest cancelRequest) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omsCustOrdNo", custOrdHead.getOmsCustOrdNo());
		List<BigDecimal> lineIds = new ArrayList<BigDecimal>();
		for (OrderCancelDetailRequest req : cancelRequest.getCancellationItems()) {
			lineIds.add(req.getLineNo());
		}
		params.put("lineIds", lineIds);
		return jdbcTemplate.query(
				"SELECT OMS_CUST_ORD_NO, FULFILL_ORDER_NO, ITEM, LINE_NO, FULFILL_LOC_TYPE, FULFILL_LOC, FULFILL_QTY, SOURCE_LOC_TYPE, SOURCE_LOC, SOURCE_QTY, BACKORDER_STATUS FROM OMS_BACK_ORDER_DTL WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND LINE_NO IN (:lineIds) ",
				params, new ResultSetExtractor<Map<BigDecimal, OmsBackOrderDtl>>() {

					@Override
					public Map<BigDecimal, OmsBackOrderDtl> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<BigDecimal, OmsBackOrderDtl> boOrdDtlMap = new HashMap<BigDecimal, OmsBackOrderDtl>();
						while (rs.next()) {
							BigDecimal lineNo = rs.getBigDecimal(4);
							OmsBackOrderDtl boOrdDtl = new OmsBackOrderDtl();
							boOrdDtl.setLineNo(lineNo);
							boOrdDtl.setOmsCustOrdNo(rs.getBigDecimal(1));
							boOrdDtl.setFulfillOrderNo(rs.getBigDecimal(2));
							boOrdDtl.setItem(rs.getString(3));
							boOrdDtl.setSourceLocType(rs.getString(8));
							boOrdDtl.setSourceLoc(rs.getBigDecimal(9));
							boOrdDtl.setSourceQty(rs.getBigDecimal(10));
							boOrdDtl.setFulfillLocType(rs.getString(5));
							boOrdDtl.setFulfillLoc(rs.getBigDecimal(6));
							boOrdDtl.setFulfillQty(rs.getBigDecimal(7));
							boOrdDtl.setBackorderStatus(rs.getString(11));
							boOrdDtlMap.put(lineNo, boOrdDtl);
						}
						return boOrdDtlMap;
					}
				});
	}

	public Map<BigDecimal, Set<OmsCoFulfillDetail>> getFulfilmentDetail(OmsCustOrdHead custOrdHead, POSOrderCancelRequest cancelRequest) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("omsCustOrdNo", custOrdHead.getOmsCustOrdNo());
		List<BigDecimal> lineIds = new ArrayList<BigDecimal>();
		for (OrderCancelDetailRequest req : cancelRequest.getCancellationItems()) {
			lineIds.add(req.getLineNo());
		}
		params.put("lineIds", lineIds);
		return jdbcTemplate.query(
				"SELECT OMS_CUST_ORD_NO, FULFILL_ORDER_NO, ITEM, LINE_NO, SOURCE_LOC_TYPE, SOURCE_LOC, FULFILL_LOC_TYPE, FULFILL_LOC, FULFILL_REQ_QTY, FULFILL_CONF_QTY, FULFILL_DELIVER_QTY, FULFILL_CANCEL_QTY, FULFILL_STATUS, TSF_NO FROM OMS_CO_FULFILL_DETAIL WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND LINE_NO IN (:lineIds) ",
				params, new ResultSetExtractor<Map<BigDecimal, Set<OmsCoFulfillDetail>>>() {

					@Override
					public Map<BigDecimal, Set<OmsCoFulfillDetail>> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<BigDecimal, Set<OmsCoFulfillDetail>> fulfilMap = new HashMap<BigDecimal, Set<OmsCoFulfillDetail>>();
						while (rs.next()) {
							BigDecimal lineNo = rs.getBigDecimal(4);
							Set<OmsCoFulfillDetail> fulfils = fulfilMap.get(lineNo);
							if (fulfils == null) {
								fulfils = new TreeSet<OmsCoFulfillDetail>(new Comparator<OmsCoFulfillDetail>() {

									@Override
									public int compare(OmsCoFulfillDetail o1, OmsCoFulfillDetail o2) {
										if (o1.getSourceLocType().equals("SU")) {
											return o2.getSourceLocType().equals("SU") ? 1 : -1;
										}
										return o2.getSourceLocType().equals("SU") ? 1 : -1;
									}
								});
								fulfilMap.put(lineNo, fulfils);
							}
							OmsCoFulfillDetail fulfil = new OmsCoFulfillDetail();
							fulfil.setLineNo(lineNo);
							fulfil.setOmsCustOrdNo(rs.getBigDecimal(1));
							fulfil.setFulfillOrderNo(rs.getBigDecimal(2));
							fulfil.setItem(rs.getString(3));
							fulfil.setSourceLocType(rs.getString(5));
							fulfil.setSourceLoc(rs.getBigDecimal(6));
							fulfil.setFulfillLocType(rs.getString(7));
							fulfil.setFulfillLoc(rs.getBigDecimal(8));
							fulfil.setFulfillReqQty(rs.getBigDecimal(9));
							fulfil.setFulfillConfQty(rs.getBigDecimal(10));
							fulfil.setFulfillDeliverQty(rs.getBigDecimal(11));
							fulfil.setFulfillCancelQty(rs.getBigDecimal(12));
							fulfil.setFulfillStatus(rs.getString(13));
							fulfil.setTsfNo(rs.getBigDecimal(14));
							fulfils.add(fulfil);
						}
						return fulfilMap;
					}
				});
	}

	public void cancelCareraFulfilment(final OmsCoFulfillDetail fulfil, final BigDecimal canQty) throws BaseException {

		LOG.info("Calling procedure to cancel the carera fulfilment " + fulfil.getFulfillOrderNo());
		String message = jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?) }");
				stmt.registerOutParameter(1, Types.VARCHAR);
				stmt.registerOutParameter(2, Types.VARCHAR);
				stmt.setBigDecimal(3, fulfil.getTsfNo());
				stmt.setString(4, fulfil.getItem());
				stmt.setBigDecimal(5, canQty);
				return stmt;
			}
		}, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				String message = cs.getString(1);
				return message;
			}
		});
		LOG.info("Message received for the carera fulfilment " + fulfil.getFulfillOrderNo() + " is " + message);
		if (!"Success".equalsIgnoreCase(message)) {
			throw new BaseException(message);
		}
	}

	private void saveOmsCancelHead(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead) throws BaseException {

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("entityId", cancelRequest.getEntityId());
		param.addValue("applicationId", cancelRequest.getApplicationId());
		param.addValue("custOrdNo", cancelRequest.getCustOrderNo());
		param.addValue("subCustOrdNo", cancelRequest.getSubCustOrderNo() != null ? cancelRequest.getSubCustOrderNo() : "1");
		param.addValue("cancelRequestorId", cancelRequest.getCancellationRequestorId());
		param.addValue("cancelReqId", cancelRequest.getCancellationId());
		param.addValue("comment", cancelRequest.getComments());
		param.addValue("refundOption", cancelRequest.getRefundPreference());
		param.addValue("refundInd", (cancelRequest.getRefundPreference().equals("CLEARING")) ? "Y" : "N");
		param.addValue("status", "N");
		param.addValue("refundAmount", cancelRequest.getRefundAmount());
		param.addValue("reasonCode", cancelRequest.getReasonCode());
		param.addValue("reason", cancelRequest.getReason());

		GeneratedKeyHolder seqHolder = new GeneratedKeyHolder();
		List<String> itemIds = new ArrayList<String>();
		for (OrderCancelDetailRequest request : cancelRequest.getCancellationItems()) {
			itemIds.add(request.getItem());
		}
		param.addValue("itemIds", itemIds);
		jdbcTemplate.update(
				" INSERT INTO OMS_CO_CANCEL_HEAD(ENTITY_ID, APPLICATION_ID, CANCEL_REQST_LOC_ID, CUST_ORD_NO, SUB_CUST_ORD_NO, CANCEL_REQ_ID, OMS_CANCEL_ID, COMMENTS, REFUND_OPTION, REFUND_COMPLT_IND, REFUND_AMOUNT, STATUS, CAN_REQ_DATETIME, CREATE_DATETIME, REASON_CODE, REASON) "
						+ " VALUES(:entityId, :applicationId, :cancelRequestorId, :custOrdNo, :subCustOrdNo, :cancelReqId, OMS_CANCEL_ID_SEQ.NEXTVAL, :comment, :refundOption, (SELECT CASE WHEN (COUNT(CR.REQUESTOR_ID) > 0) THEN 'Z' ELSE :refundInd END FROM OMS_CO_FULFILL_DETAIL F, OMS_CUST_ORD_HEAD C, "
						+ "OMS_CO_CANCEL_REGION CR where F.OMS_CUST_ORD_NO = C.OMS_CUST_ORD_NO and F.SOURCE_LOC_TYPE = 'SU' AND C.STATUS = 'S' and CR.REQUESTOR_ID = C.ORDER_REQUESTOR_ID and C.CUST_ORDER_NO = :custOrdNo AND F.ITEM IN (:itemIds)), :refundAmount, :status, SYSDATE, SYSDATE, :reasonCode, :reason)",
				param, seqHolder, new String[] { "OMS_CANCEL_ID" });
		cancelRequest.setOmsCancelId(((BigDecimal) seqHolder.getKeys().get("OMS_CANCEL_ID")).longValue());
		param.addValue("omsCustOrdNo", custOrdHead.getOmsCustOrdNo());
		param.addValue("omsCancelId", cancelRequest.getOmsCancelId());
		jdbcTemplate.update(
				"INSERT INTO OMS_CUST_ORD_LOG(OMS_CUST_ORD_NO, LOG_SEQ_NO, EVENT_ID, CREATE_DATETIME, OMS_CANCEL_ID) VALUES(:omsCustOrdNo, OMS_LOG_SEQ_NO_SEQ.NEXTVAL, 'CA', SYSDATE, :omsCancelId)",
				param, seqHolder, new String[] { "LOG_SEQ_NO" });
		cancelRequest.setLogSeqId(((BigDecimal) seqHolder.getKeys().get("LOG_SEQ_NO")).longValue());
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public void updateCancellationDetail(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead, Map<String, Object> updates) {
		List<SqlParameterSource> paramSource = new ArrayList<SqlParameterSource>();

		List<OmsCoCancelItem> cancelItems = (List<OmsCoCancelItem>) updates.get("cancelItems");
		if (cancelItems != null && !cancelItems.isEmpty()) {
			for (OmsCoCancelItem canclItem : cancelItems) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCancelId", canclItem.getOmsCancelId());
				param.addValue("lineNo", canclItem.getLineNo());
				param.addValue("item", canclItem.getItem());
				param.addValue("cancelReqQty", canclItem.getCancelReqQty());
				param.addValue("cancelCnfQty", canclItem.getCancelConfQty());
				param.addValue("comment", canclItem.getComments());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"INSERT INTO OMS_CO_CANCEL_ITEM(OMS_CANCEL_ID, LINE_NO, ITEM, CANCEL_REQ_QTY, CANCEL_CONF_QTY, COMMENTS, CREATE_DATETIME) VALUES(:omsCancelId, :lineNo, :item, :cancelReqQty, :cancelCnfQty, :comment, SYSDATE)",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
			paramSource.clear();
		}
		List<OmsCustOrdItem> ordItems = (List<OmsCustOrdItem>) updates.get("items");
		if (ordItems != null && !ordItems.isEmpty()) {
			for (OmsCustOrdItem item : ordItems) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", item.getOmsCustOrdNo());
				param.addValue("lineNo", item.getLineNo());
				param.addValue("item", item.getItem());
				param.addValue("cancelQty", item.getQtyCancelled());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"UPDATE OMS_CUST_ORD_ITEM SET QTY_CANCELLED = :cancelQty, LAST_UPDATE_DATETIME = SYSDATE WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND ITEM = :item AND LINE_NO = :lineNo",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
			paramSource.clear();
		}

		List<OmsCoFulfillDetail> omsFulfils = (List<OmsCoFulfillDetail>) updates.get("cancelOmsFulfils");
		if (omsFulfils != null && !omsFulfils.isEmpty()) {
			for (OmsCoFulfillDetail omsFulfil : omsFulfils) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", omsFulfil.getOmsCustOrdNo());
				param.addValue("lineNo", omsFulfil.getLineNo());
				param.addValue("item", omsFulfil.getItem());
				param.addValue("cancelQty", omsFulfil.getFulfillCancelQty());
				param.addValue("fulfilOrdNo", omsFulfil.getFulfillOrderNo());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"UPDATE OMS_CO_FULFILL_DETAIL SET FULFILL_CANCEL_QTY = :cancelQty, LAST_UPDATE_DATETIME = SYSDATE WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND ITEM = :item AND LINE_NO = :lineNo AND FULFILL_ORDER_NO = :fulfilOrdNo",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
			paramSource.clear();
		}

		List<OmsCoFoCancel> canFulfilItems = (List<OmsCoFoCancel>) updates.get("cancelFulfils");
		if (canFulfilItems != null && !canFulfilItems.isEmpty()) {
			for (OmsCoFoCancel coFulfil : canFulfilItems) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCancelId", coFulfil.getOmsCancelId());
				param.addValue("lineNo", coFulfil.getLineNo());
				param.addValue("item", coFulfil.getItem());
				param.addValue("fulfilOrdNo", coFulfil.getFulfillOrderNo());
				param.addValue("cancelQty", coFulfil.getFoCancelledOty());
				param.addValue("wsResponse", coFulfil.getWsResponse());

				/* For OmsCutomerOrderLogItem Table */
				param.addValue("logSeqNo", cancelRequest.getLogSeqId());
				param.addValue("qty", coFulfil.getFoCancelledOty());
				param.addValue("fulfilOrdNo", coFulfil.getFulfillOrderNo());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate("INSERT INTO OMS_CO_FO_CANCEL VALUES(:omsCancelId, :item, :lineNo, :fulfilOrdNo, :cancelQty, SYSDATE, :wsResponse)",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));

			if (custOrdHead.getOrdPaymentStatus().equals("S")) {
				jdbcTemplate.batchUpdate("INSERT INTO OMS_CUST_ORD_LOG_ITEM VALUES(:item, :lineNo, :logSeqNo, :qty, :fulfilOrdNo, SYSDATE)",
						paramSource.toArray(new SqlParameterSource[paramSource.size()]));
			}
			paramSource.clear();
		}

		List<OmsCustOrdReserve> resvItems = (List<OmsCustOrdReserve>) updates.get("reserves");
		if (resvItems != null && !resvItems.isEmpty()) {
			for (OmsCustOrdReserve reserve : resvItems) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", reserve.getOmsCustOrdNo());
				param.addValue("lineNo", reserve.getLineNo());
				param.addValue("item", reserve.getItem());
				param.addValue("resvLoc", reserve.getRmsResvLoc());
				param.addValue("qty", reserve.getQty());
				param.addValue("resvQty", reserve.getRmsResvQty());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"UPDATE OMS_CUST_ORD_RESERVE SET QTY = :qty, RMS_RESV_QTY = :resvQty WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND ITEM = :item AND LINE_NO = :lineNo AND RMS_RESV_LOC = :resvLoc",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
			paramSource.clear();
		}

		List<OmsBackOrderDtl> backOrdDtls = (List<OmsBackOrderDtl>) updates.get("boUpdates");
		if (backOrdDtls != null && !backOrdDtls.isEmpty()) {
			for (OmsBackOrderDtl backOrd : backOrdDtls) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", backOrd.getOmsCustOrdNo());
				param.addValue("lineNo", backOrd.getLineNo());
				param.addValue("item", backOrd.getItem());
				param.addValue("sourceLoc", backOrd.getSourceLoc());
				param.addValue("srcQty", backOrd.getSourceQty());
				param.addValue("status", backOrd.getBackorderStatus());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"UPDATE OMS_BACK_ORDER_DTL SET SOURCE_QTY = :srcQty, BACKORDER_STATUS = :status WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND ITEM = :item AND LINE_NO = :lineNo AND SOURCE_LOC = :sourceLoc",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
			paramSource.clear();
		}

		if (cancelRequest.getRefundPreference().equals("CLEARING") && custOrdHead.getPayInStore().equals("N") && custOrdHead.getOrdPaymentStatus().equals("S")) {
			for (OrderCancelDetailRequest canReqItem : cancelRequest.getCancellationItems()) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", custOrdHead.getOmsCustOrdNo());
				param.addValue("omsCancelId", cancelRequest.getOmsCancelId());
				param.addValue("lineNo", canReqItem.getLineNo());
				param.addValue("item", canReqItem.getItem());
				param.addValue("qty", canReqItem.getCancelQtySuom());
				param.addValue("location", Long.parseLong(cancelRequest.getCancellationRequestorId()));
				param.addValue("publishInd",(cancelRequest.getCancellationRequestorId().startsWith("2") && updates.get("isPOOrder") != null) ? "Z" : "N");
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO, OMS_CUST_ORD_NO, ITEM, LINE_NO, QTY, LOCATION, TRAN_TYPE, PUBLISHED_IND, OMS_CANCEL_ID, CREATE_DATETIME) VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.NEXTVAL, :omsCustOrdNo, :item, :lineNo, :qty, :location, 'ORC', :publishInd, :omsCancelId, SYSDATE)",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
			paramSource.clear();
		}

		Map<String, Number> omsParam = new HashMap<String, Number>();
		omsParam.put("omsCustOrdNo", custOrdHead.getOmsCustOrdNo());
		omsParam.put("omsCancelId", cancelRequest.getOmsCancelId());
		String ordCompleted = jdbcTemplate.queryForObject(
				"SELECT CASE WHEN SUM(QTY_ORDERED_SUOM) = SUM(CUM_QTY_DELIVERED) + SUM(QTY_CANCELLED) THEN 'TRUE' ELSE 'FALSE' END FROM oms_cust_ord_item WHERE OMS_CUST_ORD_NO = :omsCustOrdNo",
				omsParam, String.class);
		if (ordCompleted.equalsIgnoreCase("TRUE")) {
			String updateOrdQuery = "UPDATE OMS_CUST_ORD_HEAD SET LAST_UPDATE_DATETIME = SYSDATE, CLOSE_DATETIME = SYSDATE PAYMENT_STATUS WHERE OMS_CUST_ORD_NO = :omsCustOrdNo ";
			if (updates.get("rejectTender") != null) {
				jdbcTemplate.update("UPDATE OMS_CUST_ORD_TENDER SET PAYMENT_STATUS_IND = 'R' WHERE OMS_CUST_ORD_NO = :omsCustOrdNo", omsParam);
				updateOrdQuery = updateOrdQuery.replace("PAYMENT_STATUS", " ,ORD_PAYMENT_STATUS = 'R' ");
			} else {
				updateOrdQuery = updateOrdQuery.replace("PAYMENT_STATUS", "");
			}
			jdbcTemplate.update(updateOrdQuery, omsParam);
			jdbcTemplate.update(
					"INSERT INTO OMS_CUST_ORD_LOG(OMS_CUST_ORD_NO, LOG_SEQ_NO, EVENT_ID, CREATE_DATETIME, OMS_CANCEL_ID) VALUES(:omsCustOrdNo, OMS_LOG_SEQ_NO_SEQ.NEXTVAL, 'CL', SYSDATE, :omsCancelId)",
					omsParam);
		}
	}

	public Map<String, Entry<BigDecimal, BigDecimal>> getPickedQuantities(String custOrdNo, Collection<Set<OmsCoFulfillDetail>> fulfilSet) throws BaseException {
		List<String> simItems = new ArrayList<String>();
		List<BigDecimal> simFulfilOrds = new ArrayList<BigDecimal>();
		List<String> rmsItems = new ArrayList<String>();
		List<BigDecimal> rmsTSFNos = new ArrayList<BigDecimal>();
		for (Set<OmsCoFulfillDetail> fulfils : fulfilSet) {
			for (OmsCoFulfillDetail fulfil : fulfils) {
				if (SourceLocType.ST.equals(SourceLocType.fromValue(fulfil.getSourceLocType())) && fulfil.getSourceLoc().equals(fulfil.getFulfillLoc())) {
					simItems.add(fulfil.getItem());
					simFulfilOrds.add(fulfil.getFulfillOrderNo());
				} else if (fulfil.getTsfNo() != null && !SourceLocType.SU.equals(SourceLocType.fromValue(fulfil.getSourceLocType()))) {
					rmsItems.add(fulfil.getItem());
					rmsTSFNos.add(fulfil.getTsfNo());
				}
			}
		}
		final Map<String, Entry<BigDecimal, BigDecimal>> pickQtyMap = new HashMap<String, Map.Entry<BigDecimal, BigDecimal>>();
		Map<String, Object> params = new HashMap<String, Object>();
		if (!simItems.isEmpty()) {
			params.put("custOrdId", custOrdNo);
			params.put("itemIds", simItems);
			params.put("fulfilOrdIds", simFulfilOrds);
			/*
			 * 
			 */
			List<Long> dlvIDs = simJdbcTemplate.queryForList("SELECT FD.ID FROM FUL_ORD FU, FUL_ORD_DLV FD WHERE FU.ID = FD.FUL_ORD_ID AND FU.CUST_ORDER_ID = :custOrdId AND FU.EXTERNAL_ID IN (:fulfilOrdIds) AND FD.STATUS = 1", params, Long.class);
			if (dlvIDs != null && !dlvIDs.isEmpty()) {
				throw new BaseException("Delivery in progress, please cancel and try again", "Delivery in progress, please cancel and try again");
			}
			/* */
			simJdbcTemplate.query(
					"SELECT FU.EXTERNAL_ID, FI.ITEM_ID, FI.QUANTITY_ORDERED, FI.QUANTITY_PICKED, FI.QUANTITY_CANCELED, SUM(D.QUANTITY) FROM FUL_ORD FU, FUL_ORD_LINE_ITEM FI LEFT OUTER JOIN FUL_ORD_DLV C ON FI.FUL_ORD_ID = C.FUL_ORD_ID AND C.STATUS != '4' LEFT OUTER JOIN FUL_ORD_DLV_LINE_ITEM D ON C.ID = D.FUL_ORD_DLV_ID AND FI.ID = D.FUL_ORD_LINE_ITEM_ID LEFT OUTER JOIN SHIPMENT_BOL E ON C.SHIPMENT_BOL_ID = E.ID LEFT OUTER JOIN SHIPMENT_CARRIER_SERVICE F ON E.SHIP_CARRIER_SERVICE_ID = F.ID AND F.DESCRIPTION = 'Handed to Carrier' AND E.SHIP_CARRIER_ID = F.SHIPMENT_CARRIER_ID WHERE FI.FUL_ORD_ID = FU.ID AND FU.cust_order_id = :custOrdId AND FI.ITEM_ID IN (:itemIds) AND FU.EXTERNAL_ID IN (:fulfilOrdIds) GROUP BY FU.EXTERNAL_ID,FI.ITEM_ID, FI.QUANTITY_ORDERED, FI.QUANTITY_CANCELED, FI.QUANTITY_PICKED, FI.QUANTITY_CANCELED",
					params, new RowCallbackHandler() {

						@Override
						public void processRow(ResultSet rs) throws SQLException {
							BigDecimal pickQty = BigDecimal.valueOf(Math.max(rs.getLong(4), rs.getLong(6)));
							pickQtyMap.put(rs.getString(2) + "~" + rs.getBigDecimal(1), new SimpleEntry<BigDecimal, BigDecimal>(rs.getBigDecimal(3), pickQty.add(rs.getBigDecimal(5))));
						}
					});
		}
		if (!rmsItems.isEmpty()) {
			params.clear();
			params.put("itemIds", rmsItems);
			params.put("rmsTSFNos", rmsTSFNos);
			jdbcTemplate.query(
					"SELECT TSF_NO, ITEM, TSF_QTY, NVL(SHIP_QTY, 0) SHIP_QTY, NVL(DISTRO_QTY, 0) DISTRO_QTY, NVL(SELECTED_QTY, 0) SELECTED_QTY, NVL(CANCELLED_QTY, 0) CANCELLED_QTY FROM TSFDETAIL WHERE TSF_NO IN (:rmsTSFNos) AND ITEM IN (:itemIds)",
					params, new RowCallbackHandler() {

						@Override
						public void processRow(ResultSet rs) throws SQLException {
							pickQtyMap.put(rs.getString(1),
									new SimpleEntry<BigDecimal, BigDecimal>(rs.getBigDecimal(3).add(rs.getBigDecimal(7)), rs.getBigDecimal(4).add(rs.getBigDecimal(5)).add(rs.getBigDecimal(6))));
						}
					});
		}
		return pickQtyMap;
	}

	public void saveWSPublishReq(POSOrderCancelRequest cancelRequest, String wsMessage, String error) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appId", cancelRequest.getApplicationId());
		params.put("transKey", cancelRequest.getCustOrderNo());
		params.put("message", wsMessage);
		params.put("error", error);
		LOG.info("Saving soap xml request to rebuplish data table for ord numner -> " + cancelRequest.getCustOrderNo() + " and XML -> " + wsMessage);
		jdbcTemplate.update(
				"INSERT INTO OMS_REPUBLISH_DATA VALUES(OMS_REPUBLISH_DATASEQ.NEXTVAL, :appId, SYSDATE, :transKey, :message, (SELECT WEB_SERVICE_ID FROM OMS_WEBSERVICE_URI_DETAIL WHERE WEB_SERVICE_NAME = 'SOA_CO_STATUS_UPDATE'), 0, :error, 'N', SYSDATE)",
				params);
		LOG.info("Soap xml request to rebuplish data table for ord numner -> " + cancelRequest.getCustOrderNo() + " is saved successfully.");
	}

	public void saveEInvoicingReq(POSOrderCancelRequest cancelRequest) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("invId", dateFormat.format(cancelRequest.getCancellationDate()) + cancelRequest.getCancellationRequestorId() + cancelRequest.getOmsCancelId());
		params.put("orderNo", cancelRequest.getCustOrderNo());
		params.put("refundOpt", cancelRequest.getRefundPreference());
		params.put("cancellationId", cancelRequest.getOmsCancelId());
		params.put("storeId", cancelRequest.getCancellationRequestorId());
		LOG.info("Saving e-invoicing request to report gazat for ord numner -> " + cancelRequest.getCustOrderNo());
		jdbcTemplate.update(
				"INSERT INTO XX_EINV_CAN_RET_OMS(UNIQUE_INV_ID, CUST_ORDER_NO, TRAN_TYPE, REFUND_OPTION, INSERT_DATE, PROCESS_FLAG, CAN_RET_ID, STORE_ID) VALUES(:invId, :orderNo, 'ORC', :refundOpt, SYSDATE, 'N', :cancellationId, :storeId)",
				params);
		params.put("flag", "Y");
		jdbcTemplate.update("UPDATE OMS_CO_CANCEL_HEAD SET EIN_PROCESS_FLAG = :flag WHERE OMS_CANCEL_ID = :cancellationId", params);
		LOG.info("EInvoicing request to report gazat for ord numner -> " + cancelRequest.getCustOrderNo() + " is saved successfully.");
	}

	public String cancelOddBooking(final OmsCustOrdHead custOrderHead) throws BaseException {
		String responseStatus = null;
		if ("ODDSMALL".equals(custOrderHead.getDeliveryMode())) {
			LOG.info("STARTED CANCELLING ODD BOOKING");
			try {
				List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
				declaredParameters.add(new SqlParameter(Types.NUMERIC));
				declaredParameters.add(new SqlParameter(Types.VARCHAR));
				declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
				declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
				Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement stmt = con.prepareCall("{ call XXHDB_CORE_PKG.cancel_booking_small(?, ?, ?, ?) }");
						stmt.setInt(1, custOrderHead.getOmsCustOrdNo().intValue());
						stmt.setString(2, custOrderHead.getCustOrderNo());
						stmt.registerOutParameter(3, Types.VARCHAR);
						stmt.registerOutParameter(4, Types.VARCHAR);
						return stmt;
					}
				}, declaredParameters);

				String status = (String) result.get("status");
				String message = (String) result.get("message");
				LOG.info("Result of Package--  Status -" + status + "Message -" + message);
				if ("S".equals(status)) {
					LOG.info("CANCELLED ODD BOOKING SUCCESSFULLY");
					responseStatus = status;
				} else {
					LOG.error("FAILED CANCELLATION OF ODD BOOKING");
					responseStatus = status;
					throw new BaseException("Cancellation failed", "Cancellation failed");
				}
				if (message != null) {
					LOG.error("FAILED CANCELLATION OF ODD BOOKING: {}" + message);
					throw new BaseException(message, message);
				}
			} catch (Exception e) {
				LOG.error("Error while cancelling ODD BOOKING", e);
				throw new BaseException("Error while cancelling ODD BOOKING", e.getMessage());
			}
		}
		return responseStatus;
	}

	public void saveJoodTransactionDetail(final OmsCustOrdHead custOrdHead, List<OrderCancelDetailRequest> cancelItemsList) throws BaseException {
		LOG.info("Calling Jood Transaction Detail Package");
		final BigDecimal[] totalRetailPrice = { BigDecimal.ZERO };
		final BigDecimal[] totalRetailDiscount = { BigDecimal.ZERO };
		final BigDecimal[] totalJoodDiscount = { BigDecimal.ZERO };
		final BigDecimal[] totalSellingPrice = { BigDecimal.ZERO };
		final BigDecimal[] totalRewardCB = { BigDecimal.ZERO };
		final BigDecimal[] totalRedeemedCB = { BigDecimal.ZERO };
		Map<String, Object> resultMap = null;

		final List<TransactionDetail> transactionDetailList = getTransactionDetails(custOrdHead, cancelItemsList);
		if (transactionDetailList != null && !transactionDetailList.isEmpty()) {
			LOG.info("Calling Jood Transaction Detail Package");
			List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.VARCHAR));
			declaredParameters.add(new SqlParameter(Types.VARCHAR));
			declaredParameters.add(new SqlParameter(Types.TIMESTAMP));
			declaredParameters.add(new SqlParameter(Types.VARCHAR));
			declaredParameters.add(new SqlParameter(Types.VARCHAR));
			declaredParameters.add(new SqlParameter(Types.VARCHAR));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.ARRAY));
			declaredParameters.add(new SqlParameter(Types.VARCHAR));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));

			declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
			declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
			declaredParameters.add(new SqlOutParameter("joodTranId", Types.VARCHAR));
			declaredParameters.add(new SqlOutParameter("joodTranSeqNo", Types.NUMERIC));
			declaredParameters.add(new SqlOutParameter("errorMessage", Types.VARCHAR));

			final Object[][] objArr = new Object[transactionDetailList.size()][10];
			for (OrderCancelDetailRequest item : cancelItemsList) {
				for (int i = 0; i < transactionDetailList.size(); i++) {
					TransactionDetail detail = transactionDetailList.get(i);
					if (item.getItem().equals(detail.getItem()) && item.getLineNo().longValue() == detail.getLineNumber()) {
						objArr[i][0] = detail.getLineNumber();
						objArr[i][1] = detail.getItem();
						objArr[i][2] = item.getCancelQtySuom() != null ? item.getCancelQtySuom() : BigDecimal.ZERO;
						objArr[i][3] = detail.getUnitRetailPrice() != null ? detail.getUnitRetailPrice() : "0";
						objArr[i][4] = detail.getUnitTotalRetailDiscount() != null ? detail.getUnitTotalRetailDiscount() : "0";
						objArr[i][5] = detail.getUnitTotalJoodDiscount() != null ? detail.getUnitTotalJoodDiscount() : "0";
						objArr[i][6] = detail.getUnitSellingPrice() != null ? detail.getUnitSellingPrice() : "0";
						objArr[i][7] = detail.getUnitRewardsCB() != null ? detail.getUnitRewardsCB() : BigDecimal.ZERO;
						objArr[i][8] = detail.getUnitRedeemedCB() != null ? detail.getUnitRedeemedCB() : BigDecimal.ZERO;
						objArr[i][9] = detail.getRedeemptionEligible();

						totalRetailPrice[0] = totalRetailPrice[0].add(new BigDecimal(detail.getUnitRetailPrice() != null ? detail.getUnitRetailPrice() : "0")
								.multiply(item.getCancelQtySuom() != null ? item.getCancelQtySuom() : BigDecimal.ZERO));

						totalRetailDiscount[0] = totalRetailDiscount[0].add(new BigDecimal(detail.getUnitTotalRetailDiscount() != null ? detail.getUnitTotalRetailDiscount() : "0")
								.multiply(item.getCancelQtySuom() != null ? item.getCancelQtySuom() : BigDecimal.ZERO));

						totalJoodDiscount[0] = totalJoodDiscount[0].add(new BigDecimal(detail.getUnitTotalJoodDiscount() != null ? detail.getUnitTotalJoodDiscount() : "0")
								.multiply(item.getCancelQtySuom() != null ? item.getCancelQtySuom() : BigDecimal.ZERO));

						totalSellingPrice[0] = totalSellingPrice[0].add(new BigDecimal(detail.getUnitSellingPrice() != null ? detail.getUnitSellingPrice() : "0")
								.multiply(item.getCancelQtySuom() != null ? item.getCancelQtySuom() : BigDecimal.ZERO));

						totalRewardCB[0] = totalRewardCB[0].add((detail.getUnitRewardsCB() != null ? detail.getUnitRewardsCB() : BigDecimal.ZERO)
								.multiply(item.getCancelQtySuom() != null ? item.getCancelQtySuom() : BigDecimal.ZERO));

						totalRedeemedCB[0] = detail.getUnitRedeemedCB() != null ? detail.getUnitRedeemedCB() : BigDecimal.ZERO;
					}
				}
			}
			resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{ CALL XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
					stmt.setLong(1, transactionDetailList.get(0).getMemberId());
					stmt.setString(2, transactionDetailList.get(0).getSource());
					stmt.setString(3, "CANCEL");
					stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
					stmt.setString(5, custOrdHead.getCustOrderNo());
					stmt.setString(6, custOrdHead.getCustOrderNo());
					stmt.setString(7, custOrdHead.getCustOrderNo());
					stmt.setBigDecimal(8, totalRetailPrice[0]);
					stmt.setBigDecimal(9, totalRetailDiscount[0]);
					stmt.setBigDecimal(10, totalJoodDiscount[0]);
					stmt.setBigDecimal(11, totalSellingPrice[0]);
					stmt.setBigDecimal(12, totalRewardCB[0]);
					stmt.setBigDecimal(13, totalRedeemedCB[0]);
					stmt.setArray(14, ((oracle.jdbc.OracleConnection) con).createOracleArray("XX_JOOD_TRANDTL_TBL", objArr));
					stmt.setString(15, "N");
					stmt.setBigDecimal(16, transactionDetailList.get(0).getJoodProgram());

					stmt.registerOutParameter(17, Types.VARCHAR);
					stmt.registerOutParameter(18, Types.VARCHAR);
					stmt.registerOutParameter(19, Types.VARCHAR);
					stmt.registerOutParameter(20, Types.NUMERIC);
					stmt.registerOutParameter(21, Types.VARCHAR);
					return stmt;
				}
			}, declaredParameters);
			Object result = resultMap.get("status");
			if (!"Success".equalsIgnoreCase(result != null ? result.toString() : null)) {
				LOG.error("Message recevied from package for order number " + custOrdHead.getCustOrderNo() + ": " + resultMap.get("message") + " : " + resultMap.get("errorMessage") + " : "
						+ resultMap.get("joodTranSeqNo"));
				throw new BaseException(resultMap.get("message").toString());
			}
			LOG.info("Jood Transaction Detail Package Called Successfully");
		}
	}

	private List<TransactionDetail> getTransactionDetails(OmsCustOrdHead custOrdHead, List<OrderCancelDetailRequest> cancelItemsList) {
		List<TransactionDetail> transactionDetailList = null;
		List<String> items = new ArrayList<String>();
		List<BigDecimal> lineNumbers = new ArrayList<BigDecimal>();
		try {
			for (OrderCancelDetailRequest item : cancelItemsList) {
				items.add(item.getItem());
				lineNumbers.add(item.getLineNo());
			}
//				String query = "SELECT H.MEMBERSHIP_ID, H.SOURCE, I.ITEM, I.IINE_NO, I.QTY, I.UNIT_RETAIL, I.UNIT_DISCOUNT_AMOUNT, I.JOOD_DISCOUNT, "
//						+ "I.EARNED_CB, COALESCE(CASE WHEN UPPER(H.SOURCE) = 'E-COMMERCE' THEN R.REDEEMED_CB END, I.REDEEMED_CB) AS REDEEMED_CB, I.USED_CB, "
//						+ "I.TOTAL_SELLING_RETAIL FROM XX_JOOD_TRANSACTIONS_HEAD H JOIN XX_JOOD_TRANSACTIONS_DTL I ON H.TRAN_SEQ_NO = I.TRAN_SEQ_NO LEFT JOIN "
//						+ "XX_JOOD_TRANSACTIONS_HEAD R ON R.ORDER_NUMBER = H.ORDER_NUMBER AND UPPER(R.TRAN_TYPE) = 'REDEEM' WHERE H.ORDER_NUMBER = :custOrdNo "
//						+ "AND I.ITEM IN (:items) AND I.IINE_NO IN (:lineNos) AND UPPER(H.TRAN_TYPE) IN ('SALE', 'SALEANDREDEEM')";
			String query = "SELECT H.MEMBERSHIP_ID, H.JOOD_PROGRAM, (SELECT MAX(JM.MEMBERSHIP_ID) FROM XX_JOOD_MEMBERSHIP JM WHERE J.MOBILE_NO = JM.MOBILE_NO) LAT_MEMBERSHIP_ID, H.SOURCE, I.ITEM, I.IINE_NO, I.QTY, I.UNIT_RETAIL, "
					+ "I.UNIT_DISCOUNT_AMOUNT, I.JOOD_DISCOUNT, I.EARNED_CB, COALESCE(CASE WHEN UPPER(H.SOURCE) = 'E-COMMERCE' THEN R.REDEEMED_CB END, I.REDEEMED_CB) AS REDEEMED_CB, I.USED_CB, I.TOTAL_SELLING_RETAIL "
					+ "FROM XX_JOOD_TRANSACTIONS_HEAD H JOIN XX_JOOD_TRANSACTIONS_DTL I ON H.TRAN_SEQ_NO = I.TRAN_SEQ_NO LEFT JOIN XX_JOOD_TRANSACTIONS_HEAD R ON R.ORDER_NUMBER = H.ORDER_NUMBER AND UPPER(R.TRAN_TYPE) = "
					+ "'REDEEM' JOIN XX_JOOD_MEMBERSHIP J ON H.MEMBERSHIP_ID = J.MEMBERSHIP_ID WHERE H.ORDER_NUMBER = :custOrdNo AND I.ITEM IN (:items) AND I.IINE_NO IN (:lineNos) AND UPPER(H.TRAN_TYPE) IN ('SALE','SALEANDREDEEM')";
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("custOrdNo", custOrdHead.getCustOrderNo());
			param.addValue("items", items);
			param.addValue("lineNos", lineNumbers);

			transactionDetailList = jdbcTemplate.query(query, param, new RowMapper<TransactionDetail>() {
				public TransactionDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
					TransactionDetail transactionDetail = new TransactionDetail();
					transactionDetail.setMemberId(rs.getLong("MEMBERSHIP_ID"));
					transactionDetail.setItem(rs.getString("ITEM"));
					transactionDetail.setSource(rs.getString("SOURCE"));
					transactionDetail.setLineNumber(rs.getLong("IINE_NO"));
					transactionDetail.setQty(rs.getInt("QTY"));
					transactionDetail.setUnitRetailPrice(rs.getString("UNIT_RETAIL"));
					transactionDetail.setUnitTotalRetailDiscount(rs.getString("UNIT_DISCOUNT_AMOUNT"));
					transactionDetail.setUnitTotalJoodDiscount(rs.getString("JOOD_DISCOUNT"));
					transactionDetail.setUnitSellingPrice(rs.getString("TOTAL_SELLING_RETAIL"));
					transactionDetail.setUnitRewardsCB(rs.getBigDecimal("EARNED_CB"));
					transactionDetail.setUnitRedeemedCB(rs.getBigDecimal("REDEEMED_CB"));
					transactionDetail.setJoodProgram(rs.getBigDecimal("JOOD_PROGRAM"));
					return transactionDetail;
				}
			});

		} catch (Exception e) {
			LOG.error("Failed getting details from jood table " + custOrdHead.getCustOrderNo(), e);
		}
		return transactionDetailList;
	}
}
