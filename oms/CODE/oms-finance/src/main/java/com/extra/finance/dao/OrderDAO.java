package com.extra.finance.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author aibrahim
 *
 */
@Repository
public class OrderDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void updateOrderStatus(List<String> ordNos) {
		jdbcTemplate.update("UPDATE OMS_CUST_ORD_HEAD SET CONTRACT_FLAG = 'Y' WHERE CUST_ORDER_NO IN (:orderNos) AND STATUS = 'S'", Collections.singletonMap("orderNos", ordNos));
	}

	public List<String> getContractOrders() {
		return jdbcTemplate.query("SELECT DISTINCT CUST_ORDER_NO FROM OMS_CUST_ORD_HEAD WHERE CONTRACT_FLAG = 'N'", new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}
}
