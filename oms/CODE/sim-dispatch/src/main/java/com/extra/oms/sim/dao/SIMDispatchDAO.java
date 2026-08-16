package com.extra.oms.sim.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import com.extra.common.exception.BaseException;
import com.extra.oms.sim.bean.Request;
import com.oracle.retail.integration.base.bo.fodcremodvo.v1.FodCreItmMod;
import com.oracle.retail.integration.base.bo.fodcremodvo.v1.FodCreModVo;

/**
 * @author aibrahim
 *
 */
@Repository
public class SIMDispatchDAO {

	@Autowired
	private NamedParameterJdbcOperations jdbcTemplate;

	public FodCreModVo getFulfilmentDetails(final Request request) throws BaseException {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("extNo", request.getFulfilNo());
			params.put("ordNo", request.getOrderNo());
			params.put("itemId", request.getItem());
			return jdbcTemplate.queryForObject("SELECT O.ID FULFILL_ORDER_ID, I.ID FULFILL_ORDER_LINE_ID FROM FUL_ORD_LINE_ITEM I, FUL_ORD O WHERE O.ID = I.FUL_ORD_ID "
					+ "AND O.EXTERNAL_ID = :extNo AND CUST_ORDER_ID = :ordNo "
					+ "AND I.ITEM_ID = :itemId ", params, new RowMapper<FodCreModVo>() {

						@Override
						public FodCreModVo mapRow(ResultSet rs, int rowNum) throws SQLException {
							FodCreModVo info = new FodCreModVo();
							info.setFulfillOrderId(rs.getLong(1));
							info.setNotes("API call from RSB server.");
							FodCreItmMod itmMod = new FodCreItmMod();
							itmMod.setFulfillOrderLineId(rs.getLong(2));
							itmMod.setQuantity(BigDecimal.valueOf(request.getQty()));
							info.getFodCreItmMod().add(itmMod);
							return info;
						}
					});
		} catch (Exception e) {
			throw new BaseException("Fulfilment order id not found for the order " + request.getOrderNo() + " and exteranl ref no " + request.getFulfilNo(), e);
		}
	}
}
