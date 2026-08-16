package com.extra.oms.custOrder.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extra.bds.bean.carrera.TransferCreationRequest;
import com.extra.common.exception.BaseException;
import com.extra.common.model.OmsBackOrderDtl;
import com.extra.common.model.OmsCoFulfillDetail;
import com.extra.common.model.OmsCustAddressInfo;
import com.extra.common.model.OmsCustOrdHead;
import com.extra.common.model.OmsCustOrdItem;
import com.extra.common.model.OmsCustOrdReserve;
import com.extra.common.model.OmsUnapprovedTransfer;
import com.extra.common.model.TransferLineItem;
import com.extra.common.model.Wh;
import com.extra.oms.bean.ws.WhAdjModVo;

/**
 * @author aibrahim
 *
 */
@Repository
public class BackOrderDAO extends BaseDAO {

	private static final Logger LOG = Logger.getLogger(BackOrderDAO.class);

	private static final String INSERT_LOCK_SQL = "INSERT INTO OMS_BACK_ORDER_BATCH_CHECK(BATCH_ID, BATCH_START_TIME, STATUS) VALUES(BACK_ORDER_BATCH_SEQ.NEXTVAL, SYSTIMESTAMP, 'IN_PROGRESS')";

	private static final String SELECT_BACK_ORDER_SQL = "SELECT B.OMS_CUST_ORD_NO, B.ITEM, B.LINE_NO, B.FULFILL_LOC_TYPE, B.FULFILL_LOC, B.FULFILL_QTY, B.SOURCE_LOC_TYPE, "
			+ "B.SOURCE_LOC, B.SOURCE_QTY, B.BACKORDER_STATUS, H.CUST_ORDER_NO, H.ORD_PAYMENT_STATUS, H.ORDER_CREATE_RESERVE_IND, I.SHIP_CLASSIFICATION, "
			+ "I.SUBSTITUTE_ALLOW_IND, I.STANDARD_UOM, I.TRANSACTION_UOM, I.UNIT_RETAIL, I.RETAIL_CURR, I.COMMENTS, A.DELIVER_FIRST_NAME, A.DELIVER_LAST_NAME, "
			+ "A.DELIVER_ADD_1, A.DELIVER_ADD_2, A.DELIVER_ADD_3, A.DELIVER_CITY, A.DELIVER_STATE, A.DELIVER_COUNTRY, A.DELIVER_POST, A.DELIVER_PHONE_NO, "
			+ "A.BILL_FIRST_NAME, A.BILL_LAST_NAME, A.BILL_ADD_1, A.BILL_ADD_2, A.BILL_ADD_3, A.BILL_CITY, A.BILL_STATE, A.BILL_COUNTRY, A.BILL_POST, H.CONSUMER_DLY_TIME, H.APPLICATION_ID, H.DELIVERY_TYPE, B.COMBINATION_ID, "
			+ "W.WH, W.PHYSICAL_WH, W.CHANNEL_ID FROM OMS_BACK_ORDER_DTL B LEFT OUTER JOIN WH W ON W.WH = B.SOURCE_LOC AND B.SOURCE_LOC_TYPE = 'WH', "
			+ "OMS_CUST_ORD_HEAD H, OMS_CUST_ORD_ITEM I, OMS_CUST_ORD_ADDRESS A WHERE B.BACKORDER_STATUS = 'N' AND B.BAT_PK_STATUS = 'N' AND B.OMS_CUST_ORD_NO = H.OMS_CUST_ORD_NO "
			+ "AND H.STATUS = 'S' AND (B.SOURCE_QTY - B.FULFILL_QTY) > 0 AND I.OMS_CUST_ORD_NO = B.OMS_CUST_ORD_NO AND B.LINE_NO = I.LINE_NO "
			+ "AND B.FULFILL_QTY < B.SOURCE_QTY AND A.OMS_CUST_ORD_NO = B.OMS_CUST_ORD_NO ORDER BY B.OMS_CUST_ORD_NO ";

	private static final String SELECT_STOCK_AVAILABLITY_SQL = "SELECT DISTINCT AVAIL_QTY AVAIL_QTY, V.ITEM, B.SOURCE_LOC_TYPE, V.LOC LOC FROM XX_OMS_INVAVAIL_V V, OMS_BACK_ORDER_DTL B "
			+ "WHERE B.BACKORDER_STATUS = 'N' AND V.ITEM = B.ITEM AND V.LOC = B.SOURCE_LOC AND V.LOC_TYPE = 'W' AND B.SOURCE_LOC_TYPE = 'WH' AND (SOURCE_QTY - FULFILL_QTY) > 0 "
			+ " UNION "
			+ "SELECT V.AVAIL_TO_SELL AVAIL_QTY, V.ITEM_ID ITEM, B.SOURCE_LOC_TYPE, V.STORE_ID LOC FROM XX_OMS_INV@SIMDB V, "
			+ "OMS_BACK_ORDER_DTL B WHERE B.BACKORDER_STATUS = 'N' AND V.ITEM_ID = B.ITEM AND V.STORE_ID = B.SOURCE_LOC AND V.SOURCE = 'E-COMMERCE' AND B.SOURCE_LOC_TYPE = 'ST' "
			+ "AND (SOURCE_QTY - FULFILL_QTY) > 0 AND V.AVAIL_TO_SELL > 0";

	private static final String SELECT_FILFILORD_NO_SQL = "SELECT MAX(TO_NUMBER(FULFILL_ORDER_NO)), CUSTOMER_ORDER_NO FROM ORDCUST WHERE CUSTOMER_ORDER_NO IN "
			+ "(SELECT H.CUST_ORDER_NO FROM OMS_CUST_ORD_HEAD H, OMS_BACK_ORDER_DTL B WHERE B.OMS_CUST_ORD_NO = H.OMS_CUST_ORD_NO AND B.BACKORDER_STATUS = 'N' AND H.STATUS = 'S' AND B.FULFILL_QTY < B.SOURCE_QTY ) "
			+ "GROUP BY CUSTOMER_ORDER_NO";

	private static final String SELECT_TRANSFER_DETAIL_SQL = "SELECT T.ID, L.ID, C.CUSTOMER_ORDER_NO, C.FULFILL_ORDER_NO, C.TSF_NO, CD.ITEM FROM ORDCUST C "
			+ "LEFT OUTER JOIN TRANSFER@SIMDB T ON C.TSF_NO = T.EXTERNAL_ID LEFT OUTER JOIN TRANSFER_LINE_ITEM@SIMDB L ON T.ID = L.TRANSFER_ID, ORDCUST_DETAIL CD "
			+ "WHERE CD.ORDCUST_NO = C.ORDCUST_NO AND C.CUSTOMER_ORDER_NO = :ordNo AND C.FULFILL_ORDER_NO IN (:fulfilNos) ";

	@Transactional(rollbackFor = BaseException.class)
	public Long aquireLock() throws BaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.getJdbcOperations().execute("LOCK TABLE OMS_BACK_ORDER_BATCH_CHECK IN EXCLUSIVE MODE WAIT 5");
		Integer processRunning = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM OMS_BACK_ORDER_BATCH_CHECK WHERE STATUS = 'IN_PROGRESS'", (SqlParameterSource) null, Integer.class);
		if (processRunning > 0) {			
			throw new BaseException("Back Orders are already in process");
		} else {
			int rw = jdbcTemplate.update(INSERT_LOCK_SQL, null, keyHolder, new String[] {"BATCH_ID"});
			if (rw == 0) {
				throw new BaseException("Back Orders are already in process");
			}
			return keyHolder.getKey().longValue();
		}
	}
 
	public void releaseLock(Long seqId, String status) {
		Map<String, Object> params = new HashMap<>();
		params.put("status", status);
		params.put("batchSeqId", seqId);
		jdbcTemplate.update("UPDATE OMS_BACK_ORDER_BATCH_CHECK SET STATUS = :status, BATCH_END_TIME = SYSTIMESTAMP WHERE BATCH_ID = :batchSeqId", params);
	}

	public List<OmsCustOrdHead> getBOOrders(final HashMap<String, BigDecimal> stkMap) {
		return jdbcTemplate.query(SELECT_BACK_ORDER_SQL, new ResultSetExtractor<List<OmsCustOrdHead>>() {

			@Override
			public List<OmsCustOrdHead> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, OmsCustOrdHead> backOrders = new TreeMap<>();
				while (rs.next()) {
					if (stkMap.isEmpty()) {
						break;
					}

					String ordNumber = rs.getString("CUST_ORDER_NO");
					OmsCustOrdHead custOrdHead = backOrders.get(ordNumber);
					BigDecimal srcQty = rs.getBigDecimal(9);
					BigDecimal fulfilQty = rs.getBigDecimal(6);
					BigDecimal pendingQty = srcQty.subtract(fulfilQty);
					String item = rs.getString(2);
					BigDecimal source = rs.getBigDecimal(8);
					String sourceType = rs.getString(7);
					String itemLOCKey = item + "-" + sourceType + "-" + source.toPlainString();

					BigDecimal totalSOH = stkMap.get(itemLOCKey);
					if (totalSOH != null) {
						pendingQty = totalSOH.min(pendingQty);
						if (pendingQty.intValue() > 0) {
							stkMap.put(itemLOCKey, totalSOH.subtract(pendingQty));
						} else {
							stkMap.remove(itemLOCKey);
							continue;
						}
					} else {
						continue;
					}
					if (custOrdHead == null) {
						custOrdHead = new OmsCustOrdHead();
						custOrdHead.setOmsCustOrdNo(rs.getBigDecimal(1));
						custOrdHead.setCustOrderNo(rs.getString(11));
						custOrdHead.setOrdPaymentStatus(rs.getString(12));
						custOrdHead.setOrderCreateReserveInd(rs.getString(13));
						custOrdHead.setConsumerDlyTime(rs.getTimestamp("CONSUMER_DLY_TIME"));
						custOrdHead.setApplicationId(rs.getString("APPLICATION_ID"));
						custOrdHead.setDeliveryType(rs.getString("DELIVERY_TYPE"));
						custOrdHead.setBackOrderDetails(new LinkedList<OmsBackOrderDtl>());

						OmsCustAddressInfo addressInfo = new OmsCustAddressInfo();
						addressInfo.setBillAdd1(rs.getString("BILL_ADD_1"));
						addressInfo.setBillAdd2(rs.getString("BILL_ADD_2"));
						addressInfo.setBillAdd3(rs.getString("BILL_ADD_3"));
						addressInfo.setBillCity(rs.getString("BILL_CITY"));
						addressInfo.setBillState(rs.getString("BILL_STATE"));
						addressInfo.setBillCounty(rs.getString("BILL_COUNTRY"));
						addressInfo.setBillFirstName(rs.getString("BILL_FIRST_NAME"));
						addressInfo.setBillLastName(rs.getString("BILL_LAST_NAME"));
						addressInfo.setBillPost(rs.getString("BILL_POST"));
						addressInfo.setDeliverAdd1(rs.getString("DELIVER_ADD_1"));
						addressInfo.setDeliverAdd2(rs.getString("DELIVER_ADD_2"));
						addressInfo.setDeliverAdd3(rs.getString("DELIVER_ADD_3"));
						addressInfo.setDeliverCity(rs.getString("DELIVER_CITY"));
						addressInfo.setDeliverState(rs.getString("DELIVER_STATE"));
						addressInfo.setDeliverCountry(rs.getString("DELIVER_COUNTRY"));
						addressInfo.setDeliverFirstName(rs.getString("DELIVER_FIRST_NAME"));
						addressInfo.setDeliverLastName(rs.getString("DELIVER_LAST_NAME"));
						addressInfo.setDeliverPost(rs.getString("DELIVER_POST"));
						addressInfo.setDeliverPhone(rs.getString("DELIVER_PHONE_NO"));
						custOrdHead.setCustAddressInfo(addressInfo);
						backOrders.put(ordNumber, custOrdHead);
					}
					OmsBackOrderDtl backOrderDtl = new OmsBackOrderDtl();
					backOrderDtl.setItem(item);
					backOrderDtl.setOmsCustOrdNo(custOrdHead.getOmsCustOrdNo());
					backOrderDtl.setLineNo(rs.getBigDecimal(3));
					backOrderDtl.setFulfillLocType(rs.getString(4));
					backOrderDtl.setFulfillLoc(rs.getBigDecimal(5));
					backOrderDtl.setFulfillQty(fulfilQty);
					backOrderDtl.setSourceLocType(sourceType);
					backOrderDtl.setSourceLoc(source);
					backOrderDtl.setSourceQty(srcQty);
					backOrderDtl.setBackorderStatus(rs.getString(10));
					backOrderDtl.setCombinationId(rs.getBigDecimal("COMBINATION_ID"));

					if ("WH".equals(backOrderDtl.getSourceLocType())) {
						Wh wh = new Wh();
						wh.setChannelId(rs.getInt("CHANNEL_ID"));
						wh.setId(rs.getBigDecimal("WH"));
						wh.setPhysicalWh(rs.getBigDecimal("PHYSICAL_WH"));
						backOrderDtl.setWh(wh);
					}

					OmsCustOrdItem custOrdItem = new OmsCustOrdItem();
					custOrdItem.setItem(backOrderDtl.getItem());
					custOrdItem.setShipClassification(rs.getString(14));
					custOrdItem.setSubstituteAllowInd(rs.getString(15));
					custOrdItem.setStandardUom(rs.getString(16));
					custOrdItem.setTransactionUom(rs.getString(17));
					custOrdItem.setUnitRetail(rs.getBigDecimal(18));
					custOrdItem.setRetailCurr(rs.getString(19));
					custOrdItem.setComments(rs.getString(20));
					backOrderDtl.setOmsCustOrdItem(custOrdItem);
					backOrderDtl.setOmsCustOrdHead(custOrdHead);
					custOrdHead.getBackOrderDetails().add(backOrderDtl);
				}
				return new ArrayList<>(backOrders.values());
			}
		});
	}

	public Map<String, BigDecimal> getStockAvailablity() {
		return jdbcTemplate.query(SELECT_STOCK_AVAILABLITY_SQL, new ResultSetExtractor<Map<String, BigDecimal>>() {

			@Override
			public Map<String, BigDecimal> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, BigDecimal> stkAvlbtyMap = new HashMap<>();
				while (rs.next()) {
					BigDecimal stk = rs.getBigDecimal(1);
					if (stk.intValue() > 0) {
						String key = (rs.getString(2) + "-" + rs.getString(3) + "-" + rs.getString(4)).intern();
						stkAvlbtyMap.put(key, stk);
					}
				}
				return stkAvlbtyMap;
			}
		});
	}

	public Map<String, OmsCustOrdReserve> getReserveOrderItems(BigDecimal omsCustOrdNo) {
		return jdbcTemplate.query("SELECT * FROM OMS_CUST_ORD_RESERVE WHERE OMS_CUST_ORD_NO = :omsCustOrdNo", Collections.singletonMap("omsCustOrdNo", omsCustOrdNo), new ResultSetExtractor<Map<String, OmsCustOrdReserve>>() {

			@Override
			public Map<String, OmsCustOrdReserve> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, OmsCustOrdReserve> ordReserveMap = new HashMap<>();
				while (rs.next()) {
					OmsCustOrdReserve custOrdReserve = new OmsCustOrdReserve();
					custOrdReserve.setCombinationId(rs.getBigDecimal("COMBINATION_ID"));
					custOrdReserve.setFulfillOrderNo(rs.getBigDecimal("FULFILL_ORDER_NO"));
					custOrdReserve.setItem(rs.getString("ITEM"));
					custOrdReserve.setLineNo(rs.getBigDecimal("LINE_NO"));
					custOrdReserve.setLoc(rs.getBigDecimal("LOC"));
					custOrdReserve.setLocType(rs.getString("LOC_TYPE"));
					custOrdReserve.setOmsCustOrdNo(rs.getBigDecimal("OMS_CUST_ORD_NO"));
					custOrdReserve.setQty(rs.getBigDecimal("QTY"));
					custOrdReserve.setResvStatus(rs.getString("RESV_STATUS"));
					custOrdReserve.setRmsResvLoc(rs.getBigDecimal("RMS_RESV_LOC"));
					custOrdReserve.setRmsResvLocType(rs.getString("RMS_RESV_LOC_TYPE"));
					custOrdReserve.setRmsResvQty(rs.getBigDecimal("RMS_RESV_QTY"));
					custOrdReserve.setVirtualWH(rs.getBigDecimal("VIRTUAL_WH"));
					String key = custOrdReserve.getLineNo() + "~" + custOrdReserve.getRmsResvLoc();
					ordReserveMap.put(key, custOrdReserve);
				}
				return ordReserveMap;
			}
		});
	}


	public Map<String, BigDecimal> getMaxFulfilOrderNo() {
		return jdbcTemplate.query(SELECT_FILFILORD_NO_SQL, new ResultSetExtractor<Map<String, BigDecimal>>() {

			@Override
			public Map<String, BigDecimal> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, BigDecimal> maxFulfilOrdMap = new HashMap<>();
				while (rs.next()) {
					maxFulfilOrdMap.put(rs.getString(2), rs.getBigDecimal(1));
				}
				return maxFulfilOrdMap;
			}
		});
	}

	public Map<String, String> getSystemParams() {
		return jdbcTemplate.query("SELECT PARAMETER_VALUE, PARAMETER_NAME FROM OMS_SYSTEM_PARAMETERS WHERE PARAMETER_ID IN ('OMS_SYSTEM_OPTION', 'OMS_INV_ADJ_RSN_CODE') AND PARAMETER_NAME IN ('RESV_INV_STATUS', 'REASON_CODE', 'OMS_PARTIAL_DLV_IND', 'INV_RESV_CODE', 'INV_UNRESV_CODE')",
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

	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBackOrderDetails(Map<String, Object> updates, String custOrdNo, BigDecimal omsCustOrdNo) {
		Map<String, OmsBackOrderDtl> upBackOrderDtls = (Map<String, OmsBackOrderDtl>) updates.get("BACK_ORDS");
		List<OmsCustOrdItem> custOrsItems = (List<OmsCustOrdItem>) updates.get("OMS_ITEMS");
		Map<String, OmsCoFulfillDetail> coFulfils = (Map<String, OmsCoFulfillDetail>) updates.get("CO_FULFILS");
		List<OmsCustOrdReserve> newCustOrsReserves = (List<OmsCustOrdReserve>) updates.get("NEW_RESERVES");
		List<OmsCustOrdReserve> exCustOrsReserves = (List<OmsCustOrdReserve>) updates.get("EX_RESERVES");
		List<OmsUnapprovedTransfer> unapprovedTransfers = (List<OmsUnapprovedTransfer>) updates.get("UN_APPD_TRFS");
		List<String> whStTrfsFulfils = (List<String>) updates.get("WH_ST_TRANSERS");

		List<SqlParameterSource> paramSource = new ArrayList<SqlParameterSource>();

		for (OmsBackOrderDtl backOrderDtl : upBackOrderDtls.values()) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("status", backOrderDtl.getBackorderStatus());
			param.addValue("qty", backOrderDtl.getFulfillQty());
			param.addValue("omsCustOrdNo", backOrderDtl.getOmsCustOrdNo());
			param.addValue("item", backOrderDtl.getItem());
			param.addValue("lineNo", backOrderDtl.getLineNo());
			param.addValue("sourceLoc", backOrderDtl.getSourceLoc());
			paramSource.add(param);
		}
		jdbcTemplate.batchUpdate(
				"UPDATE OMS_BACK_ORDER_DTL SET BACKORDER_STATUS = :status, FULFILL_QTY = :qty, CONF_TS = SYSTIMESTAMP WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND ITEM = :item AND LINE_NO = :lineNo AND SOURCE_LOC = :sourceLoc ",
				paramSource.toArray(new SqlParameterSource[paramSource.size()]));

		if (exCustOrsReserves != null && !exCustOrsReserves.isEmpty()) {
			paramSource.clear();
			for (OmsCustOrdReserve custOrdReserve : exCustOrsReserves) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("rmsResvQty", custOrdReserve.getRmsResvQty());
				param.addValue("qty", custOrdReserve.getQty());
				param.addValue("omsCustOrdNo", custOrdReserve.getOmsCustOrdNo());
				param.addValue("item", custOrdReserve.getItem());
				param.addValue("lineNo", custOrdReserve.getLineNo());
				param.addValue("resvLoc", custOrdReserve.getRmsResvLoc());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"UPDATE OMS_CUST_ORD_RESERVE SET QTY = :qty, RMS_RESV_QTY = :rmsResvQty WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND ITEM = :item AND LINE_NO = :lineNo AND RMS_RESV_LOC = :resvLoc",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
		}

		if (newCustOrsReserves != null && !newCustOrsReserves.isEmpty()) {
			paramSource.clear();
			for (OmsCustOrdReserve custOrdReserve : newCustOrsReserves) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", custOrdReserve.getOmsCustOrdNo());
				param.addValue("fulfilOrdNo", custOrdReserve.getFulfillOrderNo());
				param.addValue("item", custOrdReserve.getItem());
				param.addValue("lineNo", custOrdReserve.getLineNo());
				param.addValue("locType", custOrdReserve.getLocType());
				param.addValue("loc", custOrdReserve.getLoc());
				param.addValue("qty", custOrdReserve.getQty());
				param.addValue("rmsResvLocType", custOrdReserve.getRmsResvLocType());
				param.addValue("rmsResvLoc", custOrdReserve.getRmsResvLoc());
				param.addValue("rmsResvQty", custOrdReserve.getRmsResvQty());
				param.addValue("payStatus", custOrdReserve.getPaymentStatus());
				param.addValue("resvStatus", custOrdReserve.getResvStatus());
				param.addValue("combId", custOrdReserve.getCombinationId());
				param.addValue("virtualWH", custOrdReserve.getVirtualWH());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"INSERT INTO OMS_CUST_ORD_RESERVE(OMS_CUST_ORD_NO, FULFILL_ORDER_NO, ITEM, LINE_NO, LOC_TYPE, LOC, QTY, RMS_RESV_LOC_TYPE, RMS_RESV_LOC, RMS_RESV_QTY, "
					+ "PAYMENT_STATUS, RESV_STATUS, INITIATE_TS, COMBINATION_ID, CREATED_BY, CREATE_DATETIME, VIRTUAL_WH) VALUES(:omsCustOrdNo, :fulfilOrdNo, :item, :lineNo, "
					+ " :locType, :loc, :qty, :rmsResvLocType, :rmsResvLoc, :rmsResvQty, :payStatus, :resvStatus, SYSTIMESTAMP, :combId, 'User', SYSDATE, :virtualWH)",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
		}

		if (coFulfils != null && !coFulfils.isEmpty()) {
			paramSource.clear();
			for (OmsCoFulfillDetail coFulfillDetail : coFulfils.values()) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", coFulfillDetail.getOmsCustOrdNo());
				param.addValue("fulfilOrdNo", coFulfillDetail.getFulfillOrderNo());
				param.addValue("item", coFulfillDetail.getItem());
				param.addValue("lineNo", coFulfillDetail.getLineNo());
				param.addValue("reqQty", coFulfillDetail.getFulfillReqQty());
				param.addValue("confQty", coFulfillDetail.getFulfillConfQty());
				param.addValue("delvQty", coFulfillDetail.getFulfillDeliverQty());
				param.addValue("canQty", coFulfillDetail.getFulfillCancelQty());
				param.addValue("fulfilLoc", coFulfillDetail.getFulfillLoc());
				param.addValue("fulfilLocType", coFulfillDetail.getFulfillLocType());
				param.addValue("srcLoc", coFulfillDetail.getSourceLoc());
				param.addValue("srcLocType", coFulfillDetail.getSourceLocType());
				param.addValue("combId", coFulfillDetail.getCombinationId());
				param.addValue("fulfilSts", coFulfillDetail.getFulfillStatus());
				param.addValue("orgItem", coFulfillDetail.getOrigItem());
				param.addValue("tsfApvStatus", coFulfillDetail.getTsfApprovalStatus());
				param.addValue("tsfNo", coFulfillDetail.getTsfNo());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"INSERT INTO OMS_CO_FULFILL_DETAIL(OMS_CUST_ORD_NO, FULFILL_ORDER_NO, ITEM, LINE_NO, FULFILL_REQ_QTY, FULFILL_CONF_QTY, FULFILL_DELIVER_QTY, FULFILL_CANCEL_QTY, FULFILL_LOC, FULFILL_LOC_TYPE, SOURCE_LOC, "
					+ "SOURCE_LOC_TYPE, COMBINATION_ID, FULFILL_STATUS, ORIG_ITEM, TSF_APPROVAL_STATUS, TSF_NO, CREATE_DATETIME) VALUES(:omsCustOrdNo, :fulfilOrdNo, :item, :lineNo, "
					+ " :reqQty, :confQty, :delvQty, :canQty, :fulfilLoc, :fulfilLocType, :srcLoc, :srcLocType, :combId, :fulfilSts, :orgItem, :tsfApvStatus, :tsfNo, SYSDATE)",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
		}

		if (custOrsItems != null && !custOrsItems.isEmpty()) {
			paramSource.clear();
			for (OmsCustOrdItem custOrdItem : custOrsItems) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("status", custOrdItem.getStatus());
				param.addValue("omsCustOrdNo", custOrdItem.getOmsCustOrdNo());
				param.addValue("item", custOrdItem.getItem());
				param.addValue("lineNo", custOrdItem.getLineNo());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"UPDATE OMS_CUST_ORD_ITEM SET STATUS = :status WHERE OMS_CUST_ORD_NO = :omsCustOrdNo AND ITEM = :item AND LINE_NO = :lineNo ",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
		}

		if (unapprovedTransfers != null && !unapprovedTransfers.isEmpty()) {
			paramSource.clear();
			for (OmsUnapprovedTransfer unapprovedTransfer : unapprovedTransfers) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("omsCustOrdNo", unapprovedTransfer.getOmsCustOrdNo());
				param.addValue("item", unapprovedTransfer.getItem());
				param.addValue("tsfNo", unapprovedTransfer.getTsfNo());
				param.addValue("qty", unapprovedTransfer.getUnapprovedQty());
				param.addValue("tsfNo", unapprovedTransfer.getTsfNo());
				param.addValue("location", unapprovedTransfer.getLocation());
				paramSource.add(param);
			}
			jdbcTemplate.batchUpdate(
					"INSERT INTO OMS_UNAPPROVED_TRANSFERS(OMS_CUST_ORD_NO, TSF_NO, UNAPPROVED_QTY, CREATE_DATETIME, ITEM, LOCATION) VALUES(:omsCustOrdNo, :tsfNo, :qty, SYSDATE, :item, :location)",
					paramSource.toArray(new SqlParameterSource[paramSource.size()]));
		}

		if (whStTrfsFulfils != null) {
			Map<String, Object> params = new HashMap<>();
			params.put("ordNo", custOrdNo);
			params.put("omsOrdNo", omsCustOrdNo);
			params.put("fulfilIds", whStTrfsFulfils);
			jdbcTemplate.update("UPDATE OMS_CO_FULFILL_DETAIL C SET C.TSF_NO = (SELECT O.TSF_NO FROM ORDCUST O WHERE CUSTOMER_ORDER_NO = :ordNo AND O.FULFILL_ORDER_NO = C.FULFILL_ORDER_NO) "
					+ "WHERE OMS_CUST_ORD_NO = :omsOrdNo AND FULFILL_ORDER_NO IN (:fulfilIds)", params);
		}
	}

	public void callWHAdjustment(String orderNumber, final WhAdjModVo whAdjModVo, final String invStatus, final String reasonCode) throws BaseException {
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
		final String userId = orderNumber + "_CL";
		resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ CALL OMS_INVADJ_STATUS_UNAVIALINV(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
				stmt.setString(1, whAdjModVo.getItem());
				stmt.setInt(2, Integer.parseInt(invStatus));
				stmt.setString(3, whAdjModVo.getLocationType());
				stmt.setBigDecimal(4, whAdjModVo.getLocation());
				stmt.setBigDecimal(5, whAdjModVo.getQty());
				stmt.setInt(6, Integer.parseInt(reasonCode));
				stmt.setString(7, userId);
				stmt.registerOutParameter(8, Types.NUMERIC);
				stmt.registerOutParameter(9, Types.VARCHAR);
				return stmt;
			}
		}, declaredParameters);
		Integer status = ((BigDecimal) resultMap.get("status")).intValue();
		LOG.info("Response received from the adjustment procedure call for order number " + orderNumber + " and item " + whAdjModVo.getItem() + " is " + status);
		if (status != 1) {
			String message = resultMap.get("message").toString();
			LOG.error("Business Error: " + message);
			throw new BaseException(message);
		}
	}

	public Map<String, TransferLineItem> getTransferDetails(String custOrderNo, List<String> stTrfs) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("ordNo", custOrderNo);
		param.addValue("fulfilNos", stTrfs);
		return jdbcTemplate.query(SELECT_TRANSFER_DETAIL_SQL, param, new ResultSetExtractor<Map<String, TransferLineItem>>() {

			@Override
			public Map<String, TransferLineItem> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, TransferLineItem> trfsDtlMap = new HashMap<>();
				while (rs.next()) {
					TransferLineItem item = new TransferLineItem();
					item.setFulfilOrdNo(rs.getString(4));
					item.setId(rs.getLong(1));
					item.setLineId(rs.getLong(2));
					item.setItem(rs.getString(6));
					item.setTransferNo(rs.getLong(5));
					trfsDtlMap.put(item.getFulfilOrdNo(), item);
				}
				return trfsDtlMap;
			}
		});
	}

	public void rbCarreraTransfers(final Map<String, TransferCreationRequest> tsfNoMap) {
		for (final String tsfNo : tsfNoMap.keySet()) {
			String message = jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?) }");
					stmt.registerOutParameter(1, Types.VARCHAR);
					stmt.registerOutParameter(2, Types.VARCHAR);
					stmt.setBigDecimal(3, new BigDecimal(tsfNo));
					stmt.setString(4, tsfNoMap.get(tsfNo).getCustomerItems().get(0).getItem());
					stmt.setBigDecimal(5, BigDecimal.valueOf(tsfNoMap.get(tsfNo).getCustomerItems().get(0).getQty()));
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
			if (!"Success".equalsIgnoreCase(message)) {
				LOG.warn("Rollback failed for the carera fulfilment " + tsfNo + " is " + message);
			}
		}
	}

	public void rbWHAdjustment(String orderNumber, List<WhAdjModVo> whAdjModVos, final String invStatus, final String reasonCode) {
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
		final String userId = orderNumber + "_CL";
		for (final WhAdjModVo whAdjModVo : whAdjModVos) {
			resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{ CALL OMS_INVADJ_STATUS_UNAVIALINV(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
					stmt.setString(1, whAdjModVo.getItem());
					stmt.setInt(2, Integer.parseInt(invStatus));
					stmt.setString(3, whAdjModVo.getLocationType());
					stmt.setBigDecimal(4, whAdjModVo.getLocation());
					stmt.setBigDecimal(5, whAdjModVo.getQty().negate());
					stmt.setInt(6, Integer.parseInt(reasonCode));
					stmt.setString(7, userId);
					stmt.registerOutParameter(8, Types.NUMERIC);
					stmt.registerOutParameter(9, Types.VARCHAR);
					return stmt;
				}
			}, declaredParameters);
			Integer status = ((BigDecimal) resultMap.get("status")).intValue();
			if (status != 1) {
				String message = resultMap.get("message").toString();
				LOG.warn("Rollback failed for the WH adjustment for order number: " + orderNumber + " and message: " + message);
			}
		}
	}
}
