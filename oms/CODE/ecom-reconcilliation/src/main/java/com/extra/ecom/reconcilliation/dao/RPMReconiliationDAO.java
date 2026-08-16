package com.extra.ecom.reconcilliation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extra.ecom.reconcilliation.bean.Catelogue;
import com.extra.ecom.reconcilliation.bean.CatelogueResponse;
import com.extra.ecom.reconcilliation.bean.ProductDetail;
import com.extra.ecom.reconcilliation.bean.PromotionDetail;
import com.extra.ecom.reconcilliation.bean.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aibrahim
 *
 */
@Repository
public class RPMReconiliationDAO {

	private static final Logger LOG = Logger.getLogger(RPMReconiliationDAO.class);

	@Autowired
	private NamedParameterJdbcOperations jdbcTemplate;

	private ObjectMapper mapper = new ObjectMapper();

	public List<Collection<Catelogue>> getItemDetails() {
		return jdbcTemplate.query("SELECT I.ROWID, I.ITEM, I.LOC FROM XX_ITEM_RMS_HYBRIS_RECON I WHERE I.PICK_STATUS = 'N'", new ResultSetExtractor<List<Collection<Catelogue>>>() {

			@Override
			public List<Collection<Catelogue>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Collection<Catelogue>> list = new ArrayList<Collection<Catelogue>>();
				Map<String, Catelogue> catelogueMap = new HashMap<>();
				int index = 0;
				int buckLength = 200;
				while(rs.next()) {
					index++;
					String catelogueCode = rs.getString(3);
					Catelogue catelogue = catelogueMap.get(catelogueCode);
					if (catelogue == null) {
						catelogue = new Catelogue();
						catelogue.setCatelougeCode(catelogueCode);
						catelogue.setProductCodes(new LinkedList<String>());
						catelogue.setRowIds(new LinkedList<String>());
						catelogueMap.put(catelogueCode, catelogue);
					}
					catelogue.getProductCodes().add(rs.getString(2));
					catelogue.getRowIds().add(rs.getString(1));
					if (index % buckLength == 0) {
						list.add(catelogueMap.values());
						catelogueMap = new HashMap<>();
					}
				}
				if (!catelogueMap.isEmpty()) {
					list.add(catelogueMap.values());
				}
				return list;
			}
		});
	}

	@Transactional
	public void updateProductDetail(Collection<Catelogue> catelogues, List<CatelogueResponse> catelogueProducts) {
		Map<String, Object> params = new HashMap<>();
		List<String> rowIds = new LinkedList<>();
		for (Catelogue catelogue : catelogues) {
			rowIds.addAll(catelogue.getRowIds());
		}
		params.put("rowIds", rowIds);
		jdbcTemplate.update("UPDATE XX_ITEM_RMS_HYBRIS_RECON SET PICK_STATUS = 'Y' WHERE ROWID IN (:rowIds)", params);
	
		List<SqlParameterSource> paramSource = new ArrayList<SqlParameterSource>();
		for (CatelogueResponse catelogue : catelogueProducts) {
			MapSqlParameterSource param = null;
			for (String product : catelogue.getAvailProductCodes()) {
				param = new MapSqlParameterSource();
				param.addValue("item", product);
				param.addValue("loc", catelogue.getCatelougeCode());
				param.addValue("reconType", "AVAIL_PRODUCT");
				paramSource.add(param);
			}
			for (String product : catelogue.getUnavailProductCodes()) {
				param = new MapSqlParameterSource();
				param.addValue("item", product);
				param.addValue("loc", catelogue.getCatelougeCode());
				param.addValue("reconType", "UNAVAIL_PRODUCT");
				paramSource.add(param);
			}
			for (String product : catelogue.getAvailPriceCodes()) {
				param = new MapSqlParameterSource();
				param.addValue("item", product);
				param.addValue("loc", catelogue.getCatelougeCode());
				param.addValue("reconType", "AVAIL_PRICE");
				paramSource.add(param);
			}
			for (String product : catelogue.getUnavailPriceCodes()) {
				param = new MapSqlParameterSource();
				param.addValue("item", product);
				param.addValue("loc", catelogue.getCatelougeCode());
				param.addValue("reconType", "UNAVAIL_PRICE");
				paramSource.add(param);
			}
		}
		jdbcTemplate.batchUpdate(
				"INSERT INTO XX_ITEM_RMS_HYBRIS_RECON_VALID(ITEM, LOC, RECON_TYPE, VALIDATION_FLAG, CREATE_DATE_TIME, LAST_UPDATE_DATETIME) VALUES(:item, :loc, :reconType, 'N', SYSDATE, SYSDATE)",
				paramSource.toArray(new SqlParameterSource[paramSource.size()]));
	}

	public List<Collection<Catelogue>> getPriceDetails() {
		return jdbcTemplate.query("SELECT I.ROWID, I.ITEM, I.LOC FROM XX_ITEM_PRICE_HYBRIS_STAGE I WHERE I.PICK_STATUS = 'N'", new ResultSetExtractor<List<Collection<Catelogue>>>() {

			@Override
			public List<Collection<Catelogue>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Collection<Catelogue>> list = new ArrayList<Collection<Catelogue>>();
				Map<String, Catelogue> catelogueMap = new HashMap<>();
				int index = 0;
				int buckLength = 200;
				while(rs.next()) {
					index++;
					String catelogueCode = rs.getString(3);
					Catelogue catelogue = catelogueMap.get(catelogueCode);
					if (catelogue == null) {
						catelogue = new Catelogue();
						catelogue.setCatelougeCode(catelogueCode);
						catelogue.setProductCodes(new LinkedList<String>());
						catelogue.setRowIds(new LinkedList<String>());
						catelogueMap.put(catelogueCode, catelogue);
					}
					catelogue.getProductCodes().add(rs.getString(2));
					catelogue.getRowIds().add(rs.getString(1));
					if (index % buckLength == 0) {
						list.add(catelogueMap.values());
						catelogueMap = new HashMap<>();
					}
				}
				if (!catelogueMap.isEmpty()) {
					list.add(catelogueMap.values());
				}
				return list;
			}
		});
	}

	@Transactional
	public void updatePriceDetail(Collection<Catelogue> catelogues, List<CatelogueResponse> catelogueProducts) {
		Map<String, Object> params = new HashMap<>();
		List<String> rowIds = new LinkedList<>();
		for (Catelogue catelogue : catelogues) {
			rowIds.addAll(catelogue.getRowIds());
		}
		params.put("rowIds", rowIds);
		jdbcTemplate.update("UPDATE XX_ITEM_PRICE_HYBRIS_STAGE SET PICK_STATUS = 'Y' WHERE ROWID IN (:rowIds)", params);
	
		List<SqlParameterSource> paramSource = new ArrayList<SqlParameterSource>();
		for (CatelogueResponse catelogue : catelogueProducts) {
			MapSqlParameterSource param = null;
			for (ProductDetail product : catelogue.getProductDetails()) {
				param = new MapSqlParameterSource();
				param.addValue("item", product.getProductCode());
				param.addValue("loc", catelogue.getCatelougeCode());
				param.addValue("basePrice", product.getBasePrice());
				param.addValue("sellingPrice", product.getSellingPrice());
				paramSource.add(param);
			}
		}
		jdbcTemplate.batchUpdate(
				"INSERT INTO XX_ITEM_PRICE_HYBRIS_DATA(ITEM, LOC, BASE_PRICE, SELLING_PRICE, VALIDATION_FLAG, CREATE_DATE_TIME, LAST_UPDATE_DATETIME) VALUES(:item, :loc, :basePrice, :sellingPrice, 'N', SYSDATE, SYSDATE)",
				paramSource.toArray(new SqlParameterSource[paramSource.size()]));
	}

	public List<Collection<Catelogue>> getPromotionDetails() {
		return jdbcTemplate.query("SELECT I.ROWID, I.PROMO_ID, I.PROMO_COMP_ID, I.PROMO_DTL_ID, I.LOCATION FROM XX_PROMO_INFO_HYBRIS_STAGE I WHERE I.PICK_STATUS = 'N'", new ResultSetExtractor<List<Collection<Catelogue>>>() {

			@Override
			public List<Collection<Catelogue>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Collection<Catelogue>> list = new ArrayList<Collection<Catelogue>>();
				Map<String, Catelogue> catelogueMap = new HashMap<>();
				int index = 0;
				int buckLength = 200;
				while(rs.next()) {
					index++;
					String catelogueCode = rs.getString(5);
					Catelogue catelogue = catelogueMap.get(catelogueCode);
					if (catelogue == null) {
						catelogue = new Catelogue();
						catelogue.setCatelougeCode(catelogueCode);
						catelogue.setPromotionDetails(new LinkedList<PromotionDetail>());
						catelogue.setRowIds(new LinkedList<String>());
						catelogueMap.put(catelogueCode, catelogue);
					}
					PromotionDetail promotion = new PromotionDetail();
					promotion.setId(rs.getLong(2));
					promotion.setCompId(rs.getLong(3));
					promotion.setDetailId(rs.getLong(4));
					catelogue.getPromotionDetails().add(promotion);
					catelogue.getRowIds().add(rs.getString(1));
					if (index % buckLength == 0) {
						list.add(catelogueMap.values());
						catelogueMap = new HashMap<>();
					}
				}
				if (!catelogueMap.isEmpty()) {
					list.add(catelogueMap.values());
				}
				return list;
			}
		});
	}

	@Transactional
	public void updatePromotionDetail(Collection<Catelogue> catelogues, List<CatelogueResponse> catelogueProducts) {
		Map<String, Object> params = new HashMap<>();
		List<String> rowIds = new LinkedList<>();
		for (Catelogue catelogue : catelogues) {
			rowIds.addAll(catelogue.getRowIds());
		}
		params.put("rowIds", rowIds);
		jdbcTemplate.update("UPDATE XX_PROMO_INFO_HYBRIS_STAGE SET PICK_STATUS = 'Y' WHERE ROWID IN (:rowIds)", params);
	
		List<SqlParameterSource> paramSource = new ArrayList<SqlParameterSource>();
		for (CatelogueResponse catelogue : catelogueProducts) {
			MapSqlParameterSource param = null;
			for (PromotionDetail promotion : catelogue.getAvailPromoDetails()) {
				param = new MapSqlParameterSource();
				param.addValue("id", promotion.getId());
				param.addValue("compId", promotion.getCompId());
				param.addValue("detailId", promotion.getDetailId());
				param.addValue("loc", catelogue.getCatelougeCode());
				param.addValue("reconType", "AVAIL_PROMO");
				paramSource.add(param);
			}
			for (PromotionDetail promotion : catelogue.getUnavailPromoDetails()) {
				param = new MapSqlParameterSource();
				param.addValue("id", promotion.getId());
				param.addValue("compId", promotion.getCompId());
				param.addValue("detailId", promotion.getDetailId());
				param.addValue("loc", catelogue.getCatelougeCode());
				param.addValue("reconType", "UNAVAIL_PROMO");
				paramSource.add(param);
			}
		}
		jdbcTemplate.batchUpdate(
				"INSERT INTO XX_PROMO_INFO_HYBRIS_DATA(PROMO_ID, PROMO_COMP_ID, PROMO_DTL_ID, LOCATION, RECON_TYPE, VALIDATION_FLAG, CREATE_DATETIME, LAST_UPDATE_DATETIME) VALUES(:id, :compId, :detailId, :loc, :reconType, 'N', SYSDATE, SYSDATE)",
				paramSource.toArray(new SqlParameterSource[paramSource.size()]));
	}

	public void updateResponse(Response resp) {
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		try {
			param.addValue("respbody", mapper.writeValueAsString(resp));
			jdbcTemplate.update("INSERT INTO XX_RETEK_API_LOG(ID, RESPONSE, CREATE_DATETIME) VALUES(XX_RETEK_API_SEQ.NEXTVAL, :respbody, SYSDATE)", param);
		} catch (JsonProcessingException e) {
			LOG.warn("E-Com reconciliation: Error while updating the response to table", e);
		}
	}
}
