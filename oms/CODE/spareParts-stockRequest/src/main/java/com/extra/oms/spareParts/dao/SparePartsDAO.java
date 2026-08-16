package com.extra.oms.spareParts.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extra.oms.spareParts.model.Items;
import com.extra.oms.spareParts.model.StockDeductionRequest;
import com.extra.oms.spareParts.model.StockRecDetails;
import com.extra.oms.spareParts.model.StockRequest;
import com.extra.oms.spareParts.model.CancelRequest;
import com.extra.oms.spareParts.model.TransferRecieveRequest;
import com.extra.oms.spareParts.util.UtillConstantCodes;

@Repository
public class SparePartsDAO extends BaseDAO {

	private static final Logger log = LogManager.getLogger(SparePartsDAO.class);

	@Transactional
	public String persistStockRequest(StockRequest request, Map<String, BigDecimal> itemStockQty, String message) {
		log.info("Inserting data into xx_sp_stock_req_ret table");
		try {
			if (!message.equals(UtillConstantCodes.NoStockAvail)) {
				String selectQuery = "SELECT COUNT(*) FROM XX_SP_STOCK_REQ_RET WHERE TSF_REQ_SEQ_ID = :tsf_req_seq_id and TYPE =:type";

				MapSqlParameterSource selectParamSource = new MapSqlParameterSource();
				selectParamSource.addValue("tsf_req_seq_id", request.getTsfReqSeqId());
				selectParamSource.addValue("type", request.getType());
				int count = jdbcTemplate.queryForObject(selectQuery, selectParamSource, Integer.class);

				if (count == 0) {
					String query = "INSERT INTO xx_sp_stock_req_ret(TSF_REQ_SEQ_ID, SRV_REQ_ID, SRV_AGING, SRV_LINE, REQ_LOC, REQ_TECH_ID, TYPE, CREATION_TIME, LAST_UPDATED_TIME, STATUS, COMMENTS, SP_SEQ_NO)"
							+ "VALUES(:tsf_req_seq_id, :srv_req_id, :srv_aging, :srv_line, :req_loc, :req_tech_id, :type, SYSDATE, SYSDATE, :STATUS, :COMMENTS, xx_sp_req_seq_no.nextval)";

					MapSqlParameterSource paramSource = new MapSqlParameterSource();
					paramSource.addValue("tsf_req_seq_id", request.getTsfReqSeqId());
					paramSource.addValue("srv_req_id", request.getSrvReqId());
					paramSource.addValue("srv_aging", request.getSrvAging());
					paramSource.addValue("srv_line", request.getSrvLine());
					paramSource.addValue("req_loc", request.getReqLoc());
					paramSource.addValue("req_tech_id", request.getReqTechId());
					paramSource.addValue("type", request.getType());
					paramSource.addValue("STATUS", "N");
					paramSource.addValue("COMMENTS", null);
					jdbcTemplate.update(query, paramSource);
					insertItems(request, itemStockQty);
				}
			} else {
				message = "No Stock Available - Return not created";
			}
		} catch (Exception e) {
			log.error("Failed inserting into head table", e);
			throw e;
		}
		return message;
	}

	public void insertItems(StockRequest request, Map<String, BigDecimal> itemStockQty) {
		log.info("Inserting data into xx_sp_stock_req_ret_item table");

		String itemQuery = "INSERT INTO xx_sp_stock_req_ret_item (TSF_REQ_SEQ_ID, SRV_REQ_ID, LINE_ID, ITEM, QTY, TYPE, AVAILABLE_QTY, ALLOCATED_QTY, BACK_ORDER_QTY) "
				+ "VALUES (:tsf_req_seq_id, :srv_req_id, :line_id, :item, :qty, :type, :availQty, :allocatedQty, :backOrderQty)";
		try {
			List<MapSqlParameterSource> batchParams = new ArrayList<>();
			for (Items item : request.getItems()) {
				MapSqlParameterSource itemParamSource = new MapSqlParameterSource();
				itemParamSource.addValue("tsf_req_seq_id", request.getTsfReqSeqId());
				itemParamSource.addValue("srv_req_id", request.getSrvReqId());
				itemParamSource.addValue("line_id", item.getLineId());
				itemParamSource.addValue("item", item.getItem());
				itemParamSource.addValue("qty", item.getQty());
				itemParamSource.addValue("type", request.getType());
				itemParamSource.addValue("availQty", itemStockQty.get(item.getItem()));
				itemParamSource.addValue("allocatedQty", BigDecimal.ZERO);
				itemParamSource.addValue("backOrderQty", BigDecimal.ZERO);
				batchParams.add(itemParamSource);
			}
			jdbcTemplate.batchUpdate(itemQuery, batchParams.toArray(new MapSqlParameterSource[0]));
		} catch (Exception e) {
			log.error("Failed inserting into item table", e);
			throw e;
		}
	}

	public void updateStatus(StockRequest request) {
		String query = "update xx_sp_stock_req_ret set status = 'F', LAST_UPDATED_TIME = sysdate where TSF_REQ_SEQ_ID =:seqId and TYPE = :type";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", request.getTsfReqSeqId());
		paramSource.addValue("type", request.getType());

		try {
			jdbcTemplate.update(query, paramSource);
		} catch (Exception e) {
			log.error("Failed to update status in xx_sp_stock_req_ret ", e);
			throw e;
		}
	}

	public void updateDeductStatus(String status, String seqId) {
		String query = "update xx_sp_stock_req_ret set status = :status, LAST_UPDATED_TIME = sysdate where TSF_REQ_SEQ_ID = :seqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);
		paramSource.addValue("status", status);
		try {
			jdbcTemplate.update(query, paramSource);
		} catch (Exception e) {
			log.error("Failed to update status in xx_sp_stock_req_ret ", e);
			throw e;
		}
	}

	public Map<String, BigDecimal> stockCheckForRequest(StockRequest request, Map<String, BigDecimal> itemMap) {
		log.info("Checking stock for STOCK REQUEST");
		Map<String, BigDecimal> resultMap = new HashMap<>();
		for (Items items : request.getItems()) {
			String item = items.getItem();
			BigDecimal qty = items.getQty();
			itemMap.put(item, qty);
		}
		List<String> itemList = new ArrayList<>(itemMap.keySet());

		String query = "select ITEM, LOC, SUM(AVAIL_QTY) from ( " + "select ITEM, LOC, AVAIL_QTY from XX_OMS_INVAVAIL_V UNION  " + "select PACK_NO, LOC, AVAIL_QTY from GET_PACK_QTY_V) "
				+ "where item IN (:item) and loc IN (:storeid) group by ITEM, LOC";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("item", itemList);
		paramSource.addValue("storeid", request.getReqLoc());

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, paramSource);
		for (Map<String, Object> row : rows) {
			String itemId = (String) row.get("ITEM");
			BigDecimal availToSell = (BigDecimal) row.get("SUM(AVAIL_QTY)");
			resultMap.put(itemId, availToSell);
			log.info("Itemloc and Available qty: " + itemId + " - " + availToSell);
		}

		return resultMap;
	}

	public Map<String, BigDecimal> stockCheckForReturn(StockRequest request, Map<String, BigDecimal> itemMap) {
		log.info("Checking stock for RETURN REQUEST");

		BigDecimal storeId = null;
		Map<String, BigDecimal> resultMap = new HashMap<>();
		for (Items items : request.getItems()) {
			String item = items.getItem();
			BigDecimal qty = items.getQty();
			itemMap.put(item, qty);
		}
		List<String> itemList = new ArrayList<>(itemMap.keySet());

		if (request.getReqLoc() != null) {
			storeId = request.getReqLoc();
		} else {
			String queryForStoreId = "SELECT REQ_LOC FROM xx_sp_stock_req_ret WHERE UPPER(TYPE) = 'STOCK REQUEST'" + "AND TSF_REQ_SEQ_ID = :seqId AND ROWNUM = 1";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("seqId", request.getTsfReqSeqId());

			storeId = jdbcTemplate.queryForObject(queryForStoreId, paramSource, BigDecimal.class);
			request.setReqLoc(storeId);
		}

		String query = "select item_id, AVAIL_TECH_SUB from XX_TECH_SUB_AVAIL_V@simdb where tech_sub_bucket = :techId " + "and item_id IN (:item) and store_id = :storeid";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("item", itemList);
		paramSource.addValue("storeid", storeId);
		paramSource.addValue("techId", request.getReqTechId());

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, paramSource);
		for (Map<String, Object> row : rows) {
			String itemId = (String) row.get("item_id");
			BigDecimal availToSell = (BigDecimal) row.get("AVAIL_TECH_SUB");
			resultMap.put(itemId, availToSell);
			log.info("Itemloc and Available qty: " + itemId + " - " + availToSell);
		}

		return resultMap;
	}

	public Map<String, BigDecimal> getUnapprovedQtyForReturn(StockRequest request, Map<String, BigDecimal> unApprovedItemQty) {
		log.info("Fetching unApprovedQty for Return");
		BigDecimal storeId = request.getReqLoc();

		Map<String, BigDecimal> resultMap = new HashMap<>();
		for (Items items : request.getItems()) {
			String item = items.getItem();
			BigDecimal qty = items.getQty();
			unApprovedItemQty.put(item, qty);
		}
		List<String> itemList = new ArrayList<>(unApprovedItemQty.keySet());

		String query = "SELECT NVL(MAX(ITEM), 'NULL') AS ITEM, NVL(SUM(b.QTY), 0) as QTY FROM xx_sp_stock_req_ret a, xx_sp_stock_req_ret_item b WHERE a.REQ_LOC = :storeId AND a.TYPE = 'Return Request' AND a.REQ_TECH_ID = :techId AND a.STATUS = 'N' AND a.TSF_REQ_SEQ_ID = b.TSF_REQ_SEQ_ID AND a.SRV_REQ_ID = b.SRV_REQ_ID AND b.ITEM IN (:itemList)GROUP BY b.ITEM";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("itemList", itemList);
		paramSource.addValue("storeId", storeId);
		paramSource.addValue("techId", request.getReqTechId());


		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, paramSource);
		for (Map<String, Object> row : rows) {
			String itemId = (String) row.get("ITEM");
			BigDecimal unApprovedQty = (BigDecimal) row.get("QTY");
			resultMap.put(itemId, unApprovedQty);
			log.info("Itemloc and Un Approved qty: " + itemId + " - " + unApprovedQty);
		}

		return resultMap;
	}

	@Transactional
	public void persistIntoHeadAndItem(StockDeductionRequest request) {
		log.info("Inserting data into head table - xx_spare_parts_hdr");
		try {
			String selectQuery = "SELECT COUNT(*) FROM xx_spare_parts_hdr WHERE SR_SEQ_ID = :srSeqId AND SRV_REQ_ID = :srvId";

			MapSqlParameterSource selectParamSource = new MapSqlParameterSource();
			selectParamSource.addValue("srSeqId", request.getSrSeqId());
			selectParamSource.addValue("srvId", request.getSrvReqId());
			int count = jdbcTemplate.queryForObject(selectQuery, selectParamSource, Integer.class);

			if (count == 0) {
				log.info("Inserting data into xx_sp_deduct_head table");

				String query = " INSERT INTO xx_spare_parts_hdr(SR_SEQ_ID, SRV_REQ_ID, REQ_LOC, REQ_TECH_ID, REASON_CODE, CREATE_DATETIME, STATUS) "
						+ " VALUES(:seqId, :srvId, :loc, :techId, :reasonCode, sysdate, :status)";

				MapSqlParameterSource paramSource = new MapSqlParameterSource();
				paramSource.addValue("seqId", request.getSrSeqId());
				paramSource.addValue("srvId", request.getSrvReqId());
				paramSource.addValue("loc", request.getReqLoc());
				paramSource.addValue("techId", request.getReqTechId());
				paramSource.addValue("reasonCode", request.getReasonCode());
				paramSource.addValue("status", "D");

				jdbcTemplate.update(query, paramSource);

				log.info("Inserting data into xx_spare_parts_item table");
				List<MapSqlParameterSource> batchParams = new ArrayList<>();

				String itemQuery = "INSERT INTO xx_spare_parts_item(SR_SEQ_ID, SRV_REQ_ID, ITEM, QTY, CREATE_DATETIME) " + "    VALUES(:seqId, :srvId, :item, :qty, sysdate) ";
				for (Items item : request.getItems()) {
					MapSqlParameterSource itemParamSource = new MapSqlParameterSource();
					itemParamSource.addValue("seqId", request.getSrSeqId());
					itemParamSource.addValue("srvId", request.getSrvReqId());
					itemParamSource.addValue("item", item.getItem());
					itemParamSource.addValue("qty", item.getQty());
					batchParams.add(itemParamSource);
				}
				jdbcTemplate.batchUpdate(itemQuery, batchParams.toArray(new MapSqlParameterSource[0]));
			}
		} catch (Exception e) {
			log.error("Failed inserting into tables", e);
			throw e;
		}

	}

	public String checkMessage(StockDeductionRequest request) {
		log.info("Checking COMMENTS from xx_sp_stock_req_ret");
		String query = "SELECT COMMENTS FROM xx_sp_stock_req_ret WHERE TSF_REQ_SEQ_ID = :seqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", request.getSrSeqId());

		try {
			return jdbcTemplate.queryForObject(query, paramSource, String.class);
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No record found for TSF_REQ_SEQ_ID = " + request.getSrSeqId());
			return null;
		}
	}

	public int checkRow(StockDeductionRequest request) {
		log.info("Checking record existence in xx_sp_stock_req_ret");
		String query = "SELECT COUNT(*) FROM xx_sp_stock_req_ret WHERE TSF_REQ_SEQ_ID = :seqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", request.getSrSeqId());

		try {
			Integer count = jdbcTemplate.queryForObject(query, paramSource, Integer.class);
			return (count != null && count > 0) ? 1 : 0;
		} catch (Exception e) {
			log.error("Error while checking record: ", e);
			return 0;
		}
	}

	public Long getTechReasonCode(String tsfRecSeqId, String reqTechId) {
		log.info("Fetching TechReasonCode from xx_sp_tsf_req_rec table");
		Long reasonCode = null;
		String query = "Select AVAIL_TO_TECH from XX_TECH_REAS_CODE_V @simdb where DESCRIPTION = :reqTecId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("reqTecId", reqTechId);

		try {
			reasonCode = jdbcTemplate.queryForObject(query, paramSource, Long.class);
		} catch (Exception e) {
			log.error("Failed to retrieve tech_reason_code from xx_sp_tsf_req_rec", e);
			throw e;
		}

		return reasonCode;
	}

	public void insertRequest(String seqId, String srvId) {
		String status = null;
		log.info("Inserting request XX_SP_TSF_REQ_REC table");
		String query = " INSERT INTO xx_sp_tsf_req_rec (TSF_REQ_SEQ_ID, SRV_REQ_ID, CREATION_TIME, STATUS) " + " VALUES(:seqId, :srvId, sysdate, 'N')";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId.toString());
		paramSource.addValue("srvId", srvId);
		try {
			jdbcTemplate.update(query, paramSource);
		} catch (Exception e) {
			log.error("Failed to inset to xx_sp_tsf_req_rec", e);
			throw e;
		}
	}

	public StockRecDetails fetchRecItemDetails(String seqId) {
		log.info("Fetching items details");
		String query = "select * from xx_sp_stock_req_ret where TSF_REQ_SEQ_ID IN (:seqId) and type = 'Stock Request'";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);
		try {
			return jdbcTemplate.queryForObject(query, paramSource, new StockRecDetailsRowMapper());
		} catch (Exception e) {
			log.error("Failed to get details from xx_sp_stock_req_ret", e);
			throw e;
		}
	}

	public class StockRecDetailsRowMapper implements RowMapper<StockRecDetails> {

		@Override
		public StockRecDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			StockRecDetails details = new StockRecDetails();
			details.setTsfReqSeqId(rs.getString("TSF_REQ_SEQ_ID"));
			details.setSrvReqId(rs.getString("SRV_REQ_ID"));
			details.setSrvAging(rs.getString("SRV_AGING"));
			details.setSrvLine(rs.getString("SRV_LINE"));
			details.setReqLoc(rs.getBigDecimal("REQ_LOC"));
			details.setReqTechId(rs.getString("REQ_TECH_ID"));
			details.setType(rs.getString("TYPE"));
			details.setCreationTime(rs.getTimestamp("CREATION_TIME"));
			details.setLastUpdatedTime(rs.getTimestamp("LAST_UPDATED_TIME"));
			details.setStatus(rs.getString("STATUS"));
			details.setComments(rs.getString("COMMENTS"));

			List<Items> itemsList = fetchItemsDetails(rs.getString("TSF_REQ_SEQ_ID"));

			details.setItems(itemsList);

			return details;
		}

	}

	private List<Items> fetchItemsDetails(String seqId) {
		String itemQuery = "select ITEM, QTY, LINE_ID from xx_sp_stock_req_ret_item where TSF_REQ_SEQ_ID = :seqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);

		return jdbcTemplate.query(itemQuery, paramSource, new RowMapper<Items>() {
			@Override
			public Items mapRow(ResultSet itemRs, int itemRowNum) throws SQLException {
				Items item = new Items();
				item.setItem(itemRs.getString("ITEM"));
				item.setQty(itemRs.getBigDecimal("QTY"));
				item.setLineId(itemRs.getBigDecimal("LINE_ID"));
				return item;
			}
		});
	}

	public void updateRecStatus(String seqId, String status) {
		log.info("Updating status in xx_sp_tsf_req_rec table");
		String query = "update xx_sp_tsf_req_rec set status = :status where TSF_REQ_SEQ_ID = :seqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);
		paramSource.addValue("status", status);

		try {
			jdbcTemplate.update(query, paramSource);
		} catch (Exception e) {
			log.error("Failed to update status in xx_sp_tsf_req_rec ", e);
			throw e;
		}

	}

	public String checkRecStatus(String seqId) {
		log.info("Checking status from the xx_sp_tsf_req_rec");
		String resultStatus = null;
		String query = " select status from xx_sp_tsf_req_rec where TSF_REQ_SEQ_ID = :seqId AND ROWNUM = 1";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);
		try {
			resultStatus = jdbcTemplate.queryForObject(query, paramSource, String.class);
			log.info("STATUS -" + resultStatus);
		} catch (Exception e) {
			log.error("Failed to get status from xx_sp_tsf_req_rec", e);
		}
		return resultStatus;
	}

	public void checkStock(StockDeductionRequest request, Map<String, BigDecimal> itemMap) {
		log.info("Checking the stock in tech bucket");
		for (Items items : request.getItems()) {
			String item = items.getItem();
			BigDecimal qty = items.getQty();
			itemMap.put(item, qty);
		}
		List<String> itemKeysList = new ArrayList<>(itemMap.keySet());
		String query = "select X.item_id, X.AVAIL_TECH_SUB AS AVAILABLE_QTY, V.TECH_TO_AVAIL, V.AVAIL_TO_TECH from " + "XX_TECH_SUB_AVAIL_V @simdb X , XX_TECH_REAS_CODE_V @simdb V  where "
				+ "V.DESCRIPTION = X.tech_sub_bucket and V.DESCRIPTION = :techId and X.tech_sub_bucket = :techId and X.item_id IN (:items) and X.store_id = :storeid ";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("techId", request.getReqTechId());
		paramSource.addValue("items", itemKeysList);
		paramSource.addValue("storeid", request.getReqLoc());

		try {
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, paramSource);

			for (Items item : request.getItems()) {
				for (Map<String, Object> result : resultList) {
					String currentItem = (String) result.get("ITEM_ID");
					if (currentItem.equals(item.getItem())) {
						BigDecimal availableQty = (BigDecimal) result.get("AVAILABLE_QTY");
						BigDecimal reasonCode = (BigDecimal) result.get("TECH_TO_AVAIL");
						item.setOrigReasonCode(reasonCode.longValue());
						item.setTechAvailQty(availableQty);

					}

				}
			}

		} catch (Exception e) {
			log.error("Failed to get reason code from XX_TECH_REAS_CODE_V@simdb", e);
			throw e;
		}
	}

	@Transactional
	public void updateCommentsAndStatus(StockDeductionRequest request, String message, int row, String status) {
		log.info("Updating comments in xx_sp_stock_req_ret");
		String query = "UPDATE xx_sp_stock_req_ret set COMMENTS = :comments, LAST_UPDATED_TIME = sysdate where TSF_REQ_SEQ_ID = :seqId and SRV_REQ_ID = :srvId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", request.getSrSeqId());
		paramSource.addValue("srvId", request.getSrvReqId());
		paramSource.addValue("comments", message);
		try {
			jdbcTemplate.update(query, paramSource);
		} catch (Exception e) {
			log.error("Failed to update comments in xx_sp_stock_req_ret", e);
		}
		if (row != 0 && status != null) {
			updateDeductStatus(status, request.getSrSeqId());
		}
	}

	public List<Items> fetchItems(String seqId, String srvId) {

		String query = "";

		query = "select * from (SELECT I.ITEM, I.QTY, I.LINE_ID, H.REQ_LOC,H.REQ_TECH_ID,T.TECH_TO_AVAIL, T.AVAIL_TO_TECH FROM "
				+ "xx_sp_stock_req_ret_item I JOIN xx_sp_stock_req_ret H ON H.TSF_REQ_SEQ_ID = I.TSF_REQ_SEQ_ID   JOIN XX_TECH_REAS_CODE_V@simdb T "
				+ "ON T.DESCRIPTION = H.REQ_TECH_ID   WHERE H.TSF_REQ_SEQ_ID = :seqId AND UPPER(H.TYPE) = 'STOCK REQUEST'   union all  "
				+ "SELECT K.ITEM, K.QTY, null as LINE_ID, J.REQ_LOC, J.REQ_TECH_ID, U.TECH_TO_AVAIL, U.AVAIL_TO_TECH   FROM xx_spare_parts_hdr J  JOIN omsdev.xx_spare_parts_item K "
				+ "ON K.SR_SEQ_ID = J.SR_SEQ_ID AND K.SRV_REQ_ID = J.SRV_REQ_ID JOIN XX_TECH_REAS_CODE_V@simdb U ON U.DESCRIPTION = J.REQ_TECH_ID " + "WHERE J.SR_SEQ_ID = :seqId) where rownum = 1";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);
		paramSource.addValue("srvId", srvId);

		return jdbcTemplate.query(query, paramSource, new RowMapper<Items>() {
			@Override
			public Items mapRow(ResultSet itemRs, int itemRowNum) throws SQLException {
				Items item = new Items();
				item.setItem(itemRs.getString("ITEM"));
				item.setQty(itemRs.getBigDecimal("QTY"));
				item.setLineId(itemRs.getBigDecimal("LINE_ID"));
				item.setReqLoc(itemRs.getBigDecimal("REQ_LOC"));
				item.setTechId(itemRs.getString("REQ_TECH_ID"));
				item.setOrigReasonCode(itemRs.getLong("TECH_TO_AVAIL"));
				item.setAvailToTechReasonCode(itemRs.getLong("AVAIL_TO_TECH"));
				return item;
			}
		});
	}

	@Transactional
	public void updateCancelStatus(CancelRequest request, String status) {
		log.info("Updating cancel status in xx_sp_stock_req_ret table");
		int row = fetchCanDetails(request.getSrSeqId());
		if (row > 0) {
			String query = "UPDATE xx_sp_stock_req_ret SET STATUS = :status, LAST_UPDATED_TIME = sysdate where TSF_REQ_SEQ_ID = :seqId";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("seqId", request.getSrSeqId());
			paramSource.addValue("status", status);
			jdbcTemplate.update(query, paramSource);
		}

		int dedRow = fetchDeductStatus(request.getSrSeqId(), request.getSrvReqId());
		if (dedRow > 0) {
			updateDedCanStatus(request.getSrSeqId(), request.getSrvReqId(), status);
		}
	}

	@Transactional
	public void updateStockReqStatus(String seqId, String mainStatus, String status) {
		log.info("Updating cancel status to 'R' in xx_sp_stock_req_ret table");
		String query = "UPDATE xx_sp_stock_req_ret SET STATUS = :status, LAST_UPDATED_TIME = sysdate where TSF_REQ_SEQ_ID = :seqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);
		paramSource.addValue("status", mainStatus);
		jdbcTemplate.update(query, paramSource);
		updateRecStatus(seqId, status);

	}

	public String checkCancelStatus(String seqId, String status) {
		log.info("Checking cancel status in xx_sp_stock_req_ret table");
		String query = "SELECT STATUS FROM xx_sp_stock_req_ret WHERE TSF_REQ_SEQ_ID = :seqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seqId", seqId);

		try {
			status = jdbcTemplate.queryForObject(query, paramSource, String.class);
			log.info("result-" + status);
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn("No record found for TSF_REQ_SEQ_ID = " + seqId);
			status = null;
		}
		return status;
	}

	public void insertCancelDetails(CancelRequest request, String cancelStatus, String status) {
		log.info("Inserting cancel details in xx_spare_parts_cancel table");
		String query = "Insert INTO xx_spare_parts_cancel(SR_SEQ_ID, SRV_REQ_ID, STATUS, CREATION_TIME) " + "VALUES(:srSeqId, :srvReqId, :status, sysdate)";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("srSeqId", request.getSrSeqId());
		paramSource.addValue("srvReqId", request.getSrvReqId());
		paramSource.addValue("status", cancelStatus);
		jdbcTemplate.update(query, paramSource);
	}

	public int insertRequestLog(String srId, String seqId, String request, String type) {
		log.info("Inserting request in XX_SP_LOG_INFO table");

		// Step 1: Find current max SEQ_NO for same SR_ID, SEQ_ID, and REQUEST
		String selectQuery = "SELECT NVL(MAX(SEQ_NO), 0) FROM XX_SP_LOG_INFO " + "WHERE SR_ID = :srId AND SEQ_ID = :seqId AND TYPE = :type";

		MapSqlParameterSource selectParams = new MapSqlParameterSource();
		selectParams.addValue("srId", srId);
		selectParams.addValue("seqId", seqId);
		selectParams.addValue("type", type);

		int currentMaxSeqNo = jdbcTemplate.queryForObject(selectQuery, selectParams, Integer.class);
		int newSeqNo = currentMaxSeqNo + 1;

		// Step 2: Insert the new record with new SEQ_NO
		String insertQuery = "INSERT INTO XX_SP_LOG_INFO(SR_ID, SEQ_ID, SEQ_NO, REQUEST, TYPE, CREATE_DATETIME) " + "VALUES(:srId, :seqId, :seqNo, :request, :type, sysdate)";

		MapSqlParameterSource insertParams = new MapSqlParameterSource();
		insertParams.addValue("srId", srId);
		insertParams.addValue("seqId", seqId);
		insertParams.addValue("seqNo", newSeqNo);
		insertParams.addValue("request", request);
		insertParams.addValue("type", type);

		jdbcTemplate.update(insertQuery, insertParams);
		return newSeqNo;
	}

	public void updateResponseLog(String srId, String seqId, String response, String type, int seqNo) {
		log.info("Updating response in XX_SP_LOG_INFO table");
		try {
			String query = "UPDATE XX_SP_LOG_INFO SET RESPONSE = :response, UPDATE_DATETIME = sysdate WHERE SR_ID = :srId and TYPE = :type and SEQ_ID = :seqId and  SEQ_NO = :seqNo";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("srId", srId);
			paramSource.addValue("seqId", seqId);
			paramSource.addValue("seqNo", seqNo);
			paramSource.addValue("response", response);
			paramSource.addValue("type", type);
			jdbcTemplate.update(query, paramSource);
		} catch (Exception e) {
			log.error("Error while updating ", e);
		}
	}

	public int fetchDeductStatus(String srSeqId, String srvReqId) {
		log.info("Fetching count in xx_spare_parts_hdr table");
		String selectQuery = "SELECT COUNT(*) FROM xx_spare_parts_hdr WHERE SR_SEQ_ID = :srSeqId";
		MapSqlParameterSource selectParamSource = new MapSqlParameterSource();
		selectParamSource.addValue("srSeqId", srSeqId);
		int count = jdbcTemplate.queryForObject(selectQuery, selectParamSource, Integer.class);
		log.info("count-" + count);
		return count;
	}

	public int fetchCanDetails(String srSeqId) {
		log.info("Fetching count in xx_sp_stock_req_ret table");
		String selectQuery = "SELECT COUNT(*) FROM xx_sp_stock_req_ret WHERE TSF_REQ_SEQ_ID = :srSeqId";
		MapSqlParameterSource selectParamSource = new MapSqlParameterSource();
		selectParamSource.addValue("srSeqId", srSeqId);
		int count = jdbcTemplate.queryForObject(selectQuery, selectParamSource, Integer.class);
		log.info("count-" + count);
		return count;
	}

	public long fetchFirstReasonCode(String srSeqId, String srvReqId) {
		log.info("Fetching reason code in XX_SP_REASON_CODE table");
		String selectQuery = "select RSN_CODE_IN from XX_SP_REASON_CODE where RSN_CODE_OUT IN (select distinct REASON_CODE from XX_SPARE_PARTS_HDR where SR_SEQ_ID = :srSeqId)";
		MapSqlParameterSource selectParamSource = new MapSqlParameterSource();
		selectParamSource.addValue("srSeqId", srSeqId);
		Long reasonCode = jdbcTemplate.queryForObject(selectQuery, selectParamSource, Long.class);
		log.info("reasonCode-" + reasonCode);
		return reasonCode;
	}

	public void updateDedCanStatus(String srSeqId, String srvReqId, String status) {
		log.info("Updating Deduction cancel status in xx_spare_parts_hdr table");
		String query = "UPDATE xx_spare_parts_hdr SET STATUS = :status WHERE SR_SEQ_ID = :srSeqId";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("srSeqId", srSeqId);
		paramSource.addValue("status", status);
		jdbcTemplate.update(query, paramSource);
	}
}
