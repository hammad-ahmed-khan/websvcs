/**
 * 
 */
package com.extra.apple.pricing.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.extra.apple.pricing.model.PricePlan;
import com.extra.apple.pricing.model.PricePlanOffer;
import com.extra.apple.pricing.model.PriceSheet;
import com.extra.apple.pricing.model.PriceSheetDescription;
import com.extra.apple.pricing.model.Property;

/**
 * @author aibrahim
 *
 */
@Repository
public class PricingDAO {

	private static final Logger LOG = Logger.getLogger(PricingDAO.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	@Value("${apple.id}")
	private Long appleId;

	@Autowired
	@Value("#{${iPadDeptIds}}")
	private List<String> iPadDeptIds;

	public Map<Long, Long> getCurrentPriceIDs() {
		return jdbcTemplate.query("SELECT PRICE_SHEET_ID, APPLE_ID FROM XX_APPLE_STORE_PS_DETAIL WHERE APPLE_ID = :appleId", Collections.singletonMap("appleId", appleId), new ResultSetExtractor<Map<Long, Long>>() {

			@Override
			public Map<Long, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, Long> idMap = new HashMap<Long, Long>();
				while(rs.next()) {
					idMap.put(rs.getLong(1), rs.getLong(2));
				}
				return idMap;
			}
		});
	}

	public void updateMPN(final String productName, List<String> mpns) throws Exception {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);

		declaredParameters.add(new SqlParameter(Types.ARRAY));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		final Object[][] objArr = new Object[mpns.size()][1];
		for(int i = 0; i < mpns.size();i++) {
			objArr[i][0] = mpns.get(i);
		}
		Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
			
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ CALL XX_APPLE_ON_PRICING_SQL.APPLE_MPN_REFRESH(?, ?, ?, ?) }" );
				stmt.setArray(1, ((oracle.jdbc.driver.OracleConnection)con).createOracleArray("XXMPNS_TBL_TYPE", objArr));
				stmt.setString(2, productName);
				stmt.registerOutParameter(3, Types.VARCHAR);
				stmt.registerOutParameter(4, Types.VARCHAR);
		        return stmt;
			}
		}, declaredParameters);
		Object result = resultMap.get("status");
		if (!"Y".equals(result)) {
			throw new Exception(resultMap.get("message").toString());
		}
	}

	public List<PriceSheet> getUpdatedPriceSheet() {
		return jdbcTemplate.query("SELECT P.*, S.APPLE_ID FROM XX_APPLE_STORE_ITEM_PRICES P, XX_APPLE_STORE_PS_DETAIL S "
				+ "WHERE S.STORE = P.STORE AND S.PRODUCT_CODE = P.PRODUCT_CODE AND P.PUBLISH_IND = 'N'", new ResultSetExtractor<List<PriceSheet>>() {

			@Override
			public List<PriceSheet> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, PriceSheet> priceSheetMap = new HashMap<Long, PriceSheet>();
				while (rs.next()) {
					Long priceSheetId = rs.getLong(11);
					PriceSheet sheet = priceSheetMap.get(priceSheetId);
					if (sheet == null) {
						sheet = new PriceSheet();
						sheet.setPriceSheetId(priceSheetId);
						sheet.setPriceSheetDescriptions(new LinkedList<PriceSheetDescription>());
						sheet.setLob(rs.getString("PRODUCT"));
						sheet.setStores(Collections.singletonList(rs.getLong("APPLE_ID")));
						priceSheetMap.put(priceSheetId, sheet);
					}
					PriceSheetDescription description = new PriceSheetDescription();
					description.setMpns(Collections.singletonList(rs.getString(2)));
					
					String offer = rs.getString("CONSUMER_OFFER");
					String valueProposition = rs.getString("VALUE_PROPOSITION");
					if (!iPadDeptIds.contains(rs.getString("DEPT"))) {
						Property property = new Property();
						property.setConsumerOffer(offer);
						property.setValueProposition(valueProposition);
						property.setPartnerTC(null);
						description.setProperties(property);
					}

					PricePlan pricePlan = new PricePlan();
					pricePlan.setPlanName(rs.getString("PLAN_NAME"));
					
					PricePlanOffer pricePlanOffer = new PricePlanOffer();
					pricePlanOffer.setPriceOfferTitle(rs.getString("OFFER_NAME"));
					pricePlanOffer.setOfferNumericPrice(rs.getBigDecimal("SELLING_PRICE"));
					pricePlanOffer.setOfferTerm(rs.getString("OFFER_TERM"));
					pricePlanOffer.setQualifyingDescriptionTitle(rs.getString("QUAL_DESC_TITLE"));
					pricePlanOffer.setQualifyingDescription(rs.getString("QUAL_DESC"));
					pricePlanOffer.setOfferRelation(rs.getString("OFFER_RELATION"));
					pricePlan.setPricePlanOffers(Collections.singletonList(pricePlanOffer));

					description.setPricePlans(Collections.singletonList(pricePlan));
					sheet.getPriceSheetDescriptions().add(description);
				}
				return new ArrayList<PriceSheet>(priceSheetMap.values());
			}
		});
	}

	public void publishPriceSheet(final PriceSheet sheet) throws Exception {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);

		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		try {
			Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
				
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{ CALL XX_APPLE_ON_PRICING_SQL.POST_PUBLISH_UPDATE(?, ?, ?) }" );
					stmt.setLong(1, sheet.getPriceSheetId());
					stmt.registerOutParameter(2, Types.VARCHAR);
					stmt.registerOutParameter(3, Types.VARCHAR);
			        return stmt;
				}
			}, declaredParameters);
			Object result = resultMap.get("status");
			if (!"Y".equals(result)) {
				throw new Exception(resultMap.get("message").toString());
			}
		} catch (Exception e) {
			LOG.error("Error while updating the price sheets " , e);
			throw new Exception(e.getMessage());
		}
	}

	public void rePublishPriceSheet(Map<Long, Entry<PriceSheet, Future<PriceSheet>>> updatedResults) throws Exception {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);

		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.DATE));
		declaredParameters.add(new SqlParameter(Types.DATE));
		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		try {
			for (final Entry<Long, Entry<PriceSheet, Future<PriceSheet>>> sheetEntry : updatedResults.entrySet()) {
				final PriceSheet newSheet = sheetEntry.getValue().getValue().get();
				Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
					
					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						CallableStatement stmt = con.prepareCall("{ CALL XX_APPLE_ON_PRICING.POST_REPUBLISH_UPDATE(?, ?, ?, ?, ?, ?) }" );
						stmt.setLong(1, newSheet.getPriceSheetId());
						stmt.setLong(2, newSheet.getPublishId());
						stmt.setDate(3, new Date(sheetEntry.getValue().getKey().getStartDate().getTime()));
						stmt.setDate(4, new Date(sheetEntry.getValue().getKey().getEndDate().getTime()));
						stmt.registerOutParameter(5, Types.VARCHAR);
						stmt.registerOutParameter(6, Types.VARCHAR);
				        return stmt;
					}
				}, declaredParameters);
				Object result = resultMap.get("status");
				if (!"Y".equals(result)) {
					throw new Exception(resultMap.get("message").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public Map<Long, Long> getPriceSheetIDs() {
		return jdbcTemplate.query("SELECT PRICE_SHEET_ID, APPLE_ID FROM XX_APPLE_STORE_PRICE_SHEET WHERE REPUBLISH_IND = 'N'", new ResultSetExtractor<Map<Long, Long>>() {

			@Override
			public Map<Long, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, Long> priceSheetStoreMap = new HashMap<Long, Long>();
				while(rs.next()) {
					priceSheetStoreMap.put(rs.getLong(1), rs.getLong(2));
				}
				return priceSheetStoreMap;
			}
		});
	}

	public List<Long> getUpdatedPriceSheetIds() {
		return jdbcTemplate.queryForList("SELECT PRICE_SHEET_ID FROM XX_APPLE_STORE_PS_DETAIL WHERE PUBLISH_IND = 'N'", Collections.<String, Object>emptyMap(), Long.class);
	}
}
