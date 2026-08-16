package com.extra.oms.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.extra.oms.core.bean.Warehouse;

@Repository
public class UtilDAO extends BaseDAO {

	public Map<Long, String> getStores() {
		return jdbcTemplate.query("SELECT STORE, STORE_NAME3 FROM STORE WHERE STORE_NAME10 NOT LIKE 'VS-%'", new ResultSetExtractor<Map<Long, String>>() {

			@Override
			public Map<Long, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, String> stores = new LinkedHashMap<Long, String>();
				while(rs.next()) {
					stores.put(rs.getLong(1), rs.getString(2));
				}
				return stores;
			}
		});
	}

	public List<Warehouse> getWareHouses() {
		return jdbcTemplate.query("SELECT WH, PHYSICAL_WH, WH_NAME FROM WH WHERE CHANNEL_ID IS NOT NULL ", new RowMapper<Warehouse>() {

			@Override
			public Warehouse mapRow(ResultSet rs, int rowNum) throws SQLException {
				Warehouse warehous = new Warehouse();
				warehous.setWh(rs.getLong(1));
				warehous.setPhysicalWH(rs.getLong(2));
				warehous.setName(rs.getString(3));
				return warehous;
			}
		});
	}

	public Map<Long, String> getSuppliers() {
		return jdbcTemplate.query("SELECT SUPPLIER, SUP_NAME FROM SUPS WHERE SUP_STATUS = 'A'", new ResultSetExtractor<Map<Long, String>>() {

			@Override
			public Map<Long, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, String> stores = new LinkedHashMap<Long, String>();
				while(rs.next()) {
					stores.put(rs.getLong(1), rs.getString(2));
				}
				return stores;
			}
		});
	}

	public Map<Long, String> getVirtualStores() {
		return jdbcTemplate.query("SELECT STORE, STORE_NAME3 FROM STORE WHERE STORE_NAME10 LIKE 'VS-%'", new ResultSetExtractor<Map<Long, String>>() {

			@Override
			public Map<Long, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, String> stores = new LinkedHashMap<Long, String>();
				while(rs.next()) {
					stores.put(rs.getLong(1), rs.getString(2));
				}
				return stores;
			}
		});
	}
}
